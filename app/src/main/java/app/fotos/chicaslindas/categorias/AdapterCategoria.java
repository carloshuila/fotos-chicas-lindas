package app.fotos.chicaslindas.categorias;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import app.fotos.chicaslindas.R;
import app.fotos.chicaslindas.personas.ListarPersonaActivity;
import app.fotos.chicaslindas.personas.Persona;

public class AdapterCategoria extends RecyclerView.Adapter<AdapterCategoria.MyViewHolder> {

    public Context micontext;
    private ArrayList<Categoria> listaCategorias = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AdapterCategoria(Context micontext, ArrayList<Categoria> listaCategorias) {
        this.micontext = micontext;
        this.listaCategorias = listaCategorias;
    }

    @Override
    public AdapterCategoria.MyViewHolder onCreateViewHolder(@NonNull ViewGroup miparent, int viewType) {

        View miview;
        LayoutInflater minflater = LayoutInflater.from(micontext);
        miview = minflater.inflate(R.layout.cardview_item_home_categorias, miparent, false);
        return new AdapterCategoria.MyViewHolder(miview);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategoria.MyViewHolder holder, final int position) {

        holder.nombreCategoria.setText((listaCategorias.get(position).getNombre()));

        Glide.with(micontext).load(listaCategorias.get(position).getImagen()).into(holder.imgCategoria);

        holder.cardViewCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String nombreCategoria = listaCategorias.get(position).nombre;
                Intent intent = new Intent(micontext, ListarPersonaActivity.class);
                intent.putExtra("nombreCategoria", nombreCategoria);
                micontext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombreCategoria;
        ImageView imgCategoria;
        CardView cardViewCategoria;

        public MyViewHolder(View itemView) {
            super(itemView);

            nombreCategoria = (TextView) itemView.findViewById(R.id.id_nombre_categoria_home);
            imgCategoria = (ImageView) itemView.findViewById(R.id.id_img_categoria_home);
            cardViewCategoria = (CardView) itemView.findViewById(R.id.id_cardView_CategoriaHome);
        }
    }

    public void cargarDatos(List<Persona>ArrayPersonas ){
        db.collection("personas")
                .get()
                .addOnCompleteListener(task ->  {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Persona persona = document.toObject(Persona.class);
                            ArrayPersonas.add(persona);
                        }
                    }
                });
    }


}
