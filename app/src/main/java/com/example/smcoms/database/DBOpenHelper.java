package com.example.smcoms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 该界面主要用于建表，以及设定完整性
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    /**
     * 带全部参数的构造函数，此构造函数必不可少
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //顾客信息
        String customer = "create table customer(" +
                "vipnum int primary key not null  , " +
                "vipname varchar(255) not null)";




        //商品信息
        String goods = "create table goods(" +
                "gid INTEGER primary key  AUTOINCREMENT ," +
                "price decimal(10,2) not null," +
                "resnum int not null," +
                "goodname varchar(255) )";

        //订单信息
        String t_order = "create table t_order(" +
                "oid INTEGER primary key AUTOINCREMENT," +
                "gid int not null," +
                "vipnum int default null ," +
                "num int not null," +
                "isPay int default 0," +
                "foreign key(gid) references goods(gid)," +
                "foreign key(vipnum) references customer(vipnum))";

        //用户信息
        String user = "create table user(" +
                "uid INTEGER primary key  AUTOINCREMENT," +
                "loginname varchar(255) not null," +
                "password varchar(255) not null," +
                "username varchar(255) default null," +
                "rid int not null default 1)";

        //个人资源配置表，比如更改图片之类的
        String personal_resource = "create table personal_resource(" +
                "uid int not null ," +
                "IMAGE blob," +
                "foreign key(uid) references user(uid))";

        String insert_user1  = "insert into user(uid,loginname,password,username,rid) values(1,'1','1','郑收银员',1)";
        String insert_user2  = "insert into user(uid,loginname,password,username,rid) values(2,'0','0','郑老板',0)";
        String insert_customer1 ="insert into customer(vipnum,vipname) values (1001,'郑煜东1')";
        String insert_customer2 ="insert into customer(vipnum,vipname) values (1002,'郑煜东2')";
        String insert_goods1 = "insert into goods(gid,price,resnum,goodname) values (1,13,100,'老坛酸菜')";
        String insert_goods2 = "insert into goods(gid,price,resnum,goodname) values (2,13,100,'干脆面')";

//        String insert_t_order1 = "insert into t_order(oid,gid,vipnum,num) values(1,1,1001,2)";
//        String insert_t_order2  = "insert into t_order(oid,gid,vipnum,num) values(2,2,1001,2)";

        //触发器

        String personal_resource_trigger = "CREATE TRIGGER personal_resource_trigger AFTER INSERT ON user " +
                "BEGIN INSERT INTO personal_resource(uid,IMAGE) VALUES (new.uid,null);END;";



        db.execSQL(user);
        db.execSQL(personal_resource);
        db.execSQL(personal_resource_trigger);
        db.execSQL(insert_user1);
        db.execSQL(insert_user2);

        db.execSQL(customer);
        db.execSQL(insert_customer1);
        db.execSQL(insert_customer2);

        db.execSQL(goods);
        db.execSQL(insert_goods1);
        db.execSQL(insert_goods2);


        db.execSQL(t_order);
//        db.execSQL(insert_t_order1);
//        db.execSQL(insert_t_order2);







    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS "+"customer");
//        db.execSQL("DROP TABLE IF EXISTS "+"goods");
//        db.execSQL("DROP TABLE IF EXISTS "+"user");
//        db.execSQL("DROP TABLE IF EXISTS "+"t_order");
//        db.execSQL("DROP TABLE IF EXISTS "+"personal_resourse");
//        onCreate(db);
    }

}
