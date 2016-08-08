package com.sloy.sevibus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.util.Log;

import com.sloy.sevibus.resources.StuffProvider;
import com.sloy.sevibus.resources.actions.HasExpiringBonobusAction;
import com.sloy.sevibus.ui.activities.HomeActivity;

import java.util.Random;

/**
 * $ adb -d shell am broadcast -a "com.sloy.sevibus.TEST_BONOBUS_EXPIRING"
 */
public class BonobusExpiringTestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Sevibus", "hola bebés");
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        HasExpiringBonobusAction hasExpiringBonobusAction = StuffProvider.getHasBonobusesExpiringAction(context);
        hasExpiringBonobusAction.hasExpiringBonobus()
                .filter(Boolean.TRUE::equals)
                .subscribe(__ -> {
                    String message = "Ojocuidao, tienes un bonobús con menos de 1€. ¡Acuérdate de recargarlo!";
                    Notification notification = new Notification.Builder(context)
                            .setContentTitle("Aviso de Bonobús")
                            .setContentText("Tienes uno a punto de agotarse")
                            .setStyle(new Notification.BigTextStyle().bigText(message))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setSmallIcon(R.drawable.ic_notification_default)
                            .setLights(Color.RED, 500, 5000)
                            .setColor(Color.RED)
                            .setContentIntent(PendingIntent.getActivity(context, 1, HomeActivity.createIntentForSectionBonobus(context), 0))
                            .setAutoCancel(true)
                            .build();

                    nm.notify(new Random().nextInt(1000), notification);
                });
    }
}