package beans;


import java.io.*;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import mapping.MapToClass;
import model.RiskFactor;

import model.Patient;
import model.PatientDataModel;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import utilities.JdbcUtil;
import java.util.Date;
/**
 * @author Rhiza Mae G. Talavera
 * 
 * Handles patient information and other manipulation of patient attributes.
 */
@ViewScoped
@ManagedBean
@SessionScoped
public class PatientInfoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected static Patient patient = new Patient();
	protected static List<String> docNameList = new ArrayList<String>();
	static HashMap<String, String> licenseList = new HashMap<String, String>(); 
	protected static String docName = "";
	protected static String docLicense = "";
	protected static int docID=0;
	protected static Patient selectedP;
	protected static Patient selectedLoadP;
	protected static PatientDataModel patientDataModel;
	protected static List<Patient> patients;
	protected List<Patient> filteredP;
	private static String patientLoaded="none";
	public static boolean flag = false;
    	
	/**
	 * Gets the doctor's ID assigned to current patient.
	 * @return Returns the doctor's ID assigned to current patient.
	 */
	public int getDocID() {
		return docID;
	}
	/**
	 * Sets the ID of doctor.
	 * @param docID The value to be set to the doctor assigned to current patient.
	 */
	public void setDocID(int docID) {
		PatientInfoBean.docID = docID;
	}
	/**
	 * Gets the attribute of current patient.
	 * @return The patient.
	 */
	public Patient getPatient() {
		return patient;
	}
	/**
	 * Sets all attribute of current patient.
	 * @param patient Attributes of current patient being processed.
	 */
	public void setPatient(Patient patient) {
		PatientInfoBean.patient = patient;
	}
	/**
	 * Gets the license of all current users(GP, experts, etc.).
	 * @return List of licenses of users(GP, experts, etc.).
	 */
	public HashMap<String, String> getLicenseList() {
		return licenseList;
	}
	/**
	 * Sets the list of license list.
	 * @param licenseList List of license of all users(GP, experts, etc.).
	 */
	public void setLicenseList(HashMap<String, String> licenseList) {
		PatientInfoBean.licenseList = licenseList;
	}
	/**
	 * Gets the names of all users(GP, experts, etc.).
	 * @return Names of all users(GP, experts, etc.) saved in database.
	 */
	public List<String> getDocNameList() {
		return docNameList;
	}
	/**
	 * Sets the variable for the list of names of doctors.
	 * @param docNameList List of all users'(GP, experts, etc.) names.
	 */
	public void setDocNameList(List<String> docNameList) {
		PatientInfoBean.docNameList = docNameList;
	}
	/**
	 * Gets the name of current user(general practitioner, expert, etc.).
	 * @return The name of current user(general practitioner, expert, etc.).
	 */
	public String getDocName() {
		return docName;
	}
	/**
	 * Sets the variable that holds the name of current user(general practitioner, expert, etc.).
	 * @param docName User's(general practitioner, expert, etc.) name.
	 */
	public void setDocName(String docName) {
		PatientInfoBean.docName = docName;
	}
	/**
	 * Gets the license of current user(general practitioner, expert, etc.).
	 * @return The license of current user(general practitioner, expert, etc.).
	 */
	public String getDocLicense() {
		return docLicense;
	}
	/**
	 * Sets the variable that holds the license of current user(general practitioner, expert, etc.).
	 * @param docLicense User's(general practitioner, expert, etc.) license.
	 */
	public void setDocLicense(String docLicense) {
		PatientInfoBean.docLicense = docLicense;
	}
	/**
	 * Gets the selected patient.
	 * @return Selected patient.
	 */
	public Patient getSelectedP() {
		return selectedP;
	}
	/**
	 * Sets the variable that holds the selected patient.
	 * @param selectedP Selected patient.
	 */
	public void setSelectedP(Patient selectedP) {
		PatientInfoBean.selectedP = selectedP;
	}
	/**
	 * Gets the patient currently loaded.
	 * @return The patient currently loaded.
	 */
	public Patient getSelectedLoadP() {
		return selectedLoadP;
	}
	/**
	 * Sets the patient to be loaded.
	 * @param selectedLoadP The patient to be loaded.
	 */
	public void setSelectedLoadP(Patient selectedLoadP) {
		PatientInfoBean.selectedLoadP = selectedLoadP;
	}
	/**
	 * Gets list of patients.
	 * @return List of patients.
	 */
	public List<Patient> getPatients() {
		return patients;
	}
	/**
	 * Sets list of patients.
	 * @param patients List of patients.
	 */
	public void setPatients(List<Patient> patients) {
		PatientInfoBean.patients = patients;
	}
	/**
	 * Gets patient data model.
	 * @return Patient data model.
	 */
	public PatientDataModel getPatientDataModel() {
		return patientDataModel;
	}
	/**
	 * Sets patient data model.
	 * @param patientDataModel Data model for patient.
	 */
	public void setPDataModel(PatientDataModel patientDataModel) {
		PatientInfoBean.patientDataModel = patientDataModel;
	}
	/**
	 * Gets the filtered list of patients.
	 * @return Filtered list of patients.
	 */
	public List<Patient> getFilteredP() {
		return filteredP;
	}
	/**
	 * Sets the list of filtered patients.
	 * @param filteredP List of filtered patients.
	 */
	public void setFilteredP(List<Patient> filteredP) {
		this.filteredP = filteredP;
	}
	
	/**
	 * Gets the flag value if there's a patient loaded, saved, or none at all.
	 * @return Flag value if there's a patient loaded.
	 */
	public String getPatientLoaded() {
		return patientLoaded;
	}
	/**
	 * Sets the variable to be "new", "none", or patient case number.
	 * @param patientLoaded Flag value if there's a patient loaded.
	 */
	public static void setPatientLoaded(String patientLoaded) {
		PatientInfoBean.patientLoaded = patientLoaded;
	}
	
	/**
	 * Gets the flag value if select listener should be fired after loading a patient
	 * @return flag Flag value set after loading a patient
	 */
	public static boolean isFlag() {
		return flag;
	}
	
	/**
	 * Sets the flag value to true (patient is loaded) and false (no patient was loaded)
	 * @param flag Flag value set after loading a patient
	 */
	public static void setFlag(boolean flag) {
		PatientInfoBean.flag = flag;
	}
	
	/**
	 * Listener for a change of doctor assigned to patient. 
	 */
	public void stateChangeListenerDoc(){
		getDoctorID(docName);
		docLicense=""+licenseList.get(docName);
	}
	/**
	 * Show save patient dialog box or process patient information dialog box.
	 */
	public void processPatientInfo(){
		stateChangeListenerDoc();		
		getDoctor();

		if (getPatientLoaded().equals("none") || getPatientLoaded().equals("new")){
			int tempCaseNum= getNextCaseNumber();
			patient.setCaseNumStr(""+tempCaseNum);			
			RequestContext.getCurrentInstance().execute("ppiDialog.show()");
		}
		else{
			RequestContext.getCurrentInstance().execute("savePatientDialog.show()");
			}
	}
	/**
	 * Gets the current user's (e.g. general practitioner, expert, etc.) ID. 
	 * @param docName User's (e.g. general practitioner, expert) name. 
	 */
	public void getDoctorID(String docName){

		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;		
		try {			
			ps = conn.prepareStatement("SELECT * FROM user WHERE name = ?");	
			ps.setString(1, docName);
			rs = ps.executeQuery();		
			if(rs.next()){
				docID=rs.getInt("user_id");
			}			
		} catch (SQLException e) {
			e.printStackTrace();
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
			}
		}
	}
	
	/**
	 * Gets user's (e.g. general practitioner, expert) information to be display on patient information dialog box.
	 * @return User's attributes.
	 */
	public String getDoctor(){
		licenseList = new HashMap<String, String>(); 
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String doctor="";
		docNameList=new ArrayList<String>();
		try {		
			ps = conn.prepareStatement("SELECT * FROM user WHERE user_id ="+docID);		
			rs = ps.executeQuery();
			String rightDoc="";
			String rightLicense="";
			
			if(rs.next()){
				rightDoc=rs.getString("name");
				rightLicense=rs.getString("license");
				docID=rs.getInt("user_id");
			}
			docName=rightDoc;
			docLicense=rightLicense;
			docNameList.add(rightDoc);
			licenseList.put(rightDoc, new String(rightLicense)); 
			conn = JdbcUtil.startConnection();
			ps = null;
			rs = null;
			ps = conn.prepareStatement("SELECT * FROM user");
			rs = ps.executeQuery();			
			while(rs.next()){
				String dummy1=rs.getString("name");
				String dummy2=rs.getString("license");
				if(!dummy1.equals(rightDoc)){					
					docNameList.add(dummy1);
					licenseList.put(dummy1, new String(dummy2)); 
				}
			}			
		} catch (SQLException e) {
			e.printStackTrace();
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
			}
		}
		return doctor;
	}
	
	/**
	 * Gets case number for next patient.
	 * @return Case number of patient.
	 */
	public static int getNextCaseNumber(){
		int tempCaseNum=0;
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;		
		try {								
			ps = conn.prepareStatement("SELECT * FROM patient");
			rs = ps.executeQuery();		
			int ctr=0;
			while(rs.next()){
				ctr++;
			}		
			if(ctr!=0){
				rs.last();
				tempCaseNum=rs.getInt("case_num");
			}
			tempCaseNum=tempCaseNum+1;		
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
		return tempCaseNum;
	}
	
	/**
	 * Shows patient information dialog box and hides the save patient dialog box.
	 */
	public void saveNewPatient(){
		int tempCaseNum= getNextCaseNumber();		
		setPatientLoaded("new");
		patient.setComplaint("");
		patient.setDoctor(docID);	
		patient.setCaseNumStr(""+tempCaseNum);	
		RequestContext.getCurrentInstance().execute("savePatientDialog.hide()");
		RequestContext.getCurrentInstance().execute("ppiDialog.show()");
		
	}
	
	/**
	 * Calls method to save or update patient.
	 * @throws IOException For calling the saveData() and updateData() methods.
	 */
	public void saveDataDecider() throws IOException{
		 if(getPatientLoaded().equals("none") || getPatientLoaded().equals("new")  ){
			 saveData();
		 }
		 else{		
			 updateData();			 
		 }
	}

	/**
	 * Closes the patient information dialog box.
	 */
	public void saveExportData() {
		RequestContext.getCurrentInstance().execute("ppiDialog.hide()");
		
	}
	
	/**
	 * Creates an xml file for the patient record to be used to learn the network.
	 * @param caseNum Case number of current patient to be saved.
	 * @param type Holds flag value if to save or update patient record. 
	 * @throws IOException File reading.
	 */
	
	public void xMLForNetwork(String caseNum, String type) throws IOException{
		  List<String> textFromFile=new ArrayList<String>();
		  String text="";
		  BufferedWriter output = null;	  
		  String tempRf="";
		  String tempRange="";
		  int ctr=0;
		  for (RiskFactor i: SPTBean.selectedRfs) {
				String replaceSpace=i.getName().replace(" ", "_"); 
			    tempRf=tempRf+replaceSpace+" ";  
			    replaceSpace=i.getSelectedRange().replace(" ", "_");
				tempRange=tempRange+replaceSpace+" ";			  
			
		  }
		  double max=0.0;
		  String stroke="None";
		  for(ctr=0;ctr<3;ctr++){
			  double prob=SPTBean.st.get(ctr).getProbability();
			  if(max<prob){
				  max=prob;
				  stroke=SPTBean.st.get(ctr).getName().replace(" ", "_");
			  }
		  }
		  tempRf=tempRf+"Stroke";
		  tempRange=tempRange+stroke;
		  textFromFile=readFile();
		  output = new BufferedWriter(new FileWriter("STROKEd_Files/System_Network/LearnNetwork.xml"));		
		  text="";
		  if(type.equals("SAVE")){
			   for(ctr=0;ctr<textFromFile.size()-1;ctr++){
				  text=text+textFromFile.get(ctr)+"\n";
			  }
			  text=text+"\t\t<patient_"+caseNum+">\n";
				  	text=text+"\t\t\t<risk_factor>"+tempRf+"</risk_factor>\n";
				  	text=text+"\t\t\t<range>"+tempRange+"</range>\n";
				  	text=text+"\t\t</patient_"+caseNum+">\n";			
				  	text=text+"\t</data>\n";
			  output.write(text);
		  }
		  else{			
			  String findtext="<patient_"+caseNum+">";
			  int index=0;
			  for(ctr=0;ctr<textFromFile.size();ctr++){
				  String str=textFromFile.get(ctr).trim();
				  if(str.equals(findtext)){
					  index=ctr;
					  break;
				  }				  
			  }		 
			  String string1="\t\t\t<risk_factor>"+tempRf+"</risk_factor>";
			  String string2="\t\t\t<range>"+tempRange+"</range>";
			  textFromFile.set(index+1, string1);
			  textFromFile.set(index+2, string2);			  
			  for(ctr=0;ctr<textFromFile.size();ctr++){
				  text=text+textFromFile.get(ctr)+"\n";
			  }
			  output.write(text);
		  }		  
		  output.close();		  
	}
	
	/**
	 * Reads all patient record from LearnNetwork.xml file.  
	 * @return All text read from LearnNetwork.xml file.
	 * @throws IOException File reading and writing.
	 */
	public List<String> readFile() throws IOException{			
		  
		  FileInputStream fstream = new FileInputStream("STROKEd_Files/System_Network/LearnNetwork.xml");
		  List<String> textFromFile=new ArrayList<String>();
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine="";
		  while ((strLine = br.readLine()) != null)   {
			  textFromFile.add(strLine);	
		  }				  
		  in.close();
		  return (textFromFile);	 
}
	/**
	 * Saves patient data into an xml file and insert it to database.
	 * @throws IOException File reading and writing.
	 */
	public void saveData() throws IOException{
		  
		  BufferedWriter output;	  
		  String ros="";
		  int tempCaseNum=0;
		  List<String> rf=new ArrayList<String>();
		  String tempRf="";
		  
			Connection conn = JdbcUtil.startConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;	
			try {	
				ps = conn.prepareStatement("INSERT INTO patient "+"(doctor_id) " + "VALUES " +"(?)");
				ps.setInt(1, docID);
				ps.executeUpdate();		
				ps = conn.prepareStatement("SELECT * FROM patient");
				rs = ps.executeQuery();
				rs.last();
				tempCaseNum=rs.getInt("case_num");
				
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
			
			if(docID!=AccountMgtBean.getUserId())
				setPatientLoaded("new");
			else
				setPatientLoaded(""+tempCaseNum);
			xMLForNetwork(""+tempCaseNum,"SAVE");
			
			//make .xml file
			output = new BufferedWriter(new FileWriter("STROKEd_Files/Patient_DB/"+tempCaseNum+".xml"));	
			
		  for (RiskFactor i: SPTBean.selectedRfs) {
				tempRf=i.getCreated_by()+"#"+i.getCreated_date()+"#"+i.getDescription()+"#"+i.getGeneral_measures()+"#"+i.getHistory()+
					"#"+i.getLaboratory_exams()+"#"+i.getLast_update_by()+"#"+i.getLast_update_date()+"#"+i.getName()+"#"+i.getPhysical_exams()+
					"#"+i.getProbability()+"#"+i.getRange()+"#"+i.getRangeValues()+"#"+i.getRf_id()+"#"+i.getSelectedRange()+"#"+i.getSigns_symptoms()+
					"#"+i.getSpecific_measures();
				rf.add(tempRf);

		  }
		  tempRf="";
		  for(int ctr=0;ctr<rf.size();ctr++){
			  if(ctr!=rf.size()-1)
				  tempRf=tempRf+rf.get(ctr)+"@";
			  else
				  tempRf=tempRf+rf.get(ctr);
		  }
		  
		  String rosQ="";
		  for(int ctr=0;ctr< RosierBean.rq1.size();ctr++){
			  rosQ=rosQ+RosierBean.rq1.get(ctr).getValue()+"#";
		  }
		  for(int ctr=0;ctr<RosierBean.rq2.size();ctr++){
			  if (ctr==RosierBean.rq2.size()-1)
				  rosQ=rosQ+RosierBean.rq2.get(ctr).getValue();
			  else
				  rosQ=rosQ+RosierBean.rq2.get(ctr).getValue()+"#";
		  }  

		  ros="<?xml version=\"1.0\"?>\n";
		  ros=ros+"\t<data>\n";
		  	ros=ros+"\t\t<others>\n";
			  	ros=ros+"\t\t\t<doctor>"+docID+"</doctor>\n";
			  	ros=ros+"\t\t\t<complaint>"+PatientInfoBean.patient.getComplaint()+"</complaint>\n";
			ros=ros+"\t\t</others>\n";
			ros=ros+"\t\t<rosier>\n";
				ros=ros+"\t\t\t<questions>"+rosQ+"</questions>\n";
			  	ros=ros+"\t\t\t<diagnosis>"+RosierBean.result.getDiagnosis()+"</diagnosis>\n";
			  	ros=ros+"\t\t\t<score>"+RosierBean.result.getScore()+"</score>\n";
			ros=ros+"\t\t</rosier>\n";
			ros=ros+"\t\t<spt>\n";
				ros=ros+"\t\t\t<risk-factors>"+tempRf+"</risk-factors>\n";
			  	ros=ros+"\t\t\t<hemorrhagic-stroke>"+SPTBean.st.get(0).getProbability()+"</hemorrhagic-stroke>\n";
			  	ros=ros+"\t\t\t<ischemic-stroke>"+SPTBean.st.get(1).getProbability()+"</ischemic-stroke>\n";
			  	ros=ros+"\t\t\t<none>"+SPTBean.st.get(2).getProbability()+"</none>\n";
			ros=ros+"\t\t</spt>\n";
		  ros=ros+"\t</data>\n";
			output.write(ros);
	
		  output.close();
		  PatientInfoBean.start(); 
		 selectedLoadP= new Patient(); 
		 selectedLoadP.setCaseNumStr(""+tempCaseNum);
		 selectedLoadP.setComplaint(PatientInfoBean.patient.getComplaint());
		 selectedLoadP.setDoctor(PatientInfoBean.patient.getDoctor());
			
	}
	
	/**
	 * Re-populate and re-initialize variable for new patient. 
	 */
	public void newPatient(){
		docID=AccountMgtBean.getUserId();
		new SPTBean();
		SPTBean.start();
		SKDBean.start(); 
		PatientInfoBean.start();
		SPTBean.resetSPT(); 	
		new RosierBean();
		RosierBean.start();	
			
		setPatientLoaded("none");
		patient=new Patient();
		patient.setComplaint("");
		patient.setDoctor(docID);
		RequestContext.getCurrentInstance().execute("loading.hide()");
	}
	
	/**
	 * Loads data of patient selected and shows patient information dialog box.
	 */
	public void loadOldPatient(){	
		patient.setCaseNumStr(selectedLoadP.getCaseNumStr());
		patient.setComplaint(selectedLoadP.getComplaint());
		patient.setDoctor(selectedLoadP.getDoctor());
		setPatientLoaded(selectedLoadP.getCaseNumStr());
		RequestContext.getCurrentInstance().execute("savePatientDialog.hide()");
		RequestContext.getCurrentInstance().execute("ppiDialog.show()");
	}
	
	/**
	 * Hides the dialog box for a save patient and shows the dialog for updating the data of old patient.
	 */
	public void saveOldPatient() {
		RequestContext.getCurrentInstance().execute("savePatientDialog.hide()");
		RequestContext.getCurrentInstance().execute("saveOldPatientDialog.show()");
		 	 
	}
	
	/**
	 * Updates patient record in ess_db database and xml file.
	 * @throws IOException Reading and writing to a file.
	 */
	public void updateData() throws IOException{
		  String flag="nochangedoctor";  
		  BufferedWriter output;	  
		  String ros="";
		  int tempCaseNum=0;
		  List<String> rf=new ArrayList<String>();
		  String tempRf="";
		  if(docID!=AccountMgtBean.getUserId()){
			  flag="changedoctor";  
		  }
		  int patientId=Integer.parseInt(getPatientLoaded());
		  	Connection conn = JdbcUtil.startConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			tempCaseNum=patientId;	
			try {	
				ps = conn.prepareStatement("SELECT username FROM user WHERE user_id="+PatientInfoBean.patient.getDoctor());
				rs = ps.executeQuery();
				if (rs.next()){
					ps = conn.prepareStatement("UPDATE patient SET doctor_id="+docID+" WHERE case_num="+patientId);
					ps.executeUpdate();				
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
			//make .xml file
			xMLForNetwork(""+tempCaseNum,"UPDATE");
			output = new BufferedWriter(new FileWriter("STROKEd_Files/Patient_DB/"+tempCaseNum+".xml"));	
			
		  for (RiskFactor i: SPTBean.selectedRfs) {
				tempRf=i.getCreated_by()+"#"+i.getCreated_date()+"#"+i.getDescription()+"#"+i.getGeneral_measures()+"#"+i.getHistory()+
							"#"+i.getLaboratory_exams()+"#"+i.getLast_update_by()+"#"+i.getLast_update_date()+"#"+i.getName()+"#"+i.getPhysical_exams()+
							"#"+i.getProbability()+"#"+i.getRange()+"#"+i.getRangeValues()+"#"+i.getRf_id()+"#"+i.getSelectedRange()+"#"+i.getSigns_symptoms()+
							"#"+i.getSpecific_measures();
				rf.add(tempRf);
		  }
		  tempRf="";
		  for(int ctr=0;ctr<rf.size();ctr++){
			  if(ctr!=rf.size()-1)
				  tempRf=tempRf+rf.get(ctr)+"@";
			  else
				  tempRf=tempRf+rf.get(ctr);
		  }	  
		  String rosQ="";
		  for(int ctr=0;ctr< RosierBean.rq1.size();ctr++){		  
			  rosQ=rosQ+RosierBean.rq1.get(ctr).getValue()+"#";
		  }
		  for(int ctr=0;ctr<RosierBean.rq2.size();ctr++){
			  if (ctr==RosierBean.rq2.size()-1)
				  rosQ=rosQ+RosierBean.rq2.get(ctr).getValue();
			  else
				  rosQ=rosQ+RosierBean.rq2.get(ctr).getValue()+"#";
		  }  	  
		  ros="<?xml version=\"1.0\"?>\n";
		  ros=ros+"\t<data>\n";
		  	ros=ros+"\t\t<others>\n";
			  	ros=ros+"\t\t\t<doctor>"+docID+"</doctor>\n";
			  	ros=ros+"\t\t\t<complaint>"+PatientInfoBean.patient.getComplaint()+"</complaint>\n";
			ros=ros+"\t\t</others>\n";
			ros=ros+"\t\t<rosier>\n";
				ros=ros+"\t\t\t<questions>"+rosQ+"</questions>\n";
			  	ros=ros+"\t\t\t<diagnosis>"+RosierBean.result.getDiagnosis()+"</diagnosis>\n";
			  	ros=ros+"\t\t\t<score>"+RosierBean.result.getScore()+"</score>\n";
			ros=ros+"\t\t</rosier>\n";
			ros=ros+"\t\t<spt>\n";
				ros=ros+"\t\t\t<risk-factors>"+tempRf+"</risk-factors>\n";
			  	ros=ros+"\t\t\t<hemorrhagic-stroke>"+SPTBean.st.get(0).getProbability()+"</hemorrhagic-stroke>\n";
			  	ros=ros+"\t\t\t<ischemic-stroke>"+SPTBean.st.get(1).getProbability()+"</ischemic-stroke>\n";
			  	ros=ros+"\t\t\t<none>"+SPTBean.st.get(2).getProbability()+"</none>\n";
			ros=ros+"\t\t</spt>\n";
		  ros=ros+"\t</data>\n";
		 output.write(ros);
		 output.close();	  
		 PatientInfoBean.start();
		 if(flag.equals("changedoctor")){
			 setPatientLoaded("new");
		 }
	}
	
	/**
	 * Loads specific patient into the web application.
	 */
	public void loadSpecificPatient(){	
		
		flag=true;
		List<RiskFactor> listRf = new ArrayList<RiskFactor>();	
		patient.setCaseNumStr(selectedP.getCaseNumStr());
		setPatientLoaded(selectedP.getCaseNumStr());
		selectedLoadP=selectedP;

		for(int ctr=0;ctr<selectedP.getListRf().size();ctr++){
			String[] splits=selectedP.getListRf().get(ctr).split("#");
			Date date=null;
			RiskFactor dummy = new RiskFactor();
			dummy.setCreated_by(Integer.parseInt(splits[0]));
			dummy.setCreated_date(date);
			dummy.setDescription(splits[2]);
			dummy.setGeneral_measures(splits[3]);
			dummy.setHistory(splits[4]);
			dummy.setLaboratory_exams(splits[5]);			
			dummy.setLast_update_by(Integer.parseInt(splits[6]));
			dummy.setLast_update_date(date);
			dummy.setName(splits[8]);
			dummy.setPhysical_exams(splits[9]);
			dummy.setProbability(Integer.parseInt(splits[10]));
			dummy.setRange(splits[11]);
			dummy.setRangeValues();
			dummy.setRf_id(splits[13]);
			dummy.setSelectedRange(splits[14]);
			dummy.setSigns_symptoms(splits[15]);
			dummy.setSpecific_measures(splits[16]);	
			listRf.add(dummy);
		}
		
		
		SPTBean.selectedRfs = listRf.toArray(SPTBean.selectedRfs);	
		SPTBean.recSelected = SPTBean.selectedRfs;
		
		int c=-1;
		for (RiskFactor i: SPTBean.selectedRfs){
				c++;		
				for (int j = 0; j < SPTBean.rf.size(); j++) {
					if (SPTBean.rf.get(j).getName().equals(i.getName())){
						
						SPTBean.rf.get(j).getRangeValues().remove(i.getSelectedRange());		
						SPTBean.rf.get(j).getRangeValues().add(0, i.getSelectedRange());
						
						SPTBean.selectedRfs[c].getRangeValues().remove(i.getSelectedRange());
						SPTBean.selectedRfs[c].getRangeValues().add(0, i.getSelectedRange());
						
						break;
					}
						
				}																	
		}

		SPTBean.updateData(true);
		
		int indexRosQ=-1;
		for(int ctr=0;ctr<RosierBean.rq1.size();ctr++){
			indexRosQ=indexRosQ+1;
			RosierBean.rq1.get(ctr).setValue(selectedP.getRosQuestions().get(indexRosQ));
		}
		for(int ctr=0;ctr<RosierBean.rq2.size();ctr++){
			indexRosQ=indexRosQ+1;
			RosierBean.rq2.get(ctr).setValue(selectedP.getRosQuestions().get(indexRosQ));	
		}
		RosierBean.result.setScore(selectedP.getRosScore());
		RosierBean.whatDiagnosis();
		PatientInfoBean.start();
		
		RequestContext.getCurrentInstance().execute("loading.hide()");
		RequestContext.getCurrentInstance().execute("confirmLoadDialog.hide()");
		RequestContext.getCurrentInstance().execute("loadPatientDialog.hide()");			
	}
	
	/**
	 * Sets ID of current user and initializes patient variables in PatientInfoBean class.
	 */
	public static void start () {
		docID=AccountMgtBean.getUserId();
		patient=new Patient(); 
		patients = new ArrayList<Patient>();
		populatePID(patients);
		patientDataModel = new PatientDataModel(patients);
		patient.setComplaint("");
		patient.setDoctor(docID);			
		patient.setCaseNumStr(""+getNextCaseNumber());
		docName="1";
		//setPatientLoaded("none");
	}
	
	/**
	 * 
	 * @param event
	 * @return
	 */
	public String onFlowProcess(FlowEvent event) {  
        return event.getNewStep();  
    }  
    
	/**
	 * Populates the list of patients from database.
	 * @param list List of patients from patient table in database.
	 */
	private static void populatePID (List<Patient> list) {

		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//get ALL patients
			ps = conn.prepareStatement("SELECT * FROM patient where doctor_id= "+docID);
			rs = ps.executeQuery();				
			while(rs.next()){
				Patient p = new Patient();
				list.add(MapToClass.mapP(p, rs));
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
	

}
