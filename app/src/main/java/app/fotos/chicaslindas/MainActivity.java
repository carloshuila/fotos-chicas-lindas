package app.fotos.chicaslindas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import app.fotos.chicaslindas.categorias.AdapterCategoria;
import app.fotos.chicaslindas.categorias.ListarCategoriasActivity;
import app.fotos.chicaslindas.recomendado.AdapterRecomendado;
import app.fotos.chicaslindas.categorias.Categoria;
import app.fotos.chicaslindas.personas.Persona;
//API Goolge AdmOB
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Categoria> listaCategorias = new ArrayList<>();
    public ArrayList<Persona> listaRecomendados = new ArrayList<>();

    public FirebaseFirestore db ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db  = FirebaseFirestore.getInstance();

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
        AdView mAdView = findViewById(R.id.ads_banner_home1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Fin API Goolge AdmOB

        EnviarListarRecyclerView_CategoriasHome(listaCategorias);
        EnviarListarRecyclerViewRecomendados(listaRecomendados);
        mostrarCategorias();
        int numeroAleatorio = (int) (Math.random() * 9 + 1);
        switch (numeroAleatorio){
            case 1:
                verPersonasCategoria(getResources().getString(R.string.categoria_1));
                break;
            case 2:
                verPersonasCategoria(getResources().getString(R.string.categoria_2));
                break;
            case 3:
                verPersonasCategoria(getResources().getString(R.string.categoria_3));
                break;
            case 4:
                verPersonasCategoria(getResources().getString(R.string.categoria_4));
                break;
            case 5:
                verPersonasCategoria(getResources().getString(R.string.categoria_5));
                break;
            case 6:
                verPersonasCategoria(getResources().getString(R.string.categoria_6));
                break;
            case 7:
                verPersonasCategoria(getResources().getString(R.string.categoria_7));
                break;
            case 8:
                verPersonasCategoria(getResources().getString(R.string.categoria_8));
                break;
            case 9:
                verPersonasTodas();
                break;
            default:
                verPersonasTodas();
                break;
        }

        //Barra navegacion
        
        BottomNavigationView navBar = findViewById(R.id.btnBarraNav);
        navBar.setSelectedItemId(R.id.MainActivity);

        navBar.setOnNavigationItemSelectedListener((item) -> {
                switch (item.getItemId()){
                    case R.id.MainActivity:
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

    }



    public void EnviarListarRecyclerView_CategoriasHome(ArrayList<Categoria> misCategorias){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView myRecyclerView = findViewById(R.id.id_recyclerView_Home_Categorias);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setHasFixedSize(true);
        AdapterCategoria MyAdapter = new AdapterCategoria(this,misCategorias);
        myRecyclerView.setAdapter(MyAdapter);
    }

    public void  EnviarListarRecyclerViewRecomendados( ArrayList<Persona> misRecomendados){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView myRecyclerView = findViewById(R.id.id_recyclerView_producto_recomendado);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setHasFixedSize(true);
        AdapterRecomendado MyAdapter = new AdapterRecomendado(this,misRecomendados);
        myRecyclerView.setAdapter(MyAdapter);
    }

    public void mostrarCategorias(){
        db.collection("categorias")
                .get()
                .addOnCompleteListener(task ->  {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Categoria categoria = document.toObject(Categoria.class);
                            listaCategorias.add(categoria);
                            EnviarListarRecyclerView_CategoriasHome(listaCategorias);
                        }
                    }
                });
    }

    public void verPersonasTodas(){
        db.collection("personas").limit(30)
                .get()
                .addOnCompleteListener(task ->  {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Persona persona = document.toObject(Persona.class);
                            listaRecomendados.add(persona);
                            EnviarListarRecyclerViewRecomendados(listaRecomendados);
                        }
                    }
                });
    }

    public void verPersonasCategoria(String nombre_categoria){
        db.collection("personas").whereEqualTo("categoria", nombre_categoria).limit(30)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Persona persona = document.toObject(Persona.class);
                                listaRecomendados.add(persona);
                                EnviarListarRecyclerViewRecomendados(listaRecomendados);
                            }
                        }
                    }
                });
    }

}