package com.example.mohammad.workclock2;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mohammad.workclock2.Adapter.RecycleView.ad_Project;
import com.example.mohammad.workclock2.DB.DB;
import com.example.mohammad.workclock2.Dialog.j_dg_new_project;
import com.example.mohammad.workclock2.Tables.Tables;
import com.example.mohammad.workclock2.Utility.Utility;
import com.example.mohammad.workclock2.interfaces.actvListener;
import com.example.mohammad.workclock2.interfaces.tableDataBaseListener;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class main extends AppCompatActivity implements
    tableDataBaseListener,actvListener, View.OnClickListener
{
    RecyclerView recycleView;
    static ad_Project adapter;
    tableDataBaseListener listener;
    private actvListener myactvListener;
    private FloatingActionButton fab_Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        initialize();
    }

    private void initialize(){

        Tables.Project project = new Tables.Project(this.getBaseContext());
        recycleView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

        try {
            adapter = new ad_Project(project.getItems(), this, this.listener, this.myactvListener);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        recycleView.setAdapter(adapter);
    }

    private void findView(){
        recycleView = (RecyclerView) findViewById(R.id.rv_project);
        this.fab_Main = ((FloatingActionButton) findViewById(R.id.fab_main));
        this.fab_Main.setOnClickListener(this);
        this.listener = new main();
        this.myactvListener = new main();
    }

    @Override
    public <T> void OnItemAdded(T item) {
        this.adapter.Add(item);
    }

    @Override
    public <T> void OnItemDeleted(T item) {
        this.adapter.removeWithIdentifier(((Tables.Project)item).getProjectId());
    }

    @Override
    public <T> void OnItemUpdated(T item) {
        this.adapter.Updated(item);
    }

    @Override
    public void Update_Actv() {
        initialize();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_main:
                j_dg_new_project dg_new_projec = new j_dg_new_project(this, this.listener);
                dg_new_projec.setCancelable(true);
                dg_new_projec.show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

}

