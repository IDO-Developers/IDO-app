package com.matricula.ido.PDF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.matricula.ido.Login;
import com.matricula.ido.MainActivity;
import com.matricula.ido.PDF.ViewPdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TemplatePDF {
    private Context context;
    private  File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    int i;
    private Paragraph paragraph;
    private SharedPreferences sharedPreferences;
    private Font fTitulo = new Font(Font.FontFamily.TIMES_ROMAN,14,Font.BOLD);
    private Font fSubTitulo = new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL);
     private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN,14,Font.UNDERLINE|Font.BOLD);
    private Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD);
    private Font fTextNota = new Font(Font.FontFamily.TIMES_ROMAN,11,Font.BOLD);
    private Font fEspecificar = new Font(Font.FontFamily.TIMES_ROMAN,11,Font.NORMAL);
    private Font fSubrayada = new Font(Font.FontFamily.TIMES_ROMAN,11,Font.UNDERLINE|Font.BOLD);
    private Font fSubTitleTabla = new Font(Font.FontFamily.TIMES_ROMAN,6,Font.BOLD);
    private Font fTextTabla = new Font(Font.FontFamily.TIMES_ROMAN,1,Font.BOLD);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD );
    private Font fpequenia = new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD );

    private Font fbixText = new Font(Font.FontFamily.TIMES_ROMAN,11,Font.BOLD );

    public TemplatePDF(Context context) {
        this.context = context;
    }

    public  void openDocument(String identidad){
        createFile(identidad);
        try{
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFile));
            document.open();



        }catch (Exception e){
         }
    }
    public  void closeDocument(){
        document.close();
    }


    public void  addMetaData(String title, String subject, String author){
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);

    }


    public void createFechaID(String detalle,String detalleTex ,String id,String numeroId ) throws IOException, DocumentException {

        paragraph =new Paragraph();

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        float[] medidaCeldas = {5.9f, 20.0f ,50.0f};
        table.setWidths(medidaCeldas);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);



        PdfPCell pdfPCell = new PdfPCell(new Phrase(detalle, fTextNota));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setBackgroundColor(BaseColor.WHITE);
        pdfPCell.setBorder(Rectangle.NO_BORDER);

        table.addCell(pdfPCell );

        PdfPCell pdfPCell2 = new PdfPCell(new Phrase(detalleTex, fEspecificar));
        pdfPCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell2.setBackgroundColor(BaseColor.WHITE);
        pdfPCell2.setBorder(Rectangle.NO_BORDER);
        pdfPCell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell2 );

        PdfPTable table2 = new PdfPTable(2);
        float[] medidaCeldas2 = {30f, 50.0f};
        table2.setWidths(medidaCeldas2);
        table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table2.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell pdfPCell3 = new PdfPCell(new Phrase(id, fTextNota));
        pdfPCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell3.setBorder(Rectangle.NO_BORDER);
        pdfPCell3.setBackgroundColor(BaseColor.WHITE);


        table2.addCell(pdfPCell3 );

        PdfPCell pdfPCell4 = new PdfPCell(new Phrase(numeroId, fEspecificar));
        pdfPCell4.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell4.setBackgroundColor(BaseColor.WHITE);
        pdfPCell4.setBorder(Rectangle.NO_BORDER);
        table2.addCell(pdfPCell4 );

          table.addCell(table2);

        paragraph.add(table);

        document.add(paragraph);

    }


    public void createEspecificar(String detalle,String detalleTex) throws IOException, DocumentException {

        paragraph =new Paragraph();


        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        float[] medidaCeldas = {30.9f, 50.0f };
        table.setWidths(medidaCeldas);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);



        PdfPCell pdfPCell = new PdfPCell(new Phrase(detalle, fEspecificar));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setBackgroundColor(BaseColor.WHITE);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell );

        PdfPCell pdfPCell2 = new PdfPCell(new Phrase(detalleTex, fSubrayada));
        pdfPCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell2.setBackgroundColor(BaseColor.WHITE);
        pdfPCell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell2 );

        paragraph.add(table);

        document.add(paragraph);

    }


    public void createDepto(String detalle,String detalleTex) throws IOException, DocumentException {

        paragraph =new Paragraph();

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        float[] medidaCeldas = {8.9f, 50.0f };
        table.setWidths(medidaCeldas);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);



        PdfPCell pdfPCell = new PdfPCell(new Phrase(detalle, fTextNota));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setBackgroundColor(BaseColor.WHITE);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell );

        PdfPCell pdfPCell2 = new PdfPCell(new Phrase(detalleTex, fEspecificar));
        pdfPCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell2.setBackgroundColor(BaseColor.WHITE);
        pdfPCell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell2 );

        paragraph.add(table);

        document.add(paragraph);

    }

    public void createNota(String detalle,String detalleTex) throws IOException, DocumentException {

        paragraph =new Paragraph();

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        float[] medidaCeldas = {3.2f, 50.0f };
        table.setWidths(medidaCeldas);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);



        PdfPCell pdfPCell = new PdfPCell(new Phrase(detalle, fTextNota));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setBackgroundColor(BaseColor.WHITE);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell );

        PdfPCell pdfPCell2 = new PdfPCell(new Phrase(detalleTex, fEspecificar));
        pdfPCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell2.setBackgroundColor(BaseColor.WHITE);
        pdfPCell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell2 );

        paragraph.add(table);

        document.add(paragraph);

    }
    public void createEmpleado(String detalle,String detalleTex) throws IOException, DocumentException {

        paragraph =new Paragraph();

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        float[] medidaCeldas = {13.5f, 50.0f };
        table.setWidths(medidaCeldas);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);



        PdfPCell pdfPCell = new PdfPCell(new Phrase(detalle, fTextNota));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setBackgroundColor(BaseColor.WHITE);
        pdfPCell.setBorder(Rectangle.NO_BORDER);

        table.addCell(pdfPCell );

        PdfPCell pdfPCell2 = new PdfPCell(new Phrase(detalleTex, fEspecificar));
        pdfPCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell2.setBackgroundColor(BaseColor.WHITE);
        pdfPCell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell2 );

        paragraph.add(table);

        document.add(paragraph);

    }
    public void createDetalle(String detalle,String detalleTex) throws IOException, DocumentException {

        paragraph =new Paragraph();

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        float[] medidaCeldas = {4.5f, 50.0f };
        table.setWidths(medidaCeldas);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);



        PdfPCell pdfPCell = new PdfPCell(new Phrase(detalle, fTextNota));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setBackgroundColor(BaseColor.WHITE);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell );

        PdfPCell pdfPCell2 = new PdfPCell(new Phrase(detalleTex, fEspecificar));
        pdfPCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell2.setBackgroundColor(BaseColor.WHITE);
        pdfPCell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell2 );

        paragraph.add(table);

        document.add(paragraph);

    }

    public void createPdf(Image image) throws IOException, DocumentException {

        paragraph =new Paragraph();

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        float[] medidaCeldas = {14.50f, 18.5f, 30.5f,30.55f, };
        table.setWidths(medidaCeldas);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.addCell(image);
        table.addCell("");
        table.addCell("Instituto Departamental de Oriente");

        PdfPCell pdfPCell = new PdfPCell(new Phrase("Tel 2763-9900 ext 23300004\nHttp//www.sivce.ido.edu.hn", fSubTitle));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setBackgroundColor(BaseColor.WHITE);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(pdfPCell );

        paragraph.add(table);

        document.add(paragraph);
    }

    public void addTitles(String title,String subTitle,String date ,String control){
        paragraph = new Paragraph();
        addChildP(new Paragraph(title,fTitulo));
        addChildP(new Paragraph(subTitle,fSubTitulo));
        addChildP(new Paragraph(date,fSubTitulo));
        addChildP(new Paragraph(control,fTitle));

        try {
            document.add(paragraph);
        } catch (Exception e) {
         }
    }

    public void addSuperior(Image image,String subTitle ){
        paragraph = new Paragraph();

        superiorDerecha(new Paragraph(subTitle,fpequenia));
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {
            image.scalePercent(15);
            image.setAlignment(Element.ALIGN_LEFT);
            superiorDerecha(new Paragraph(subTitle,fpequenia));
            document.add(image);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public  void addParagraph(String text){
        try{
            paragraph = new Paragraph(text ,fTextNota);
            document.add(paragraph);
        } catch (Exception e) {
         }
    }

    public  void addEspecificar(String text){
        try{
            paragraph = new Paragraph(text ,fEspecificar);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        } catch (Exception e) {
        }

    }
    public  void addnotaParrafo(String text){
        try{
            paragraph = new Paragraph(text ,fEspecificar);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph);
        } catch (Exception e) {
        }
    }

    public  void addNota(String text){
        try{
            paragraph = new Paragraph(text ,fTextNota);
            document.add(paragraph);
        } catch (Exception e) {
        }
    }

    public  void addEspacio(String text){
        try{
            paragraph = new Paragraph(text ,fTextNota);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        } catch (Exception e) {
        }
    }

    private  void addChildP(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    private  void superiorDerecha(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.add(childParagraph);
    }

    private  void  createFile(String identidad){
        try {
            File folder = new File(Environment.getExternalStorageDirectory().toString(), "IDO");
             if (!folder.exists()) {
                folder.mkdirs();
                pdfFile = new File(folder, "Verificar Matricula: "+identidad + ".pdf");
            }else {
                 pdfFile = new File(folder,"Verificar Matricula: "+identidad+ ".pdf");
            }
        }catch (Exception e){
        e.printStackTrace();
        }
    }

    public  void viewPDF(){
        Intent intent = new Intent(context, ViewPdf.class);
        intent.putExtra("path",pdfFile.getAbsolutePath());
        intent.putExtra("file",pdfFile.toString());
        intent.putExtra("i",i+".pdf");
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
