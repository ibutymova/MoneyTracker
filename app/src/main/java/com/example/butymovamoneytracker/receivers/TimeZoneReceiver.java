package com.example.butymovamoneytracker.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.butymovamoneytracker.App;
import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.prefs.FilterDate;
import com.example.butymovamoneytracker.prefs.Prefs;
import com.example.butymovamoneytracker.screens.StartActivity;
import com.example.butymovamoneytracker.states.TimeZoneState;
import com.example.butymovamoneytracker.utils.FormatUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import static com.example.butymovamoneytracker.screens.StartActivity.KEY_AUTO_SIGN_IN;

public class TimeZoneReceiver extends BroadcastReceiver {

    private static final  int NOTIFY_ID = 99;
    private static final String CHANNEL_ID = "channel_id";
    private static final int REQUEST_CODE = 999;

    @Inject
    Prefs prefs;
    @Inject
    TimeZoneState timeZoneState;
    @Inject
    FormatUtils formatUtils;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        ((App)context.getApplicationContext()).getComponentsHolder().getAppComponent().injectTimeZoneReceiver(this);

        if (action.equals(Intent.ACTION_TIMEZONE_CHANGED) &&
                prefs.getLastTimeZone()!=null &&
                !TimeZone.getDefault().getID().equals(prefs.getLastTimeZone())) {

            FilterDate filterMinDate = formatUtils.getFilterDate(prefs.getFilterMinDate(), TimeZone.getTimeZone(prefs.getLastTimeZone()));
            prefs.setFilterMinDate(formatUtils.getDateMinLong(filterMinDate));

            FilterDate filterMaxDate = formatUtils.getFilterDate(prefs.getFilterMaxDate(), TimeZone.getTimeZone(prefs.getLastTimeZone()));
            prefs.setFilterMaxDate(formatUtils.getDateMaxLong(filterMaxDate));

            prefs.setLastTimeZone(TimeZone.getDefault().getID());
            timeZoneState.setTimeZoneId(TimeZone.getDefault().getID());

            Intent innerIntent = new Intent(context, StartActivity.class);
            innerIntent.putExtra(KEY_AUTO_SIGN_IN, true);
            innerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, innerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            String rawOffset = new SimpleDateFormat(context.getString(R.string.timezone_offset), Locale.getDefault()).format(TimeZone.getDefault().getRawOffset());

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(context.getResources().getString(R.string.notification_timezone_title))
                    .setContentText(context.getResources().getString(R.string.notification_timezone_description, TimeZone.getDefault().getDisplayName(), rawOffset));

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager==null)
                return ;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.notification_timezone_channel_name), NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(context.getResources().getString(R.string.notification_timezone_channel_description));
                notificationManager.createNotificationChannel(channel);
            }

            Notification notification = builder.build();
            notificationManager.notify(NOTIFY_ID, notification);
        }
    }
}
