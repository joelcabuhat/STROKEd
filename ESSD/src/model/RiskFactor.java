package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class RiskFactor implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Global Variables */
	private String rf_id, name, range, description, history, signs_symptoms, physical_exams, laboratory_exams, general_measures, specific_measures;
	private Date created_date, last_update_date;
	private int created_by, last_update_by, probability;
	private List<String> rangeValues;
	private String selectedRange;
	private boolean display, toEdit;
	
	/*
	 * Constructor
	 */
	public RiskFactor () {
	}

	/*
	 * Getter for the risk factor id assigned by the database
	 */
	public String getRf_id() {
		return rf_id;
	}

	/*
	 * Setter for the risk factor id assigned by the database
	 */
	public void setRf_id(String rf_id) {
		this.rf_id = rf_id;
	}

	/*
	 * Getter for the risk factor name
	 */
	public String getName() {
		return name;
	}

	/*
	 * Setter for the risk factor name
	 */
	public void setName(String name) {
		this.name = name;
	} 

	/*
	 * Getter for the valid ranges for the risk factor
	 */
	public String getRange() {
		return range;
	}

	/*
	 * Setter for the valid ranges for the risk factor
	 */
	public void setRange(String range) {
		this.range = range;
	}

	/*
	 * Getter for the description of the risk factor
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * Setter for the description of the risk factor
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * Getter for the history of the risk factor
	 */
	public String getHistory() {
		return history;
	}

	/*
	 * Setter for the history of the risk factor
	 */
	public void setHistory(String history) {
		this.history = history;
	}

	/*
	 * Getter for the signs and symptoms associated with the risk factor
	 */
	public String getSigns_symptoms() {
		return signs_symptoms;
	}

	/*
	 * Setter for the signs and symptoms associated with the risk factor
	 */
	public void setSigns_symptoms(String signs_symptoms) {
		this.signs_symptoms = signs_symptoms;
	}

	/*
	 * Getter for the physical examinations associated with the risk factor
	 */
	public String getPhysical_exams() {
		return physical_exams;
	}

	/*
	 * Setter for the physical examinations associated with the risk factor
	 */
	public void setPhysical_exams(String physical_exams) {
		this.physical_exams = physical_exams;
	}

	/*
	 * Getter for the laboratory examinations associated with the risk factor
	 */
	public String getLaboratory_exams() {
		return laboratory_exams;
	}

	/*
	 * Setter for the laboratory examinations associated with the risk factor
	 */
	public void setLaboratory_exams(String laboratory_exams) {
		this.laboratory_exams = laboratory_exams;
	}

	/*
	 * Getter for the general measures associated with the risk factor
	 */
	public String getGeneral_measures() {
		return general_measures;
	}

	/*
	 * Setter for the general measures associated with the risk factor
	 */
	public void setGeneral_measures(String general_measures) {
		this.general_measures = general_measures;
	}

	/*
	 * Getter for the specific measures associated with the risk factor
	 */
	public String getSpecific_measures() {
		return specific_measures;
	}

	/*
	 * Setter for the specific measures associated with the risk factor
	 */
	public void setSpecific_measures(String specific_measures) {
		this.specific_measures = specific_measures;
	}

	/*
	 * Getter for the date when the risk factor was added to the network
	 */
	public Date getCreated_date() {
		return created_date;
	}

	/*
	 * Setter for the date when the risk factor was added to the network
	 */
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	/*
	 * Getter for the date when the risk factor was last updated
	 */
	public Date getLast_update_date() {
		return last_update_date;
	}

	/*
	 * Setter for the date when the risk factor was last updated
	 */
	public void setLast_update_date(Date last_update_date) {
		this.last_update_date = last_update_date;
	}

	/*
	 * Getter for the user id of who added the risk factor in the network
	 */
	public int getCreated_by() {
		return created_by;
	}

	/*
	 * Setter for the user id of who added the risk factor in the network
	 */
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	/*
	 * Getter for the user id of who last updated the risk factor
	 */
	public int getLast_update_by() {
		return last_update_by;
	}

	/*
	 * Setter for the user id of who last updated the risk factor
	 */
	public void setLast_update_by(int last_update_by) {
		this.last_update_by = last_update_by;
	}
	
	/*
	 * Getter for the list containing the valid ranges of the risk factor
	 */
	public List<String> getRangeValues() {
		return rangeValues;
	}

	/*
	 * Setter for the list containing the valid ranges of the risk factor
	 * separated by a '/' in the string variable range
	 */
	public void setRangeValues() {
		rangeValues = new ArrayList<String> ();
		
		int start = 0;
		for (int end = 0; end < range.length(); end++) {
			if (range.charAt(end) == '/') {
				rangeValues.add(range.substring(start, end));
				start = end + 1;
				continue;
			}
		}
	}

	/*
	 * Getter for the selected range
	 */
	public String getSelectedRange() {
		return selectedRange;
	}

	/*
	 * Setter for the selected range
	 */
	public void setSelectedRange(String selectedRange) {
		this.selectedRange = selectedRange;
	}

	/*
	 * Getter for the probability associated with the risk factor
	 */
	public int getProbability() {
		return probability;
	}

	/*
	 * Setter for the probability associated with the risk factor
	 */
	public void setProbability(int probability) {
		this.probability = probability;
	}

	/*
	 * Getter for the value of display - if the dropdown box of the range should be displayed or not
	 */
	public boolean isDisplay() {
		return display;
	}

	/*
	 * Setter for the value of display
	 */
	public void setDisplay(boolean display) {
		this.display = display;
	}

	/*
	 * Getter if it needs editing
	 */
	public boolean isToEdit() {
		return toEdit;
	}

	/*
	 * Setter if it needs editing
	 */
	public void setToEdit(boolean toEdit) {
		this.toEdit = toEdit;
	}
}
