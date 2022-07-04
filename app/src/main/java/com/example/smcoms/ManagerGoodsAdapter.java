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

public class ManagerGoodsAdapter extends BaseAdapter {
    Context mContext ;
    List<GoodsItem> mList;
    ViewHolder mViewHolder;
    SQLiteDatabase db;

    public ManagerGoodsAdapter(Context mContext, List<GoodsItem> mList) {
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
        final GoodsItem it = mList.get(position);
        if (convertView ==null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.manager_goods_item, parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.manager_gid = convertView.findViewById(R.id.manager_goods_gid);
            mViewHolder.manager_goodname = convertView.findViewById(R.id.manager_goods_goodname);
            mViewHolder.manager_goodsprice = convertView.findViewById(R.id.manager_goods_price);
            mViewHolder.manager_goodsresnum = convertView.findViewById(R.id.manager_goods_resnum);
            convertView.setTag(mViewHolder);


        }else {
            mViewHolder = (ViewHolder)   convertView.getTag();
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChangeGoods.class);
                intent.putExtra("gid", it.getGid());
                intent.putExtra("goodname", it.getGoodname());
                intent.putExtra("price", it.getPrice());
                intent.putExtra("resnum", it.getResnum());
                mContext.startActivity(intent);
            }
        });


        mViewHolder.manager_gid.setText(it.getGid());
        mViewHolder.manager_goodname.setText(it.getGoodname());
        mViewHolder.manager_goodsprice.setText(it.getPrice());
        mViewHolder.manager_goodsresnum.setText(it.getResnum());


        Button manager_goods_delete = convertView.findViewById(R.id.manager_goods_delete);
        TextView goods_gid = convertView.findViewById(R.id.manager_goods_gid);
        String gid = goods_gid.getText().toString();
        db = new CommonDatabase().getSqliteObject(mContext,"SuperMarket");
        manager_goods_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.drawable.ic_launcher_background)
                        .setTitle("提示")
                        .setMessage("你确定要删除吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int t_order = db.delete("goods", "gid = ?", new String[]{gid});
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
        TextView manager_gid;
        TextView manager_goodname;
        TextView manager_goodsprice;
        TextView manager_goodsresnum;


    }
}
