package com.june.project.community.controller;

import com.june.project.community.dto.PaginationDTO;
import com.june.project.community.dto.QuestionDTO;
import com.june.project.community.mapper.QuestionMapper;
import com.june.project.community.mapper.UserMapper;
import com.june.project.community.model.Question;
import com.june.project.community.model.User;
import com.june.project.community.service.QuestionService;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author June
 * @date 2020/6/29 - 13:24
 */
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        // 分页 1.：page 分页的页码， size 分页数
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {
        // 分页 2.：把两个参数传入 Service
        PaginationDTO pagination = questionService.list(page, size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
