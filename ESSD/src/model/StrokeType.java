package model;

/**
 * @author Arisa C. Ochavez
 *
 * Model attributes of stroke types
 */
public class StrokeType {

	/**
	 * Global variables 
	 */
	String name, description;
	double probability;
	
	/**
	 * Constructor 
	 */
	public StrokeType () {		
	}
	
	/**
	 * Constructor
	 * 
	 * @param name - term for the stroke type
	 * @param description - definition of the stroke type
	 */
	public StrokeType (String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * Getter for the name of the stroke type
	 * @return name - term for the stroke type
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the name of the stroke type
	 * @param name - term for the stroke type
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for the description of the stroke type
	 * @return description - definition of the stroke type
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for the description of the stroke type
	 * @param description - definition of the stroke type
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for the probability associated with the stroke type
	 * @return probability - value of stroke type from the truth table
	 */
	public double getProbability() {
		return probability;
	}

	/**
	 * Setter for the probability associated with the stroke type
	 * @param probability - value of the stroke type from the truth table
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
