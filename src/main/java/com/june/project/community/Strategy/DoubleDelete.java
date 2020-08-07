package com.june.project.community.Strategy;

import com.june.project.community.model.Comment;
import com.june.project.community.service.CommentService;
import com.june.project.community.service.RedisLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 延君
 * @date 2020/8/7 - 20:37
 */
@Service("doubleDelete")
public class DoubleDelete implements Strategy {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisLikeService redisLikeService;

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
}
