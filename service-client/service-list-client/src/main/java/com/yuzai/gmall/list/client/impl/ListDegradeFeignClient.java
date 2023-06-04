package com.yuzai.gmall.list.client.impl;

import com.yuzai.gmall.common.result.Result;
import com.yuzai.gmall.list.client.ListFeignClient;
import com.yuzai.gmall.model.list.SearchParam;
import org.springframework.stereotype.Component;

@Component
public class ListDegradeFeignClient implements ListFeignClient {

    @Override
    public Result incrHotScore(Long skuId) {

            return Result.fail();
    }

    @Override
    public Result list(SearchParam searchParam) {
        return Result.fail();
    }

    @Override
    public Result upperGoods(Long skuId) {
        return null;
    }

    @Override
    public Result lowerGoods(Long skuId) {
        return null;
    }

}
