package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
/**
 * Model for patient attributes.
 *
 */
@ManagedBean
public class Patient implements Serializable {

	private static final long serialVersionUID = 1L;
	

	protected String complaint, caseNumStr;
	private int caseNum, doctor;
	protected String rosDiagnosis;
	protected int rosScore;
	protected List<String> rosQuestions=new ArrayList<String>();
	protected double probHemorrhagic, probIschemic, probNone;
	protected List<String> listRf=new ArrayList<String>();
	protected List<String> nameRfs=new ArrayList<String>();
	protected List<String> rangeRfs=new ArrayList<String>();
	
	
	/**
	 * Patient model constructor.
	 */
	public Patient () {
		
	}
	
	

	
	/**
	 * Getter for patient's complaint.
	 * @return Complaint of patient.
	 */
	public String getComplaint() {
		return complaint;
	}


	/**
	 * Setter for patient's complaint.
	 * @param complaint Complaint of patient.
	 */
	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}
	
	/**
	* Getter for patient's case number in string.
	* @return Case number of patient in string.
	*/
	public String getCaseNumStr() {
		return caseNumStr;
	}
	/**
	 * Setter for patient's case number in string.
	 * @param caseNumStr Case number in string of patient.
	 */
	public void setCaseNumStr(String caseNumStr) {
		this.caseNumStr = caseNumStr;
	}
	
	
	/**
	 * Getter for patient's doctor.
	 * @return Patient's doctor.
	 */
	public int getDoctor() {
		return doctor;
	}
	/**
	 * Setter for patient's doctor.
	 * @param doctor Patient's doctor.
	 */
	public void setDoctor(int doctor) {
		this.doctor = doctor;
	}
	/**
	 * Getter for patient's case number.
	 * @return Case number of patients.
	 */
	public int getCaseNum() {
		return caseNum;
	}
	/**
	 * Setter for patient's case number.
	 * @param caseNum Case number of patients.
	 */
	public void setCaseNum(int caseNum) {
		this.caseNum = caseNum;
	}
	/**
	 * Getter for ROSIER diagnosis.
	 * @return Diagnosis of ROSIER.
	 */
	public String getRosDiagnosis() {
		return rosDiagnosis;
	}
	/**
	 * Setter for ROSIER diagnosis.
	 * @param rosDiagnosis Diagnosis of ROSIER.
	 */
	public void setRosDiagnosis(String rosDiagnosis) {
		this.rosDiagnosis = rosDiagnosis;
	}
	/**
	 * Getter of score for ROSIER scoring system.
	 * @return ROSIER score.
	 */
	public int getRosScore() {
		return rosScore;
	}
	/**
	 * Setter of score for ROSIER scoring system.
	 * @param rosScore ROSIER score.
	 */
	public void setRosScore(int rosScore) {
		this.rosScore = rosScore;
	}
	/**
	 * Getter for the probability of hemorrhagic stroke.
	 * @return Probability of hemorrhagic stroke. 
	 */
	public double getProbHemorrhagic() {
		return probHemorrhagic;
	}
	/**
	 * Setter for the probability of hemorrhagic stroke.
	 * @param probHemorrhagic Probability of hemorrhagic stroke.
	 */
	public void setProbHemorrhagic(double probHemorrhagic) {
		this.probHemorrhagic = probHemorrhagic;
	}
	/**
	 * Getter for the probability of ischemic stroke.
	 * @return Probability of ischemic stroke. 
	 */
	public double getProbIschemic() {
		return probIschemic;
	}
	/**
	 * Setter for the probability of ischemic stroke.
	 * @param probIschemic Probability of ischemic stroke.
	 */
	public void setProbIschemic(double probIschemic) {
		this.probIschemic = probIschemic;
	}
	/**
	 * Getter for the probability of having no stroke.
	 * @return Probability of no stroke attack. 
	 */
	public double getProbNone() {
		return probNone;
	}
	/**
	 * Setter for the probability of having no stroke.
	 * @param probNone Probability of having no stroke.
	 */
	public void setProbNone(double probNone) {
		this.probNone = probNone;
	}
	/**
	 * Getter for ROSIER questions. 
	 * @return ROSIER questions.
	 */
	public List<String> getRosQuestions() {
		return rosQuestions;
	}
	
	/**
	 * Setter for ROSIER questions.
	 * @param rosQuestions ROSIER questions.
	 */
	public void setRosQuestions(List<String> rosQuestions) {
		this.rosQuestions = rosQuestions;
	}
	/**
	 * Getter for list of risk factors.
	 * @return List of risk factors.
	 */
	public List<String> getListRf() {
		return listRf;
	}
	/**
	 * Setter for list of risk factors.
	 * @param listRf List of risk factors.
	 */
	public void setListRf(List<String> listRf) {
		this.listRf = listRf;
	}
	/**
	 * Getter for the list of names of risk factors.
	 * @return List of names of risk factors.
	 */
	public List<String> getNameRfs() {
		return nameRfs;
	}
	/**
	 * Setter for the list of names of risk factors.
	 * @param nameRfs Names of risk factors.
	 */
	public void setNameRfs(List<String> nameRfs) {
		this.nameRfs = nameRfs;
	}
	/**
	 * Getter for the list of range of selected risk factors.
	 * @return List of range of selected risk factors.
	 */
	public List<String> getRangeRfs() {
		return rangeRfs;
	}
	/**
	 * Setter for the list of range of selected risk factors.
	 * @param rangeRfs List of range of selected risk factors.
	 */
	public void setRangeRfs(List<String> rangeRfs) {
		this.rangeRfs = rangeRfs;
	}
	
}