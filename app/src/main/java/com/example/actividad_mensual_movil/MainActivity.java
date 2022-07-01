package com.example.actividad_mensual_movil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView txt_usser, txt_password, txt_email, txt_registro;
    private Button btn_registrar;
    private ProgressBar prb_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_usser = findViewById(R.id.txt_usser);
        txt_password = findViewById(R.id.txt_password);
        txt_email = findViewById(R.id.txt_email);
        txt_registro = findViewById(R.id.txt_registro);
        btn_registrar = findViewById(R.id.btn_login);
        prb_register = findViewById(R.id.prb_register);

    }

    public void ValData(View view) {
        String Nombre = txt_usser.getText().toString();
        String Password = txt_password.getText().toString();
        String Email = txt_email.getText().toString();
        if (Nombre.length() == 0) {
            txt_usser.setError("Ingresa un nombre");
        } else {
            if (Password.length() == 0) {
                txt_password.setError("Ingresa una contraseña");
            } else {
                if (Email.length() == 0) {
                    txt_email.setError("Ingresa una correo");
                } else {//Se llama al metodo de insert a la web
                    ConsultaBD(Nombre, Password, Email);
                    btn_registrar.setEnabled(false);
                    prb_register.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    public void ConsultaBD(String Nombre, String Password, String Email) {
        String url = "https://actividadm.proyectoarp.com/Metodos/Registrar.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        btn_registrar.setEnabled(true);
                        prb_register.setVisibility(View.INVISIBLE);
                        if (!response.equals("null")) {
                            if (response.equals("null")) {
                                Toast.makeText(MainActivity.this, "Registro incorrecto!", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    Toast.makeText(MainActivity.this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                                    //Manda al login
//                                    TimerTask logo = new TimerTask() {
//                                        @Override
//                                        public void run() {
                                    Intent In = new Intent(getApplicationContext(), Login.class);
                                    startActivity(In);
                                    finish();
//                                        }
//                                    };
//                                    Timer tiempo = new Timer();
//                                    tiempo.schedule(logo, 2500);

                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, "Error tryCatch", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            //error no se encontro el usuario
                            //presentador.Error_usser("El usuario es incorrecto o no existe");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //presentador.Error_volley("Ocurrio un problema con la lirberia Volley");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("Name_usser", Nombre);
                parametros.put("Password", Password);
                parametros.put("Email", Email);
                return parametros;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    public void Login(View view) {
        Intent In = new Intent(getApplicationContext(), Login.class);
        startActivity(In);
    }


}

