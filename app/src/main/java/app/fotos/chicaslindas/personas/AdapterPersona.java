package app.fotos.chicaslindas.personas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.fotos.chicaslindas.R;

public class AdapterPersona extends RecyclerView.Adapter<AdapterPersona.MyViewHolder> {

    private final Context micontext;
    private ArrayList<Persona> listapersonas;


    public AdapterPersona(Context micontext, ArrayList<Persona> listaPersonas) {
        this.micontext = micontext;
        listapersonas = listaPersonas;
    }

    public ArrayList<Persona> getListapersonas() {
        return listapersonas;
    }

    public void setListapersonas(ArrayList<Persona> listapersonas) {
        this.listapersonas = listapersonas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup miparent, int viewType) {

        View miview;
        LayoutInflater minflater = LayoutInflater.from(micontext);
        miview = minflater.inflate(R.layout.cardview_item_listar_persona,miparent,false);
        return new MyViewHolder(miview);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Glide.with(micontext).load(listapersonas.get(position).getImagen()).into(holder.imgPersona);

        final Persona persona = listapersonas.get(position);


        holder.cardViewPersona.setOnClickListener((v) -> {

            Intent intent = new Intent(micontext, VerPersonaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("persona", persona);
            intent.putExtras(bundle);
            micontext.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return listapersonas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPersona;
        CardView cardViewPersona;

        public MyViewHolder(View itemView){
            super(itemView);

            imgPersona = (ImageView) itemView.findViewById(R.id.id_persona_img);
            cardViewPersona = (CardView) itemView.findViewById(R.id.id_cardViewPersona);
        }
    }
}
