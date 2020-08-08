package com.june.project.community.controller;

import com.june.project.community.Strategy.StrategyContext;
import com.june.project.community.dto.CommentDTO;
import com.june.project.community.dto.LikeAndCommentDTO;
import com.june.project.community.dto.QuestionDTO;
import com.june.project.community.enums.CommentTypeEnum;
import com.june.project.community.enums.RedisKeyEnum;
import com.june.project.community.service.CommentService;
import com.june.project.community.service.LikeService;
import com.june.project.community.service.QuestionService;
import com.june.project.community.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
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
    private LikeService likeService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StrategyContext strategyContext;


    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") String id,
                           Model model) {
        Long questionId = null;
        questionId = Long.parseLong(id);

        // questionService.incView(questionId);

        // redis 记录累加阅读数 viewCount_x
        String key= RedisKeyEnum.QUESTION_VIEW_COUNT_CODE.getKey() + questionId;

        Map<Object, Object> viewCountItem = redisUtil.hmget(RedisKeyEnum.QUESTION_VIEW_COUNT_KEY.getKey());
        Integer viewCount = 0;
        if (!viewCountItem.isEmpty()) {
            if (viewCountItem.containsKey(key)) {
                // 若redis中有该问题的浏览量，+1
                viewCount = (Integer) viewCountItem.get(key);
                redisUtil.hset(RedisKeyEnum.QUESTION_VIEW_COUNT_KEY.getKey(), key, viewCount + 1);
                viewCount = viewCount + 1;
            } else {
                // 若redis中没有该问题的浏览量，创建
                redisUtil.hset(RedisKeyEnum.QUESTION_VIEW_COUNT_KEY.getKey(), key, 1);
                viewCount = 1;
            }
        } else {
            // 若redis中没有该问题的浏览量，创建
            redisUtil.hset(RedisKeyEnum.QUESTION_VIEW_COUNT_KEY.getKey(), key, 1);
            viewCount = 1;
        }

        QuestionDTO questionDTO = questionService.getById(questionId);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(questionId, CommentTypeEnum.QUESTION);


        ArrayList<LikeAndCommentDTO> likeAndCommentDTOS = new ArrayList<>();
        for (CommentDTO commentDTO : comments) {
            LikeAndCommentDTO likeAndCommentDTO = new LikeAndCommentDTO();
            Long commentId = commentDTO.getId();
            // 从 redis 中读数据
            int likeCount = strategyContext.getTotalLikeCount(commentId);
            likeAndCommentDTO.setCommentDTO(commentDTO);
            likeAndCommentDTO.setLikeCount(likeCount);
            likeAndCommentDTOS.add(likeAndCommentDTO);
        }

        model.addAttribute("viewCount", viewCount);
        model.addAttribute("question", questionDTO);
        model.addAttribute("commentsAndLikeCounts", likeAndCommentDTOS);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }


}
