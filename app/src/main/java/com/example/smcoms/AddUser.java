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

public class AddUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);

        EditText editloginname = findViewById(R.id.edit_loginname);
        EditText editpassword = findViewById(R.id.edit_password);
        EditText editusername = findViewById(R.id.edit_username);
        EditText editrid = findViewById(R.id.edit_rid);

        Button adduser = findViewById(R.id.button_user_add);
        Button finishadd = findViewById(R.id.button_user_finish_add);

        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddUser.this);
                builder.setIcon(R.drawable.ic_launcher_background)
                        .setTitle("提示")
                        .setMessage("你确定要添加吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CommonDatabase commonDatabase = new CommonDatabase();
                                SQLiteDatabase db =commonDatabase.getSqliteObject(AddUser.this,"SuperMarket");
                                String loginname = editloginname.getText().toString();
                                String password = editpassword.getText().toString();
                                String username = editusername.getText().toString();
                                String rid = editrid.getText().toString();
                                if (password.equals("")||(!rid.equals("1")&&!rid.equals("0"))){
                                    Toast.makeText(AddUser.this,"输入错误",Toast.LENGTH_SHORT).show();;
                                }else {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("loginname",loginname);
                                    contentValues.put("password",password);
                                    contentValues.put("username",username);
                                    contentValues.put("rid",rid);
                                    long customer = db.insert("user", null, contentValues);
                                    if (customer>0){
                                        Toast.makeText(AddUser.this, "添加成功", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(AddUser.this, "添加失败", Toast.LENGTH_SHORT).show();
                                    }
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