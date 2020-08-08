package com.june.project.community.Strategy;

import com.june.project.community.enums.RedisKeyEnum;
import com.june.project.community.model.Comment;
import com.june.project.community.service.CommentService;
import com.june.project.community.service.LikeService;
import com.june.project.community.service.RedisLikeService;
import com.june.project.community.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 延君
 * @date 2020/8/7 - 22:23
 */
@Service(value = "fullUpdate")
public class FullUpdate implements Strategy {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LikeService likeService;

    @Override
    public void incLikedCount(Long commentId) {
        likeService.incLikedCount(commentId);
    }

    @Override
    public void decLikedCount(Long commentId) {
        likeService.decLikedCount(commentId);
    }

    @Override
    public int getTotalLikeCount(Long commentId) {
        if (!redisUtil.hasKey(RedisKeyEnum.COMMENT_LIKE_COUNT_KEY.getKey())) {
            // 若 key 不存在，则代表是第一次设置，设置过期时间为 300s
            int ret = likeService.getTotalLikeCount(commentId);
            redisUtil.expire(RedisKeyEnum.COMMENT_LIKE_COUNT_KEY.getKey(), 300L);
            return ret;
        }
        return likeService.getTotalLikeCount(commentId);
    }
}
