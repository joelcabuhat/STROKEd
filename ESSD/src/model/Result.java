package model;

import javax.faces.bean.ManagedBean;

@ManagedBean
/**
 * Model for the result/output of ROSIER scoring system.
 *
 */
public class Result {

	private int score;
	private String diagnosis;
	private String description;
	/**
	 * Constructor.
	 */
	public Result () {		
	}
	
	/**
	 * Constructor with parameter data.
	 * @param diagnosis Diagnosis for ROSIER result.
	 * @param score Score of ROSIER system.
	 * @param description Description of the diagnosis of ROSIER.
	 */
	public Result(String diagnosis,int score, String description) {
		this.score = score;
		this.diagnosis=diagnosis;
		this.description=description;
	}

	/**
	 * Getter for the score of ROSIER output.
	 * @return Score of ROSIER.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Setter for the score of ROSIER output.
	 * @param score Score of ROSIER.
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Getter for ROSIER diagnosis.
	 * @return ROSIER diagnosis.
	 */
	public String getDiagnosis() {
		return diagnosis;
	}

	/**
	 * Setter for ROSIER diagnosis.
	 * @param diagnosis ROSIER diagnosis.
	 */
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	/**
	 * Getter for the description of ROSIER's diagnosis.
	 * @return Description of ROSIER's diagnosis.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for the description of ROSIER's diagnosis.
	 * @param description Description of ROSIER's diagnosis.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}