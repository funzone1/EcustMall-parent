package com.yuzai.gmall.cart.service.impl;

import com.yuzai.gmall.cart.service.CartService;
import com.yuzai.gmall.constant.RedisConst;
import com.yuzai.gmall.common.util.DateUtil;
import com.yuzai.gmall.model.cart.CartInfo;
import com.yuzai.gmall.model.product.SkuInfo;
import com.yuzai.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void addToCart(Long skuId, String userId, Integer skuNum) {
        //  获取缓存key
        String cartKey = getCartKey(userId);

        BoundHashOperations<String, String, CartInfo> boundHashOps = this.redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = null;
        //包含的话更新数量
        if(boundHashOps.hasKey(skuId.toString())) {
            cartInfo = boundHashOps.get(skuId.toString());
            cartInfo.setSkuNum(cartInfo.getSkuNum()+skuNum);
            cartInfo.setIsChecked(1);
            cartInfo.setSkuPrice(productFeignClient.getSkuPrice(skuId));
            cartInfo.setUpdateTime(new Date());
        } else {
            cartInfo = new CartInfo();
            //  给cartInfo 赋值！
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);

            //  给表的字段赋值！
            cartInfo.setUserId(userId);
            cartInfo.setSkuId(skuId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
            cartInfo.setSkuPrice(skuInfo.getPrice());
        }
        boundHashOps.put(skuId.toString(), cartInfo);

    }

    @Override
    public List<CartInfo> getCartList(String userId, String userTempId) {
        /*
            1.  判断是否登录，根据判断结果查询不同的购物车！
            2.  查询的结果需要排序！
            3.  有可能需要合并！
                    在登录的情况下
                    .  未登录 ---> 登录合并！
                        合并完成之后，需要删除未登录购物车数据！
                     case1: 有userId ,没有userTempId
                     case2: 没有userId ,有userTempId   return noLoginCartInfoList
                     case3: 有userId ,有userTempId
                        登录情况下合并购物车：
                            先判断未登录购物车集合有数据！
                                true: 有数据
                                    合并
                                false: 没有数据
                                    只需要登录购物车数据
                            删除未登录购物车！
         */
        //  声明一个集合来存储未登录数据
        List<CartInfo> noLoginCartInfoList = null;

        //  完成case2 业务逻辑
        //  属于未登录
        if (!StringUtils.isEmpty(userTempId)){
            String cartKey = this.getCartKey(userTempId);
            //  获取登录的购物车集合数据！
            //  noLoginCartInfoList = this.redisTemplate.boundHashOps(cartKey).values();
            noLoginCartInfoList  = this.redisTemplate.opsForHash().values(cartKey);
        }
        //  这个是集合的排序
        if (StringUtils.isEmpty(userId)){
            if (!CollectionUtils.isEmpty(noLoginCartInfoList)){
                noLoginCartInfoList.sort((o1,o2)->{
                    //  按照更新时间：
                    return DateUtil.truncatedCompareTo(o2.getUpdateTime(),o1.getUpdateTime(), Calendar.SECOND);
                });
            }
            //  返回未登录数据！
            return noLoginCartInfoList;
        }
        //  ----------------------case 1 and case3 -----------------
        /*
        demo:
            登录：
                17  1
                18  1

            未登录：
                17  1
                18  1
                19  2

             合并：
                17  2
                18  2
                19  2
         */
        //  属于登录
        List<CartInfo> LoginCartInfoList = null;
        //  先获取到登录购物车的key
        String cartKey = this.getCartKey(userId);
        //  hset key field value;  hget key field;  hvals key ; hmset key field value field value;  hmset key map;
        //  合并思路二：
        BoundHashOperations<String, String, CartInfo>  boundHashOperations = this.redisTemplate.boundHashOps(cartKey);
        //  判断购物车中的field
        //  boundHashOperations.hasKey(skuId.toString);
        if (!CollectionUtils.isEmpty(noLoginCartInfoList)){
            //  循环遍历未登录购物车集合
            noLoginCartInfoList.stream().forEach(cartInfo -> {
                //  在未登录购物车中的skuId 与登录的购物车skuId 相对  skuId = 17 18
                if (boundHashOperations.hasKey(cartInfo.getSkuId().toString())){
                    //  合并业务逻辑 : skuNum + skuNum 更新时间
                    CartInfo loginCartInfo = boundHashOperations.get(cartInfo.getSkuId().toString());
                    loginCartInfo.setSkuNum(loginCartInfo.getSkuNum()+cartInfo.getSkuNum());
                    loginCartInfo.setUpdateTime(new Date());
                    //  最新价格
                    loginCartInfo.setSkuPrice(productFeignClient.getSkuPrice(cartInfo.getSkuId()));

                    //  选中状态合并！
                    if (cartInfo.getIsChecked().intValue()==1){
//                        if (loginCartInfo.getIsChecked().intValue()==0){
//                            loginCartInfo.setIsChecked(1);
//                        }
                        loginCartInfo.setIsChecked(1);
                    }
                    //  修改缓存的数据：    hset key field value
                    boundHashOperations.put(cartInfo.getSkuId().toString(),loginCartInfo);
                }else {
                    //  直接添加到缓存！    skuId = 19
                    cartInfo.setUserId(userId);
                    cartInfo.setCreateTime(new Date());
                    cartInfo.setUpdateTime(new Date());
                    boundHashOperations.put(cartInfo.getSkuId().toString(),cartInfo);
                }
            });
            //  删除未登录购物车数据！
            this.redisTemplate.delete(this.getCartKey(userTempId));
        }

        //  获取到合并之后的数据：
        LoginCartInfoList = this.redisTemplate.boundHashOps(cartKey).values();
        if (CollectionUtils.isEmpty(LoginCartInfoList)){
            return new ArrayList<>();
        }
        //  设置合并之后的排序结果！
        LoginCartInfoList.sort(((o1, o2) -> {
            return DateUtil.truncatedCompareTo(o2.getUpdateTime(),o1.getUpdateTime(), Calendar.SECOND);
        }));
        return LoginCartInfoList;


    }

    @Override
    public void checkCart(String userId, Integer isChecked, Long skuId) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> boundHashOps = this.redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = boundHashOps.get(skuId.toString());
        if(null != cartInfo) {
            cartInfo.setIsChecked(isChecked);
            boundHashOps.put(skuId.toString(), cartInfo);
        }

    }

    @Override
    public void deleteCart(Long skuId, String userId) {
        BoundHashOperations<String, String, CartInfo> boundHashOps = this.redisTemplate.boundHashOps(this.getCartKey(userId));
        //  判断购物车中是否有该商品！
        if (boundHashOps.hasKey(skuId.toString())){
            boundHashOps.delete(skuId.toString());
        }

    }

    @Override
    public List<CartInfo> getCartCheckedList(String userId) {
        //  获取的选中的购物车列表！
        String cartKey = this.getCartKey(userId);
        //  获取到购物车集合数据：
        List<CartInfo>  cartInfoList = this.redisTemplate.opsForHash().values(cartKey);
        List<CartInfo> cartInfos = cartInfoList.stream().filter(cartInfo -> {
            //  再次确认一下最新价格
            cartInfo.setSkuPrice(productFeignClient.getSkuPrice(cartInfo.getSkuId()));
            return cartInfo.getIsChecked().intValue() == 1;
        }).collect(Collectors.toList());

        //  返回数据
        return cartInfos;

    }

    // 获取购物车的key
        private String getCartKey(String userId) {
                //定义key user:userId:cart
                return RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_CART_KEY_SUFFIX;
        }
}
