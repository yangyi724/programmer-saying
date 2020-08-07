package com.june.project.community.service;

import com.june.project.community.dto.LikedCountDTO;
import com.june.project.community.enums.LikedStatusEnum;
import com.june.project.community.enums.RedisKeyEnum;
import com.june.project.community.mapper.LikedInfoMapper;
import com.june.project.community.model.LikedInfo;
import com.june.project.community.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 延君
 * @date 2020/8/6 - 16:01
 */
@Service
public class RedisLikeService {

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 点赞，状态变为 1
     *
     * @param likedCommentId
     * @param likedUserId
     */
    public void saveLiked2Redis(Long likedCommentId, Long likedUserId) {
        String mapKey = RedisKeyEnum.COMMENT_LIKE_INFO_CODE.getKey() + likedCommentId + "_" + likedUserId;
        redisUtil.hset(RedisKeyEnum.COMMENT_LIKE_INFO_KEY.getKey(), mapKey, LikedStatusEnum.LIKE.getStatus());
    }

    /**
     * 取消点赞，状态变为 0
     *
     * @param likedCommentId
     * @param likedUserId
     */
//    public void unlikeFromRedis(Long likedCommentId, Long likedUserId) {
//        String mapKey = RedisKeyEnum.COMMENT_LIKE_INFO_CODE.getKey() + likedCommentId + "_" + likedUserId;
//        redisUtil.hset(RedisKeyEnum.COMMENT_LIKE_INFO_KEY.getKey(), mapKey, LikedStatusEnum.UNLIKE.getStatus());
//    }
//
//    /**
//     * 从Redis中获取一条点赞状态数据
//     *
//     * @param likedCommentId
//     * @param likedUserId
//     */
//    public Object getLikedStatusFromRedis(Long likedCommentId, Long likedUserId) {
//        String mapKey = RedisKeyEnum.COMMENT_LIKE_INFO_CODE.getKey() + likedCommentId + "_" + likedUserId;
//        return redisUtil.hget(RedisKeyEnum.COMMENT_LIKE_INFO_KEY.getKey(), mapKey);
//    }

    /**
     * 从Redis中删除一条点赞数据
     *
     * @param likedCommentId
     * @param likedUserId
     */
    public void deleteLikedFromRedis(Long likedCommentId, Long likedUserId) {
        String mapKey = RedisKeyEnum.COMMENT_LIKE_INFO_CODE.getKey() + likedCommentId + "_" + likedUserId;
        redisUtil.hdel(RedisKeyEnum.COMMENT_LIKE_INFO_KEY.getKey(), mapKey);
    }

    public boolean hasLikedInfoFromRedis(Long likedCommentId, Long likedUserId) {
        String mapKey = RedisKeyEnum.COMMENT_LIKE_INFO_CODE.getKey() + likedCommentId + "_" + likedUserId;
        return redisUtil.hHasKey(RedisKeyEnum.COMMENT_LIKE_INFO_KEY.getKey(), mapKey);
    }

    /**
     * @param likedCommentId
     * @return
     */
    public Object getLikedCountFromRedis(Long likedCommentId) {
        String mapKey = RedisKeyEnum.COMMENT_LIKE_COUNT_CODE.getKey() + likedCommentId;
        return redisUtil.hget(RedisKeyEnum.COMMENT_LIKE_COUNT_KEY.getKey(), mapKey);
    }

    /**
     * 保存点赞量到redis
     * @param likedCommentId
     * @param likedCount
     */
    public void saveLikedCount2Redis(Long likedCommentId, int likedCount) {
        String mapKey = RedisKeyEnum.COMMENT_LIKE_COUNT_CODE.getKey() + likedCommentId;
        redisUtil.hset(RedisKeyEnum.COMMENT_LIKE_COUNT_KEY.getKey(), mapKey, likedCount);
    }

    /**
     * 从redis中删除一条likeCount数据
     * @param likedCommentId
     */
    public void delLikedCountFromRedis(Long likedCommentId) {
        String mapKey = RedisKeyEnum.COMMENT_LIKE_COUNT_CODE.getKey() + likedCommentId;
        redisUtil.hdel(RedisKeyEnum.COMMENT_LIKE_COUNT_KEY.getKey(), mapKey);
    }

    /**
     * 获取Redis中所有的点赞信息
     *
     * @return
     */
    public List<LikedInfo> getLikedInfoFromRedis() {
        Map<Object, Object> likedInfoItems = redisUtil.hmget(RedisKeyEnum.COMMENT_LIKE_INFO_KEY.getKey());
        List<LikedInfo> list = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : likedInfoItems.entrySet()) {
            String key = (String) entry.getKey();
            // 分离出likedCommentId,likedUserId
            String[] split = key.split("_");
            Long likedCommentId = Long.parseLong(split[1]);
            Long likedUserId = Long.parseLong(split[2]);
            int status = (int) entry.getValue();

            LikedInfo likedInfo = new LikedInfo();
            likedInfo.setCommentId(likedCommentId);
            likedInfo.setUserId(likedUserId);
            likedInfo.setStatus(status);
            list.add(likedInfo);

            // redisUtil.hdel(RedisKeyEnum.COMMENT_LIKE_INFO_KEY.getKey(), key);
        }
        return list;
    }

    /**
     * 获取Redis中所有的点赞数量信息
     *
     * @return
     */
//    public List<LikedCountDTO> getLikedCountFromRedis() {
//        Map<Object, Object> likedCountItems = redisUtil.hmget(RedisKeyEnum.COMMENT_LIKE_COUNT_KEY.getKey());
//        List<LikedCountDTO> list = new ArrayList<>();
//        for (Map.Entry<Object, Object> entry : likedCountItems.entrySet()) {
//            String key = (String) entry.getKey();
//            // 分离出likedCommentId
//            String[] split = key.split("_");
//            Long likedCommentId = Long.parseLong(split[1]);
//            int count = (int) entry.getValue();
//
//            LikedCountDTO likedCountDTO = new LikedCountDTO();
//            likedCountDTO.setCommentId(likedCommentId);
//            likedCountDTO.setLikeCount(count);
//            list.add(likedCountDTO);
//
//            // redisUtil.hdel(RedisKeyEnum.COMMENT_LIKE_COUNT_KEY.getKey(), key);
//        }
//        return list;
//    }
}
