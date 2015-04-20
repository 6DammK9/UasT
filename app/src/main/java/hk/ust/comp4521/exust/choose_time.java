package hk.ust.comp4521.exust;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Date;

import hk.ust.comp4521.exust.data.CalendarEvent;

/**
 * Created by YuXuZaWa on 17/4/2015.
 */
public class choose_time extends BaseFragment {
    private String curStr;
    Record _parent;
    private String _target;

    private View view;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button BtnAccept;
    private Button BtnDecline;

    private Date cur;

    public void setParam(String time, Record parent, String target) {
        this.curStr = time;
        this._parent = parent;
        this._target = target;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.choose_time, null);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        BtnAccept = (Button) view.findViewById(R.id.BtnAccept);
        BtnDecline = (Button) view.findViewById(R.id.BtnDecline);

        cur = CalendarEvent.StringToDate(curStr);
        if (cur == null)
            cur = _parent.getFromTime();
        datePicker.init(cur.getYear() + 1900, cur.getMonth(), cur.getDate(), null);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(cur.getHours());
        timePicker.setCurrentMinute(cur.getMinutes());

        BtnAccept.setOnClickListener(AcceptListener);
        BtnDecline.setOnClickListener(DeclineListener);

        return view;
    }

    @Override
    public String getTitle() {
        return "Calendar";
    }

    public OnClickListener AcceptListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            cur = new Date(datePicker.getYear()-1900,datePicker.getMonth(),datePicker.getDayOfMonth(),timePicker.getCurrentHour(),timePicker.getCurrentMinute());
            _parent.updateText(_target, cur);

            ((MainActivity) getActivity()).popFragment();
        }
    };

    private OnClickListener DeclineListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ((MainActivity) getActivity()).popFragment();
        }
    };
}
