package com.edmundo.pfseguridad.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.edmundo.pfseguridad.Model.ListItem;
import com.edmundo.pfseguridad.Model.Mensaje;
import com.edmundo.pfseguridad.R;
import java.util.Vector;

/**
 * Created by Edmundo on 10/21/15.
 */
public class RVAdapter extends RecyclerView.Adapter<ListItem> {


    public Vector<Mensaje> vectorDeMensajes;

    public RVAdapter(Vector<Mensaje> vectorDeMensajes){
        this.vectorDeMensajes = vectorDeMensajes;
    }



    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);

        ListItem listItem = new ListItem((LinearLayout) view);
        return listItem;
    }

    @Override
    public void onBindViewHolder(ListItem holder, int position) {
        TextView usuario;
        TextView campoMensajePlano;
        TextView campoMensajeEncriptado;

        usuario = (TextView) holder.linearLayout.findViewById(R.id.textusuario);
        campoMensajeEncriptado = (TextView) holder.linearLayout.findViewById(R.id.list_itm_text_view_encrypt);
        campoMensajePlano = (TextView) holder.linearLayout.findViewById(R.id.list_itm_text_view);

        usuario.setText(vectorDeMensajes.get(position).getUsuario());
        campoMensajeEncriptado.setText(vectorDeMensajes.get(position).getMensajeEncriptado());
        campoMensajePlano.setText(vectorDeMensajes.get(position).getMensajePlano());


    }

    @Override
    public int getItemCount() {
        return vectorDeMensajes.size();
    }


}
