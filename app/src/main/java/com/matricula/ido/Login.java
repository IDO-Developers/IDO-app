package com.matricula.ido;

import android.Manifest;
import android.app.ActivityOptions;
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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.itextpdf.text.Image;
import com.matricula.ido.PDF.TemplatePDF;
import com.matricula.ido.PDF.ViewPdf;
import com.matricula.ido.SharedPreferences.SaveSharedPreference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import com.matricula.ido.PDF.crearPDF;
import static android.R.layout.simple_spinner_dropdown_item;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.IDENTIDAD;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.PDF;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.TOKEN_AUTH;


public class Login extends AppCompatActivity {

    private TemplatePDF templatePDF;
    private String URL_Login = new Utilidades().URL_Login;
    private String URL_Datos_Alumno = new Utilidades().URL_Datos_Alumno;
    private String URL_Datos_Grupos = new Utilidades().URL_Datos_Grupos;
    private File pdfFile;
    private RelativeLayout rellay1, rellay2, rellay3;
    private Button btn_login, btn_restaurar_contrasenia;
    private EditText contraseña,identidad;
    private TextView tituloapp;
    private EditText correo_restaurar;
    private Button restaurar;
    private ProgressBar loading;
    private Handler handler = new Handler();
    private SharedPreferences sharedPreferences;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
                rellay1.setVisibility(View.VISIBLE);
                rellay2.setVisibility(View.VISIBLE);
                tituloapp.setVisibility(View.INVISIBLE);
        }
    };
    private String pass;
    private String ident;
    private Boolean registered;
    private String identidadPref;
    private String pathPdfPref;
    private String pathPdf;
    private String nombre;
    private String grado;
    private String grupo;
    private String modulo;
    private String modalidad;
    private String jornada;
    private String identidadPdf;
    private int contadorGrupos;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        if (SaveSharedPreference.getLoggedStatus(Login.this)){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                identidadPref = sharedPreferences.getString(IDENTIDAD,"");

                obtenerDatosAlumno(identidadPref);
                obtenerDatosGrupos(identidadPref);

               /**Valida si el pdf existe pàra abrirlo*/
                File folder = new File(Environment.getExternalStorageDirectory().toString(), "IDO/");
                pdfFile = new File(folder,"Verificar Matricula: "+identidadPref+".pdf");

                if (pdfFile.exists()==true){
                    viewPDF(pdfFile);
                }else {
                    pdf();
                }
            }else{
                String identidad=sharedPreferences.getString(IDENTIDAD,"");

                /**Valida si el pdf existe pàra abrirlo*/
                File folder = new File(Environment.getExternalStorageDirectory().toString(), "IDO/");
                pdfFile = new File(folder,"Verificar Matricula: "+identidad+"");

                if (pdfFile.exists()==true){
                    viewPDF(pdfFile);
                }else {
                    pdf();
                }
            }
        }else{
            handler.postDelayed(runnable, 1000); //800 es el tiempo de espera
        }
        setContentView(R.layout.login);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        rellay3 = findViewById(R.id.rellay3);
        tituloapp = findViewById(R.id.tituloApp);
        btn_restaurar_contrasenia = findViewById(R.id.button_restaurar_contrasenia);
        btn_login = findViewById(R.id.button_login);
        identidad = findViewById(R.id.correo_login_ET);
        correo_restaurar = findViewById(R.id.correo_restaurar);
        contraseña = findViewById(R.id.contrasenia_ET);
        loading = findViewById(R.id.loading);
        restaurar = findViewById(R.id.boton_restaurar);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btn_login.setBackground(getResources().getDrawable(R.drawable.bg_buttons2));
                }

                pass=contraseña.getText().toString();
                ident=identidad.getText().toString();

                obtenerDatosAlumno(ident);
                obtenerDatosGrupos(ident);


                if ((identidad.getText().toString().trim().length() > 0) && (contraseña.getText().toString().trim().length() > 0)) {
                    /**Valida si el pdf existe pàra abrirlo**/
                    File folder = new File(Environment.getExternalStorageDirectory().toString(), "IDO/");
                    pdfFile = new File(folder,"Verificar Matricula: "+identidad.getText().toString()+".pdf");

                    if (pdfFile.exists()==true){
                        viewPDF(pdfFile);
                    }else{
                        login(ident, pass);
                    }
                } else {
                    if (identidad.getText().toString().length() == 0 ||
                            identidad.getText().toString().trim().equalsIgnoreCase("")) {
                        identidad.setError("Ingresa su Identidad");
                    }
                    if (contraseña.getText().toString().trim().length() == 0 ||
                            contraseña.getText().toString().trim().equalsIgnoreCase("")) {
                        contraseña.setError("Ingresa la contraseña");
                    }
                }
            }
        });
    }

    /**Metodo para loggearse**/
    private void login(final String identidad, final String contraseña){
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.POST, URL_Login,
                new String_a_Json_Object().deStringAJsonLogin(identidad,contraseña),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response!=null){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                                    Toast.makeText(Login.this, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                    String token = response.getString("access_token");
                                    String token_type = response.getString("token_type");
                                    String token_mas_token_type = token_type + " " + token;

                                    /**Crea el shared preferences**/
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(TOKEN_AUTH, token_mas_token_type);
                                    editor.putString(IDENTIDAD,identidad);
                                    SaveSharedPreference.setLoggedIn(Login.this,true);
                                    editor.apply();
                                    editor.commit();

                                    if (contadorGrupos==1){
                                        pdf();
                                    }else{
                                        /**Lanza a la actividad principal cuando el logueo es correcto**/
                                        Intent mainActivity = new Intent(Login.this, MainActivity.class);
                                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        finish();
                                        startActivity(mainActivity, ActivityOptions.makeSceneTransitionAnimation(Login.this).toBundle());
                                    }
                                }else{
                                    if (contadorGrupos==1){
                                        pdf();
                                    }else {
                                        /**Lanza a la actividad principal cuando el logueo es correcto**/
                                        Intent mainActivity = new Intent(Login.this, MainActivity.class);
                                        startActivity(mainActivity);
                                    }
                                }
                            }
                        }catch (Exception exc){
                            Toast.makeText(Login.this, ""+exc, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Login.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Login.this, "Problemas con la red", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Login.this, "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Login.this, "Tenemos problemas, intentelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        }));
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

                                        identidadPdf = jsonObject.getString("RNE_Alumno");
                                        nombre = jsonObject.getString("Nombres")+" "+jsonObject.getString("Apellidos");
                                    }
                                } catch (Exception exc) {

                                    if (exc instanceof JSONException) {
                                        Toast.makeText(Login.this, "Error con alguno de los datos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof AuthFailureError) {
                            Toast.makeText(Login.this, "Hay problemas con la autenticación", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Login.this, "Problemas con la red", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(Login.this, "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(Login.this, "Tenemos problemas, intentelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
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

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                contadorGrupos=jsonArray.length();
                                jornada = jsonObject.getString("Jornada");
                                modalidad = jsonObject.getString("Nombre_Modalidad");
                                modulo = jsonObject.getString("Nombre_Modulo");
                                grupo = jsonObject.getString("Grupo");
                                grado = jsonObject.getString("Grado");
                            }
                        } catch (Exception exc) {
                            if (exc instanceof JSONException) {
                                Toast.makeText(Login.this, "Error con alguno de los datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof AuthFailureError) {
                    Toast.makeText(Login.this, "Hay problemas con la autenticación", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Login.this, "Problemas con la red", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Login.this, "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Login.this, "Tenemos problemas, intentelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    private void pdf(){
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
                new crearPDF().crear(identidadPdf,nombre,grado,grupo,modalidad,modulo,jornada,Login.this);
            }
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    public  void viewPDF(File pdfFile){
        Intent intent = new Intent(Login.this, ViewPdf.class);
        intent.putExtra("path",pdfFile.getAbsolutePath());
        intent.putExtra("file",pdfFile.toString());
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

}
