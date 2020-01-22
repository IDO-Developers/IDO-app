package com.matricula.ido;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String URL_Datos_Alumno=R.string.urlAlumno+"getDatosRegistro/";
    private String identidadAlumno;
    private EditText rneAlumno;
    private EditText sexo;
    private EditText centro;
    private EditText grado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rneAlumno = (EditText)findViewById(R.id.editTextRneAlumno);
        sexo = (EditText)findViewById(R.id.editTextSexo);
        centro = (EditText)findViewById(R.id.editTextCentro);
        grado = (EditText)findViewById(R.id.editTextGrado);


        /**Metodo para obtener la informacion del alumno pasando como paramentro la identidad**/
        obtenerDatosAlumno(identidadAlumno);
        /**Metodo para obtener la informacion del centro del estudiante pasando como parametro la identidad**/
        obtenerDatoCentro(identidadAlumno);
        /**Metodo para obtener el grado del alumno**/
        obtenerDatoGrado(identidadAlumno);



    }


    private void obtenerDatosAlumno(final String identidad_Alumno){
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, URL_Datos_Alumno+identidad_Alumno, null,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray=response.getJSONArray("usuario");

                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                rneAlumno.setText(jsonObject.getString("RNE_Alumno"));
                                sexo.setText(jsonObject.getString("Sexo"));

                            }
                        }catch (Exception exc){

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof AuthFailureError){
                            Toast.makeText(MainActivity.this, "Hay problemas con la autenticación", Toast.LENGTH_SHORT).show();
                        }else if (error instanceof NetworkError){
                            Toast.makeText(MainActivity.this, "Problemas con la red", Toast.LENGTH_SHORT).show();
                        }else if(error instanceof TimeoutError){
                            Toast.makeText(MainActivity.this, "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                        }else if(error instanceof ServerError){
                            Toast.makeText(MainActivity.this, "Tenemos problemas, intentelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
        );

    }

    private void obtenerDatoCentro(final String identidad_Alumno){
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, "", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("centro");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                centro.setText(jsonObject.getString("Nombre_Centro"));
                            }

                        }catch (Exception exc){

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof AuthFailureError){
                    Toast.makeText(MainActivity.this, "Hay problemas con la autenticación", Toast.LENGTH_SHORT).show();
                }else if (error instanceof NetworkError){
                    Toast.makeText(MainActivity.this, "Problemas con la red", Toast.LENGTH_SHORT).show();
                }else if(error instanceof TimeoutError){
                    Toast.makeText(MainActivity.this, "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                }else if(error instanceof ServerError){
                    Toast.makeText(MainActivity.this, "Tenemos problemas, intentelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                }
            }

        }));
    }

    private void obtenerDatoGrado(final String identidad_Alumno){
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, "", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("grado");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                grado.setText(jsonObject.getString("Grado"));

                            }
                        }catch (Exception exc){

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }
}
