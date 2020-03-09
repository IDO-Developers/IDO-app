package com.matricula.ido.Grupos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.matricula.ido.R;
import java.util.ArrayList;

public class Adaptador_Grupos extends ArrayAdapter<PojoGrupos> {
    ArrayList<PojoGrupos> lista;
    Context context;

    public Adaptador_Grupos(Context context, ArrayList<PojoGrupos> lista) {
        super(context, R.layout.item_grupos, lista);
        this.lista= lista;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return view(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return view(position, convertView, parent);
    }

    private View view (int position, View convertView, ViewGroup parent){
        PojoGrupos pojoGrupos = getItem(position);
        if (convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grupos, null);

        TextView grupo = (TextView)convertView.findViewById(R.id.grupo);
        TextView cupos = (TextView)convertView.findViewById(R.id.cupos);

        if (pojoGrupos!=null) {
            grupo.setText(pojoGrupos.getGrupo());
        }
        return convertView;
    }
    @Nullable
    @Override
    public PojoGrupos getItem(int position) {
        return lista.get(position);
    }
}
