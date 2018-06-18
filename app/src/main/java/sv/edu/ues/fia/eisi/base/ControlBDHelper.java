package sv.edu.ues.fia.eisi.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ControlBDHelper {

    private final Context context;
    public DatabaseHelper DBHelper;
    public SQLiteDatabase db;
    private static final String DROP_TABLE1 = "DROP TABLE IF EXISTS materia; ";
    private static final String DROP_TABLE2 = "DROP TABLE IF EXISTS alumno; ";
    private static final String DROP_TABLE3 = "DROP TABLE IF EXISTS nota; ";

    public ControlBDHelper(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String BASE_DATOS = "alumno.s3db";
        private static final int VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, BASE_DATOS, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
            /*AQUI SE CREA LA BASE DE DATOS EN SQLITE*/
                db.execSQL("CREATE TABLE alumno(carnet VARCHAR(7) NOT NULL PRIMARY KEY," +
                                                "nombre VARCHAR(30)," +
                                                "apellido VARCHAR(30)," +
                                                "sexo VARCHAR(1)," +
                                                "matganadas INTEGER);");

                db.execSQL("CREATE TABLE materia(codmateria VARCHAR(6) NOT NULL PRIMARY KEY," +
                                                "nommateria VARCHAR(30)," +
                                                "unidadesval VARCHAR(1));");

                db.execSQL("CREATE TABLE nota(carnet VARCHAR(7) NOT NULL ," +
                                              "codmateria VARCHAR(6) NOT NULL," +
                                              "ciclo VARCHAR(5)," +
                                              "notafinal REAL ," +
                                              "PRIMARY KEY(carnet,codmateria,ciclo));");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE1);
                db.execSQL(DROP_TABLE2);
                db.execSQL(DROP_TABLE3);
                onCreate(db);
            } catch (Exception e) {
            }
        }
    }

    public void abrir() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return;
    }

    public void cerrar() {
        DBHelper.close();
    }

    protected boolean verificarIntegridad(Object dato, int relacion) throws SQLException {
        switch (relacion) {
            case 1:
                Nota nota= (Nota) dato;
                String[] id1= {nota.getCarnet(),nota.getCodigo()};
                abrir();
                Cursor cursor1 = db.query("nota",null,"codmateria = ? AND carnet = ?",id1,null,null,null,null);
                if (cursor1.moveToFirst()){
                    return true;
                }else {
                    return false;
                }
            default:
                return false;
        }
    }


    public String insertar(Nota notaFinal) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista nota
        if (verificarIntegridad(notaFinal, 1)) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            ContentValues eva1 = new ContentValues();
            eva1.put("codmateria",notaFinal.getCodigo());
            eva1.put("carnet",notaFinal.getCarnet());
            eva1.put("ciclo",notaFinal.getCiclo());
            eva1.put("notaFinal", notaFinal.getNotaFinal());
            contador = db.insert("nota", null, eva1);
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }
}
/*
    public Cursor ConsultarUsuPas(String user, String pass) throws SQLException {
        Cursor cursor = null;
        cursor = DBHelper.getReadableDatabase().query("usuario", new String[]{"nomusuario", "clave", "isadmin", "isdocente", "isestudiante"}, "nomusuario='" + user + "'" + " AND clave='" + pass + "'", null, null, null, null);
        return cursor;
    }

    public Cursor ConsultarUsuPasAdmin(String user, String pass) throws SQLException {
        Cursor cursor = null;
        cursor = DBHelper.getReadableDatabase().query("usuario", new String[]{"nomusuario", "clave", "isadmin", "isdocente", "isestudiante"}, "nomusuario='" + user + "'" + "AND clave='" + pass + "'" + "AND isadmin= 1", null, null, null, null);
        return cursor;
    }

    public Cursor ConsultarUsuPasDocente(String user, String pass) throws SQLException {
        Cursor cursor = null;
        cursor = DBHelper.getReadableDatabase().query("usuario", new String[]{"nomusuario", "clave", "isadmin", "isdocente", "isestudiante"}, "nomusuario='" + user + "'" + "AND clave='" + pass + "'" + "AND isdocente= 1", null, null, null, null);
        return cursor;
    }

    public Cursor ConsultarUsuPasEstudiante(String user, String pass) throws SQLException {
        Cursor cursor = null;
        cursor = DBHelper.getReadableDatabase().query("usuario", new String[]{"nomusuario", "clave", "isadmin", "isdocente", "isestudiante"}, "nomusuario='" + user + "'" + "AND clave='" + pass + "'" + "AND isestudiante= 1", null, null, null, null);
        return cursor;
    }

    public Cursor consultarListaMaterias() {
        DBHelper.getReadableDatabase();
        Cursor cursor1 = null;
        cursor1 = db.rawQuery("SELECT * FROM materia", null);
        return cursor1;
    }

    public Cursor consultarListaDocentes() {
        DBHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT * FROM docente", null);
        return cursor1;
    }

    public Cursor consultarListaCiclos() {
        DBHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT * FROM ciclo", null);
        return cursor1;
    }


    public Cursor consultarListaOferta(String user) {
        DBHelper.getReadableDatabase();
        int auxid=0;
        Docente docente = new Docente();
        docente.setNomusuario(user);

        if (verificarIntegridad(docente, 5)) {
            Cursor cursorId = db.rawQuery("SELECT iddocente FROM docente WHERE nomusuario='" + docente.getNomusuario() + "'", null);
            if (cursorId.moveToFirst()) {
                auxid = cursorId.getInt(0);

            }
        }
        Cursor cursor1 = null;
        cursor1 = db.rawQuery("SELECT * FROM ofertaAcademica WHERE iddocente="+auxid, null);
        return cursor1;
    }

    public Cursor consultarListaRespuesta(int idcuestionario) {
        DBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT carnet,nota FROM respuesta WHERE idcuestionario="+idcuestionario+" AND nota IS NOT NULL",null);
        return cursor;
    }

    public Cursor consultarListaOfertaMateria(int idmateria) {
        DBHelper.getReadableDatabase();

        Cursor cursor1 = null;
        cursor1 = db.rawQuery("SELECT idoferta, descripcion FROM ofertaAcademica WHERE idmateria="+idmateria, null);
        return cursor1;
    }
    public Cursor consultarListaCuestionario(int oferta) {
        System.out.println("ESTE ES EL PARAMETRO");
        System.out.println(oferta);
        Cursor cursor1 = null;
        cursor1 = db.rawQuery("SELECT idcuestionario, descricuestionario,ponderacion FROM cuestionario WHERE idoferta="+oferta, null);
        return cursor1;
    }

    public Cursor consultarListaAreas() {
        DBHelper.getReadableDatabase();
        Cursor cursor1 = null;
        cursor1 = db.rawQuery("SELECT * FROM areaEvaluacion", null);
        return cursor1;

    }


    public Cursor consultarListaPregunta(int idarea) {
        DBHelper.getReadableDatabase();
        Cursor cursor=null;
        cursor = db.rawQuery("SELECT descrippreg,idpregunta,tipopreg FROM pregunta WHERE idarea= "+idarea,null);
        return cursor;
    }

    public Cursor consultarListaOpciones(int id) {
        DBHelper.getReadableDatabase();
        Cursor cursor1 = null;
        cursor1 = db.rawQuery("SELECT idopcion,descripopc,idpregunta FROM opcion WHERE idpregunta="+id, null);
        return cursor1;
    }





    public String insertar(Docente docente) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista docente
        if (verificarIntegridad(docente, 5)) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            ContentValues docen = new ContentValues();
            docen.put("nombredocente", docente.getNombdocente());
            docen.put("apelldocente", docente.getApelldocente());
            docen.put("nomusuario", docente.getNomusuario());
            docen.put("correo", docente.getCorreo());
            docen.put("direccion", docente.getDireccion());
            contador = db.insert("docente", null, docen);
            regInsertados = regInsertados + contador;
        }
        if (contador == -1 || contador == 0) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }


    public String eliminar(Docente docente) {
        String regAfectados = "filas afectadas= ";
        int contador = 0;
        int auxid = 0;
        if (verificarIntegridad(docente, 5)) {
            Cursor cursorId = db.rawQuery("SELECT iddocente FROM docente WHERE nomusuario='" + docente.getNomusuario() + "'", null);
            if (cursorId.moveToFirst()) {
                System.out.println(auxid);
                auxid = cursorId.getInt(0);
                System.out.println(auxid);
            }
            db.delete("ofertaAcademica", "iddocente=" + auxid, null);
            db.delete("usuario", "nomusuario='" + docente.getNomusuario() + "'", null);
            db.delete("docente", "nomusuario='" + docente.getNomusuario() + "'", null);
            regAfectados += contador;
        } else {
            return "Registro con Usuario " + docente.getNomusuario() + " no existe";
        }
        return regAfectados;
    }

    public Docente consultarDocente(String usuario) {
        String[] id = {usuario};
        Cursor cursor = db.rawQuery("SELECT * FROM docente WHERE nomusuario='"+usuario+"'",null);
        if (cursor.moveToFirst()) {
            Docente profe = new Docente();
            System.out.println(cursor.getString(0));
            System.out.println(cursor.getString(1));
            System.out.println(cursor.getString(2));
            System.out.println(cursor.getString(3));
            System.out.println(cursor.getString(4));
            System.out.println(cursor.getString(5));
            profe.setNombdocente(cursor.getString(1));
            profe.setApelldocente(cursor.getString(2));
            profe.setCorreo(cursor.getString(3));
            profe.setDireccion(cursor.getString(4));
            return profe;
        } else {
            return null;
        }
    }

    public String actualizar(Docente docente) {
        //Si existe
        if (verificarIntegridad(docente, 5)) {
            String[] id = {docente.getNomusuario()};
            ContentValues cv = new ContentValues();
            cv.put("nombredocente", docente.getNombdocente());
            cv.put("apelldocente", docente.getApelldocente());
            cv.put("correo", docente.getCorreo());
            cv.put("direccion", docente.getDireccion());
            System.out.println(docente.getNomusuario());
            db.update("docente", cv, "nomusuario = ?", id);
            return "Registro Actualizado Correctamente";
        } else {
            return "Registro con Usuario " + docente.getNomusuario() + " no existe";
        }
    }




















    public String insertar(Estudiante estudiante) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista docente
        if (verificarIntegridad(estudiante, 3)) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            ContentValues estudiantes = new ContentValues();
            estudiantes.put("carnet", estudiante.getCarnet());
            estudiantes.put("nombreestu", estudiante.getNombreestu());
            estudiantes.put("apellidoestu", estudiante.getApellidoestu());
            estudiantes.put("correoestu", estudiante.getCorreoestu());
            estudiantes.put("direccionestu", estudiante.getDireccionestu());
            estudiantes.put("nomusuario", estudiante.getNomusuario());
            contador = db.insert("estudiante", null, estudiantes);
            regInsertados = regInsertados + contador;
        }
        if (contador == -1 || contador == 0) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }

    public String eliminar(Estudiante estudiante) {
        String regAfectados = "filas afectadas= ";
        int contador = 0;
        if (verificarIntegridad(estudiante, 3)) {
            db.delete("detalleEstudiante", "carnet='" + estudiante.getCarnet() + "'", null);
            db.delete("respuesta", "carnet='" + estudiante.getCarnet() + "'", null);
            db.delete("usuario", "nomusuario='" + estudiante.getCarnet() + "'", null);
            contador += db.delete("estudiante", "carnet='" + estudiante.getCarnet() + "'", null);
            regAfectados += contador;
        } else {
            return "Registro con carnet " + estudiante.getCarnet() + " no existe";
        }
        return regAfectados;
    }

    public Estudiante consultarEstudiante(String carnet) {
        String[] id = {carnet};
        Cursor cursor = db.query("estudiante", camposEstudiante, "carnet = ?", id, null, null, null);
        if (cursor.moveToFirst()) {
            Estudiante est1 = new Estudiante();
            est1.setCarnet(cursor.getString(0));
            est1.setNombreestu(cursor.getString(1));
            est1.setApellidoestu(cursor.getString(2));
            est1.setCorreoestu(cursor.getString(3));
            est1.setDireccionestu(cursor.getString(4));
            return est1;
        } else {
            return null;
        }
    }

    public String actualizar(Estudiante estudiante) {
        //Si existe
        if (verificarIntegridad(estudiante, 3)) {
            String[] id = {estudiante.getCarnet()};
            ContentValues cv = new ContentValues();
            cv.put("carnet", estudiante.getCarnet());
            cv.put("nombreestu", estudiante.getNombreestu());
            cv.put("apellidoestu", estudiante.getApellidoestu());
            cv.put("correoestu", estudiante.getCorreoestu());
            cv.put("direccionestu", estudiante.getDireccionestu());
            cv.put("nomusuario", estudiante.getNomusuario());
            db.update("estudiante", cv, "carnet = ?", id);
            return "Registro Actualizado Correctamente";
        } else {
            return "Registro con Usuario " + estudiante.getNombreestu() + " no existe";
        }
    }


    public String insertar(Materia materia) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista usuario
        if (verificarIntegridad(materia, 1)) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            ContentValues mat1 = new ContentValues();
            mat1.put("codmateria", materia.getCodmateria());
            mat1.put("unidadval", materia.getUnidadval());
            mat1.put("nombremat", materia.getNombremat());
            contador = db.insert("materia", null, mat1);
            regInsertados = regInsertados + contador;
        }
        if (contador == -1 || contador == 0) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";


        } else {
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }

    public String eliminar(Materia materia) {
        String regAfectados = "filas afectadas= ";
        int contador = 0;
        int auxid = 0;
        if (verificarIntegridad(materia, 1)) {
            Cursor cursorId = db.rawQuery("SELECT idmateria FROM materia WHERE codmateria='" + materia.getCodmateria() + "'", null);
            if (cursorId.moveToFirst()) {
                auxid = cursorId.getInt(0);
            }
            db.delete("ofertaAcademica", "idmateria=" + auxid, null);
            db.delete("materia", "codmateria='" + materia.getCodmateria() + "'", null);
            regAfectados += contador;
        } else {
            return "Registro con Usuario " + materia.getCodmateria() + " no existe";
        }
        return regAfectados;
    }

    public Materia consultarMateria(String materia) {
        String[] id = {materia};
        Cursor cursor = db.query("materia", camposMateria, "codmateria = ?", id, null, null, null);
        if (cursor.moveToFirst()) {
            Materia est1 = new Materia();
            est1.setCodmateria(cursor.getString(0));
            est1.setUnidadval(cursor.getInt(1));
            est1.setNombremat(cursor.getString(2));
            return est1;
        } else {
            return null;
        }
    }

    public String actualizar(Materia materia) {
        //Si existe
        if (verificarIntegridad(materia, 1)) {
            String[] id = {materia.getCodmateria()};
            ContentValues cv = new ContentValues();
            cv.put("unidadval", materia.getUnidadval());
            cv.put("nombremat", materia.getNombremat());
            db.update("materia", cv, "codmateria = ?", id);
            return "Registro Actualizado Correctamente";
        } else {
            return "Registro con Usuario " + materia.getCodmateria() + " no existe";
        }
    }


    public String insertar(Ciclo ciclo) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista docente
        if (verificarIntegridad(ciclo, 2)) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            ContentValues cic = new ContentValues();
            cic.put("numciclo", ciclo.getNumCiclo());
            cic.put("aniociclo", ciclo.getAnioCiclo());
            cic.put("fechaini", ciclo.getFechaIni());
            cic.put("fechafin", ciclo.getFechaFin());
            contador = db.insert("ciclo", null, cic);
            regInsertados = regInsertados + contador;
        }
        if (contador == -1 || contador == 0) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }

    public String eliminar(Ciclo ciclo) {
        String regAfectados = "filas afectadas= ";
        int contador = 0;
        int auxid = 0;
        if (verificarIntegridad(ciclo, 2)) {
            Cursor cursorId = db.rawQuery("SELECT idciclo FROM ciclo WHERE numciclo='" + ciclo.getNumCiclo() + "'" + " AND aniociclo='" + ciclo.getAnioCiclo() + "'", null);
            if (cursorId.moveToFirst()) {
                System.out.println(auxid);
                auxid = cursorId.getInt(0);
                System.out.println(auxid);
            }
            db.delete("ofertaAcademica", "idciclo=" + auxid, null);
            contador = db.delete("ciclo", "idciclo='" + auxid + "'", null);
            regAfectados += contador;
        } else {
            return "Registro con Numero de Ciclo  " + ciclo.getNumCiclo() + "y Anio de Ciclo " + ciclo.getAnioCiclo() + " no existe";
        }
        return regAfectados;
    }

    public Ciclo consultarCiclo(String numero, String anio) {
        String[] id = {numero, anio};
        Cursor cursor = db.query("ciclo", camposCiclo, "numciclo = ?  AND aniociclo = ?", id, null, null, null);
        if (cursor.moveToFirst()) {
            Ciclo ciclo = new Ciclo();
            ciclo.setNumCiclo(cursor.getInt(0));
            ciclo.setAnioCiclo(cursor.getInt(1));
            ciclo.setFechaIni(cursor.getString(2));
            ciclo.setFechaFin(cursor.getString(3));

            return ciclo;
        } else {
            return null;
        }
    }

    public String actualizar(Ciclo ciclo) {
        //Si existe
        if (verificarIntegridad(ciclo, 2)) {
            String[] id = {String.valueOf(ciclo.getNumCiclo()), String.valueOf(ciclo.getAnioCiclo())};
            ContentValues cv = new ContentValues();
            cv.put("fechaini", ciclo.getFechaIni());
            cv.put("fechafin", ciclo.getFechaFin());
            db.update("ciclo", cv, "numciclo = ? AND aniociclo = ?", id);
            return "Registro Actualizado Correctamente";
        } else {
            return "Registro con Numero de Ciclo  " + ciclo.getNumCiclo() + "y Anio de Ciclo " + ciclo.getAnioCiclo() + " no existe";
        }
    }





















    public String insertar(OfertaAcademica oferta) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // 1 Verificar integridad referencial
        if (verificarIntegridad(oferta, 7)) {
            // 2 Verificar registro duplicado
            if (verificarIntegridad(oferta, 6)) {
                regInsertados = "Error al Insertar el registro, Registro Duplicado.Verificar inserción";
            } else {
                int auxid1 = 0;
                int auxid2 = 0;
                int auxid3 = 0;
                Materia mat1 = oferta.getMater();
                Ciclo cic1 = oferta.getCic();
                Docente doc1 = oferta.getDoc();

                if(verificarIntegridad(doc1, 5)) {
                    Cursor cursorId= db.rawQuery("SELECT iddocente FROM docente WHERE  nomusuario = '"+doc1.getNomusuario()+"'",null);
                    if(cursorId.moveToFirst()){
                        System.out.println(auxid1);
                        auxid1=cursorId.getInt(0);
                        System.out.println(auxid1);
                    }
                }
                if(verificarIntegridad(cic1, 2)) {
                    Cursor cursorId = db.rawQuery("SELECT idciclo FROM ciclo WHERE numciclo="+String.valueOf(cic1.getNumCiclo())+" AND aniociclo="+String.valueOf(cic1.getAnioCiclo()),null);
                    if(cursorId.moveToFirst()){
                        System.out.println(auxid2);
                        auxid2=cursorId.getInt(0);
                        System.out.println(auxid2);
                    }
                }
                if(verificarIntegridad(mat1, 1)) {
                    Cursor cursorId= db.rawQuery("SELECT idmateria FROM materia WHERE  codmateria = '"+mat1.getCodmateria()+"'",null);
                    if(cursorId.moveToFirst()){
                        System.out.println(auxid3);
                        auxid3=cursorId.getInt(0);
                        System.out.println(auxid3);
                    }
                }
                ContentValues ofertas = new ContentValues();
                ofertas.put("idmateria", auxid3);
                ofertas.put("iddocente", auxid1);
                ofertas.put("idciclo", auxid2);
                ofertas.put("descripcion", oferta.getDescripcion());
                contador = db.insert("ofertaAcademica", null, ofertas);
            }
        } else {
            regInsertados = "Error al Insertar el registro, Registro sin referencias.Verificar inserción";
        }
        return  regInsertados+=contador;
    }






    public String insertar(AreaEvaluacion areas) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista docente
        if (verificarIntegridad(areas, 8)) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            ContentValues areaEva = new ContentValues();
            areaEva.put("tipoarea", areas.getArea());
            contador = db.insert("areaEvaluacion", null, areaEva);
            regInsertados = regInsertados + contador;
        }
        if (contador == -1 || contador == 0) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }

    public String eliminar(AreaEvaluacion areadeEvaluacion) {
        String regAfectados = "filas afectadas= ";
        int contador = 0;
        int auxid = 0;
        if (verificarIntegridad(areadeEvaluacion, 8)) {
            Cursor cursorId = db.rawQuery("SELECT idarea FROM areaEvaluacion WHERE tipoarea='" + areadeEvaluacion.getArea()+ "'", null);
            if (cursorId.moveToFirst()) {
                auxid = cursorId.getInt(0);
            }
            db.delete("pregunta", "idarea=" + auxid, null);
            db.delete("areaEvaluacion", "tipoarea='" + areadeEvaluacion.getArea() + "'", null);
            regAfectados += contador;
        } else {
            return "Registro de Area de Evaluacion " + areadeEvaluacion.getArea() + " no existe";
        }
        return regAfectados;
    }






    public String insertar(DetallePregunta detalle){
        String regInsertados="Registro Inserto N= ";
        long contador=0;
        if (verificarIntegridad(detalle, 12)) {
            // 2 Verificar registro duplicado
            if (verificarIntegridad(detalle, 13)) {
                regInsertados = "Error al Insertar el registro, Registro Duplicado.Verificar inserción";
            } else {
                ContentValues deta1 = new ContentValues();
                deta1.put("idcuestionario", detalle.getIdcuestionario());
                deta1.put("idpregunta", detalle.getIdpregunta());
                contador = db.insert("detallePregunta", null, deta1);
            }
        } else {
            regInsertados = "Error al Insertar el registro, Registro sin referencias.Verificar inserción";
        }
        return  regInsertados+=contador;

    }


    public String insertarNull(Respuesta respuesta) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista docente
        ContentValues docen = new ContentValues();
        docen.put("carnet", respuesta.getCarnet());
        docen.put("idcuestionario", respuesta.getIdcuestionario());
        contador = db.insert("respuesta", null, docen);
        regInsertados = regInsertados + contador;
        return regInsertados;
    }


    public String insertar(Respuesta respuesta) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista docente
        ContentValues docen = new ContentValues();
        docen.put("carnet", respuesta.getCarnet());
        docen.put("idcuestionario", respuesta.getIdcuestionario());
        docen.put("nota",respuesta.getNota());
        contador = db.insert("respuesta", null, docen);
        regInsertados = regInsertados + contador;
        return regInsertados;
    }

    public String insertar(Usuario us){
        String regInsertados="Registro Insertado Nº= ";
        long contador=0;
        // verificar que no exista usuario
        if(verificarIntegridad(us,4))
        {
            regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        }
        else
        {
            ContentValues us1 = new ContentValues();
            us1.put("nomusuario", us.getNomusuario());
            us1.put("clave",us.getClave());
            us1.put("isadmin",us.getAdmin());
            us1.put("isdocente", us.getDocente());
            us1.put("isestudiante", us.getEstudiante());
            contador=db.insert("usuario", null, us1);
            regInsertados=regInsertados+contador;
        }
        if(contador==-1 || contador==0)
        {
            regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";


        }
        else {
            regInsertados=regInsertados+contador;
        }
        return regInsertados;
    }



    public String insertar(Pregunta pr1) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // 1 Verificar integridad referencial
        if (verificarIntegridad(pr1, 9)) {
            // 2 Verificar registro duplicado
            if (verificarIntegridad(pr1, 10)) {
                regInsertados = "Error al Insertar el registro, Registro Duplicado.Verificar inserción";
            } else {
                ContentValues preg = new ContentValues();
                preg.put("idarea", pr1.getIdarea());
                preg.put("ponderapregunt",pr1.getPonderapregunta());
                preg.put("descrippreg", pr1.getDescrippreg());
                preg.put("tipopreg", pr1.getTipo());
                contador = db.insert("pregunta", null, preg);
                regInsertados = regInsertados + contador;
            }
        } else {
            regInsertados = "Error al Insertar el registro, Registro sin referencias.Verificar inserción";
        }
        return  regInsertados+=contador;
    }

    public String eliminar(Pregunta pregunta) {
        String regAfectados = "filas afectadas= ";
        int contador = 0;
        int auxid = 0;
        if (verificarIntegridad(pregunta, 9)) {
            System.out.println("ENTRA");
            if (verificarIntegridad(pregunta, 10)) {
                Cursor cursorId = db.rawQuery("SELECT idpregunta FROM pregunta WHERE descrippreg='" + pregunta.getDescrippreg() + "' AND idarea="+pregunta.getIdarea(), null);
                if (cursorId.moveToFirst()) {
                    auxid = cursorId.getInt(0);
                    System.out.println(auxid);
                }
                db.delete("detallePregunta", "idpregunta=" + auxid, null);
                db.delete("opcion", "idpregunta="+auxid,null);
                contador=db.delete("pregunta", "idpregunta=" +auxid , null);
                regAfectados += contador;
            } else {
                return "Pregunta: " + pregunta.getDescrippreg() + " no existe";
            }
        }
        return regAfectados;
    }



    public String actualizarPregunta(Pregunta pregunta) {
        //Si existe
        int auxid=0;

        if (verificarIntegridad(pregunta, 10)) {
            Cursor cursor1 = db.rawQuery("SELECT idpregunta FROM pregunta WHERE idarea= "+pregunta.getIdarea()+" AND descrippreg ='"+pregunta.getDescrippreg()+"'",null);
            if (cursor1.moveToFirst()){
                auxid=cursor1.getInt(0);
                System.out.println(cursor1.getInt(0));
            }
            String[] id = {String.valueOf(auxid)};
            ContentValues cv = new ContentValues();
            cv.put("descrippreg", pregunta.getDescrippreg());
            cv.put("ponderapregunt", pregunta.getPonderapregunta());
            db.update("pregunta", cv, "idpregunta = ?", id);
            return "Registro Actualizado Correctamente";
        } else {
            return "La pregunta no Existe ";
        }
    }


    public String insertar(Cuestionario cuestionario) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista docente
        if (verificarIntegridad(cuestionario, 11)) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            ContentValues eva1 = new ContentValues();
            eva1.put("idoferta", cuestionario.getIdoferta());
            eva1.put("descricuestionario",cuestionario.getDescricuestinario());
            eva1.put("fechaexamen",cuestionario.getFechacuestionario());
            eva1.put("ponderacion",cuestionario.getPonderacion());
            contador = db.insert("cuestionario", null, eva1);
            regInsertados = regInsertados + contador;
        }
        if (contador == -1 || contador == 0) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }

    public String eliminar(Cuestionario cuestionario) {
        String regAfectados = "filas afectadas= ";
        int contador = 0;
        int auxid = 0;
        db.delete("detallePregunta", "idcuestionario=" + cuestionario.getIdcuestionario(), null);
        contador=db.delete("cuestionario", "idcuestionario=" +cuestionario.getIdcuestionario() , null);
        regAfectados += contador;
        return regAfectados;
    }

    public String actualizarCuestionarios(Cuestionario cuestionario) {
        //Si existe
        ContentValues cv = new ContentValues();
        cv.put("ponderacion", cuestionario.getPonderacion());
        cv.put("fechaexamen", cuestionario.getFechacuestionario());
        db.update("cuestionario", cv, "idcuestionario = ?",new String[]{String.valueOf(cuestionario.getIdcuestionario())});
        return "Registro Actualizado Correctamente";
    }

    public String insertar(Opcion op) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista docente
        if (verificarIntegridad(op, 14)) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            ContentValues eva1 = new ContentValues();
            eva1.put("idpregunta",op.getIdpregunta());
            eva1.put("descripopc",op.getDescripcion());
            eva1.put("esCorrecta",op.getIsCorrect());
            contador = db.insert("opcion", null, eva1);
            regInsertados = regInsertados + contador;
        }
        if (contador == -1 || contador == 0) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }

    public String eliminar(Opcion opcion) {
        String regAfectados = "filas afectadas= ";
        int contador = 0;

        contador=db.delete("opcion", "idpregunta=" +opcion.getIdpregunta()+" AND descripopc='"+opcion.getDescripcion()+"'" , null);
        regAfectados += contador;
        return regAfectados;
    }

    public String insertar(DetalleEstudiante detalleEstudiante) {
        String regInsertados = "Registro Insertado Nº= ";
        long contador = 0;
        // verificar que no exista docente
        if (verificarIntegridad(detalleEstudiante, 15)) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            ContentValues eva1 = new ContentValues();
            eva1.put("idoferta",detalleEstudiante.getIdoferta());
            eva1.put("carnet",detalleEstudiante.getCarnet());
            contador = db.insert("detalleEstudiante", null, eva1);
            regInsertados = regInsertados + contador;
        }
        if (contador == -1 || contador == 0) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
        } else {
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }
*/
