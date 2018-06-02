package com.example.mohammad.workclock2.DB;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.mohammad.workclock2.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Mohammad on 12/10/2017.
 */

public class DB  {
    private DataBase database;
    private Context context;

    public DB(Context context){
        this.database = new DataBase(context);
        this.context = context;
    }

    public long Insert(String TableName,ContentValues CV){
        SQLiteDatabase db = database.getWritableDatabase();
        try{

            long Identifire = db.insert(TableName, null, CV);
            db.close();
            Toast.makeText(this.context, R.string.Message_Insert_Success, Toast.LENGTH_SHORT).show();
            return Identifire;
        }
        catch (Exception ex){
            db.close();
            Log.d("Error_DB", ex.toString());
            Toast.makeText(this.context, R.string.Message_Insert_UnSuccess, Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    public<E> List<E> getItems(Class clazz,String Query_Select){
        SQLiteDatabase db = database.getReadableDatabase();
        List<E> Result = new ArrayList<E>();

        Cursor cursor = db.rawQuery(Query_Select, null);
        if(cursor.moveToFirst()){
            do{
                try {
                    Result.add((E) clazz.getDeclaredConstructor(Cursor.class).newInstance(cursor));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }while(cursor.moveToNext());
        }
        db.close();
        return Result;
    }

    public<E> E getItem(Class clazz,String Query_Select) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query_Select, null);
        cursor.moveToFirst();
        db.close();
        return (E) clazz.getDeclaredConstructor(Cursor.class).newInstance(cursor);
    }

    public boolean update(String TableName, String Where, ContentValues CV){
        SQLiteDatabase db = database.getWritableDatabase();

        try {
            db.update(TableName, CV, Where, null);
            db.close();
            Toast.makeText(this.context, R.string.Message_Update_Success, Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (Exception ex){
            Toast.makeText(this.context, R.string.Message_Update_UnSuccess, Toast.LENGTH_SHORT).show();
            db.close();
            Log.d("Error_DB", ex.toString());
            return false;
        }

    }

    public boolean Delete(String TableName, String Where){
        SQLiteDatabase db = database.getWritableDatabase();
        try{
            db.delete(TableName, Where, null);
            Toast.makeText(this.context, R.string.Message_Delete_Success, Toast.LENGTH_SHORT).show();
            db.close();
            return true;
        }catch (Exception ex){
            Toast.makeText(this.context, R.string.Message_Delete_UnSuccess, Toast.LENGTH_SHORT).show();
            db.close();
            return  false;
        }
    }


    public Cursor RunQuery(String Query){

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        db.close();
        return cursor;
    }

}
