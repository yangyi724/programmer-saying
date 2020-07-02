package com.june.project.community.service;

import com.june.project.community.dto.PaginationDTO;
import com.june.project.community.dto.QuestionDTO;
import com.june.project.community.mapper.QuestionMapper;
import com.june.project.community.mapper.UserMapper;
import com.june.project.community.model.Question;
import com.june.project.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author June
 * @date 2020/7/1 - 20:10
 * 中间层
 * 比如：QuestionService 的产生是因为需要组装 QuestionMapper 和 UserMapper
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count(); // 分页 3. ：通过 mapper 数据库查询到 totalCount 记录的总数
        paginationDTO.setPagination(totalCount, page, size); // 分页 4. ：通过 totalCount, page, size 算出分页需要的其他数据，如是否展示上一页，下一页，首尾页等

        // 校验页码
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }

        // offset : size*(page-1)
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
}
