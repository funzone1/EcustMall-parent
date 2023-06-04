package com.yuzai.gmall.product.controller;

import com.yuzai.gmall.common.result.Result;
import com.yuzai.gmall.model.product.*;
import com.yuzai.gmall.product.service.ManageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品基础属性接口")
@RestController
@RequestMapping("admin/product")
//@CrossOrigin
public class BaseManageController {

    @Autowired
    ManageService manageService;

    @GetMapping("getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable Long attrId){
        BaseAttrInfo baseAttrInfo = manageService.getAttrInfo(attrId);
        List<BaseAttrValue> list = baseAttrInfo.getAttrValueList();

        return Result.ok(list);
    }

    /**
     * 保存和修改平台属性
     * @param baseAttrInfo
     * @return
     */
    @PostMapping("saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        manageService.saveAttrInfo(baseAttrInfo);
        return Result.ok();

    }

    @GetMapping("attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result attrInfoList(@PathVariable Long category1Id,
                               @PathVariable Long category2Id,
                               @PathVariable Long category3Id){

        List<BaseAttrInfo> list = manageService.getattrInfoList(category1Id,category2Id,category3Id);
        return Result.ok(list);

    }

    /**
     * 查询所有的一级分类信息
     * @return
     */
    @GetMapping("getCategory1")
    public Result getCategory1(){
        List<BaseCategory1> category1List =  manageService.getCategery1();
        return Result.ok(category1List);
    }


    /**
     * 根据一级分类Id 查询二级分类数据
     * @param category1Id
     * @return
     */
    @GetMapping("getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable Long category1Id){
        List<BaseCategory2> category2List = manageService.getCategery2(category1Id);
        return Result.ok(category2List);
    }

    /**
     * 根据二级分类Id 查询三级分类数据
     * @param category2Id
     * @return
     */
    @GetMapping("getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable Long category2Id){
        List<BaseCategory3> category3List = manageService.getCategery3(category2Id);
        return Result.ok(category3List);
    }
}
