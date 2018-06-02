package com.example.mohammad.workclock2.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;
import com.example.mohammad.workclock2.myEnum.myEnum;

/**
 * Created by Mohammad on 3/22/2018.
 */

public class j_new_project extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNameProject;
    private Button btnCancel, btnSave;
    private tableDataBaseListener myListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_new_project);


        findView();
    }


    private void findView(){

        edtNameProject = (EditText) findViewById(R.id.edi_NameProject);
        btnCancel = ((Button) findViewById(R.id.btn_Cancel));
        btnSave = ((Button) findViewById(R.id.btn_Save));
        myListener = (tableDataBaseListener) getIntent().getSerializableExtra("listener");

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Save:
                Tables.Project myProject = new Tables.Project(this, edtNameProject.getText() + "", "00:00", false);
                myProject.Insert();
                if(myListener != null)
                    myListener.OnItemAdded(myProject);
                break;
            case R.id.btn_Cancel:
                break;
        }
        this.finish();
        Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
    }
}
