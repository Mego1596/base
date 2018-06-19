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

public class ActualizarTabla2 extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText carnet;
    EditText apellido;
    EditText nombre;
    RequestQueue request;
    JsonRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_tabla2);
        carnet = (EditText) findViewById(R.id.upCarnet);
        nombre= (EditText) findViewById(R.id.upNombre);
        apellido = (EditText) findViewById(R.id.upApellido);
        request= Volley.newRequestQueue(getApplicationContext());
    }

    public void cargarWebServiceLocal(View v){
        if(carnet.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() || apellido.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos ",Toast.LENGTH_LONG).show();
        }else{
            String url ="http://192.168.1.113/ws_actualizar_alumno.php?nombre="+nombre.getText().toString()+
                    "&apellido="+ apellido.getText().toString()+
                    "&carnet="+ carnet.getText().toString();
            url=url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }
    }
    public void cargarWebServiceExterno(View v){
        if(carnet.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() || apellido.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos ",Toast.LENGTH_LONG).show();
        }else{
            String url ="https://go14002.000webhostapp.com//ws_actualizar_alumno.php?nombre="+ nombre.getText().toString()+
                    "&apellido="+ apellido.getText().toString()+
                    "&carnet="+ carnet.getText().toString();
            url=url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
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
        apellido.setText("");
        nombre.setText("");
        carnet.setText("");

    }
}
