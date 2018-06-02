package com.example.mohammad.workclock2.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;

import org.w3c.dom.Text;

/**
 * Created by Mohammad on 3/25/2018.
 */

public class j_dg_delete_project extends Dialog implements
        View.OnClickListener
{

    private Tables.Project myProject;
    private long ProjectId = -1;
    private Context context;
    private Button btn_Cancel, btn_Ok;
    private TextView txt_Title;
    private tableDataBaseListener listener;


    public j_dg_delete_project(@NonNull Context context) {
        super(context);
    }

    public j_dg_delete_project(Context context, long ProjectId, tableDataBaseListener listener)
    {
        super(context);
        this.ProjectId = ProjectId;
        this.listener = listener;
        this.context = context;
    }

    private void findView(){
        btn_Cancel = ((Button) findViewById(R.id.bnt_Cancel_));
        Utility.setFont(this.btn_Cancel, this.context);
        btn_Ok = ((Button) findViewById(R.id.btn_Ok));
        Utility.setFont(this.btn_Ok, this.context);


        txt_Title = (TextView) findViewById(R.id.txt_Title_);
        Utility.setFont(this.txt_Title, this.context);

        btn_Ok.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
    }

    private void initialize(){
        this.myProject = new Tables.Project(this.context);
        //txt_Title.setText(this.myProject.getItem(this.ProjectId).getTitle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dg_delete_project);

        findView();
        initialize();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Ok:
                this.myProject = new Tables.Project(this.context, this.ProjectId);
                this.myProject.Delete(this.ProjectId);
                if(this.listener != null)
                    listener.OnItemDeleted(this.myProject);
                break;
            case R.id.btn_Cancel:
                break;
        }
        this.cancel();
    }
}
