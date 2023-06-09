package com.yuzai.gmall.product.mapper;

import com.yuzai.gmall.model.product.SkuSaleAttrValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SkuSaleAttrValueMapper extends BaseMapper<SkuSaleAttrValue> {
    // 根据spuId 查询map 集合数据
    List<Map> selectSaleAttrValuesBySpu(Long spuId);
}
