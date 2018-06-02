package com.example.mohammad.workclock2.Utility;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.myEnum.myEnum;
import com.example.mohammad.workclock2.myEnum.myEnum.TypeInOut;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.example.mohammad.workclock2.myEnum.myEnum.TypeInOut.*;

/**
 * Created by Mohammad on 3/22/2018.
 */

public class Utility {
    static int  sumDayMiladiMonthLeap[]= {0,31,60,91,121,152,182,213,244,274,305,335};
    static int  sumDayMiladiMonth[] = {0,31,59,90,120,151,181,212,243,273,304,334};

    static public void ActivityAnimation(Context context, TypeInOut type) {
         Activity actv = (Activity) context;
         switch (type) {
             case fadeOut:
                actv.overridePendingTransition(R.anim.fade_left_in_activity, R.anim.fade_right_out_activity);
                 break;
             case fadeIn:
                 actv.overridePendingTransition(R.anim.fade_right_in_activity, R.anim.fade_left_out_activity);
                 break;
         }
    }

    static public void setFont(View[] view, Context context){
        for (View item: view) {
            if (item instanceof TextView || item instanceof EditText || item instanceof Button) {
                Typeface myTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
                ((TextView) item).setTypeface(myTypeFace);
            }
        }
    }

    static public void setFont(View view, Context context){
        Typeface myTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
        if (view instanceof TextView || view instanceof EditText || view instanceof Button)
                ((TextView) view).setTypeface(myTypeFace);
        //view.setTypeface(myTypeFace);
    }

    static public void setFont(Button btn, Context context){
        Typeface myTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
        btn.setTypeface(myTypeFace);
    }


    static public boolean isLeapYear(long Year) {
        if(((Year % 100)!= 0 && (Year % 4) == 0) || ((Year % 100)== 0 && (Year % 400) == 0))
            return true;
        else
            return false;
    }

    static public int dpToPx(Context context, float dp) {
        // Reference http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    static public boolean WriteExcel(Context context, String fileName, List<Tables.Clock> data){
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            Log.e("Execl", "Storage not available or read only");
            return false;
        }


        int rowOfExcel = 0;
        String SumDateTime = "00:00:00";
        boolean Success = false;
        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("test");

        // Generate column headings
        Row row = sheet1.createRow(rowOfExcel);
        rowOfExcel++;

        c = row.createCell(0);
        c.setCellValue("زمان ورود");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("زمان خروج");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("کارکرد");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (17 * 500));
        sheet1.setColumnWidth(1, (17 * 500));
        sheet1.setColumnWidth(2, (17 * 500));

        for (int i = 0; i < data.size(); i = i + 2){

            JalaliCalendar.gDate J1 = new JalaliCalendar.gDate(data.get(i).getDate());

            Row row_ = sheet1.createRow(rowOfExcel);

            c = row_.createCell(0);
            c.setCellValue(JalaliCalendar.MiladiToJalali(J1).toString() + " - " + data.get(i).getTime());
            c.setCellStyle(cs);

            if (i == data.size() - 1){
                c = row_.createCell(1);
                c.setCellValue("تردد ناقص");
                c.setCellStyle(cs);

                c = row_.createCell(2);
                c.setCellValue("00:00:00");
                c.setCellStyle(cs);
            }
            else {
                JalaliCalendar.gDate J2 = new JalaliCalendar.gDate(data.get(i + 1).getDate());

                c = row_.createCell(1);
                c.setCellValue(JalaliCalendar.MiladiToJalali(J2).toString() + " - " + data.get(i + 1).getTime());
                c.setCellStyle(cs);

                c = row_.createCell(2);
                String betwenTime = Tables.Clock.getBettwenTime(data.get(i), data.get(i + 1));
                c.setCellValue(betwenTime);
                c.setCellStyle(cs);

                SumDateTime = Tables.Clock.SumTime(SumDateTime, betwenTime);
            }
            rowOfExcel++;
        }

        Row row_ = sheet1.createRow(rowOfExcel);
        c = row_.createCell(2);
        c.setCellValue(SumDateTime);
        c.setCellStyle(cs);


        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            Success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return Success;

        //return true;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

//    public void setFont(ViewGroup group, Typeface font) {
//        int count = group.getChildCount();
//        View v;
//        for (int i = 0; i < count; i++) {
//            v = group.getChildAt(i);
//            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
//                ((TextView) v).setTypeface(font);
//            } else if (v instanceof ViewGroup)
//                setFont((ViewGroup) v, font);
//        }
//    }
}
