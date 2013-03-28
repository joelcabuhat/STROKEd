package model;

public class StrokeType {

	/* Global Variables */
	String name, description;
	double probability;
	
	/*
	 * Constructor
	 */
	public StrokeType () {		
	}
	
	/*
	 * Constructor
	 */
	public StrokeType (String name, String description) {
		this.name = name;
		this.description = description;
	}

	/*
	 * Getter for the name of the stroke type
	 */
	public String getName() {
		return name;
	}

	/*
	 * Setter for the name of the stroke type
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * Getter for the description of the stroke type
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * Setter for the description of the stroke type
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * Getter for the probability associated with the stroke type
	 */
	public double getProbability() {
		return probability;
	}

	/*
	 * Setter for the probability associated with the stroke type
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
