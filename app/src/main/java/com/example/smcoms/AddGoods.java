package com.example.smcoms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smcoms.database.CommonDatabase;

public class AddGoods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);

        EditText editgoodname = findViewById(R.id.edit_goodname);
        EditText editgoodprice = findViewById(R.id.edit_price);
        EditText editgoodresnum = findViewById(R.id.edit_resnum);

        Button addgoods = findViewById(R.id.button_goods_add);
        Button finishadd = findViewById(R.id.button_goods_finish_add);

        addgoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddGoods.this);
                builder.setIcon(R.drawable.ic_launcher_background)
                        .setTitle("提示")
                        .setMessage("你确定要添加吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CommonDatabase commonDatabase = new CommonDatabase();
                                SQLiteDatabase db =commonDatabase.getSqliteObject(AddGoods.this,"SuperMarket");
                                String goodname = editgoodname.getText().toString();
                                String goodsprice = editgoodprice.getText().toString();
                                String goodresnum = editgoodresnum.getText().toString();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("goodname",goodname);
                                contentValues.put("price",goodsprice);
                                contentValues.put("resnum",goodresnum);
                                long customer = db.insert("goods", null, contentValues);
                                if (customer>0){
                                    Toast.makeText(AddGoods.this, "添加成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(AddGoods.this, "添加失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                builder.show();


            }
        });

        finishadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}