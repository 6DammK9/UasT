package hk.ust.comp4521.exust;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

import hk.ust.comp4521.exust.data.CalendarEvent;
import hk.ust.comp4521.exust.data.Database;

public class Record extends BaseFragment
{
    private View view;
    private Spinner freqSpin;
	private EditText TitleText;
	private TextView BodyText;
    private EditText FromText;
    private EditText ToText;
    private EditText LocText;
	private Button confirmButton;
	private Button deleteButton;
    private Date FromTime;
    private Date ToTime;
	int CalEventsSelectedIndex;
	CalendarFragment calendarFragment;
    boolean createMode;

	public void setParam(Boolean create, int position, CalendarFragment calendarFragment)
    {
        this.createMode = create;
        this.calendarFragment = calendarFragment;
        if (create) {this.CalEventsSelectedIndex = position;}
            else {this.CalEventsSelectedIndex = calendarFragment.CalEventsFilteredIndex.get(position);}
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.record, null);

        freqSpin = (Spinner) view.findViewById(R.id.freqSpin);

		TitleText = (EditText) view.findViewById(R.id.titleText);
        BodyText = (TextView) view.findViewById(R.id.bodyText);
        FromText = (EditText) view.findViewById(R.id.time_from);
        ToText = (EditText) view.findViewById(R.id.time_to);
        LocText = (EditText) view.findViewById(R.id.locationText);

		confirmButton = (Button) view.findViewById(R.id.submitbutton);
		deleteButton = (Button) view.findViewById(R.id.deletebutton);

		confirmButton.setOnClickListener(confirmButtonListener);
		deleteButton.setOnClickListener(deleteButtonListener);

        if (createMode) {
            freqSpin.setSelection(0);
            TitleText.setText("NEW EVENT");
            BodyText.setText("Detail:");
            FromText.setText(new Date(calendarFragment.cal_m.getDate()).toString());
            ToText.setText(new Date(calendarFragment.cal_m.getDate() + CalendarEvent.ONE_HOUR).toString());
            confirmButton.setText("Create");
            LocText.setText("");
        }
        else {
            freqSpin.setSelection(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getFreqIndex());
            TitleText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getTitle());
            BodyText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getBody());
            FromText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getFrom().toString());
            ToText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getTo().toString());
            confirmButton.setText("Modify");
            LocText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getLoc());
        }

		return view;
	}

	@Override
	public String getTitle()
	{
		return "Calendar";
	}

	public OnClickListener confirmButtonListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			FromTime = CalendarEvent.StringToDate(FromText.getText().toString());
            ToTime = CalendarEvent.StringToDate(FromText.getText().toString());

            CalendarEvent cal_e = new CalendarEvent();
            cal_e.setCalEvent(FromTime,ToTime, TitleText.getText().toString(), BodyText.getText().toString(), String.valueOf(freqSpin.getSelectedItem()), LocText.getText().toString());

            if (createMode) {calendarFragment.CalEvents.add(cal_e);}
                else {calendarFragment.CalEvents.set(CalEventsSelectedIndex, cal_e);}

            Database.getUser().setCalendar(new String[7 * 24]);
            Database.getUser().setCalendar2(calendarFragment.CalEvents);
            Database.commitUser();

            ((MainActivity) getActivity()).popFragment();
		}
	};

	private OnClickListener deleteButtonListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
            calendarFragment.CalEvents.remove(CalEventsSelectedIndex);
            Database.getUser().setCalendar(new String[7 * 24]);
            Database.getUser().setCalendar2(calendarFragment.CalEvents);
            Database.commitUser();

			((MainActivity) getActivity()).popFragment();
		}
	};
}