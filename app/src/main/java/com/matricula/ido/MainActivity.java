package com.matricula.ido;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.matricula.ido.PDF.TemplatePDF;
import com.itextpdf.text.Image;
import com.matricula.ido.PDF.crearPDF;
import com.matricula.ido.SharedPreferences.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static android.R.layout.simple_spinner_dropdown_item;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.matricula.ido.SharedPreferences.PreferencesUtility.IDENTIDAD;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.TOKEN_AUTH;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.NOMBRE;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.GRADO;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.GRUPO;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.MODALIDAD;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.MODULO;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.JORNADA;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.PDF;


public class MainActivity extends AppCompatActivity {

    private TemplatePDF templatePDF;

    private String URL_Datos_Alumno = new Utilidades().URL_Datos_Alumno;
    private String URL_Datos_Grupos = new Utilidades().URL_Datos_Grupos;
    private String URL_Matricular = new Utilidades().URL_Matricular;
    private String URL_LOGOUT = new Utilidades().URL_LOGOUT;
    public String identidadAlumno;
    private TextView nombreAlumno;
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
    private int reponseLength;
    private View contentViewTxtModalidad;
    private View contentViewTxtModulo;
    private View contentViewTxtJornada;
    private View contentViewEditModalidad;
    private View contentViewEditModulo;
    private View contentViewEditJornada;

    private int shortAnimationDuration;
    private View loadingView;
    private SharedPreferences sharedPreferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        identidadAlumno=sharedPreferences.getString(IDENTIDAD,"");
        token=sharedPreferences.getString(TOKEN_AUTH,"");

        rneAlumno = (EditText) findViewById(R.id.editTextRneAlumno);
        sexo = (EditText) findViewById(R.id.editTextSexo);
        grado = (EditText) findViewById(R.id.editTextGrado);
        grupo = (Spinner) findViewById(R.id.espinnerGrupo);
        modalidad = (EditText) findViewById(R.id.editTextModalidad);
        jornada = (EditText) findViewById(R.id.editTextJornada);
        modulo = (EditText) findViewById(R.id.editTextModulo);
        matricular = (Button) findViewById(R.id.botonMatricular);
        nombreAlumno = (TextView) findViewById(R.id.txtNombreAlumno);


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
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(NOMBRE, nombreAlumno.getText().toString());
                    editor.putString(GRADO, grado.getText().toString());
                    editor.putString(GRUPO, grupo.getSelectedItem().toString());
                    editor.putString(MODALIDAD, modalidad.getText().toString());
                    editor.putString(MODULO, modulo.getText().toString());
                    editor.putString(JORNADA, jornada.getText().toString());
                    editor.apply();
                    editor.commit();
                    matricularAlumno(rneAlumno.getText().toString());
                }
            }
        });
    }

    private void obtenerDatosAlumno(final String identidad_Alumno) {
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, URL_Datos_Alumno+identidad_Alumno, null,
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
                                        nombreAlumno.setText(jsonObject.getString("Nombres")+" "+jsonObject.getString("Apellidos"));
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

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, URL_Datos_Grupos+identidad_Alumno,
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

    /**Metodoo que realiza la peticion de matricula al servidor**/
    private void matricularAlumno(final String identidad_Alumno) {

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.POST, URL_Matricular,
                        new String_a_Json_Object().deStringAJsonInfoAlumno(sexoJson, identidad_Alumno, idGrupo),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                   if (response!=null){
                                       Toast.makeText(MainActivity.this, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                       crearPDF(identidad_Alumno);
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

    /**Metodo para cerrar Sesion**/
    private void cerrarSesion(){
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,URL_LOGOUT, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("sesion");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Toast.makeText(MainActivity.this, ""+jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();
                            }
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            SaveSharedPreference.setLoggedIn(MainActivity.this,false);
                            Intent intent = new Intent(MainActivity.this,Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }catch (Exception exc){
                            exc.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof AuthFailureError) {
                    Toast.makeText(MainActivity.this, "Problema en la autenticacion", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(MainActivity.this, "Problemas con la red", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MainActivity.this, "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MainActivity.this, "Tenemos problemas, intentelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization",token);

                return headers;
            }
        });
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



    private void crearPDF(String identidad_Alumno){

        try{
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        101);
            } else {
                new crearPDF().crear(rneAlumno.getText().toString(),
                        nombreAlumno.getText().toString(),
                        grado.getText().toString(),
                        grupo.getSelectedItem().toString(),
                        modalidad.getText().toString(),
                        modulo.getText().toString(),
                        jornada.getText().toString(),MainActivity.this);
            }
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 100:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    new crearPDF().crear(rneAlumno.getText().toString(),
                            nombreAlumno.getText().toString(),
                            grado.getText().toString(),
                            grupo.getSelectedItem().toString(),
                            modalidad.getText().toString(),
                            modulo.getText().toString(),
                            jornada.getText().toString(),MainActivity.this);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cerrarsesion, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.cerrarSesion:
                cerrarSesion();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
