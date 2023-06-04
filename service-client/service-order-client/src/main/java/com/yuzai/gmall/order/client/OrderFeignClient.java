package com.yuzai.gmall.order.client;


import com.yuzai.gmall.common.result.Result;
import com.yuzai.gmall.model.order.OrderInfo;
import com.yuzai.gmall.order.client.impl.OrderDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "service-order", fallback = OrderDegradeFeignClient.class)
public interface OrderFeignClient {
    @GetMapping("/api/order/auth/trade")
    Result<Map<String, Object>> trade();

    /**
     * 获取订单
     * @param orderId
     * @return
     */
    @GetMapping("/api/order/inner/getOrderInfo/{orderId}")
    OrderInfo getOrderInfo(@PathVariable(value = "orderId") Long orderId);

}
