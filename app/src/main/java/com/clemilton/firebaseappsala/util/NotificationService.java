package com.clemilton.firebaseappsala.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.clemilton.firebaseappsala.NavigationActivity;
import com.clemilton.firebaseappsala.R;
import com.clemilton.firebaseappsala.model.Request;
import com.clemilton.firebaseappsala.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.clemilton.firebaseappsala.util.App.CHANNEL_1;

public class NotificationService extends Service {
    //https://gist.github.com/k33ptoo/8c34e0c767750e88f5db2ed55db4c783
    @Override
    public void onCreate() {
        //chamado uma unica vez
        super.onCreate();

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("requests")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Query items = reference.limitToLast(1);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Pegar novos usuarios
                for (DataSnapshot items : snapshot.getChildren()) {
                    Request req = items.getValue(Request.class);


                    DatabaseReference user = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(req.getUserId());

                    user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User u = snapshot.getValue(User.class);
                            showNotif(req.getType(),u);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showNotif(String reqType,User user) {
        //push a notification
        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                .setContentTitle(reqType)
                .setContentText(user.getNome())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("info");


        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }


/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        DatabaseReference database = (DatabaseReference)intent.getSerializableExtra("firebasedb");


        Intent notificationIntent  = new Intent(getApplicationContext(), NavigationActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, notificationIntent, 0);

        Notification notification = new NotificationCompat
                .Builder(this,CHANNEL_FIREBASE)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_one)
                .build();

        startForeground(1, notification);

        //constantes definem o que o S.O faz quando o servico termina
        //NOT_STRICKY -> serviço é terminado e nao sera executado novamente
        // STICKY -> o sistema vai reiniciar o serviço assim que possivel, com INTENT  nula
        // REDILIVER_STICKY-> reiniciar serviço com a mesma intent
        return START_NOT_STICKY;

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

