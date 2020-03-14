package com.matricula.ido;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.matricula.ido.Base_de_Datos.BaseDeDatosInfoAlumno;
import com.matricula.ido.Grupos.Adaptador_Grupos;
import com.matricula.ido.Grupos.PojoGrupos;
import com.matricula.ido.PDF.TemplatePDF;
import com.matricula.ido.PDF.crearPDF;
import com.matricula.ido.SharedPreferences.SaveSharedPreference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static android.R.layout.simple_spinner_dropdown_item;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.matricula.ido.SharedPreferences.PreferencesUtility.TOKEN_AUTH;

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
    private String grupoNom;
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
    private SharedPreferences sharedContador;
    private String token;
    private BaseDeDatosInfoAlumno db = new BaseDeDatosInfoAlumno(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        identidadAlumno=getIntent().getExtras().getString("identidad");
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
                        contentViewTxtModalidad.setVisibility(View.GONE);
                        contentViewTxtModulo.setVisibility(View.GONE);
                        contentViewTxtJornada.setVisibility(View.GONE);
                        contentViewEditModalidad.setVisibility(View.GONE);
                        contentViewEditModulo.setVisibility(View.GONE);
                        contentViewEditJornada.setVisibility(View.GONE);
                    } else {

                        final AlertDialog.Builder alertDialo = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater= LayoutInflater.from(MainActivity.this);
                        View viewDialogo = inflater.inflate(R.layout.item_dialogo_info_grupo, null);
                        alertDialo.setTitle("Detalle de Grupo");
                        TextView numM = (TextView)viewDialogo.findViewById(R.id.nH);
                        TextView numF = (TextView)viewDialogo.findViewById(R.id.nM);

                        numM.setText(arrayListsGrupos.get(position).getNum_hombres());
                        numF.setText(arrayListsGrupos.get(position).getNum_mujeres());

                        alertDialo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                modalidad.setText(arrayListsGrupos.get(position).getModalidad());
                                jornada.setText(arrayListsGrupos.get(position).getJornada());
                                modulo.setText(arrayListsGrupos.get(position).getModulo());
                                idGrupo = String.valueOf(arrayListsGrupos.get(position).getId());
                                grupoNom = arrayListsGrupos.get(position).getGrupo();
                                crossfade();
                            }
                        });
                        alertDialo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                grupo.setSelection(0);
                            }
                        });
                        alertDialo.setView(viewDialogo);
                        alertDialo.create().show();


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
                    SQLiteDatabase dbEscribir = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRADO,grado.getText().toString());
                    values.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.GRUPO,grupoNom);
                    values.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODALIDAD,modalidad.getText().toString());
                    values.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.MODULO,modulo.getText().toString());
                    values.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.JORNADA,jornada.getText().toString());
                    values.put(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.FECHA_HORA,obtenerFecha()+" "+obtenerHora());
                    dbEscribir.update(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.TABLE_NAME,values,
                            BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD+"=?", new String[]{identidadAlumno});

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

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                pojoGrupos = new PojoGrupos();

                                pojoGrupos.setId(jsonObject.getInt("Id_Grupo"));
                                pojoGrupos.setJornada(jsonObject.getString("Jornada"));
                                pojoGrupos.setModalidad(jsonObject.getString("Nombre_Modalidad"));
                                pojoGrupos.setModulo(jsonObject.getString("Nombre_Modulo"));
                                pojoGrupos.setGrupo(jsonObject.getString("Grupo"));
                                pojoGrupos.setNum_hombres(jsonObject.getString("Num_Hombres"));
                                pojoGrupos.setNum_mujeres(jsonObject.getString("Num_Mujeres"));
                                grado.setText(jsonObject.getString("Grado"));

                                /**ArrayList con solo el grupo para mostrar en el spinner**/
                                arrayListString.add(pojoGrupos.getGrupo());

                                /**Arraylist con todos los elementos de la clase pojo**/
                                arrayListsGrupos.add(pojoGrupos);

                            }
                            //adaptador_grupos = new Adaptador_Grupos(MainActivity.this,arrayListsGrupos);
                            //adaptador_grupos.notifyDataSetChanged();
                            //grupo.setAdapter(adaptador_grupos);

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
                                       finish();
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
                            SQLiteDatabase sqliteEscribir = db.getWritableDatabase();
                            sqliteEscribir.delete(BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.TABLE_NAME,
                                    BaseDeDatosInfoAlumno.FeedReaderContract.FeedEntry.IDENTIDAD+"=?",new String[]{identidadAlumno});
                            sqliteEscribir.close();
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
       /* loadingView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        contentViewTxtModalidad.setVisibility(View.GONE);
                        contentViewTxtModulo.setVisibility(View.GONE);
                        contentViewTxtJornada.setVisibility(View.GONE);
                        contentViewEditModalidad.setVisibility(View.GONE);
                        contentViewEditModulo.setVisibility(View.GONE);
                        contentViewEditJornada.setVisibility(View.GONE);
                    }
                });*/

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
                String horaFecha = obtenerFecha()+" "+obtenerHora();
                new crearPDF().crear(rneAlumno.getText().toString(),
                        nombreAlumno.getText().toString(),
                        grado.getText().toString(),
                        grupoNom,
                        modalidad.getText().toString(),
                        modulo.getText().toString(),
                        jornada.getText().toString(),horaFecha,MainActivity.this);
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
                            jornada.getText().toString(),obtenerFecha()+" "+obtenerHora(),MainActivity.this);
                }
                break;
        }
    }

    private String obtenerHora() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String hora = dateFormat.format(date);
        return  hora;
    }

    private String obtenerFecha(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fechaForm = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = fechaForm.format(calendar.getTime());
        return fecha;
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

}
