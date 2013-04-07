package beans;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;


import model.RiskFactor;



import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
 

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;

import com.itextpdf.text.pdf.PdfPageEventHelper;

import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.GrayColor;

import com.itextpdf.text.Font;


/**
 * @author Rhiza Mae G. Talavera
 * 
 * Generates the medical report of the application in pdf form.
 * Servlet implementation class Pdf
 */
@ManagedBean
@SessionScoped
@WebServlet("/Pdf")
public class Pdf extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	
	/**
	 * Post action of the servlet to create the pdf form for medical report.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String image2 = getServletContext().getRealPath("images/Report/Rosier1.png");
     	String image3 = getServletContext().getRealPath("images/Report/ESSD.png");
     	String image4 = getServletContext().getRealPath("images/Report/Header1.png");
     	String imag1 =  getServletContext().getRealPath("images/Report/transparent1.png");
     	String imag2 =  getServletContext().getRealPath("images/Report/transparent2.png");
     	String image5 = getServletContext().getRealPath("images/Report/Background.png");
     	String image6 = getServletContext().getRealPath("images/Report/Info.png");
     	String image7 = getServletContext().getRealPath("images/Report/Transparent31.png");
     	String image8 = getServletContext().getRealPath("images/Report/Verified.png");  	
     	
        response.setContentType("application/pdf");
               
        try {
        	Image verify= Image.getInstance(image8);
			Image rosier = Image.getInstance(image2);
			Image essd = Image.getInstance(image3);
			Image transparent1 = Image.getInstance(imag1);
			Image transparent2 = Image.getInstance(imag2);
			Image header = Image.getInstance(image4);
			Image background = Image.getInstance(image5);
			Image info= Image.getInstance(image6);
			Image transparent3= Image.getInstance(image7);
            Document document = new Document(background);
            PdfWriter writer=PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new Watermark());
            document.open();

            
            info.setAbsolutePosition(0, 600);       
	        header.setAbsolutePosition(0, 771);
	        document.add(header);
	        document.add(transparent3);
	        absText(PatientInfoBean.patient.getCaseNumStr(),540,750,13,writer);       
	        
	        float heady=transparent1.getAbsoluteY(); 

	        rosier.setAbsolutePosition(0,heady);
	        document.add(transparent1);
	        document.add(rosier);
	        document.add(new Paragraph(" "));
	        document.add(createFirstTable());
	        document.add(new Paragraph(" "));
	        
	        document.add(rosierResult());        
	        document.add(transparent2);
	            	      
	        float yy=transparent2.getAbsoluteY();
	        
	        essd.setAbsolutePosition(0,yy);
	        document.add(new Paragraph(" "));
	        document.add(essd);
	        document.add(new Paragraph(" "));
	       
	        document.add(riskFactors());
	        document.add(new Paragraph("  "));
	        document.add(sPTResult());

	        document.add(new Paragraph("  "));	    	        	    	        
	        verify.setAbsolutePosition(0,100);

            document.close();
        } catch (DocumentException de) {
            throw new IOException(de.getMessage());
        }
	}
	
	/**
	 * Get action of the servlet to create the pdf form for medical report.
	 */
	 protected void doGet(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {

		     	String image2 = getServletContext().getRealPath("images/Report/Rosier1.png");     	
		     	String image3 = getServletContext().getRealPath("images/Report/ESSD.png");
		     	String image4 = getServletContext().getRealPath("images/Report/Header1.png");
		     	String imag1 =  getServletContext().getRealPath("images/Report/transparent1.png");
		     	String imag2 =  getServletContext().getRealPath("images/Report/transparent2.png");
		     	String image5 = getServletContext().getRealPath("images/Report/Background.png");
		     	String image6 = getServletContext().getRealPath("images/Report/Info.png");
		     	String image7 = getServletContext().getRealPath("images/Report/Transparent31.png");
		     	String image8 = getServletContext().getRealPath("images/Report/Verified.png");
     	
		        response.setContentType("application/pdf");    
		        
		        try {
		        	Image verify= Image.getInstance(image8);
	    			Image rosier = Image.getInstance(image2);
	    			Image essd = Image.getInstance(image3);
	    			Image transparent1 = Image.getInstance(imag1);
	    			Image transparent2 = Image.getInstance(imag2);
	    			Image header = Image.getInstance(image4);
	    			Image background = Image.getInstance(image5);
	    			Image info= Image.getInstance(image6);
	    			Image transparent3= Image.getInstance(image7);
		            Document document = new Document(background);
		            PdfWriter writer=PdfWriter.getInstance(document, response.getOutputStream());
		            writer.setPageEvent(new Watermark());
		            document.open();
		            
		            info.setAbsolutePosition(0, 600);    	        
	    	        header.setAbsolutePosition(0, 771);
	    	        document.add(header);
	    	        document.add(transparent3);

	    	        absText(PatientInfoBean.patient.getCaseNumStr(),540,750,13,writer);
     
	    	        float heady=transparent1.getAbsoluteY(); 
	    	        rosier.setAbsolutePosition(0,heady);
	    	        document.add(transparent1);
	    	        document.add(rosier);
	    	        document.add(new Paragraph(" "));
	    	        document.add(createFirstTable());
	    	        document.add(new Paragraph(" "));
	    	        
	    	        document.add(rosierResult());        
	    	        document.add(transparent2);
	    	            	      
	    	        float yy=transparent2.getAbsoluteY();
	    	        
	    	        essd.setAbsolutePosition(0,yy);
	    	        document.add(new Paragraph(" "));
	    	        document.add(essd);
	    	        document.add(new Paragraph(" "));
	    	       
	    	        document.add(riskFactors());
	    	        document.add(new Paragraph("  "));
	    	        document.add(sPTResult());
	    	        
	    	        document.add(new Paragraph("  "));	    	        	    	        
	    	        verify.setAbsolutePosition(0,100);

		            document.close();
		        } catch (DocumentException de) {
		            throw new IOException(de.getMessage());
		        }        
		    }
	 
	/**
	 * Creates the first table structure for ROSIER report.
	 * @return PdfTable of the first table for ROSIER.
	 * @throws DocumentException Pdf creation.
	 */
	 public static PdfPTable createFirstTable()  throws  DocumentException {
	        PdfPTable table = new PdfPTable(2);
	        table.setWidths(new int[]{4, 1});
	        PdfPCell cell;
	        
	        cell = new PdfPCell(new Phrase("QUESTIONS [Yes=score-1] "));
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        cell.setColspan(2);
	        table.addCell(cell);
	              
	        table.addCell("Has there been loss of consciousness or syncope?");
	        table.addCell(RosierBean.holderID.get(0).getValue()+"");
	        table.addCell("Has there been seizure activity?");
	        table.addCell(RosierBean.holderID.get(1).getValue()+"");
	        
	        cell = new PdfPCell(new Phrase(" Is there a New Acute onset (or on awakening from sleep)? [Yes=score+1] "));
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        cell.setColspan(2);
	        table.addCell(cell);
	        
	        table.addCell("Asymmetric facial weakness");
	        table.addCell(RosierBean.holderID.get(2).getValue()+"");
	        table.addCell("Asymmetric arm weakness");
	        table.addCell(RosierBean.holderID.get(3).getValue()+"");
	        table.addCell("Asymmetric leg weakness");
	        table.addCell(RosierBean.holderID.get(4).getValue()+"");
	        table.addCell("Speech disturbance");
	        table.addCell(RosierBean.holderID.get(5).getValue()+"");
	        table.addCell("Visual field defect");
	        table.addCell(RosierBean.holderID.get(6).getValue()+"");
	     
	        return table;
	    }
	 
	 /**
	  * Creates the table structure for the result of ROSIER scoring system.
	  * @return Pdf table for ROSIER result.
	  * @throws DocumentException Pdf formatting and writing.
	  */
	 public static PdfPTable rosierResult()  throws DocumentException {
		    
	        PdfPTable table = new PdfPTable(2);
	        table.setWidths(new int[]{2,2});  
	        table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell("Provisional Diagnosis");      
	        table.addCell("Total Score");
	        table.getDefaultCell().setBackgroundColor(null);
	        table.addCell(RosierBean.result.getDiagnosis()+"");        
	        table.addCell(RosierBean.result.getScore()+"");
	        return table;
	    }
	    
	 /**
	  * Creates the table structure for the list of risk factor for a certain patient.
	  * @return Pdf table for the list of risk factor of patient.
	  * @throws DocumentException Pdf creation.
	  */
	    public static PdfPTable riskFactors()  throws DocumentException {
	        
	        PdfPTable table = new PdfPTable(3);
	        table.setWidths(new int[]{3,2, 2});              
	        table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell("Risk Factor");
	        table.addCell("Value");
	        table.addCell("Probability");
	        table.getDefaultCell().setBackgroundColor(null);
	        if(SPTBean.selectedRfs.length!=0){
		        for (RiskFactor i: SPTBean.selectedRfs) {
					table.addCell(i.getName()+"");
				    table.addCell(i.getSelectedRange()+"");
				    table.addCell(i.getProbability()+"");
				}
	        }
	        else{
	        	table.addCell("none");
			    table.addCell("none");
			    table.addCell("none");
	        }

	        return table;
	    }
	    
	    /**
	     * Creates the table structure for the result of SPT.
	     * @return PDF table for SPT result.
	     * @throws DocumentException Pdf creation.
	     */
	    public static PdfPTable sPTResult()  throws DocumentException {
	        
	        PdfPTable table = new PdfPTable(2);
	        table.setWidths(new int[]{2, 2});     
	        table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell("Stoke Type");
	        table.addCell("Probability");
	        table.getDefaultCell().setBackgroundColor(null);         
	        for (model.StrokeType i: SPTBean.st) {
				table.addCell(i.getName()+"");
				double temp=i.getProbability();
				temp=temp*100;
				DecimalFormat fmt = new DecimalFormat("00.00");  
				String string = fmt.format(temp);
			    table.addCell(string+"%");
			}        
	        return table;
	    }
	 
	  /**
	   *   Watermark for the PDF document of medical report.
	   *
	   */
	 class Watermark extends PdfPageEventHelper {
    	 
	        Font FONT = new Font(FontFamily.HELVETICA, 52, Font.BOLD, new GrayColor(0.75f));
	        String image5 = getServletContext().getRealPath("images/Report/Background.png");
	        public void onEndPage(PdfWriter writer, Document document)  {
	        	  Image background=null;
				try {
					background = Image.getInstance(image5);
					background.setAbsolutePosition(0, 0);
				} catch (BadElementException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        	  PdfContentByte content = writer.getDirectContentUnder();
	        	  try {
					content.addImage (background);
				} catch (DocumentException e) {
					e.printStackTrace();
				}
	        	
	          
	        }
	    }
	 
	 /**
	  * Sets a text to an absolute position in the pdf.
	  * @param text Text to be added on the PDF.
	  * @param x The x value for the text on PDF.
	  * @param y The v value for the text on PDF.
	  * @param size Font size of text.
	  * @param writer Pdf writer.
	  */
	 private static void absText(String text, int x, int y, int size, PdfWriter writer) {
	        try {
	          PdfContentByte cb = writer.getDirectContent();
	          BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	          cb.saveState();
	          cb.beginText();
	          cb.moveText(x, y);
	          cb.setFontAndSize(bf, size);
	          cb.showText(text);
	          cb.endText();
	          cb.restoreState();
	        } catch (DocumentException e) {
	          e.printStackTrace();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
    
}