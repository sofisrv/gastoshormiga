package com.example.sofia.gastoshormiga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class agregar extends AppCompatActivity implements View.OnClickListener{
    Button br, ba;
    EditText nombre, descripcion, gasto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar);
        br = (Button) findViewById(R.id.breg);
        ba = (Button) findViewById(R.id.ba);
        nombre = (EditText) findViewById(R.id.nombre);
        descripcion = (EditText) findViewById(R.id.descripcion);
        gasto = (EditText) findViewById(R.id.gasto);
        ba.setOnClickListener(this);
        br.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(agregar.this, pantallaprincipal.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onClick(View v) {
        Thread tr= new Thread(){
            @Override
            public void run() {
                final String resultado=enviardatosGET(nombre.getText().toString() ,gasto.getText().toString(), descripcion.getText().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r1 = obtdatosJSON(resultado);
                        if (nombre.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Falta titulo del gasto a ingresar", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if (gasto.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "Falta ingresar gasto", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Intent i = new Intent(getApplicationContext(), pantallaprincipal.class);
                                Toast.makeText(getApplicationContext(), "Producto agregado", Toast.LENGTH_LONG).show();
                                i.putExtra("cod", nombre.getText().toString());
                                startActivity(i);
                            }
                        }
                    }
                });
            }
        };
        tr.start();
    }

    public String enviardatosGET(String nombre, String gasto, String descripcion) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://169.254.248.183/gastos/rgasto.php?nombre="+nombre+"&gasto="+gasto+"&descripcion="+descripcion);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            respuesta=connection.getResponseCode();
            result=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(connection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));

                while ((linea=reader.readLine())!=null){
                    result.append(linea);
                }
            }

        } catch (Exception e) {}
        return result.toString();
    }

    public int obtdatosJSON (String response){
        int res=0;
        try{
            JSONArray json=new JSONArray(response);
            if(json.length()>0){
                res=1;
            }
        }catch (Exception e){}
        return res;
    }
}
