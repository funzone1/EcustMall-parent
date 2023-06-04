package com.yuzai.gmall.product.controller;

import com.yuzai.gmall.common.result.Result;
import com.yuzai.gmall.model.product.BaseTrademark;
import com.yuzai.gmall.model.product.CategoryTrademarkVo;
import com.yuzai.gmall.product.service.BaseCategoryTrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/product/baseCategoryTrademark")
public class BaseCategoryTrademarkController {

    @Autowired
    BaseCategoryTrademarkService baseCategoryTrademarkService;

    @PostMapping("save")
    public Result save(@RequestBody CategoryTrademarkVo categoryTrademarkVo){
        baseCategoryTrademarkService.saveVo(categoryTrademarkVo);

        return Result.ok();
    }

    @DeleteMapping("remove/{category3Id}/{trademarkId}")
    public Result remove(@PathVariable Long category3Id,
                         @PathVariable Long trademarkId){
        baseCategoryTrademarkService.removeTrademark(category3Id, trademarkId);
        return Result.ok();

    }

    @GetMapping("findTrademarkList/{category3Id}")
    public Result findTrademarkList(@PathVariable Long category3Id){
        List<BaseTrademark> list = baseCategoryTrademarkService.findTrademarkList(category3Id);
        return Result.ok(list);

    }

    @GetMapping("findCurrentTrademarkList/{category3Id}")
    public Result findCurrentTrademarkList(@PathVariable Long category3Id){
        List<BaseTrademark> list = baseCategoryTrademarkService.findCurrentTrademarkList(category3Id);
        return Result.ok(list);
    }

}
