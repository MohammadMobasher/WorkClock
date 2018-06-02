package com.example.mohammad.workclock2.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.JalaliCalendar;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;
import com.example.mohammad.workclock2.myEnum.myEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mohammad on 3/24/2018.
 */

public class j_new_clock_main extends AppCompatActivity implements
        View.OnClickListener,
        DateSetListener,
        TimePickerDialog.OnTimeSetListener
{

    private tableDataBaseListener listener, testListener;
    private TabHost myTabHost;
    private long ProjectId;
    private Button InsertAutomatic, btn_Insert, btn_Start_end, btn_Insert_Handy;
    private TextView txt_TimeView, txt_TitleTolbar, txt_Date_Handy, txt_Time_Handy, txt_TimeAuto;
    private Boolean flag = false;
    private Tables.Project myProject;
    private Pair<Integer, Tables.Clock> TypeShoudInsert;
    private ImageView img_Close;
    private int TypeInsert = -1;

    private int wichTimerTxt = 1; // 0 == for auto and 1 == for timer


    /// for timer tab
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private long timeInMilliSeconds = 0L;
    private long updateTime = 0L;
    private long timeSwapBuff = 0L;
    private Date startDateTime, endDateTime;
    /// for timer tab

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_new_clock_main);
        getSupportActionBar().hide();
        findView();
        initialize();
    }

    private void findView(){
        myTabHost = ((TabHost) findViewById(R.id.tabHostInsert));
        myTabHost.setup();

        this.txt_TimeAuto = ((TextView) findViewById(R.id.txt_Time_Automatic));
        Utility.setFont(this.txt_TimeAuto, this);

        btn_Insert = ((Button) findViewById(R.id.btn_Insert_NewClockTimer));
        Utility.setFont(this.btn_Insert, this);
        btn_Start_end = ((Button) findViewById(R.id.btn_Start_End__NewClockTimer));
        Utility.setFont(this.btn_Start_end, this);
        btn_Insert_Handy = ((Button) findViewById(R.id.btn_Insert_Handy_NewClock));
        Utility.setFont(this.btn_Insert_Handy, this);

        InsertAutomatic = ((Button) findViewById(R.id.btn_InsertAutomatic));
        Utility.setFont(this.InsertAutomatic, this);
        this.img_Close = ((ImageView) findViewById(R.id.img_Close_NewClock));
        txt_TitleTolbar = ((TextView) findViewById(R.id.txt_Title_Toolbar));
        Utility.setFont(this.txt_TitleTolbar, this);
        txt_Date_Handy = ((TextView) findViewById(R.id.txt_Date_NewClock));
        Utility.setFont(this.txt_Date_Handy, this);
        txt_Time_Handy = ((TextView) findViewById(R.id.txt_Time_NewClock));
        Utility.setFont(this.txt_Time_Handy, this);

        btn_Insert.setOnClickListener(this);
        btn_Start_end.setOnClickListener(this);
        InsertAutomatic.setOnClickListener(this);
        this.img_Close.setOnClickListener(this);
        btn_Insert_Handy.setOnClickListener(this);

        txt_Date_Handy.setOnClickListener(this);
        txt_Time_Handy.setOnClickListener(this);


        txt_TimeView = ((TextView) findViewById(R.id.txt_TimeView_NewClockTimer));
        Utility.setFont(this.txt_TimeView , this);
    }

    private void initialize(){
        this.ProjectId = getIntent().getExtras().getLong("ProjectId");
        this.listener = (tableDataBaseListener) getIntent().getSerializableExtra("listener");
        this.testListener = (tableDataBaseListener) getIntent().getSerializableExtra("testListener");

        Tables.Clock myClock= new Tables.Clock(this);
        this.myProject = new Tables.Project(this);

        this.myProject = this.myProject.getItem(this.ProjectId);
        txt_TitleTolbar.setText(this.myProject.getTitle());
        //getSupportActionBar().setTitle(this.myProject.getTitle());

        TabHost.TabSpec tab1 = myTabHost.newTabSpec("Automatic").setIndicator("اتوماتیک").setContent(R.id.tab_Automatic);
        TabHost.TabSpec tab2 = myTabHost.newTabSpec("Handy").setIndicator("دستی").setContent(R.id.tab_handy);
        TabHost.TabSpec tab3 = myTabHost.newTabSpec("Timer").setIndicator("تایمر").setContent(R.id.tab_Timer);

        myTabHost.addTab(tab1);
        myTabHost.addTab(tab2);
        myTabHost.addTab(tab3);

        this.TypeShoudInsert = myClock.TypeShoudInsert(this.ProjectId);

        Calendar calendar = Calendar.getInstance();
        JalaliCalendar.gDate ShamsiConvertor = new JalaliCalendar.gDate(calendar.get(Calendar.YEAR),
                calendar.get(calendar.MONTH) + 1, calendar.get(calendar.DAY_OF_MONTH));
        this.txt_Date_Handy.setText(JalaliCalendar.MiladiToJalali(ShamsiConvertor).toString());
        this.txt_Time_Handy.setText(new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()));


        if(this.TypeShoudInsert.first != -1){
            if(this.TypeShoudInsert.first == 0) {
                SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");

                this.wichTimerTxt = 0;
                try {
                    this.startTime = sdfDateTime.parse(this.TypeShoudInsert.second.getDate() + this.TypeShoudInsert.second.getTime()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                this.customHandler.postDelayed(updateTimerThread, 0);

                this.InsertAutomatic.setBackgroundColor(getColor(android.R.color.holo_red_dark));
                this.InsertAutomatic.setText(getString(R.string.Stop));
            }

            this.flag = true;

            myTabHost.getTabWidget().getChildTabViewAt(0).setEnabled(false);
            myTabHost.getTabWidget().getChildTabViewAt(1).setEnabled(false);
            myTabHost.getTabWidget().getChildTabViewAt(2).setEnabled(false);

            myTabHost.getTabWidget().getChildTabViewAt(this.TypeShoudInsert.first).setEnabled(true);
            myTabHost.setCurrentTab(this.TypeShoudInsert.first);
        }
        if(getIntent().hasExtra("TypeInsert")) {
            this.TypeInsert = getIntent().getExtras().getInt("TypeInsert");
            myTabHost.getTabWidget().getChildTabViewAt(0).setEnabled(false);
            myTabHost.getTabWidget().getChildTabViewAt(1).setEnabled(false);
            myTabHost.getTabWidget().getChildTabViewAt(2).setEnabled(false);

            myTabHost.getTabWidget().getChildTabViewAt(this.TypeInsert).setEnabled(true);
            myTabHost.setCurrentTab(this.TypeInsert);
        }
    }

    @Override
    public void onClick(View view) {
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Tables.Clock myClock;
        Tables.AdvanceClock myAdvClock ;

        switch (view.getId()){
            case R.id.txt_Time_NewClock:
                String[] time = this.txt_Time_Handy.getText().toString().split(":");
                TimePickerDialog timePicker_ = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                        txt_Time_Handy.setText(String.format("%02d:%02d:00", Hour, Minute));
                    }
                }, Integer.parseInt(time[0]), Integer.parseInt(time[1]), false);
                timePicker_.show();
                break;
            case R.id.txt_Date_NewClock:
                DatePicker persianBuilder ;
                if(this.txt_Date_Handy.getText() != ""){
                    String[] splite_ = this.txt_Date_Handy.getText().toString().split("/");
                    persianBuilder = new DatePicker
                            .Builder()
                            .date((int)Integer.parseInt(splite_[2]),
                                    (int)Integer.parseInt(splite_[1]),
                                    (int)Integer.parseInt(splite_[0]))
                            .id(1)
                            .build(this);
                }
                else
                {
                     persianBuilder = new DatePicker
                            .Builder()
                            .id(1)
                            .build(this);
                }

                persianBuilder.show(this.getSupportFragmentManager(), "");
                break;
            case R.id.btn_Insert_Handy_NewClock:
                if(this.flag){
                    String[] firstDate = this.TypeShoudInsert.second.getDate().split("/");
                    String[] firstTime = this.TypeShoudInsert.second.getTime().split(":");


                }

                myClock = new Tables.Clock(this, this.ProjectId,
                        JalaliCalendar.jalaliToMiladi(this.txt_Date_Handy.getText().toString()).toString(),
                        this.txt_Time_Handy.getText().toString(), myEnum.TypeInsert.Handy.getValue(), false);
                myClock.Insert();

                if(this.flag){

                    String result = Tables.Clock.SumTime(this.myProject.getSumDT(),
                            Tables.Clock.getBettwenTime(myClock, this.TypeShoudInsert.second));

                    this.myProject.setSumDT(result);
                    this.myProject.setContext(this);
                    this.myProject.Update();
                    if(this.listener != null)
                        this.listener.OnItemUpdated(this.myProject);
                    if(this.testListener != null) {
                        myAdvClock = new Tables.AdvanceClock(
                                this.TypeShoudInsert.second.getDate() +" - " + this.TypeShoudInsert.second.getTime(),
                                this.TypeShoudInsert.second.getClockId(),
                                myClock.getDate() + " - " + myClock.getTime(),
                                myClock.getClockId(),
                                Tables.Clock.getBettwenTime(myClock, this.TypeShoudInsert.second),
                                myEnum.TypeInsert.Handy.getValue(),
                                this.TypeShoudInsert.second.getIsChanged(),
                                myClock.getIsChanged());
                        this.testListener.OnItemUpdated(myAdvClock);
                    }
                    this.finish();
                    Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                    break;
                }

                if(this.testListener != null) {
                    myAdvClock = new Tables.AdvanceClock(
                            myClock.getDate() + " - " + myClock.getTime(),
                            myClock.getClockId(),
                            "تردد ناقص",
                            -1,
                            "00:00:00",
                            myEnum.TypeInsert.Handy.getValue(),
                            myClock.getIsChanged(),
                            false);
                    this.testListener.OnItemAdded(myAdvClock);
                }
                this.finish();
                Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                break;
            case R.id.img_Close_NewClock:
                this.customHandler.removeCallbacks(updateTimerThread);
                this.finish();
                Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                break;
            case R.id.btn_Insert_NewClockTimer:
                Tables.Clock myClockStart, myClockEnd;

                myClockStart = new Tables.Clock(this, this.ProjectId, DateFormat.format(this.startDateTime.getTime()), TimeFormat.format(this.startDateTime), myEnum.TypeInsert.Timer.getValue(), false);
                myClockStart.Insert();

                myClockEnd = new Tables.Clock(this, this.ProjectId, DateFormat.format(this.endDateTime.getTime()), TimeFormat.format(this.endDateTime), myEnum.TypeInsert.Timer.getValue(), false);
                myClockEnd.Insert();

                this.myProject = new Tables.Project(this);
                this.myProject = this.myProject.getItem(this.ProjectId);
                this.myProject.setContext(this);
                String ResultSumTimes = Tables.Clock.SumTime(this.myProject.getSumDT(), Tables.Clock.getBettwenTime(myClockStart, myClockEnd));
                this.myProject.setSumDT(ResultSumTimes);
                this.myProject.Update();
                if(this.listener != null)
                    listener.OnItemUpdated(this.myProject);

                if(this.testListener != null){
                    myAdvClock = new Tables.AdvanceClock(
                            myClockStart.getDate() + " - " + myClockStart.getTime(),
                            myClockStart.getClockId(),
                            myClockEnd.getDate() + " - " + myClockEnd.getTime(),
                            myClockEnd.getClockId(),
                            ResultSumTimes,
                            myEnum.TypeInsert.Timer.getValue(),
                            myClockStart.getIsChanged(),
                            myClockEnd.getIsChanged()
                    );
                    this.testListener.OnItemAdded(myAdvClock);
                }

                this.finish();
                Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                break;
            case R.id.btn_Start_End__NewClockTimer:
                if(this.btn_Start_end.getText().toString() == getString(R.string.Start)) {
                    this.btn_Start_end.setText(R.string.Stop);
                    this.btn_Start_end.setBackgroundColor(getColor(android.R.color.holo_red_dark));
                    this.startDateTime = calendar.getTime();
                    this.startTime = SystemClock.uptimeMillis();
                    this.customHandler.postDelayed(updateTimerThread, 0);
                }
                else {
                    this.btn_Start_end.setText(R.string.Start);
                    this.endDateTime = calendar.getTime();
                    this.customHandler.removeCallbacks(updateTimerThread);
                    this.btn_Insert.setEnabled(true);
                }
                break;
            case R.id.btn_InsertAutomatic:
                Date now = calendar.getTime();

                myClock = new Tables.Clock(this, this.ProjectId, DateFormat.format(now),
                        TimeFormat.format(now), myEnum.TypeInsert.Automatic.getValue(), false);
                myClock.Insert();

                if(this.flag){

                    String result = Tables.Clock.SumTime(this.myProject.getSumDT(),
                            Tables.Clock.getBettwenTime(myClock, this.TypeShoudInsert.second));

                    this.myProject.setSumDT(result);
                    this.myProject.setContext(this);
                    this.myProject.Update();
                    if(this.listener != null)
                        listener.OnItemUpdated(this.myProject);

                    if(this.testListener != null) {
                        myAdvClock = new Tables.AdvanceClock(
                                this.TypeShoudInsert.second.getDate() +" - " + this.TypeShoudInsert.second.getTime(),
                                this.TypeShoudInsert.second.getClockId(),
                                myClock.getDate() + " - " + myClock.getTime(),
                                myClock.getClockId(),
                                Tables.Clock.getBettwenTime(myClock, this.TypeShoudInsert.second),
                                myEnum.TypeInsert.Handy.getValue(),
                                this.TypeShoudInsert.second.getIsChanged(),
                                myClock.getIsChanged());
                        this.testListener.OnItemUpdated(myAdvClock);
                    }
                    this.finish();
                    Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                    return;
                }

                if(this.testListener != null) {
                    myAdvClock = new Tables.AdvanceClock(
                            myClock.getDate() + " - " + myClock.getTime(),
                            myClock.getClockId(),
                            "تردد ناقص",
                            -1,
                            "00:00:00",
                            myEnum.TypeInsert.Handy.getValue(),
                            myClock.getIsChanged(),
                            false);
                    this.testListener.OnItemAdded(myAdvClock);
                }
                this.finish();
                Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.customHandler.removeCallbacks(updateTimerThread);
        this.finish();
        Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
    }

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {

            switch (wichTimerTxt){
                case 0:
                    updateTime = Calendar.getInstance().getTimeInMillis() - startTime;
                    break;
                case 1:
                    //Log.d("wichTimerTxt", System.currentTimeMillis() + "  =  " + startTime);
                    updateTime =  SystemClock.uptimeMillis() - startTime;
                    //updateTime = timeSwapBuff + timeInMilliSeconds;
                    break;
            }

            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int hour = mins / 60;
            mins = mins % 60;
            //int milliseconds = (int) (updateTime % 1000);
            if(wichTimerTxt == 1)
                txt_TimeView.setText(String.format("%02d:%02d:%02d", hour, mins, secs));
            else
                txt_TimeAuto.setText(String.format("%02d:%02d:%02d", hour, mins, secs));

            customHandler.postDelayed(this, 0);
        }
    };

    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        txt_Date_Handy.setText(String.format("%d/%02d/%02d", year, month, day));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }
}

