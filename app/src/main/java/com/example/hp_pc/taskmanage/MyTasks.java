package com.example.hp_pc.taskmanage;

import java.util.Date;

/**
 * Created by HP_PC on 09-11-2017.
 */

public class MyTasks {
    private String title;
    private String description;
    private String deadlineDateTime;
    private boolean isComplete;
    MyTasks(String title,String description,String deadlineDateTime,Boolean isComplete)
    {
        this.title=title;
        this.description=description;
        this.deadlineDateTime=deadlineDateTime;
        this.isComplete=isComplete;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDeadlineDateTime() {
        return deadlineDateTime;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
