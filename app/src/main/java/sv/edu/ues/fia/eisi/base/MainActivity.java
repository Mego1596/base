package sv.edu.ues.fia.eisi.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void lanzarActividad(View v){
        Intent i = null;

        switch(v.getId()){

            case R.id.btn_insert_materia:
                i = new Intent(this, InsertarTabla1.class);
                break;
            case R.id.btn_delete_materia:
                i = new Intent(this,EliminarTabla1.class);
                break;
            case R.id.btn_update_materia:
                i = new Intent(this,ActualizarTabla1.class);
                break;
            case R.id.btn_insert_alumno:
                i = new Intent(this, InsertarTabla2.class);
                break;
            case R.id.btn_delete_alumno:
                i = new Intent(this,EliminarTabla2.class);
                break;
            case R.id.btn_update_alumno:
                i = new Intent(this,ActualizarTabla2.class);
                break;
            case R.id.btn_insert_nota:
                i = new Intent(this, InsertarTabla3.class);
                break;
            case R.id.btn_delete_nota:
                i = new Intent(this,EliminarTabla3.class);
                break;
            case R.id.btn_update_nota:
                i = new Intent(this,ActualizarTabla3.class);
                break;
        }

        if(i!=null)
            startActivity(i);
    }
}
