package com.yuzai.gmall.payment.service;


public interface AlipayService {

    String createaliPay(Long orderId);
    boolean refund(Long orderId);
}
