package hk.ust.comp4521.exust;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Date;

import hk.ust.comp4521.exust.data.CalendarEvent;

/**
 * Created by YuXuZaWa on 18/4/2015.
 */
public class choose_before extends BaseFragment {
    Record _parent;
    Date _from;
    int[] choices; //Day, Hour, Min, Second

    private View view;
    private NumberPicker numDay;
    private NumberPicker numHour;
    private NumberPicker numMinute;
    private NumberPicker numSecond;
    private Button BtnAccept;
    private Button BtnDecline;

    final private long SECOND = 1000;
    final private long MINUTE = 1000*60;
    final private long HOUR = 1000*60*60;
    final private long DAY = 1000*60*60*24;

    public void setParam(String from, String rem, Record parent) {
        this._parent = parent;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.choose_before, null);

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

    public View.OnClickListener AcceptListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            long amount = numDay.getValue()*DAY + numHour.getValue()*HOUR + numMinute.getValue()*MINUTE + numSecond.getValue()*SECOND;
            if (_from.getTime() - amount >= 0)
                _parent.updateText("rem_inf", new Date(_from.getTime() - amount));

            ((MainActivity) getActivity()).popFragment();
        }
    };

    private View.OnClickListener DeclineListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ((MainActivity) getActivity()).popFragment();
        }
    };

}
