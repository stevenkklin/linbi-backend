package com.yupi.springbootinit.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Steven
 * @create 2023-07-06-9:50
 */
@SpringBootTest
class ChartMapperTest {

    @Resource
    ChartMapper chartMapper;

    @Test
    void queryChartData() {
        String chartId = "1676267604667355137";
        String querySql = String.format("select * from chart_%s", chartId);
        List<Map<String, Object>> resultMap = chartMapper.queryChartData(querySql);
        System.out.println(resultMap);
    }
}