package com.example.smcoms;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.smcoms.database.CommonDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class CheckOutAdapter extends BaseAdapter {
    Context mContext ;
    List<OrdersItem> mList;
    ViewHolder mViewHolder;
    SQLiteDatabase db;
    public CheckOutAdapter(Context mContext, List<OrdersItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final OrdersItem it = mList.get(i);
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.checkout_order_item,viewGroup,false);

            mViewHolder = new ViewHolder();
            mViewHolder.oid = (TextView) view.findViewById(R.id.orders_item_oid);
            mViewHolder.goodsname = (TextView) view.findViewById(R.id.orders_item_goodsname);
            mViewHolder.goodsprice = (TextView) view.findViewById(R.id.orders_item_goodsprice);
            mViewHolder.goodsnum = (EditText) view.findViewById(R.id.orders_item_goodsnum);
            view.setTag(mViewHolder);

        }else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        mViewHolder.oid.setText(it.getOid());
        mViewHolder.goodsname.setText(it.getGoodsname());
        mViewHolder.goodsprice.setText(it.getGoodsprice());
        mViewHolder.goodsnum.setText(it.getGoodsnum());

        Button order_item_deleteButton = view.findViewById(R.id.orders_item_delete);
        TextView orderitem_oid = view.findViewById(R.id.orders_item_oid);
        String order_item_oid = orderitem_oid.getText().toString();
        db = new CommonDatabase().getSqliteObject(mContext,"SuperMarket");
        order_item_deleteButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
             builder.setIcon(R.drawable.ic_launcher_background)
                     .setTitle("提示")
                     .setMessage("你确定要删除吗")
                     .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             int t_order = db.delete("t_order", "oid = ?", new String[]{order_item_oid});
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

        EditText order_item_goodsnum = view.findViewById(R.id.orders_item_goodsnum);
        order_item_goodsnum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                EditText order_item_goodsnum = view.findViewById(R.id.orders_item_goodsnum);
                String item_goodsnum = order_item_goodsnum.getText().toString();
                ContentValues contentValues = new ContentValues();
                contentValues.put("num",item_goodsnum);
                int t_order = db.update("t_order", contentValues, "oid = ?", new String[]{order_item_oid});


            }
        });


        return view;
    }

    class ViewHolder{
        TextView oid;
        TextView goodsname;
        TextView goodsprice;
        EditText goodsnum;
    }
}
