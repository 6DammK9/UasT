package hk.ust.comp4521.UasT.data;

import java.util.Date;

/**
 * Created by Darren on 7/4/2015.
 */
public class CalendarEvent {
    private Date From;
    private Date To;
    private String Title;
    private String Body;
    private String Freq;
    private String Loc;
    private Date Remind;
    public final static long ONE_HOUR = 1000 * 60 * 60;
    public final static long ONE_DAY = 1000 * 60 * 60 * 24;
    public final static long ONE_WEEK = 1000 * 60 * 60 * 24 * 7;
    public final static long TWO_WEEK = 1000 * 60 * 60 * 24 * 7 * 2;
    //static final String TAG = "UasT.CalendarEvent";

    public CalendarEvent() {
        //EMPTY CONSTRUCTOR
        From = new Date();
        To = new Date(From.getTime() + ONE_HOUR);
        Title = "NEW  EVENT";
        Body = "Detail:";
        Freq = "Once";
        Loc = "Somewhere";
        Remind = null;
    }

    public CalendarEvent(long Cal_m) {
        //EMPTY CONSTRUCTOR
        From = new Date(Cal_m);
        To = new Date(From.getTime() + ONE_HOUR);
        Title = "NEW  EVENT";
        Body = "Detail:";
        Freq = "Once";
        Loc = "Somewhere";
        Remind = null;
    }

    public CalendarEvent(String in) {
        //Decided to use if statement instead of switch
        String[] pro = in.split("\n");
        if (pro.length > 0) From = StringToDate(pro[0]);
        if (pro.length > 1) To = StringToDate(pro[1]);
        if (pro.length > 2) Title = pro[2];
        if (pro.length > 3) Body = Body_MultiLine(pro[3]);
        if (pro.length > 4) Freq = pro[4];
        if (pro.length > 5) Loc = pro[5];
        if (pro.length > 6) Remind = StringToDate(pro[6]);
    }

    public void setCalEvent(Date a, Date b, String c, String d, String e, String f, Date g) {
        From = a;
        To = b;
        Title = c;
        Body = d;
        Freq = e;
        Loc = f;
        Remind = g;
    }

    public Date getFrom() {
        return From;
    }

    public Date getTo() {
        return To;
    }

    public String getTitle() {
        return Title;
    }

    public String getBody() {
        return Body;
    }

    public String getLoc() {
        return Loc;
    }

    public Date getRemind() {
        return Remind;
    }

    public String getFreq() {
        return Freq;
    }

    @Override
    public String toString() {
        if (Remind != null)
            return From.toString() + "\n" +
                    To.toString() + "\n" +
                    Title + "\n" +
                    Body_OneLine(Body) + "\n" +
                    Freq + "\n" +
                    Loc + "\n" +
                    Remind.toString();
        else return From.toString() + "\n" +
                To.toString() + "\n" +
                Title + "\n" +
                Body_OneLine(Body) + "\n" +
                Freq + "\n" +
                Loc;
    }

    public int getFreqIndex() {
        if (Freq.equals("Daily")) {
            return 1;
        } else if (Freq.equals("Weekly")) {
            return 2;
        } else if (Freq.equals("Bi-weekly")) {
            return 3;
        } else if (Freq.equals("Monthly")) {
            return 4;
        } else {
            return 0;
        }
    }

    public static int getFreqIndex(String in) {
        if (in.equals("Daily")) {
            return 1;
        } else if (in.equals("Weekly")) {
            return 2;
        } else if (in.equals("Bi-weekly")) {
            return 3;
        } else if (in.equals("Monthly")) {
            return 4;
        } else {
            return 0;
        }
    }

    public Boolean matchFrom(long time) {
        // Don't try to simplify - I mean Android Studio
        Date target = new Date(time);
        if ((From.getTime() - time) > (ONE_DAY)) {
            return false;
        }
        if ((Freq.equals("Once")) && (dif(From.getTime(), time) < ONE_DAY) &&
                (From.getDate() == target.getDate())) {
            return true;
        }
        if (Freq.equals("Daily")) {
            return true;
        }
        if ((Freq.equals("Weekly")) && (From.getDay() == target.getDay())) {
            return true;
        }
        if ((Freq.equals("Bi-weekly")) && (From.getDay() == target.getDay()) &&
                (dif(From.getTime(), time) % TWO_WEEK < ONE_WEEK)) {
            return true;
        }
        if ((Freq.equals("Monthly")) && (From.getDate() == target.getDate())) {
            return true;
        }
        return false;
    }

    public static long dif(long a, long b) {
        if (a > b) {
            return a - b;
        } else {
            return b - a;
        }
    }

    public static Date StringToDate(String in) {
        if (in == null) return null;
        if (in.isEmpty()) return null;

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

    public void setFrom(Date newDate) {
        From = newDate;
    }

    public void setTo(Date newDate) {
        To = newDate;
    }

    public void setRemind(Date newDate) {
        Remind = newDate;
    }

    public String Body_OneLine(String Body) {
        String a = Body.replaceAll("\n", "\t");
        return a.split("\n")[0];
    }

    public String Body_MultiLine(String in) {
        return in.replaceAll("\t", "\n");
    }
}
