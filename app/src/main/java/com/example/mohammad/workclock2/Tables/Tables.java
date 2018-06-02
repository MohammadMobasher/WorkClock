package com.example.mohammad.workclock2.Tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.Pair;
import android.util.Log;

import com.example.mohammad.workclock2.DB.DB;
import com.example.mohammad.workclock2.DB.DataBase;

import com.example.mohammad.workclock2.Utility.*;
import com.example.mohammad.workclock2.myEnum.myEnum;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Created by Mohammad on 12/10/2017.
 */

public class Tables {
    public static class Project{

        private Context context;

        private long ProjectId;
        private String Title;
        private String SumDT;
        private boolean HasHalfTraffic;


        public Project(Cursor cursor){
            this.ProjectId = cursor.getLong(cursor.getColumnIndex("ProjectId"));
            this.Title = cursor.getString(cursor.getColumnIndex("Title"));
            this.SumDT = cursor.getString(cursor.getColumnIndex("SumDT"));
            this.HasHalfTraffic = cursor.getInt(cursor.getColumnIndex("HasHalfTraffic")) > 0;
        }

        public Project(Context context, long projectId){
            this.ProjectId = projectId;
            this.context = context;
        }

        public Project(Context context){
            this.context = context;
        }

        public Project(Context context,String title, String sumDT, boolean hasHalfTraffic){
            this.Title = title;
            this.SumDT = sumDT;
            this.HasHalfTraffic = hasHalfTraffic;
            this.context = context;
        }

        public String getTitle(){
            return this.Title;
        }

        public String getSumDT(){
            return this.SumDT;
        }

        public long getProjectId(){
            return this.ProjectId;
        }

        public Project setTitle(String Name){
            this.Title = Name;
            return this;
        }

        public Project setContext(Context context){
            this.context = context;
            return this;
        }

        public Project setSumDT(String SumDT){
            this.SumDT = SumDT;
            return this;
        }

        public boolean Insert(){
            ContentValues CV = new ContentValues();

            CV.put("Title", this.Title);
            CV.put("SumDT", this.SumDT);
            CV.put("HasHalfTraffic", this.HasHalfTraffic);

            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else {
                DB db = new DB(this.context);
                this.ProjectId = db.Insert("Project", CV);
                if(this.ProjectId == -1)
                    return false;
                return true;
            }
        }

        public boolean Update(){
            ContentValues CV = new ContentValues();

            CV.put("Title", this.Title);
            CV.put("SumDT", this.SumDT);
            CV.put("HasHalfTraffic", this.HasHalfTraffic);

            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else {
                DB db = new DB(this.context);
                return db.update("Project", "ProjectId = " + this.ProjectId, CV);
            }
        }

        public List<Project> getItems() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            DB db = new DB(this.context);
            return db.getItems((Class) this.getClass(),"select * from Project");
        }

        public Project getItem(long ProjectId) {

            DB db = new DB(this.context);
            try {
                return db.getItem(this.getClass(), "select * from Project where ProjectId = " + ProjectId);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }

        public boolean Delete(long ProjectId){
            DB db = new DB(this.context);
            if(db.Delete("Project", "ProjectId = " + ProjectId)) {
                Clock myClock = new Clock(this.context);
                myClock.Delete(ProjectId);
                return true;
            }
            return false;
        }

        public boolean DeleteClocks(long ProjectId){
            DB db = new DB(this.context);
            if(db.Delete("Clock", "ProjectId = " + ProjectId))
                return true;
            return false;
        }

    }

    public static class Clock{
        private Context context;

        private long ClockId;
        private long ProjectId;
        private String Date;
        private String Time;
        //private myEnum.TypeInsert TypeInsert;
        private int TypeInsert;
        private Boolean IsChanged;

        public Clock(){

        }

        public Clock(Cursor cursor){
            this.ClockId = cursor.getLong(cursor.getColumnIndex("ClockId"));
            this.ProjectId = cursor.getLong(cursor.getColumnIndex("ProjectId"));
            this.Date = cursor.getString(cursor.getColumnIndex("Date"));
            this.Time = cursor.getString(cursor.getColumnIndex("Time"));
            this.TypeInsert = cursor.getInt(cursor.getColumnIndex("TypeInsert"));
            this.IsChanged = cursor.getInt(cursor.getColumnIndex("IsChanged")) > 0;
        }

        public Clock(Context context){
            this.context = context;
        }

        public Clock(Context context, long ClockId){
            this.context = context;
            this.ClockId = ClockId;
        }

        public Clock(Context context, long ProjectId, String Date, String Time, int TypeInsert, boolean IsChange){
            this.context = context;
            this.ProjectId = ProjectId;
            this.Date = Date;
            this.Time = Time;
            this.TypeInsert = TypeInsert;
            this.IsChanged = IsChange;
        }

        public Clock(Context context, long ClockId, long ProjectId, String Date, String Time, int TypeInsert, boolean IsChange){
            this.context = context;
            this.ClockId = ClockId;
            this.ProjectId = ProjectId;
            this.Date = Date;
            this.Time = Time;
            this.TypeInsert = TypeInsert;
            this.IsChanged = IsChange;
        }

        public Clock setContext(Context context){this.context = context; return this;}

        public long getClockId(){
            return this.ClockId;
        }

        public Clock setClockId(long ClockId){this.ClockId = ClockId; return this;}

        public Clock setDate(String Date){this.Date = Date; return  this;}

        public Clock setTime(String Time){this.Time = Time;return this;}

        public Clock setIsChange(boolean IsChanged){this.IsChanged = IsChanged; return this;}

        public long getProjectId(){
            return this.ProjectId;
        }

        public String getDate(){
            return this.Date;
        }

        public String getTime(){
            return this.Time;
        }

        public int getTypeInsert(){
            return this.TypeInsert;
        }

        public Boolean getIsChanged(){
            return this.IsChanged;
        }

        public boolean Insert(){
            ContentValues myContentValue = new ContentValues();

            myContentValue.put("ProjectId", this.ProjectId);
            myContentValue.put("Date", this.Date);
            myContentValue.put("Time", this.Time);
            myContentValue.put("TypeInsert", this.TypeInsert);
            myContentValue.put("IsChanged", this.IsChanged);

            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else {
                DB db = new DB(this.context);
                this.ClockId = db.Insert("Clock", myContentValue);
                if(this.ClockId == -1)
                    return false;
                return true;
            }
        }

        public boolean Update(){
            ContentValues myContentValue = new ContentValues();

            myContentValue.put("ProjectId", this.ProjectId);
            myContentValue.put("Date", this.Date);
            myContentValue.put("Time", this.Time);
            myContentValue.put("TypeInsert", this.TypeInsert);
            myContentValue.put("IsChanged", this.IsChanged);

            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else{
                DB db = new DB(this.context);
                return db.update("Clock", "ClockId = " + this.ClockId, myContentValue);
            }
        }

        public List<Clock> getItems(long ProjectId){
            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else {
                DB db = new DB(this.context);
                return db.getItems((Class) this.getClass(), " SELECT * FROM Clock WHERE ProjectId = " + ProjectId);
            }
        }

        public Clock getItem(long ClockId){
            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else {
                DB db = new DB(this.context);
                try {
                    return db.getItem((Class) this.getClass(), "select * from Clock where ClockId = " + ClockId);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public boolean Delete(long ClockId){
            DB db = new DB(this.context);
            return db.Delete("Clock", "ClockId = " + ClockId);
        }

        public Pair<Integer, Clock> TypeShoudInsert(long ProjectId){
            DB db = new DB(this.context);
            Cursor cursor = db.RunQuery("SELECT c.*, b.CountClock as CountClock FROM Clock AS c join (SELECT Count(*) AS CountClock, * FROM Clock WHERE ProjectId = " + ProjectId + ") AS b ON c.ClockId = b.ClockId WHERE c.ProjectId = " + ProjectId + " ORDER BY c.ClockId DESC LIMIT 1");

            if(cursor.getCount() == 0)
                return new Pair<>(-1, new Tables.Clock());
            if(cursor.getLong(cursor.getColumnIndex("CountClock")) % 2 == 0)
                return new Pair<>(-1, new Tables.Clock());

            return new Pair<>(cursor.getInt(cursor.getColumnIndex("TypeInsert")), new Tables.Clock(cursor));
        }

        static public String getBettwenTime(Clock firstClock, Clock secondClock){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                Date dateFirst = sdf.parse(firstClock.getDate().replace("/", "") + firstClock.getTime().replace(":", ""));
                Date dateSecond = sdf.parse(secondClock.getDate().replace("/", "") + secondClock.getTime().replace(":", ""));
                long mills = Math.abs(dateFirst.getTime() - dateSecond.getTime());

                int hours = (int) (mills/(1000 * 60 * 60));
                int mins = (int) ((mills/(1000*60)) % 60);
                int Sec = (int) ((mills/(1000)) % 60);

                return String.format("%02d:%02d:%02d", hours, mins, Sec);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";
        }

        static public long getMills(String Time){
            String[] Timesplit = Time.split(":");
            long Hours = Long.parseLong(Timesplit[0]);
            long Mins = Long.parseLong(Timesplit[1]);
            long Secs = Long.parseLong(Timesplit[2]);

            Hours = (Hours * (1000 * 60 * 60));
            Mins = (Mins * (1000 * 60));
            Secs = (Secs* (1000));

            return Hours + Mins + Secs;
        }


        static public String getBettwenTime(String dateFirst, String dateSecond){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                Date dateFirst_ = sdf.parse(dateFirst.replace("/", "").replace(":", ""));
                Date dateSecond_ = sdf.parse(dateSecond.replace("/", "").replace(":", ""));

                long mills = Math.abs(dateFirst_.getTime() - dateSecond_.getTime());

                int hours = (int) (mills/(1000 * 60 * 60));
                int mins = (int) ((mills/(1000*60)) % 60);
                int Sec = (int) ((mills/(1000)) % 60);

                return String.format("%02d:%02d:%02d", hours, mins, Sec);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return"00:00:00";
        }

        static public String SumTime(String firstTime, String secondTime){
            String[] firstTimeArray = firstTime.split(":");
            String[] secondTimeArray = secondTime.split(":");
            long Hour = 0, Minute = 0, Second = 0;

            //second
            Second = Long.parseLong(firstTimeArray[2]) + Long.parseLong(secondTimeArray[2]);

            //minute
            Minute = Long.parseLong(firstTimeArray[1]) + Long.parseLong(secondTimeArray[1]);

            //hour
            Hour = Long.parseLong(firstTimeArray[0]) + Long.parseLong(secondTimeArray[0]);

            if(Second >= 60){
                Minute += Second / 60;
                Second = Second % 60;
            }
            if(Minute >= 60){
                Hour += Minute / 60;
                Minute = Minute % 60;
            }

            return String.format("%d:%02d:%02d", Hour, Minute, Second);
        }

        static public String MinusTime(String firstTime, String secondTime){
            String[] firstTimeArray = firstTime.split(":");
            String[] secondTimeArray = secondTime.split(":");
            long Hour = 0, Minute = 0, Second = 0;

            //second
            Second = Long.parseLong(firstTimeArray[2]) - Long.parseLong(secondTimeArray[2]);

            //minute
            Minute = Long.parseLong(firstTimeArray[1]) - Long.parseLong(secondTimeArray[1]);

            //hour
            Hour = Long.parseLong(firstTimeArray[0]) - Long.parseLong(secondTimeArray[0]);

            if(Second < 0){
                Minute--;
                Second = 60 + Second;
            }
            if(Minute < 0){
                Hour--;
                Minute = 60 - Minute;
            }

            return String.format("%d:%02d:%02d", Hour, Minute, Second);
        }

    }

    public static class AdvanceClock{
        private String Date_TimeFirst;
        private long Id_First;
        private String Date_TimeSecond;
        private long Id_Second;
        private String Sum;
        private int TypeInsert;
        private boolean IsChangeFirst, IsChangeSecond;

        public List<AdvanceClock> getAdvanceClock(List<Clock> items){
            JalaliCalendar.gDate Miladi1 ;
            List<AdvanceClock> myList = new ArrayList<>();
            for (int i = 0; i < items.size(); i = i + 2){

                Clock myClock1 = items.get(i);
                Miladi1 = new JalaliCalendar.gDate(myClock1.getDate());
                if (i == items.size() - 1){
                    myList.add(new AdvanceClock(JalaliCalendar.MiladiToJalali(Miladi1) + " - " + myClock1.getTime(),
                            myClock1.getClockId(),
                            "تردد ناقص",
                            -1,
                            "00:00:00",
                            myClock1.TypeInsert,
                            myClock1.getIsChanged(),
                            false));
                    continue;
                }
                Clock myClock2 = items.get(i + 1);
                JalaliCalendar.gDate Miladi2 = new JalaliCalendar.gDate(myClock2.getDate()) ;
                myList.add(new AdvanceClock(JalaliCalendar.MiladiToJalali(Miladi1) + " - " + myClock1.getTime(),
                                            myClock1.getClockId(),
                                            JalaliCalendar.MiladiToJalali(Miladi2) + " - " + myClock2.getTime(),
                                            myClock2.getClockId(),
                                            Clock.getBettwenTime(myClock1, myClock2), myClock1.TypeInsert,
                                            myClock1.getIsChanged(), myClock2.getIsChanged()));
            }
            return myList;
        }

        public AdvanceClock(String Date_TimeFirst, long Id_first, String Date_TimeSecond, long Id_Second, String Sum, int TypeInsert, boolean IsChangeFirst, boolean IsChangeSecond){
            this.Date_TimeFirst = Date_TimeFirst;
            this.Date_TimeSecond = Date_TimeSecond;
            this.Sum = Sum;
            this.TypeInsert = TypeInsert;
            this.Id_First = Id_first;
            this.Id_Second = Id_Second;
            this.IsChangeFirst = IsChangeFirst;
            this.IsChangeSecond = IsChangeSecond;
        }

        public AdvanceClock(){

        }

        public String getDate_TimeFirst(){
            return this.Date_TimeFirst;
        }

        public String getDate_TimeSecond(){
            return this.Date_TimeSecond;
        }

        public String getSum(){
            return this.Sum;
        }

        public long getId_First(){return this.Id_First;}

        public long getId_Second(){
            return this.Id_Second;
        }

        public int getTypeInsert(){
            return this.TypeInsert;
        }

        public boolean getIsChangeSecond(){
            return this.IsChangeSecond;
        }

        public boolean getIsChangeFirst(){
            return this.IsChangeFirst;
        }

        public AdvanceClock setDate_TimeFirst(String Date_TimeFirst){
            this.Date_TimeFirst = Date_TimeFirst;
            return this;
        }

        public AdvanceClock setDate_TimeSecond(String Date_TimeSecond){
            this.Date_TimeSecond = Date_TimeSecond;
            return this;
        }

        public AdvanceClock setSum(String Sum){
            this.Sum = Sum;
            return this;}

        public AdvanceClock setIsChangeFirst(boolean IsChangeFirst){
            this.IsChangeFirst = IsChangeFirst;
            return this;
        }

        public AdvanceClock setIsChangeSecond(boolean IsChangeSecond){
            this.IsChangeSecond = IsChangeSecond;
            return this;
        }
    }

    public static class Description{

        private String Descript;
        private long Id;
        private long ClockId;

        private Context context;

        public Description(){}

        public Description(Cursor cursor){
            this.Descript = cursor.getString(cursor.getColumnIndex("Descript"));
            this.ClockId = cursor.getLong(cursor.getColumnIndex("ClockId"));
            this.Id = cursor.getLong(cursor.getColumnIndex("Id"));
        }

        public Description Description(Context context){
            this.context = context;
            return this;
        }

        public Description Description(Context context, long Id){
            this.Id = Id;
            this.context = context;
            return this;
        }

        public Description Description(Context context, long ClockId, String Descript){
            this.context = context;
            this.ClockId = ClockId;
            this.Descript = Descript;
            return this;
        }

        public Description setContext(Context context){
            this.context = context;
            return this;
        }

        public String getDescript(){
            return this.Descript;
        }

        public long getId(){
            return this.Id;
        }

        public long getClockId(){
            return this.ClockId;
        }

        public boolean Insert(){
            ContentValues myCV = new ContentValues();

            myCV.put("ClockId", this.ClockId);
            myCV.put("Descript", this.Descript);

            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else
            {
                DB db = new DB(this.context);
                this.Id = db.Insert("Description",myCV);
                if(this.Id == -1)
                    return false;
                return true;
            }
        }

        public boolean Delete(long Id){
            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else {
                DB db = new DB(this.context);
                return db.Delete("Description", "Id = " + Id);
            }
        }

        public boolean Update(){
            ContentValues myCV = new ContentValues();

            myCV.put("Id", this.Id);
            myCV.put("ClockId", this.ClockId);
            myCV.put("Descript", this.Descript);

            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else
            {
                DB db = new DB(this.context);
                return db.update("Description", "Id = " + this.Id, myCV);
            }
        }

        public Description getItem(long Id){
            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else {
                DB db = new DB(this.context);
                try {
                    return db.getItem((Class) this.getClass(), "select * from Description where Id = " + Id);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public List<Description> getItems(long ClockIdStart, long ClockIdEnd){
            if(this.context == null)
                throw new NullPointerException("the Context is null pleas initialize that");
            else {
                DB db = new DB(this.context);
                return db.getItems((Class) this.getClass(), " SELECT * FROM Description WHERE ClockId = " + ClockIdStart + " OR ClockId = " + ClockIdEnd);
            }
        }
    }

}



