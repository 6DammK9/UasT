package hk.ust.comp4521.UasT;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import hk.ust.comp4521.UasT.data.CalendarEvent;
import hk.ust.comp4521.UasT.data.Database;

public class Record extends BaseFragment {
    private View view;
    private Spinner freqSpin;
    private Spinner remSpin;
    private EditText TitleText;
    private TextView BodyText;
    private TextView FromText;
    private TextView ToText;
    private TextView rem_inf;
    private EditText LocText;
    private Button confirmButton;
    private Button deleteButton;
    private Date FromTime;
    private Date ToTime;
    private Date Remind;
    int CalEventsSelectedIndex;
    CalendarFragment calendarFragment;
    boolean createMode;
    PendingIntent pi; //For Reminder
    AlarmManager am; //For Reminder
    Intent ReminderIntent; //For Reminder

    public void setParam(Boolean create, int position, CalendarFragment calendarFragment) {
        this.createMode = create;
        this.calendarFragment = calendarFragment;
        if (create) {
            calendarFragment.CalEvents.add(new CalendarEvent(new Date(calendarFragment.cal_m.getDate()).getTime()));
            this.CalEventsSelectedIndex = calendarFragment.CalEvents.size() - 1;
        } else {
            this.CalEventsSelectedIndex = calendarFragment.CalEventsFilteredIndex.get(position);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.record, container, false);

        freqSpin = (Spinner) view.findViewById(R.id.freqSpin);
        remSpin = (Spinner) view.findViewById(R.id.remSpin);

        TitleText = (EditText) view.findViewById(R.id.titleText);
        BodyText = (TextView) view.findViewById(R.id.bodyText);
        FromText = (TextView) view.findViewById(R.id.time_from);
        ToText = (TextView) view.findViewById(R.id.time_to);
        LocText = (EditText) view.findViewById(R.id.locationText);
        rem_inf = (TextView) view.findViewById(R.id.rem_inf);
        confirmButton = (Button) view.findViewById(R.id.submitbutton);
        deleteButton = (Button) view.findViewById(R.id.deletebutton);

        FromText.setOnClickListener(FromTimeListener);
        ToText.setOnClickListener(ToTimeListener);
        rem_inf.setOnClickListener(ReminderListener);
        remSpin.setOnItemSelectedListener(remSpinListener);
        confirmButton.setOnClickListener(confirmButtonListener);
        deleteButton.setOnClickListener(deleteButtonListener);

        if (createMode) {
            confirmButton.setText("Create");
        } //Has been created in setParam
        else {
            confirmButton.setText("Modify");
        }

        freqSpin.setSelection(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getFreqIndex());
        TitleText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getTitle());
        BodyText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getBody());
        FromText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getFrom().toString());
        ToText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getTo().toString());
        LocText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getLoc());
        if (calendarFragment.CalEvents.get(CalEventsSelectedIndex).getRemind() != null) {
            rem_inf.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getRemind().toString());
            remSpin.setSelection(1);
        } else
            rem_inf.setText("");

        //For deleting saved alarm
        FromTime = CalendarEvent.StringToDate(FromText.getText().toString());
        ReminderIntent = new Intent(view.getContext(), PlayReceiver.class);
        ReminderIntent.putExtra("msg", "hk.ust.comp4521.UasT.reminder_alarm");
        ReminderIntent.putExtra("remTitle", TitleText.getText().toString());
        ReminderIntent.putExtra("remTime", FromTime.toString());
        ReminderIntent.addCategory(FromTime.toString());

        pi = PendingIntent.getBroadcast(view.getContext(), 1, ReminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return view;
    }

    @Override
    public String getTitle() {
        return "Calendar";
    }

    public final OnClickListener FromTimeListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity main = (MainActivity) getActivity();
            choose_time fragment = new choose_time();
            fragment.setParam(FromText.getText().toString(), Record.this, "FromText");

            main.gotoFragment(1, fragment);
        }
    };

    public final OnClickListener ToTimeListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity main = (MainActivity) getActivity();
            choose_time fragment = new choose_time();
            fragment.setParam(ToText.getText().toString(), Record.this, "ToText");

            main.gotoFragment(1, fragment);
        }
    };

    public final OnClickListener ReminderListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (remSpin.getSelectedItem().toString().equals("On")) {
                MainActivity main = (MainActivity) getActivity();
                choose_time fragment = new choose_time();
                fragment.setParam(rem_inf.getText().toString(), Record.this, "rem_inf");

                main.gotoFragment(1, fragment);
            } else if (remSpin.getSelectedItem().toString().equals("Before")) {
                MainActivity main = (MainActivity) getActivity();
                choose_before fragment = new choose_before();
                fragment.setParam(FromText.getText().toString(), rem_inf.getText().toString(), Record.this);

                main.gotoFragment(1, fragment);
            }
        }
    };

    public final OnClickListener confirmButtonListener = new OnClickListener() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            FromTime = CalendarEvent.StringToDate(FromText.getText().toString());
            ToTime = CalendarEvent.StringToDate(ToText.getText().toString());
            Remind = CalendarEvent.StringToDate(rem_inf.getText().toString());

            if (ToTime.getTime() <= FromTime.getTime()) {
                Toast.makeText(v.getContext(), "Error: Invalid time setting", Toast.LENGTH_LONG).show();
                return;
            }

            if (Remind != null)
                if (Remind.getTime() >= FromTime.getTime()) {
                    Toast.makeText(v.getContext(), "Error: Invalid time setting", Toast.LENGTH_LONG).show();
                    return;
                }

            CalendarEvent cal_e = new CalendarEvent();
            if (remSpin.getSelectedItem().toString().equals("Off")) {
                cal_e.setCalEvent(FromTime, ToTime, TitleText.getText().toString(), BodyText.getText().toString(), String.valueOf(freqSpin.getSelectedItem()), LocText.getText().toString(), null);
                am = (AlarmManager) v.getContext().getSystemService(Context.ALARM_SERVICE);
                if (pi != null) {
                    am.cancel(pi);
                    Toast.makeText(v.getContext(), "Reminder disabled.", Toast.LENGTH_LONG).show();
                }
            } else {
                cal_e.setCalEvent(FromTime, ToTime, TitleText.getText().toString(), BodyText.getText().toString(), String.valueOf(freqSpin.getSelectedItem()), LocText.getText().toString(), Remind);

                if (Remind != null) {
                    Intent intent = new Intent(v.getContext(), PlayReceiver.class);
                    intent.putExtra("msg", "hk.ust.comp4521.UasT.reminder_alarm");
                    intent.putExtra("remTitle", TitleText.getText().toString());
                    intent.putExtra("remTime", FromTime.toString());
                    intent.addCategory(FromTime.toString());

                    pi = PendingIntent.getBroadcast(v.getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    am = (AlarmManager) v.getContext().getSystemService(Context.ALARM_SERVICE);
                    am.setWindow(AlarmManager.RTC_WAKEUP, Remind.getTime(), Calendar.SECOND * 10, pi);
                    Toast.makeText(v.getContext(), "Reminder Created on: \n" + Remind.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(v.getContext(), "Reminder is not set properly.", Toast.LENGTH_LONG).show();
                }
            }

            calendarFragment.CalEvents.set(CalEventsSelectedIndex, cal_e);
            Database.getUser().setCalendar2(calendarFragment.CalEvents);
            Database.commitUser();
            calendarFragment.SaveToServer();

            ((MainActivity) getActivity()).popFragment();
        }
    };

    public final OnItemSelectedListener remSpinListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (remSpin.getSelectedItem().toString().equals("Off")) {
                rem_inf.setText("");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            rem_inf.setText("");
            remSpin.setSelection(0);
        }
    };

    public final OnClickListener deleteButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            calendarFragment.CalEvents.remove(CalEventsSelectedIndex);
            Database.getUser().setCalendar2(calendarFragment.CalEvents);
            Database.commitUser();
            calendarFragment.SaveToServer();

            ((MainActivity) getActivity()).popFragment();
        }
    };

    public void updateText(String target, Date date) {
        if (target.equals("FromText")) {
            calendarFragment.CalEvents.get(CalEventsSelectedIndex).setFrom(date);
        } else if (target.equals("ToText")) {
            calendarFragment.CalEvents.get(CalEventsSelectedIndex).setTo(date);
        } else if (target.equals("rem_inf")) {
            calendarFragment.CalEvents.get(CalEventsSelectedIndex).setRemind(date);
        }
    }

    public Date getFromTime() {
        return CalendarEvent.StringToDate(FromText.getText().toString());
    }
}