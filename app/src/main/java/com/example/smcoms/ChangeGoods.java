package com.example.smcoms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smcoms.database.CommonDatabase;

public class ChangeGoods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_goods);

        final TextView gid = findViewById(R.id.edit_goods_gid);
        final EditText goodname = findViewById(R.id.edit_goods_goodname);
        final EditText goodprice = findViewById(R.id.edit_goods_goodprice);
        final EditText goodresnum = findViewById(R.id.edit_goods_goodresnum);
        Button button_goods_change = findViewById(R.id.button_goods_change);
        Button button_goods_back = findViewById(R.id.button_goods_back);
        //获取进来这个活动的intent
        final Intent intent = getIntent();

        gid.setText(intent.getStringExtra("gid"));
        goodname.setText(intent.getStringExtra("goodname"));
        goodprice.setText(intent.getStringExtra("price"));
        goodresnum.setText(intent.getStringExtra("resnum"));

        //初始化数据库对象
        CommonDatabase commonDatabase = new CommonDatabase();
        SQLiteDatabase db = commonDatabase.getSqliteObject(ChangeGoods.this, "SuperMarket");

        View.OnClickListener listener = new View.OnClickListener(

        ){
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.button_goods_change:
                        if (goodname.getText().toString().equals("")){
                            Toast.makeText(ChangeGoods.this, "昵称不能修改为空", Toast.LENGTH_SHORT).show();
                            goodname.setText(intent.getStringExtra("goodname"));
                        }else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangeGoods.this);
                            builder.setIcon(R.drawable.ic_launcher_background)
                                    .setTitle("提示")
                                    .setMessage("你确定要修改吗")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("gid",gid.getText().toString());
                                            contentValues.put("goodname",goodname.getText().toString());
                                            contentValues.put("price",goodprice.getText().toString());
                                            contentValues.put("resnum",goodresnum.getText().toString());
                                            db.update("goods",contentValues,"gid = ?",new String[]{intent.getStringExtra("gid")});
                                            Toast.makeText(ChangeGoods.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create();
                            builder.show();


                        }

                        break;
                    case R.id.button_goods_back:
                        finish();
                        break;
                    default:
                }
            }
        };
        button_goods_change.setOnClickListener(listener);
        button_goods_back.setOnClickListener(listener);




    }
}