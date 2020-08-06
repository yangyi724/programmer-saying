package com.june.project.community.service;

import com.june.project.community.dto.LikedCountDTO;
import com.june.project.community.enums.LikedStatusEnum;
import com.june.project.community.mapper.LikedInfoMapper;
import com.june.project.community.model.Comment;
import com.june.project.community.model.LikedInfo;
import com.june.project.community.model.LikedInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 延君
 * @date 2020/8/6 - 19:27
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisLikeService redisLikeService;

    @Autowired
    private LikedInfoMapper likedInfoMapper;

    @Autowired
    private CommentService commentService;

    /**
     * 点赞或取消点赞
     * @param commentId
     * @param userId
     */
    public void like(Long commentId, Long userId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                boolean exists = redisLikeService.hasLikedInfoFromRedis(commentId, userId);
                redisOperations.multi();
                if(exists) {
                    redisLikeService.deleteLikedFromRedis(commentId, userId);
                    redisLikeService.decrementLikedCount(commentId);
                } else {
                    redisLikeService.saveLiked2Redis(commentId, userId);
                    redisLikeService.incrementLikedCount(commentId);
                }
                redisOperations.exec();
                return null;
            }
        });
    }

    /**
     * 获取点赞数量
     * @param commentId
     * @return
     */
    public int getTotalLikeCount(Long commentId) {
        return redisLikeService.getLikedCountFromRedis(commentId);
    }

    /**
     * 判断是否已经点赞
     * @param commentId
     * @param userId
     * @return
     */
    public int getLikeStatus(Long commentId, Long userId) {
        return redisLikeService.hasLikedInfoFromRedis(commentId, userId) ? LikedStatusEnum.LIKE.getStatus() :
                LikedStatusEnum.UNLIKE.getStatus();
    }

    public LikedInfo getByCommentIdAndUserId(Long commentId, Long userId) {
        LikedInfoExample likedInfoExample = new LikedInfoExample();
        likedInfoExample.createCriteria()
                .andCommentIdEqualTo(commentId)
                .andUserIdEqualTo(userId);
        List<LikedInfo> likedInfos = likedInfoMapper.selectByExample(likedInfoExample);
        if(likedInfos.isEmpty())
            return null;
        return likedInfos.get(0);
    }

    /**
     * 新插入一条信息
     * @param likedInfo
     */
    public void save(LikedInfo likedInfo) {
        likedInfoMapper.insert(likedInfo);
    }

    /**
     * 更新一条信息
     * @param likedInfo
     */
    public void update(LikedInfo likedInfo) {
        LikedInfoExample likedInfoExample = new LikedInfoExample();
        likedInfoExample.createCriteria()
                .andCommentIdEqualTo(likedInfo.getCommentId())
                .andUserIdEqualTo(likedInfo.getUserId());
        likedInfoMapper.updateByExampleSelective(likedInfo, likedInfoExample);
    }

    /**
     * 将Redis里的点赞信息数据保存到数据库中
     */
    @Transactional
    public void transLikedInfoFromRedis2DB() {
        List<LikedInfo> likedInfoFromRedis = redisLikeService.getLikedInfoFromRedis();
        for (LikedInfo likedInfo : likedInfoFromRedis) {
            LikedInfo likedInfo1 = getByCommentIdAndUserId(likedInfo.getCommentId(), likedInfo.getUserId());
            if(likedInfo1 == null) {
                // 没有记录，插入
                save(likedInfo);
            } else {
                // 有记录，更新
                likedInfo1.setStatus(likedInfo.getStatus());
                update(likedInfo1);
            }
        }
    }

    /**
     * 将Redis中的点赞数量存入数据库
     */
    @Transactional
    public void transLikedCountFromRedis2DB() {
        List<LikedCountDTO> likedCountDTOS = redisLikeService.getLikedCountFromRedis();
        for (LikedCountDTO likedCountDTO : likedCountDTOS) {
            Comment comment = commentService.getByCommentId(likedCountDTO.getCommentId());
            if(comment != null) {
                commentService.updateCountById(likedCountDTO.getCommentId(), likedCountDTO.getLikeCount());
            }
        }
    }
}
