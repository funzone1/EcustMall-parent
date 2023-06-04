package com.yuzai.gmall.product.service;

import com.yuzai.gmall.model.product.BaseTrademark;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BaseTrademarkService extends IService<BaseTrademark> {
    IPage<BaseTrademark> getPage(Page<BaseTrademark> trademarkPage);


}
