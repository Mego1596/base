package sv.edu.ues.fia.eisi.base;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class ConsultarTabla3 extends Activity {
    EditText carnet;
    EditText codigo;
    RequestQueue request;
    JsonRequest jsonObjectRequest;
    ArrayList<Nota> listaNotas;
    ArrayList<String> listaNota;
    String json_string;
    ListView listView;
    Button btn1;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ControlBDHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_tabla3);
        carnet= (EditText) findViewById(R.id.queryCarnetNota);
        codigo= (EditText) findViewById(R.id.queryCodigoMateria);
        listView = (ListView) findViewById(R.id.lista);
        listaNotas = new ArrayList<>();
        btn1 = (Button) findViewById(R.id.btn_guardar);
        helper = new ControlBDHelper(this);

    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String jsonUrl;
        String JSON_String;
        int valor=0;

        public BackgroundTask( int x){
            valor=x;
        }

        public int getValor() {
            return valor;
        }

        public void setValor(int valor) {
            this.valor = valor;
        }

            @Override
            protected void onPreExecute () {
            if(valor==1) {
                jsonUrl = "http://192.168.1.113/ws_consultar_nota.php?codmateria=" + codigo.getText().toString() +
                        "&carnet=" + carnet.getText().toString();
                jsonUrl = jsonUrl.replace(" ", "%20");
            }else if(valor ==2){
                jsonUrl ="https://go14002.000webhostapp.com/ws_consultar_nota.php?codmateria="+ codigo.getText().toString()+
                        "&carnet="+carnet.getText().toString();
                jsonUrl=jsonUrl.replace(" ","%20");
            }

        }

            @Override
            protected String doInBackground (Void...voids){
            try {
                URL url = new URL(jsonUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_String = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_String + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

            @Override
            protected void onProgressUpdate (Void...values){
            super.onProgressUpdate(values);
        }

            @Override
            protected void onPostExecute (String result){
            json_string=result;
                if(json_string==null){
                    Toast.makeText(getApplicationContext(),"Error, no se pudo consultar los datos",Toast.LENGTH_LONG).show();
                }else{
                    try {
                        System.out.println("ENTRA");
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("server_response");

                        int count=0;

                        while(count  < jsonArray.length()){
                            Nota notas = new Nota();
                            JSONObject JO = jsonArray.getJSONObject(count);
                            notas.setCodigo(JO.getString("CODMATERIA").toString());
                            notas.setCarnet(JO.getString("CARNET").toString());
                            notas.setCiclo(JO.getInt("CICLO"));
                            notas.setNotaFinal(JO.getDouble("NOTAFINAL"));
                            listaNotas.add(notas);
                            count++;
                        }
                        obtenerListaNota();
                        actualizarListView();

                        btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               String regInsertados=null;
                                for (int i=0; i<listaNotas.size();i++){
                                   regInsertados=helper.insertar(listaNotas.get(i));
                               }
                                listaNotas.clear();
                                obtenerListaNota();
                                actualizarListView();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }

    }


    public void cargarWebServiceLocal(View v){
        if(carnet.getText().toString().isEmpty() || codigo.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos ",Toast.LENGTH_LONG).show();
        }else{
            new BackgroundTask(1).execute();

        }
    }
    public void cargarWebServiceExterno(View v){
        if(carnet.getText().toString().isEmpty() || codigo.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos ",Toast.LENGTH_LONG).show();
        }else{
            new BackgroundTask(2).execute();
        }
    }

    public void obtenerListaNota(){
        listaNota = new ArrayList<>();
        for (int i=0; i< listaNotas.size();i++){
            listaNota.add(listaNotas.get(i).getCarnet()+"   "+listaNotas.get(i).getCodigo()+"   "+listaNotas.get(i).getCiclo()+"    "+ listaNotas.get(i).getNotaFinal());
        }
    }

    public void actualizarListView(){
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listaNota);
        listView.setAdapter(adapter);
    }
}
