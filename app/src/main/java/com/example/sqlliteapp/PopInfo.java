package com.example.sqlliteapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class PopInfo extends DialogFragment{
    View view;
    TextView datetime_pop;
    TextView title_pop;
    TextView desc_pop;
    Button edit_pop,delete_pop;
    Button cancel_pop;
    LayoutInflater inflater;
    ViewGroup container;
    Bundle savedInstanceState;
    String id_value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.pop_info, container, false);
        datetime_pop=(TextView)view.findViewById(R.id.datetime_tv3);
        title_pop=(TextView)view.findViewById(R.id.title_tv3);
        desc_pop=(TextView)view.findViewById(R.id.desc_tv3);
        edit_pop=(Button)view.findViewById(R.id.edit_popup);
        delete_pop=(Button)view.findViewById(R.id.delete_popup);

        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("datetime")))
            datetime_pop.setText(getArguments().getString("datetime"));
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("title")))
            title_pop.setText(getArguments().getString("title"));
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("description")))
            desc_pop.setText(getArguments().getString("description"));
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getCharSequence("IDvalue")))
            id_value=getArguments().getString("IDvalue");
//        System.out.println("Hello World1"+id_value);

//        cancel_pop=(Button)view.findViewById(R.id.cancel_popup);
//        title_pop.setSelected(true);
        title_pop.setMovementMethod(new ScrollingMovementMethod());
//        title_pop.setHorizontallyScrolling(true);
        desc_pop.setMovementMethod(new ScrollingMovementMethod());

//        edit_pop.setOnClickListener(this);
        edit_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),"After modifying please press on UPDATE button",Toast.LENGTH_SHORT).show();
                dismiss();
                MainActivity ma1=(MainActivity)getActivity();
                ma1.update_element(title_pop.getText().toString(),desc_pop.getText().toString());


            }
        });
        delete_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder info1=new AlertDialog.Builder(getContext());
                info1.setMessage("Are you sure you want to delete this note?")
                        .setTitle("Warning")
                        .setNeutralButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                                MainActivity ma=(MainActivity)getActivity();
//                System.out.println("Hello World"+id_value);
                                ma.delete_element(id_value);
                            }
                        })
                        .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

//                Toast.makeText(getContext(),"DELETE",Toast.LENGTH_SHORT).show();
//                dismiss();
//                MainActivity ma=(MainActivity)getActivity();
////                System.out.println("Hello World"+id_value);
//                ma.delete_element(id_value);

            }
        });

        return view;
    }



//    public void setvalues(String datetime1,String title1,String desc1){
////        datetime_pop.setText(datetime1);
////        title_pop.setText(title1);
////        desc_pop.setText(desc1);
//        Toast.makeText(getContext(),datetime1+title1+desc1,Toast.LENGTH_SHORT).show();
//    }




}
