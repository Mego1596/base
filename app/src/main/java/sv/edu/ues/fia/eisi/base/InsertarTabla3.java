package sv.edu.ues.fia.eisi.base;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class InsertarTabla3 extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{
    EditText carnet;
    EditText codigo;
    EditText ciclo;
    EditText notaFinal;
    RequestQueue request;
    JsonRequest jsonObjectRequest;
    ControlBDHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_tabla3);
        getSupportActionBar().hide();
        helper = new ControlBDHelper(this);
        carnet= (EditText) findViewById(R.id.editCarnetNota);
        codigo= (EditText) findViewById(R.id.editCodigoMateriaNota);
        ciclo= (EditText) findViewById(R.id.editCiclo);
        notaFinal= (EditText) findViewById(R.id.editNotaFinal);

        request = Volley.newRequestQueue(getApplicationContext());
    }

    public void cargarWebServiceLocal(View v){
        if(carnet.getText().toString().isEmpty() || codigo.getText().toString().isEmpty() || ciclo.getText().toString().isEmpty() || notaFinal.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos ",Toast.LENGTH_LONG).show();
        }else{
            String url ="http://192.168.1.113/ws_insertar_nota.php?codmateria="+ codigo.getText().toString()+
                    "&carnet="+carnet.getText().toString()+
                    "&ciclo="+ciclo.getText().toString()+
                    "&notafinal="+notaFinal.getText().toString();
            url=url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,  this,this);
            request.add(jsonObjectRequest);
        }
    }
    public void cargarWebServiceExterno(View v){
        if(carnet.getText().toString().isEmpty() || codigo.getText().toString().isEmpty() || ciclo.getText().toString().isEmpty() || notaFinal.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos ",Toast.LENGTH_LONG).show();
        }else{
            String url ="https://go14002.000webhostapp.com//ws_insertar_nota.php?codmateria="+ codigo.getText().toString()+
                    "&carnet="+carnet.getText().toString()+
                    "&ciclo="+ciclo.getText().toString()+
                    "&notafinal="+notaFinal.getText().toString();
            url=url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, this,this);
            request.add(jsonObjectRequest);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"No se puedo registrar, asegurate de llenar todos los campos y/o no introducir un usuario ya registrado", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(),"Se ha registrado exitosamente",Toast.LENGTH_LONG).show();
        ciclo.setText("");
        notaFinal.setText("");
        codigo.setText("");
        carnet.setText("");

    }
}
