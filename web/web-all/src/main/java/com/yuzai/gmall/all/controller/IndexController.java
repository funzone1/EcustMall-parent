package com.yuzai.gmall.all.controller;

import com.yuzai.gmall.common.result.Result;
import com.yuzai.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @GetMapping({"/","index.html"})
    public String index(HttpServletRequest request){
      // 获取首页分类数据
       Result result = productFeignClient.getBaseCategoryList();
      request.setAttribute("list",result.getData());
      return "index/index";
  }
}
