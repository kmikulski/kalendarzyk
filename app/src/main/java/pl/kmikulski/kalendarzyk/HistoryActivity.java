package pl.kmikulski.kalendarzyk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.DayDecorator;
import com.imanoweb.calendarview.DayView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences prefs = getSharedPreferences(Util.PREFS_DATA, 0);
        CustomCalendarView ccv = (CustomCalendarView) findViewById(R.id.calendar_view);
        DayDecorator dd = new DayDecorator() {
            @Override
            public void decorate(DayView dayView) {
                String key = Util.getIntervalKey(dayView.getDate());
                if(prefs.contains(key)) {
                    if(prefs.getBoolean(key, false)) {
                        dayView.setBackgroundColor(Color.BLUE);
                    } else {
                        dayView.setBackgroundColor(Color.GREEN);
                    }
                }
            }
        };
        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(dd);
        ccv.setDecorators(decorators);
        ccv.refreshCalendar(ccv.getCurrentCalendar());
    }
}
