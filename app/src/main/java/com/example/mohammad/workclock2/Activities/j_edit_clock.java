package com.example.mohammad.workclock2.Activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.JalaliCalendar;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;
import com.example.mohammad.workclock2.myEnum.myEnum;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Mohammad on 3/27/2018.
 */

public class j_edit_clock extends AppCompatActivity implements
        View.OnClickListener,
        DateSetListener
{

    private Button btn_Insert;
    private TextView txt_DateFirst, txt_TimeFirst, txt_DateSecond, txt_TimeSecond, txt_TitleToolBar;
    private ImageView img_Close;
    private tableDataBaseListener listener;
    private long ProjectId;
    private Tables.Project myProject;
    private Tables.Clock myClockFirst, myClockSecond;
    private String TimeBettwenFirst, TimeBettwenSecond;
    private String mySumDT;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_edit_clock);

        findView();
        initialize();
    }

    private void findView(){
        this.txt_DateFirst = ((TextView) findViewById(R.id.txt_Date_First_EditClock));
        this.txt_TimeFirst = ((TextView) findViewById(R.id.txt_Time_First_EditClock));
        this.txt_DateFirst.setOnClickListener(this);
        this.txt_TimeFirst.setOnClickListener(this);

        this.txt_TitleToolBar = ((TextView) findViewById(R.id.txt_Title_Toolbar_EditClock));

        this.txt_DateSecond = ((TextView) findViewById(R.id.txt_Date_Second_EditClock));
        this.txt_TimeSecond = ((TextView) findViewById(R.id.txt_Time_Second_EditClock));
        this.txt_DateSecond.setOnClickListener(this);
        this.txt_TimeSecond.setOnClickListener(this);

        this.img_Close = ((ImageView) findViewById(R.id.img_Close_EditClock));
        this.img_Close.setOnClickListener(this);


        this.btn_Insert = ((Button) findViewById(R.id.btn_Insert_EditClock));
        this.btn_Insert.setOnClickListener(this);
    }

    private void initialize(){
        Utility.setFont(this.txt_DateFirst, this);
        Utility.setFont(this.txt_TimeFirst, this);
        Utility.setFont(this.txt_DateSecond, this);
        Utility.setFont(this.txt_TimeSecond, this);

        JalaliCalendar.gDate MiladiToJalali_;
        this.listener = ((tableDataBaseListener) getIntent().getSerializableExtra("listener"));
        getSupportActionBar().hide();

        this.myClockFirst = new Tables.Clock(this).getItem(getIntent().getExtras().getLong("ClockId1"));
        MiladiToJalali_ = new JalaliCalendar.gDate(this.myClockFirst.getDate());
        this.txt_DateFirst.setText(JalaliCalendar.MiladiToJalali(MiladiToJalali_).toString());
        this.txt_TimeFirst.setText(this.myClockFirst.getTime());
        this.ProjectId = this.myClockFirst.getProjectId();

        long myClockIdSecond = getIntent().getExtras().getLong("ClockId2");
        if(myClockIdSecond != -1) {
            this.myClockSecond = new Tables.Clock(this).getItem(myClockIdSecond);
            MiladiToJalali_ = new JalaliCalendar.gDate(this.myClockSecond.getDate());
            this.txt_DateSecond.setText(JalaliCalendar.MiladiToJalali(MiladiToJalali_).toString());
            this.txt_TimeSecond.setText(this.myClockSecond.getTime());
            this.TimeBettwenFirst = Tables.Clock.getBettwenTime(this.myClockFirst, this.myClockSecond);
        }
        else {
            this.myClockSecond = new Tables.Clock(this);
            this.myClockSecond.setClockId(-1);
            this.txt_DateSecond.setText("تردد ناقص");
            this.txt_TimeSecond.setText("تردد ناقص");
            this.TimeBettwenFirst = "00:00:00";
        }

        this.myProject = new Tables.Project(this);
        this.myProject = this.myProject.getItem(this.myClockFirst.getProjectId());
        this.txt_TitleToolBar.setText(this.myProject.getTitle());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_Date_First_EditClock:
                String[] split_first = this.txt_DateFirst.getText().toString().split("/");
                DatePicker persianBuilderFirst = new DatePicker
                        .Builder()
                        .date(Integer.parseInt(split_first[2]),
                                Integer.parseInt(split_first[1]),
                                Integer.parseInt(split_first[0]))
                        .id(1)
                        .build(this);
                persianBuilderFirst.show(this.getSupportFragmentManager(), "");
                break;
            case R.id.txt_Time_First_EditClock:
                final String[] timeFirst = this.txt_TimeFirst.getText().toString().split(":");
                TimePickerDialog timePicker_First = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                        txt_TimeFirst.setText(String.format("%02d:%02d:", Hour, Minute) + timeFirst[2]);
                    }
                }, Integer.parseInt(timeFirst[0]), Integer.parseInt(timeFirst[1]), false);
                timePicker_First.show();
                break;
            case R.id.txt_Date_Second_EditClock:
                String[] minDate = this.txt_DateFirst.getText().toString().split("/");
                if(this.myClockSecond.getClockId() != -1){

                    String[] splite_second = this.txt_DateSecond.getText().toString().split("/");

                    DatePicker persianBuilderSecond = new DatePicker
                            .Builder()
                            .minYear(Integer.parseInt(minDate[0]))
                            .date(Integer.parseInt(splite_second[2]),
                                    Integer.parseInt(splite_second[1]),
                                    Integer.parseInt(splite_second[0]))
                            .id(2)
                            .build(this);
                    persianBuilderSecond.show(this.getSupportFragmentManager(), "");
                }
                else{
                    DatePicker persianBuilderSecond = new DatePicker
                            .Builder()
                            .id(2)
                            .build(this);
                    persianBuilderSecond.show(this.getSupportFragmentManager(), "");
                }
                break;
            case R.id.txt_Time_Second_EditClock:
                if(this.myClockSecond.getClockId() != -1){
                    final String[] timeSecond = this.txt_TimeSecond.getText().toString().split(":");
                    TimePickerDialog timePicker_Second = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {

                            String[] firstTime = txt_TimeFirst.getText().toString().split(":");
                            if(Integer.parseInt(firstTime[0]) > Hour) {
                                Toast.makeText(getApplicationContext(), "ساعت نمی تواند کمتر از شروع باشد", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(Integer.parseInt(firstTime[1]) > Minute) {
                                Toast.makeText(getApplicationContext(), "ساعت نمی تواند کمتر از شروع باشد", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            txt_TimeSecond.setText(String.format("%02d:%02d:", Hour, Minute) + timeSecond[2]);
                        }
                    }, Integer.parseInt(timeSecond[0]), Integer.parseInt(timeSecond[1]), false);
                    timePicker_Second.show();
                }
                else{
                    Calendar calendar = Calendar.getInstance();
                    final String[] timeSecond = new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()).toString().split(":");
                    TimePickerDialog timePicker_Second = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                            txt_TimeSecond.setText(String.format("%02d:%02d:", Hour, Minute) + timeSecond[2]);
                        }
                    }, Integer.parseInt(timeSecond[0]), Integer.parseInt(timeSecond[1]), false);
                    timePicker_Second.show();
                }
                break;
            case R.id.img_Close_EditClock:
                this.finish();
                Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                break;
            case R.id.btn_Insert_EditClock:
                boolean FirstIsChange = false, SecondIsChange= false;
                Tables.AdvanceClock myAdvanceClock;

                JalaliCalendar.gDate JalaliToMiladi = new JalaliCalendar.gDate(this.txt_DateFirst.getText().toString());
                String DateFirst = JalaliCalendar.jalaliToMiladi(JalaliToMiladi).toString();

                if(this.myClockFirst.getDate() != DateFirst &&
                        this.myClockFirst.getTime() != this.txt_TimeFirst.getText()) {
                    FirstIsChange = true;

                    this.myClockFirst = new Tables.Clock(this, this.myClockFirst.getClockId(), this.ProjectId,
                            DateFirst,
                            this.txt_TimeFirst.getText().toString(), this.myClockFirst.getTypeInsert(), true);
                    this.myClockFirst.Update();
                }

                //Update Project because we have second Clock!
                if(this.txt_DateSecond.getText().toString() != "تردد ناقص") {
                    // update Second Clock
                    if (this.myClockSecond.getClockId() != -1) {
                        JalaliToMiladi = new JalaliCalendar.gDate(this.txt_DateSecond.getText().toString());
                        String DateSecond = JalaliCalendar.jalaliToMiladi(JalaliToMiladi).toString();
                        if(this.myClockSecond.getDate() != DateSecond &&
                                this.myClockSecond.getTime() != this.txt_TimeSecond.getText().toString()) {
                            SecondIsChange = true;
                            this.myClockSecond.setContext(this).setDate(DateSecond).setTime(this.txt_TimeSecond.getText().toString()).setIsChange(true);
                            this.myClockSecond.Update();
                        }
                    }
                    // insert Second Clock
                    else{
                        JalaliToMiladi = new JalaliCalendar.gDate(this.txt_DateSecond.getText().toString());
                        this.myClockSecond = new Tables.Clock(this, this.ProjectId, JalaliCalendar.jalaliToMiladi(JalaliToMiladi).toString(),
                                this.txt_TimeSecond.getText().toString(), myEnum.TypeInsert.Handy.getValue(), false);
                        this.myClockSecond.Insert();
                    }
                    this.TimeBettwenSecond = Tables.Clock.getBettwenTime(this.myClockFirst, this.myClockSecond);

                    this.mySumDT = Tables.Clock.MinusTime(this.myProject.getSumDT(), this.TimeBettwenFirst.toString());
                    this.mySumDT = Tables.Clock.SumTime(this.mySumDT, this.TimeBettwenSecond);
                    this.myProject.setContext(this).setSumDT(this.mySumDT).Update();
                    if(this.listener != null) {
                        myAdvanceClock = new Tables.AdvanceClock(
                                this.myClockFirst.getDate() + " - " + this.myClockFirst.getTime(), this.myClockFirst.getClockId(),
                                this.myClockSecond.getDate() + " - " + this.myClockSecond.getTime(), this.myClockSecond.getClockId(),
                                this.TimeBettwenSecond, myEnum.TypeInsert.Handy.getValue(),
                                FirstIsChange,
                                SecondIsChange);
                        this.listener.OnItemUpdated(myAdvanceClock);
                        this.finish();
                        Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                        break;
                    }
                }
                if(this.listener != null){
                    myAdvanceClock = new Tables.AdvanceClock(
                            this.myClockFirst.getDate() + " - " + this.myClockFirst.getTime(), this.myClockFirst.getClockId(),
                            "تردد ناقص", -1,
                            "00:00:00", myEnum.TypeInsert.Handy.getValue(),
                            FirstIsChange,
                            false);

                    this.listener.OnItemUpdated(myAdvanceClock);
                }
                this.finish();
                Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                break;
        }
    }

    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        switch (id){
            //first
            case 1:
                this.txt_DateFirst.setText(String.format("%d/%02d/%02d", year, month, day));
                break;
            //second
            case 2:
                String[] firstSplit = this.txt_DateFirst.getText().toString().split("/");
                boolean flag = false;
                if(Integer.parseInt(firstSplit[0]) > year)
                    Toast.makeText(this, "سال انتخاب شده کمتر از شروع است", Toast.LENGTH_SHORT).show();
                if(Integer.parseInt(firstSplit[1]) > month)
                    Toast.makeText(this, "ماه انتخاب شده کمتر از شروع است", Toast.LENGTH_SHORT).show();
                if(Integer.parseInt(firstSplit[2]) > day)
                    Toast.makeText(this, "روز انتخاب شده کمتر از شروع است", Toast.LENGTH_SHORT).show();
                if(flag)
                    break;

                this.txt_DateSecond.setText(String.format("%d/%02d/%02d", year, month, day));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
    }
}
