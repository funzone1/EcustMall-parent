package com.yuzai.gmall.product.service;

import com.yuzai.gmall.model.product.BaseCategoryTrademark;
import com.yuzai.gmall.model.product.BaseTrademark;
import com.yuzai.gmall.model.product.CategoryTrademarkVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BaseCategoryTrademarkService extends IService<BaseCategoryTrademark> {
    List<BaseTrademark> findTrademarkList(Long category3Id);

    void saveVo(CategoryTrademarkVo categoryTrademarkVo);

    List<BaseTrademark> findCurrentTrademarkList(Long category3Id);

    void removeTrademark(Long category3Id, Long trademarkId);
}
