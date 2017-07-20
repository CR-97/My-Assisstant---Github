package com.cr97.myassistant;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuah_000 on 2/7/2017.
 */

public class PlanFragment extends Fragment implements View.OnClickListener {
    private View mView;

    private FloatingActionButton mFab;
    static List<Plan> plan_list = new ArrayList<Plan>();

    static int clickedItemIndex;

    ListView mListView;
    DbHelper mDbHelper;

    public PlanFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_plan, container, false);

        //Refesh Plan ListView
        mFab = (FloatingActionButton) mView.findViewById(R.id.fab);
        setListener();

        mListView = (ListView) mView.findViewById(R.id.plistView);
        mDbHelper = new DbHelper(getActivity().getApplicationContext());
        loading();

        return mView;
    }

    private void setListener(){
        mFab.setOnClickListener(this);
    }

    private void refreshList() {
        ArrayAdapter<Plan> planAdapter = new PlanAdapter();
        mListView.setAdapter(planAdapter);

    }

    private void loading(){
        mDbHelper = new DbHelper(getActivity());
        if(!(mDbHelper.getPlanCount() == 0)){
            plan_list.addAll(mDbHelper.getAllPlan());
        }
        refreshList();
    }

    private class PlanAdapter extends ArrayAdapter<Plan>{

        public PlanAdapter(){
            super(getActivity(), R.layout.plan_layout, plan_list );
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent){
            if(view == null){
                view = getActivity().getLayoutInflater().inflate(R.layout.plan_layout,parent,false);

            }

            final Plan currentPlan = plan_list.get(position);

            TextView titletext = (TextView) view.findViewById(R.id.taskDescription);
            titletext.setText(currentPlan.get_task());

            Button delete = (Button)view.findViewById(R.id.done);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDbHelper = new DbHelper(getActivity());
                    clickedItemIndex = position;
                    mDbHelper.deleteplan(plan_list.get(clickedItemIndex));
                    plan_list.remove(clickedItemIndex);

                    Toast.makeText(getActivity(),"Plan Deleted",Toast.LENGTH_SHORT).show();
                    refreshList();

                }
            });

            return view;
        }


    }

    @Override
    public void onClick(View v) {
        if(v == mFab) {
            final Dialog adddialog = new Dialog(getActivity());
            adddialog.setContentView(R.layout.add_plan_layout);
            adddialog.setCancelable(false);
            adddialog.show();
            Button confirm = (Button) adddialog.findViewById(R.id.plan);
            Button cancel = (Button) adddialog.findViewById(R.id.no_thank);
            final EditText plan = (EditText) adddialog.findViewById(R.id.add_plan);
            mDbHelper = new DbHelper(getActivity());


            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Plan planList = new Plan(mDbHelper.getPlanCount(), String.valueOf(plan.getText()));
                    // if(!taskExists(planList)) {
                    mDbHelper.insertplan(planList);
                    plan_list.add(planList);
                    refreshList();

                    Toast.makeText(getActivity(), "Plan Created", Toast.LENGTH_SHORT).show();

                    adddialog.cancel();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adddialog.cancel();
                }
            });

        }
    }
}
