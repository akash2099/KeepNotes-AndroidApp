package com.example.sqlliteapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Main3Activity extends AppCompatActivity {
    private static final String CHANNEL_ID = "Reminder";
    DBManager dbManager;
    Button add_update1,cancel1;
    String add_this;
    String title_received1;
    String desc_received1;
    EditText title_value,desc_value;
    String RecordID_received1;
    Integer RecordID_received;
    TextView RemTime, RemDate;
    String RemDate_received,RemTime_received;
    public static String time_pass, date_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        dbManager=new DBManager(this);
        add_update1=(Button)findViewById(R.id.add_update_button1);
        cancel1=(Button)findViewById(R.id.cancel_button1);
        title_value=(EditText)findViewById(R.id.title_value);
        desc_value=(EditText)findViewById(R.id.desc_value);
        RemTime=(TextView)findViewById(R.id.rem_time1);
        RemDate=(TextView)findViewById(R.id.rem_date1);

        RemTime.setVisibility(View.GONE);
        RemDate.setVisibility(View.GONE);
        String yo1="ignore";
        RemTime.setText(yo1);
        RemDate.setText(yo1);

        Bundle b1=getIntent().getExtras();
        title_received1=b1.getString("titlefrom");
        desc_received1=b1.getString("descriptionfrom");
        add_this=b1.getString("add_or_update");
        RecordID_received1=b1.getString("recordno");
        RemTime_received=b1.getString("rem_time");
        RemDate_received=b1.getString("rem_date");
//        Toast.makeText(getApplicationContext(),RecordID_received1+" value",Toast.LENGTH_SHORT).show();
        RecordID_received= Integer.parseInt(RecordID_received1);
        if(add_this.equals("UPDATE")){
            title_value.setText(title_received1);
            desc_value.setText(desc_received1);

            if(!RemTime_received.equalsIgnoreCase("notset")) {
                RemTime.setVisibility(View.VISIBLE);
                RemDate.setVisibility(View.VISIBLE);
                RemTime.setText(RemTime_received);
                RemDate.setText(RemDate_received);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_reminder:
                androidx.fragment.app.FragmentManager fm=getSupportFragmentManager();
                PopInfo popInfo=new PopInfo();

                Bundle bundle1 = new Bundle();
                bundle1.putString("time_value", RemTime.getText().toString());
                bundle1.putString("date_value", RemDate.getText().toString());

                popInfo.setArguments(bundle1);

                popInfo.show(fm,"Show Fragment");
//                Toast.makeText(getApplicationContext(),"Reminder is currently under development!",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete:
                if(add_this.equals("UPDATE")) {
                    AlertDialog.Builder info1=new AlertDialog.Builder(this);
                    info1.setMessage("Are you sure you want to delete this note?")
                            .setTitle("Warning")
                            .setNeutralButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delete_element(RecordID_received1);
                                }
                            })
                            .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Note is not added yet!",Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void SetAlarm(String TimeAlarm,String DateAlarm, String Title_Received,String Desc_Received){

        String[] time_arr = TimeAlarm.split(":",2);
        String[] date_arr = DateAlarm.split("-",3);
        for (String a : time_arr)
            System.out.println("Holathis1"+a);

        for (String b : date_arr)
            System.out.println("Holathis2"+b);

        int Hour=Integer.parseInt(time_arr[0]);
        int Minute=Integer.parseInt(time_arr[1]);
        int Year=Integer.parseInt(date_arr[2]);
        int Month=Integer.parseInt(date_arr[1])-1;
        int Day=Integer.parseInt(date_arr[0]);


        System.out.println("Setting alarm for "+RecordID_received+"at "+Hour+":"+Minute+" and "+Day+"-"+String.valueOf(Integer.valueOf(Month+1))+"-"+Year);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Year);
        calendar.set(Calendar.MONTH, Month);
        calendar.set(Calendar.DAY_OF_MONTH, Day);
        calendar.set(Calendar.HOUR_OF_DAY, Hour);
        calendar.set(Calendar.MINUTE, Minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager am = (AlarmManager)getSystemService  (Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        intent.setAction("com.akashmanna.rem");
//        String msg1=context.getResources().getString(R.string.msg_notify);
        String msg1="Hello from Keep Notes";
        intent.putExtra("AlarmMessage",msg1);
        intent.putExtra("NotiID",RecordID_received1);
        intent.putExtra("Noti_Title",Title_Received);
        intent.putExtra("Noti_Desc",Desc_Received);
        intent.putExtra("Rem_Time",TimeAlarm);
        intent.putExtra("Rem_Date",DateAlarm);
        intent.putExtra("SetNotify","SetNotification");


//        PendingIntent pi = PendingIntent.getBroadcast(this, RecordID_received, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pi = PendingIntent.getBroadcast(this, RecordID_received, intent,0);

//        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY , pi);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
//        SetNotification();
    }

    public void CancelAlarm(){
//        Toast.makeText(this,"Alarm canceled",Toast.LENGTH_SHORT).show();
        AlarmManager am = (AlarmManager)getSystemService (Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyReceiver.class);
        intent.setAction("com.akashmanna.rem");
        String msg1="Hello from Keep Notes";
        intent.putExtra("AlarmMessage",msg1);
        intent.putExtra("NotiID",RecordID_received1);
        intent.putExtra("Noti_Title","ignore");
        intent.putExtra("Noti_Desc","ignore");
        intent.putExtra("Rem_Time","ignore");
        intent.putExtra("Rem_Date","ignore");
        intent.putExtra("SetNotify","SetNotificationNot");

        PendingIntent pi = PendingIntent.getBroadcast(this, RecordID_received, intent,0);
        assert am != null;
        am.cancel(pi);


        System.out.println("Canceling alarm for "+RecordID_received);



//        CancelNotification();
    }



    public void delete_element(String ID1){
        String[] SelectionArgs={ID1};
        int count=dbManager.Delete("ID=?",SelectionArgs);
        if (count>0){
            finish();
            CancelAlarm();
            Toast.makeText(getApplicationContext(),"Note deleted!",Toast.LENGTH_SHORT).show();
//            getdatabaseinfo(1,"ignore");
//            RecordID=0;
        }
        else{
            Toast.makeText(getApplicationContext(),"Can't delete!",Toast.LENGTH_SHORT).show();
        }
    }

    public void save_button_add_update(View view) {
        if(!add_this.equals("UPDATE")){
            // For adding to database
            push_values_database();
        }
        else{
            // For updating in database
            update_database();
        }

    }

    public void push_values_database(){
        if (!title_value.getText().toString().equals("") && !desc_value.getText().toString().equals("")) {
            ContentValues values = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            values.put(DBManager.ColDateTime, currentDateandTime);
            values.put(DBManager.ColTitle, title_value.getText().toString());
            values.put(DBManager.ColDescription, desc_value.getText().toString());

            if(!RemTime.getText().toString().equalsIgnoreCase("ignore")) {
                values.put(DBManager.ColRemTime, RemTime.getText().toString());
                values.put(DBManager.ColRemDate, RemDate.getText().toString());
                SetAlarm(RemTime.getText().toString(), RemDate.getText().toString(),title_value.getText().toString(),desc_value.getText().toString());
            }
            else{
                values.put(DBManager.ColRemTime, "notset");
                values.put(DBManager.ColRemDate, "notset");
                // Then also cancel the alarm
            }
            long id = dbManager.Insert(values);
            if (id > 0) {
                Toast.makeText(getApplicationContext(), "Note Taken!", Toast.LENGTH_SHORT).show();
                finish();
//                etTitle.setText("");
//                etDesc.setText("");
//                getdatabaseinfo(1, "ignore");
//                RecordID = 0;

//                MainActivity ma1=new MainActivity();
//                ma1.getdatabaseinfo(1,"ignore");

            } else
                Toast.makeText(getApplicationContext(), "Failed to take Note", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void update_database(){
        if (!title_value.getText().toString().equals("") && !desc_value.getText().toString().equals("")) {
            ContentValues values = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            values.put(DBManager.ColDateTime, currentDateandTime);
            values.put(DBManager.ColTitle, title_value.getText().toString());
            values.put(DBManager.ColDescription, desc_value.getText().toString());
            values.put(DBManager.ColID, RecordID_received);

            if(!RemTime.getText().toString().equalsIgnoreCase("ignore")) {
                values.put(DBManager.ColRemTime, RemTime.getText().toString());
                values.put(DBManager.ColRemDate, RemDate.getText().toString());
                SetAlarm(RemTime.getText().toString(), RemDate.getText().toString(),title_value.getText().toString(),desc_value.getText().toString());

            }
            else{
                values.put(DBManager.ColRemTime, "notset");
                values.put(DBManager.ColRemDate, "notset");
                CancelAlarm();
            }

            String[] SelectionArgs = {String.valueOf(RecordID_received)};
            int count2 = dbManager.Update(values, "ID=?", SelectionArgs);

            long id = dbManager.Insert(values);
            if (count2 > 0) {
                Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show();
                finish();
//                RecordID = 0;

//                MainActivity ma2=new MainActivity();
//                ma2.getdatabaseinfo(1,"ignore");

            } else
                Toast.makeText(getApplicationContext(), "Choose one Note to update!", Toast.LENGTH_SHORT).show();
//            etTitle.setText("");
//            etDesc.setText("");


//        androidx.fragment.app.FragmentManager fm=getSupportFragmentManager();
//        PopInfo popInfo=new PopInfo();
//        popInfo.show(fm,"Show Fragment");
        }
        else{
            Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
        }
    }
    public void close_act(View view) {
        finish();
    }



    public void setDateTime(String time_received_reminder,String date_received_reminder){
        // Will be called from save button click of Dialog Frag
        RemTime.setText(time_received_reminder);
        RemDate.setText(date_received_reminder);
        RemTime.setVisibility(View.VISIBLE);
        RemDate.setVisibility(View.VISIBLE);
    }

    public void deleteRem(){
        RemTime.setVisibility(View.GONE);
        RemDate.setVisibility(View.GONE);
        String yo="ignore";
        RemTime.setText(yo);
        RemDate.setText(yo);
    }

    public void setTime_pass(String t){
        time_pass=t;
    }
    public void setDate_pass(String d){
        date_pass=d;
    }

}
