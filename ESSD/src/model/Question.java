package model;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Question {

	protected String question;
	protected String value;
	
	/*
	 * Constructor
	 */	
	public Question () {
		
	}
	
	/*
	 * Constructor with a string argument
	 */
	public Question (String q, String value) {
		this.question = q;
		this.value = value;
	}

	/*
	 * Getter for the question
	 */
	public String getQuestion() {
		return question;
	}

	/*
	 * Setter for the question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/*
	 * Getter for the value of the checkbox in Rosier table
	 */
	public String getValue() {
		return value;
	}
	/*
	 * Setter for the value of the checkbox in Rosier table
	 */
	public void setValue(String value) {
		this.value = value;
	}
}