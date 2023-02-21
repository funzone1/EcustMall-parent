package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ManageService {
    List<BaseCategory1> getCategery1();

    List<BaseCategory2> getCategery2(Long category1Id);

    List<BaseCategory3> getCategery3(Long category2Id);

    List<BaseAttrInfo> getattrInfoList(Long category1Id, Long category2Id, Long category3Id);

    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    BaseAttrInfo getAttrInfo(Long attrId);

    IPage<SpuInfo> getSpuInfoPage(Page<SpuInfo> spuInfoPage, SpuInfo spuInfo);

    List<BaseSaleAttr> getBaseSaleAttrList();

    void saveSpuInfo(SpuInfo spuInfo);

    /**
     * 根据spuId查询销售属性和销售属性值集合
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> spuSaleAttrList(Long spuId);

    /**
     * 根据spuId查询图片列表
     * @param spuId
     * @return
     */
    List<SpuImage> spuImageList(Long spuId);

    /**
     * 保存skuInfo
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * sku分页列表
     * @param skuInfoPage
     * @return
     */
    IPage<SkuInfo> skuListPage(Page<SkuInfo> skuInfoPage);

    /**
     * 商品的上架
     * @param skuId
     */
    void onSale(Long skuId);

    /**
     * 商品的下架
     * @param skuId
     */
    void cancelSale(Long skuId);


}
