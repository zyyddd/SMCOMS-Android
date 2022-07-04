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

public class CheckOut  extends AppCompatActivity {

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
    private ListView listView_mygoods;

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


    private FloatingActionButton add_goods;
    private FloatingActionButton add_vip;
    private FloatingActionButton jiesuan;
    private Integer vipnum = null;

    private CheckOutAdapter c;
    private List<OrdersItem> list;
    private Button order_item_deleteButoon;
    private int sumprice = 0;
    private TextView zongjia;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check_out);

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
        new CommonToolbarColor().toolbarColorSet(CheckOut.this);

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
                        builder = new AlertDialogBuilder(CheckOut.this).build();
                        //   显示出该对话框
                        builder.show();

                        break;




                    default:
                        break;
                }
                return true;
            }
        });



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
                            imageUri = FileProvider.getUriForFile(CheckOut.this, "com.example.SMCOMS.fileprovider", outputImage);
                        }
                        // 启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);

                        break;

                    case R.id.floatingbutton_add_goods:
                        LayoutInflater userdialog = LayoutInflater.from(CheckOut.this);
                        final View myviewdialog = userdialog.inflate(R.layout.checkout_add_goods,null);
                        AlertDialog mydialog = new AlertDialog.Builder(CheckOut.this)
                                .setIcon(R.drawable.ic_launcher_background)
                                .setTitle("订单添加商品")
                                .setView(myviewdialog)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText goodsid = myviewdialog.findViewById(R.id.edit_goods_id);
                                        EditText goodsnum = myviewdialog.findViewById(R.id.edit_goods_sum);
                                        String goods_id = goodsid.getText().toString();
                                        String goods_num = goodsnum.getText().toString();
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("gid",goods_id);
                                        contentValues.put("num",goods_num);
                                        contentValues.put("vipnum",vipnum);
                                        Cursor goodscursor = db.query("goods", null, "gid = ?", new String[]{goods_id}, null, null, null);
                                        if (goodscursor.moveToNext()){
                                            db.insert("t_order",null,contentValues);
                                            String decsum = "update goods set resnum = resnum - ? where gid = ? ";
                                            db.execSQL(decsum,new Object[]{goods_num,goods_id});
                                            Toast.makeText(CheckOut.this, "添加成功", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(CheckOut.this, "不存在该商品", Toast.LENGTH_SHORT).show();
                                        }

                                        

                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).create();
                                mydialog.show();


                        break;
                        //添加会员
                    case R.id.floatingbutton_add_vip:
                        LayoutInflater userdialog2 = LayoutInflater.from(CheckOut.this);
                        final View myviewdialog2 = userdialog2.inflate(R.layout.checkout_add_vip,null);
                        AlertDialog mydialog2 = new AlertDialog.Builder(CheckOut.this)
                                .setIcon(R.drawable.ic_launcher_background)
                                .setTitle("添加会员信息")
                                .setView(myviewdialog2)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @SuppressLint("Range")
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText vip_id = myviewdialog2.findViewById(R.id.edit_vip_id);
                                        String vipid = vip_id.getText().toString();
                                        String vipname = "";
                                        Cursor cursor = db.query("customer", null, "vipnum = ?", new String[]{vipid}, null, null, null);
                                        if(cursor.moveToNext()){
                                            vipname = cursor.getString(cursor.getColumnIndex("vipname"));
                                            vipnum = Integer.parseInt(vipid);
                                            if (!vipname.equals("")){
                                                TextView textView_vipname = findViewById(R.id.vipname);
                                                textView_vipname.setText(vipname);
                                            }
                                            Toast.makeText(CheckOut.this, "会员登录成功", Toast.LENGTH_SHORT).show();

                                        }else {
                                            Toast.makeText(CheckOut.this, "不存在该会员用户", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).create();
                        mydialog2.show();


                        break;
                    //结算
                    case R.id.floatingbutton_jiesuan:
                        Cursor t_order_cursor;
                        if (vipnum != null) {
                            t_order_cursor = db.query("t_order", null, "vipnum = ? and isPay = 0", new String[]{vipnum.toString()}, null, null, null);
                        }else {
                            t_order_cursor = db.query("t_order", null, "vipnum is null and isPay = 0", new String[]{}, null, null, null);
                        }
                        if (t_order_cursor.getCount() == 0){
                            Toast.makeText(CheckOut.this,"当前无订单需要结算",Toast.LENGTH_SHORT).show();
                        }else {
                            while (t_order_cursor.moveToNext()){
                                String oid = t_order_cursor.getString(t_order_cursor.getColumnIndex("oid"));
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("isPay",1);
                                int t_order = db.update("t_order", contentValues, "oid = ?", new String[]{oid});
                            }
                            zongjia.setText("*");
                            TextView textView_vipname = findViewById(R.id.vipname);
                            textView_vipname.setText("游客");
                            Toast.makeText(CheckOut.this, "支付成功", Toast.LENGTH_SHORT).show();
                        }



                        break;







                    default:
                        break;
                }
            }
        };


        add_goods.setOnClickListener(listener);
        add_vip.setOnClickListener(listener);
        jiesuan.setOnClickListener(listener);
        circleImageView.setOnClickListener(listener);





        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.check_out_swipe_refresh);
        //进度条刷新旋钮的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //设置下拉刷新的监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        CheckOut.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapterListview();
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
                        Bitmap bitmap1 = CommonMethon.compressBoundsBitmap(CheckOut.this, imageUri, 200, 200);
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
        db = new CommonDatabase().getSqliteObject(CheckOut.this, "SuperMarket");

        listView_mygoods = findViewById(R.id.listview_mygoods);

        toolbar = findViewById(R.id.toolbar_checkout);

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerlayout_checkout);

        navigationView = findViewById(R.id.navigation_view);

        headview = navigationView.inflateHeaderView(R.layout.headlayout);


        textView_welcome = headview.findViewById(R.id.welcome_textview);


         circleImageView = (CircleImageView) headview.findViewById(R.id.circleimage);

        add_goods = findViewById(R.id.floatingbutton_add_goods);
        add_vip = findViewById(R.id.floatingbutton_add_vip);
        jiesuan = findViewById(R.id.floatingbutton_jiesuan);

        imageStore = new image_store();
        order_item_deleteButoon = findViewById(R.id.orders_item_delete);

        zongjia = findViewById(R.id.checkout_zongjia);



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


    /**
     * 配置listview的adpater
     */
    @SuppressLint("Range")
    public void adapterListview(){
        //配置adpater
        Cursor t_order_cursor;
        if (vipnum != null) {
            t_order_cursor = db.query("t_order", null, "vipnum = ? and isPay = 0", new String[]{vipnum.toString()}, null, null, null);
        }else {
            t_order_cursor = db.query("t_order", null, "vipnum is null and isPay = 0", new String[]{}, null, null, null);

        }
        list = new ArrayList<OrdersItem>();
        sumprice = 0;
        if (t_order_cursor.getCount() == 0){
            Toast.makeText(CheckOut.this, "订单中还没有任何商品", Toast.LENGTH_SHORT).show();
        }else {
            while(t_order_cursor.moveToNext()){
                String oid = t_order_cursor.getString(t_order_cursor.getColumnIndex("oid"));
                 String gid = t_order_cursor.getString(t_order_cursor.getColumnIndex("gid"));
                String goodname = null;
                String price = null;
                Cursor goods = db.query("goods", null, "gid = ?", new String[]{gid}, null, null, null);
                if (goods.moveToNext()){
                    goodname = goods.getString(goods.getColumnIndex("goodname"));
                    price = goods.getString(goods.getColumnIndex("price"));

                }

                String num  =t_order_cursor.getString(t_order_cursor.getColumnIndex("num"));
                sumprice += Integer.parseInt(price) * Integer.parseInt(num);
                String sumprice_S = sumprice+"";
                //setText如果直接传入int类型可能以为是传入的是资源文件的id导致找不到资源的错误
                zongjia.setText(sumprice_S);
                OrdersItem it = new OrdersItem(oid,goodname,price,num);
                list.add(it);
            }
        }

        c = new CheckOutAdapter(CheckOut.this,list);
        listView_mygoods.setAdapter(c);


    }



}




