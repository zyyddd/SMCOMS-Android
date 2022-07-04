package com.example.smcoms;

import java.math.BigDecimal;

public class OrdersItem {
    String oid;
    String goodsname;
    String goodsprice;
    String goodsnum;

    public OrdersItem(String oid, String goodsname, String goodsprice, String goodsnum) {
        this.oid = oid;
        this.goodsname = goodsname;
        this.goodsprice = goodsprice;
        this.goodsnum = goodsnum;
    }

    public OrdersItem() {
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(String goodsprice) {
        this.goodsprice = goodsprice;
    }

    public String getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(String goodsnum) {
        this.goodsnum = goodsnum;
    }
}
