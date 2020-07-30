package com.june.project.community.controller;

import com.june.project.community.dto.PaginationDTO;
import com.june.project.community.mapper.UserMapper;
import com.june.project.community.model.User;
import com.june.project.community.service.QuestionService;
import jdk.nashorn.internal.ir.FunctionNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author June
 * @date 2020/7/2 - 10:21
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;


    // 希望调用 profile.html 的时候访问 ProfileController
    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,      // 分页 1.：page 分页的页码， size 分页数
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");

        if(user == null) {
            return "redirect:/";
        }

        if("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        } else if("replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination", paginationDTO);  // 引号中的是前端的变量名
        // 表示返回到 profile.html
        return "profile";
    }
}
