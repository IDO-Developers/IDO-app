package com.matricula.ido;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private RelativeLayout rellay1, rellay2, rellay3;
    private Button btn_login, btn_restaurar_contrasenia;
    private EditText contrasenia, correo;
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



    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        rellay3 = findViewById(R.id.rellay3);
        tituloapp = findViewById(R.id.tituloApp);
        btn_restaurar_contrasenia = findViewById(R.id.button_restaurar_contrasenia);
        handler.postDelayed(runnable, 800); //800 es el tiempo de espera
        btn_login = findViewById(R.id.button_login);
        correo = findViewById(R.id.correo_login_ET);
        correo_restaurar = findViewById(R.id.correo_restaurar);
        contrasenia = findViewById(R.id.contrasenia_ET);
        loading = findViewById(R.id.loading);
        restaurar = findViewById(R.id.boton_restaurar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(Login.this).toBundle();
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Apply activity transition
                    Intent mainActivity = new Intent(Login.this, MainActivity.class);
                    startActivity(mainActivity, ActivityOptions.makeSceneTransitionAnimation(Login.this).toBundle());
                } else {
                    // Swap without transition
                    Intent mainActivity = new Intent(Login.this, MainActivity.class);
                    startActivity(mainActivity);
                }

            }
        });
    }
}
