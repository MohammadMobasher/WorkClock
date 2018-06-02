package com.example.mohammad.workclock2.Adapter.RecycleView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.mohammad.workclock2.Activities.j_edit_project;
import com.example.mohammad.workclock2.Activities.j_new_clock_main;
import com.example.mohammad.workclock2.Dialog.*;
import com.example.mohammad.workclock2.Activities.test;
import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.actvListener;
import com.example.mohammad.workclock2.interfaces.adapterMethods;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;
import com.example.mohammad.workclock2.myEnum.myEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammad on 12/14/2017.
 */

public class ad_Project  extends RecyclerView.Adapter<ad_Project.ViewHolder> implements
        adapterMethods,
        Filterable
{

    private List<Tables.Project> values, realValues;
    public Context context;
    private tableDataBaseListener listener;
    private actvListener myactvListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.rv_project_item, parent, false);
        v.setClickable(true);
        ViewHolder vh = new ViewHolder(v, this.context, this.listener, this.myactvListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tables.Project value = this.values.get(position);

        holder.txtName.setText(value.getTitle());
        holder.txtSumDT.setText(value.getSumDT());
    }

    @Override
    public int getItemCount() {
        if(this.values != null)
            return this.values.size();
        else
            return 0;
    }

    public ad_Project(List<Tables.Project> values){
        this.values = values;
    }

    public ad_Project(List<Tables.Project> values, Context context, tableDataBaseListener listener){
        this.values = values;
        //this.context = context;
        this.listener = listener;
    }

    public ad_Project(List<Tables.Project> values, Context context, tableDataBaseListener listener, actvListener myactvListener){
        this.values = values;
        this.realValues = values;
        this.context = context;
        this.listener = listener;
        this.myactvListener = myactvListener;
    }


    @Override
    public <T> void Add(T item) {
        this.values.add(((Tables.Project) item));
        notifyDataSetChanged();
    }

    @Override
    public <T> void Updated(T item) {
        for (Tables.Project project: this.values) {
            if(project.getProjectId() == ((Tables.Project) item).getProjectId()) {
                project.setTitle(((Tables.Project) item).getTitle());
                project.setSumDT(((Tables.Project) item).getSumDT());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void removeWithPosition(int position) {
        this.values.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void removeWithIdentifier(long Id) {
        for(int i = 0; i < this.values.size(); i++)
            if(values.get(i).getProjectId() == Id)
                values.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public <T> void OnDataUpDate(ArrayList<T> listProject) {
        this.values = ((List<Tables.Project>) listProject);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Log.d("search", charSequence.toString());
                String searchText = charSequence.toString();
                if (searchText.isEmpty()) {
                    values = realValues;
                } else {
                    ArrayList<Tables.Project> filteredList = new ArrayList<>();

                    for (Tables.Project project : realValues) {
                        if (project.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                            filteredList.add(project);
                        }
                    }

                    values = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = values;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                values = (ArrayList<Tables.Project>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener,
            DialogInterface.OnClickListener{

        public TextView txtName, txtSumDT;
        public Context context;
        public View layout;
        public Tables.Project myProject;
        public tableDataBaseListener listener;
        public actvListener myactvListener;


        public ViewHolder(View itemView, Context context, tableDataBaseListener listener) {
            super(itemView);
            this.context = context;
            this.listener = listener;

            layout = itemView;
            txtName = (TextView) itemView.findViewById(R.id.txt_Name);
            txtSumDT = (TextView) itemView.findViewById(R.id.txt_SumDT);

            layout.setOnClickListener(this);
            layout.setOnLongClickListener(this);
        }

        public ViewHolder(View itemView, Context context, tableDataBaseListener listener, actvListener myactvListener) {
            super(itemView);
            this.context = context;
            this.listener = listener;
            this.myactvListener = myactvListener;

            layout = itemView;
            txtName = (TextView) itemView.findViewById(R.id.txt_Name);
            txtSumDT = (TextView) itemView.findViewById(R.id.txt_SumDT);

            Utility.setFont(txtName, this.context);
            Utility.setFont(txtSumDT, this.context);

            layout.setOnClickListener(this);
            layout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.myProject = values.get(getAdapterPosition());
            Activity actv = (Activity)this.context;

            Intent myIntent = new Intent(this.context, test.class);
            //Intent myIntent = new Intent(this.context, j_new_clock_timer.class);
            //Intent myIntent = new Intent(this.context, test2.class);

            myIntent.putExtra("ProjectId", this.myProject.getProjectId());

            myIntent.putExtra("actvListener", this.myactvListener);
            //myIntent.putExtra("listener", this.listener);
            //myIntent.putExtra("listener", this.myactvListener);
            actv.startActivity(myIntent);
            Utility.ActivityAnimation(this.context, myEnum.TypeInOut.fadeIn);
        }

        @Override
        public boolean onLongClick(View v) {
            this.myProject = values.get(getAdapterPosition());
            this.myProject.setContext(this.context);

            AlertDialog.Builder AD = new AlertDialog.Builder(context);

            AD.setTitle(this.myProject.getTitle())
              .setItems(R.array.ListProjectItemsLongClick, this);
            AD.show();
            return true;
        }


        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Log.d("ProjectID is = " , this.myProject.getProjectId() + "");
            Intent intent;
            Activity activity;
            switch (i)
            {
                //Rename
                case 0:
                    com.example.mohammad.workclock2.Dialog.j_edit_project dg_edit = new
                            com.example.mohammad.workclock2.Dialog.j_edit_project(this.context, this.myProject.getProjectId(), this.listener);

                    dg_edit.show();
//                    //-------------------------------------------------------------
//                        intent = new Intent(context, j_edit_project.class);
//                        activity = (Activity)context;
//                    //-------------------------------------------------------------
//
//                    intent.putExtra("ProjectId", this.myProject.getProjectId());
//                    intent.putExtra("listener", this.listener);
//                    activity.startActivity(intent);
//                    Utility.ActivityAnimation(context, myEnum.TypeInOut.fadeIn);
                    break;
                //Delete
                case 1:
                    j_dg_delete_project DP = new j_dg_delete_project(context, this.myProject.getProjectId(),listener);
                    DP.show();
                    break;
                //AddClock
                case 2:
                    //-------------------------------------------------------------
                        intent = new Intent(context, j_new_clock_main.class);
                        activity = (Activity)context;
                    //-------------------------------------------------------------
                    intent.putExtra("ProjectId", this.myProject.getProjectId());
                    intent.putExtra("listener", this.listener);

                    activity.startActivity(intent);
                    Utility.ActivityAnimation(context, myEnum.TypeInOut.fadeIn);
                    break;
            }
        }
    }
}
