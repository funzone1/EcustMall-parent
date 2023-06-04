package com.yuzai.gmall.product.client.impl;

import com.yuzai.gmall.common.result.Result;
import com.atguigu.gmall.model.product.*;
import com.yuzai.gmall.model.product.*;
import com.yuzai.gmall.product.client.ProductFeignClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ProductDegradeFeignClient implements ProductFeignClient {

    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        return null;
    }

    @Override
    public BaseCategoryView getCategoryView(Long category3Id) {
        return null;
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return null;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return null;
    }


    @Override
    public Map getSkuValueIdsMap(Long spuId) {
        return null;
    }
    @Override
    public List<SpuPoster> getSpuPosterBySpuId(Long spuId) {
        return null;
    }
    @Override
    public List<BaseAttrInfo> getAttrList(Long skuId) {
        return null;
    }

    @Override
    public Result getBaseCategoryList() {
        return Result.fail();
    }

    @Override
    public BaseTrademark getTrademark(Long tmId) {
        return null;
    }
}
