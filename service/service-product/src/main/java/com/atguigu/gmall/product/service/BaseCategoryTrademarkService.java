package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BaseCategoryTrademarkService extends IService<BaseCategoryTrademark> {
    List<BaseTrademark> findTrademarkList(Long category3Id);

    void saveVo(CategoryTrademarkVo categoryTrademarkVo);

    List<BaseTrademark> findCurrentTrademarkList(Long category3Id);

    void removeTrademark(Long category3Id, Long trademarkId);
}
