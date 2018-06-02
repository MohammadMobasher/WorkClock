package com.example.mohammad.workclock2.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.JalaliCalendar;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;


/**
 * Created by Mohammad on 3/29/2018.
 */

public class j_dg_delete_clock extends Dialog implements
        View.OnClickListener
{
    private tableDataBaseListener listener;
    private long ClockId1, ClockId2;

    private Context context;

    private Button btnCancel, btnOk;
    private TextView SumDT, FirstDT, SecondDT;

    private String BettwenTime;
    private ImageView img_TypeInsert;

    private Tables.Clock myClock1, myClock2;
    private long ProjectId;

    public j_dg_delete_clock(@NonNull Context context) {
        super(context);
    }

    public j_dg_delete_clock(Context context, tableDataBaseListener listener, long Clock1, long Clock2){
        super(context);
        this.context = context;
        this.listener = listener;
        this.ClockId1 = Clock1;
        this.ClockId2 = Clock2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dg_delete_clock);

        findView();
        initialize();
    }

    private void findView(){
        this.btnOk = ((Button) findViewById(R.id.btn_Ok_DeleteCLock));
        this.btnCancel = ((Button) findViewById(R.id.bnt_Cancel_DeleteClock));

        this.img_TypeInsert = ((ImageView) findViewById(R.id.img_TypeInsert_DeleteClock));

        this.btnCancel.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);

        this.SumDT = ((TextView) findViewById(R.id.txt_SumDT_DeletClock));
        Utility.setFont(this.SumDT, this.context);
        this.FirstDT = ((TextView) findViewById(R.id.txt_FirstDT_DeleteClock));
        Utility.setFont(this.FirstDT, this.context);
        this.SecondDT = ((TextView) findViewById(R.id.txt_SecondDT_DeleteClock));
        Utility.setFont(this.SecondDT, this.context);
    }

    private void initialize(){
        JalaliCalendar.gDate myJ1;
        JalaliCalendar.gDate myJ2;

        this.myClock1 = new Tables.Clock(this.context).getItem(this.ClockId1);
        myJ1 = new JalaliCalendar.gDate(this.myClock1.getDate());

        this.img_TypeInsert.setImageResource(this.context.getResources().getIdentifier("img" + this.myClock1.getTypeInsert(), "mipmap", this.context.getPackageName()));

        this.ProjectId = this.myClock1.getProjectId();

        this.FirstDT.setText(JalaliCalendar.MiladiToJalali(myJ1).toString() + " - " + this.myClock1.getTime());

        if(this.ClockId2 != -1) {
            this.myClock2 = new Tables.Clock(this.context).getItem(this.ClockId2);
            myJ2 = new JalaliCalendar.gDate(this.myClock2.getDate());

            this.SecondDT.setText(JalaliCalendar.MiladiToJalali(myJ2).toString() + " - " + this.myClock2.getTime());

            this.BettwenTime = Tables.Clock.getBettwenTime(this.myClock1, this.myClock2);
            this.SumDT.setText(this.BettwenTime);
        }
        else{
            this.SecondDT.setText("تردد ناقص");
            this.SumDT.setText("00:00:00");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bnt_Cancel_DeleteClock:
                break;
            case R.id.btn_Ok_DeleteCLock:
                this.myClock1.setContext(this.context).Delete(this.ClockId1);
                Tables.AdvanceClock myADclock ;
                if(this.ClockId2 != -1) {
                    this.myClock2.setContext(this.context).Delete(this.ClockId2);

                    Tables.Project myProject = new Tables.Project(this.context).getItem(this.ProjectId);
                    myProject.setContext(this.context).setSumDT(Tables.Clock.MinusTime(myProject.getSumDT(), this.BettwenTime)).Update();
                }
                if(this.listener != null){
                    myADclock = new Tables.AdvanceClock("", this.myClock1.getClockId(), "", this.myClock2.getClockId(), "", 0,true,true);
                    this.listener.OnItemDeleted(myADclock);
                }
                break;
        }
        this.cancel();
    }
}



