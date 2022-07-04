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

public class AddVip extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vip);

        EditText editvipnum = findViewById(R.id.edit_vipnum);
        EditText editvipname = findViewById(R.id.edit_vipname);

        Button addvip = findViewById(R.id.button_vip_add);
        Button finishadd = findViewById(R.id.button_finish_add);

        addvip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddVip.this);
                builder.setIcon(R.drawable.ic_launcher_background)
                        .setTitle("提示")
                        .setMessage("你确定要添加吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CommonDatabase commonDatabase = new CommonDatabase();
                                SQLiteDatabase db =commonDatabase.getSqliteObject(AddVip.this,"SuperMarket");
                                String vipnum = editvipnum.getText().toString();
                                String vipname = editvipname.getText().toString();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("vipnum",vipnum);
                                contentValues.put("vipname",vipname);
                                long customer = db.insert("customer", null, contentValues);
                                if (customer>0){
                                    Toast.makeText(AddVip.this, "添加成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(AddVip.this, "添加失败", Toast.LENGTH_SHORT).show();
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