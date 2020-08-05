package com.june.project.community.controller;

import com.june.project.community.consts.RedisKey;
import com.june.project.community.dto.CommentCreateDTO;
import com.june.project.community.dto.CommentDTO;
import com.june.project.community.dto.QuestionDTO;
import com.june.project.community.enums.CommentTypeEnum;
import com.june.project.community.service.CommentService;
import com.june.project.community.service.QuestionService;
import com.june.project.community.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * @author June
 * @date 2020/7/2 - 16:06
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisUtil redisUtil;

    // 从"question/{id}"的 id 拿到数据存储在 Integer id 中，这是对应于数据库中 question 的 id
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") String id, // 接收请求路径中占位符的值
                           Model model) {
        Long questionId = null;
        questionId = Long.parseLong(id);

        // 累加阅读数，累加的量先存入 redis，以后再定时写入数据库，减少对数据库频繁的写操作
        // questionService.incView(questionId);

        // redis 记录累加阅读数
        String key= RedisKey.ARTICLE_VIEWCOUNT_CODE+questionId; // viewCount_x
        //找到redis中该问题的浏览数，如果不存在则向redis中添加一条
        Map<Object, Object> viewCountItem = redisUtil.hmget(RedisKey.ARTICLE_VIEWCOUNT_KEY);
        Integer viewCount = 0;
        if (!viewCountItem.isEmpty()) {
            if (viewCountItem.containsKey(key)) {
                viewCount = (Integer) viewCountItem.get(key);
                redisUtil.hset(RedisKey.ARTICLE_VIEWCOUNT_KEY, key, viewCount + 1);
                viewCount = viewCount + 1;
            } else {
                redisUtil.hset(RedisKey.ARTICLE_VIEWCOUNT_KEY, key, 1);
            }
        } else {
            redisUtil.hset(RedisKey.ARTICLE_VIEWCOUNT_KEY, key, 1);
        }

        QuestionDTO questionDTO = questionService.getById(questionId);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(questionId, CommentTypeEnum.QUESTION);
        int totalViewCount = viewCount + questionDTO.getViewCount();
        model.addAttribute("viewCount", totalViewCount);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }


}
