package com.example.actividad_mensual_movil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        TimerTask logo = new TimerTask() {
            @Override
            public void run() {
                Intent In = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(In);
                finish();
            }
        };

        Timer tiempo = new Timer();
        tiempo.schedule(logo, 5000);
    }

}