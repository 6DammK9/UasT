package hk.ust.comp4521.UasT;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.CalEventsEntry;
import hk.ust.comp4521.UasT.data.CalendarEvent;
import hk.ust.comp4521.UasT.data.Database;
import hk.ust.comp4521.UasT.data.DatabaseLoad;
import hk.ust.comp4521.UasT.json.ApiResponseBase;

public class CalendarFragment extends BaseFragment
{
    //static final String TAG = "UasT.CalendarFragment";
    CalendarView cal_m;
    GridView cal_d;
    View view;
    Button new_event;

	boolean isGroup;

    ArrayList<CalendarEvent> CalEvents;
    ArrayList<Integer> CalEventsFilteredIndex;
    long selectedDate;

	OnItemClickListener personalListener;
	OnItemClickListener groupListener;
    View.OnClickListener new_eventListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
        //Log.i(TAG, "onCreateView()");

		view = inflater.inflate(R.layout.fragment_calendar, container, false);
        cal_m = (CalendarView) view.findViewById(R.id.calendar_month);
		cal_d = (GridView) view.findViewById(R.id.calendar_day);
        new_event = (Button) view.findViewById(R.id.btn_new_event);

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
            fragment.setParam(false, position, CalendarFragment.this);

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
            fragment.setParam(false, position, CalendarFragment.this);

            main.gotoFragment(1, fragment);
            }
        };

        new_eventListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) getActivity();
                Record fragment = new Record();
                fragment.setParam(true, -1, CalendarFragment.this);

                main.gotoFragment(1, fragment);
            }
        };

		cal_d.setOnItemClickListener(!isGroup ? personalListener : groupListener);

        new_event.setOnClickListener(new_eventListener);

        cal_m.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i2, int i3) {
                //Log.i(TAG, "DayChange()");
                selectedDate = new Date(i - 1900,i2,i3).getTime();
                cal_m.setDate(selectedDate);
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
        items.clear();

        for (int index : CalEventsFilteredIndex) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.clear();
            item.put("eventName", CalEvents.get(index).getTitle());
            items.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
                R.layout.grid_item, new String[] {"eventName" },
                new int[] { R.id.text });

        cal_d.setAdapter(adapter);
	}

	public void updateGroup()
	{
        CalEventsFilteredIndex = filterEvent(selectedDate);

        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        items.clear();

        for (int index : CalEventsFilteredIndex) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.clear();
            item.put("eventName", CalEvents.get(index).getTitle());
            items.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
                R.layout.grid_item, new String[] {"eventName" },
                new int[] { R.id.text });

        cal_d.setAdapter(adapter);
	}

    public void initialize() {
        //Log.i(TAG, "initialize()");
        CalEvents = new ArrayList<CalendarEvent>();
        CalEvents.clear();
        //CalEvents.add(new CalendarEvent());
    }

    public ArrayList<Integer> filterEvent (long time) {
        // Wasted some RAM spaces for unwanted index
        //Log.i(TAG, "filterEvent");
        if (CalEvents == null) {return null;}
        else {
            ArrayList<Integer> answer = new  ArrayList<Integer>();
            answer.clear();

            for (int i = 0; i < CalEvents.size(); i++) {
               if (CalEvents.get(i).matchFrom(time)) {
                    answer.add(i);
               }
            }
            //Log.i(TAG, Arrays.toString(answer));
            //Log.i(TAG, new Date(selectedDate).toString());
            return answer;
        }
    }

    public void setEvents2(ArrayList<CalendarEvent> in) {
        //if (in.isEmpty()) {in.add(new CalendarEvent());}
        CalEvents = in;
    }

    public void SaveToServer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ProgressBar bar = new ProgressBar(view.getContext(), null,
                android.R.attr.progressBarStyleHorizontal);
        bar.setIndeterminate(true);
        builder.setCancelable(true).setTitle("Saving to server").setView(bar);
        final AlertDialog dialog = builder.show();

        ArrayList<CalendarEvent> calendar2 = Database.getUser().getCalendar2();

        //Make arrays
        String CalEventArr[] = new String[calendar2.size()];
        for (int i = 0; i < calendar2.size(); i++) {
            CalEventArr[i] = calendar2.get(i).toString();
        }

        ApiManager.upCalEvents(CalEventArr, new ApiHandler<ApiResponseBase>() {

            @Override
            public void onSuccess(ApiResponseBase response) {
                dialog.dismiss();
                Toast.makeText(view.getContext(), response.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                dialog.dismiss();
                Toast.makeText(view.getContext(), message,
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    //Not sure when to use this
    public void LoadFromServer() {
        Database.getData("CalEventsEntries", Database.getUser().getITSC(), CalEventsEntry.class,
                new DatabaseLoad<Map<String, CalEventsEntry>>() {
                    @Override
                    public void load(Map<String, CalEventsEntry> obj) {
                        if (obj == null)
                            return;
                        ArrayList<CalEventsEntry> threads = new ArrayList<CalEventsEntry>();
                        threads.addAll(obj.values());
                        //Toast.makeText(ChatCardView.this.getContext(), Integer.toString(threads.size()),
                        //Toast.LENGTH_LONG).show();

                        CalEvents.clear();
                        for (int i = 0; i < threads.size(); i++) {
                            for (int j = 0; j < threads.get(i).getCalEventArr().length; j++) {
                                CalEvents.add( new CalendarEvent(threads.get(i). getCalEventArr()[j]) );
                            }
                        }

                    }
                });
    }
}


