package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

/**
 * @author Arisa C. Ochavez
 *
 * Model the attributes of a Risk Factor
 */
@ManagedBean
public class RiskFactor implements Serializable {
	
	/**
	 * Global variables
	 */
	private static final long serialVersionUID = 1L;
	private String rf_id, name, range, description, history, signs_symptoms, physical_exams, laboratory_exams, general_measures, specific_measures;
	private Date created_date, last_update_date;
	private int created_by, last_update_by, probability;
	private List<String> rangeValues;
	private String selectedRange;
	private boolean display, toEdit;
	
	/**
	 * Constructor
	 */
	public RiskFactor () {
	}

	/**
	 * Getter for the risk factor id assigned by the database
	 * @return rf_id - unique identifier for the risk factor
	 */
	public String getRf_id() {
		return rf_id;
	}

	/**
	 * Setter for the risk factor id assigned by the database
	 * @param rf_id - unique identifier for the risk factor
	 */
	public void setRf_id(String rf_id) {
		this.rf_id = rf_id;
	}

	/**
	 * Getter for the risk factor name
	 * @return name - term for the risk factor
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the risk factor name
	 * @param name - term for the risk factor
	 */
	public void setName(String name) {
		this.name = name;
	} 

	/**
	 * Getter for the valid ranges for the risk factor
	 * @return range - possible values that the risk factor may take on
	 */
	public String getRange() {
		return range;
	}

	/**
	 * Setter for the valid ranges for the risk factor
	 * @param range - possible values that the risk factor may take on
	 */
	public void setRange(String range) {
		this.range = range;
	}

	/**
	 * Getter for the description of the risk factor
	 * @return description - definition of the risk factor
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for the description of the risk factor
	 * @param description - definition of the risk factor
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for the history of the risk factor
	 * @return history - states where the risk factor originated
	 */
	public String getHistory() {
		return history;
	}

	/**
	 * Setter for the history of the risk factor
	 * @param history - states where the risk factor originated
	 */
	public void setHistory(String history) {
		this.history = history;
	}

	/**
	 * Getter for the signs and symptoms associated with the risk factor
	 * @return signs_symptoms - states the indicators of having a risk factor
	 */
	public String getSigns_symptoms() {
		return signs_symptoms;
	}

	/**
	 * Setter for the signs and symptoms associated with the risk factor
	 * @param signs_symptoms - states the indicators of having a risk factor
	 */
	public void setSigns_symptoms(String signs_symptoms) {
		this.signs_symptoms = signs_symptoms;
	}

	/**
	 * Getter for the physical examinations associated with the risk factor
	 * @return physical_exams - states the possible physical examinations to check for the risk factor
	 */
	public String getPhysical_exams() {
		return physical_exams;
	}

	/**
	 * Setter for the physical examinations associated with the risk factor
	 * @param physical_exams - states the possible physical examinations to check for the risk factor
	 */
	public void setPhysical_exams(String physical_exams) {
		this.physical_exams = physical_exams;
	}

	/**
	 * Getter for the laboratory examinations associated with the risk factor
	 * @return laboratory_exams - states the possible laboratory examinations to check for the risk factor
	 */
	public String getLaboratory_exams() {
		return laboratory_exams;
	}

	/**
	 * Setter for the laboratory examinations associated with the risk factor
	 * @param laboratory_exams - states the possible laboratory examinations to check for the risk factor
	 */
	public void setLaboratory_exams(String laboratory_exams) {
		this.laboratory_exams = laboratory_exams;
	}

	/**
	 * Getter for the general measures associated with the risk factor
	 * @return general_measures - states the possible general measures to be taken with respect to the risk factor
	 */
	public String getGeneral_measures() {
		return general_measures;
	}

	/**
	 * Setter for the general measures associated with the risk factor
	 * @param general_measures - states the possible general measures to be taken with respect to the risk factor
	 */
	public void setGeneral_measures(String general_measures) {
		this.general_measures = general_measures;
	}

	/**
	 * Getter for the specific measures associated with the risk factor
	 * @return specific_measures - states the possible specific measures to be taken with respect to the risk factor
	 */
	public String getSpecific_measures() {
		return specific_measures;
	}

	/**
	 * Setter for the specific measures associated with the risk factor
	 * @param specific_measures - states the possible specific measures to be taken with respect to the risk factor
	 */
	public void setSpecific_measures(String specific_measures) {
		this.specific_measures = specific_measures;
	}

	/**
	 * Getter for the date when the risk factor was added to the network
	 * @return created_date - date when risk factor was added
	 */
	public Date getCreated_date() {
		return created_date;
	}

	/**
	 * Setter for the date when the risk factor was added to the network
	 * @param created_date - date when risk factor was added
	 */
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	/**
	 * Getter for the date when the risk factor was last modified
	 * @return last_update_date - date when risk factor was last modified
	 */
	public Date getLast_update_date() {
		return last_update_date;
	}

	/**
	 * Setter for the date when the risk factor was last modified
	 * @param last_update_date - date when risk factor was last modified
	 */
	public void setLast_update_date(Date last_update_date) {
		this.last_update_date = last_update_date;
	}

	/**
	 * Getter for the user id of who added the risk factor in the network
	 * @return created_by - identifier of the one who inserted the risk factor in the network
	 */
	public int getCreated_by() {
		return created_by;
	}

	/**
	 * Setter for the user id of who added the risk factor in the network
	 * @param created_by - identifier of the one who inserted the risk factor in the network
	 */
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	/**
	 * Getter for the user id of who last updated the risk factor
	 * @return last_update_by - identifier of the one who last modified the risk factor
	 */
	public int getLast_update_by() {
		return last_update_by;
	}

	/**
	 * Setter for the user id of who last updated the risk factor
	 * @param last_update_by - identifier of the one who last modified the risk factor
	 */
	public void setLast_update_by(int last_update_by) {
		this.last_update_by = last_update_by;
	}
	
	/**
	 * Getter for the list containing the valid ranges of the risk factor
	 * @return rangeValues - list of values that the risk factor may take on
	 */
	public List<String> getRangeValues() {
		return rangeValues;
	}

	/**
	 * Setter for the list containing the valid ranges of the risk factor
	 * @param rangeValues - list of values that the risk factor may take on
	 */
	public void setRangeValues() {
		rangeValues = new ArrayList<String> ();
		
		int start = 0;
		//Populate list by separating the range values via '/'
		for (int end = 0; end < range.length(); end++) {
			if (range.charAt(end) == '/') {
				rangeValues.add(range.substring(start, end));
				start = end + 1;
				continue;
			}
		}
	}

	/**
	 * Getter for the selected range
	 * @return selectedRange - observed value of the risk factor 
	 */
	public String getSelectedRange() {
		return selectedRange;
	}

	/**
	 * Setter for the selected range
	 * @param selectedRange - observed value of the risk factor 
	 */
	public void setSelectedRange(String selectedRange) {
		this.selectedRange = selectedRange;
	}

	/**
	 * Getter for the probability associated with the risk factor
	 * @return probability - value of the risk factor from the truth table
	 */
	public int getProbability() {
		return probability;
	}

	/**
	 * Setter for the probability associated with the risk factor
	 * @param probability - value of the risk factor from the truth table
	 */
	public void setProbability(int probability) {
		this.probability = probability;
	}

	/**
	 * Getter for the value of display - if the dropdown box of the range should be displayed or not
	 * @return display - true if value of the risk factor is modifiable, otherwise, false
	 */
	public boolean isDisplay() {
		return display;
	}

	/**
	 * Setter for the value of display - if the dropdown box of the range should be displayed or not
	 * @param display - true if value of the risk factor is modifiable, otherwise, false
	 */
	public void setDisplay(boolean display) {
		this.display = display;
	}

	/**
	 * Getter for the need for editing of the risk factor
	 * @return toEdit - true if risk factor should be edited, otherwise, false
	 */
	public boolean isToEdit() {
		return toEdit;
	}

	/**
	 * Setter for the need for editing of the risk factor
	 * @param toEdit - true if risk factor should be edited, otherwise, false
	 */
	public void setToEdit(boolean toEdit) {
		this.toEdit = toEdit;
	}
}
