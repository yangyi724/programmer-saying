package com.june.project.community.controller;

import com.june.project.community.Strategy.StrategyContext;
import com.june.project.community.dto.ResultDTO;
import com.june.project.community.exception.CustomizeErrorCode;
import com.june.project.community.model.User;
import com.june.project.community.service.LikeService;
import com.june.project.community.service.RedisLikeService;
import com.june.project.community.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 延君
 * @date 2020/8/6 - 17:59
 */
@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private StrategyContext strategyContext;

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String  like(Long commentId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return JsonUtil.getJSONString(-1, CustomizeErrorCode.NO_LOGIN.getMessage());
        }
        Long userId = user.getId();
        likeService.like(commentId, userId);
        int likeCount = strategyContext.getTotalLikeCount(commentId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        return JsonUtil.getJSONString(0, null, map);
    }

}
