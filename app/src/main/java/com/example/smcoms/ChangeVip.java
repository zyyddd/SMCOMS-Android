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

import org.w3c.dom.Text;

public class ChangeVip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_vip);
        final TextView vipnum = findViewById(R.id.edit_vip_vipnum);
        final EditText vipname = findViewById(R.id.edit_vip_vipname);
        Button button_vip_change = findViewById(R.id.button_vip_change);
        Button button_vip_back = findViewById(R.id.button_vip_back);
        //获取进来这个活动的intent
        final Intent intent = getIntent();

        vipnum.setText(intent.getStringExtra("vipnum"));
        vipname.setText(intent.getStringExtra("vipname"));

        //初始化数据库对象
        CommonDatabase commonDatabase = new CommonDatabase();
        SQLiteDatabase db = commonDatabase.getSqliteObject(ChangeVip.this, "SuperMarket");

        View.OnClickListener listener = new View.OnClickListener(

        ){
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.button_vip_change:
                        if (vipname.getText().toString().equals("")){
                            Toast.makeText(ChangeVip.this, "昵称不能修改为空", Toast.LENGTH_SHORT).show();
                            vipname.setText(intent.getStringExtra("vipname"));
                        }else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangeVip.this);
                            builder.setIcon(R.drawable.ic_launcher_background)
                                    .setTitle("提示")
                                    .setMessage("你确定要修改吗")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("vipnum",vipnum.getText().toString());
                                            contentValues.put("vipname",vipname.getText().toString());
                                            db.update("customer",contentValues,"vipnum = ?",new String[]{intent.getStringExtra("vipnum")});
                                            Toast.makeText(ChangeVip.this, "修改成功", Toast.LENGTH_SHORT).show();
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
                    case R.id.button_vip_back:
                        finish();
                        break;
                    default:
                }
            }
        };
        button_vip_change.setOnClickListener(listener);
        button_vip_back.setOnClickListener(listener);



    }
}