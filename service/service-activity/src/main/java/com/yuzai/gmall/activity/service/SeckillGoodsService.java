package com.yuzai.gmall.activity.service;

import com.yuzai.gmall.model.activity.SeckillGoods;

import java.util.List;

public interface SeckillGoodsService {
    /**
     * 查询秒杀列表
     * @return
     */
    List<SeckillGoods> findAll();

    /**
     * 商品详情
     * @param skuId
     * @return
     */
    SeckillGoods getSeckillGoods(Long skuId);

    /**
     * 抢单
     * @param userId
     * @param skuId
     */
    void seckillOrder(String userId, Long skuId);

}
