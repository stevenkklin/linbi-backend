package com.yupi.springbootinit.model.vo;

import lombok.Data;

/**
 * Bi 返回结果
 * @author Steven
 * @create 2023-07-04-16:52
 */
@Data
public class BiResponse {

    private String genChart;

    private String genResult;

    private Long ChartId;
}
