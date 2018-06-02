package com.example.mohammad.workclock2.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * Created by Mohammad on 12/10/2017.
 */

public class DataBase extends SQLiteOpenHelper {


    private static final int Version = 1;
    private static final String NameDataBase = "WorckClockDB";


    public DataBase(Context context) {
        super(context, NameDataBase, null, Version);
    }

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreatQuery_Project = "create table Project ([ProjectId] INTEGER PRIMARY KEY NOT NULL, [Title] TEXT NOT NULL, [SumDT] TEXT , [HasHalfTraffic] INTEGER NOT NULL);";
        String CreatQuery_Clock = "create table Clock ([ClockId] INTEGER PRIMARY KEY NOT NULL, [ProjectId] INTEGER NOT NULL, [Date] TEXT NOT NULL, [Time] TEXT NOT NULL, [TypeInsert] INTEGER NOT NULL, [IsChanged] INTEGER NOT NULL);";

        db.execSQL(CreatQuery_Project);
        db.execSQL(CreatQuery_Clock);

        //String insert = "insert into Project (Title, SumDT, HasHalfTraffic) values ('mohammad', '00:00:00', 1)";
        //db.execSQL(insert);
        //db.execSQL(insert);
        //db.execSQL(insert);

//        String insertClock = "insert into Clock (ProjectId, Date, Time, TypeInsert, IsChanged) values (1, '1397/01/02', '12:00', 1, 0)";
//        String insertClock2 = "insert into Clock (ProjectId, Date, Time, TypeInsert, IsChanged) values (1, '1397/01/02', '12:01', 1, 0)";
        //String insertClock3 = "insert into Clock (ProjectId, Date, Time, TypeInsert, IsChanged) values (1, '2018/03/25', '14:23:00', 0, 0)";
//        db.execSQL(insertClock);
        //Log.d("test", "test");
//        db.execSQL(insertClock2);
        //db.execSQL(insertClock3);




        //db.execSQL(insertClock);
        //db.execSQL(insertClock);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
