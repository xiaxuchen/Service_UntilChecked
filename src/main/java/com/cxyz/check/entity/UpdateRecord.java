package com.cxyz.check.entity;

import com.cxyz.check.util.DateTime;

/**
 * Created by 夏旭晨 on 2018/9/23.
 */

public class UpdateRecord{
    public int _id;
    public User student;
    public int result;
    public TaskCompletion taskCompletion;
    public User updater;
    public String _describe;
    public DateTime update_time;
}
