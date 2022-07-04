package com.example.smcoms;

public class VipItem {
    String vipnum;
    String vipname;

    public VipItem(String vipnum, String vipname) {
        this.vipnum = vipnum;
        this.vipname = vipname;
    }

    public VipItem() {
    }

    public String getVipnum() {
        return vipnum;
    }

    public void setVipnum(String vipnum) {
        this.vipnum = vipnum;
    }

    public String getVipname() {
        return vipname;
    }

    public void setVipname(String vipname) {
        this.vipname = vipname;
    }
}
