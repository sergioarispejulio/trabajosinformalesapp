package com.example.sergioarispejulio.proyectogrado.clases;

import android.app.IntentService;
import android.content.Intent;

import com.example.sergioarispejulio.proyectogrado.R;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Sergio Arispe Julio on 7/18/2016.
 */
public class RegistrationService extends IntentService {

    public RegistrationService() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID myID = InstanceID.getInstance(this);
        try {
            String registrationToken = myID.getToken(
                    getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null
            );
            //GcmPubSub subscription = GcmPubSub.getInstance(this);
            //subscription.subscribe(registrationToken, "Proyectodegrado", null);
            singleton.getInstance().token_mensajeria = registrationToken;
            System.out.println("**********************************************");
            System.out.println("Registration Token: " + registrationToken);
            System.out.println("**********************************************");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}