package com.example.smcoms;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.smcoms.database.CommonDatabase;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    //state选择是收银员还是管理员
    private String state = "" ;
    private SQLiteDatabase db;
    private EditText edit_login_zhanghao;
    private EditText edit_login_password;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = new Intent(Login.this,Music.class);
        /*0开始播放音乐*/
        intent.putExtra("action",0);
        startService(intent);

        //状态栏融为一体的方法
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        edit_login_zhanghao = findViewById(R.id.edit_login_zhanghao);
        edit_login_password = findViewById(R.id.edit_login_password);

        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(Login.this,"SuperMarket");
        //轮子TextputLayout
        final TextInputLayout textInputLayout_p = findViewById(R.id.textinput_p);
        RadioGroup radioGroup = findViewById(R.id.login_radiogroup);
        ImageButton imageButton = findViewById(R.id.imagebutton);

        //眼睛睁开闭上
        edit_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        imageButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) //按下重新设置背景图片

                {

                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    edit_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示


                } else if (event.getAction() == MotionEvent.ACTION_UP) //松手恢复原来图片

                {

                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                    edit_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏

                }

                return false;

            }
        });

        //单选按钮监听器
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radiobutton_shouyinyuan:


                        state = "shouyinyuan";

                        break;
                    case R.id.radiobutton_guanliyuan:


                        state = "guanliyuan";
                        break;

                    default:
                        break;

                }
            }
        });


        Button button_login = findViewById(R.id.button_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                String account_login = edit_login_zhanghao.getText().toString();
                String password_login = edit_login_password.getText().toString();
                if (state == "guanliyuan") {
                    String true_password = "";
                    String uid = "";

                    Cursor cursor = db.query("user", null, "loginname = ? and rid = 0", new String[]{account_login}, null, null, null);

                    while (cursor.moveToNext()) {
                        true_password = cursor.getString(cursor.getColumnIndex("password"));
                        uid = cursor.getString(cursor.getColumnIndex("uid"));

                    }

                    if (password_login.equals(true_password)) {
                        Intent intent = new Intent(Login.this, Manager.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Login.this, "密码错误！", Toast.LENGTH_SHORT).show();
                    }
                } else if (state.equals("shouyinyuan")) {


                    String true_password = "";
                    String uid = "";
                    Cursor cursor = db.query("user", null, "loginname = ? and rid =1", new String[]{account_login}, null, null, null);

                    while (cursor.moveToNext()) {
                        true_password = cursor.getString(cursor.getColumnIndex("password"));
                        uid = cursor.getString(cursor.getColumnIndex("uid"));
                    }

                    if (password_login.equals(true_password) && true_password.equals("") == false) {
                        Intent intent = new Intent(Login.this, CheckOut.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        Toast.makeText(Login.this, "登陆成功！", Toast.LENGTH_SHORT).show();

                        finish();

                    } else {
                        Toast.makeText(Login.this, "密码错误！", Toast.LENGTH_SHORT).show();
                    }


                }
                else {
                    Toast.makeText(Login.this, "您还没有选择任何登录类型！", Toast.LENGTH_SHORT).show();
                }

            }
        });


        edit_login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_login_password.getText().toString().equals("")) {
                    textInputLayout_p.setError("不能为空！");
                } else {
                    if (textInputLayout_p.getCounterMaxLength() < edit_login_password.length()) {
                        textInputLayout_p.setError("超出字数限制！");
                    } else {
                        textInputLayout_p.setErrorEnabled(false);
                    }
                }


            }
        });


    }
}