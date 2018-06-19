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

public class ActualizarTabla1 extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    EditText codigoMateria;
    EditText nombreMateria;
    EditText unidadesValorativas;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_tabla1);
        getSupportActionBar().hide();
        codigoMateria = (EditText) findViewById(R.id.upMateria);
        nombreMateria = (EditText) findViewById(R.id.upNombreMateria);
        unidadesValorativas = (EditText) findViewById(R.id.upUnidadesValorativas);
        request = Volley.newRequestQueue(getApplicationContext());
    }
    public void cargarWebServiceLocal(View v){
        if(codigoMateria.getText().toString().isEmpty() || nombreMateria.getText().toString().isEmpty() || unidadesValorativas.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos ",Toast.LENGTH_LONG).show();
        }else{
            String url ="http://192.168.1.113/ws_actualizar_materia.php?codmateria="+ codigoMateria.getText().toString()+
                    "&nommateria="+nombreMateria.getText().toString()+
                    "&unidadesval="+unidadesValorativas.getText().toString();
            url=url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }
    }
    public void cargarWebServiceExterno(View v){
        if(codigoMateria.getText().toString().isEmpty() || nombreMateria.getText().toString().isEmpty() || unidadesValorativas.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debes llenar todos los campos ",Toast.LENGTH_LONG).show();
        }else{
            String url ="https://go14002.000webhostapp.com//ws_actualizar_materia.php?codmateria="+ codigoMateria.getText().toString()+
                    "&nommateria="+nombreMateria.getText().toString()+
                    "&unidadesval="+unidadesValorativas.getText().toString();
            url=url.replace(" ","%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, (Response.Listener<JSONObject>) this,this);
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
        codigoMateria.setText("");
        nombreMateria.setText("");
        unidadesValorativas.setText("");

    }
}
