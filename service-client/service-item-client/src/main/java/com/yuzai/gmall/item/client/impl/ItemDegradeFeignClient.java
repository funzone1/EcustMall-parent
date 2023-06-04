package com.yuzai.gmall.item.client.impl;

import com.yuzai.gmall.common.result.Result;
import com.yuzai.gmall.item.client.ItemFeignClient;
import org.springframework.stereotype.Component;

@Component
public class ItemDegradeFeignClient implements ItemFeignClient {
    @Override
    public Result getItem(Long skuId) {
        return Result.fail();
    }
}
