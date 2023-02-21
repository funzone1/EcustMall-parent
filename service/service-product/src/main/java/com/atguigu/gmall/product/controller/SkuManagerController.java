package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class SkuManagerController {

    @Autowired
    private ManageService manageService;


    /**
     * 商品的上架
     *    admin/product/onSale/{skuId}
     * @return
     */
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable Long skuId){

        manageService.onSale(skuId);

        return Result.ok();
    }



    /**
     * 商品的下架
     *    admin/product/cancelSale/{skuId}
     * @return
     */
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable Long skuId){

        manageService.cancelSale(skuId);

        return Result.ok();
    }



    /**
     * sku分页列表
     * admin/product/list/{page}/{limit}
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/list/{page}/{limit}")
    public Result skuListPage(@PathVariable Long page,
                              @PathVariable Long limit){

        //封装分页对象
        Page<SkuInfo> skuInfoPage=new Page<>(page,limit);
        //查询
        IPage<SkuInfo> infoIPage=manageService.skuListPage(skuInfoPage);



        return Result.ok(infoIPage);

    }
    /**
     * admin/product/saveSkuInfo
     * 保存skuInfo
     * @param skuInfo
     * @return
     */
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){

        manageService.saveSkuInfo(skuInfo);

        return Result.ok();

    }


    /**
     * admin/product/spuImageList/{spuId}
     * 根据spuId查询图片列表
     * @param spuId
     * @return
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result spuImageList(@PathVariable Long spuId){

      List<SpuImage> spuImageList=  manageService.spuImageList(spuId);

        return Result.ok(spuImageList);
    }


    /**
     * 根据spuId查询销售属性和销售属性值集合
     * admin/product/spuSaleAttrList/{spuId}
     * @return
     */
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result spuSaleAttrList(@PathVariable Long spuId){


       List<SpuSaleAttr> spuSaleAttrList=manageService.spuSaleAttrList(spuId);


        return Result.ok(spuSaleAttrList);
    }


}
