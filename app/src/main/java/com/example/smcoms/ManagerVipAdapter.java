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

import org.w3c.dom.Text;

import java.util.List;

public class ManagerVipAdapter extends BaseAdapter {
    Context mContext ;
    List<VipItem> mList;
    ViewHolder mViewHolder;
    SQLiteDatabase db;


    public ManagerVipAdapter(Context mContext, List<VipItem> mList) {
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
        final VipItem it = mList.get(position);
        if (convertView ==null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.manager_vip_item, parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.manager_vipnum = convertView.findViewById(R.id.manager_vipnum);
            mViewHolder.manager_vipname = convertView.findViewById(R.id.manager_vipname);
            convertView.setTag(mViewHolder);


        }else {
            mViewHolder = (ViewHolder)   convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChangeVip.class);
                intent.putExtra("vipnum", it.getVipnum());
                intent.putExtra("vipname", it.getVipname());
                mContext.startActivity(intent);
            }
        });
        mViewHolder.manager_vipnum.setText(it.getVipnum());
        mViewHolder.manager_vipname.setText(it.getVipname());


            Button manager_vip_delete = convertView.findViewById(R.id.manager_vip_delete);
            TextView vip_num = convertView.findViewById(R.id.manager_vipnum);
            String vipnum = vip_num.getText().toString();
            db = new CommonDatabase().getSqliteObject(mContext,"SuperMarket");
            manager_vip_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setIcon(R.drawable.ic_launcher_background)
                            .setTitle("提示")
                            .setMessage("你确定要删除吗")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int t_order = db.delete("customer", "vipnum = ?", new String[]{vipnum});
                                    if (t_order>0){
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
        TextView manager_vipnum;
        TextView manager_vipname;
    }
}
