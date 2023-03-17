package com.atguigu.gmall.common.config;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.model.GmallCorrelationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description 消息发送确认
 * <p>
 * ConfirmCallback  只确认消息是否正确到达 Exchange 中
 * ReturnCallback   消息没有正确到达队列时触发回调，如果正确到达队列不执行
 * <p>
 * 1. 如果消息没有到exchange,则confirm回调,ack=false
 * 2. 如果消息到达exchange,则confirm回调,ack=true
 * 3. exchange到queue成功,则不回调return
 * 4. exchange到queue失败,则回调return
 * 
 */
@Component
@Slf4j
public class MQProducerAckConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    // 修饰一个非静态的void（）方法,在服务器加载Servlet的时候运行，并且只会被服务器执行一次在构造函数之后执行，init（）方法之前执行。
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);            //指定 ConfirmCallback
        rabbitTemplate.setReturnCallback(this);             //指定 ReturnCallback
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //  ack = true 说明消息正确发送到了交换机
        if (ack){
            System.out.println("哥们你来了.");
            log.info("消息发送到了交换机");
        }else {
            //  消息没有到交换机
            log.info("消息没发送到交换机");
            //  调用重试发送方法
            this.retrySendMsg(correlationData);
        }
    }

    @Override
    public void returnedMessage(Message message, int code, String codeText, String exchange, String routingKey) {
        System.out.println("消息主体: " + new String(message.getBody()));
        System.out.println("应答码: " + code);
        System.out.println("描述：" + codeText);
        System.out.println("消息使用的交换器 exchange : " + exchange);
        System.out.println("消息使用的路由键 routing : " + routingKey);

        //  获取这个CorrelationData对象的Id  spring_returned_message_correlation
        String correlationDataId = (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
        //  因为在发送消息的时候，已经将数据存储到缓存，通过 correlationDataId 来获取缓存的数据
        String strJson = (String) redisTemplate.opsForValue().get(correlationDataId);
        //  消息没有到队列的时候，则会调用重试发送方法
        GmallCorrelationData gmallCorrelationData = JSON.parseObject(strJson,GmallCorrelationData.class);
        //  调用方法  gmallCorrelationData 这对象中，至少的有，交换机，路由键，消息等内容.
        this.retrySendMsg(gmallCorrelationData);
    }

    /**
     * 重试发送方法
     * @param correlationData   父类对象  它下面还有个子类对象 GmallCorrelationData
     */
    private void retrySendMsg(CorrelationData correlationData) {
        //  数据类型转换  统一转换为子类处理
        GmallCorrelationData gmallCorrelationData = (GmallCorrelationData) correlationData;
        //  获取到重试次数 初始值 0
        int retryCount = gmallCorrelationData.getRetryCount();
        //  判断
        if (retryCount>=3){
            //  不需要重试了
            log.error("重试次数已到，发送消息失败:"+JSON.toJSONString(gmallCorrelationData));
        } else {
            //  变量更新
            retryCount+=1;
            //  重新赋值重试次数 第一次重试 0->1 1->2 2->3
            gmallCorrelationData.setRetryCount(retryCount);
            //更新redis
            redisTemplate.opsForValue().set(gmallCorrelationData.getId(), JSON.toJSONString(gmallCorrelationData));
            System.out.println("重试次数：\t"+retryCount);

            //  判断是否属于延迟消息
            if (gmallCorrelationData.isDelay()){
                //  属于延迟消息
                this.rabbitTemplate.convertAndSend(gmallCorrelationData.getExchange(),gmallCorrelationData.getRoutingKey(),gmallCorrelationData.getMessage(),message -> {
                    //  设置延迟时间
                    message.getMessageProperties().setDelay(gmallCorrelationData.getDelayTime()*1000);
                    return message;
                },gmallCorrelationData);
            }else {
                //  调用发送消息方法 表示发送普通消息  发送消息的时候，不能调用 new RabbitService().sendMsg() 这个方法
                this.rabbitTemplate.convertAndSend(gmallCorrelationData.getExchange(),gmallCorrelationData.getRoutingKey(),gmallCorrelationData.getMessage(),gmallCorrelationData);
            }

        }
    }


}
