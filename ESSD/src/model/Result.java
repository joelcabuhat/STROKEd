package model;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Result {

	private int score;
	private String diagnosis;
	private String description;

	public Result () {		
	}
	public Result(String diagnosis,int score, String description) {
		this.score = score;
		this.diagnosis=diagnosis;
		this.description=description;
	}
	/*
	 * Getter for the score of Rosier output
	 */
	public int getScore() {
		return score;
	}
	/*
	 * Setter for the score of Rosier output
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/*
	 * Getter for the diagnosis associated with Rosier's output score
	 */
	public String getDiagnosis() {
		return diagnosis;
	}
	/*
	 * Setter for the diagnosis associated with Rosier's output score
	 */
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	/*
	 * Getter for the description of Rosier's diagnosis
	 */
	public String getDescription() {
		return description;
	}
	/*
	 * Setter for the description of Rosier's diagnosis
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}