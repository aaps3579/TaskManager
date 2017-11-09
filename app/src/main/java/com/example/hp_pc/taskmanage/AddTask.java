package com.example.hp_pc.taskmanage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTask extends AppCompatActivity {

    EditText titleET,descET,dateET,timeET;
    Button addBT;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        titleET=(EditText)findViewById(R.id.name_et);
        descET=(EditText)findViewById(R.id.desc_et);
        dateET=(EditText)findViewById(R.id.date_et);
        timeET=(EditText)findViewById(R.id.time_et);
        addBT=(Button)findViewById(R.id.submitTask_bt);
        database=openOrCreateDatabase("MyDB",MODE_PRIVATE,null);
        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleET.getText().toString().isEmpty()||descET.getText().toString().isEmpty())
                {
                    Toast.makeText(AddTask.this,"Fill All Details",Toast.LENGTH_SHORT).show();
                }
                else {
                    Calendar calendar = Calendar.getInstance();
                    String s = dateET.getText().toString();
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s.substring(0, 2)));
                    calendar.set(Calendar.MONTH, Integer.parseInt(s.substring(3, 5)));
                    calendar.set(Calendar.YEAR, Integer.parseInt(s.substring(6)));
                    String s1 = timeET.getText().toString();
                    calendar.set(Calendar.HOUR, Integer.parseInt(s1.substring(0, 2)));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(s1.substring(3)));
                    Calendar calendar1 = Calendar.getInstance();
                    if (calendar.compareTo(calendar1) > 0) {
                        MyTasks task = new MyTasks(titleET.getText().toString(), descET.getText().toString(), dateET.getText().toString() + "," + timeET.getText().toString(), false);
                        GlobalApp.myTasks.add(task);
                        database.execSQL("INSERT INTO task values('" + titleET.getText().toString() + "','"
                                + descET.getText().toString() + "','" + dateET.getText().toString() + "," + timeET.getText().toString() + "','false')");
                        Toast.makeText(AddTask.this, "Task Added", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                    else
                    {
                        Toast.makeText(AddTask.this,"Date or Time is wrong",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
