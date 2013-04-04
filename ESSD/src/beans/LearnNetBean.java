package beans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import org.primefaces.context.RequestContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import smile.Network;
import smile.learning.DataMatch;
import smile.learning.DataSet;
import smile.learning.EM;
import utilities.JdbcUtil;


/**
 * @author Rhiza Mae G. Talavera
 * 
 * This class reads an xml file then feeds it to the system's network for dynamic learning. 
 */
@ManagedBean
@SessionScoped
public class LearnNetBean {
	
	protected Patient selectedP;
	protected List<Patient> filteredP;
	protected static Patient [] recSelectedP;
	protected static PatientDataModel pDataModel;
	protected static List<Patient> p;
	protected static Patient [] selectedPs;
	
	/**
	 * This method is a constructor of the class
	 */
	public LearnNetBean () {
		
	}

	/**
	 * This method gets the selected patient.
	 * @return The patient selected
	 */
	public Patient getSelectedP() {
		return selectedP;
	}


	/**
	 * Sets the value of the selected patient.
	 * @param selectedP The value to be set to be the selected patient.
	 */
	public void setSelectedP(Patient selectedP) {
		this.selectedP = selectedP;
	}


	/**
	 * Gets the filtered list of patients.
	 * @return The list of filtered patients selected. 
	 */
	public List<Patient> getFilteredP() {
		return filteredP;
	}


	/**
	 * Sets the value for the list of filtered patients.
	 * @param filteredP The value to be set to the list of patients.
	 */
	public void setFilteredP(List<Patient> filteredP) {
		this.filteredP = filteredP;
	}


	/**
	 * Gets the list of recorded selected patients.
	 * @return The array of the recorded selected patients.
	 */
	public Patient[] getRecSelectedP() {
		return recSelectedP;
	}


	/**
	 * Sets the list of recorded selected patients.
	 * @param recSelectedP The value to set to the list of selected patients.
	 */
	public void setRecSelectedP(Patient[] recSelectedP) {
		LearnNetBean.recSelectedP = recSelectedP;
	}


	/**
	 * Gets the patient data model.
	 * @return The data model for patient.
	 */
	public PatientDataModel getpDataModel() {
		return pDataModel;
	}


	/**
	 * Sets the data model for patients.
	 * @param pDataModel The value to be set to the model of patients.
	 */
	public void setpDataModel(PatientDataModel pDataModel) {
		LearnNetBean.pDataModel = pDataModel;
	}


	/**
	 * Gets the attributes of patients.
	 * @return Array list of patients.
	 */
	public List<Patient> getP() {
		return p;
	}


	/**
	 * Sets the value for the array list of patients. 
	 * @param p The value to set for the array list of patients.
	 */
	public void setP(List<Patient> p) {
		LearnNetBean.p = p;
	}


	/**
	 * Gets the array list of selected patients to be used to learn the network.
	 * @return An array list of patients selected to learn.
	 */
	public Patient[] getSelectedPs() {
		return selectedPs;
	}


	/**
	 * Sets the list for the selected patients to learn the network. 
	 * @param selectedPs The list of patients selected to learn the network.
	 */
	public void setSelectedPs(Patient[] selectedPs) {
		LearnNetBean.selectedPs = selectedPs;
	}



	/**
	 * Initializes the list of patients and data model for the learning function of the system's network.
	 */
	public static void start () {
	
		p = new ArrayList<Patient>();
		populateLearnNet(p);
		pDataModel = new PatientDataModel(p);
	}
	
	/**
	 * Calls the start() function to retrieve the list of patients.
	 */
	public void retrievePatients () {
		start();		
	}
	

	/**
	 * Gets all the patients from database then populates the value for patient list.
	 * @param list Holds the values for the list of patients.
	 */
	private static void populateLearnNet (List<Patient> list) {

		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
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
	
	
	/**
	 * Gets the values for each patient from an xml file then saves it to a txt file for network learning.
	 * @throws IOException For reading LearnNetworkInput.txt
	 */
	public static void learn() throws IOException{
		BufferedWriter output;	
		DocumentBuilder dBuilder = null;
		Document doc = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		XPath xp = XPathFactory.newInstance().newXPath();
		File details = new File("ESSD_Files/System_Network/LearnNetwork.xml");
		
		for (Patient i: selectedPs) {
			
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
	
	/**
	 * Uses an EM() function algorithm for the learning of the system's network. Overwrites the old network with a learned one.
	 */
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
}
