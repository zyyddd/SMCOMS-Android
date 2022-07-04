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

public class ManagerOrderAdapter extends BaseAdapter {
    Context mContext ;
    List<OrderItem2> mList;
    ViewHolder mViewHolder;
    SQLiteDatabase db;

    public ManagerOrderAdapter(Context mContext, List<OrderItem2> mList) {
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
        final OrderItem2 it = mList.get(position);
        if (convertView ==null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.manager_order_item, parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.manager_vipnum = convertView.findViewById(R.id.manager_orders_vipnum);
            mViewHolder.manager_oid = convertView.findViewById(R.id.manager_orders_oid);
            mViewHolder.manager_goodname = convertView.findViewById(R.id.manager_orders_goodname);
            mViewHolder.manager_num = convertView.findViewById(R.id.manager_orders_num);
            mViewHolder.manager_ispay = convertView.findViewById(R.id.manager_orders_ispay);
            convertView.setTag(mViewHolder);


        }else {
            mViewHolder = (ViewHolder)   convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mViewHolder.manager_vipnum.setText(it.getVipnum());
        mViewHolder.manager_oid.setText(it.getOid());
        mViewHolder.manager_goodname.setText(it.getGoodname());
        mViewHolder.manager_num.setText(it.getNum());


        Button manager_vip_delete = convertView.findViewById(R.id.manager_orders_delete);
        TextView orders_oid = convertView.findViewById(R.id.manager_orders_oid);
        String oid = orders_oid.getText().toString();
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
                                int t_order = db.delete("t_order", "oid = ?", new String[]{oid});
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
        TextView manager_oid;
        TextView manager_goodname;
        TextView manager_num;
        TextView manager_vipnum;
        TextView manager_ispay;
    }
}
