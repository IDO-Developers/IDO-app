package com.matricula.ido;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.layout.simple_spinner_dropdown_item;
import static android.R.layout.simple_spinner_item;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String URL_Datos_Alumno=new Utilidades().URL_Datos_Alumno;
    private String URL_Datos_Grupos=new Utilidades().URL_Dstos_Grupos;
    private String identidadAlumno;
    private EditText rneAlumno;
    private EditText sexo;
    private EditText centro;
    private EditText grado;
    private EditText modalidad;
    private EditText jornada;
    private Spinner grupo;
    private EditText modulo;
    private ArrayList<PojoGrupos> arrayListsGrupos;
    private ArrayList<String> arrayListString = new ArrayList<String>();
    private Adaptador_Grupos adaptador_grupos;
    private PojoGrupos pojoGrupos = new PojoGrupos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rneAlumno = (EditText)findViewById(R.id.editTextRneAlumno);
        sexo = (EditText)findViewById(R.id.editTextSexo);
        centro = (EditText)findViewById(R.id.editTextCentro);
        grado = (EditText)findViewById(R.id.editTextGrado);
        grupo = (Spinner) findViewById(R.id.espinnerGrupo);
        modalidad = (EditText)findViewById(R.id.editTextModalidad);
        jornada = (EditText)findViewById(R.id.editTextJornada);
        modulo = (EditText)findViewById(R.id.editTextModulo);



        /**Metodo para obtener la informacion del alumno pasando como paramentro la identidad**/
        obtenerDatosAlumno(identidadAlumno);


        /**Metodo para obtener la informacion del los grupos pasando como paramentro la identidad**/
        obtenerDatosGrupos(identidadAlumno);

        grupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                   String posicion =grupo.getItemAtPosition(grupo.getSelectedItemPosition()).toString();

                   if (position!=0&&id!=0){
                       for (int i=0;i<arrayListsGrupos.size();i++){
                           modalidad.setText(arrayListsGrupos.get(i).getModalidad());
                           jornada.setText(arrayListsGrupos.get(i).getJornada());
                           modulo.setText(arrayListsGrupos.get(i).getModulo());
                       }

                   }



                }catch (Exception exc) {
                    {
                        Toast.makeText(MainActivity.this, "" + exc, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                modalidad.setText("");
                jornada.setText("");

            }
        });
    }



    private void obtenerDatosAlumno(final String identidad_Alumno){
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, URL_Datos_Alumno, null,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray=response.getJSONArray("infoAlumno");

                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                rneAlumno.setText(jsonObject.getString("RNE_Alumno"));
                                if (jsonObject.getString("Sexo").equalsIgnoreCase("F")){
                                    sexo.setText("Femenino");
                                }else if (jsonObject.getString("Sexo").equalsIgnoreCase("M")){
                                    sexo.setText("Masculino");
                                }
                                centro.setText(jsonObject.getString("Centro_Procedencia"));

                            }
                        }catch (Exception exc){

                            if (exc instanceof JSONException){
                                Toast.makeText(MainActivity.this, "Error con alguno de los datos", Toast.LENGTH_SHORT).show();
                            }
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

    private void obtenerDatosGrupos (String identidad_Alumno){

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, URL_Datos_Grupos, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            JSONArray jsonArray=response.getJSONArray("filaGrupos");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                arrayListsGrupos=new ArrayList<>();

                                pojoGrupos.setGrupo(jsonObject.getString("Grupo"));
                                pojoGrupos.setId(jsonObject.getInt("Id_Grupo"));
                                pojoGrupos.setJornada(jsonObject.getString("Jornada"));
                                pojoGrupos.setModalidad(jsonObject.getString("Nombre_Modalidad"));
                                grado.setText(jsonObject.getString("Grado"));
                                pojoGrupos.setModulo(jsonObject.getString("Nombre_Modulo"));

                                arrayListsGrupos.add(pojoGrupos);
                              //  adaptador_grupos = new Adaptador_Grupos(MainActivity.this,arrayListsGrupos);
                              //  grupo.setAdapter(adaptador_grupos);

                                for (int a = 0; a< arrayListsGrupos.size(); a++){
                                    arrayListString.add(pojoGrupos.getGrupo().toString());
                                }
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,simple_spinner_dropdown_item,
                                        arrayListString);
                                arrayAdapter.setDropDownViewResource(simple_spinner_dropdown_item);
                                grupo.setAdapter(arrayAdapter);
                            }

                        }catch (Exception exc){
                            if (exc instanceof JSONException){
                                Toast.makeText(MainActivity.this, "Error con alguno de los datos", Toast.LENGTH_SHORT).show();
                            }

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


}
