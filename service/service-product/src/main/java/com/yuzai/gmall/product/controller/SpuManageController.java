package com.yuzai.gmall.product.controller;

import com.yuzai.gmall.common.result.Result;
import com.yuzai.gmall.model.product.BaseSaleAttr;
import com.yuzai.gmall.model.product.SpuInfo;
import com.yuzai.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/product")
public class SpuManageController {

    @Autowired
    ManageService manageService;

    @PostMapping("saveSpuInfo")
    @Transactional(rollbackFor = Exception.class)
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        manageService.saveSpuInfo(spuInfo);

        return Result.ok();

    }

    @GetMapping("baseSaleAttrList")
    public Result baseSaleAttrList(){
        List<BaseSaleAttr> baseSaleAttrList = manageService.getBaseSaleAttrList();

        return Result.ok(baseSaleAttrList);
    }

    @GetMapping("{page}/{size}")
    public Result getSpuInfoPage(@PathVariable Long page,
                             @PathVariable Long size,
                             SpuInfo spuInfo){

        Page<SpuInfo> spuInfoPage = new Page<>(page, size);

        IPage<SpuInfo> spuInfoIPageList = manageService.getSpuInfoPage(spuInfoPage, spuInfo);

        return Result.ok(spuInfoIPageList);
    }
}
