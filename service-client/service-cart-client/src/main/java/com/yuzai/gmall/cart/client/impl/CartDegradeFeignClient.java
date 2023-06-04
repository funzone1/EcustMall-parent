package com.yuzai.gmall.cart.client.impl;

import com.yuzai.gmall.cart.client.CartFeignClient;
import com.yuzai.gmall.model.cart.CartInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartDegradeFeignClient implements CartFeignClient {

    @Override
    public List<CartInfo> getCartCheckedList(String userId) {
        return null;
    }
}
