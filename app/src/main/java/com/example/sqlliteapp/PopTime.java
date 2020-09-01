package com.example.sqlliteapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PopTime extends DialogFragment{
    View view;
    Button save_timepick,cancel_timepick;
    TimePicker tp;
    String time_get_time,date_get_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.pop_time, container, false);
        save_timepick=(Button)view.findViewById(R.id.time_save_picker);
        cancel_timepick=(Button)view.findViewById(R.id.time_cancel_picker);
        tp=(TimePicker)view.findViewById(R.id.tp);



        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("time_value1")))
            time_get_time=getArguments().getString("time_value1");
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("date_value1")))
            date_get_time=getArguments().getString("date_value1");

        if(!time_get_time.equalsIgnoreCase("ignore")) {
            String[] time_arr = time_get_time.split(":", 2);
//            String[] date_arr = date_get_time.split("-", 3);
            for (String a : time_arr)
                System.out.println("Holathis1" + a);

//            for (String b : date_arr)
//                System.out.println("Holathis2" + b);

            int Hour = Integer.parseInt(time_arr[0]);
            int Minute = Integer.parseInt(time_arr[1]);
//            int Year = Integer.parseInt(date_arr[0]);
//            int Month = Integer.parseInt(date_arr[1]);
//            int Day = Integer.parseInt(date_arr[2]);

            tp.setHour(Hour);
            tp.setMinute(Minute);
        }

        save_timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"Save timepick",Toast.LENGTH_SHORT).show();
                String timeOn=tp.getHour()+":"+tp.getMinute();
                System.out.println("MEWW"+timeOn);
                String[] time_arr = timeOn.split(":",2);

                for (String a : time_arr)
                    System.out.println("Holathis1"+a);

                System.out.println("Holathis12"+time_arr[0]);
                System.out.println("Holathis12"+time_arr[1]);


                androidx.fragment.app.FragmentManager fragmentManager5=getFragmentManager();


                PopInfo maa3=new PopInfo();
//                maa3.setTime_pass(timeOn);
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("time_value", RemTime.getText().toString());
//                bundle1.putString("date_value", RemDate.getText().toString());
//
//                popInfo.setArguments(bundle1);
                Bundle bundleTime_back = new Bundle();
                bundleTime_back.putString("time_value",timeOn);
                bundleTime_back.putString("date_value", date_get_time);

                maa3.setArguments(bundleTime_back);

                assert fragmentManager5 != null;
                maa3.show(fragmentManager5,"Back to dialog save time");




                dismiss();
            }
        });
        cancel_timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"Cancel timepick",Toast.LENGTH_SHORT).show();
                androidx.fragment.app.FragmentManager fragmentManager6=getFragmentManager();


                PopInfo ma3=new PopInfo();
                Bundle bundleTime_back = new Bundle();
                bundleTime_back.putString("time_value",time_get_time);
                bundleTime_back.putString("date_value", date_get_time);

                ma3.setArguments(bundleTime_back);

                assert fragmentManager6 != null;
                ma3.show(fragmentManager6,"Back to dialog cancel time");


                dismiss();
            }
        });
        return view;
    }



}
