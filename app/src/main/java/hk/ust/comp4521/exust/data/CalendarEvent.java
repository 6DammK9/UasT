package hk.ust.comp4521.exust.data;

import java.util.Date;

/**
 * Created by YuXuZaWa on 7/4/2015.
 */
public class CalendarEvent {
    private Date From;
    private Date To;
    private String Title;
    private String Body;
    private String Freq;
    private String Loc;
    public final static long ONE_HOUR = 1000*60*60;
    public final static long ONE_DAY = 1000*60*60*24;
    public final static long ONE_WEEK = 1000*60*60*24*7;
    public final static long TWO_WEEK = 1000*60*60*24*7*2;
    //static final String TAG = "exust.CalendarEvent";

    public CalendarEvent() {
        //EMPTY CONSTRUCTOR
        From = new Date();
        To = new Date(From.getTime()+ONE_HOUR);
        Title = "ADD NEW";
        Body = "BLANK EVENT";
        Freq = "Once";
        Loc = "SOMEWHERE";
    }

    public CalendarEvent(String in) {
        String[] pro = in.split("\n");
        From = StringToDate(pro[0]);
        To = StringToDate(pro[1]);
        Title = pro[2];
        Body = pro[3];
        Freq = pro[4];
        Loc = pro[5];
    }

    public void setCalEvent(Date a, Date b, String c, String d, String e, String f) {
        From = a;
        To = b;
        Title = c;
        Body = d;
        Freq = e;
        Loc = f;
    }

    public Date getFrom() {return From;}
    public Date getTo() {return To;}
    public String getTitle() {return Title;}
    public String getBody() {return Body;}
    public String getLoc() {return Loc;}

    @Override
    public String toString() {
        return From.toString() + "\n" +
                To.toString() + "\n" +
                Title + "\n" +
                Body + "\n" +
                Freq + "\n" +
                Loc;
    }

    public int getFreqIndex() {
        if (Freq.equals("Daily")) {return 1;}
        else if (Freq.equals("Weekly")) {return 2;}
        else if (Freq.equals("Bi-weekly")) {return 3;}
        else if (Freq.equals("Monthly")) {return 4;}
        else {return 0;}
    }

    public String getFrom4UI() {
        if (From.getHours() < 10) {
            if (From.getMinutes() < 10) {
                return "0" + From.getHours() + ":0" + From.getMinutes();
            } else {
                return "0" + From.getHours() + ":" + From.getMinutes();
            }
        } else if (From.getMinutes() < 10) {
            return From.getHours() + ":0" + From.getMinutes();
        } else {
            return From.getHours() + ":" + From.getMinutes();
        }

    }

    public Boolean matchFrom(long time) {
        // Don't try to simplify - I mean Android Studio
        Date target = new Date(time);
        if ((Freq.equals("Once")) && (dif(From.getTime(), time) < ONE_DAY) &&
                (From.getDate() == target.getDate())) {return true;}
        if (Freq.equals("Daily")) {return true;}
        if ((Freq.equals("Weekly")) && (From.getDay() == target.getDay())) {return true;}
        if ((Freq.equals("Bi-weekly")) && (From.getDay() == target.getDay()) &&
                (dif(From.getTime(),time)%TWO_WEEK < ONE_WEEK)) {return true;}
        if ((Freq.equals("Monthly")) && (From.getDate() == target.getDate())) {return true;}
        return false;
    }

    public static long dif(long a, long b) {
        if (a>b) {return a-b;}
        else {return b-a;}
    }

    public static Date StringToDate (String in) {
        String[] MonStr = {"Jun", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] prase1 = in.split(" "); //Tue Apr 07 17:32:47 CST 2015
        String[] prase2 = prase1[3].split(":");

        int mon = 0;
        while ((!prase1[1].equals(MonStr[mon])) || mon >= 11) {
            mon++;
        }

        return new Date(Integer.parseInt(prase1[5]) - 1900,
                mon,
                Integer.parseInt(prase1[2]),
                Integer.parseInt(prase2[0]),
                Integer.parseInt(prase2[1]),
                Integer.parseInt(prase2[2]));
    }
}
