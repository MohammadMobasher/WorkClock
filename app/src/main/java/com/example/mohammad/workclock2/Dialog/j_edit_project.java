package com.example.mohammad.workclock2.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;

/**
 * Created by Mohammad on 4/2/2018.
 */

public class j_edit_project extends Dialog implements View.OnClickListener {

    private Context context;
    private long ProjectId;

    private Button btnOk, btn_Cancel;

    private EditText edt_Titile;
    private TextView Titledg;

    private Tables.Project myProject;

    private tableDataBaseListener listener;

    public j_edit_project(@NonNull Context context) {
        super(context);
    }

    public j_edit_project(Context context, long ProjectId, tableDataBaseListener listener){
        super(context);
        this.context = context;
        this.ProjectId = ProjectId;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dg_edit_project);

        findView();
        initialize();
    }

    private void findView(){
        this.myProject = new Tables.Project(this.context);

        this.Titledg = ((TextView) findViewById(R.id.txt_Title_dgEditProject));
        Utility.setFont(this.Titledg, this.context);

        this.btnOk = ((Button) findViewById(R.id.bnt_Insert_dgEditProject));
        Utility.setFont(btnOk, this.context);
        this.btn_Cancel = ((Button) findViewById(R.id.btn_Cancel_dgEditProject));
        Utility.setFont(btn_Cancel, this.context);

        this.btnOk.setOnClickListener(this);
        this.btn_Cancel.setOnClickListener(this);

        this.edt_Titile = ((EditText) findViewById(R.id.edt_Title_dgEditProject));
        Utility.setFont(edt_Titile, this.context);

    }

    private void initialize(){
        this.myProject = this.myProject.getItem(this.ProjectId);
        this.edt_Titile.setText(this.myProject.getTitle());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bnt_Insert_dgEditProject:
                this.myProject.setContext(this.context).setTitle(this.edt_Titile.getText().toString()).Update();
                if(this.listener != null)
                    this.listener.OnItemUpdated(this.myProject);
                break;
            case R.id.btn_Cancel_dgEditProject:
                break;
        }
        this.cancel();
    }
}
