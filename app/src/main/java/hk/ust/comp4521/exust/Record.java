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
    final static long ONE_HOUR = 1000*60*60;
    private View view;
    private Spinner freqSpin;
	private EditText TitleText;
	private TextView BodyText;
    private EditText FromText;
    private EditText ToText;
	private Button confirmButton;
	private Button deleteButton;
    private Date FromTime;
    private Date ToTime;
	int CalEventsSelectedIndex;
	CalendarFragment calendarFragment;

	public void setParam(int position, CalendarFragment calendarFragment)
	{
		this.calendarFragment = calendarFragment;
        this.CalEventsSelectedIndex = calendarFragment.CalEventsFilteredIndex[position];
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

		confirmButton = (Button) view.findViewById(R.id.submitbutton);
		deleteButton = (Button) view.findViewById(R.id.deletebutton);

		confirmButton.setOnClickListener(confirmButtonListener);
		deleteButton.setOnClickListener(deleteButtonListener);

        freqSpin.setSelection(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getFreqIndex());

        TitleText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getTitle());
        BodyText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getBody());

        /**
                FromTime = new Date(calendarFragment.cal_m.getDate());
                    FromText.setText(FromTime.toString());
                 ToTime = new Date(calendarFragment.cal_m.getDate() + ONE_HOUR);
                ToText.setText(ToTime.toString());
                **/
        FromText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getFrom().toString());
        ToText.setText(calendarFragment.CalEvents.get(CalEventsSelectedIndex).getTo().toString());

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
            cal_e.setCalEvent(FromTime,ToTime, TitleText.getText().toString(), BodyText.getText().toString(),String.valueOf(freqSpin.getSelectedItem()));

            if (!cal_e.toString().equals("")) {
                calendarFragment.CalEvents.add(cal_e);

                Database.getUser().setCalendar(new String[7 * 24]);
                Database.getUser().setCalendar2(calendarFragment.CalEvents);
                Database.commitUser();
            }

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