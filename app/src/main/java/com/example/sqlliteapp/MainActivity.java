package com.example.sqlliteapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBManager dbManager;
//    EditText etTitle;
//    EditText etDesc;
    long RecordID;
    String RecordTitle;
    String RecordDesc;
    String RecordDateRem;
    String RecordTimeRem;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(),"Welcome back!",Toast.LENGTH_SHORT).show();
        dbManager=new DBManager(this);
//        etTitle=(EditText)findViewById(R.id.et1);
//        etDesc=(EditText)findViewById(R.id.et2);
        getdatabaseinfo(1,"ignore");
        createNotificationChannel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        SearchView sv=(SearchView)menu.findItem(R.id.menu_search).getActionView();
        SearchManager sm=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getdatabaseinfo(2,query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getdatabaseinfo(2,newText);

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_help:
                AlertDialog.Builder info=new AlertDialog.Builder(this);
                info.setMessage("Project started on 23-08-2020\nby AKASH MANNA\nFEATURES:\nADD, DELETE, UPDATE and SEARCH Notes along with setting REMINDERs at any date and time.")
                        .setTitle("Information")
                        .setPositiveButton("Source Code", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent webintent=new Intent(getApplicationContext(),Main2Activity.class);
                                startActivity(webintent);
//                                Toast.makeText(getApplicationContext(),"Hi!",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

                return true;
            case R.id.menu_settings:
//                AlertDialog.Builder info1=new AlertDialog.Builder(this);
//                info1.setMessage("All information about information")
//                        .setTitle("Ignore")
//                        .setNeutralButton("EDIT", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(getApplicationContext(),"After editing please press on UPDATE!",Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(getApplicationContext(),"Note DELETED Successfully!",Toast.LENGTH_SHORT).show();
//                            }
//                        })
//
//                        .show();
                Toast.makeText(getApplicationContext(),"Settings is currently under development!",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void createNotificationChannel() {
        String CHANNEL_ID="ReminderID";
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setLightColor(Color.RED);
            channel.setVibrationPattern(new long[]{0});
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

/*
    // Add notes to database old code
    public void buSave(View view) {
        push_values_database();
    }


    public void push_values_database(){
        if (!etTitle.getText().toString().equals("") && !etDesc.getText().toString().equals("")) {
            ContentValues values = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            values.put(DBManager.ColDateTime, currentDateandTime);
            values.put(DBManager.ColTitle, etTitle.getText().toString());
            values.put(DBManager.ColDescription, etDesc.getText().toString());
            long id = dbManager.Insert(values);
            if (id > 0) {
                Toast.makeText(getApplicationContext(), "Note Taken!", Toast.LENGTH_SHORT).show();
                etTitle.setText("");
                etDesc.setText("");
                getdatabaseinfo(1, "ignore");
                RecordID = 0;
            } else
                Toast.makeText(getApplicationContext(), "Failed to take Note", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
        }
    }

*/
    ArrayList<AdapterItems> listnewsData = new ArrayList<AdapterItems>();
    MyCustomAdapter myadapter;

//    public void buLoad(View view) {
//        getdatabaseinfo(2,"ignore");
//    }

    public void getdatabaseinfo(int count1,String to_search){


        //add data and view it
        listnewsData.clear();
        //String[] projection={"","",""};
        Cursor cursor;
        if (count1==1) {
            cursor = dbManager.query(null, null, null, DBManager.ColID);
        }
        else{
            String[] SelectionsArgs={"%"+to_search+"%","%"+to_search+"%"};
            cursor = dbManager.query(null, "Title like ? or Description like ?", SelectionsArgs, DBManager.ColID);
        }
        //If wanted to select all data then give null in selection
        if(cursor.moveToFirst()){
//            String tableData="";
            do{
                /*
                tableData=tableData+cursor.getString(cursor.getColumnIndex(DBManager.ColDateTime))+" :- "+cursor.getString(cursor.getColumnIndex(DBManager.ColTitle))+","+
                        cursor.getString(cursor.getColumnIndex(DBManager.ColDescription))+" :: ";
                */

                listnewsData.add(new AdapterItems(cursor.getLong(cursor.getColumnIndex(DBManager.ColID)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColDateTime)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColTitle)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColDescription)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColRemTime)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColRemDate))));

            }while (cursor.moveToNext());
//            Toast.makeText(getApplicationContext(),tableData,Toast.LENGTH_SHORT).show();

        }

        myadapter=new MyCustomAdapter(listnewsData);


        final ListView lsNews=(ListView)findViewById(R.id.lv_all);
        lsNews.setAdapter(myadapter);//intisal with data

//        lsNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                final AdapterItems s1=listnewsData.get(position);
//                TextView txt_datetime=(TextView)view.findViewById(R.id.datetime_tv2);
//                TextView txt_title=(TextView)view.findViewById(R.id.title_tv2);
//                TextView txt_desc=(TextView)view.findViewById(R.id.desc_tv2);
//                androidx.fragment.app.FragmentManager fm=getSupportFragmentManager();
//                PopInfo popInfo=new PopInfo();
//                popInfo.datetime_pop.setText(txt_datetime.getText().toString());
//                popInfo.title_pop.setText(txt_title.getText().toString());
//                popInfo.desc_pop.setText(txt_desc.getText().toString());
//                popInfo.show(fm,"Show Fragment");
//            }
//        });
//        lsNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClicked(AdapterView<?> parent, View view, int position, long id) {
//                final AdapterItems s1=listnewsData.get(position);
//                TextView txt_datetime=(TextView)view.findViewById(R.id.datetime_tv2);
//                TextView txt_title=(TextView)view.findViewById(R.id.title_tv2);
//                TextView txt_desc=(TextView)view.findViewById(R.id.desc_tv2);
//                androidx.fragment.app.FragmentManager fm=getSupportFragmentManager();
//                PopInfo popInfo=new PopInfo();
//                popInfo.datetime_pop.setText(txt_datetime.getText().toString());
//                popInfo.title_pop.setText(txt_title.getText().toString());
//                popInfo.desc_pop.setText(txt_desc.getText().toString());
//                popInfo.show(fm,"Show Fragment");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }
//    PopInfo pp=new PopInfo();

//    public void checkthis1(View view) {
//        Toast.makeText(pp.getContext(),"hi",Toast.LENGTH_SHORT).show();
//
////        androidx.fragment.app.FragmentManager fm=getSupportFragmentManager();
////        PopInfo popInfo=new PopInfo();
////        popInfo.show(fm,"Show Fragment");
//    }

/*
    // Update note old code
    public void buUpdate(View view) {
        if (!etTitle.getText().toString().equals("") && !etDesc.getText().toString().equals("")) {
            ContentValues values = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            values.put(DBManager.ColDateTime, currentDateandTime);
            values.put(DBManager.ColTitle, etTitle.getText().toString());
            values.put(DBManager.ColDescription, etDesc.getText().toString());
            values.put(DBManager.ColID, RecordID);
            String[] SelectionArgs = {String.valueOf(RecordID)};
            int count2 = dbManager.Update(values, "ID=?", SelectionArgs);

            long id = dbManager.Insert(values);
            if (count2 > 0) {
                Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show();
                getdatabaseinfo(1, "ignore");
                RecordID = 0;
            } else
                Toast.makeText(getApplicationContext(), "Choose one Note to update!", Toast.LENGTH_SHORT).show();
            etTitle.setText("");
            etDesc.setText("");
//        androidx.fragment.app.FragmentManager fm=getSupportFragmentManager();
//        PopInfo popInfo=new PopInfo();
//        popInfo.show(fm,"Show Fragment");
        }
        else{
            Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void update_element(String title_received,String description_received){
        etTitle.setText(title_received);
        etDesc.setText(description_received);

    }
*/

    public void update_element_new(){
        // For updating notes
        String title_received=RecordTitle;
        String description_received= RecordDesc;
        String RecordID_string=String.valueOf(RecordID);
        String time_rem_received=RecordTimeRem;
        String date_rem_received=RecordDateRem;
        Intent add_edit_act_intent=new Intent(getApplicationContext(),Main3Activity.class);
        add_edit_act_intent.putExtra("titlefrom",title_received);
        add_edit_act_intent.putExtra("descriptionfrom",description_received);
        add_edit_act_intent.putExtra("add_or_update","UPDATE");
        add_edit_act_intent.putExtra("recordno",RecordID_string);
        add_edit_act_intent.putExtra("rem_time",time_rem_received);
        add_edit_act_intent.putExtra("rem_date",date_rem_received);
        startActivityForResult(add_edit_act_intent,4);
//        getdatabaseinfo(1, "ignore");
//        RecordID=0;
}

/*
old code for delete
    public void delete_element(String ID1){
        String[] SelectionArgs={ID1};
        int count=dbManager.Delete("ID=?",SelectionArgs);
        if (count>0){
            getdatabaseinfo(1,"ignore");
            RecordID=0;
        }
    }
*/
    public void bu_add_edit_activity(View view) {
        // For adding new notes


        // Setting recordid to maximum exact size of the row+1
        RecordID=dbManager.RowCount()+1;

        String RecordID_string=String.valueOf(RecordID);
        Intent add_edit_act_intent1=new Intent(getApplicationContext(),Main3Activity.class);
        add_edit_act_intent1.putExtra("titlefrom","ignore");
        add_edit_act_intent1.putExtra("descriptionfrom","ignore");
        add_edit_act_intent1.putExtra("add_or_update","ADD");
        add_edit_act_intent1.putExtra("recordno",RecordID_string);
        add_edit_act_intent1.putExtra("rem_time","ignore");
        add_edit_act_intent1.putExtra("rem_date","ignore");
        startActivityForResult(add_edit_act_intent1,3);
//        getdatabaseinfo(1, "ignore");
//        RecordID=0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==3)
        {
//            do the things u wanted
            getdatabaseinfo(1, "ignore");
            RecordID=0;
        }
        else if(requestCode==4){
            getdatabaseinfo(1, "ignore");
            RecordID=0;
        }
    }


    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<AdapterItems> listnewsDataAdpater ;

        public MyCustomAdapter(ArrayList<AdapterItems>  listnewsDataAdpater) {
            this.listnewsDataAdpater=listnewsDataAdpater;
        }


        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        TextView txt_datetime;
        TextView txt_datetime_rem, txt_title, txt_desc;
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_ticket, null);

            final   AdapterItems s = listnewsDataAdpater.get(position);

//            txt_datetime=(TextView)myView.findViewById(R.id.datetime_tv2);
//            txt_datetime.setText(s.DateTime);

            String rem_DateTime=s.Time+" "+s.Date;
            txt_datetime_rem=(TextView)myView.findViewById(R.id.date_time_id_rem);

            if(s.Time.equalsIgnoreCase("notset")) {
                txt_datetime_rem.setVisibility(View.GONE);
            }
            else {
                txt_datetime_rem.setVisibility(View.VISIBLE);
                txt_datetime_rem.setText(rem_DateTime);
            }
            txt_title=(TextView)myView.findViewById(R.id.title_tv2);
            txt_title.setText(s.Title);
//            txt_title.setMovementMethod(new ScrollingMovementMethod());
//            txt_title.setHorizontallyScrolling(true);
            txt_title.setSelected(true);

            txt_desc=(TextView)myView.findViewById(R.id.desc_tv2);
            txt_desc.setText(s.Description);

//            txt_desc.setMovementMethod(new ScrollingMovementMethod());
//            txt_desc.setHorizontallyScrolling(true);



            txt_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    old code for dialog fragment delete and edit
                    androidx.fragment.app.FragmentManager fm=getSupportFragmentManager();
                    PopInfo popInfo=new PopInfo();

                    Bundle bundle = new Bundle();
                    bundle.putString("datetime", s.DateTime);
                    bundle.putString("title", s.Title);
                    bundle.putString("description", s.Description);

                    bundle.putString("IDvalue",String.valueOf(RecordID));

                    popInfo.setArguments(bundle);

                    popInfo.show(fm,"Show Fragment");
                    */
                    RecordID=s.ID;
                    RecordTitle=s.Title;
                    RecordDesc=s.Description;
                    RecordDateRem=s.Date;
                    RecordTimeRem=s.Time;
                    update_element_new();


//                    Toast.makeText(getApplicationContext(),txt_datetime.getText().toString()+txt_datetime.getText().toString()+txt_desc.getText().toString(),Toast.LENGTH_SHORT).show();
//                    popInfo.setvalues(txt_datetime.getText().toString(),txt_datetime.getText().toString(),txt_desc.getText().toString());

//                    pop1.getApplicationContext();
//                    pop1.datetime_pop.setText(txt_datetime.getText().toString());
//                    pop1.title_pop.setText(txt_title.getText().toString());
//                    pop1.desc_pop.setText(txt_desc.getText().toString());

                }
            });
            return myView;
        }

    }

}
