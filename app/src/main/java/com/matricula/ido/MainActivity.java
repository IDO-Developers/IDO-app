package com.matricula.ido;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private String URL_Datos_Alumno = new Utilidades().URL_Datos_Alumno;
    private String URL_Datos_Grupos = new Utilidades().URL_Datos_Grupos;
    private String URL_Matricular = new Utilidades().URL_Matricular;
    private String identidadAlumno;
    private EditText rneAlumno;
    private EditText sexo;
    private EditText grado;
    private EditText modalidad;
    private EditText jornada;
    private Spinner grupo;
    private EditText modulo;
    private Button matricular;
    private ArrayList<PojoGrupos> arrayListsGrupos;
    private ArrayList<String> arrayListString = new ArrayList<String>();
    private Adaptador_Grupos adaptador_grupos;
    private PojoGrupos pojoGrupos;
    private String espacioVacioSpinner;
    private String sexoJson;
    private String idGrupo;
    private String obtnValorSpinner;
    private JSONArray jsonArraySpinner;
    private int posicion;
    private View contentViewTxtModalidad;
    private View contentViewTxtModulo;
    private View contentViewTxtJornada;
    private View contentViewEditModalidad;
    private View contentViewEditModulo;
    private View contentViewEditJornada;

    private int shortAnimationDuration;
    private View loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rneAlumno = (EditText) findViewById(R.id.editTextRneAlumno);
        sexo = (EditText) findViewById(R.id.editTextSexo);
        grado = (EditText) findViewById(R.id.editTextGrado);
        grupo = (Spinner) findViewById(R.id.espinnerGrupo);
        modalidad = (EditText) findViewById(R.id.editTextModalidad);
        jornada = (EditText) findViewById(R.id.editTextJornada);
        modulo = (EditText) findViewById(R.id.editTextModulo);
        matricular = (Button) findViewById(R.id.botonMatricular);

        contentViewTxtModalidad = (TextView)findViewById(R.id.txtModalidad);
        contentViewTxtModulo = (TextView)findViewById(R.id.txtModulo);
        contentViewTxtJornada = (TextView)findViewById(R.id.txtJornada);
        contentViewEditModalidad = (EditText)findViewById(R.id.editTextModalidad);
        contentViewEditModulo = (EditText)findViewById(R.id.editTextModulo);
        contentViewEditJornada = (EditText)findViewById(R.id.editTextJornada);

        contentViewTxtModalidad.setVisibility(View.GONE);
        contentViewTxtModulo.setVisibility(View.GONE);
        contentViewTxtJornada.setVisibility(View.GONE);
        contentViewEditModalidad.setVisibility(View.GONE);
        contentViewEditModulo.setVisibility(View.GONE);
        contentViewEditJornada.setVisibility(View.GONE);

        shortAnimationDuration = getResources().getInteger(
               android.R.integer.config_mediumAnimTime);

        /**Metodo para obtener la informacion del alumno pasando como paramentro la identidad**/
        obtenerDatosAlumno(identidadAlumno);

        /**Metodo para obtener la informacion del los grupos pasando como paramentro la identidad**/
        obtenerDatosGrupos(identidadAlumno);

        /**eveento que establecera los campos de modulo, modalidad y jornada cuando seleccionen un elemento del spinner**/
        grupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
               obtnValorSpinner = grupo.getItemAtPosition(grupo.getSelectedItemPosition()).toString();

               posicion=position;
                try {
                    if (obtnValorSpinner.equals("")) {
                        modalidad.setText("");
                        jornada.setText("");
                        modulo.setText("");
                    } else {

                        modalidad.setText(arrayListsGrupos.get(position).getModalidad());
                        jornada.setText(arrayListsGrupos.get(position).getJornada());
                        modulo.setText(arrayListsGrupos.get(position).getModulo());
                        idGrupo = String.valueOf(arrayListsGrupos.get(position).getId());
                        crossfade();

                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                modalidad.setText("");
                jornada.setText("");
                modulo.setText("");
            }
        });

        /**evento de boton matricular**/
        matricular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obtnValorSpinner.equals("")){
                    Toast.makeText(MainActivity.this, "¡Por Favor! Seleccione un grupo para poder matricular", Toast.LENGTH_LONG).show();
                }else {
                    matricularAlumno(rneAlumno.getText().toString());
                }
            }
        });
    }

    private void obtenerDatosAlumno(final String identidad_Alumno) {
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, URL_Datos_Alumno, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    JSONArray jsonArray = response.getJSONArray("infoAlumno");

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        rneAlumno.setText(jsonObject.getString("RNE_Alumno"));
                                        if (jsonObject.getString("Sexo").equalsIgnoreCase("F")) {
                                            sexo.setText("Femenino");
                                            sexoJson = "F";
                                        } else if (jsonObject.getString("Sexo").equalsIgnoreCase("M")) {
                                            sexo.setText("Masculino");
                                            sexoJson = "M";
                                        }
                                    }
                                } catch (Exception exc) {

                                    if (exc instanceof JSONException) {
                                        Toast.makeText(MainActivity.this, "Error con alguno de los datos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof AuthFailureError) {
                            Toast.makeText(MainActivity.this, "Hay problemas con la autenticación", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(MainActivity.this, "Problemas con la red", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(MainActivity.this, "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(MainActivity.this, "Tenemos problemas, intentelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
    }

    private void obtenerDatosGrupos(String identidad_Alumno) {

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, URL_Datos_Grupos,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("filaGrupos");
                            arrayListsGrupos = new ArrayList<>();
                            arrayListString.add("");
                            arrayListsGrupos.add(pojoGrupos);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (jsonArray.length()==1){
                                    arrayListsGrupos.clear();
                                    arrayListString.clear();
                                    grupo.setSelection(0);
                                }

                                jsonArraySpinner =jsonArray;

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                pojoGrupos = new PojoGrupos();

                                pojoGrupos.setId(jsonObject.getInt("Id_Grupo"));
                                pojoGrupos.setJornada(jsonObject.getString("Jornada"));
                                pojoGrupos.setModalidad(jsonObject.getString("Nombre_Modalidad"));
                                pojoGrupos.setModulo(jsonObject.getString("Nombre_Modulo"));
                                pojoGrupos.setGrupo(jsonObject.getString("Grupo"));
                                grado.setText(jsonObject.getString("Grado"));

                                /**ArrayList con solo el grupo para mostrar en el spinner**/
                                arrayListString.add(pojoGrupos.getGrupo());

                                /**Arraylist con todos los elementos de la clase pojo**/
                                arrayListsGrupos.add(pojoGrupos);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, simple_spinner_dropdown_item,
                                    arrayListString);
                            adapter.setDropDownViewResource(simple_spinner_dropdown_item);
                            grupo.setAdapter(adapter);

                        } catch (Exception exc) {
                            if (exc instanceof JSONException) {
                                Toast.makeText(MainActivity.this, "Error con alguno de los datos", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof AuthFailureError) {
                    Toast.makeText(MainActivity.this, "Hay problemas con la autenticación", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(MainActivity.this, "Problemas con la red", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MainActivity.this, "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MainActivity.this, "Tenemos problemas, intentelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    private void matricularAlumno(final String identidad_Alumno) {

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.POST, URL_Matricular,
                        new Cursor_a_Json_Object().deStringAJson(sexoJson, identidad_Alumno, idGrupo),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                   if (response!=null){
                                       Toast.makeText(MainActivity.this, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                   }

                                } catch (Exception exc) {
                                    exc.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof AuthFailureError) {
                            String mensaje;
                            mensaje = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            try {
                                JSONObject jsonObject = new JSONObject(mensaje);
                                Toast.makeText(MainActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(MainActivity.this, "Problemas con la red", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(MainActivity.this, "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(MainActivity.this, "Tenemos problemas, intentelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
    }

    private void crossfade() {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        contentViewTxtModalidad.setAlpha(0f);
        contentViewTxtModalidad.setVisibility(View.VISIBLE);

        contentViewTxtModulo.setAlpha(0f);
        contentViewTxtModulo.setVisibility(View.VISIBLE);

        contentViewTxtJornada.setAlpha(0f);
        contentViewTxtJornada.setVisibility(View.VISIBLE);

        contentViewEditModalidad.setAlpha(0f);
        contentViewEditModalidad.setVisibility(View.VISIBLE);

        contentViewEditModulo.setAlpha(0f);
        contentViewEditModulo.setVisibility(View.VISIBLE);

        contentViewEditJornada.setAlpha(0f);
        contentViewEditJornada.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        contentViewTxtModalidad.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);
        contentViewTxtModulo.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);
        contentViewTxtJornada.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);
        contentViewEditModalidad.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);
        contentViewEditModulo.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);
        contentViewEditJornada.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        loadingView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingView.setVisibility(View.GONE);
                    }
                });
    }

}
