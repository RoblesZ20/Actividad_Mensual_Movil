package com.example.actividad_mensual_movil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Loged extends AppCompatActivity {
    Bundle bundle;
    private TextView txt_usser_name, txt_usser_estado;
    private ProgressBar pb_loaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged);

        bundle = getIntent().getExtras();
        String name = bundle.getString("usser");
        String Estado = bundle.getString("Estado");
        txt_usser_name = (TextView) findViewById(R.id.txt_usser_name);
        txt_usser_name.setText(name);


    }
}