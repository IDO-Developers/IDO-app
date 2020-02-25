package com.matricula.ido.PDF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.barteksc.pdfviewer.PDFView;
import com.matricula.ido.Login;
import com.matricula.ido.MainActivity;
import com.matricula.ido.R;
import com.matricula.ido.SharedPreferences.SaveSharedPreference;
import com.matricula.ido.Utilidades;
import com.matricula.ido.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;
import static com.matricula.ido.SharedPreferences.PreferencesUtility.TOKEN_AUTH;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class ViewPdf extends AppCompatActivity {

    PDFView pdfView;
    public File file;
    private Object ruta;
    private String i;
    private Cursor cursorList;
    private JSONArray lista;
    private JSONObject json_data;
    private String emailAdmin;
    private String tokenAdministrador;
    private SharedPreferences sharedPreferences;
    private String token;
    private String URL_LOGOUT = new Utilidades().URL_LOGOUT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        token=sharedPreferences.getString(TOKEN_AUTH,"");


        pdfView=(PDFView) findViewById(R.id.pdfView);
        getSupportActionBar().setTitle("Comprobante de Matrícula");
        Bundle bundle = getIntent().getExtras();

        if(bundle !=null){
            file =new File(bundle.getString("path",""));
            ruta = bundle.getString("file");
            i= bundle.getString("i");
             Toast.makeText(this, "Archivo guardado en: "+ruta, Toast.LENGTH_LONG).show();
        }
        pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.enviaradjunto, menu);
            inflater.inflate(R.menu.cerrarsesion, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.enviarAdjunto:
                String[] mailto = {emailAdmin};
                Uri uri = Uri.fromFile(new File(String.valueOf(file)));
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"Hola PDF se adjunta en este correo. ");
                emailIntent.setType("application/pdf");
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Send email using:"));

            case R.id.cerrarSesion:
                cerrarSesion();
                break;
        }

        return super.onOptionsItemSelected(item);
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
                                Toast.makeText(ViewPdf.this, ""+jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();
                            }
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            SaveSharedPreference.setLoggedIn(ViewPdf.this,false);
                            Intent intent = new Intent(ViewPdf.this, Login.class);
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
                    Toast.makeText(ViewPdf.this, "Problema en la autenticacion", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(ViewPdf.this, "Problemas con la red", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(ViewPdf.this, "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ViewPdf.this, "Tenemos problemas, intentelo de nuevo mas tarde", Toast.LENGTH_SHORT).show();
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

}
