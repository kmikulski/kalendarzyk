package pl.kmikulski.kalendarzyk;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Date;

public class MainService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        SharedPreferences prefs = getSharedPreferences(Util.PREFS_DATA, 0);
        Date date = new Date();
        String key = Util.getIntervalKey(date);
        if(!prefs.contains(key) && Util.shouldAsk(date)) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        }
        Util.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
