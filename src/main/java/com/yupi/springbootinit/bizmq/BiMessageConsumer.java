package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.utils.ExcelUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.yupi.springbootinit.constant.ChartStatusConstant.*;
import static com.yupi.springbootinit.constant.ModelIdConstant.BI_MODEL_ID;

/**
 * @author Steven
 * @create 2023-07-13-9:23
 */
@Component
@Slf4j
public class BiMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);

        try {
            if (StringUtils.isBlank(message)) {
                // 如果失败消息拒绝
                channel.basicNack(deliveryTag, false, false);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
            }
            long chartId = Long.parseLong(message);
            Chart chart = chartService.getById(chartId);
            if (chart == null) {
                channel.basicNack(deliveryTag, false, false);
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
            }
            // 先修改图表任务状态为“执行中”。等执行成功后，修改为“已完成”，保存执行结果：执行失败后，状态修改为“失败”，记录任务失败信息。
            Chart updateChart = new Chart();
            updateChart.setId(chart.getId());
            updateChart.setStatus(CHART_GEN_RUNNING);
            boolean b = chartService.updateById(updateChart);
            if (!b) {
                channel.basicNack(deliveryTag, false, false);
                handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
                return;
            }
            // 调用AI
            String result = aiManager.doChat(BI_MODEL_ID, buildUserInput(chart));
            String[] split = result.split("【【【【【");
            if (split.length < 3) {
                // 如果失败消息拒绝
                channel.basicNack(deliveryTag, false, true);
                handleChartUpdateError(chart.getId(), "抱歉你的运气有点不好~, AI 生成错误,正在重新生成");
                return;
            }
            String genChart = split[1].trim();
            String genResult = split[2].trim();
            // 调用成功更改任务状态
            Chart updateChartResult = new Chart();
            updateChartResult.setId(chart.getId());
            updateChartResult.setStatus(CHART_GEN_SUCCEED);
            updateChartResult.setGenChart(genChart);
            updateChartResult.setGenResult(genResult);
            boolean updateResult = chartService.updateById(updateChartResult);
            if (!updateResult) {
                channel.basicNack(deliveryTag, false, false);
                handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
            }
        } catch (BusinessException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "任务队列已满请稍后重试");
        }

        // 消息确认
        channel.basicAck(deliveryTag, false);
    }

    /**
     * 构造用户输入
     *
     * @param chart
     * @return
     */
    private String buildUserInput(Chart chart) {
        String goal = chart.getGoal();
        String chartType = chart.getCharType();
        String csvData = chart.getChartData();

        // 用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += ", 请使用" + chartType;
        }

        userInput.append("分析目标：").append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");


        userInput.append(csvData).append("\n");

        return userInput.toString();
    }

    private void handleChartUpdateError(long charId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(charId);
        updateChartResult.setStatus(CHART_GEN_FAILED);
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + charId + "," + execMessage);
        }
    }

}
