package app.fotos.chicaslindas.personas;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.CookieManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

import app.fotos.chicaslindas.R;

public class VerPersonaActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;//Google AdMob
    private String url_Imagen;

    String nombreImagen;

    FirebaseFirestore db;
    Persona persona = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nombreImagen = "nombreImagen";
        db  = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_ver_persona);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        isStoragePermissionGranted();


       //API Goolge AdmOB
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        ///Banner Inferior
        AdView adView2 = new AdView(this);
        adView2.setAdSize(AdSize.BANNER);
        adView2.setAdUnitId(getResources().getString(R.string.admob_banner_ad2));
        //Google AdMob
        AdView mAdView2 = findViewById(R.id.ads_banner_verPersona2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest);

        anuncioIntersticial(adRequest);
        //Fin API Goolge AdmOB

        PhotoView ivImagen = (PhotoView) findViewById(R.id.id_imagen_persona_Act);
        //Recibir datos
        Bundle personaEnviado = getIntent().getExtras();
        if (personaEnviado != null){
            persona = (Persona) personaEnviado.getSerializable("persona");
            Glide.with(this).load(persona.getImagen()).into(ivImagen);
            url_Imagen = persona.getImagen();
            nombreImagen = persona.getNombre();
            if(nombreImagen == null){
                toolbar_title.setText(getResources().getString(R.string.titulo_barra_ver_persona));
            }else {
                toolbar_title.setText(nombreImagen);
            }

        }
        FloatingActionButton btnDescargar = findViewById(R.id.floating_btn_descargar);
        btnDescargar.setOnClickListener(v -> {
           if (mInterstitialAd != null) {
                mInterstitialAd.show(VerPersonaActivity.this);
            }
            startDownload(url_Imagen);
        });

        //boton atras
        ImageButton btnAtras = (ImageButton) findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(v -> onBackPressed());
    }

    public void startDownload(String url_img) {
        Toast makeText = android.widget.Toast.makeText(this, getResources().getString(R.string.inicio_descarga), android.widget.Toast.LENGTH_SHORT);
        makeText.show();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_img));
        request.setAllowedNetworkTypes(3);
        request.setNotificationVisibility(1);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(nombreImagen+".png");
        request.setVisibleInDownloadsUi(true);
        String cookie = CookieManager.getInstance().getCookie(url_img);
        request.addRequestHeader("cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Fotos Chicas Lindas/" + nombreImagen+".png" );
        ((DownloadManager) this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(this, new String[]{new File(Environment.DIRECTORY_DOWNLOADS + "/" + "/Fotos Chicas Lindas/" +  nombreImagen+".png" ).getAbsolutePath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String url_img, Uri uri) {
                    }
                });
                return;
            }
            this.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(Environment.DIRECTORY_DOWNLOADS + "/" + "/Fotos Chicas Lindas/" + nombreImagen+".png" ))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    public void anuncioIntersticial(AdRequest adRequest){
        InterstitialAd.load(this,getResources().getString(R.string.admob_interstitial_ad), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {

                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

}
