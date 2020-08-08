package com.june.project.community.Strategy;

import com.june.project.community.model.Comment;
import com.june.project.community.service.CommentService;
import com.june.project.community.service.LikeService;
import com.june.project.community.service.RedisLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 延君
 * @date 2020/8/7 - 20:37
 */
@Service(value = "doubleDelete")
public class DoubleDelete implements Strategy {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisLikeService redisLikeService;

    @Autowired
    private LikeService likeService;

    @Override
    public void incLikedCount(Long commentId) {
        // DB加一
        commentService.incLikeById(commentId);
        // 删除缓存
        redisLikeService.delLikedCountFromRedis(commentId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 再次删除
        redisLikeService.delLikedCountFromRedis(commentId);
    }

    @Override
    public void decLikedCount(Long commentId) {
        // DB减一
        commentService.decLikeById(commentId);
        // 删除缓存
        redisLikeService.delLikedCountFromRedis(commentId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 再次删除
        redisLikeService.delLikedCountFromRedis(commentId);
    }

    @Override
    public int getTotalLikeCount(Long commentId) {
        return likeService.getTotalLikeCount(commentId);
    }
}
