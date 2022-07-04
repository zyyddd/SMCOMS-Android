package com.example.smcoms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.smcoms.database.CommonDatabase;

import java.util.List;

public class ManagerUserAdapter extends BaseAdapter {
    Context mContext ;
    List<UserItem> mList;
    ViewHolder mViewHolder;
    SQLiteDatabase db;

    public ManagerUserAdapter(Context mContext, List<UserItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserItem it = mList.get(position);
        if (convertView ==null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.manager_user_item, parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.uid = convertView.findViewById(R.id.manager_user_uid);
            mViewHolder.loginname = convertView.findViewById(R.id.manager_user_loginname);
            mViewHolder.password = convertView.findViewById(R.id.manager_user_password);
            mViewHolder.username = convertView.findViewById(R.id.manager_user_username);
            mViewHolder.rid = convertView.findViewById(R.id.manager_user_rid);
            convertView.setTag(mViewHolder);


        }else {
            mViewHolder = (ViewHolder)   convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChangeUser.class);
                intent.putExtra("uid", it.getUid());
                intent.putExtra("loginname", it.getLoginname());
                intent.putExtra("password", it.getPassword());
                intent.putExtra("rid", it.getRid());
                intent.putExtra("username", it.getUsername());
                mContext.startActivity(intent);



            }
        });


        mViewHolder.uid.setText(it.getUid());
        mViewHolder.loginname.setText(it.getLoginname());
        mViewHolder.password.setText(it.getPassword());
        mViewHolder.username.setText(it.getUsername());
        mViewHolder.rid.setText(it.getRid());


        Button manager_user_delete = convertView.findViewById(R.id.manager_user_delete);
        TextView user_uid = convertView.findViewById(R.id.manager_user_uid);
        String uid = user_uid.getText().toString();
        db = new CommonDatabase().getSqliteObject(mContext,"SuperMarket");
        manager_user_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.drawable.ic_launcher_background)
                        .setTitle("提示")
                        .setMessage("你确定要删除吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int user = db.delete("user", "uid = ?", new String[]{uid});
                                if (user>0){
                                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(mContext, "删除失败，不存在该订单", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                builder.show();
            }
        });


        return convertView;
    }



    class ViewHolder{
        TextView uid;
        TextView loginname;
        TextView password;
        TextView username;
        TextView rid;
    }
}
