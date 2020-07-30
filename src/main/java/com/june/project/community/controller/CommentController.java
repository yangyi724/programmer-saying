package com.june.project.community.controller;

import com.june.project.community.dto.CommentDTO;
import com.june.project.community.dto.ResultDTO;
import com.june.project.community.exception.CustomizeErrorCode;
import com.june.project.community.mapper.CommentMapper;
import com.june.project.community.model.Comment;
import com.june.project.community.model.User;
import com.june.project.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author June
 * @date 2020/7/4 - 17:11
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST) // 从前端拿到一个JSON，然后服务端反序列化成一个对象
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }
}
