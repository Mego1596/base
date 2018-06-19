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
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ActualizarTabla3 extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    EditText materia;
    EditText carnet;
    EditText ciclo;
    EditText notaUp;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_tabla3);
        materia= (EditText) findViewById(R.id.updateMateria);
        carnet= (EditText) findViewById(R.id.updateCarnet);
        ciclo= (EditText) findViewById(R.id.updateCiclo);
        notaUp= (EditText) findViewById(R.id.updateNota);
        request = Volley.newRequestQueue(getApplicationContext());

    }
    public void cargarWebServiceLocal(View v){
        if(carnet.getText().toString().isEmpty() || materia.getText().toString().isEmpty() || ciclo.getText().toString().isEmpty() || notaUp.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos ",Toast.LENGTH_LONG).show();
        }else{
            String url ="http://192.168.1.113/ws_actualizar_nota.php?notafinal="+notaUp.getText().toString()+
                    "&codmateria="+ materia.getText().toString()+
                    "&carnet="+carnet.getText().toString()+
                    "&ciclo="+ciclo.getText().toString();
            url=url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,  this,this);
            System.out.println("JSONREQUEST:"+jsonObjectRequest.toString());
            request.add(jsonObjectRequest);
        }
    }
    public void cargarWebServiceExterno(View v){
        if(carnet.getText().toString().isEmpty() || materia.getText().toString().isEmpty() || ciclo.getText().toString().isEmpty() || notaUp.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos ",Toast.LENGTH_LONG).show();
        }else{
            String url ="https://go14002.000webhostapp.com//ws_actualizar_nota.php?notafinal="+notaUp.getText().toString()+
                    "&codmateria="+ materia.getText().toString()+
                    "&carnet="+carnet.getText().toString()+
                    "&ciclo="+ciclo.getText().toString();
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
        notaUp.setText("");
        materia.setText("");
        carnet.setText("");

    }
}
