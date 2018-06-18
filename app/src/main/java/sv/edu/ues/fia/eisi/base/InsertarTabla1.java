package sv.edu.ues.fia.eisi.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class InsertarTabla1 extends AppCompatActivity {
    EditText editNombreestu;
    EditText editApellido;
    EditText editCorreo;
    EditText editDireccion;
    EditText editUser;
    EditText editClave;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_tabla1);


    }
}
