package model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

/**
 * @author Arisa C. Ochavez
 *
 */
@ManagedBean
public class State implements Serializable {

	/**
	 * Global variables
	 */
	private static final long serialVersionUID = 1L;
	private String stateName;
	private double stateProb;

	/**
	 * Constructor
	 */
	public State () {
		
	}

	/**
	 * Getter for the state name
	 * @return stateName - name of the node's state
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * Setter for the state name 
	 * @param stateName - name of the node's state
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * Getter for the probability assigned to the state
	 * @return stateProb - probability associated to the state from the network
	 */
	public double getStateProb() {
		return stateProb;
	}

	/**
	 * Setter for the probability assigned to the state
	 * @param stateProb - probability associated to the state from the network
	 */
	public void setStateProb(double stateProb) {
		this.stateProb = stateProb;
	}
}
