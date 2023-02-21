package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional
public class ManageServiceImpl implements ManageService {

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private  SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    BaseCategory2Mapper baseCategory2Mapper;

    @Autowired
    BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SpuPosterMapper spuPosterMapper;
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;



    @Override
    public List<BaseCategory1> getCategery1() {
        return baseCategory1Mapper.selectList(null);
    }

    @Override
    public List<BaseCategory2> getCategery2(Long category1Id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("category1_id",category1Id);

        return baseCategory2Mapper.selectList(queryWrapper);
    }

    @Override
    public List<BaseCategory3> getCategery3(Long category2Id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("category2_id",category2Id);

        return baseCategory3Mapper.selectList(queryWrapper);
    }

    @Override
    public List<BaseAttrInfo> getattrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        return baseAttrInfoMapper.getAttrInfoList(category1Id,category2Id,category3Id);

    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //修改属性
        if(baseAttrInfo.getId() != null){
            baseAttrInfoMapper.updateById(baseAttrInfo);
        }else {
            //保存属性
            baseAttrInfoMapper.insert(baseAttrInfo);
        }
        //保存AttrValue属性
        //先删除之前的再覆盖
        QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id", baseAttrInfo.getId());
        baseAttrValueMapper.delete(wrapper);
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if(!CollectionUtils.isEmpty(attrValueList)) {
            for (BaseAttrValue baseAttrValue : attrValueList) {
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }

    @Override
    public BaseAttrInfo getAttrInfo(Long attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectById(attrId);
        QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id",attrId);
        List<BaseAttrValue> list = baseAttrValueMapper.selectList(wrapper);
        baseAttrInfo.setAttrValueList(list);

        return baseAttrInfo;
    }

    @Override
    public IPage<SpuInfo> getSpuInfoPage(Page<SpuInfo> spuInfoPage, SpuInfo spuInfo) {

        QueryWrapper<SpuInfo> spuInfoQueryWrapper = new QueryWrapper<>();
        spuInfoQueryWrapper.eq("category3_id", spuInfo.getCategory3Id());
        spuInfoQueryWrapper.orderByDesc("id");
        return spuInfoMapper.selectPage(spuInfoPage,spuInfoQueryWrapper);

    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectList(null);
    }

    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        spuInfoMapper.insert(spuInfo);

        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if(!CollectionUtils.isEmpty(spuImageList)){
            for (SpuImage spuImage : spuImageList) {
                spuImage.setSpuId(spuInfo.getId());
                spuImageMapper.insert(spuImage);
            }
        }

        List<SpuPoster> spuPosterList = spuInfo.getSpuPosterList();
        if(!CollectionUtils.isEmpty(spuPosterList)){
            for (SpuPoster spuPoster : spuPosterList) {
                spuPoster.setSpuId(spuInfo.getId());
                spuPosterMapper.insert(spuPoster);
            }
        }

        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if(!CollectionUtils.isEmpty(spuSaleAttrList)){
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(spuSaleAttr);

                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if(!CollectionUtils.isEmpty(spuSaleAttrValueList)){
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    }
                }

            }
        }

    }
    /**
     * 根据spuId查询销售属性和销售属性值集合
     * @param spuId
     * @return
     */
    @Override
    public List<SpuSaleAttr> spuSaleAttrList(Long spuId) {

        return  spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    /**
     * 根据spuId查询图片列表
     * @param spuId
     * @return
     */
    @Override
    public List<SpuImage> spuImageList(Long spuId) {

        //创建条件对象
        QueryWrapper<SpuImage> queryWrapper=new QueryWrapper<>();
        //select *from spu_image where spuid=spuId
        queryWrapper.eq("spu_id",spuId);

        return  spuImageMapper.selectList(queryWrapper);
    }

    /**
     * 保存skuInfo
     *  操作的表：
     *   sku_info 基本信息表
     *   sku_image sku图片表
     *   sku_sale_attr_value sku销售属性表
     *   sku_attr_value sku品台属性表
     *
     * @param skuInfo
     */
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {

        //设置is_sale
        skuInfo.setIsSale(0);
        //保存skuinfo
        skuInfoMapper.insert(skuInfo);
        //保存图片
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        //判断
        if(!CollectionUtils.isEmpty(skuImageList)){

            for (SkuImage skuImage : skuImageList) {

                //设置skuid
                skuImage.setSkuId(skuInfo.getId());
                //保存
                skuImageMapper.insert(skuImage);

            }

        }

        //保存平台属性
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        //判断
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {

                //设置skuId
                skuAttrValue.setSkuId(skuInfo.getId());
                //保存
                skuAttrValueMapper.insert(skuAttrValue);
            }


        }


        //保存销售属性
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        //判断
        if(!CollectionUtils.isEmpty(skuSaleAttrValueList)){

            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {

                //设置skuId
                skuSaleAttrValue.setSkuId(skuInfo.getId());

                //设置spuId
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                //保存
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);

            }

        }


    }

    /**
     * sku分页列表
     * @param skuInfoPage
     * @return
     */
    @Override
    public IPage<SkuInfo> skuListPage(Page<SkuInfo> skuInfoPage) {

        //排序
        QueryWrapper<SkuInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");


        return skuInfoMapper.selectPage(skuInfoPage,queryWrapper);
    }

    /**
     * 商品上架
     * 将is_sale改为1
     * @param skuId
     */
    @Override
    public void onSale(Long skuId) {

        //封装对象
        SkuInfo skuInfo=new SkuInfo();
        //设置条件
        skuInfo.setId(skuId);
        //设置修改的内容
        skuInfo.setIsSale(1);


        skuInfoMapper.updateById(skuInfo);

    }

    /**
     * 商品的下架
     *  将is_sale改为0
     * @param skuId
     */
    @Override
    public void cancelSale(Long skuId) {
        //封装对象
        SkuInfo skuInfo=new SkuInfo();
        //设置条件
        skuInfo.setId(skuId);
        //设置修改的内容
        skuInfo.setIsSale(0);

        skuInfoMapper.updateById(skuInfo);
    }
}
