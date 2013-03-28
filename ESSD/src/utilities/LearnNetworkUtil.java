package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import smile.Network;
import smile.learning.DataMatch;
import smile.learning.DataSet;
import smile.learning.EM;

public class LearnNetworkUtil {
	
	
	
	public static void main(String[] args) throws IOException{
		List<String> case_num=new ArrayList<String>();
		BufferedWriter output;
		BufferedReader br=new BufferedReader (new InputStreamReader(System.in));
		
		System.out.println("[1]=Specific Case Numbers  [2]=Use all Data: [3]=modified ");
		String caseNumber=br.readLine();
		String[] arrayCN=null;
		if(caseNumber.equals("1")){
			System.out.println("Enter Case Numbers-use comma:");
			caseNumber=br.readLine();
			arrayCN=caseNumber.split(",");
			
			
		}
		else if(caseNumber.equals("3")){
			arrayCN = new String[111];
			for(int c=0;c<111;c++){
				arrayCN[c]=""+(c+1);
			}						
			
		}
		else{
			case_num=getListCaseNumber();
			arrayCN = new String[case_num.size()];
			arrayCN = case_num.toArray(arrayCN);
		}
		
		DocumentBuilder dBuilder = null;
		Document doc = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		XPath xp = XPathFactory.newInstance().newXPath();
		File details = new File("ESSD_Files/System_Network/LearnNetwork.xml");
		
		for(int ctr=0;ctr<arrayCN.length;ctr++){
			String tempCaseNum=arrayCN[ctr];
			System.out.println(tempCaseNum);
			File temp = new File("ESSD_Files/System_Network/LearnNetworkInput.txt");
			//System.out.printf("File is located at %s%n", temp.getAbsolutePath());
			
			output = new BufferedWriter(new FileWriter(temp));
			
			 //File file = new File("StrokeProbabilities.txt");
			 
			
			try {
				dBuilder = dbFactory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}					
			try {
				doc = dBuilder.parse(details);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			doc.getDocumentElement().normalize();
			
			try {		
				
				String tempQ=xp.evaluate("/data/patient_"+tempCaseNum+"/risk_factor/text()", doc.getDocumentElement());
				System.out.println(tempCaseNum);						
				String tempRf=xp.evaluate("/data/patient_"+tempCaseNum+"/range/text()", doc.getDocumentElement());
				
				String data=tempQ+"\n"+tempRf;
				output.write(data);
				output.close();
				feedToNetwork();
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	public static void feedToNetwork(){
		  DataSet dataSet = new DataSet(); 	  
		  Network net = new Network();	
		  EM em = new EM();
		  
		  dataSet.readFile("ESSD_Files/System_Network/LearnNetworkInput.txt");
		  net.readFile("ESSD_Files/System_Network/STROKEd.xdsl");
		  
		  DataMatch[] matching = dataSet.matchNetwork(net);
		
		  
		  em.learn(dataSet,net,matching);
		 
		  net.writeFile("ESSD_Files/System_Network/STROKEd.xdsl");
		
	}
	
	public static List<String> getListCaseNumber(){
		List<String> caseNumber=new ArrayList<String>();
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			//Add node to the database									
			ps = conn.prepareStatement("SELECT * FROM patient");
			rs = ps.executeQuery();
			while(rs.next()){
				//tempCaseNum=;
				caseNumber.add(""+rs.getInt("case_num"));
			}
			
			//tempCaseNum=tempCaseNum+1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Internal Error:", "Error Occured, Please Refresh Page."));
			//RequestContext.getCurrentInstance().execute("addDialog.hide()");
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Internal Error:", "Error Occured, Please Refresh Page."));
			}
		}
		return caseNumber;
	}
}
