package pl.kmikulski.kalendarzyk;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by Kuba on 12.05.2017.
 */

public class Util {

    public static final String PREFS_DATA = "KALENDARZYK_PREFS_DATA";


    public static final SimpleDateFormat INTERVAL_KEY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String getIntervalKey(Date date) {
        return INTERVAL_KEY_FORMAT.format(date);
    }

    public static String getIntervalKey() {
        return getIntervalKey(new Date());
    }

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, MainService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(3600 * 1000); // wait at least
        builder.setOverrideDeadline(5400 * 1000); // maximum delay
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    public static boolean shouldAsk(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour >= 19 && hour <= 23;
    }

    public static String getXml(Context context) {
        Map<String, ?> prefs = context.getSharedPreferences(Util.PREFS_DATA, 0).getAll();
        StringBuffer sb = new StringBuffer();
        sb.append("<KALENDARZYK>\n");
        for(String key : new TreeSet<String>(prefs.keySet())) {
            sb.append("<DAY DATE=" + key + ">" + prefs.get(key) + "</DAY>\n");
        }
        sb.append("</KALENDARZYK>");
        return sb.toString();
    }

    public static void copyToClipboard(Context context, String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
