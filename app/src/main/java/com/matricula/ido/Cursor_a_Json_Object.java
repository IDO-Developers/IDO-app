package com.matricula.ido;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class Cursor_a_Json_Object {

    public static JSONObject deStringAJson(String sexo, String rneAlumno, String idGrupo) {
        JSONObject jObject = new JSONObject();

        try {
            jObject.put("Sexo", sexo);
            jObject.put("RNE_Alumno", rneAlumno);
            jObject.put("Id_Grupo", idGrupo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObject;
    }
}
