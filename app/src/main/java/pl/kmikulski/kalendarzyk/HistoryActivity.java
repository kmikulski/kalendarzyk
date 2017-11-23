package pl.kmikulski.kalendarzyk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.DayDecorator;
import com.imanoweb.calendarview.DayView;

import java.util.ArrayList;
import java.util.Date;
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
        final CustomCalendarView ccv = (CustomCalendarView) findViewById(R.id.calendar_view);
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
        ccv.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                showDayChangePopup(date, ccv);
            }
            @Override
            public void onMonthChanged(Date date) {
            }
        });
    }

    private void showDayChangePopup(final Date date, final CustomCalendarView ccv) {
        ccv.refreshCalendar(ccv.getCurrentCalendar());
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup, (ViewGroup) findViewById(R.id.popup_1));
        final PopupWindow pw = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
        final String key = Util.getIntervalKey(date);
        final SharedPreferences prefs = getSharedPreferences(Util.PREFS_DATA, 0);
        ((TextView)layout.findViewById(R.id.popup_text)).setText("Zmiana " + key + " - czy wtedy tak?");
        layout.findViewById(R.id.popup_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putBoolean(key, true).commit();
                pw.dismiss();
                ccv.refreshCalendar(ccv.getCurrentCalendar());
            }
        });
        layout.findViewById(R.id.popup_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putBoolean(key, false).commit();
                pw.dismiss();
                ccv.refreshCalendar(ccv.getCurrentCalendar());
            }
        });
    }
}
