package com.dam.asfaltame;

import android.content.Intent;

import com.dam.asfaltame.Database.AppRepository;
import com.dam.asfaltame.Model.Report;
import com.dam.asfaltame.Model.Status;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class AsfaltameMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){


        if (remoteMessage.getData().size() > 0){
            final int reportId = Integer.valueOf(remoteMessage.getData().get("reportId"));

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    AppRepository ap = AppRepository.getInstance(getApplicationContext());

                    Report report = ap.reportDao.getById(reportId).get(0);

                    report.setStatus(Status.REPARADO);
                    ap.reportDao.update(report);

                    Intent i = new Intent();
                    i.putExtra("reportId", reportId);
                    i.setAction("com.dam.asfaltame.REPARADO");
                    sendBroadcast(i);
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }
}
