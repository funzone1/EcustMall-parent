package com.yuzai.gmall.product.service.impl;

import com.yuzai.gmall.model.product.BaseTrademark;
import com.yuzai.gmall.product.mapper.BaseTrademarkMapper;
import com.yuzai.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper,BaseTrademark> implements BaseTrademarkService {



    @Override
    public IPage<BaseTrademark> getPage(Page<BaseTrademark> trademarkPage) {
        QueryWrapper<BaseTrademark> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        return baseMapper.selectPage(trademarkPage,queryWrapper);


    }


}
