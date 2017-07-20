package com.cr97.myassistant;

/**
 * Created by chuah_000 on 13/7/2017.
 */

public class Assistant {
    private String  _title, _note, _date, _day, _time, _mood, _weather;
    private int _id ;



    public Assistant(int id, String title, String note, String date, String day, String time, String mood, String weather){
        _id = id;
        _title = title;
        _note = note;
        _date = date;
        _day = day;
        _time = time;
        _mood = mood;
        _weather = weather;
    }


    public int get_id() {
        return _id;
    }

    public String get_title() {
        return _title;
    }

    public String get_note() {
        return _note;
    }

    public String get_date() {
        return _date;
    }

    public String get_day() {
        return _day;
    }

    public String get_time() {
        return _time;
    }

    public String get_mood() {
        return _mood;
    }

    public String get_weather() {
        return _weather;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_note(String _note) {
        this._note = _note;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public void set_day(String _day) {
        this._day = _day;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public void set_mood(String _mood) {
        this._mood = _mood;
    }

    public void set_weather(String _weather) {
        this._weather = _weather;
    }


}
