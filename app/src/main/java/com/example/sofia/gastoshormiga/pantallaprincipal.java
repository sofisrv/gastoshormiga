package com.example.sofia.gastoshormiga;

import android.bluetooth.BluetoothServerSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class pantallaprincipal extends AppCompatActivity {
    ImageButton ba, bb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallaprincipal);
        getSupportActionBar().hide();
        ba = (ImageButton) findViewById(R.id.bb);
        bb = (ImageButton) findViewById(R.id.ba);
        ba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pantallaprincipal.this, agregar.class);
                startActivity(intent);
                finish();

            }
        });
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pantallaprincipal.this, mostrardatos.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
