package com.yuzai.gmall.product.service.impl;

import com.yuzai.gmall.model.product.BaseCategoryTrademark;
import com.yuzai.gmall.model.product.BaseTrademark;
import com.yuzai.gmall.model.product.CategoryTrademarkVo;
import com.yuzai.gmall.product.mapper.BaseCategoryTrademarkMapper;
import com.yuzai.gmall.product.mapper.BaseTrademarkMapper;
import com.yuzai.gmall.product.service.BaseCategoryTrademarkService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseCategoryTrademarkServiceImpl extends ServiceImpl<BaseCategoryTrademarkMapper, BaseCategoryTrademark> implements BaseCategoryTrademarkService {

    @Autowired
    BaseTrademarkMapper baseTrademarkMapper;

    @Autowired
    BaseCategoryTrademarkMapper baseCategoryTrademarkMapper;

    @Override
    public List<BaseTrademark> findTrademarkList(Long category3Id) {
        QueryWrapper<BaseCategoryTrademark> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category3_id",category3Id);

        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseCategoryTrademarkMapper.selectList(queryWrapper);
        if(!CollectionUtils.isEmpty(baseCategoryTrademarkList)){
            List<Long> list = baseCategoryTrademarkList.stream().map(baseCategoryTrademark -> baseCategoryTrademark.getTrademarkId()).collect(Collectors.toList());
            return baseTrademarkMapper.selectBatchIds(list);
        }
        return null;

    }

    @Override
    public void saveVo(CategoryTrademarkVo categoryTrademarkVo) {
        List<Long> trademarkIdList = categoryTrademarkVo.getTrademarkIdList();
        if(!CollectionUtils.isEmpty(trademarkIdList)){
            List<BaseCategoryTrademark> categoryTrademarkList = trademarkIdList.stream().map(trademarkId -> {
                BaseCategoryTrademark categoryTrademark = new BaseCategoryTrademark();
                categoryTrademark.setCategory3Id(categoryTrademarkVo.getCategory3Id());
                categoryTrademark.setTrademarkId(trademarkId);
                return categoryTrademark;
            }).collect(Collectors.toList());
            this.saveBatch(categoryTrademarkList);
        }

    }

    @Override
    public List<BaseTrademark> findCurrentTrademarkList(Long category3Id) {

        QueryWrapper<BaseCategoryTrademark> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category3_id", category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseCategoryTrademarkMapper.selectList(queryWrapper);

        if(!CollectionUtils.isEmpty(baseCategoryTrademarkList)){
            List<Long> trademarkIds = baseCategoryTrademarkList.stream().map(baseCategoryTrademark -> baseCategoryTrademark.getTrademarkId()).collect(Collectors.toList());

            QueryWrapper<BaseTrademark> baseTrademarkQueryWrapper = new QueryWrapper<>();
            baseTrademarkQueryWrapper.notIn("id",trademarkIds);
            List<BaseTrademark> baseTrademarkList = baseTrademarkMapper.selectList(baseTrademarkQueryWrapper);
            return baseTrademarkList;
        }
        return baseTrademarkMapper.selectList(null);


    }

    @Override
    public void removeTrademark(Long category3Id, Long trademarkId) {
        QueryWrapper<BaseCategoryTrademark> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category3_id",category3Id);
        queryWrapper.eq("trademark_id",trademarkId);
        baseCategoryTrademarkMapper.delete(queryWrapper);
    }
}
