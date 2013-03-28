package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Patient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Global Variables */
	private String fname, mname, lname;
	
	protected String complaint, caseNumStr;
	private int caseNum, doctor;
	
	protected String rosDiagnosis;
	protected int rosScore;
	protected List<String> rosQuestions=new ArrayList<String>();
	
	protected double probHemorrhagic, probIschemic, probNone;

	protected List<String> listRf=new ArrayList<String>();
	protected List<String> nameRfs=new ArrayList<String>();

	protected List<String> rangeRfs=new ArrayList<String>();
	
	
	/*
	 * Constructor
	 */
	public Patient () {
		
	}
	
	/*
	 * Getter for patient's first name
	 */
	public String getFname() {
		return fname;
	}
	
	/*
	 * Setter for patient's first name
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	/*
	 * Getter for patient's middle name
	 */
	public String getMname() {
		return mname;
	}
	
	/*
	 * Setter for patient's middle name
	 */
	public void setMname(String mname) {
		this.mname = mname;
	}
	
	/*
	 * Getter for patient's last name
	 */
	public String getLname() {
		return lname;
	}
	
	/*
	 * Setter for patient's last name
	 */
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	

	/*
	 * Getter for patient's complaint
	 */
	public String getComplaint() {
		return complaint;
	}

	/*
	 * Setter for patient's complaint
	 */
	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}
	
	
	
	
	
	public void setCaseNumStr(String caseNumStr) {
		this.caseNumStr = caseNumStr;
	}
	
	public String getCaseNumStr() {
		return caseNumStr;
	}
	
	
	public int getDoctor() {
		return doctor;
	}
	public void setDoctor(int doctor) {
		this.doctor = doctor;
	}
	public int getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(int caseNum) {
		this.caseNum = caseNum;
	}
	
	public String getRosDiagnosis() {
		return rosDiagnosis;
	}
	public void setRosDiagnosis(String rosDiagnosis) {
		this.rosDiagnosis = rosDiagnosis;
	}
	public int getRosScore() {
		return rosScore;
	}
	public void setRosScore(int rosScore) {
		this.rosScore = rosScore;
	}
	public double getProbHemorrhagic() {
		return probHemorrhagic;
	}
	public void setProbHemorrhagic(double probHemorrhagic) {
		this.probHemorrhagic = probHemorrhagic;
	}
	public double getProbIschemic() {
		return probIschemic;
	}
	public void setProbIschemic(double probIschemic) {
		this.probIschemic = probIschemic;
	}
	
	public double getProbNone() {
		return probNone;
	}
	public void setProbNone(double probNone) {
		this.probNone = probNone;
	}
	
	public List<String> getRosQuestions() {
		return rosQuestions;
	}
	public void setRosQuestions(List<String> rosQuestions) {
		this.rosQuestions = rosQuestions;
	}
	
	public List<String> getListRf() {
		return listRf;
	}
	public void setListRf(List<String> listRf) {
		this.listRf = listRf;
	}
	
	
	public List<String> getNameRfs() {
		return nameRfs;
	}
	public void setNameRfs(List<String> nameRfs) {
		this.nameRfs = nameRfs;
	}
	
	public List<String> getRangeRfs() {
		return rangeRfs;
	}
	public void setRangeRfs(List<String> rangeRfs) {
		this.rangeRfs = rangeRfs;
	}
	
}