package beans;

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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mapping.MapToClass;
import model.Patient;
import model.PatientDataModel;
import model.RiskFactor;


import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import smile.Network;
import smile.learning.DataMatch;
import smile.learning.DataSet;
import smile.learning.EM;
import utilities.JdbcUtil;


@ManagedBean
@SessionScoped
public class LearnNetBean {
	
	/* Global Variables */
	private static final long serialVersionUID = 1L;
	
	protected Patient selectedP;
	protected List<Patient> filteredP;
	protected static Patient [] recSelectedP;
	protected static PatientDataModel pDataModel;
	protected static List<Patient> p;
	protected static Patient [] selectedPs;
	
	public LearnNetBean () {
		
	}

	public Patient getSelectedP() {
		return selectedP;
	}



	public void setSelectedP(Patient selectedP) {
		this.selectedP = selectedP;
	}



	public List<Patient> getFilteredP() {
		return filteredP;
	}



	public void setFilteredP(List<Patient> filteredP) {
		this.filteredP = filteredP;
	}



	public Patient[] getRecSelectedP() {
		return recSelectedP;
	}



	public void setRecSelectedP(Patient[] recSelectedP) {
		LearnNetBean.recSelectedP = recSelectedP;
	}



	public PatientDataModel getpDataModel() {
		return pDataModel;
	}



	public void setpDataModel(PatientDataModel pDataModel) {
		LearnNetBean.pDataModel = pDataModel;
	}



	public List<Patient> getP() {
		return p;
	}



	public void setP(List<Patient> p) {
		LearnNetBean.p = p;
	}



	public Patient[] getSelectedPs() {
		return selectedPs;
	}



	public void setSelectedPs(Patient[] selectedPs) {
		LearnNetBean.selectedPs = selectedPs;
	}



	public static void start () {
	
		p = new ArrayList<Patient>();
		populateLearnNet(p);
		pDataModel = new PatientDataModel(p);
		//RequestContext.getCurrentInstance().execute("loading.hide()");
	}
	
	public void retrievePatients () {
		start();
		//System.out.println("RHIZUUUUH: "+pDataModel.getRowData().getCaseNumStr());
		
		
	}
	

	
	private static void populateLearnNet (List<Patient> list) {

		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			//get ALL patients
			ps = conn.prepareStatement("SELECT * FROM patient");
			rs = ps.executeQuery();
						
			while(rs.next()){
				Patient p = new Patient();
				list.add(MapToClass.mapPLearnNet(p, rs));
			}
						
		} catch (SQLException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Internal Error:", "Error Occured, Please Refresh Page."));
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
		
	}
	
	
	
	public static void learn() throws IOException{
		
		//System.out.println("START LEARNING");
		BufferedWriter output;
		BufferedReader br=new BufferedReader (new InputStreamReader(System.in));

		
		DocumentBuilder dBuilder = null;
		Document doc = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		XPath xp = XPathFactory.newInstance().newXPath();
		File details = new File("ESSD_Files/System_Network/LearnNetwork.xml");
		
		
		int ctr=-1;

		for (Patient i: selectedPs) {
			
			ctr++;
			String tempCaseNum=""+i.getCaseNum();
	
			File temp = new File("ESSD_Files/System_Network/LearnNetworkInput.txt");
			
			output = new BufferedWriter(new FileWriter(temp));
			
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
				//System.out.println(tempCaseNum);						
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
		
		
		SPTBean.start();
		SKDBean.start(); 
		PatientInfoBean.start();
		PatientInfoBean.setPatientLoaded("none");
		SPTBean.resetSPT(); 	
		RosierBean.start();	
			
		
		RequestContext.getCurrentInstance().execute("loading.hide()");
		RequestContext.getCurrentInstance().execute("confirmLearn.hide()");
		RequestContext.getCurrentInstance().execute("learnNetworkDialog.hide()");
	}
	
	public static void feedToNetwork(){
		  DataSet dataSet = new DataSet(); 	  
		  Network net = new Network();	
		  EM em = new EM();
		  
		  dataSet.readFile("ESSD_Files/System_Network/LearnNetworkInput.txt");
		  net.readFile("ESSD_Files/System_Network/STROKEd_Network.xdsl");
		  
		  DataMatch[] matching = dataSet.matchNetwork(net);
		
		  
		  em.learn(dataSet,net,matching);
		 
		  net.writeFile("ESSD_Files/System_Network/STROKEd_Network.xdsl");
		
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
