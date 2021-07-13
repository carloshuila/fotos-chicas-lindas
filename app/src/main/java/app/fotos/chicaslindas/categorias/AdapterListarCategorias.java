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

import java.util.ArrayList;

import app.fotos.chicaslindas.R;
import app.fotos.chicaslindas.personas.ListarPersonaActivity;

public class AdapterListarCategorias  extends RecyclerView.Adapter<AdapterListarCategorias.MyViewHolder> {

        public Context micontext;
        public ArrayList<Categoria> listaCategorias;

        public AdapterListarCategorias(Context micontext, ArrayList<Categoria> listaCategorias) {
            this.micontext = micontext;
            this.listaCategorias = listaCategorias;
        }

        @Override
        public AdapterListarCategorias.MyViewHolder onCreateViewHolder(@NonNull ViewGroup miparent, int viewType) {

            View miview;
            LayoutInflater minflater = LayoutInflater.from(micontext);
            miview = minflater.inflate(R.layout.cardview_item_listar_categoria,miparent,false);
            return new AdapterListarCategorias.MyViewHolder(miview);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterListarCategorias.MyViewHolder holder, final int position) {
            holder.nombreCategoria.setText((listaCategorias.get(position).getNombre()));
            Glide.with(micontext).load(listaCategorias.get(position).getImagen()).into(holder.imgCategoria);

            holder.cardViewCategoria.setOnClickListener(v -> {
                final  String nombreCategoria = listaCategorias.get(position).nombre;

                Intent intent = new Intent(micontext, ListarPersonaActivity.class);
                intent.putExtra("nombreCategoria", nombreCategoria);
                micontext.startActivity(intent);
            });



        }

        @Override
        public int getItemCount() {
            return listaCategorias.size();
        }


        public static class MyViewHolder extends RecyclerView.ViewHolder{
            TextView nombreCategoria;
            ImageView imgCategoria;
            CardView cardViewCategoria;
            public MyViewHolder(View itemView){
                super(itemView);

                nombreCategoria = (TextView) itemView.findViewById(R.id.id_nombre_listar_categoria);
                imgCategoria  = (ImageView) itemView.findViewById(R.id.id_img_listar_categoria);
                cardViewCategoria= (CardView) itemView.findViewById(R.id.id_cardView_ListarCategoria);
            }
        }


}
