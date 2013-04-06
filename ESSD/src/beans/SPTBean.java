package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import utilities.JdbcUtil;

import mapping.MapToClass;
import model.RiskFactor;
import model.RiskFactorDataModel;
import model.StrokeType;

/**
 * @author Arisa C. Ochavez
 * 
 * Backing bean for the Prediction Tool
 */
@ManagedBean
@SessionScoped
public class SPTBean implements Serializable {
	
	/**
	 * Global variables
	 */
	private static final long serialVersionUID = 1L;
	protected RiskFactor selectedRf;
	private List<RiskFactor> filteredRfs;
	protected static RiskFactor [] recSelected;
	private static RiskFactorDataModel rfDataModel;
	protected static List<RiskFactor> rf;
	protected static List<StrokeType> st;
	protected static RiskFactor [] selectedRfs;
	
	/**
	 * Constructor 
	 */
	public SPTBean () {
		
	}
	
	/**
	 * Getter for selected risk factor
	 * @return selectedRf - risk factor chosen by the user
	 */
	public RiskFactor getSelectedRf() {
		return selectedRf;
	}

	/**
	 * Setter for selected risk factor
	 * @param selectedRf - risk factor chosen by the user
	 */
	public void setSelectedRf(RiskFactor selectedRf) {
		this.selectedRf = selectedRf;
	}

	/**
	 * Getter for the list containing the risk factors in the network
	 * @return rf - list of risk factors
	 */
	public List<RiskFactor> getRf() {
		return rf;
	}

	/**
	 * Setter for the list containing the risk factors in the network
	 * @param rf - list of risk factors
	 */
	public void setRf(List<RiskFactor> rf) {
		SPTBean.rf = rf;
	}

	/**
	 * Getter for the filtered risk factors
	 * @return filteredRfs - list of risk factors trimmed down according to parameter
	 */
	public List<RiskFactor> getFilteredRfs() {
		return filteredRfs;
	}

	/**
	 * Setter for the filtered risk factors
	 * @param filteredRfs - list of risk factors trimmed down according to parameter
	 */
	public void setFilteredRfs(List<RiskFactor> filteredRfs) {
		this.filteredRfs = filteredRfs;
	}

	/**
	 * Getter for the list containing the different stroke types
	 * @return st - list of stroke types
	 */
	public List<StrokeType> getSt() {
		return st;
	}

	/**
	 * Setter for the list containing the different stroke types
	 * @param st - list of stroke types
	 */
	public void setSt(List<StrokeType> st) {
		SPTBean.st = st;
	}

	/**
	 * Getter for the array containing the selected risk factors from the data table
	 * @return selectedRfs - list of observed risk factors of the patient
	 */
	public RiskFactor [] getSelectedRfs() {
		return selectedRfs;
	}

	/**
	 * Setter for the array containing the selected risk factors from the data table
	 * @param selectedRfs - list of observed risk factors of the patient
	 */
	public void setSelectedRfs(RiskFactor [] selectedRfs) {
		SPTBean.selectedRfs = selectedRfs;
	}

	/**
	 * Getter for copy of selected risk factors before select/deselect
	 * @return recSelected - original list of observed risk factors before another select/deselect action
	 */
	public RiskFactor[] getRecSelected() {
		return recSelected;
	}

	/**
	 * Setter for copy of selected risk factors before select/deselect
	 * @param recSelected - original list of observed risk factors before another select/deselect action
	 */
	public static void setRecSelected(RiskFactor[] recSelected) {
		SPTBean.recSelected = recSelected;
	}

	/**
	 * Getter for the data model of the risk factors
	 * @return rfDataModel - data model of the list of risk factors
	 */
	public RiskFactorDataModel getRfDataModel() {
		return rfDataModel;
	}

	/**
	 * Setter for the data model of the risk factors
	 * @param rfDataModel - data model of the list of risk factors
	 */
	public void setRfDataModel(RiskFactorDataModel rfDataModel) {
		SPTBean.rfDataModel = rfDataModel;
	}

	/**
	 * Method for firing action upon selecting a risk factor
	 * @param se - event indicating a select action by the user
	 */
	public static void selectListener(SelectEvent se) {
		PatientInfoBean.flag = false;
		for (RiskFactor i: selectedRfs) {
			if(((RiskFactor)se.getObject()).getName().equals(i.getName()))
				i.setSelectedRange(i.getRangeValues().get(0));
		}
		updateData(true);
	}
	
	/**
	 * Method for firing action upon changing the value of a risk factor
	 * @param se - event indicating the change in value of the dropdown
	 */
	public void selectListener(AjaxBehaviorEvent se) {
		String selected  = (String) se.getComponent().getAttributes().get("value");
		
		if(PatientInfoBean.flag == true){
			for (RiskFactor i: selectedRfs) {
				String temp=(String) se.getComponent().getAttributes().get("label");
				if(temp.equals(i.getName()))
					i.setSelectedRange(selected);
			}		
		}
		
		updateData(true);
	}
	
	/**
	 * Method for firing action upon deselecting a risk factor
	 * @param se - event indicating unselect action by the user
	 */
	public void unselectListener(UnselectEvent se) {
		PatientInfoBean.flag = false;
		updateData(false);
	}
	
	/**
	 * Method to update probabilities after adding a node
	 */
	public static void addNodeUpdate () {
		//Update data
		InferenceBean.doInference(selectedRfs, st);
	}
	
	/**
	 * Method to remove deleted node in selected risk factors (if ever it was included)
	 * @param deletedNode - name of node to be removed from the list
	 */
	public static void removeDeleted (String deletedNode) {
		
		//Manipulate a dummy copy of the selected risk factors
		List<RiskFactor> dummy = new ArrayList<RiskFactor>();		
		Collections.addAll(dummy, selectedRfs);
		
		for (int i = 0; i < dummy.size(); i++) {
			if (deletedNode.equals(dummy.get(i).getName())) {
				dummy.remove(i);
				break;
			}
		}
		
		//Reflect changes in the selected risk factors
		selectedRfs = new RiskFactor[dummy.size()];
		selectedRfs = dummy.toArray(selectedRfs);
		recSelected = selectedRfs;
		
		//Update data
		InferenceBean.doInference(selectedRfs, st);
	}
	
	/**
	 * Method to initialize the prediction tool
	 */
	public static void start () {
		rf = new ArrayList<RiskFactor>();
		st = new ArrayList<StrokeType>();
		populateSPT(rf, st);
		rfDataModel = new RiskFactorDataModel(rf);
		RequestContext.getCurrentInstance().execute("loading.hide()");
	}
	
	/**
	 * Method to call to reset necessary values upon log out
	 */
	public static void resetSPT () {
		selectedRfs = null;
		recSelected = null;
	}

	/**
	 * Method that retrieves all the risk factors from the database and the different stroke types
	 * @param list - list of risk factors
	 * @param list2 - list of stroke types
	 */
	private static void populateSPT (List<RiskFactor> list, List<StrokeType> list2) {
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			//Get ALL Risk Factors
			ps = conn.prepareStatement("SELECT * FROM risk_factors");
			rs = ps.executeQuery();
			
			while(rs.next()){
				RiskFactor rf = new RiskFactor();
				list.add(MapToClass.mapRf(rf, rs));
			}
			
			//Get Stroke Types
			ps = conn.prepareStatement("SELECT * FROM stroke_types");
			rs = ps.executeQuery();
			
			while(rs.next()){
				StrokeType type = new StrokeType();
				list2.add(MapToClass.mapSt(type, rs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Internal Error:", "Error Occured, Please Refresh Page."));
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Internal Error:", "Error Occured, Please Refresh Page."));
			}
		}
	}
	
	/**
	 * Method for updating the probabilities of the risk factors and the stroke types
	 * @param action - true if update is needed to be performed for selecting a risk factor and false if it is for deselecting a risk factor
	 */
	public static void updateData (boolean action) {
		//Do Bayesian inference
		InferenceBean.doInference(selectedRfs, st);
		
		if (action) {
			//Iterate over the selected risk factors and set their probabilities to 100%
			for (RiskFactor i : selectedRfs) {
				
				int j = 0;
				for (j = 0; j < rf.size(); j++) {
					if (rf.get(j).getName().equals(i.getName()))
						break;
				}
				rf.get(j).setProbability(100);
				rf.get(j).setDisplay(false);
			}
		} else {
			//Iterate over the previously selected risk factors and identify which one was deselected
			String removed = null;
			boolean found = false;
			
			for (RiskFactor i: recSelected) {
				found = false;
				for (RiskFactor j: selectedRfs) {
					if (i.getName().equals(j.getName())) {
						found = true;
						break;
					}
				}
				
				if(!found) {
					removed = i.getName();
					break;
				}
			}
			
			//Get index of deselected risk factor in the list of risk factors
			int j = 0;
			for (j = 0; j < rf.size(); j++) {
				if (rf.get(j).getName().equals(removed))
					break;
			}
			
			//Reset the probability of the deselected risk factor
			if (rf.get(j).getRangeValues().isEmpty())
				rf.get(j).setProbability((int)((InferenceBean.getProbValue(removed, InferenceBean.with)*100)));
			else
				rf.get(j).setProbability((int)((InferenceBean.getProbValue(removed, rf.get(j).getSelectedRange())*100)));
			
			rf.get(j).setDisplay(true);
		}
		
		setRecSelected(selectedRfs);
	}
}
