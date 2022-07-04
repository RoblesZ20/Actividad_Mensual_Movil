package com.example.actividad_mensual_movil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
    private String CHANNEL_ID = "MiApp";


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

        //CREAR CANAL Y DEFINIR IMPORTANCIA
        createNotificationChannel();
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
                    Configuracion();
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
                        txt_usser.setText("");
                        txt_password.setText("");
                        txt_email.setText("");
                        btn_registrar.setEnabled(true);
                        prb_register.setVisibility(View.INVISIBLE);
                        if (!response.equals("null")) {
                            if (response.equals("null")) {
                                Toast.makeText(MainActivity.this, "Registro incorrecto!", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    NotificacionRegistro();
                                    Intent In = new Intent(getApplicationContext(), Login.class);
                                    startActivity(In);
                                    finish();

                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, "Error tryCatch", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            //error no se encontro el usuario
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorConexion();
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


    //NOTIFICACION
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void NotificacionRegistro() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("REGISTRO")
                .setContentText("Genial!: Se ha registrado correctamente en Kcal2022")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Es la hora de registrar tu primer recorrido en la App para dispositivos Wearable " +
                        "para comenzar a visualizar tu progreso y status de tu meta indicada, ¡empecemos!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    public void Configuracion() {
        txt_usser.setEnabled(false);
        txt_password.setEnabled(false);
        txt_email.setEnabled(false);
        btn_registrar.setEnabled(false);
        prb_register.setVisibility(View.VISIBLE);
    }

    public void ErrorConexion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Error de conexión");
        builder.setMessage("¿No se a podido iniciar sesión, ¿Desea intentar de nuevo?")
                .setCancelable(false)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//Intentar login de nuevo
                        Reconexion();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prb_register.setVisibility(View.INVISIBLE);
                        btn_registrar.setEnabled(true);
                        txt_usser.setEnabled(true);
                        txt_password.setEnabled(true);
                        txt_email.setEnabled(true);
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void Reconexion() {
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
                    Configuracion();
                }
            }
        }
    }
}

