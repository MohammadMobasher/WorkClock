package com.example.mohammad.workclock2.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohammad.workclock2.Adapter.RecycleView.j_ad_clock_item;
import com.example.mohammad.workclock2.R;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.actvListener;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;
import com.example.mohammad.workclock2.myEnum.myEnum;

/**
 * Created by Mohammad on 3/23/2018.
 **/

public class test extends AppCompatActivity implements
        View.OnClickListener,
        tableDataBaseListener{

    private RecyclerView recycleView;
    private static j_ad_clock_item adapter;
    private tableDataBaseListener listener;
    private static long ProjectId;
    private Tables.Project myProject;
    private static actvListener myactvListener;
    private TextView txt_Title_Toolbar;
    private ImageView img_Close;
    private FloatingActionButton fab_test, fabAutomatic, fabHandy, fabTimer;
    private Animation anim_fab_open, anim_fab_close;
    private boolean IsFade = false;
    private Pair<Integer, Tables.Clock> TypeShoudInsert;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        findView();
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myInflater = getMenuInflater();
        myInflater.inflate(R.menu.menu_delete_items,menu);
        return true;
    }

    private void findView(){
        this.anim_fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        this.anim_fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        this.fab_test = ((FloatingActionButton) findViewById(R.id.fab_test));
        this.fabAutomatic = ((FloatingActionButton) findViewById(R.id.fabAutomatic_test));
        this.fabHandy = ((FloatingActionButton) findViewById(R.id.fabHandy_test));
        this.fabTimer = ((FloatingActionButton) findViewById(R.id.fabTimer_test));

        this.fab_test.setOnClickListener(this);
        this.fabAutomatic.setOnClickListener(this);
        this.fabHandy.setOnClickListener(this);
        this.fabTimer.setOnClickListener(this);

        this.toolbar = ((Toolbar) findViewById(R.id.toolbar_test));
        this.toolbar.inflateMenu(R.menu.menu_delete_items);
        this.txt_Title_Toolbar = ((TextView) findViewById(R.id.txt_Title_Toolbar_test));

        this.img_Close = ((ImageView) findViewById(R.id.img_Close_test));
        this.img_Close.setOnClickListener(this);

        recycleView = (RecyclerView) findViewById(R.id.rv_clock);
        listener = new test();
    }

    private void initialize(){

        getSupportActionBar().hide();
        this.ProjectId = getIntent().getExtras().getLong("ProjectId");
        this.myactvListener = ((actvListener) getIntent().getSerializableExtra("actvListener"));

        Tables.Clock myClock = new Tables.Clock(this);
        this.TypeShoudInsert = myClock.TypeShoudInsert(this.ProjectId);

        this.myProject = new Tables.Project(this);
        this.myProject = this.myProject.getItem(this.ProjectId);

        this.txt_Title_Toolbar.setText(this.myProject.getTitle());

        //getSupportActionBar().setTitle(this.myProject.getTitle());

        recycleView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

        adapter = new j_ad_clock_item(new Tables.AdvanceClock().getAdvanceClock(new Tables.Clock(this).getItems(this.ProjectId)), this, this.listener);

        recycleView.setAdapter(adapter);
    }

    @Override
    public <T> void OnItemAdded(T item) {
        this.adapter.Add(item);
    }

    @Override
    public <T> void OnItemDeleted(T item) {
        this.adapter.removeWithIdentifier(((Tables.AdvanceClock) item).getId_First());

//        if(this.myactvListener != null) {
//            this.myactvListener.Update_Actv();
//        }
    }

    @Override
    public <T> void OnItemUpdated(T item) {
        this.adapter.Updated(item);
        if(this.myactvListener != null) {
            //this.myactvListener.Update_Actv();
            //this.finish();
            //Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_Close_test:
                this.finish();
                Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
                break;
            case R.id.fab_test:
                Tables.Clock myClock_ = new Tables.Clock(this);
                this.TypeShoudInsert = myClock_.TypeShoudInsert(this.ProjectId);

                if(this.IsFade){
                    this.IsFade = false;
                    if(this.TypeShoudInsert.first != -1) {
                        switch (this.TypeShoudInsert.first) {
                            //Automatic
                            case 0:
                                this.fabAutomatic.startAnimation(anim_fab_close);
                                this.fabAutomatic.setVisibility(View.INVISIBLE);
                                break;
                            //Handy
                            case 1:
                                this.fabHandy.startAnimation(anim_fab_close);
                                this.fabHandy.setVisibility(View.INVISIBLE);
                                break;
                            //Timer
                            case 2:
                                this.fabTimer.startAnimation(anim_fab_close);
                                this.fabTimer.setVisibility(View.INVISIBLE);
                                break;
                        }
                    }
                    else{
                        this.fabAutomatic.startAnimation(anim_fab_close);
                        this.fabAutomatic.setVisibility(View.INVISIBLE);
                        this.fabHandy.startAnimation(anim_fab_close);
                        this.fabHandy.setVisibility(View.INVISIBLE);
                        this.fabTimer.startAnimation(anim_fab_close);
                        this.fabTimer.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    this.IsFade = true;
                    if(this.TypeShoudInsert.first != -1){
                        ViewGroup.MarginLayoutParams params;
                        switch (this.TypeShoudInsert.first){
                            //Automatic
                            case 0:
                                params = (ViewGroup.MarginLayoutParams) this.fabAutomatic.getLayoutParams();
                                params.setMargins(0, 0, Utility.dpToPx(this, 17), Utility.dpToPx(this, 80));
                                this.fabAutomatic.setLayoutParams(params);
                                this.fabAutomatic.startAnimation(anim_fab_open);
                                this.fabAutomatic.setVisibility(View.VISIBLE);
                                break;
                            //Handy
                            case 1:
                                params = (ViewGroup.MarginLayoutParams) this.fabHandy.getLayoutParams();
                                params.setMargins(0, 0, Utility.dpToPx(this, 17), Utility.dpToPx(this, 80));
                                this.fabHandy.setLayoutParams(params);
                                this.fabHandy.startAnimation(anim_fab_open);
                                this.fabHandy.setVisibility(View.VISIBLE);
                                break;
                            //Timer
                            case 2:
                                params = (ViewGroup.MarginLayoutParams) this.fabTimer.getLayoutParams();
                                params.setMargins(0, 0, Utility.dpToPx(this, 17), Utility.dpToPx(this, 80));
                                this.fabTimer.setLayoutParams(params);
                                this.fabTimer.startAnimation(anim_fab_open);
                                this.fabTimer.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                    else {
                        this.fabAutomatic.startAnimation(anim_fab_open);
                        this.fabAutomatic.setVisibility(View.VISIBLE);
                        this.fabHandy.startAnimation(anim_fab_open);
                        this.fabHandy.setVisibility(View.VISIBLE);
                        this.fabTimer.startAnimation(anim_fab_open);
                        this.fabTimer.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.fabAutomatic_test:
                this.ShowInsertActv(0);
                break;
            case R.id.fabHandy_test:
                this.ShowInsertActv(1);
                break;
            case R.id.fabTimer_test:
                this.ShowInsertActv(2);
                break;
        }
    }

    private void ShowInsertActv(int TypeInsert){
        this.IsFade = false;
        this.fabAutomatic.setVisibility(View.INVISIBLE);
        this.fabHandy.setVisibility(View.INVISIBLE);
        this.fabTimer.setVisibility(View.INVISIBLE);

        Activity actv = this;
        Intent myIntent = new Intent(this, j_new_clock_main.class);
        myIntent.putExtra("ProjectId", this.ProjectId);
        myIntent.putExtra("testListener", this.listener);
        myIntent.putExtra("TypeInsert", TypeInsert);
        actv.startActivity(myIntent);
        Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeIn);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Utility.ActivityAnimation(this, myEnum.TypeInOut.fadeOut);
    }
}
