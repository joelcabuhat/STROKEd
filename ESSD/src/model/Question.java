package model;

import javax.faces.bean.ManagedBean;

@ManagedBean
/**
 * @author Rhiza Mae G. Talavera
 * 
 * Structure for the questions of ROSIER system.
 */
public class Question {

	protected String question;
	protected String value;
	
	/**
	 * Constructor
	 */	
	public Question () {
		
	}
	
	/**
	 * Constructor with a string argument. Initialize attributes.
	 * @param q Question in ROSIER system.
	 * @param value True or false value.
	 */
	public Question (String q, String value) {
		this.question = q;
		this.value = value;
	}


	/**
	 * Getter for the questions in ROSIER system.
	 * @return Question selected by user in ROSIER.
	 */
	public String getQuestion() {
		return question;
	}

	
	/**
	 * Setter for the questions in ROSIER system.
	 * @param question Question selected by user in ROSIER.
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * Getter for the value (true or false) of the check box for a certain question in ROSIER.
	 * @return True or false value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Setter for the value (true or false) of the check box for a certain question in ROSIER.
	 * @param value True or false value.
	 */
	public void setValue(String value) {
		this.value = value;
	}
}