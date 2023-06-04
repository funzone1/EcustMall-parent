package com.yuzai.gmall.user.client.impl;


import com.yuzai.gmall.model.user.UserAddress;
import com.yuzai.gmall.user.client.UserFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDegradeFeignClient implements UserFeignClient {


    @Override
    public List<UserAddress> findUserAddressListByUserId(String userId) {
        return null;
    }
}
