package com.example.todoapp;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.todoapp.Room.TaskDatabase;
import com.example.todoapp.Room.TaskEntry;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;

public class NewTask extends Fragment {

    private Button mButton;
    private EditText mEditText;
    private EditText mEditText2;
    private TextView mTextView;
    private Button btn_onsave;

    private TaskDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_task, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = TaskDatabase.getInstance(getContext());

        mEditText = view.findViewById(R.id.task_name);
        mButton = view.findViewById(R.id.button_timepicker);
        mEditText2 = view.findViewById(R.id.task_description);
        mTextView = (TextView) view.findViewById(R.id.time);
        btn_onsave = view.findViewById(R.id.button_onsave);

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
//        c.set(Calendar.MINUTE, minute);
//        c.set(Calendar.SECOND, 0);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mTextView.setText(hourOfDay+" : "+minute);

//                        Toast.makeText(getContext(), "ontimeset", Toast.LENGTH_LONG).show();
                        startAlarm(c);
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(getContext()));
//              DialogFragment timePicker = new TimePickerFragment();
                timePickerDialog.show();
            }
        });

        btn_onsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
                Navigation.findNavController(view).navigate(R.id.action_newTask_to_todoList2);
            }
        });
    }


//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
//        TextView textView = (TextView) view.findViewById(R.id.time);
//        textView.setText(hourOfDay+" : "+ minute);
//
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
//        c.set(Calendar.MINUTE, minute);
//        c.set(Calendar.SECOND, 0);
//
//        Toast.makeText(getContext(), "ontimeset", Toast.LENGTH_LONG).show();
//        startAlarm(c);
//    }

    public void onSaveButtonClicked(){
        String name = mEditText.getText().toString();
        String description = mEditText2.getText().toString();
        Date date = new Date();
        String time = mTextView.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                TaskEntry taskEntry = new TaskEntry(name, description, date, time);
                db.getTaskDAO().insertTask(taskEntry);
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0 );

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//        Toast.makeText(getContext(), "startAlarm", Toast.LENGTH_LONG).show();
    }

}