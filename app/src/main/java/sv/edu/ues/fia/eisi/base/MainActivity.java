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

            case R.id.btn_insertar_materia:
                i = new Intent(this, InsertarTabla1.class);
                break;
            case R.id.btn_eliminar_materia:
                i = new Intent(this,EliminarTabla1.class);
                break;
            case R.id.btn_actualizar_materia:
                i = new Intent(this,ActualizarTabla1.class);
                break;
            case R.id.btn_consultar_materia:
                i = new Intent(this,ConsultarTabla1.class);
                break;
            case R.id.btn_insertar_alumno:
                i = new Intent(this, InsertarTabla2.class);
                break;
            case R.id.btn_eliminar_alumno:
                i = new Intent(this,EliminarTabla2.class);
                break;
            case R.id.btn_actualizar_alumno:
                i = new Intent(this,ActualizarTabla2.class);
                break;
            case R.id.btn_consultar_alumno:
                i = new Intent(this,ConsultarTabla2.class);
                break;
            case R.id.btn_insertar_nota:
                i = new Intent(this, InsertarTabla3.class);
                break;
            case R.id.btn_eliminar_nota:
                i = new Intent(this,EliminarTabla3.class);
                break;
            case R.id.btn_actualizar_nota:
                i = new Intent(this,ActualizarTabla3.class);
                break;
            case R.id.btn_consultar_nota:
                i = new Intent(this,ConsultarTabla3.class);
                break;
        }

        if(i!=null)
            startActivity(i);
    }
}
