package app.fotos.chicaslindas;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import app.fotos.chicaslindas.personas.Persona;
import app.fotos.chicaslindas.personas.VerPersonaActivity;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String titulo = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
       String body = remoteMessage.getNotification().getBody();
        String imagen = remoteMessage.getData().get("imagen");
        String nombre = remoteMessage.getData().get("nombre");
        String categoria = remoteMessage.getData().get("categoria");
        String imgIcono = remoteMessage.getData().get("icono");

        if (!remoteMessage.getData().isEmpty()){

            crearNotificacion(titulo, body, imagen, nombre, categoria,imgIcono);
        }
        else{
            crearNotificacion(titulo, body, imagen, nombre, categoria,imgIcono);
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }


    private void crearNotificacion(String titulo, String body, String imagen, String nombre, String categoria, String imgIcono){
        final  String CHANNEL_ID = " NOTIFICATION_CHANNEL_ID";

        Persona persona = new Persona(nombre, categoria,imagen);
        Intent intent = new Intent(this, VerPersonaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("persona", persona);
        intent.putExtras(bundle);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(VerPersonaActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(titulo)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLights(Color.MAGENTA, 1000, 1000)
                .setSound(defaultSoundUri)
                .setColor(Color.BLACK)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


        GlideApp.with(getApplicationContext())
                .asBitmap()
                .load(imgIcono)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        //largeIcon
                        builder.setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });


        GlideApp.with(getApplicationContext())
                .asBitmap()
                .load(imagen)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        //Big Picture
                        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource));

                        Notification notification = builder.build();
                        notificationManager.notify(NotificationID.getID(), notification);
                        startForeground(0, notification);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });
    }
    static class NotificationID {
        private final static AtomicInteger c = new AtomicInteger(100);

        public static int getID() {
            return c.incrementAndGet();
        }
    }

}