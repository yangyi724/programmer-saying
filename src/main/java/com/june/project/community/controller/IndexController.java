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

    /*
    * 每次回到主页的时候都在reuqest(request是浏览器发送到服务端的)中取得名为"token"的cookie，这个cookie是我们在AuthorizeController
    * 中利用response添加的。如果找到这个cookie再从数据库中找有没有和这个cookie(含义就是token)的value相同的user，如果有，说明
    * 该用户登录过，我们就直接在request中set用户，然后在前端判断user是否存在从而显示登录后的界面
     */
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,      // 分页 1.：page 分页的页码， size 分页数
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {  // @RequestParam 注入 Model，因为要把数据传到前端，具体数据是 question 的列表，这里是接收前端传来的 page 和 size 数据
        // 首页加载：先从数据库中看寻找有没有和传到服务器端 cookie 相对应的 user 信息，若有，把该用户信息传回浏览器，表示已登录

        PaginationDTO pagination = questionService.list(page, size); // 分页 2.：把两个参数传入 Service
        model.addAttribute("pagination", pagination);  // 引号中的是前端的变量名
        // 在跳转到 index.html 之前把所需的数据放进去，如 question 列表
        return "index";
    }
}
