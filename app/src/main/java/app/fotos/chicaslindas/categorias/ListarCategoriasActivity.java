package app.fotos.chicaslindas.categorias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;


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

public class ListarCategoriasActivity extends AppCompatActivity {
    ArrayList<Categoria> listaCategorias;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaCategorias = new ArrayList<>();
        db  = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_listar_categorias);
        EnviarListarRecyclerView(listaCategorias);

        //API Goolge AdmOB
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView2 = new AdView(this);
        adView2.setAdSize(AdSize.BANNER);
        adView2.setAdUnitId(getResources().getString(R.string.admob_banner_ad2));
        //Google AdMob
        AdView mAdView2 = findViewById(R.id.ads_banner_categoria2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest);
        //Fin API Goolge AdmOB

        //boton atras
        ImageButton btnAtras = (ImageButton) findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(v -> onBackPressed());

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

        db.collection("categorias")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Categoria categoria = document.toObject(Categoria.class);
                                listaCategorias.add(categoria);
                                EnviarListarRecyclerView(listaCategorias);
                            }
                        }
                    }
                });


        }
        public void  EnviarListarRecyclerView( ArrayList<Categoria> misCategorias){
            RecyclerView myRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerView_listaCategorias);
            AdapterListarCategorias  MyAdapter = new AdapterListarCategorias (this,misCategorias);
            myRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
            myRecyclerView.setAdapter(MyAdapter);
        }


}

