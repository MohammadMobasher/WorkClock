package com.example.mohammad.workclock2.Adapter.RecycleView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohammad.workclock2.Activities.j_edit_clock;
import com.example.mohammad.workclock2.Activities.test;
import com.example.mohammad.workclock2.Dialog.j_dg_delete_clock;
import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.adapterMethods;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;
import com.example.mohammad.workclock2.myEnum.myEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Mohammad on 3/24/2018.
 */

public class j_ad_clock_item  extends RecyclerView.Adapter<j_ad_clock_item.ViewHolder> implements
        adapterMethods
{
    private List<Tables.AdvanceClock> values;
    public Context context;
    private tableDataBaseListener listener;
    private int See = 0;
    private int time_ = 0;

    public j_ad_clock_item(List<Tables.AdvanceClock> values){
        this.values = values;
    }

    public j_ad_clock_item(List<Tables.AdvanceClock> values, Context context, tableDataBaseListener listener){
        this.values = values;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.rv_clock_item, parent, false);
        v.setClickable(true);
        ViewHolder vh = new ViewHolder(v, this.context, this.listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tables.AdvanceClock myAdClock = this.values.get(position);
        holder.txtFirstDT.setText(myAdClock.getDate_TimeFirst());
        holder.txtُSecondDT.setText(myAdClock.getDate_TimeSecond());
        holder.txtSumDT.setText(myAdClock.getSum());
        holder.imgTypeInsert.setImageResource(holder.imgTypeInsert.getResources().getIdentifier("img" + myAdClock.getTypeInsert(), "mipmap", holder.imgTypeInsert.getContext().getPackageName()));

        holder.IsChangeFirst.setVisibility((myAdClock.getIsChangeFirst()) ? View.VISIBLE : View.INVISIBLE);
        holder.IsChangeSecond.setVisibility((myAdClock.getIsChangeSecond()) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        if(this.values != null)
            return this.values.size();
        else
            return 0;
    }

    @Override
    public <T> void Add(T item) {
        this.values.add(((Tables.AdvanceClock) item));
        notifyDataSetChanged();
    }

    @Override
    public <T> void Updated(T item) {
        for (Tables.AdvanceClock myAC: this.values) {
            if(myAC.getId_First() == ((Tables.AdvanceClock) item).getId_First()) {

                myAC.setDate_TimeFirst(((Tables.AdvanceClock) item).getDate_TimeFirst().toString());
                myAC.setDate_TimeSecond(((Tables.AdvanceClock) item).getDate_TimeSecond().toString());
                myAC.setSum(((Tables.AdvanceClock) item).getSum().toString());
                myAC.setIsChangeFirst(((Tables.AdvanceClock) item).getIsChangeFirst());
                myAC.setIsChangeSecond(((Tables.AdvanceClock) item).getIsChangeSecond());
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
        for(int i = 0; i < this.values.size(); i++){
            if(this.values.get(i).getId_First() == Id){
                this.values.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public <T> void OnDataUpDate(ArrayList<T> listClock) {
        this.values = ((List<Tables.AdvanceClock>) listClock);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener,
            DialogInterface.OnClickListener
    {

        private TextView txtSumDT, txtFirstDT, txtُSecondDT;
        private ImageView IsChangeFirst, IsChangeSecond;

        public View myLayout;
        public tableDataBaseListener listener;
        public Context context;
        public Tables.Clock myClock;
        public ImageView imgTypeInsert;

        private Tables.AdvanceClock myAC;


        public ViewHolder(View itemView , Context context, tableDataBaseListener listener) {

            super(itemView);
            this.context = context;
            this.listener = listener;

            this.txtSumDT = ((TextView) itemView.findViewById(R.id.txt_SumDT));
            this.txtFirstDT = ((TextView) itemView.findViewById(R.id.txt_FirstDT));
            this.txtُSecondDT = ((TextView) itemView.findViewById(R.id.txt_ُSecondDT));
            this.imgTypeInsert = ((ImageView) itemView.findViewById(R.id.img_TypeInsert));

            this.IsChangeFirst = ((ImageView) itemView.findViewById(R.id.img_IsChange_First_Item));
            this.IsChangeSecond = ((ImageView) itemView.findViewById(R.id.img_IsChange_Second_Item));

            Utility.setFont(txtFirstDT, this.context);
            Utility.setFont(txtُSecondDT, this.context);
            Utility.setFont(txtSumDT, this.context);

            myLayout = itemView;

            myLayout.setOnClickListener(this);
            myLayout.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View view) {
//            this.myAC = values.get(getAdapterPosition());
//            Activity actv = (Activity)this.context;
//
//            Intent myIntent = new Intent(this.context, j_edit_clock.class);
//            myIntent.putExtra("ClockId1", this.myAC.getId_First());
//            myIntent.putExtra("ClockId2", this.myAC.getId_Second());
//            myIntent.putExtra("listener", this.listener);
//            actv.startActivity(myIntent);
//            Utility.ActivityAnimation(this.context, myEnum.TypeInOut.fadeIn);
        }

        @Override
        public boolean onLongClick(View view) {
            this.myAC = values.get(getAdapterPosition());

            AlertDialog.Builder AD = new AlertDialog.Builder(context);
            AD.setTitle("حذف ساعت کاری").
                    setItems(R.array.ListClockItemsLongClick, this);
            AD.show();
            return true;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent;
            Activity activity;
            switch (i){
                //Delete
                case 0:
                    j_dg_delete_clock dg_delte = new j_dg_delete_clock(this.context, this.listener, this.myAC.getId_First(), this.myAC.getId_Second());
                    dg_delte.show();
                    break;
                //Update
                case 1:
                    activity = (Activity)this.context;
                    Intent myIntent = new Intent(this.context, j_edit_clock.class);

                    myIntent.putExtra("ClockId1", this.myAC.getId_First());
                    myIntent.putExtra("ClockId2", this.myAC.getId_Second());
                    myIntent.putExtra("listener", this.listener);

                    activity.startActivity(myIntent);
                    Utility.ActivityAnimation(this.context, myEnum.TypeInOut.fadeIn);
                    break;
            }
        }
    }
}
