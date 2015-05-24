/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT;

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

import hk.ust.comp4521.UasT.data.CalendarEvent;

public class MatchRecord extends BaseFragment {
    private View view;
    private Spinner freqSpin;
    private TextView freqLabel;
    private TextView FromText;
    private TextView DurText;
    private TextView RangeText;
    private Button showBtn;
    private Button exitBtn;
    private ListView slotList;

    private Date FromTime;
    private long Duration;
    private long Range;
    private ArrayList<Date> CalStart, CalEnd;
    private ArrayList<String> CalFreq;

    static final String TAG = "UasT.MatchRecord";

    public void setParam(ArrayList<String> JointCalStart, ArrayList<String> JointCalEnd, ArrayList<String> JointCalFreq) {
        CalStart = new ArrayList<Date>();
        CalEnd = new ArrayList<Date>();
        CalFreq = new ArrayList<String>();


        for (int i = 0; i < JointCalStart.size(); i++) {
            CalStart.add(CalendarEvent.StringToDate(JointCalStart.get(i)));
            CalEnd.add(CalendarEvent.StringToDate(JointCalEnd.get(i)));
            CalFreq.add(JointCalFreq.get(i));
        }
    }

    public void setParam(ArrayList<CalendarEvent> in) {
        CalStart = new ArrayList<Date>();
        CalEnd = new ArrayList<Date>();
        CalFreq = new ArrayList<String>();

        for (int i = 0; i < in.size(); i++) {
            CalStart.add(in.get(i).getFrom());
            CalEnd.add(in.get(i).getTo());
            CalFreq.add(in.get(i).getFreq());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.match_record, container, false);

        freqSpin = (Spinner) view.findViewById(R.id.freqSpin);
        FromText = (TextView) view.findViewById(R.id.fromChoice);
        DurText = (TextView) view.findViewById(R.id.durChoice);
        RangeText = (TextView) view.findViewById(R.id.rangeChoice);

        slotList = (ListView) view.findViewById(R.id.slotList);

        showBtn = (Button) view.findViewById(R.id.showBtn);
        exitBtn = (Button) view.findViewById(R.id.exitBtn);

        freqSpin.setSelection(0);
        if (FromTime != null) {
            FromText.setText(FromTime.toString());
        }
        if (Duration != 0) {
            DurText.setText(choose_before.LongToUIString(Duration));
        }
        if (Range != 0) {
            RangeText.setText(choose_before.LongToUIString(Range));
        }

        FromText.setOnClickListener(FromListener);
        DurText.setOnClickListener(DurListener);
        RangeText.setOnClickListener(RangeListener);
        showBtn.setOnClickListener(showBtnListener);
        exitBtn.setOnClickListener(exitBtnListener);

        //Function limited to creating One-off event
        freqLabel = (TextView) view.findViewById(R.id.freqLabel);
        freqLabel.setVisibility(View.GONE);
        freqSpin.setVisibility(View.GONE);

        return view;
    }

    @Override
    public String getTitle() {
        return "Calendar";
    }

    public final OnClickListener FromListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity main = (MainActivity) getActivity();
            choose_time fragment = new choose_time();
            fragment.setParam(FromText.getText().toString(), MatchRecord.this, "FromText");

            main.gotoFragment(2, fragment);
        }
    };

    public final OnClickListener DurListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity main = (MainActivity) getActivity();
            choose_before fragment = new choose_before();
            fragment.setParam(DurText.getText().toString(), MatchRecord.this, "DurText");

            main.gotoFragment(2, fragment);
        }
    };

    public final OnClickListener RangeListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity main = (MainActivity) getActivity();
            choose_before fragment = new choose_before();
            fragment.setParam(RangeText.getText().toString(), MatchRecord.this, "RangeText");

            main.gotoFragment(2, fragment);
        }
    };

    public final OnClickListener showBtnListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //Multithreading issue - reset groupCalEvents
            if (ChatFragment.groupCalEvents != null) {
                setParam(ChatFragment.groupCalEvents);
                Log.i(TAG, Integer.toString(ChatFragment.groupCalEvents.size()));
            }

            //Validation
            if ((FromTime == null) || (Duration == 0) || (Range == 0) || (Duration > Range))
                return;

            //Start making available time slots
            //Toast.makeText(view.getContext(), Integer.toString(CalStart.size()), Toast.LENGTH_LONG).show();

            //SEE DEFINITION - ALGORITHM HELL - MORE THAN 100 LINES
            ArrayList<Date[]> timeSlots = makeSlots();

            // Output time slots
            Date[] slotStart = new Date[timeSlots.size()];
            Date[] slotEnd = new Date[timeSlots.size()];
            for (int i = 0; i < timeSlots.size(); i++) {
                slotStart[i] = timeSlots.get(i)[0];
                slotEnd[i] = timeSlots.get(i)[1];
            }

            List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
            items.clear();

            for (int i = 0; i < slotStart.length; i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.clear();
                item.put("slot", i + "S " + slotStart[i].toString());
                items.add(item);
                item = new HashMap<String, Object>();
                item.clear();
                item.put("slot", i + "E " + slotEnd[i].toString());
                items.add(item);
            }

            SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
                    R.layout.grid_item, new String[]{"slot"},
                    new int[]{R.id.text});

            slotList.setAdapter(adapter);
        }
    };

    public final OnClickListener exitBtnListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MainActivity) getActivity()).popFragment();
        }
    };

    public void updateText(String target, Date date) {
        Log.i(TAG, "updateText");
        if (target.equals("FromText")) {
            FromTime = date;
        }
    }

    public void updateText(String target, long amount) {
        Log.i(TAG, "updateText");
        if (target.equals("DurText")) {
            Duration = amount;
        } else if (target.equals("RangeText")) {
            Range = amount;
        }
    }

    private ArrayList<Date[]> makeSlots() {
        //Initialise answer
        ArrayList<Date[]> answer = new ArrayList<Date[]>();

        //Initialise local variables form UI
        ArrayList<Long> FreqStart = new ArrayList<Long>();
        ArrayList<Long> FreqEnd = new ArrayList<Long>();
        ArrayList<Long> BufStart = new ArrayList<Long>(); //Buffer for FreqStart
        ArrayList<Long> BufEnd = new ArrayList<Long>(); //Buffer for FreqSEnd
        int IndexStart;
        int IndexEnd = 0;
        long[] LongStart, LongEnd;
        //Evil variables, for reducing memory usage only
        Date tS, tE;
        int[] tSi, tEi;

        //Validation: CalFreq.size() == CalStart.size() == CalEnd.size() > 0
        if ((CalStart.size() != CalEnd.size()) || (CalStart.size() != CalFreq.size()) || (CalEnd.size() != CalFreq.size())) {
            Toast.makeText(view.getContext(), "Error: Invalid data from server", Toast.LENGTH_LONG).show();
            return answer;
        }

        //Special Case: Empty parameter - Totally free - end procedure
        if (CalStart.size() == 0) {
            answer.add(new Date[]{new Date(FromTime.getTime()), new Date(FromTime.getTime() + Range)});
            return answer;
        }

        //If they are frequent events, break it down into single events for the Range
        for (int i = 0; i < CalFreq.size(); i++) {

            //Initialise FreqStart and FreqStart
            FreqStart.clear();
            FreqEnd.clear();
            FreqStart.add(CalStart.get(i).getTime());
            FreqEnd.add(CalEnd.get(i).getTime());

            //"Once" will be ignored directly
            if (!CalFreq.get(i).equals("Once"))
                while (FreqEnd.get(FreqEnd.size() - 1) <= (FromTime.getTime() + Range)) {
                    //Log.i(TAG, Long.toString(FreqEnd.get(FreqEnd.size() - 1)) +" "+ Long.toString((FromTime.getTime() + Range)));
                    switch (CalendarEvent.getFreqIndex(CalFreq.get(i))) {
                        case 1: //"Daily"
                            FreqStart.add(FreqStart.get(FreqStart.size() - 1) + CalendarEvent.ONE_DAY);
                            FreqEnd.add(FreqEnd.get(FreqEnd.size() - 1) + CalendarEvent.ONE_DAY);
                            break;
                        case 2: //"Weekly"
                            FreqStart.add(FreqStart.get(FreqStart.size() - 1) + CalendarEvent.ONE_WEEK);
                            FreqEnd.add(FreqEnd.get(FreqEnd.size() - 1) + CalendarEvent.ONE_WEEK);
                            break;
                        case 3: //"Bi-weekly"
                            FreqStart.add(FreqStart.get(FreqStart.size() - 1) + CalendarEvent.TWO_WEEK);
                            FreqEnd.add(FreqEnd.get(FreqEnd.size() - 1) + CalendarEvent.TWO_WEEK);
                            break;
                        case 4: //"Monthly" - evil part - make new Date and use them
                            tS = new Date(FreqStart.get(FreqStart.size() - 1));
                            tE = new Date(FreqEnd.get(FreqEnd.size() - 1));
                            if (tS.getMonth() + 1 > 11) {
                                tSi = new int[]{tS.getYear() + 1, tS.getMonth() - 11};
                            } else {
                                tSi = new int[]{tS.getYear(), tS.getMonth() - +1};
                            }
                            if (tE.getMonth() + 1 > 11) {
                                tEi = new int[]{tE.getYear() + 1, tE.getMonth() - 11};
                            } else {
                                tEi = new int[]{tE.getYear(), tE.getMonth() - +1};
                            }
                            FreqStart.add(new Date(tSi[0], tSi[1], tS.getDate(), tS.getHours(), tS.getMinutes(), tS.getSeconds()).getTime());
                            FreqEnd.add(new Date(tEi[0], tEi[1], tE.getDate(), tE.getHours(), tE.getMinutes(), tE.getSeconds()).getTime());
                            break;
                        default:
                            break;
                    }
                }
            BufStart.addAll(FreqStart);
            BufEnd.addAll(FreqEnd);
        }

        //After breaking, make them into arrays
        LongStart = new long[BufStart.size()];
        LongEnd = new long[BufEnd.size()];
        for (int i = 0; i < BufStart.size(); i++) {
            LongStart[i] = BufStart.get(i);
            LongEnd[i] = BufEnd.get(i);
        }

        //Sort the arrays - may take time
        Arrays.sort(LongStart);
        Arrays.sort(LongEnd);

        //Special case: Before LongStart[0]
        if (LongStart[0] - FromTime.getTime() >= Duration) {
            if (LongStart[0] - FromTime.getTime() >= Range) {
                //Special case: Whole defined time is free - end procedure
                answer.add(new Date[]{new Date(FromTime.getTime()), new Date(FromTime.getTime() + Range)});
                return answer;
            } else {
                answer.add(new Date[]{new Date(FromTime.getTime()), new Date(LongStart[0])});
            }
        }
        while (LongEnd[IndexEnd] <= FromTime.getTime()) {
            IndexEnd++;
            if (IndexEnd >= LongEnd.length) {
                //Special case: Whole defined time is free - end procedure
                answer.add(new Date[]{new Date(FromTime.getTime()), new Date(FromTime.getTime() + Range)});
                return answer;
            }
        }
        IndexStart = IndexEnd;

        //Loop until LongEnd[LongEnd.length - 1]
        //while (LongEnd[IndexEnd] > LongStart[IndexStart]) {
        while ((IndexStart < LongStart.length) && (IndexEnd < LongEnd.length)) {
            //Assume valid data: LongEnd[X] > LongStart[X]
            IndexStart += 1;

            //Exit condition
            if ((IndexStart >= LongStart.length) || (LongStart[IndexStart] > (FromTime.getTime() + Range)))
                break;

            //If not exit, do the checking
            if ((IndexStart - IndexEnd == 1) && (LongStart[IndexStart] - LongEnd[IndexEnd] >= Duration)) {
                if (LongStart[IndexStart] >= (FromTime.getTime() + Range))
                    //Special case: boundary time
                    answer.add(new Date[]{new Date(LongEnd[IndexEnd]), new Date(FromTime.getTime() + Range)});
                else
                    //Usual desired time
                    answer.add(new Date[]{new Date(LongEnd[IndexEnd]), new Date(LongStart[IndexStart])});
            }
            //Move to next round
            IndexEnd = IndexStart;
        }

        //Special case: After LongEnd[LongEnd.length - 1]
        if ((FromTime.getTime() + Range) - LongEnd[LongEnd.length - 1] >= Duration)
            answer.add(new Date[]{new Date(LongEnd[LongEnd.length - 1]), new Date(FromTime.getTime() + Range)});

        //Return answer
        return answer;
    }

}