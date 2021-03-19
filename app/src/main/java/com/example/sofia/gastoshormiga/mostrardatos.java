package com.example.sofia.gastoshormiga;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class mostrardatos extends AppCompatActivity
{
    ArrayList nombre = new ArrayList();
    ArrayList gasto = new ArrayList();
    ArrayList fecha = new ArrayList();
    ArrayList descripcion = new ArrayList();
    String id_d = "";
    ListView listView;
    Context context = this;
    boolean connectionEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrardatos);

        listView = (ListView) findViewById(R.id.list2);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
            ActualizarServicios();
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        if(connectionEnabled)
        {
            ActualizarServicios();
        }
        else
        {
            Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
        }
    }

    public String enviarDatosGet()
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            //Se establece el url
            url = new URL("http://169.254.248.183/gastos/cgastos.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            respuesta = connection.getResponseCode();
            resul = new StringBuilder();
            if(respuesta == HttpURLConnection.HTTP_OK)
            {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while((linea = reader.readLine()) != null)
                {
                    resul.append(linea);
                }
            }
        }
        catch (Exception e)
        {

        }
        return resul.toString();
    }

    public int objDatosJSON(String response)
    {
        int res =0;
        try
        {
            JSONArray json =    new JSONArray(response);
            if(json.length() > 0)
            {
                res = 1;
            }
        }
        catch(Exception e)
        {

        }
        return res;
    }

    private void ActualizarServicios()
    {
        nombre.clear();
        gasto.clear();
        fecha.clear();
        descripcion.clear();

        final ProgressDialog dialogo = ProgressDialog.show(this, "Actualizando datos", "Por favor espera un momento...", true);
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                final String resultado = enviarDatosGet();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int r=objDatosJSON(resultado);
                        if(r > 0)
                        {
                            dialogo.dismiss();
                            try
                            {
                                JSONArray jsonarray = new JSONArray(resultado);
                                for (int i = 0; i < jsonarray.length(); i++)
                                {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    nombre.add(jsonobject.getString("nombre"));
                                    gasto.add(jsonobject.getString("gasto"));
                                    String dia = jsonobject.getString("dia");
                                    String mes = jsonobject.getString("mes");
                                    String ano = jsonobject.getString("ano");
                                    String fechaa = dia+"/"+mes+"/"+ano;
                                    fecha.add(fechaa);
                                    descripcion.add(jsonobject.getString("descripcion"));
                                }
                                listView.setAdapter(new mostrardatos.ServicioAdapter(getApplicationContext()));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Toast toast1 = Toast.makeText(getApplicationContext(), "No hay gastos registrados", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                    }
                });
            }
        };
        tr.start();
    }

    private class ServicioAdapter extends BaseAdapter
    {
        Context ctx;
        LayoutInflater layoutInflater;
        TextView name,gatito,date,descripcionit;
        ImageView icono;

        public ServicioAdapter(Context applicationContext)
        {
            this.ctx= applicationContext;
            layoutInflater = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return nombre.size();
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @RequiresApi (api= Build.VERSION_CODES.KITKAT)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            final ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.servicio,null);

            name = (TextView)viewGroup.findViewById(R.id.nombreg);
            gatito = (TextView)viewGroup.findViewById(R.id.gastog);
            date = (TextView)viewGroup.findViewById(R.id.fechag);
            descripcionit = (TextView)viewGroup.findViewById(R.id.descripciong);
            name.setText(nombre.get(position).toString());
            gatito.setText(gasto.get(position).toString());
            date.setText(fecha.get(position).toString());
            descripcionit.setText(descripcion.get(position).toString());
            return viewGroup;
        }
    }
}

