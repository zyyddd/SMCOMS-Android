package com.example.smcoms;

//给管理员适配器使用的
public class OrderItem2 {
    String oid;
    String goodname;
    String num;
    String vipnum;
    String isPay;

    public OrderItem2(String oid, String goodname, String num, String vipnum, String isPay) {
        this.oid = oid;
        this.goodname = goodname;
        this.num = num;
        this.vipnum = vipnum;
        this.isPay = isPay;
    }

    public OrderItem2() {
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getVipnum() {
        return vipnum;
    }

    public void setVipnum(String vipnum) {
        this.vipnum = vipnum;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }
}
