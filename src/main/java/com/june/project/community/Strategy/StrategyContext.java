package com.june.project.community.Strategy;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 延君
 * @date 2020/8/7 - 21:09
 */
@Component
public class StrategyContext implements Strategy{
    private final Map<String, Strategy> strategyMap = new ConcurrentHashMap<>();

    private final String MODE = "fullUpdate";

    @Autowired
    public StrategyContext(Map<String, Strategy> strategyMap) {
        this.strategyMap.clear();
        this.strategyMap.putAll(strategyMap);
    }

    public Strategy strategySelect(String mode) {
        if(!StringUtils.isEmpty(mode)){
            return this.strategyMap.get(mode);
        }
        return null;
    }

    @Override
    public void incLikedCount(Long commentId) {
        this.strategySelect(MODE).incLikedCount(commentId);
    }

    @Override
    public void decLikedCount(Long commentId) {
        this.strategySelect(MODE).decLikedCount(commentId);
    }

    @Override
    public int getTotalLikeCount(Long commentId) {
        return this.strategySelect(MODE).getTotalLikeCount(commentId);
    }
}
