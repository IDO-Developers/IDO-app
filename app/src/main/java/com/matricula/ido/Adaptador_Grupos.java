package com.matricula.ido;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Adaptador_Grupos extends ArrayAdapter<PojoGrupos> {
    ArrayList<PojoGrupos> lista;
    Context context;

    public Adaptador_Grupos(Context context, ArrayList<PojoGrupos> lista) {
        super(context, R.layout.lista_spinner, lista);
        this.lista= lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PojoGrupos pojoGrupos = getItem(position);
        if (convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grupos, null, false);

        TextView grupo = (TextView)convertView.findViewById(R.id.grupo);

        grupo.setText(pojoGrupos.getGrupo());

        return convertView;
    }
}

