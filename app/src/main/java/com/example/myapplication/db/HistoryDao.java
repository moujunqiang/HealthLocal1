package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.bean.HealthHistoryBean;

import java.util.ArrayList;
import java.util.List;


public class HistoryDao {
    Context context;
    HistoryDBHelper dbHelper;

    public HistoryDao(Context context) {
        this.context = context;
        dbHelper = new HistoryDBHelper(context, "health.db", null, 1);
    }

    public void insertNote(HealthHistoryBean bean) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("health_name", bean.getName());
        cv.put("health_time", bean.getTime());
        cv.put("health_tem", bean.getTem());
        cv.put("health_address", bean.getLocation());

        sqLiteDatabase.insert("health", null, cv);
    }

    public int DeleteNote(int id) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int ret = 0;
        ret = sqLiteDatabase.delete("health", "health_id=?", new String[]{id + ""});
        return ret;
    }

    public Cursor getAllData() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String sql = "select * from health";
        return sqLiteDatabase.rawQuery(sql, null);
    }

    public void updateNote(HealthHistoryBean bean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("health_name", bean.getName());
        cv.put("health_time", bean.getTime());
        cv.put("health_tem", bean.getTem());
        cv.put("health_address", bean.getLocation());

        db.update("health", cv, "health_id=?", new String[]{bean.getId() + ""});
        db.close();
    }


    public List<HealthHistoryBean> queryTypesAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<HealthHistoryBean> noteList = new ArrayList<>();
        HealthHistoryBean note;
        Cursor cursor = null;
        String sql = "select * from health";

        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            note = new HealthHistoryBean();
            note.setId(cursor.getInt(cursor.getColumnIndex("health_id")));
            note.setName(cursor.getString(cursor.getColumnIndex("health_name")));
            note.setTime(cursor.getString(cursor.getColumnIndex("health_time")));
            note.setTem(cursor.getString(cursor.getColumnIndex("health_tem")));
            note.setLocation(cursor.getString(cursor.getColumnIndex("health_address")));

            noteList.add(note);
        }

        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }

        return noteList;
    }



}
