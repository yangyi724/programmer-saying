package com.june.project.community.schedule;

import com.june.project.community.service.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author 延君
 * @date 2020/8/6 - 21:01
 */
@Component
public class SyncLike {

    @Autowired
    private LikeService likeService;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0/15 * * * * ? ") //每15秒
    public void SyncNodesAndShips() {
        logger.info("开始保存点赞信息");
        try {
            // likeService.transLikedCountFromRedis2DB();
            likeService.transLikedInfoFromRedis2DB();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("结束保存点赞信息");
    }
}
