package remining.farzad119.timeremining;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Farzad119 on 7/13/2016.
 */

public class TimeRemining {
    public TimeRemining() {}

    public String getReminingTime(String jsonTimes) {
        long diff;
        String reminingTime;
        diff = CalcDiff(jsonTimes);
        reminingTime = getFormattedMill(diff);
        return reminingTime;
    }

    //convert mill to day,hours,minutes and seconds
    public String getFormattedMill(long millis) {
        if (millis < 0) {
            Log.wtf("diff", "Duration must be greater than zero!");
        }
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(hours + days * 24);
        sb.append(" Hours ");
        sb.append(minutes + hours);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");

        return (sb.toString());
    }

    /*
    calculate difference Between now time and end time with json inpu
    json sample : {"id":"49","now":"2016-07-13 16:04:00","endtime":"2016-07-14 17:30:50"}
    */
    public long CalcDiff(String json) {

        long diff = 0;

        try {
            JSONObject jObj = new JSONObject(json);

            //Toast.makeText(ReminingTimeActivity.this, "End time : " + jObj.getString("endtime") + "\n now :" + jObj.getString("now"), Toast.LENGTH_LONG).show();

            //joda kardan zaman va tarikh
            //example current : 2016-1-30 19:20:25
            //example endtime : 2016-1-30 09:20:00
            String[] CurrentDateArrayHelper = jObj.getString("now").split(" ");
            String[] FinalDateArrayHelper = jObj.getString("endtime").split(" ");

            //joda kardan adaad tarikh
            String[] CurrentDate = CurrentDateArrayHelper[0].split("-");
            String[] FinalDate = FinalDateArrayHelper[0].split("-");

            //joda karda adaad zaman
            String[] CurrentTime = CurrentDateArrayHelper[1].split(":");
            String[] FinalTime = FinalDateArrayHelper[1].split(":");

            //tabdil tarikh be calendar
            Calendar calDatenow = Calendar.getInstance();
            calDatenow.set(Integer.valueOf(CurrentDate[0]), Integer.valueOf(CurrentDate[1]), Integer.valueOf(CurrentDate[2]));

            Calendar calDatenowFinal = Calendar.getInstance();
            calDatenowFinal.set(Integer.valueOf(FinalDate[0]), Integer.valueOf(FinalDate[1]), Integer.valueOf(FinalDate[2]));

            //tabdil zaman be calendar
            Calendar calTimeenow = Calendar.getInstance();
            calTimeenow.set(Integer.valueOf(CurrentTime[0]), Integer.valueOf(CurrentTime[1]), Integer.valueOf(CurrentTime[2]));

            Calendar calTimeFinal = Calendar.getInstance();
            calTimeFinal.set(Integer.valueOf(FinalTime[0]), Integer.valueOf(FinalTime[1]), Integer.valueOf(FinalTime[2]));

            //set kardan tarikh va zaman dar yek calendar
            Calendar thatDay = Calendar.getInstance();
            thatDay.set(Calendar.SECOND, Integer.valueOf(FinalTime[2]));
            thatDay.set(Calendar.MINUTE, Integer.valueOf(FinalTime[1]));
            thatDay.set(Calendar.HOUR, Integer.valueOf(FinalTime[0]));
            thatDay.set(Calendar.DAY_OF_MONTH, Integer.valueOf(FinalDate[2]));
            thatDay.set(Calendar.MONTH, Integer.valueOf(FinalDate[1]) - 1);
            thatDay.set(Calendar.YEAR, Integer.valueOf(FinalDate[0]));

            Log.wtf("SECOND", Integer.valueOf(FinalTime[2]) + "");
            Log.wtf("MINUTE", Integer.valueOf(FinalTime[1]) + "");
            Log.wtf("HOUR", Integer.valueOf(FinalTime[0]) + "");
            Log.wtf("DAY_OF_MONTH", Integer.valueOf(FinalDate[2]) + "");
            Log.wtf("MONTH", Integer.valueOf(FinalDate[1]) + "");
            Log.wtf("YEAR", Integer.valueOf(FinalDate[0]) + "");

            Calendar today = Calendar.getInstance();
            today.set(Calendar.SECOND, Integer.valueOf(CurrentTime[2]));
            today.set(Calendar.MINUTE, Integer.valueOf(CurrentTime[1]));
            today.set(Calendar.HOUR, Integer.valueOf(CurrentTime[0]));
            today.set(Calendar.DAY_OF_MONTH, Integer.valueOf(CurrentDate[2]));
            today.set(Calendar.MONTH, Integer.valueOf(CurrentDate[1]) - 1); // 0-11 so 1 less
            today.set(Calendar.YEAR, Integer.valueOf(CurrentDate[0]));

            diff = thatDay.getTimeInMillis() - today.getTimeInMillis();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return diff;
    }
}
