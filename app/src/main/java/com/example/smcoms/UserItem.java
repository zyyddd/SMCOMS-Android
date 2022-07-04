package com.example.smcoms;

public class UserItem {
    String uid;
    String loginname;
    String password;
    String rid;
    String username;

    public UserItem(String uid, String loginname, String password, String rid, String username) {
        this.uid = uid;
        this.loginname = loginname;
        this.password = password;
        this.rid = rid;
        this.username = username;
    }

    public UserItem() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
