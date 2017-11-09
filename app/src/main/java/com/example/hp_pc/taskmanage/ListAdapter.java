package com.example.hp_pc.taskmanage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;


/**
 * Created by HP_PC on 09-11-2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder>  {

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyTasks task=GlobalApp.myTasks.get(position);
        holder.title.setText(task.getTitle());
        holder.desc.setText(task.getDescription());
        if(task.getDeadlineDateTime().isEmpty())
        {
            holder.date.setVisibility(View.GONE);
        }
        else
        {
            holder.date.setText(task.getDeadlineDateTime());
        }
        if(task.isComplete())
        {
            holder.bt_edit.setEnabled(false);
            holder.bt_done.setEnabled(false);
        }
        Calendar calendar=Calendar.getInstance();
        String s=task.getDeadlineDateTime();

        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s.substring(0, 2)));
        calendar.set(Calendar.MONTH, Integer.parseInt(s.substring(3, 5)));
        calendar.set(Calendar.YEAR, Integer.parseInt(s.substring(6,s.indexOf(","))));
        String s1 = s.substring(s.indexOf(",")+1);
        calendar.set(Calendar.HOUR, Integer.parseInt(s1.substring(0, 2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(s1.substring(3)));
        Calendar calendar1=Calendar.getInstance();
        if(calendar.compareTo(calendar1)<0)
        {
            holder.bt_edit.setEnabled(false);
            holder.bt_done.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return GlobalApp.myTasks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, desc;
        Button bt_edit,bt_del,bt_done;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.row_title);
            date = (TextView) view.findViewById(R.id.row_date);
            desc = (TextView) view.findViewById(R.id.row_desc);
            bt_del=(Button)view.findViewById(R.id.delete_task);
            bt_edit=(Button)view.findViewById(R.id.edit_task);
            bt_done=(Button)view.findViewById(R.id.done_bt);
            bt_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SQLiteDatabase database=view.getContext().openOrCreateDatabase("MyDB",Context.MODE_PRIVATE,null);
                    database.execSQL("update task set complete='true' where title='"+title.getText()+"' and desc ='"+desc.getText()+"'");
                    database.close();
                    ((MainActivity) view.getContext()).onResume();
                }
            });
            bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(view.getContext(),EditTask.class);
                    intent.putExtra("position",getPosition());
                    view.getContext().startActivity(intent);
                }
            });
            bt_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    System.out.println("clicked del");
                    final AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Are your sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    String t= (String) title.getText();
                                    String d=(String) desc.getText();
                                    SQLiteDatabase db= view.getContext().openOrCreateDatabase("MyDB",Context.MODE_PRIVATE,null);
                                    db.execSQL("delete from task where title='"+t+"' and desc='"+d+"'");
                                    db.close();
                                    ((MainActivity) view.getContext()).onResume();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();

                }

            });

        }
    }
}
