package com.example.hp_pc.taskmanage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

     SQLiteDatabase db;
     RecyclerView recyclerView;
     ListAdapter listAdapter;
    Spinner spinner;
    String status[]={"All","Complete","Incomplete"};
    String status_item="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS task(title varchar,desc varchar,datetime varchar,complete varchar)");
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter aa=new ArrayAdapter(this,android.R.layout.simple_spinner_item,status);
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("Selected");
                if(i==0)
                    status_item="All";
                else if(i==1)
                    status_item="Complete";
                else if(i==2)
                    status_item="Incomplete";
                populateTasks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        listAdapter=new ListAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(listAdapter);
        populateTasks();

    }
    public void populateTasks()
    {

        GlobalApp.myTasks.clear();
        String temp="";
        Cursor c=null;
        if (status_item.equals("All"))
            c=db.rawQuery("select * from task order by datetime ASC",null);
        else if(status_item.equals("Complete"))
        {
            c=db.rawQuery("select * from task where complete ='true' order by datetime ASC",null);

        }else
        {
            c=db.rawQuery("select * from task where complete ='false' order by datetime ASC",null);
        }

        if(c.moveToFirst())
        {
            do {

                    MyTasks tasks;

                        if (c.getString(2).equals(temp)) {

                            tasks = new MyTasks(c.getString(0), c.getString(1), "", Boolean.parseBoolean(c.getString(3)));
                        } else {
                            tasks = new MyTasks(c.getString(0), c.getString(1), c.getString(2), Boolean.parseBoolean(c.getString(3)));
                        }

                        System.out.println(tasks.getDeadlineDateTime() + "----" + tasks.getDescription());
                        GlobalApp.myTasks.add(tasks);
                        temp = c.getString(2);

            }while (c.moveToNext());
        }
        listAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.menuAddTask:
            {
                startActivity(new Intent(this,AddTask.class));
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTasks();
    }


}
