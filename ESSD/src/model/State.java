package model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class State implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Global Variables */
	private String stateName;
	private double stateProb;

	/*
	 * Constructor
	 */
	public State () {
		
	}

	/*
	 * Getter for the state name
	 */
	public String getStateName() {
		return stateName;
	}

	/*
	 * Setter for the state name 
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/*
	 * Getter for the probability assigned to the state
	 */
	public double getStateProb() {
		return stateProb;
	}

	/*
	 * Setter for the probability assigned to the state
	 */
	public void setStateProb(double stateProb) {
		this.stateProb = stateProb;
	}
}
