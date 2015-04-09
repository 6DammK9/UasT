package hk.ust.comp4521.exust;

/**
 * Created by YuXuZaWa on 7/4/2015.
 */
public class CALENDAR_OLD {

//BACKUP ONLY - THIS IS AN EMPTY CLASS!

/**
    public class CalendarFragment extends BaseFragment
    {

        GridView gridView;
        View view;

        boolean isGroup;

        // events8m25 is used to update gridview
        public String[] events8m25;

        // if the time interval is full, events7m24[i]= "F"
        // if the time interval is empty, events7m24[i]="";
        // you can use events7m24 to communicate with the data(7*24) in your server
        public String[] events7m24;

        public String[] group8m25;
        public String[] group7m24;

        public String[] eventContents;// record contents
        private boolean[] ifClick;
        // determine which position can be clicked
        // the cell contains"mon","fir","00.01","00:02" cannot be cliked.

        AdapterView.OnItemClickListener personalListener;
        AdapterView.OnItemClickListener groupListener;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {

            view = inflater.inflate(R.layout.grid_view, null);
            gridView = (GridView) view.findViewById(R.id.mygridview);
            gridView.setNumColumns(8);

            if (isGroup)

                updateGroup();

            else
                updatePersonal();

            personalListener = new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id)
                {

                    if (ifClick[position])
                    {

                        MainActivity main = (MainActivity) getActivity();
                        Record fragment = new Record();
                        fragment.setParam(position, CalendarFragment.this);

                        main.gotoFragment(1, fragment);
                    }

                }

            };

            groupListener = new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id)
                {
                }

            };

            gridView.setOnItemClickListener(!isGroup ? personalListener
                    : groupListener);

            return view;

        }

        public void setPersonal()
        {
            isGroup = false;
        }

        public void setGroup()
        {
            isGroup = true;
            syncGroupEvents8m25WithEvents7m24();
        }

        public void setGroup8m25(String[] group8m25)
        {
            for (int i = 0; i < 8 * 25; i++)
                this.group8m25[i] = group8m25[i];
        }

        public void setGroup7m24(String[] group7m24)
        {
            for (int i = 0; i < 7 * 24; i++)
                this.group7m24[i] = group7m24[i];
        }

        // get a 7*24 array(the users' calender)
        public String[] get7m24()
        {

            return events7m24;
        }

        public void syncGroupEvents8m25WithEvents7m24()
        {
            int j = 0;
            for (int i = 0; i < 8 * 25; i++)
            {
                if (ifClick[i] == true)
                {
                    group8m25[i] = group7m24[j];
                    j++;
                }
            }
        }

        public void syncEvents8m25WithEvents7m24()
        {
            int j = 0;
            for (int i = 0; i < 8 * 25; i++)
            {
                if (ifClick[i] == true)
                {
                    events8m25[i] = events7m24[j];
                    j++;
                }
            }
        }

        public void syncEvents7m24WithEvents8m25()
        {
            int j = 0;

            for (int i = 0; i < 8 * 25; i++)
            {
                if (ifClick[i] == true)
                {
                    events7m24[j] = events8m25[i];
                    j++;
                }
            }
        }

        public void setEvents7m24and8m25(String[] str7m24)
        {

            for (int w = 0; w < 7 * 24; w++)
                events7m24[w] = str7m24[w];

            syncEvents8m25WithEvents7m24();

        }

        public void setEvents(String[] str7m24)
        {
            for (int w = 0; w < 7 * 24; w++)

                group7m24[w] = str7m24[w];

            int j = 0;
            for (int i = 0; i < 8 * 25; i++)
            {
                if (ifClick[i] == true)
                {
                    eventContents[i] = str7m24[j];
                    j++;
                }
            }

        }

        public String[] getEvents()
        {
            String[] str7m24 = new String[7 * 24];

            int j = 0;
            for (int i = 0; i < 8 * 25; i++)
            {
                if (ifClick[i] == true)
                {
                    str7m24[j] = eventContents[i];
                    j++;
                }
            }
            return str7m24;

        }

        public String[] get8m25()
        {
            return eventContents;

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

        public void initialize()
        {
            events8m25 = new String[8 * 25];
            eventContents = new String[8 * 25];
            events7m24 = new String[7 * 24];

            group8m25 = new String[8 * 25];
            group7m24 = new String[7 * 24];

            ifClick = new boolean[8 * 25];

            for (int i = 0; i < 7 * 24; i++)
            {
                events7m24[i] = "";
                group7m24[i] = "";
            }

            for (int i = 0; i < 8 * 25; i++)
            {
                events8m25[i] = "";
                eventContents[i] = "";
                group8m25[i] = "";
                ifClick[i] = true;
            }

            for (int i = 0; i < 8; i++)
                ifClick[i] = false;

            events8m25[0] = "";
            events8m25[1] = "Mon ";
            events8m25[2] = "Tues";
            events8m25[3] = "Wed ";
            events8m25[4] = "Thur";
            events8m25[5] = "Fri    ";
            events8m25[6] = "Sat    ";
            events8m25[7] = "Sun  ";

            group8m25[0] = "";
            group8m25[1] = "Mon ";
            group8m25[2] = "Tues";
            group8m25[3] = "Wed ";
            group8m25[4] = "Thur";
            group8m25[5] = "Fri    ";
            group8m25[6] = "Sat    ";
            group8m25[7] = "Sun  ";

            for (int i = 0; i < 24; i++)
            {
                String hourStr;
                if (i < 10)
                    hourStr = '0' + String.valueOf(i) + ":00";
                else
                    hourStr = String.valueOf(i) + ":00";

                events8m25[(i + 1) * 8] = hourStr;
                group8m25[(i + 1) * 8] = hourStr;
                ifClick[(i + 1) * 8] = false;
            }
            // eventNames[8] = "00:00";
            // eventNames[16] = "01:00";
            // eventNames[24] = "02:00";
            // eventNames[32] = "03:00";
            // eventNames[40] = "04:00";

        }

        // you can use this function to
        // ensure SetEvents7m24and8m25() is working well;
        // public void testSetEvents7m24and8m25()
        // {
        // String[] str7m24 = new String[7 * 24];
        // for (int i = 0; i < 7 * 24; i++)
        // {
        // str7m24[i] = "";
        // if (i % 7 == 6)
        // {
        // str7m24[i] = "hi";
        // }
        // }
        //
        // setEvents7m24and8m25(str7m24);
        // }

        public void updatePersonal()
        {
            // testSetEvents7m24and8m25();

            for (int i = 0; i < 8 * 25; i++)
            {
                if (ifClick[i] == true)
                {
                    if (!eventContents[i].equals(""))
                    {
                        events8m25[i] = "F";
                    }
                }
            }

            List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < events8m25.length; i++)
            {
                Map<String, Object> item = new HashMap<String, Object>();

                item.put("eventName", events8m25[i]);
                items.add(item);

            }

            SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
                    R.layout.grid_item, new String[] { "eventName" },
                    new int[] { R.id.text });

            gridView.setAdapter(adapter);

        }

        public void updateGroup()
        {
            // testSetEvents7m24and8m25();

            List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < group8m25.length; i++)
            {
                Map<String, Object> item = new HashMap<String, Object>();

                item.put("eventName", group8m25[i]);
                items.add(item);

            }

            SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
                    R.layout.grid_item, new String[] { "eventName" },
                    new int[] { R.id.text });

            gridView.setAdapter(adapter);

        }

    }

// Toast.makeText(getApplicationContext(),
// String.valueOf(position) ,
// Toast.LENGTH_SHORT).show();

// eventNames[1] = "Monday";
// eventNames[2] = "Thusday";
// eventNames[3] = "Wednesday";
// eventNames[4] = "Thursday";
// eventNames[5] = "Firday";
// eventNames[6] = "Saturday";
// eventNames[7] = "Sunday";

// string[] groupCalendar=new string [7*24];
//
// for(calendar: calendars)
// {
// for(i=0;i<7*24;i++)
// {
// if( calendar[i] =="F")
// groupCalendar[i]++;
// }
// }
//
// setGroup7m24( groupCalendar);

**/
}
