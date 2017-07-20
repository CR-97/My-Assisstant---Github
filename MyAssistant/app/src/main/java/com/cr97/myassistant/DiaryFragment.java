/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.cr97.myassistant;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DiaryFragment extends Fragment implements View.OnClickListener {

    ListView listView;
    DbHelper mDbHelper;

    private View view;
    private FloatingActionButton mfab;
    ArrayAdapter<Assistant> diaryAdapter;
    static List<Assistant> diary_list = new ArrayList<Assistant>();

    static int clickItemindex;

    private RadioGroup rg;
    private RadioButton r1,r2,r3;
    private CheckBox c1,c2,c3,c4,c5,c6;
    int selection;
    int select_id;
    int checked;

    public DiaryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_diary, container, false);
        mfab = (FloatingActionButton) view.findViewById(R.id.diary_fab);
        setListener();

        listView = (ListView)view.findViewById(R.id.listView);

        //Refresh Diary Listview
        mDbHelper = new DbHelper(getActivity().getApplicationContext());
        loading();
        return view;
    }

    private void refreshDiary() {
        ArrayAdapter<Assistant> diaryAdapter = new DiaryAdapter();
        listView.setAdapter(diaryAdapter);
    }

    private void loading(){
        mDbHelper = new DbHelper(getActivity());
        if(!(mDbHelper.getDiaryCount() == 0)){
            diary_list.addAll(mDbHelper.getAllDiary());
        }
        refreshDiary();
    }

    private void setListener() {
        mfab.setOnClickListener(this);
    }

    private class DiaryAdapter extends ArrayAdapter<Assistant>{
        public DiaryAdapter(){
            super(getActivity(),R.layout.list_item_layout , diary_list);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent){
            if(view == null){
                view = getActivity().getLayoutInflater().inflate(R.layout.list_item_layout,parent,false);
            }

            final Assistant currentDiary = diary_list.get(position);

            TextView timetext = (TextView)view.findViewById(R.id.time);
            timetext.setText(currentDiary.get_time());

            TextView daytext = (TextView)view.findViewById(R.id.day);
            daytext.setText(currentDiary.get_day());

            TextView titletext = (TextView)view.findViewById(R.id.title);
            titletext.setText(currentDiary.get_title());

            final TextView notetext = (TextView)view.findViewById(R.id.body);
            notetext.setText(currentDiary.get_note());

            TextView datetext = (TextView)view.findViewById(R.id.date);
            datetext.setText(currentDiary.get_date());

            ImageView moodview = (ImageView)view.findViewById(R.id.item_mood);
            if(currentDiary.get_mood() == "1"){
                moodview.setImageResource(R.drawable.ic_mood_happy);
            }
            else if(currentDiary.get_mood() == String.valueOf(2)){
                moodview.setImageResource(R.drawable.ic_mood_soso);
            }
            else if(currentDiary.get_mood() == String.valueOf(3)){
                moodview.setImageResource(R.drawable.ic_mood_unhappy);
            }

            ImageView weatherview = (ImageView)view.findViewById(R.id.item_weather);
            if(currentDiary.get_weather() == String.valueOf(1)){
                weatherview.setImageResource(R.drawable.ic_weather_sunny);
            }
            else if(currentDiary.get_weather() == String.valueOf(2)){
                weatherview.setImageResource(R.drawable.ic_weather_rainy);
            }
            else if(currentDiary.get_weather() == String.valueOf(3)){
                weatherview.setImageResource(R.drawable.ic_weather_windy);
            }
            else if(currentDiary.get_weather() == String.valueOf(4)){
                weatherview.setImageResource(R.drawable.ic_weather_foggy);
            }
            else if(currentDiary.get_weather() == String.valueOf(5)){
                weatherview.setImageResource(R.drawable.ic_weather_cloud);
            }
            else if(currentDiary.get_weather() == String.valueOf(6)){
                weatherview.setImageResource(R.drawable.ic_weather_snowy);
            }


            Button edit = (Button)view.findViewById(R.id.view);
            Button delete = (Button)view.findViewById(R.id.delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog viewdialog = new Dialog(getActivity());
                    viewdialog.setContentView(R.layout.view_dialog_layout);
                    viewdialog.setCancelable(true);

                    viewdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            viewdialog.cancel();
                        }
                    });

                    TextView v1 = (TextView) viewdialog.findViewById(R.id.view_title_textview);
                    TextView v2 = (TextView) viewdialog.findViewById(R.id.view_note_textview);
                    TextView v3 = (TextView) viewdialog.findViewById(R.id.view_date);
                    TextView v4 = (TextView) viewdialog.findViewById(R.id.view_time);

                    ImageView i1 = (ImageView) viewdialog.findViewById(R.id.imageView2);
                    ImageView i2 = (ImageView) viewdialog.findViewById(R.id.view_weather);

                    final Assistant temp = diary_list.get(clickItemindex);
                    v1.setText(temp.get_title());
                    v2.setText(temp.get_note());
                    v3.setText(temp.get_date());
                    v4.setText("Created at " + temp.get_time());

                    if(temp.get_mood() == String.valueOf(1)){
                       i1.setImageResource(R.drawable.ic_mood_happy);
                    }
                    else if(temp.get_mood() == String.valueOf(2)){
                        i1.setImageResource(R.drawable.ic_mood_soso);
                    }
                    else if(temp.get_mood() == String.valueOf(3)){
                        i1.setImageResource(R.drawable.ic_mood_unhappy);
                    }

                    if(temp.get_weather() == String.valueOf(1)){
                        i2.setImageResource(R.drawable.ic_weather_sunny);
                    }
                    else if(temp.get_weather() == String.valueOf(2)){
                        i2.setImageResource(R.drawable.ic_weather_rainy);

                    }
                    else if(temp.get_weather() == String.valueOf(3)){
                        i2.setImageResource(R.drawable.ic_weather_windy);

                    }
                    else if(temp.get_weather() == String.valueOf(4)){
                        i2.setImageResource(R.drawable.ic_weather_foggy);

                    }
                    else if(temp.get_weather() == String.valueOf(5)){
                        i2.setImageResource(R.drawable.ic_weather_cloud);

                    }
                    else if(temp.get_weather() == String.valueOf(6)){
                        i2.setImageResource(R.drawable.ic_weather_snowy);

                    }
                    viewdialog.show();

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog deldialog = new Dialog(getActivity());
                    deldialog.setContentView(R.layout.delete_dialog_layout);
                    deldialog.setCancelable(false);

                    final Button confirm = (Button) deldialog.findViewById(R.id.sure);
                    Button no = (Button) deldialog.findViewById(R.id.no_cancel);

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDbHelper = new DbHelper(getActivity());
                            clickItemindex = position;
                            mDbHelper.deleteDiary(diary_list.get(clickItemindex));
                            diary_list.remove(clickItemindex);

                            Toast.makeText(getActivity(),"Diary Deleted",Toast.LENGTH_SHORT).show();
                            refreshDiary();
                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deldialog.cancel();
                        }
                    });

                    deldialog.show();
                }
            });
            return view;
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mfab) {
            Toast.makeText(getActivity(),"Creating New Diary" ,Toast.LENGTH_SHORT).show();
            final Dialog createDialog = new Dialog(getActivity());
            createDialog.setContentView(R.layout.create_dialog_layout);
            createDialog.setCancelable(true);

            createDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    createDialog.cancel();
                }
            });

            final Button create = (Button) createDialog.findViewById(R.id.save);
            Button cancel = (Button) createDialog.findViewById(R.id.cancel);

            final EditText ed1 = (EditText) createDialog.findViewById(R.id.create_title);
            final EditText ed2 = (EditText) createDialog.findViewById(R.id.create_description);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH,01);
            cal.setFirstDayOfWeek(Calendar.SUNDAY);
            final String date = cal.get(Calendar.YEAR) + " - " + cal.get(Calendar.MONTH) + " - " + cal.get(Calendar.DAY_OF_MONTH);

            SimpleDateFormat df = new SimpleDateFormat("E",Locale.UK);
            final String day = df.format(cal.get(Calendar.DAY_OF_WEEK));

            int hrs = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            final String time = String.format("%02d : %02d",hrs,min);

            TextView tx1 = (TextView) createDialog.findViewById(R.id.view_day);
            TextView tx2 = (TextView) createDialog.findViewById(R.id.view_time);

            tx1.setText(date);
            tx2.setText(time);

            rg = (RadioGroup) createDialog.findViewById(R.id.radGroup);

            r1 = (RadioButton) createDialog.findViewById(R.id.radButton1);
            r2 = (RadioButton) createDialog.findViewById(R.id.radButton2);
            r3 = (RadioButton) createDialog.findViewById(R.id.radButton3);

            c1 = (CheckBox) createDialog.findViewById(R.id.cb1);
            c2 = (CheckBox) createDialog.findViewById(R.id.cb2);
            c3 = (CheckBox) createDialog.findViewById(R.id.cb3);
            c4 = (CheckBox) createDialog.findViewById(R.id.cb4);
            c5 = (CheckBox) createDialog.findViewById(R.id.cb5);
            c6 = (CheckBox) createDialog.findViewById(R.id.cb6);

            mDbHelper = new DbHelper(getActivity());


            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Radio Button selected
                    select_id = rg.getCheckedRadioButtonId();
                    if (r1.isChecked()) {
                        selection = 1;
                    } else if (r2.isChecked()) {
                        selection = 2;
                    } else if (r3.isChecked()) {
                        selection = 3;
                    }

                    //Checbox Checked
                    if (c1.isChecked()) {
                        checked = 1;
                    } else if (c2.isChecked()) {
                        checked = 2;
                    } else if (c3.isChecked()) {
                        checked = 3;
                    } else if (c4.isChecked()) {
                        checked = 4;
                    } else if (c5.isChecked()) {
                        checked = 5;
                    } else if (c6.isChecked()) {
                        checked = 6;
                    }

                    Assistant diaryList = new Assistant(mDbHelper.getDiaryCount(),
                            String.valueOf(ed1.getText()),
                            String.valueOf(ed2.getText()),
                            String.valueOf(date),
                            String.valueOf(day),
                            String.valueOf(time),
                            String.valueOf(selection),
                            String.valueOf(checked));

                    //if (!diaryExists(diaryList)) {
                        mDbHelper.InsertDiary(diaryList);
                        diary_list.add(diaryList);
                        refreshDiary();

                        Toast.makeText(getActivity(), "Diary Created", Toast.LENGTH_SHORT).show();

                        createDialog.cancel();
                   // } else {
                        //Toast.makeText(getActivity(), "Diary Create Failed", Toast.LENGTH_SHORT).show();
                    //}


                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog.cancel();
                }
            });

            createDialog.show();
        }
    }

}
