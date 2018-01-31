package com.laohe.sign;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: LaoHe
 * @Description:
 * @Date: 18:50 2018/1/31
 */
public class Person {

    /***身份证*/
    private String cardNumber;
    /***身份证--加密*/
    private String cardNumberA;
    /***身份证--签名*/
    private String cardNumberM;

    public String getCardNumber() {

        if (StringUtils.isNotEmpty(this.cardNumberA)) {
            cardNumber = SecurityUtils.BUSINESS.deCryptText(this.cardNumberA);
        }
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        if (!StringUtils.isEmpty(cardNumber)) {
            this.cardNumberA = SecurityUtils.BUSINESS.cryptText(cardNumber);
            this.cardNumberM = SecurityUtils.BUSINESS.getSign(cardNumber);
        }
    }

    public String getCardNumberA() {
        return cardNumberA;
    }

    public void setCardNumberA(String cardNumberA) {
        this.cardNumberA = cardNumberA;
    }

    public String getCardNumberM() {
        return cardNumberM;
    }

    public void setCardNumberM(String cardNumberM) {
        this.cardNumberM = cardNumberM;
    }
}
