package com.cr97.myassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by chuah_000 on 29/6/2017.
 */


public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    int p=0;

    private static final String DATABASE_NAME = "DiaryDB";
    private static final String TableName = "myDiary",
            TableId = "_id",
            TableTitle = "title",
            TableNote = "note",
            TableDate = "date",
            TableDay = "day",
            TableTime = "time",
            TableMood = "mood",
            TableWeather = "weather";

    private static final String PlanTable = "myPlan",
            PlanId = "_planid",
            PlanTask = "task";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TableName + "("
                + TableId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TableTitle + " TEXT NOT NULL,"
                + TableNote + " TEXT NOT NULL,"
                + TableDate + " TEXT,"
                + TableDay + " TEXT,"
                + TableTime + " TEXT,"
                + TableMood + " TEXT,"
                + TableWeather + " TEXT)");

        db.execSQL("CREATE TABLE " + PlanTable + "("
                + PlanId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PlanTask + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        db.execSQL("DROP TABLE IF EXISTS " + PlanTable);


        onCreate(db);
    }

    // Inserting product into table
    public void InsertDiary(Assistant assistant) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues(); // create an instance of ContentValues
        values.put(TableTitle, assistant.get_title()); // parameters are (key, value) but the key = columnName in our Database Table
        values.put(TableNote, assistant.get_note());

        values.put(TableDate, assistant.get_date());
        values.put(TableDay, assistant.get_day());
        values.put(TableTime, assistant.get_time());

        values.put(TableMood , assistant.get_mood());
        values.put(TableWeather,assistant.get_weather());

        db.insert(TableName, null, values);
        db.close();
    }


    public void insertplan(Plan plan) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PlanTask, plan.get_task());

        db.insert(PlanTable, null, cv);
        db.close();
    }

//        public void EditNote(int id, String title, String note, String remark) {
//
//            ContentValues values = new ContentValues(); // create an instance of ContentValues
//            values.put("title", title); // parameters are (key, value) but the key = columnName in our Database Table
//            values.put("note", note);
//
//            //db.update removes the risk of SQL Injection
//            db.update(TableName, values, "_id = ?", new String[]{"" + id});
//        }

    public int getDiaryCount(){
       SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TableName, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    public int getPlanCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PlanTable, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    public void deleteDiary(Assistant assistant) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName, TableTitle + "=?", new String[] { String.valueOf(assistant.get_title()) });
        db.close();
    }

    public void deleteplan(Plan plan) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PlanTable, PlanTask + "=?", new String[] { String.valueOf(plan.get_task()) });
        db.close();
    }

    // Showing all rows from table
//    public void showNote(List<Integer> ids_list, List<String> titles_list, List<String> notes_list, List<String> remark_list, List<String> created_list, List<String> day_list, List<String> time_list) {
//
//        // this will execute select query
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TableName + " ORDER BY _id DESC", null);
//        // getting data from all rows
//        while (cursor.moveToNext()) {
//            //filling up the arraylist with database coloumns
//            ids_list.add(cursor.getInt(0));
//            titles_list.add(cursor.getString(1));
//            notes_list.add(cursor.getString(2));
//            remark_list.add(cursor.getString(3));
//            created_list.add(cursor.getString(4));
//            day_list.add(cursor.getString(5));
//            time_list.add(cursor.getString(6));
//        }
//
//    }

    public List<Assistant> getAllDiary(){
        List<Assistant> assistantList = new ArrayList<Assistant>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TableName, null);

        if (cursor.moveToFirst()) {
            do {
                assistantList.add(new Assistant(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)));

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return assistantList;

    }

    public List<Plan> getAllPlan(){
        List<Plan> planList = new ArrayList<Plan>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PlanTable, null);

        if (cursor.moveToFirst()) {
            do {
                planList.add(new Plan(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return planList;
    }
//    public void showPlan(List<Integer> pids_list, List<String> task_list) {
//
//        // this will execute select query
//        Cursor cursor = db.rawQuery("SELECT * FROM " + PlanTable + " ORDER BY _planid DESC", null);
//        // getting data from all rows
//        while (cursor.moveToNext()) {
//            //filling up the arraylist with database coloumns
//            pids_list.add(cursor.getInt(0));
//            task_list.add(cursor.getString(1));
//        }
//    }


}

