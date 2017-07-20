package com.cr97.myassistant;

/**
 * Created by chuah_000 on 13/7/2017.
 */

public class Plan {
    private String _task;
    private int _planid;


    public Plan(int planid, String task){
        _planid = planid;
        _task = task;
    }

    public int get_planid(int position) {
        return _planid;
    }

    public String get_task() {
        return _task;
    }

    public void set_planid(int _planid) {
        this._planid = _planid;
    }

    public void set_task(String _task) {
        this._task = _task;
    }


}
