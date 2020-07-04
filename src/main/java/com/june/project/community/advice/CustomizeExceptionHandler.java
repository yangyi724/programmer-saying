package com.june.project.community.advice;

import com.june.project.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author June
 * @date 2020/7/2 - 23:01
 */
@ControllerAdvice
public class CustomizeExceptionHandler { // 只能拦截所有mvc可以handle的异常，不能handle的异常怎么办，那么需要做一个通用的controller处理
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model) { // ModelAndView表示渲染后的页面，和Controller里面的return "index"返回的是一样的
        if(e instanceof CustomizeException) {
            model.addAttribute("message", e.getMessage());
        } else {
            model.addAttribute("message", "服务冒烟了，要不然你稍后再试试！");
        }
        return new ModelAndView("error"); // 这里返回 error.html
    }
}
