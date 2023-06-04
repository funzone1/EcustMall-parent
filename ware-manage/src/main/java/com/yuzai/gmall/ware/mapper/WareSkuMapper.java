package com.yuzai.gmall.ware.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzai.gmall.ware.bean.WareSku;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @param
 * @return
 */
@Repository
public interface WareSkuMapper extends BaseMapper<WareSku> {

    public Integer selectStockBySkuid(String skuid);

    public int incrStockLocked(WareSku wareSku);

    public int selectStockBySkuidForUpdate(WareSku wareSku);

    public int  deliveryStock(WareSku wareSku);

    public List<WareSku> selectWareSkuAll();
}
