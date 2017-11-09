package com.example.hp_pc.taskmanage;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class EditTask extends AppCompatActivity {

    EditText titleET,descET,dateET,timeET;
    Button editBT;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Intent intent=getIntent();
        final int position=intent.getIntExtra("position",-1);
        db=openOrCreateDatabase("MyDB",MODE_PRIVATE,null);
        titleET=(EditText)findViewById(R.id.name_et_edit);
        descET=(EditText)findViewById(R.id.desc_et_edit);
        dateET=(EditText)findViewById(R.id.date_et_edit);
        timeET=(EditText)findViewById(R.id.time_et_edit);
        editBT=(Button)findViewById(R.id.EditTask_bt);
        MyTasks task=GlobalApp.myTasks.get(position);
        titleET.setText(task.getTitle());
        descET.setText(task.getDescription());
        String s=task.getDeadlineDateTime();
        dateET.setText(s.substring(0,s.indexOf(",")));
        timeET.setText(s.substring(s.indexOf(",")+1));
        editBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(titleET.getText().toString().isEmpty()||descET.getText().toString().isEmpty())
                {
                    Toast.makeText(EditTask.this,"Fill All Details",Toast.LENGTH_SHORT).show();
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
                        db.execSQL("delete from task where title='"+GlobalApp.myTasks.get(position).getTitle()+"' and desc='"+GlobalApp.myTasks.get(position).getDescription()+"'");
                        db.execSQL("INSERT INTO task values('" + titleET.getText().toString() + "','"
                                + descET.getText().toString() + "','" + dateET.getText().toString() + "," + timeET.getText().toString() + "','false')");
                        Toast.makeText(EditTask.this, "Task Edited", Toast.LENGTH_SHORT).show();
                        db.close();

                        finish();
                    }
                    else
                    {
                        Toast.makeText(EditTask.this,"Date or Time is wrong",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        }
}
