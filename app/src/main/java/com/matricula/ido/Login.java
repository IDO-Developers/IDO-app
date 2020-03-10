package com.matricula.ido;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.matricula.ido.Base_de_Datos.BaseDeDatosInfoAlumno;
import com.matricula.ido.PDF.TemplatePDF;
import com.matricula.ido.PDF.ViewPdf;
import com.matricula.ido.SharedPreferences.SaveSharedPreference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.matricula.ido.PDF.crearPDF;

import static com.matricula.ido.SharedPreferences.PreferencesUtility.HORA_FECHA;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.IDENTIDAD;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.PDF;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.TOKEN_AUTH;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.NOMBRE;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.GRADO;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.GRUPO;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.MODALIDAD;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.MODULO;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.JORNADA;


public class Login extends AppCompatActivity {

    private TemplatePDF templatePDF;
    private String URL_Login = new Utilidades().URL_LOGIN;
    private String URL_Datos_Alumno = new Utilidades().URL_Datos_Alumno;
    private String URL_Datos_Grupos = new Utilidades().URL_Datos_Grupos;
    private File pdfFile;
    private RelativeLayout rellay1, rellay2, rellay3;
    private Button btn_login, btn_restaurar_contrasenia;
    private EditText contraseña,identidad;
    private SharedPreferences sharedPreferences;
    private TextView tituloapp;
    private EditText correo_restaurar;
    private Button restaurar;
    private ProgressBar loading;
    private Handler handler = new Handler();
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
    private String identidadPref;
    private String nombre;
    private String grado;
    private String grupo;
    private String modalidad;
    private String modulo;
    private String jornada;
    private String fecha;
    private String cont;
    private BaseDeDatosInfoAlumno db = new BaseDeDatosInfoAlumno(this);
    private String PROJECTION[] = new String[]{BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.NOMBRE,
            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD,
            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRUPO,
            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRADO,
            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODULO,
            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODALIDAD,
            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.JORNADA,
            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.JORNADA,
            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.FECHA_HORA,
            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.CONTADOR_GRUPOS};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        sharedPreferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);



        if (SaveSharedPreference.getLoggedStatus(Login.this)){
            SQLiteDatabase sqliteLeer = db.getReadableDatabase();
            Cursor cursor = sqliteLeer.query(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.TABLE_NAME,PROJECTION,
                    null,null,
                    null,
                    null,
                    null);
            cursor.moveToFirst();

            identidadPref = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD));
            nombre = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.NOMBRE));
            grado = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRADO));
            grupo = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRUPO));
            modalidad = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODALIDAD));
            modulo = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODULO));
            jornada = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.JORNADA));
            fecha = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.FECHA_HORA));
            cont = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.CONTADOR_GRUPOS));
            /**Valida si el pdf existe pàra abrirlo*/
            File folder = new File(Environment.getExternalStorageDirectory().toString(), "IDO/");
            pdfFile = new File(folder,"Verificar Matricula: "+identidadPref+".pdf");

           if (grupo!=null){
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   if (pdfFile.exists()==true){
                       viewPDF(pdfFile);
                   }else {
                       pdf(identidadPref,nombre,grado, grupo, modalidad,modulo,jornada,fecha);
                   }
               }else{
                   if (pdfFile.exists()==true){
                       viewPDF(pdfFile);
                   }else {
                       pdf(identidadPref,nombre,grado, grupo, modalidad,modulo,jornada,fecha);
                   }
               }
           }else{
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   /**Lanza a la actividad principal cuando el logueo es correcto**/
                   Bundle identidadI = new Bundle();
                   identidadI.putString("identidad", identidadPref);
                   Intent mainActivity = new Intent(Login.this, MainActivity.class);
                   mainActivity.putExtras(identidadI);
                   mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(mainActivity, ActivityOptions.makeSceneTransitionAnimation(Login.this).toBundle());
                   finish();
               }else{
                   /**Lanza a la actividad principal cuando el logueo es correcto**/
                   Bundle identidadI = new Bundle();
                   identidadI.putString("identidad", identidadPref);
                   Intent mainActivity = new Intent(Login.this, MainActivity.class);
                   mainActivity.putExtras(identidadI);
                   mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   finish();
                   startActivity(mainActivity);
               }
           }
        }else{
           handler.postDelayed(runnable, 1200); //800 es el tiempo de espera
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btn_login.setBackground(getResources().getDrawable(R.drawable.bg_buttons2));
                }
                pass=contraseña.getText().toString();
                ident=identidad.getText().toString();

                if ((identidad.getText().toString().trim().length() > 0) && (contraseña.getText().toString().trim().length() > 0)) {
                    /**Valida si el pdf existe pàra abrirlo**/

                        login(ident, pass);
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
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.POST, URL_Login,
                new String_a_Json_Object().deStringAJsonLogin(identidad,contraseña),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loading.setVisibility(View.GONE);

                        try {

                            Log.i("LOGIN",""+response.toString());
                            if (response!=null){

                                    JSONArray jsonArray = response.getJSONArray("datoAlumno");
                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                       SQLiteDatabase dbEscribir = db.getWritableDatabase();
                                       ContentValues values = new ContentValues();
                                       values.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.NOMBRE,jsonObject.getString("Nombres")+" "+
                                               jsonObject.getString("Apellidos"));
                                       values.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD,identidad);
                                       dbEscribir.insert(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.TABLE_NAME,null,values);
                                       dbEscribir.close();
                                    }
                                    Toast.makeText(Login.this, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                    String token = response.getString("access_token");
                                    String token_type = response.getString("token_type");
                                    String token_mas_token_type = token_type + " " + token;
                                    int cp = response.getInt("cp");
                                Log.i("LOGIN",""+token_mas_token_type+" cp "+cp);

                                    /**Crea el shared preferences**/
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(TOKEN_AUTH, token_mas_token_type);
                                    SaveSharedPreference.setLoggedIn(Login.this,true);
                                    editor.apply();
                                    editor.commit();

                                File folder = new File(Environment.getExternalStorageDirectory().toString(), "IDO/");
                                pdfFile = new File(folder,"Verificar Matricula: "+identidad+".pdf");
                                if (pdfFile.exists()==true){
                                    viewPDF(pdfFile);
                                }else{
                                    Log.i("LOGIN",""+cp);
                                    if (cp==1){
                                        obtenerDatosGrupos(ident,token_mas_token_type);
                                    }else{
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            /**Lanza a la actividad principal cuando el logueo es correcto**/
                                            Bundle identidadI = new Bundle();
                                            identidadI.putString("identidad", identidad);
                                            Intent mainActivity = new Intent(Login.this, MainActivity.class);
                                            mainActivity.putExtras(identidadI);
                                            mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(mainActivity, ActivityOptions.makeSceneTransitionAnimation(Login.this).toBundle());
                                            finish();
                                        }else{
                                            /**Lanza a la actividad principal cuando el logueo es correcto**/
                                            Bundle identidadI = new Bundle();
                                            identidadI.putString("identidad", identidad);
                                            Intent mainActivity = new Intent(Login.this, MainActivity.class);
                                            mainActivity.putExtras(identidadI);
                                            mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            finish();
                                            startActivity(mainActivity);
                                        }
                                    }

                                }


                            }
                        }catch (Exception exc){
                            btn_login.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_login.setBackground(getResources().getDrawable(R.drawable.background_buttons2));
                loading.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);

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
        }).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        }));
    }


    private void obtenerDatosGrupos(final String identidad_Alumno, final String token) {

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, URL_Datos_Grupos+identidad_Alumno,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("filaGrupos");

                            for (int i = 0; i < jsonArray.length(); i++) {


                                    SQLiteDatabase sqLiteDatabaseEscribir = db.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.CONTADOR_GRUPOS,"1");
                                    sqLiteDatabaseEscribir.update(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.TABLE_NAME,values,
                                            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD+"=?",new String[]{identidad_Alumno});
                                    sqLiteDatabaseEscribir.close();
                                    Log.i("LOGIN","entra");

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SQLiteDatabase sqLiteDatabaseEscribir2 = db.getWritableDatabase();
                                ContentValues values2 = new ContentValues();
                                values2.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRADO,jsonObject.getString("Grado"));
                                values2.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRUPO,jsonObject.getString("Grupo"));
                                values2.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODALIDAD,jsonObject.getString("Nombre_Modalidad"));
                                values2.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODULO,jsonObject.getString("Nombre_Modulo"));
                                values2.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.JORNADA,jsonObject.getString("Jornada"));
                                sqLiteDatabaseEscribir2.update(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.TABLE_NAME,values,
                                        BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD+"=?",new String[]{identidad_Alumno});
                                sqLiteDatabaseEscribir.close();
                                Log.i("LOGIN","entra");

                            }

                                SQLiteDatabase sqLiteDatabaseEscribir = db.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.FECHA_HORA,response.getString("created_at").substring(0,16));
                                sqLiteDatabaseEscribir.update(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.TABLE_NAME,values,
                                        BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD+"=?",new String[]{identidad_Alumno});
                                sqLiteDatabaseEscribir.close();


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                File folder = new File(Environment.getExternalStorageDirectory().toString(), "IDO/");
                                pdfFile = new File(folder,"Verificar Matricula: "+identidad+".pdf");
                                if (pdfFile.exists()==true){
                                    viewPDF(pdfFile);
                                }else{
                                    SQLiteDatabase dbLeer = db.getReadableDatabase();
                                    Cursor cursor = dbLeer.query(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.TABLE_NAME,PROJECTION,
                                            null,null,
                                            null,
                                            null,
                                            null);
                                    cursor.moveToFirst();

                                    String contad =sharedPreferences.getString(PDF,"");
                                    Log.i("LOGIN", contad);
                                        identidadPref = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD));
                                        nombre = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.NOMBRE));
                                        grado = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRADO));
                                        grupo = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRUPO));
                                        modalidad = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODALIDAD));
                                        modulo = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODULO));
                                        jornada = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.JORNADA));
                                        fecha = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.FECHA_HORA));
                                        Log.i("LOGIN",""+identidadPref+" "+nombre+" "+grado+" "+grupo+" "+modalidad+" "+modulo+" "+jornada+
                                                " "+fecha);
                                        pdf(identidadPref,nombre,grado, grupo, modalidad,modulo,jornada,fecha);
                                        dbLeer.close();
                                }
                            }else{
                                File folder = new File(Environment.getExternalStorageDirectory().toString(), "IDO/");
                                pdfFile = new File(folder,"Verificar Matricula: "+identidad+".pdf");
                                if (pdfFile.exists()==true){
                                    viewPDF(pdfFile);
                                }else{
                                    SQLiteDatabase dbLeer = db.getReadableDatabase();
                                    Cursor cursor = dbLeer.query(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.TABLE_NAME,PROJECTION,
                                            null,null,null,null,
                                            null);
                                    cursor.moveToFirst();

                                            identidadPref = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD));
                                            nombre = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.NOMBRE));
                                            grado = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRADO));
                                            grupo = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRUPO));
                                            modalidad = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODALIDAD));
                                            modulo = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODULO));
                                            jornada = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.JORNADA));
                                            fecha = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.FECHA_HORA));

                                            pdf(identidadPref,nombre,grado, grupo, modalidad,modulo,jornada,fecha);
                                            dbLeer.close();

                                }
                            }

                        } catch (Exception exc) {
                           exc.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof AuthFailureError) {
                    error.printStackTrace();
                } else if (error instanceof NetworkError) {
                    error.printStackTrace();
                } else if (error instanceof TimeoutError) {
                    error.printStackTrace();
                } else if (error instanceof ServerError) {
                    error.printStackTrace();
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

    private void pdf(String identidad_Alumno, String nombre, String grado, String grupo, String modalidad,
                     String modulo, String jornada, String fecha){
        try{
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        101);
            } else {
                new crearPDF().crear(identidad_Alumno,nombre,grado,grupo,modalidad,modulo,jornada,fecha,Login.this);
                finish();
            }
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    public  void viewPDF(File pdfFile){

        try{
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        101);
            } else {
                Intent intent = new Intent(Login.this, ViewPdf.class);
                intent.putExtra("path",pdfFile.getAbsolutePath());
                intent.putExtra("file",pdfFile.toString());
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
                finish();
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
                    SQLiteDatabase dbLeer = db.getReadableDatabase();
                    Cursor cursor = dbLeer.query(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.TABLE_NAME,PROJECTION,
                            null,null,
                            null,
                            null,
                            null);
                    cursor.moveToFirst();
                    identidadPref = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD));
                    nombre = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.NOMBRE));
                    grado = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRADO));
                    grupo = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRUPO));
                    modalidad = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODALIDAD));
                    modulo = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODULO));
                    jornada = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.JORNADA));
                    fecha = cursor.getString(cursor.getColumnIndex(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.FECHA_HORA));
                    new crearPDF().crear(identidadPref,nombre,grado, grupo, modalidad,modulo,jornada,fecha,Login.this);
                }
                break;

            case 102:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Login.this, ViewPdf.class);
                    intent.putExtra("path",pdfFile.getAbsolutePath());
                    intent.putExtra("file",pdfFile.toString());
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }
}
