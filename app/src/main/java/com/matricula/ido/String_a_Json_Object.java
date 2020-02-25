package com.matricula.ido;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class String_a_Json_Object {

    public static JSONObject deStringAJsonInfoAlumno(String sexo, String rneAlumno, String idGrupo) {
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

    public static JSONObject deStringAJsonLogin(String rneAlumno, String contraseña){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("RNE_Alumno",rneAlumno);
            jsonObject.put("password", contraseña);
        }catch (Exception exc){
            exc.printStackTrace();
        }

        return jsonObject;
    }
}
