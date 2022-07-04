package com.example.smcoms.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;


public class image_store {
    public image_store() {

    }

    public void update(Bitmap bitmap, SQLiteDatabase db, String id) {
        //bitmapToBytes
        byte[] by = bitmapToBytes(bitmap);
        ContentValues contentValues = new ContentValues();
        contentValues.put("IMAGE", by);
        int personal_resource = db.update("personal_resource", contentValues, "uid = ? ", new String[]{id});
        System.out.println("result is " + personal_resource);

    }

    public Bitmap getBmp(SQLiteDatabase db, String id) {
        Bitmap bitmap = null;

        Cursor cursor = db.query("personal_resource", null, "uid = ?", new String[]{id}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") byte[] bytes = cursor.getBlob(cursor.getColumnIndex("IMAGE"));
                if (bytes != null) {
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
                }
            }
        }


        return bitmap;
    }


    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        //除了PNG还有很多常见格式，如jpeg等。
        return os.toByteArray();
    }
}
