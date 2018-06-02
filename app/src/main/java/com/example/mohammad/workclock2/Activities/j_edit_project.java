package com.example.mohammad.workclock2.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;
import com.example.mohammad.workclock2.myEnum.myEnum;

/**
 * Created by Mohammad on 3/23/2018.
 **/

public class j_edit_project extends AppCompatActivity implements
        View.OnClickListener {

    private EditText edtNameProject;
    private Button btnCancel, btnSave;
    private tableDataBaseListener myListener;
    private long ProjectId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_edit_project);

        findView();
        initialize();
    }

    private void findView(){
        edtNameProject = (EditText) findViewById(R.id.edi_NameProject);
        btnCancel = ((Button) findViewById(R.id.btn_Cancel));
        btnSave = ((Button) findViewById(R.id.btn_Save));
        myListener = (tableDataBaseListener) getIntent().getSerializableExtra("listener");

        this.ProjectId = getIntent().getExtras().getLong("ProjectId");

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initialize(){
        Tables.Project myProject = new Tables.Project(this);
        myProject = myProject.getItem(this.ProjectId);
        edtNameProject.setText(myProject.getTitle());
        getSupportActionBar().setTitle(myProject.getTitle());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Save:
                Tables.Project myProject = new Tables.Project(this).getItem(this.ProjectId);
                myProject.setTitle(edtNameProject.getText().toString());
                myProject.setContext(this);
                myProject.Update();
                if(myListener != null)
                    myListener.OnItemUpdated(myProject);
                break;
            case R.id.btn_Cancel:
                break;
        }
        this.finish();
        Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
    }
}
