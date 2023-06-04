package com.yuzai.gmall.order.client.impl;


import com.yuzai.gmall.common.result.Result;
import com.yuzai.gmall.model.order.OrderInfo;
import com.yuzai.gmall.order.client.OrderFeignClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderDegradeFeignClient implements OrderFeignClient {

    @Override
    public Result<Map<String, Object>> trade() {
        return Result.fail();
    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return null;
    }
}
