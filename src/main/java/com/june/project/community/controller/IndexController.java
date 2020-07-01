package com.june.project.community.controller;

import com.june.project.community.mapper.UserMapper;
import com.june.project.community.model.User;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author June
 * @date 2020/6/29 - 13:24
 */
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    /*
    * 每次回到主页的时候都在reuqest(request是浏览器发送到服务端的)中取得名为"token"的cookie，这个cookie是我们在AuthorizeController
    * 中利用response添加的。如果找到这个cookie再从数据库中找有没有和这个cookie(含义就是token)的value相同的user，如果有，说明
    * 该用户登录过，我们就直接在request中set用户，然后在前端判断user是否存在从而显示登录后的界面
     */
    @GetMapping("/")
    public String index(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if(user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        return "index";
    }
}
