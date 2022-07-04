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

public class ChangeUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);

        final TextView uid = findViewById(R.id.edit_user_uid);
        final EditText loginname = findViewById(R.id.edit_user_loginname);
        final EditText password = findViewById(R.id.edit_user_password);
        final EditText username = findViewById(R.id.edit_user_username);
        final EditText rid = findViewById(R.id.edit_user_rid);
        Button button_user_change = findViewById(R.id.button_user_change);
        Button button_user_back = findViewById(R.id.button_user_back);
        //获取进来这个活动的intent
        final Intent intent = getIntent();

        uid.setText(intent.getStringExtra("uid"));
        loginname.setText(intent.getStringExtra("loginname"));
        password.setText(intent.getStringExtra("password"));
        rid.setText(intent.getStringExtra("rid"));
        username.setText(intent.getStringExtra("username"));

        //初始化数据库对象
        CommonDatabase commonDatabase = new CommonDatabase();
        SQLiteDatabase db = commonDatabase.getSqliteObject(ChangeUser.this, "SuperMarket");

        View.OnClickListener listener = new View.OnClickListener(

        ){
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.button_user_change:
                        if (password.getText().toString().equals("")){
                            Toast.makeText(ChangeUser.this, "昵称不能修改为空", Toast.LENGTH_SHORT).show();
                            password.setText(intent.getStringExtra("password"));
                        }else if (!rid.getText().toString().equals("1") && !rid.getText().toString().equals("0")){
                            Toast.makeText(ChangeUser.this,"角色输入错误", Toast.LENGTH_SHORT).show();
                            rid.setText(intent.getStringExtra("rid"));
                        }
                        else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangeUser.this);
                            builder.setIcon(R.drawable.ic_launcher_background)
                                    .setTitle("提示")
                                    .setMessage("你确定要修改吗")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("loginname",loginname.getText().toString());
                                            contentValues.put("password",password.getText().toString());
                                            contentValues.put("rid",rid.getText().toString());
                                            contentValues.put("username",username.getText().toString());
                                            db.update("user",contentValues,"uid = ?",new String[]{intent.getStringExtra("uid")});
                                            Toast.makeText(ChangeUser.this, "修改成功", Toast.LENGTH_SHORT).show();
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
                    case R.id.button_user_back:
                        finish();
                        break;
                    default:
                }
            }
        };
        button_user_change.setOnClickListener(listener);
        button_user_back.setOnClickListener(listener);




    }
}