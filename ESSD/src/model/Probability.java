package model;

import java.io.Serializable;

/**
 * @author Arisa C. Ochavez
 *
 */
public class Probability implements Serializable {

	/**
	 * Global variables
	 */
	private static final long serialVersionUID = 1L;
	private String label;
	private Object value;
	
	/**
	 * Constructor
	 * @param label - identifies the probability
	 */
	public Probability (String label) {
		this.label = label;
	}
	
	/**
	 * Constructor
	 * @param value - the actual value of the probability from the truth table
	 */
	public Probability (Object value) {
		this.value = value;
	}
	
	/**
	 * Getter for the label of the probability
	 * @return label - identifies the probability
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Setter for the label of the probability
	 * @param label - identifies the probability
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Getter for the probability value
	 * @return value - the actual value of the probability from the truth table
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Setter for the probability value
	 * @param value - the actual value of the probability from the truth table
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}