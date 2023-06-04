package com.yuzai.gmall.user.service;


import com.yuzai.gmall.model.user.UserInfo;

public interface UserService {

    /**
     * 登录方法
     * @param userInfo
     * @return
     */
    UserInfo login(UserInfo userInfo);

}
