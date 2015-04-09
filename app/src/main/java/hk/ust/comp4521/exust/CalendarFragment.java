package hk.ust.comp4521.exust;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.ust.comp4521.exust.data.CalendarEvent;

public class CalendarFragment extends BaseFragment
{
    static final String TAG = "exust.CalendarFragment";
    CalendarView cal_m;
    GridView cal_d;
    View view;

	boolean isGroup;
    String[] eventContents;

    ArrayList<CalendarEvent> CalEvents;
    int[] CalEventsFilteredIndex;
    long selectedDate;

	OnItemClickListener personalListener;
	OnItemClickListener groupListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
        Log.i(TAG, "onCreateView()");

		view = inflater.inflate(R.layout.fragment_calendar, null);
        cal_m = (CalendarView) view.findViewById(R.id.calendar_month);
		cal_d = (GridView) view.findViewById(R.id.calendar_day);

        //cal_m.setDate(new Date().getTime());
        selectedDate = cal_m.getDate();

		if (isGroup)
            updateGroup();
		else
			updatePersonal();

        personalListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id)
        {
            MainActivity main = (MainActivity) getActivity();
            Record fragment = new Record();
            fragment.setParam(position, CalendarFragment.this);

            main.gotoFragment(1, fragment);
        }

    };

        groupListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id)
        {
            MainActivity main = (MainActivity) getActivity();
            Record fragment = new Record();
            fragment.setParam(position, CalendarFragment.this);

            main.gotoFragment(1, fragment);
        }
    };

		cal_d.setOnItemClickListener(!isGroup ? personalListener
				: groupListener);

        cal_m.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i2, int i3) {
                Log.i(TAG, "DayChange()");
                selectedDate = new Date(i - 1900,i2,i3).getTime();
                if (isGroup)
                    updateGroup();
                else
                    updatePersonal();
            }
        });

		return view;

	}


    public void setPersonal()
	{
		isGroup = false;
	}

	public void setGroup()
	{
		isGroup = true;
	}

	@Override
	public String getTitle()
	{
		return "Calendar";
	}

	@Override
	public boolean onBackPressed()
	{
		return super.onBackPressed();
	}

	public void updatePersonal()
	{
        CalEventsFilteredIndex = filterEvent(selectedDate);

		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (int index : CalEventsFilteredIndex) {
            Map<String, Object> item = new HashMap<String, Object>();

            item.put("eventName", CalEvents.get(index).getTitle());
            item.put("StartTime", CalEvents.get(index).getFrom4UI());
            items.add(item);
        }

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
				R.layout.grid_item, new String[] {"StartTime", "eventName" },
				new int[] { R.id.text });

		cal_d.setAdapter(adapter);
	}

	public void updateGroup()
	{
        CalEventsFilteredIndex = filterEvent(selectedDate);

        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (int index : CalEventsFilteredIndex) {
            Map<String, Object> item = new HashMap<String, Object>();

            item.put("eventName", CalEvents.get(index).getTitle());
            item.put("StartTime", CalEvents.get(index).getFrom4UI());
            items.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
                R.layout.grid_item, new String[] {"StartTime", "eventName" },
                new int[] { R.id.text });

        cal_d.setAdapter(adapter);
	}

    public void initialize() {
        Log.i(TAG, "initialize()");
        CalEvents = new ArrayList<CalendarEvent>();
        CalEvents.add(new CalendarEvent());
    }

    public int[] filterEvent (long time) {
        // Wasted some RAM spaces for unwanted index
        if (CalEvents == null) {return null;}
        else {
            int count = 0;
            int[] answer = new int[CalEvents.size()];

            for (int i = 0; i < CalEvents.size(); i++) {

               //if (CalEvents.get(i).matchFrom(time)) {
                    //answer[count] = i;
                    //count++;
               //}

                answer[count] = i;
                count++;
            }
            Log.i(TAG, Arrays.toString(answer));
            return answer;
        }
    }

    public void setEvents2(ArrayList<CalendarEvent> in) {
        if (in.isEmpty()) {in.add(new CalendarEvent());}
        CalEvents = in;
    }
}


