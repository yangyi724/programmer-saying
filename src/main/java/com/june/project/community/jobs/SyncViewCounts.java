package com.june.project.community.jobs;

import com.june.project.community.consts.RedisKey;
import com.june.project.community.mapper.QuestionMapper;
import com.june.project.community.service.QuestionService;
import com.june.project.community.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 延君
 * @date 2020/8/5 - 21:58
 */
@Component
public class SyncViewCounts {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private QuestionService questionService;

    @Scheduled(cron = "0 0/1 * * * ? ")//每1分钟
    public void SyncNodesAndShips() {
        logger.info("开始保存点赞数 、浏览数");
        try {
            //先获取这段时间的浏览数
            Map<Object,Object> viewCountItem=redisUtil.hmget(RedisKey.ARTICLE_VIEWCOUNT_KEY);
            //然后删除redis里这段时间的浏览数
            redisUtil.remove(RedisKey.ARTICLE_VIEWCOUNT_KEY);
            if(!viewCountItem.isEmpty()){
                for(Object item :viewCountItem.keySet()){
                    String questionKey=item.toString();//viewcount_1
                    String[]  kv=questionKey.split("_");
                    Long questionId=Long.parseLong(kv[1]);
                    Integer viewCount=(Integer) viewCountItem.get(questionKey);
                    System.out.println("问题id:"+questionId+"浏览量:"+viewCount);
                    //更新到数据库
                    questionService.incMoreView(questionId, viewCount);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("结束保存点赞数 、浏览数");
    }
}