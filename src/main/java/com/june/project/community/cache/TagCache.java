package com.june.project.community.cache;

import com.june.project.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 延君
 * @date 2020/8/3 - 20:21
 */
public class TagCache {
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("Java基础");
        program.setTags(Arrays.asList("Java基本数据类型", "Java容器", "字符串", "反射", "异常", "接口和抽象类", "关键字", "特性", "泛型", "Object通用方法", "继承", "lambda表达式"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("虚拟机");
        framework.setTags(Arrays.asList("内存区域", "垃圾收集器", "内存分配策略", "Class类文件结构", "虚拟机类加载机制"));
        tagDTOS.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("并发");
        server.setTags(Arrays.asList("线程状态", "锁", "线程池"));
        tagDTOS.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("计算机基础");
        db.setTags(Arrays.asList("MySql", "Redis", "网络", "操作系统", "设计模式"));
        tagDTOS.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("框架");
        tool.setTags(Arrays.asList("Spring", "SpringMVC", "Spring Boot", "MyBatis"));
        tagDTOS.add(tool);

        TagDTO daily = new TagDTO();
        daily.setCategoryName("记录");
        daily.setTags(Arrays.asList("求职", "生活"));
        tagDTOS.add(daily);

        return tagDTOS;
    }

    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
