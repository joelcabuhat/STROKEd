package model;

import java.io.Serializable;

public class Probability implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Global Variables */
	private String label;
	private Object value;
	
	/*
	 * Constructor
	 */
	public Probability (String label) {
		this.label = label;
	}
	
	/*
	 * Constructor
	 */
	public Probability (Object value) {
		this.value = value;
	}
	
	/*
	 * Getter for probability label
	 */
	public String getLabel() {
		return label;
	}
	
	/*
	 * Setter for probability label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/*
	 * Getter for probability value
	 */
	public Object getValue() {
		return value;
	}
	
	/*
	 * Setter for probability value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}