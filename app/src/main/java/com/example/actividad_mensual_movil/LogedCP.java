package com.example.actividad_mensual_movil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

public class LogedCP extends AppCompatActivity {
    Bundle bundle;
    private TextView txt_usser_name_, txt_Meta, txt_Recorrido, txt_D1, txt_D2, txt_D3, txt_D4, txt_D5;
    private LinearLayout view_open, view_close;
    private final int DURACION_SPLASH = 2500; // 2.5 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged_cp);

        bundle = getIntent().getExtras();
        String name_ = bundle.getString("usser");
        String email_ = bundle.getString("email");
        txt_usser_name_ = (TextView) findViewById(R.id.txt_usser_name_);
        txt_usser_name_.setText(name_);
        Consulta(email_);
    }

    //Metodo del menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sesion, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.grafica) {
            Toast.makeText(this, "Graficas", Toast.LENGTH_LONG).show();
        } else if (id == R.id.sesion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LogedCP.this);
            builder.setTitle("Cerrar Sesión");
            builder.setMessage("¿Estás seguro que quieres cerrar tu sesión?")
                    .setCancelable(false)
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CerrarSesion();
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            return true;
        }
        return false;
    }


    public void Consulta(String email_) {//REVISAR ESTO
        String url = "https://actividadm.proyectoarp.com/Metodos/Info.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String usser_meta = "";
                        String usser_recorrido = "";
                        String D1 = "";
                        String D2 = "";
                        String D3 = "";
                        String D4 = "";
                        String D5 = "";
                        if (!response.equals("null")) {
                            try {
                                JSONArray data = new JSONArray(response);
                                for (int x = 0; x < data.length(); x++) {//Recorrer el JSON
                                    JSONObject values = data.getJSONObject(x);
                                    usser_meta = values.getString("meta");
                                    D1 = values.getString("day1");
                                    D2 = values.getString("day2");
                                    D3 = values.getString("day3");
                                    D4 = values.getString("day4");
                                    D5 = values.getString("day5");
                                    usser_recorrido = values.getString("recorrido");
                                }
                                String usser_meta_ = usser_meta;
                                String usser_recorrido_ = usser_recorrido;
                                //PASAR PARAMETROS A LA VISTA
                                txt_Meta = (TextView) findViewById(R.id.txt_Meta);
                                txt_Meta.setText(usser_meta_);

                                txt_Recorrido = (TextView) findViewById(R.id.txt_Recorrido);
                                txt_Recorrido.setText(usser_recorrido_);

                                txt_D1 = (TextView) findViewById(R.id.D1);
                                txt_D1.setText(D1);
                                txt_D2 = (TextView) findViewById(R.id.D2);
                                txt_D2.setText(D2);
                                txt_D3 = (TextView) findViewById(R.id.D3);
                                txt_D3.setText(D3);
                                txt_D4 = (TextView) findViewById(R.id.D4);
                                txt_D4.setText(D4);
                                txt_D5 = (TextView) findViewById(R.id.D5);
                                txt_D5.setText(D5);
                            } catch (Exception e) {
                            }
                        } else {

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
                parametros.put("Email", email_);
                return parametros;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    public void CerrarSesion() {
        //vista normal
        view_open = (LinearLayout) findViewById(R.id.view_open);
        view_open.setVisibility(View.GONE);
        //vista de cierre de sesion
        view_close = (LinearLayout) findViewById(R.id.view_close);
        view_close.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent In = new Intent(getApplicationContext(), Login.class);
                startActivity(In);
                finish();
            }
        }, DURACION_SPLASH);
    }


}