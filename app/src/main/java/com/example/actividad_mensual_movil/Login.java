package com.example.actividad_mensual_movil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.DialogInterface;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {
    private TextView txt_registro, txt_email, txt_password;
    private ProgressBar prg_bar_log;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_registro = findViewById(R.id.txt_registro);
        txt_password = findViewById(R.id.txt_password);
        txt_email = findViewById(R.id.txt_email);
        btn_login = findViewById(R.id.btn_login);
        prg_bar_log = findViewById(R.id.prg_bar_log);
    }

    public void ValData(View view) {
        String Password = txt_password.getText().toString();
        String Email = txt_email.getText().toString();
        if (Email.length() == 0) {
            txt_email.setError("Ingresa una email");
        } else {
            if (Password.length() == 0) {
                txt_password.setError("Ingresa una contraseña");
            } else {
                ConsultaBD_(Password, Email);
                Configuracion();
            }
        }
    }


    //Revisar esta parte----
    public void ConsultaBD_(String Password, String Email) {
        String url = "https://actividadm.proyectoarp.com/Metodos/Acceso.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        prg_bar_log.setVisibility(View.INVISIBLE);
                        btn_login.setEnabled(true);
                        txt_email.setText("");
                        txt_password.setText("");
                        txt_password.setEnabled(true);
                        txt_email.setEnabled(true);
                        String usser_find = "";
                        String estado_find = "";
                        String email_find = "";
                        if (!response.equals("null")) {
                            if (response.equals("No")) {
                                Toast.makeText(Login.this, "El correo electronico no existe", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("Nan")) {
                                Toast.makeText(Login.this, "La contraseña no es correcta", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray data = new JSONArray(response);
                                    for (int x = 0; x < data.length(); x++) {//Recorrer el JSON
                                        JSONObject values = data.getJSONObject(x);
                                        usser_find = values.getString("Name_usser");
                                        estado_find = values.getString("Estado");
                                        email_find = values.getString("Email");
                                    }
                                    String Name_Usser = usser_find;
                                    String Estado_usser = estado_find;
                                    String Email_usser = email_find;
                                    int Status = Integer.parseInt(Estado_usser);

                                    if (Status == 1) {//ENVIO A LA VISTA LOGED [CON PROCESO]
                                        Intent In_C = new Intent(getApplicationContext(), LogedCP.class);
                                        In_C.putExtra("usser", Name_Usser);
                                        In_C.putExtra("email", Email_usser);
                                        startActivity(In_C);
                                    } else { //ENVIO A LA VISTA LOGED [SIN PROCESO]
                                        Intent In_ = new Intent(getApplicationContext(), Loged.class);
                                        In_.putExtra("usser", Name_Usser);
                                        startActivity(In_);
                                    }
                                } catch (Exception e) {
                                    //error en la extraccion de los datos del Json
                                }
                            }
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
                parametros.put("Password", Password);
                parametros.put("Email", Email);
                return parametros;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    public void Registro(View view) {
        Intent In = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(In);
    }

    public void Configuracion() {
        btn_login.setEnabled(false);
        txt_password.setEnabled(false);
        txt_email.setEnabled(false);
        prg_bar_log.setVisibility(View.VISIBLE);
    }

    public void ErrorConexion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
                        prg_bar_log.setVisibility(View.INVISIBLE);
                        btn_login.setEnabled(true);
                        txt_password.setEnabled(true);
                        txt_email.setEnabled(true);
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void Reconexion() {
        String Password = txt_password.getText().toString();
        String Email = txt_email.getText().toString();
        if (Email.length() == 0) {
            txt_email.setError("Ingresa una email");
        } else {
            if (Password.length() == 0) {
                txt_password.setError("Ingresa una contraseña");
            } else {
                ConsultaBD_(Password, Email);
                Configuracion();
            }
        }
    }
}
