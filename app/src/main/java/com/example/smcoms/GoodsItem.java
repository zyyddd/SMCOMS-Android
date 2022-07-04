package com.example.smcoms;

public class GoodsItem {
    String gid;
    String goodname;
    String price;
    String resnum;

    public GoodsItem(String gid, String goodname, String price, String resnum) {
        this.gid = gid;
        this.goodname = goodname;
        this.price = price;
        this.resnum = resnum;
    }

    public GoodsItem() {
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getResnum() {
        return resnum;
    }

    public void setResnum(String resnum) {
        this.resnum = resnum;
    }
}
