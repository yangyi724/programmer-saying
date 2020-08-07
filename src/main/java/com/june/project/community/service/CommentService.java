package com.june.project.community.service;

import com.june.project.community.dto.CommentCreateDTO;
import com.june.project.community.dto.CommentDTO;
import com.june.project.community.enums.CommentTypeEnum;
import com.june.project.community.enums.NotificationStatusEnum;
import com.june.project.community.enums.NotificationTypeEnum;
import com.june.project.community.exception.CustomizeErrorCode;
import com.june.project.community.exception.CustomizeException;
import com.june.project.community.exception.ICustomizeErrorCode;
import com.june.project.community.mapper.*;
import com.june.project.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author June
 * @date 2020/7/4 - 18:48
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        // 未选中问题，当前评论可能被删除
        if(comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARAGET_PARAM_NOT_FOUND);
        }
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if(comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            // 查找评论的问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
               throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            // 创建通知
            createNotify(commentator, dbComment.getCommentator(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        } else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            createNotify(commentator, question.getCreator(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    private void createNotify(User commentator, Long receiver, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if (receiver == commentator.getId()) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(commentator.getId());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(commentator.getName());
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 获取去重的评论人的 ID
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);

        // 获取评论人并转换为 Map，Map是从评论人的 id 路由到 user对象
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }

    /**
     * 按评论id查找评论
     * @param commentId
     * @return
     */
    public Comment getByCommentId(Long commentId) {
        return commentMapper.selectByPrimaryKey(commentId);
    }

    /**
     * 根据评论id更新评论的点赞数
     * @param commentId
     * @param likeCount
     */
    public void updateCountById(Long commentId, Integer likeCount) {
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setLikeCount((long) likeCount);
        commentMapper.updateByPrimaryKeySelective(comment);
    }

    public void incLikeById(Long commentId) {
        Comment comment = getByCommentId(commentId);
        updateCountById(commentId, Integer.parseInt(String.valueOf(comment.getLikeCount())) + 1);
    }

    public void decLikeById(Long commentId) {
        Comment comment = getByCommentId(commentId);
        updateCountById(commentId, Integer.parseInt(String.valueOf(comment.getLikeCount())) - 1);
    }
}
