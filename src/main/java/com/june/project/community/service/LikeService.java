package com.june.project.community.service;

import com.june.project.community.Strategy.StrategyContext;
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

    @Autowired
    private StrategyContext strategyContext;

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
                    // 如果redis中存在点赞信息，那么取消点赞
                    redisLikeService.deleteLikedFromRedis(commentId, userId);
                    strategyContext.decLikedCount(commentId);
                    // decLikedCount(commentId);
                } else {
                    // 如果redis中不存在点赞信息，那么点赞
                    redisLikeService.saveLiked2Redis(commentId, userId);
                    strategyContext.incLikedCount(commentId);
                    // incLikedCount(commentId);
                }
                redisOperations.exec();
                return null;
            }
        });
    }


    /**
     *  该回复的点赞数加一
     *  更新操作，先更新DB，然后删除缓存
     * @param commentId
     */
    public void incLikedCount(Long commentId) {
        // DB加一
        commentService.incLikeById(commentId);
        // 删除缓存
        redisLikeService.delLikedCountFromRedis(commentId);
    }

    /**
     *  该回复的点赞数减一
     *  更新操作，先更新DB，然后删除缓存
     * @param commentId
     */
    public void decLikedCount(Long commentId) {
        // DB减一
        commentService.decLikeById(commentId);
        // 删除缓存
        redisLikeService.delLikedCountFromRedis(commentId);
    }


    /**
     * 获取点赞数量
     * 读操作
     * @param commentId
     * @return
     */
    public int getTotalLikeCount(Long commentId) {
        Object retFromRedis = redisLikeService.getLikedCountFromRedis(commentId);
        if(retFromRedis != null) {
            // 从redis读不为空，直接返回
            return (int) retFromRedis;
        } else {
            // 从redis读为空，从数据库读
            Comment comment = commentService.getByCommentId(commentId);
            // 从DB读为空，直接返回
            if(comment == null) {
                return 0;
            }
            Long likeCount = comment.getLikeCount();
            // 从DB读为空，直接返回
            if(likeCount == null)
                return 0;
            // 从DB读不为空，先写入缓存，再返回
            redisLikeService.saveLikedCount2Redis(commentId, Integer.parseInt(String.valueOf(likeCount)));
            return Integer.parseInt(String.valueOf(likeCount));
        }
    }

    /**
     * 判断是否已经点赞
     * @param commentId
     * @param userId
     * @return
     */
//    public int getLikeStatus(Long commentId, Long userId) {
//        return redisLikeService.hasLikedInfoFromRedis(commentId, userId) ? LikedStatusEnum.LIKE.getStatus() :
//                LikedStatusEnum.UNLIKE.getStatus();
//    }

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
//    @Transactional
//    public void transLikedCountFromRedis2DB() {
//        List<LikedCountDTO> likedCountDTOS = redisLikeService.getLikedCountFromRedis();
//        for (LikedCountDTO likedCountDTO : likedCountDTOS) {
//            Comment comment = commentService.getByCommentId(likedCountDTO.getCommentId());
//            if(comment != null) {
//                commentService.updateCountById(likedCountDTO.getCommentId(), likedCountDTO.getLikeCount());
//            }
//        }
//    }
}
