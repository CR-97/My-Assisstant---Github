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
    //database version
    private static final int DATABASE_VERSION = 1;
    //database name
    private static final String DATABASE_NAME = "DiaryDB";
    // diary table
    private static final String TableName = "myDiary",
            TableId = "_id",
            TableTitle = "title",
            TableNote = "note",
            TableDate = "date",
            TableDay = "day",
            TableTime = "time",
            TableMood = "mood",
            TableWeather = "weather";
    //plan table
    private static final String PlanTable = "myPlan",
            PlanId = "_planid",
            PlanTask = "task";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create diary table
        db.execSQL("CREATE TABLE " + TableName + "("
                + TableId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TableTitle + " TEXT NOT NULL,"
                + TableNote + " TEXT NOT NULL,"
                + TableDate + " TEXT,"
                + TableDay + " TEXT,"
                + TableTime + " TEXT,"
                + TableMood + " TEXT,"
                + TableWeather + " TEXT)");
        //create plan table
        db.execSQL("CREATE TABLE " + PlanTable + "("
                + PlanId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PlanTask + " TEXT)");
    }

    //upgrade database function
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        db.execSQL("DROP TABLE IF EXISTS " + PlanTable);


        onCreate(db);
    }

    // Inserting diary into table
    public void InsertDiary(Assistant assistant) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues(); // create an instance of ContentValues
        values.put(TableTitle, assistant.get_title());
        values.put(TableNote, assistant.get_note());

        values.put(TableDate, assistant.get_date());
        values.put(TableDay, assistant.get_day());
        values.put(TableTime, assistant.get_time());

        values.put(TableMood , assistant.get_mood());
        values.put(TableWeather,assistant.get_weather());

        db.insert(TableName, null, values);
        db.close();
    }

    //insert plan to table
    public void insertplan(Plan plan) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PlanTask, plan.get_task());

        db.insert(PlanTable, null, cv);
        db.close();
    }

    //get diary count
    public int getDiaryCount(){
       SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TableName, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    //get plan count
    public int getPlanCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PlanTable, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    //delete diary in table
    public void deleteDiary(Assistant assistant) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName, TableTitle + "=?", new String[] { String.valueOf(assistant.get_title()) });
        db.close();
    }

    //delete plan in table
    public void deleteplan(Plan plan) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PlanTable, PlanTask + "=?", new String[] { String.valueOf(plan.get_task()) });
        db.close();
    }


    //get all diary data
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

    //get all plan data
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

}

