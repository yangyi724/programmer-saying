package com.june.project.community.Strategy;

/**
 * @author 延君
 * @date 2020/8/7 - 20:33
 */
public interface Strategy {

    void incLikedCount(Long commentId);

    void decLikedCount(Long commentId);

    int getTotalLikeCount(Long commentId);
}
