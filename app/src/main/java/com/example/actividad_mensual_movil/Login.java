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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {
    private TextView txt_registro, txt_email, txt_password;
    private ProgressBar prg_bar_log;
    private Button btn_registrar, btn_login;

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
        if (Password.length() == 0) {
            txt_password.setError("Ingresa una contraseña");
        } else {
            if (Email.length() == 0) {
                txt_email.setError("Ingresa una email");
            } else {
                ConsultaBD_(Password, Email);
                btn_login.setEnabled(false);
                prg_bar_log.setVisibility(View.VISIBLE);
            }
        }
    }

    public void ConsultaBD_(String Password, String Email) {//REVISAR ESTO
        String url = "https://actividadm.proyectoarp.com/Metodos/Acceso.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        prg_bar_log.setVisibility(View.INVISIBLE);
                        btn_login.setEnabled(true);
                        String usser_find = "";
                        String estado_find = "";
                        if (!response.equals("null")) {
                            if (response.equals("Nan")) {
                                //presentador.Error_Password_Incorrect("La contraseña es incorrecta");
                                Toast.makeText(Login.this, "El correo electronico no existe", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray data = new JSONArray(response);
                                    for (int x = 0; x < data.length(); x++) {//Recorrer el JSON
                                        JSONObject values = data.getJSONObject(x);
                                        usser_find = values.getString("Name_usser");
                                        estado_find = values.getString("Estado");
                                    }
                                    String Name_Usser = usser_find;
                                    String Estado_usser = estado_find;
                                    //ENVIO A LA VISTA LOGED
                                    Toast.makeText(Login.this, "CORRECTO-LOGED", Toast.LENGTH_SHORT).show();
                                    Intent In_ = new Intent(getApplicationContext(), Loged.class);
                                    In_.putExtra("usser", Name_Usser);
                                    In_.putExtra("Estado", Estado_usser);
                                    startActivity(In_);
                                } catch (Exception e) {
                                    //error en la extraccion de los datos del Json
                                    //presentador.Error_array("Error en la extraccion de los datos del Json");
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

    @Override
    public void onBackPressed() {
        //En caso de querer permitir volver atrás usa esta llamada:
        super.onBackPressed();
    }
}
