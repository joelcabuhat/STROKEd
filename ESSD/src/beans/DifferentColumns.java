package beans;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

@ManagedBean
@SessionScoped
public class DifferentColumns implements Serializable {
	private static final long serialVersionUID = 1L;
  protected final Paragraph HEADER = new Paragraph("YOUR NOTES");
  protected final Paragraph LINES = new Paragraph("...............\n...............\n...............\n...............\n...............\n...............");

  public void start() throws IOException, DocumentException {
	  System.out.println("differentcolumns");
    new DifferentColumns().createPdf("differentcolumns.pdf");
  }

  public void createPdf(String filename) throws IOException, DocumentException {
    Document document = new Document();
    PdfWriter writer = PdfWriter.getInstance(document,
      new FileOutputStream(filename));
    document.open();
    PdfContentByte canvas = writer.getDirectContent();
    ColumnText ct = new ColumnText(canvas);
    for (int i = 0; i < 50; ) {
      ct.addElement(new Paragraph(String.valueOf(++i) + ": quick brown fox jumps over the lazy dog"));
    }
    int status = ColumnText.START_COLUMN;
    while (ColumnText.hasMoreText(status)) {
      addNotes(canvas);
      ct.setSimpleColumn(36, 36, 200, 806);
      ct.setYLine(806);
      status = ct.go();
      document.newPage();
    }
    document.close();
  }

  public void addNotes(PdfContentByte canvas) throws DocumentException {
    ColumnText ct = new ColumnText(canvas);
    ct.addElement(HEADER);
    ct.addElement(LINES);
    ct.setSimpleColumn(236, 36, 500, 806);
    ct.go();
  }
}