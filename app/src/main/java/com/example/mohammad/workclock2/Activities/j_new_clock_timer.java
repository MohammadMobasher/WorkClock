package com.example.mohammad.workclock2.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;
import com.example.mohammad.workclock2.myEnum.myEnum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mohammad on 3/26/2018.
 */

public class j_new_clock_timer extends Activity implements
        View.OnClickListener
{

    Button btn_Insert, btn_Start_end;
    TextView txt_TimeView;

    private tableDataBaseListener listener;

    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private long timeInMilliSeconds = 0L;
    private long updateTime = 0L;
    private long timeSwapBuff = 0L;

    private Date startDate, endTime;
    private long ProjectId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_new_clock_timer);

        findView();
        initialize();
    }

    private void findView(){
        btn_Insert = ((Button) findViewById(R.id.btn_Insert_NewClockTimer));
        btn_Start_end = ((Button) findViewById(R.id.btn_Start_End__NewClockTimer));

        btn_Insert.setOnClickListener(this);
        btn_Start_end.setOnClickListener(this);

        txt_TimeView = ((TextView) findViewById(R.id.txt_TimeView_NewClockTimer));
    }

    private void initialize(){
        this.ProjectId = getIntent().getExtras().getLong("ProjectId");
        this.listener = ((tableDataBaseListener) getIntent().getSerializableExtra("listener"));

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Insert_NewClockTimer:
                SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss");
                Tables.Clock myClockStart, myClockEnd;

                myClockStart = new Tables.Clock(this, this.ProjectId, DateFormat.format(this.startDate.getTime()), TimeFormat.format(this.startDate), myEnum.TypeInsert.Timer.getValue(), false);
                myClockStart.Insert();

                myClockEnd = new Tables.Clock(this, this.ProjectId, DateFormat.format(this.endTime.getTime()), TimeFormat.format(this.endTime), myEnum.TypeInsert.Timer.getValue(), false);
                myClockEnd.Insert();

                Tables.Project myProject = new Tables.Project(this);
                myProject = myProject.getItem(this.ProjectId);
                myProject.setContext(this);

                myProject.setSumDT(Tables.Clock.SumTime(myProject.getSumDT(), Tables.Clock.getBettwenTime(myClockStart, myClockEnd)));
                myProject.Update();

                this.finish();
                Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                break;
            case R.id.btn_Start_End__NewClockTimer:
                Calendar calendar = Calendar.getInstance();
                if(this.btn_Start_end.getText().toString() == getString(R.string.Start)) {
                    this.btn_Start_end.setText(R.string.Stop);
                    this.startDate = calendar.getTime();
                    this.startTime = SystemClock.uptimeMillis();
                    this.customHandler.postDelayed(updateTimerThread, 0);
                }
                else {
                    this.btn_Start_end.setText(R.string.Start);
                    this.endTime = calendar.getTime();
                    this.customHandler.removeCallbacks(updateTimerThread);
                    this.btn_Insert.setEnabled(true);
                }
                break;
        }
    }


    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliSeconds = SystemClock.uptimeMillis() - startTime;

            updateTime = timeSwapBuff + timeInMilliSeconds;

            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int hour = mins / 60;
            mins = mins % 60;
            //int milliseconds = (int) (updateTime % 1000);
            txt_TimeView.setText(String.format("%02d:%02d:%02d", hour, mins, secs));
            customHandler.postDelayed(this, 0);
        }
    };
}
