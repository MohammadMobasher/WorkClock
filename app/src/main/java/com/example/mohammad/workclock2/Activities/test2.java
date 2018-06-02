package com.example.mohammad.workclock2.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.example.mohammad.workclock2.R;

import java.util.Calendar;

/**
 * Created by Mohammad on 3/27/2018.
 */

public class test2 extends AppCompatActivity implements DateSetListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2);
        DatePicker persianBuilder;
        persianBuilder = new DatePicker
                .Builder()
                .id(1)
                .build(this);
        persianBuilder.show(this.getSupportFragmentManager(), "");
    }

    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
    }

}
