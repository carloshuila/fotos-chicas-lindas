package app.fotos.chicaslindas.personas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import app.fotos.chicaslindas.MainActivity;
import app.fotos.chicaslindas.R;
import app.fotos.chicaslindas.categorias.ListarCategoriasActivity;

public class ListarPersonaActivity extends AppCompatActivity {

    ArrayList<Persona> listaPersonas;
    FirebaseFirestore db ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaPersonas = new ArrayList<>();
        db  = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_listar_personas);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        EnviarListarRecyclerView(listaPersonas);

       //API Goolge AdmOB
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getResources().getString(R.string.admob_banner_ad1));
        //Google AdMob
        AdView mAdView = findViewById(R.id.ads_banner_personaLista1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //API Goolge AdmOB


        //boton atras
        ImageButton btnAtras = (ImageButton) findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(v -> onBackPressed());
        //Recibir datos
        String  nombreCategoria = getIntent().getStringExtra("nombreCategoria");
        toolbar_title.setText(nombreCategoria);


        db.collection("personas").whereEqualTo("categoria", nombreCategoria )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Persona persona = document.toObject(Persona.class);
                                listaPersonas.add(persona);
                                EnviarListarRecyclerView(listaPersonas);
                            }
                        }
                    }
                });

        //Barra navegacion
        BottomNavigationView navBar = findViewById(R.id.btnBarraNav);
        navBar.setSelectedItemId(R.id.MainActivity);

        navBar.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()){
                case R.id.MainActivity:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.CategoriasActivity:
                    startActivity(new Intent(getApplicationContext(), ListarCategoriasActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.CompartirActivity:
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.putExtra("android.intent.extra.SUBJECT", this.getString(R.string.app_name));
                    intent.putExtra("android.intent.extra.TEXT", this.getString(R.string.share_app_message) + ("\nhttps://play.google.com/store/apps/details?id=" + this.getPackageName()));
                    intent.setType("text/plain");
                    this.startActivity(Intent.createChooser(intent, getResources().getString(R.string.menu_compartir)));
                    return true;

            }
            return false;

        });
        //Fin barra navegacion

    }
    public void  EnviarListarRecyclerView( ArrayList<Persona> mispersonas){
        RecyclerView myRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerView_persona);
        AdapterPersona MyAdapter = new AdapterPersona(this,mispersonas);
        myRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        myRecyclerView.setAdapter(MyAdapter);
    }
}
