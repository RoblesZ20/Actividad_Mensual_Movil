package com.example.actividad_mensual_movil;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

public class Loged extends AppCompatActivity {
    Bundle bundle;
    private TextView txt_usser_name, txt_usser_estado, txt_1, txt_2;
    private LinearLayout view_open, view_close;
    private Button btn_1;
    private ProgressBar pb_loaded;
    private LottieAnimationView lottie;
    private final int DURACION_SPLASH = 2500; // 2.5 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged);

        bundle = getIntent().getExtras();
        String name = bundle.getString("usser");
        String status = bundle.getString("status");
        txt_usser_name = (TextView) findViewById(R.id.txt_usser_name);
        txt_usser_name.setText(name);

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
            AlertDialog.Builder builder = new AlertDialog.Builder(Loged.this);
            builder.setTitle("Cerrar Sesión");
            builder.setMessage("¿Estás seguro que quieres cerrar tu sesión?")
                    .setCancelable(false)
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//Cerrar Sesion
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
