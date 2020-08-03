package com.june.project.community.controller;

import com.june.project.community.dto.CommentCreateDTO;
import com.june.project.community.dto.CommentDTO;
import com.june.project.community.dto.QuestionDTO;
import com.june.project.community.enums.CommentTypeEnum;
import com.june.project.community.service.CommentService;
import com.june.project.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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

    // 从"question/{id}"的 id 拿到数据存储在 Integer id 中，这是对应于数据库中 question 的 id
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") String id, // 接收请求路径中占位符的值
                           Model model) {
        Long questionId = null;
        questionId = Long.parseLong(id);
        QuestionDTO questionDTO = questionService.getById(questionId);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(questionId, CommentTypeEnum.QUESTION);

        // 累加阅读数
        questionService.incView(questionId);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }


}
