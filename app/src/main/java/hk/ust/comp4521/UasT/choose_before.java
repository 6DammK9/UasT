/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Date;

import hk.ust.comp4521.UasT.data.CalendarEvent;

/**
 * Created by Darren on 18/4/2015.
 * Main purpose: To choose time amount through GUI.
 */
public class choose_before extends BaseFragment {
    Record _parent;
    MatchRecord _parent2;
    Date _from;
    String _target;
    int[] choices; //Day, Hour, Min, Second

    private View view;
    private NumberPicker numDay;
    private NumberPicker numHour;
    private NumberPicker numMinute;
    private NumberPicker numSecond;
    private Button BtnAccept;
    private Button BtnDecline;

    final private static long SECOND = 1000;
    final private static long MINUTE = 1000*60;
    final private static long HOUR = 1000*60*60;
    final private static long DAY = 1000*60*60*24;

    public void setParam(String from, String rem, Record parent) {
        this._parent = parent;
        this._parent2 = null;
        this._target = null;
        _from = CalendarEvent.StringToDate(from);
        Date temp = CalendarEvent.StringToDate(rem);
        long dif = 0;
        if (temp != null)
         dif = _from.getTime()-temp.getTime();
        if (dif < 0)
            choices = new int[]{0,0,0,0};
        else
            choices = new int[]{
                    (int) (dif/DAY),
                    (int) ((dif%DAY)/HOUR),
                    (int) ((dif%HOUR)/MINUTE),
                    (int) ((dif%MINUTE)/SECOND)
            };
    }

    public void setParam(String input, MatchRecord parent2, String target) {
        this._parent = null;
        this._parent2 = parent2;
        this._target = target;
        long dif = 0;
        if (input != null)
            dif = UIStringToLong(input);
        if (dif < 0)
            choices = new int[]{0,0,0,0};
        else
            choices = new int[]{
                    (int) (dif/DAY),
                    (int) ((dif%DAY)/HOUR),
                    (int) ((dif%HOUR)/MINUTE),
                    (int) ((dif%MINUTE)/SECOND)
            };
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.choose_before, container, false);

        numDay = (NumberPicker) view.findViewById(R.id.numDay);
        numHour = (NumberPicker) view.findViewById(R.id.numHour);
        numMinute = (NumberPicker) view.findViewById(R.id.numMinute);
        numSecond = (NumberPicker) view.findViewById(R.id.numSecond);

        BtnAccept = (Button) view.findViewById(R.id.BtnAccept);
        BtnDecline = (Button) view.findViewById(R.id.BtnDecline);

        numDay.setMinValue(0);
        numDay.setMaxValue(366);
        numDay.setValue(choices[0]);
        numHour.setMinValue(0);
        numHour.setMaxValue(23);
        numHour.setValue(choices[1]);
        numMinute.setMinValue(0);
        numMinute.setMaxValue(59);
        numMinute.setValue(choices[2]);
        numSecond.setMinValue(0);
        numSecond.setMaxValue(59);
        numSecond.setValue(choices[3]);

        BtnAccept.setOnClickListener(AcceptListener);
        BtnDecline.setOnClickListener(DeclineListener);

        return view;
    }

    @Override
    public String getTitle() {
        return "Calendar";
    }

    public final View.OnClickListener AcceptListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            long amount = numDay.getValue()*DAY + numHour.getValue()*HOUR + numMinute.getValue()*MINUTE + numSecond.getValue()*SECOND;
            if (_parent != null)
                if (_from.getTime() - amount >= 0)
                    _parent.updateText("rem_inf", new Date(_from.getTime() - amount));
            if ((_parent2 != null) && (_target != null))
                if (amount >= 0)
                    _parent2.updateText(_target, amount);

            ((MainActivity) getActivity()).popFragment();
        }
    };

    private final View.OnClickListener DeclineListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ((MainActivity) getActivity()).popFragment();
        }
    };

    //123D 23H 23M 23S
    public static long UIStringToLong(String input) {
        if (input == null) {return 0;}
        if (input.equals("")) {return 0;}
        String[] timeStr = input.split(" ");
        long[] timeLong = new long[4];
        for (int i=0; i<4; i++) {
            timeStr[i] = timeStr[i].substring(0,timeStr[i].length() - 1);
            timeLong[i] = Long.parseLong(timeStr[i]);
        }
        return timeLong[0] * DAY + timeLong[1] * HOUR + timeLong[2] * MINUTE + timeLong[3] * SECOND;
    }

    public static String LongToUIString(long input) {
        return (input/DAY) + "D " +  ((input%DAY)/HOUR) + "H " +  ((input%HOUR)/MINUTE) + "M " + ((input%MINUTE)/SECOND) + "S";
    }

}
