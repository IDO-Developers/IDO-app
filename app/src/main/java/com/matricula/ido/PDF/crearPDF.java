package com.matricula.ido.PDF;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.itextpdf.text.Image;
import com.matricula.ido.Login;
import com.matricula.ido.R;
import java.io.ByteArrayOutputStream;

public class crearPDF {

    private TemplatePDF templatePDF;



    public void crear(String identidad_Alumno, String nombre, String grado, String grupo, String modalidad,
                          String modulo, String jornada, Context context){

        try{
                /**Metodo para obtener datos del Alumno**/

                templatePDF = new TemplatePDF(context);
                templatePDF.openDocument(identidad_Alumno);
                try {
                    Drawable d = context.getResources().getDrawable(R.drawable.icono);
                    BitmapDrawable bitDw = ((BitmapDrawable) d);
                    Bitmap bmp = bitDw.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());

                    templatePDF.createPdf(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                templatePDF.addTitles("", "Instituto Departamental de Oriente", "Comprobante de Matricula", null);
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addTexto("Nombre del alumno:");
                templatePDF.addTexto(""+nombre);
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addTexto("Numero de Identidad:");
                templatePDF.addTexto(""+identidad_Alumno);
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                if (grado.equals("Décimo Grado")||grado.equals("Undécimo Grado")||grado.equals("Duodécimo Grado")){
                    templatePDF.addTexto("Modalidad:");
                    templatePDF.addTexto(""+modalidad);
                }else {
                    templatePDF.addTexto("Modulo:");
                    templatePDF.addTexto(""+modulo);
                }
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addTexto("Grado:");
                templatePDF.addTexto(""+grado);
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addTexto("Grupo:");
                templatePDF.addTexto(""+grupo);
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addTexto("Jornada:");
                templatePDF.addTexto(""+jornada);
                templatePDF.closeDocument();
                templatePDF.viewPDF();

        }catch (Exception exc){
            exc.printStackTrace();
        }
    }


}
