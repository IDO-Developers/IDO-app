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
    private Font fTitulo = new Font(Font.FontFamily.TIMES_ROMAN,24,Font.BOLD);
    private Font fSubTitulo = new Font(Font.FontFamily.TIMES_ROMAN,20,Font.BOLDITALIC);
    private Font fecha = new Font(Font.FontFamily.TIMES_ROMAN,16,Font.NORMAL|Font.BOLD);

    private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN,14,Font.UNDERLINE|Font.BOLD);
    private Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN,13,Font.NORMAL);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD);
    private Font fTextNota = new Font(Font.FontFamily.TIMES_ROMAN,11,Font.BOLD);
    private Font text = new Font(Font.FontFamily.TIMES_ROMAN,16,Font.NORMAL);
    private Font textT = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD);
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


    public void createPdf(Image image) throws IOException, DocumentException {

        paragraph =new Paragraph();

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        float[] medidaCeldas = {20.50f, 14.5f, 36.5f,30.55f};
        table.setWidths(medidaCeldas);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        addEspacio("");
        table.addCell(image);
        table.addCell("");
        table.addCell("");

        PdfPCell pdfPCell = new PdfPCell(new Phrase("http//www.sivce.ido.edu.hn", fSubTitle));
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
        addChildP(new Paragraph(date,fecha));
        addChildP(new Paragraph(control,fTitle));

        try {
            document.add(paragraph);
        } catch (Exception e) {
         }
    }

    public void addTexto(String texto){
     paragraph = new Paragraph();
     addChildP(new Paragraph(texto,text));

        try {
            document.add(paragraph);
        } catch (Exception e) {
        }
    }
    public void addTextoT(String texto){
        paragraph = new Paragraph();
        addChildP(new Paragraph(texto,textT));

        try {
            document.add(paragraph);
        } catch (Exception e) {
        }
    }



    public  void addEspacio(String text){
        try{
            paragraph = new Paragraph(text ,fTextNota);
            paragraph.setSpacingBefore(10);
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

    public void viewPDF(){
        Intent intent = new Intent(context, ViewPdf.class);
        intent.putExtra("path",pdfFile.getAbsolutePath());
        intent.putExtra("file",pdfFile.toString());
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


}
