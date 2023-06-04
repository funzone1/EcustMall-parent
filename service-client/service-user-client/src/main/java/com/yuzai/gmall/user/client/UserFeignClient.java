package com.yuzai.gmall.user.client;


import com.yuzai.gmall.model.user.UserAddress;
import com.yuzai.gmall.user.client.impl.UserDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "service-user", fallback = UserDegradeFeignClient.class)
public interface UserFeignClient {

    @GetMapping("/api/user/inner/findUserAddressListByUserId/{userId}")
    List<UserAddress> findUserAddressListByUserId(@PathVariable(value = "userId") String userId);
}

