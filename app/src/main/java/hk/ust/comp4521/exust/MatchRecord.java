package hk.ust.comp4521.exust;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.ust.comp4521.exust.data.CalendarEvent;

public class MatchRecord extends BaseFragment
{
    private View view;
    private Spinner freqSpin;
    private TextView FromText;
    private TextView DurText;
    private TextView RangeText;
	private Button showBtn;
	private Button exitBtn;
    private ListView slotList;

    private Date FromTime;
    private long Duration;
    private long Range;
    private Date[] CalStart, CalEnd;

    static final String TAG = "exust.MatchRecord";

	public void setParam(ArrayList<String> JointCalStart, ArrayList<String> JointCalEnd)
    {
        CalStart = new Date[JointCalStart.size()];
        CalEnd = new Date[JointCalEnd.size()];
        for (int i=0; i < CalStart.length; i++) {
            CalStart[i] = CalendarEvent.StringToDate(JointCalStart.get(i));
            CalEnd[i] = CalendarEvent.StringToDate(JointCalEnd.get(i));
        }
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
        Log.i(TAG, "onCreateView");
		view = inflater.inflate(R.layout.match_record, null);

        freqSpin = (Spinner) view.findViewById(R.id.freqSpin);
        FromText = (TextView) view.findViewById(R.id.fromChoice);
        DurText = (TextView) view.findViewById(R.id.durChoice);
        RangeText = (TextView) view.findViewById(R.id.rangeChoice);

        slotList = (ListView)  view.findViewById(R.id.slotList);

        showBtn = (Button) view.findViewById(R.id.showBtn);
        exitBtn = (Button) view.findViewById(R.id.exitBtn);

        freqSpin.setSelection(0);
        if (FromTime != null) {FromText.setText(FromTime.toString());}
        if (Duration != 0) {DurText.setText(choose_before.LongToUIString(Duration));}
        if (Range != 0) {RangeText.setText(choose_before.LongToUIString(Range));}

        FromText.setOnClickListener(FromListener);
        DurText.setOnClickListener(DurListener);
        RangeText.setOnClickListener(RangeListener);
        showBtn.setOnClickListener(showBtnListener);
        exitBtn.setOnClickListener(exitBtnListener);

        freqSpin.setVisibility(View.GONE);

		return view;
	}

	@Override
	public String getTitle()
	{
		return "Calendar";
	}

    public OnClickListener FromListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity main = (MainActivity) getActivity();
            choose_time fragment = new choose_time();
            fragment.setParam(FromText.getText().toString(), MatchRecord.this, "FromText");

            main.gotoFragment(2, fragment);
        }
    };

    public OnClickListener DurListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity main = (MainActivity) getActivity();
            choose_before fragment = new choose_before();
            fragment.setParam(DurText.getText().toString(), MatchRecord.this, "DurText");

            main.gotoFragment(2, fragment);
        }
    };

    public OnClickListener RangeListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity main = (MainActivity) getActivity();
            choose_before fragment = new choose_before();
            fragment.setParam(RangeText.getText().toString(), MatchRecord.this, "RangeText");

            main.gotoFragment(2, fragment);
        }
    };

	public OnClickListener showBtnListener = new OnClickListener()
	{
        @Override
		public void onClick(View v)
		{
			// Validation
            if ((FromTime == null) || (Duration == 0) || (Range == 0) ||
                    (CalStart.length == 0) || (CalEnd.length == 0) ||
                    (Duration > Range))
                return;

			// Start making available time slots
            Toast.makeText(view.getContext(), Integer.toString(CalStart.length), Toast.LENGTH_LONG).show();

            ArrayList<Date[]> timeSlots = makeSlots();
            Date[] slotStart = new Date[timeSlots.size()];
            Date[] slotEnd = new Date[timeSlots.size()];
            for (int i=0; i<timeSlots.size(); i++) {
                slotStart[i] = timeSlots.get(i)[0];
                slotEnd[i] = timeSlots.get(i)[1];
            }

            // Output time slots
            List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
            items.clear();

            for (int i=0; i < slotStart.length; i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.clear();
                item.put("slot", "S " + slotStart[i].toString());
                items.add(item);
                item = new HashMap<String, Object>();
                item.clear();
                item.put("slot", "E " + slotEnd[i].toString());
                items.add(item);
            }

            SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
                    R.layout.grid_item, new String[] {"slot"},
                    new int[] { R.id.text });

            slotList.setAdapter(adapter);
		}
	};

	public OnClickListener exitBtnListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			((MainActivity) getActivity()).popFragment();
		}
	};

    public void updateText (String target, Date date) {
        Log.i(TAG, "updateText");
        if (target.equals("FromText")) {
           FromTime = date;
        }
    }

    public void updateText (String target, long amount) {
        Log.i(TAG, "updateText");
        if (target.equals("DurText")) {
            Duration = amount;
        } else if  (target.equals("RangeText")) {
            Range = amount;
        }
    }

    private ArrayList<Date[]> makeSlots() {
        //Initialise answer
        ArrayList<Date[]> answer = new  ArrayList<Date[]>();

        //Initialise local variables form UI
        long LongFrom = FromTime.getTime();
        long Dur = Duration;
        long Ran = Range;
        long[] LongStart = new long [CalStart.length];
        long[] LongEnd = new long [CalEnd.length];
        for (int i=0; i<CalStart.length; i++) {
            LongStart[i] = CalStart[i].getTime();
            LongEnd[i] = CalEnd[i].getTime();
        }
        int IndexStart = 0;
        int IndexEnd = 0;

        //Sort the arrays - may take time
        Arrays.sort(LongStart);
        Arrays.sort(LongEnd);

        //Special case: Before LongStart[0]
        if (LongStart[0] - LongFrom >= Dur)
            if (LongStart[0] - LongFrom > Ran)
                //Special case: Whole defined time is not free - end procedure
                return answer;
            else
                answer.add(new Date[] {new Date(LongFrom), new Date(LongStart[0])});
        else while(LongEnd[IndexEnd] < LongFrom) {
            IndexEnd++;
            if (IndexEnd >= LongEnd.length) {
                //Special case: Whole defined time is free - end procedure
                answer.add(new Date[] {new Date(LongFrom), new Date(LongFrom + Ran)});
                return answer;
            }
        }

        //Loop until LongEnd[LongEnd.length - 1]
        while (LongEnd[IndexEnd] > LongStart[IndexStart]) {
            //Assume valid data: LongEnd[X] > LongStart[X]
            IndexStart++;
            if (IndexStart - IndexEnd > 1) {
                //Skip checking if multiple start points, move to next round
                IndexEnd = IndexStart;
            } else if ((IndexStart - IndexEnd == 1) && (LongStart[IndexStart] - LongEnd[IndexEnd] >= Dur)) {
                if (LongStart[IndexStart] > (LongFrom + Ran))
                    //Special case: boundary time
                    answer.add(new Date[] {new Date(LongEnd[IndexEnd]), new Date(LongFrom + Ran)});
                else
                    //Usual desired time
                    answer.add(new Date[] {new Date(LongEnd[IndexEnd]), new Date(LongStart[IndexStart])});
                //Move to next round
                IndexEnd = IndexStart;
            }
            //Exit condition
            if ((IndexStart >= LongStart.length) || (LongStart[IndexStart] > (LongFrom + Ran)))
                break;
        }

        //Special case: After LongEnd[LongEnd.length - 1]
        if ((LongFrom + Ran) - LongEnd[LongEnd.length -1] >= Dur)
            answer.add(new Date[] {new Date(LongEnd[LongEnd.length -1]), new Date(LongFrom + Ran)});

        //Return answer
        return answer;
    }

}