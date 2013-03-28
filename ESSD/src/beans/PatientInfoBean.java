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
import java.util.Hashtable;
import java.util.List;
import smile.Network;
import smile.learning.*;
import utilities.JdbcUtil;
import java.util.Date;
@ViewScoped
@ManagedBean
@SessionScoped
public class PatientInfoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected static Patient patient = new Patient();
	protected static List<String> docNameList = new ArrayList<String>();
	static HashMap licenseList = new HashMap(); 
	protected static String docName = "";
	protected static String docLicense = "";
	protected static int docID=0;
	
	protected static Patient selectedP;
	protected static Patient selectedLoadP;
	protected static PatientDataModel patientDataModel;
	protected static List<Patient> patients;
	protected List<Patient> filteredP;
	
	
	
	protected String caseNumStrTemp;
	
	private static String patientLoaded="none";
	
	protected List<String> strNameRf;
	
	protected String[] arr = new String[3];
    
	public String getCaseNumStrTemp() {
		return caseNumStrTemp;
	}
	public void setCaseNumStrTemp(String caseNumStrTemp) {
		this.caseNumStrTemp = caseNumStrTemp;
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		PatientInfoBean.docID = docID;
	}
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		PatientInfoBean.patient = patient;
	}
	public HashMap getLicenseList() {
		return licenseList;
	}
	public void setLicenseList(HashMap licenseList) {
		PatientInfoBean.licenseList = licenseList;
	}
	public List<String> getDocNameList() {
		return docNameList;
	}
	public void setDocNameList(List<String> docNameList) {
		PatientInfoBean.docNameList = docNameList;
	}
	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		PatientInfoBean.docName = docName;
	}

	public String getDocLicense() {
		return docLicense;
	}

	public void setDocLicense(String docLicense) {
		PatientInfoBean.docLicense = docLicense;
	}
	
	
	
	public Patient getSelectedP() {
		return selectedP;
	}
	public void setSelectedP(Patient selectedP) {
		PatientInfoBean.selectedP = selectedP;
	}
	
	public Patient getSelectedLoadP() {
		return selectedLoadP;
	}
	public void setSelectedLoadP(Patient selectedLoadP) {
		this.selectedLoadP = selectedLoadP;
	}
	public List<Patient> getPatients() {
		return patients;
	}
	public void setPatients(List<Patient> patients) {
		PatientInfoBean.patients = patients;
	}
	public PatientDataModel getPatientDataModel() {
		return patientDataModel;
	}
	public void setPDataModel(PatientDataModel patientDataModel) {
		PatientInfoBean.patientDataModel = patientDataModel;
	}
	public List<Patient> getFilteredP() {
		return filteredP;
	}
	public void setFilteredP(List<Patient> filteredP) {
		this.filteredP = filteredP;
	}

	public List<String> getStrNameRf() {
		return strNameRf;
	}
	public void setStrNameRf(List<String> strNameRf) {
		this.strNameRf = strNameRf;
	}
	
	public String[] getArr() {
		return arr;
	}
	public void setArr(String[] arr) {
		this.arr = arr;
	}
	
	
	
	
	
	public void stateChangeListenerDoc(){
		getDoctorID(docName);
		docLicense=""+licenseList.get(docName);
	}
	public void processPatientInfo(){
		//docName="1";
		stateChangeListenerDoc();
		
		getDoctor();
		//System.out.println(AccountMgtBean.getUserId()+"PROCESS PATIENT: "+getPatientLoaded());
		if (getPatientLoaded().equals("none") || getPatientLoaded().equals("new")){
			
			int tempCaseNum= getNextCaseNumber();
			patient.setCaseNumStr(""+tempCaseNum);			
						
			RequestContext.getCurrentInstance().execute("ppiDialog.show()");
			
		}
		else{
			RequestContext.getCurrentInstance().execute("savePatientDialog.show()");
			}
        
	}
	
	public void getDoctorID(String docName){
		//System.out.println(docName);
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;		
		try {			
			ps = conn.prepareStatement("SELECT * FROM user WHERE name = ?");	
			ps.setString(1, docName);
			rs = ps.executeQuery();		
			if(rs.next()){
				//System.out.println("name: "+rs.getString("name"));
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
	
	
	public String getDoctor(){
		licenseList = new HashMap(); 
		int tempCaseNum=0;
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		docNameList=new ArrayList<String>();

		String doctor="";
		try {	
			
			ps = conn.prepareStatement("SELECT * FROM user WHERE user_id ="+docID);
			
			rs = ps.executeQuery();
			String rightDoc="";
			String rightLicense="";
			
			if(rs.next()){
				//String temp=rs.getString("name")+"_"+rs.getString("license");
				//System.out.println("RD: "+temp);
				rightDoc=rs.getString("name");
				rightLicense=rs.getString("license");
				docID=rs.getInt("user_id");
				//System.out.println("ODID: "+docID);
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
				//System.out.println("DUMMY: "+dummy1);
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
	
	public static int getNextCaseNumber(){
		int tempCaseNum=0;
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			//Add node to the database									
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
		return tempCaseNum;
	}
	
	public void saveNewPatient(){
		int tempCaseNum= getNextCaseNumber();
		
		setPatientLoaded("new");
		patient.setComplaint("");
		patient.setDoctor(docID);
		
		patient.setCaseNumStr(""+tempCaseNum);
		
		RequestContext.getCurrentInstance().execute("savePatientDialog.hide()");
		RequestContext.getCurrentInstance().execute("ppiDialog.show()");
		
	}
	
	public void saveDataDecider() throws IOException{
		//RequestContext.getCurrentInstance().execute("confirmSaveDialog.hide()");
		//System.out.println("STOPPER! "+getPatientLoaded());
		 if(getPatientLoaded().equals("none") || getPatientLoaded().equals("new")  ){
			 saveData();
		 }
		 else{		
			 updateData();
			 
		 }
	}

	public void saveExportData() throws IOException{
		RequestContext.getCurrentInstance().execute("ppiDialog.hide()");
		
	}
	
	//create an xml file for the patient record to be used to learn the network
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
		  output = new BufferedWriter(new FileWriter("ESSD_Files/System_Network/LearnNetwork.xml"));		
		 
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
	
	public List<String> readFile() throws IOException{			
		  
		  FileInputStream fstream = new FileInputStream("ESSD_Files/System_Network/LearnNetwork.xml");
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
			
			if(docID!=AccountMgtBean.getUserId())
				setPatientLoaded("new");
			else
				setPatientLoaded(""+tempCaseNum);
			xMLForNetwork(""+tempCaseNum,"SAVE");
			
			//make .xml file
			
			output = new BufferedWriter(new FileWriter("ESSD_Files/Patient_DB/"+tempCaseNum+".xml"));	
			
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
	public void loadOldPatient(){
		
		patient.setCaseNumStr(selectedLoadP.getCaseNumStr());
		patient.setComplaint(selectedLoadP.getComplaint());
		patient.setDoctor(selectedLoadP.getDoctor());
		setPatientLoaded(selectedLoadP.getCaseNumStr());
		RequestContext.getCurrentInstance().execute("savePatientDialog.hide()");
		RequestContext.getCurrentInstance().execute("ppiDialog.show()");
	}
	
	public void saveOldPatient() {
		RequestContext.getCurrentInstance().execute("savePatientDialog.hide()");
		RequestContext.getCurrentInstance().execute("saveOldPatientDialog.show()");
		 	 
	}
	
	public void updateData() throws IOException{
		  //System.out.println("STARTUPDATE");
		  //RequestContext.getCurrentInstance().execute("saveOldPatientDialog.hide()");
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
			
			
			//make .xml file
			xMLForNetwork(""+tempCaseNum,"UPDATE");
			output = new BufferedWriter(new FileWriter("ESSD_Files/Patient_DB/"+tempCaseNum+".xml"));	
			
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
			 //System.out.println("UpdateNewpatient");
			 setPatientLoaded("new");
			 //newPatient();
		 }
		 //System.out.println("UPDATEDONE");
	}
	
	public void loadSpecificPatient(){
		
		new ArrayList<String>();
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
		
		SPTBean.start();

		SPTBean.selectedRfs = new RiskFactor[listRf.size()];
		SPTBean.recSelected = new RiskFactor[listRf.size()];		
		SPTBean.selectedRfs = listRf.toArray(SPTBean.selectedRfs);		
		SPTBean.recSelected = SPTBean.selectedRfs;
		SPTBean.updateData(true);
		InferenceBean.doInference(SPTBean.selectedRfs, SPTBean.st);
		
		
		
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
		//patientLoaded="none";
		//setPatientLoaded("none");
		
		
	}
	
	public String onFlowProcess(FlowEvent event) {  
        return event.getNewStep();  
    }  
    
	
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
	public String getPatientLoaded() {
		return patientLoaded;
	}
	public static void setPatientLoaded(String patientLoaded) {
		PatientInfoBean.patientLoaded = patientLoaded;
	}
	
	
	

}
