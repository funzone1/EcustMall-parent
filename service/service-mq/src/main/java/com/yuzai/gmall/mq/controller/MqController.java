package com.yuzai.gmall.mq.controller;


import com.yuzai.gmall.common.result.Result;
import com.yuzai.gmall.mq.config.DelayedMqConfig;
import com.yuzai.gmall.service.RabbitService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/mq")
public class MqController {


   @Autowired
   private RabbitService rabbitService;
   @Autowired
   private RabbitTemplate rabbitTemplate;


   /**
    * 消息发送
    */
   //http://localhost:8282/mq/sendConfirm
   @GetMapping("sendConfirm")
   public Result sendConfirm() {
     
      rabbitService.sendMessage("exchange.confirm", "routing.confirm", "来人了，开始接客吧！");
      return Result.ok();
   }

   @GetMapping("sendelay")
   public Result sendDelay() {
      //  声明一个时间对象
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      System.out.println("发送时间："+simpleDateFormat.format(new Date()));
      this.rabbitService.sendDelayMsg(DelayedMqConfig.exchange_delay,DelayedMqConfig.routing_delay,"iuok",3);
      return Result.ok();

   }


}
