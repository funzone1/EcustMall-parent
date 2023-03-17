package com.atguigu.gmall.mq.receiver;


import com.atguigu.gmall.mq.config.DelayedMqConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class DelayReceiver {
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = DelayedMqConfig.queue_delay_1)
    public void get(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Receive queue_delay_1: " + sdf.format(new Date()) + " Delay rece." + msg);
    }

    @SneakyThrows
    @RabbitListener(queues = DelayedMqConfig.queue_delay_1)
    public void getMsg2(String msg, Message message, Channel channel){

        //  使用setnx 命令来解决 msgKey = delay:iuok
        String msgKey = "delay:"+msg;
        Boolean result = this.redisTemplate.opsForValue().setIfAbsent(msgKey, "0", 10, TimeUnit.MINUTES);
        //  result = true : 说明执行成功，redis 里面没有这个key ，第一次创建， 第一次消费。
        //  result = false : 说明执行失败，redis 里面有这个key
        //  不能： 那么就表示这个消息只能被消费一次！  那么第一次消费成功或失败，我们确定不了！  --- 只能被消费一次！
        //        if (result){
        //            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //            System.out.println("接收时间："+simpleDateFormat.format(new Date()));
        //            System.out.println("接收的消息："+msg);
        //            //  手动确认消息
        //            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        //        } else {
        //          //    不能消费！
        //        }
        //  能： 保证消息被消费成功    第二次消费，可以进来，但是要判断上一个消费者，是否将消息消费了。如果消费了，则直接返回，如果没有消费成功，我消费。
        //  在设置key 的时候给了一个默认值 0 ，如果消费成功，则将key的值 改为1
        if (!result){
            //  获取缓存key对应的数据
            String status = (String) this.redisTemplate.opsForValue().get(msgKey);
            if ("1".equals(status)){
                //  手动确认
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            } else {
                //  说明第一个消费者没有消费成功，所以消费并确认
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("接收时间："+simpleDateFormat.format(new Date()));
                System.out.println("接收的消息："+msg);
                //  修改redis 中的数据
                this.redisTemplate.opsForValue().set(msgKey,"1");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("接收时间："+simpleDateFormat.format(new Date()));
        System.out.println("接收的消息："+msg);

        //  修改redis 中的数据
        this.redisTemplate.opsForValue().set(msgKey,"1");
        //  手动确认消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }


}
