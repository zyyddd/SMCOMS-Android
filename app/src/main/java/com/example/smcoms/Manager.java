package com.example.smcoms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smcoms.common.CommonMethon;
import com.example.smcoms.common.CommonToolbarColor;
import com.example.smcoms.database.CommonDatabase;
import com.example.smcoms.database.image_store;
import com.example.smcoms.util.AlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager extends AppCompatActivity {

    private SQLiteDatabase db;

    /**
     * 切换用户弹出的对话框
     */
    private AlertDialog.Builder builder;

    /**
     * Toolbar用于替代原有的actionBar
     */
    private Toolbar toolbar;

    /**
     * 用于显示已經選擇的商品
     */
    private ListView listView_manager;

    //侧滑
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    /**
     * 用于获取NavigationView的headlayout，方便监听子项
     */
    private View headview;


    private TextView textView_welcome;


    private CircleImageView circleImageView;


    private Uri imageUri;

    private static final int TAKE_PHOTO = 1;

    private image_store imageStore;

    private Intent intent_1;

    //当前状况、对什么进行操作
    private String state = "";
    private Button manager_vip;
    private Button manager_goods;
    private Button manager_orders;
    private Button manager_user;
    private FloatingActionButton manager_add;
    private EditText manager_search_edit;
    private Button manager_search_button;

    private ManagerVipAdapter managerVipAdapter;
    private ManagerGoodsAdapter managerGoodsAdapter;
    private ManagerOrderAdapter managerOrderAdapter;
    private ManagerUserAdapter managerUserAdapter;
    private List<VipItem> listVip;
    private List<GoodsItem> listGoods;
    private List<OrderItem2> listOrders;
    private List<UserItem> listUser;


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager);

        initView();

        //获取登录信息，以锁定用户
        intent_1 = getIntent();

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            //设置左箭头图片
            actionBar.setHomeAsUpIndicator(R.drawable.a);

        }

        //headlayout中的欢迎实现
        textView_welcome.setText(findNameById(intent_1.getStringExtra("uid")));


        //菜单栏实现
        navigationView.setCheckedItem(R.id.nav_menu_myinfo);
        navigationView.setCheckedItem(R.id.nav_menu_changeacc);


        //设置标题栏与状态栏颜色保持一致
        new CommonToolbarColor().toolbarColorSet(Manager.this);

        //头像初始化
        Bitmap bitmap_temp = imageStore.getBmp(db, intent_1.getStringExtra("uid"));

        if (bitmap_temp != null) {
            circleImageView.setImageBitmap(bitmap_temp);
        }


        //NavigationView的菜单项监听器
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("Range")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_changeacc:
                        builder = new AlertDialogBuilder(Manager.this).build();
                        //   显示出该对话框
                        builder.show();

                        break;


                    default:
                        break;
                }
                return true;
            }
        });


        //相片拍照监听事件
        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.circleimage:
                        // 创建File对象，用于存储拍照后的图片
                        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT < 24) {
                            imageUri = Uri.fromFile(outputImage);
                        } else {
                            imageUri = FileProvider.getUriForFile(Manager.this, "com.example.SMCOMS.fileprovider", outputImage);
                        }
                        // 启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);

                        break;


                    default:
                        break;
                }
            }
        };
        circleImageView.setOnClickListener(listener);


        /**
         * 切换不同内容管理监听器
         */
        View.OnClickListener listenerChooseButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.manager_goods:
                        state = "goods";
                        adapterGoods();
                        break;
                    case R.id.manager_vip:
                        state = "vip";
                        adapterVip();

                        break;

                    case R.id.manager_orders:
                        state = "order";
                        adapterOrders();

                        break;

                    case R.id.manager_user:
                        state = "user";
                        adapterUser();
                        break;

                    default:
                        break;
                }
            }
        };

        manager_orders.setOnClickListener(listenerChooseButton);
        manager_vip.setOnClickListener(listenerChooseButton);
        manager_goods.setOnClickListener(listenerChooseButton);
        manager_user.setOnClickListener(listenerChooseButton);


        //添加按钮监听事件
        manager_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("vip")) {
                    Intent intent = new Intent(Manager.this, AddVip.class);
                    startActivity(intent);
                } else if (state.equals("goods")) {
                    Intent intent2 = new Intent(Manager.this,AddGoods.class);
                    startActivity(intent2);
                } else if (state.equals("order")) {

                } else if (state.equals("user")) {
                    Intent intent3 = new Intent(Manager.this,AddUser.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(Manager.this, "请先选择要添加的类型", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //查找按钮监听事件
        manager_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("vip")) {
                    String searchedit = manager_search_edit.getText().toString();
                    List<VipItem> templistvip = new ArrayList<>();
                    Cursor customer = db.query("customer", null, "vipnum = ?", new String[]{searchedit}, null, null, null);
                    if (customer.moveToNext()) {
                        String vipnum = customer.getString(customer.getColumnIndex("vipnum"));
                        String vipname = customer.getString(customer.getColumnIndex("vipname"));
                        VipItem it = new VipItem(vipnum, vipname);
                        templistvip.add(it);
                        managerVipAdapter = new ManagerVipAdapter(Manager.this, templistvip);
                        listView_manager.setAdapter(managerVipAdapter);
                    } else {
                        managerVipAdapter = new ManagerVipAdapter(Manager.this, templistvip);
                        listView_manager.setAdapter(managerVipAdapter);
                        Toast.makeText(Manager.this, "查找失败", Toast.LENGTH_SHORT).show();
                    }

                } else if (state.equals("goods")) {
                    String searchedit = manager_search_edit.getText().toString();
                    List<GoodsItem> templistvip = new ArrayList<>();
                    Cursor goods = db.query("goods", null, "gid = ?", new String[]{searchedit}, null, null, null);
                    if (goods.moveToNext()) {
                        String gid = goods.getString(goods.getColumnIndex("gid"));
                        String goodname = goods.getString(goods.getColumnIndex("goodname"));
                        String price = goods.getString(goods.getColumnIndex("price"));
                        String resnum = goods.getString(goods.getColumnIndex("resnum"));
                        GoodsItem it = new GoodsItem(gid, goodname,price,resnum);
                        templistvip.add(it);
                        managerGoodsAdapter = new ManagerGoodsAdapter(Manager.this, templistvip);
                        listView_manager.setAdapter(managerGoodsAdapter);
                    } else {
                        managerGoodsAdapter = new ManagerGoodsAdapter(Manager.this, templistvip);
                        listView_manager.setAdapter(managerGoodsAdapter);
                        Toast.makeText(Manager.this, "查找失败", Toast.LENGTH_SHORT).show();
                    }

                } else if (state.equals("order")) {
                    String searchedit = manager_search_edit.getText().toString();
                    List<OrderItem2> templistorder = new ArrayList<>();
                    Cursor order = db.query("t_order", null, "oid = ?", new String[]{searchedit}, null, null, null);
                    if (order.moveToNext()) {
                        String oid = order.getString(order.getColumnIndex("oid"));
                        String gid = order.getString(order.getColumnIndex("gid"));
                        //cursor不能直接使用需要判断moveToNext()
                        Cursor goods = db.query("goods", null, "gid = ?", new String[]{gid}, null, null, null);
                        String goodname = "";
                        if (goods.moveToNext()){
                            goodname = goods.getString(goods.getColumnIndex("goodname"));
                        }

                        String num = order.getString(order.getColumnIndex("num"));
                        String vipnum = order.getString(order.getColumnIndex("vipnum"));
                        OrderItem2 it = new OrderItem2(oid,goodname,num,vipnum,"");
                        templistorder.add(it);
                        managerOrderAdapter = new ManagerOrderAdapter(Manager.this, templistorder);
                        listView_manager.setAdapter(managerOrderAdapter);
                    } else {
                        managerOrderAdapter = new ManagerOrderAdapter(Manager.this, templistorder);
                        listView_manager.setAdapter(managerOrderAdapter);
                        Toast.makeText(Manager.this, "查找失败", Toast.LENGTH_SHORT).show();
                    }
                } else if (state.equals("user")) {
                    String searchedit = manager_search_edit.getText().toString();
                    List<UserItem> templistuser = new ArrayList<>();
                    Cursor user = db.query("user", null, "uid = ?", new String[]{searchedit}, null, null, null);
                    if (user.moveToNext()) {
                        String uid = user.getString(user.getColumnIndex("uid"));
                        String loginname = user.getString(user.getColumnIndex("loginname"));
                        String password = user.getString(user.getColumnIndex("password"));
                        String rid = user.getString(user.getColumnIndex("rid"));
                        String username = user.getString(user.getColumnIndex("username"));
                        UserItem it = new UserItem(uid,loginname,password,rid,username);
                        templistuser.add(it);
                        managerUserAdapter = new ManagerUserAdapter(Manager.this, templistuser);
                        listView_manager.setAdapter(managerUserAdapter);
                    } else {
                        managerUserAdapter = new ManagerUserAdapter(Manager.this, templistuser);
                        listView_manager.setAdapter(managerUserAdapter);
                        Toast.makeText(Manager.this, "查找失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Manager.this, "请先选择要查找的类型", Toast.LENGTH_SHORT).show();
                }
            }
        });





        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.manager_swipe_refresh);
        //进度条刷新旋钮的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //设置下拉刷新的监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Manager.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });


    }


    /**
     * 点击头像拍照实现
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示到头像中
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        circleImageView.setImageBitmap(bitmap);

                        //更新本人资源表
                        Bitmap bitmap1 = CommonMethon.compressBoundsBitmap(Manager.this, imageUri, 200, 200);
                        imageStore.update(bitmap1, db, intent_1.getStringExtra("uid"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    }

                }
                break;
            default:
                break;
        }
    }

    private void initView() {

        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(Manager.this, "SuperMarket");

        listView_manager = findViewById(R.id.listview_manager);

        toolbar = findViewById(R.id.toolbar_manager);

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerlayout_manager);

        navigationView = findViewById(R.id.navigation_view_manage);

        headview = navigationView.inflateHeaderView(R.layout.headlayout);


        textView_welcome = headview.findViewById(R.id.welcome_textview);

        imageStore = new image_store();

        circleImageView = (CircleImageView) headview.findViewById(R.id.circleimage);

        manager_vip = findViewById(R.id.manager_vip);
        manager_goods = findViewById(R.id.manager_goods);
        manager_orders = findViewById(R.id.manager_orders);
        manager_user = findViewById(R.id.manager_user);

        manager_add = findViewById(R.id.manager_add);

        manager_search_edit = findViewById(R.id.manager_search_edit);
        manager_search_button = findViewById(R.id.manager_search_button);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 根据用户的学号去查找姓名
     *
     * @param
     * @return
     */
    @SuppressLint("Range")
    public String findNameById(String uid) {
        Cursor cursor = db.query("user", null, "uid = ?", new String[]{uid}, null, null, null, null);

        //如果没查到
        if (cursor.getCount() == 0) {
            return "无法获取您的个人信息";
        } else {
            String str = "";
            while (cursor.moveToNext()) {
                str = cursor.getString(cursor.getColumnIndex("username"));
            }
            return str + " 欢迎您！";
        }

    }


    //给显示会员信息添加适配器
    @SuppressLint("Range")
    public void adapterVip() {
        listVip = new ArrayList<>();
        Cursor customer = db.query("customer", null, null, null, null, null, null);
        if (customer.getCount() == 0) {
            Toast.makeText(Manager.this, "没有任何会员信息", Toast.LENGTH_SHORT).show();
        } else {
            while (customer.moveToNext()) {
                String vipnum = customer.getString(customer.getColumnIndex("vipnum"));
                String vipname = customer.getString(customer.getColumnIndex("vipname"));
                VipItem it = new VipItem(vipnum, vipname);
                listVip.add(it);
            }
        }


        managerVipAdapter = new ManagerVipAdapter(Manager.this, listVip);
        listView_manager.setAdapter(managerVipAdapter);

    }

    //给商品信息添加适配器
    @SuppressLint("Range")
    public void adapterGoods(){
        listGoods = new ArrayList<>();
        Cursor customer = db.query("goods", null, null, null, null, null, null);
        if (customer.getCount() == 0) {
            Toast.makeText(Manager.this, "没有任何商品信息", Toast.LENGTH_SHORT).show();
        } else {
            while (customer.moveToNext()) {
                String gid = customer.getString(customer.getColumnIndex("gid"));
                String goodname = customer.getString(customer.getColumnIndex("goodname"));
                String price = customer.getString(customer.getColumnIndex("price"));
                String resnum = customer.getString(customer.getColumnIndex("resnum"));
                GoodsItem it = new GoodsItem(gid,goodname,price,resnum);
                listGoods.add(it);
            }
        }


        managerGoodsAdapter = new ManagerGoodsAdapter(Manager.this, listGoods);
        listView_manager.setAdapter(managerGoodsAdapter);
    }

    //给显示订单添加适配器
    @SuppressLint("Range")
    public void adapterOrders(){
        listOrders = new ArrayList<>();
        Cursor customer = db.query("t_order", null, "isPay = 1", null, null, null, null);
        if (customer.getCount() == 0) {
            Toast.makeText(Manager.this, "没有任何订单信息", Toast.LENGTH_SHORT).show();
        } else {
            while (customer.moveToNext()) {
                String oid = customer.getString(customer.getColumnIndex("oid"));
                String gid = customer.getString(customer.getColumnIndex("gid"));
                //cursor不能直接使用需要判断moveToNext()
                Cursor goods = db.query("goods", null, "gid = ?", new String[]{gid}, null, null, null);
                String goodname = "";
                if (goods.moveToNext()){
                    goodname = goods.getString(goods.getColumnIndex("goodname"));
                }

                String num = customer.getString(customer.getColumnIndex("num"));
                String vipnum = customer.getString(customer.getColumnIndex("vipnum"));
                OrderItem2 it = new OrderItem2(oid,goodname,num,vipnum,"");
                listOrders.add(it);
            }
        }


        managerOrderAdapter = new ManagerOrderAdapter(Manager.this, listOrders);
        listView_manager.setAdapter(managerOrderAdapter);

    }

    //给显示用户信息添加适配器
    @SuppressLint("Range")
    public void adapterUser(){
        listUser = new ArrayList<>();
        Cursor customer = db.query("user", null, null, null, null, null, null);
        if (customer.getCount() == 0) {
            Toast.makeText(Manager.this, "没有任何用户信息", Toast.LENGTH_SHORT).show();
        } else {
            while (customer.moveToNext()) {
                String uid = customer.getString(customer.getColumnIndex("uid"));
                String loginname = customer.getString(customer.getColumnIndex("loginname"));
                String password = customer.getString(customer.getColumnIndex("password"));
                String rid = customer.getString(customer.getColumnIndex("rid"));
                String username = customer.getString(customer.getColumnIndex("username"));
                UserItem it = new UserItem(uid,loginname,password,rid,username);
                listUser.add(it);
            }
        }


        managerUserAdapter = new ManagerUserAdapter(Manager.this, listUser);
        listView_manager.setAdapter(managerUserAdapter);
    }




}




