package com.example.mohammad.workclock2.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;
import com.example.mohammad.workclock2.myEnum.myEnum;

/**
 * Created by Mohammad on 3/28/2018.
 */

public class j_dg_new_project extends Dialog implements
        View.OnClickListener
{

    private Context context;
    private tableDataBaseListener listener;
    private Button btn_Ok, btn_Cancel;
    private EditText edt_Title;
    private TextView txt_Title_DG;

    public j_dg_new_project(@NonNull Context context) {
        super(context);
    }

    public j_dg_new_project(Context context, tableDataBaseListener listener){
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dg_new_project);


        findView();
    }

    private void findView(){
        this.btn_Ok = ((Button) findViewById(R.id.bnt_Insert_NewProject));
        this.btn_Cancel = ((Button) findViewById(R.id.btn_Cancel_NewProject));

        this.btn_Ok.setOnClickListener(this);
        this.btn_Cancel.setOnClickListener(this);

        this.edt_Title = ((EditText) findViewById(R.id.edt_Title_NewProject));
    }

    @Override
    public void onClick(View view) {
        Log.d("mohammad", "mohammad");
        switch (view.getId()){
            case R.id.bnt_Insert_NewProject:

                Tables.Project myProject = new Tables.Project(this.context, this.edt_Title.getText().toString(), "00:00:00", false);
                myProject.Insert();
                if(this.listener != null)
                    listener.OnItemAdded(myProject);

                break;
            case R.id.btn_Cancel_NewProject:
                this.cancel();
                break;
        }
        this.cancel();
        Utility.ActivityAnimation(this.context, myEnum.TypeInOut.fadeOut);
    }
}
