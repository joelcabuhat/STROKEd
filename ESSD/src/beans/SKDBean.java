package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.model.DualListModel;

import utilities.JdbcUtil;

import mapping.MapToClass;
import model.Probability;
import model.RiskFactor;
import model.RiskFactorDataModel;
import model.State;
import model.StrokeType;

@ManagedBean
@SessionScoped
public class SKDBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Global Variables */
	private static final String disease = "Stroke";
	
		/* Populating the datable */
		private static List<RiskFactor> rfOfUser;
		private static RiskFactorDataModel rfDataModel;
		private List<RiskFactor> filteredRfs;
	
		/* Adding new risk factor */
		private String newNodeName, newNodeDescrip, newNodeHist, newNodeSS, newNodePE, newNodeLE, newNodeGM, newNodeSM;
		
		/* Editing/Deleting risk factors and stroke*/
		private RiskFactor selectedRf = new RiskFactor();
		private State selectedState, stateToEdit;
		private String dummy, dummy2;
		private List<String> origParents, origChildren, origStates;
		private DualListModel<String> parents;
		private DualListModel<String> children;
		private List<State> states, statesForEdit;
		private String newStateName;
		private double newStateProb;
		private List<StrokeType> strokeTypes, origTypes;
		private StrokeType selectedType;
		private String newTypeName, newTypeDescrip;
		
		/* GUI Manipulation and DynaForm */
		private Boolean editProb, editSave, editCancel, editPick, editConn, editStatesRow, editStates;
		private int startIdx, columnEndIdx, outputs;
		private DynaFormModel rfModel = new DynaFormModel();
		private DynaFormModel strokeModel = new DynaFormModel();
	
	/*
	 * Constructor
	 */
	public SKDBean () {
		
		//Initialize choices for parent and child nodes
		initPickList();
		
		//Initialize default states of new node
		initStates();
	}
	
	/*
	 * Getter for the list containing the risk factors in the network
	 * associated with the logged in user
	 */
	public List<RiskFactor> getRfOfUser() {
		return rfOfUser;
	}

	/*
	 * Setter for the list containing the risk factors in the network
	 * associated with the logged in user
	 */
	public void setRfOfUser(List<RiskFactor> rfOfUser) {
		SKDBean.rfOfUser = rfOfUser;
	}
	
	/*
	 * Getter for the data model of the selected risk factors associated with logged in user
	 */
	public RiskFactorDataModel getRfDataModel() {
		return rfDataModel;
	}

	/*
	 * Setter for the data model of the selected risk factors associated with logged in user
	 */
	public void setRfDataModel(RiskFactorDataModel rfDataModel) {
		SKDBean.rfDataModel = rfDataModel;
	}
	
	/*
	 * Getter for selected risk factor
	 */
	public RiskFactor getSelectedRf() {
		return selectedRf;
	}

	/*
	 * Setter for selected risk factor
	 */
	public void setSelectedRf(RiskFactor selectedRf) {
		this.selectedRf = selectedRf;
	}
	
	/*
	 * Getter for selected state
	 */
	public State getSelectedState() {
		return selectedState;
	}

	/*
	 * Setter for selected state
	 */
	public void setSelectedState(State selectedState) {
		this.selectedState = selectedState;
	}

	/*
	 * Getter for control for probability tab
	 */
	public Boolean getEditProb() {
		return editProb;
	}

	/*
	 * Setter for control for probability tab
	 */
	public void setEditProb(Boolean editProb) {
		this.editProb = editProb;
	}

	/*
	 * Getter for control for save button
	 */
	public Boolean getEditSave() {
		return editSave;
	}

	/*
	 * Setter for control for save button
	 */
	public void setEditSave(Boolean editSave) {
		this.editSave = editSave;
	}

	/*
	 * Getter for control for cancel button
	 */
	public Boolean getEditCancel() {
		return editCancel;
	}

	/*
	 * Setter for control for cancel button
	 */
	public void setEditCancel(Boolean editCancel) {
		this.editCancel = editCancel;
	}

	/*
	 * Getter for control for save changes in picklist
	 */
	public Boolean getEditPick() {
		return editPick;
	}

	/*
	 * Setter for control for save changes in picklist
	 */
	public void setEditPick(Boolean editPick) {
		this.editPick = editPick;
	}

	/*
	 * Getter for control for states tab
	 */
	public Boolean getEditStates() {
		return editStates;
	}

	/*
	 * Setter for control for states tab
	 */
	public void setEditStates(Boolean editStates) {
		this.editStates = editStates;
	}

	/*
	 * Getter for control for save changes in states
	 */
	public Boolean getEditStatesRow() {
		return editStatesRow;
	}

	/*
	 * Setter for control for save changes in states
	 */
	public void setEditStatesRow(Boolean editStatesRow) {
		this.editStatesRow = editStatesRow;
	}

	/*
	 * Getter for control for connections tab
	 */
	public Boolean getEditConn() {
		return editConn;
	}

	/*
	 * Setter for control for connections tab
	 */
	public void setEditConn(Boolean editConn) {
		this.editConn = editConn;
	}

	/*
	 * Getter for the filtered risk factors
	 */
	public List<RiskFactor> getFilteredRfs() {
		return filteredRfs;
	}

	/*
	 * Setter for the filtered risk factors
	 */
	public void setFilteredRfs(List<RiskFactor> filteredRfs) {
		this.filteredRfs = filteredRfs;
	}
	
	/*
	 * Getter for the list containing names of the probable parent risk factors
	 */
	public DualListModel<String> getParents() {
		return parents;
	}

	/*
	 * Setter for the list containing names of the probable parent risk factors
	 */
	public void setParents(DualListModel<String> parents) {
		this.parents = parents;
	}

	/*
	 * Getter for the list containing names of the probable child risk factors
	 */
	public DualListModel<String> getChildren() {
		return children;
	}

	/*
	 * Setter for the list containing names of the probable child risk factors
	 */
	public void setChildren(DualListModel<String> children) {
		this.children = children;
	}

	/*
	 * Getter for the list of states of the new node
	 */
	public List<State> getStates() {
		return states;
	}

	/*
	 * Setter for the list of states of the new node
	 */
	public void setStates(List<State> states) {
		this.states = states;
	}

	/*
	 * Getter for list of states of selected risk factor for editing
	 */
	public List<State> getStatesForEdit() {
		return statesForEdit;
	}

	/*
	 * Setter for list of states of selected risk factor for editing
	 */
	public void setStatesForEdit(List<State> statesForEdit) {
		this.statesForEdit = statesForEdit;
	}

	/*
	 * Getter for state for editing of selected risk factor
	 */
	public State getStateToEdit() {
		return stateToEdit;
	}

	/*
	 * Setter for state for editing of selected risk factor
	 */
	public void setStateToEdit(State stateToEdit) {
		this.stateToEdit = stateToEdit;
	}

	/*
	 * Getter for new state's name
	 */
	public String getNewStateName() {
		return newStateName;
	}

	/*
	 * Setter for new state's name
	 */
	public void setNewStateName(String newStateName) {
		this.newStateName = newStateName;
	}

	/*
	 * Getter for new state's probability
	 */
	public double getNewStateProb() {
		return newStateProb;
	}

	/*
	 * Setter for new state's probability
	 */
	public void setNewStateProb(double newStateProb) {
		this.newStateProb = newStateProb;
	}

	/*
	 * Getter for new node name
	 */
	public String getNewNodeName() {
		return newNodeName;
	}

	/*
	 * Setter for new node name
	 */
	public void setNewNodeName(String newNodeName) {
		this.newNodeName = newNodeName;
	}

	/*
	 * Getter for new node description
	 */
	public String getNewNodeDescrip() {
		return newNodeDescrip;
	}

	/*
	 * Setter for new node description
	 */
	public void setNewNodeDescrip(String newNodeDescrip) {
		this.newNodeDescrip = newNodeDescrip;
	}

	/*
	 * Getter for new node history
	 */
	public String getNewNodeHist() {
		return newNodeHist;
	}

	/*
	 * Setter for new node history
	 */
	public void setNewNodeHist(String newNodeHist) {
		this.newNodeHist = newNodeHist;
	}

	/*
	 * Getter for new node signs and symptoms
	 */
	public String getNewNodeSS() {
		return newNodeSS;
	}

	/*
	 * Setter for new node signs and symptoms
	 */
	public void setNewNodeSS(String newNodeSS) {
		this.newNodeSS = newNodeSS;
	}

	/*
	 * Getter for new node physical examinations
	 */
	public String getNewNodePE() {
		return newNodePE;
	}

	/*
	 * Setter for new node physical examinations
	 */
	public void setNewNodePE(String newNodePE) {
		this.newNodePE = newNodePE;
	}

	/*
	 * Getter for new node laboratory examinations
	 */
	public String getNewNodeLE() {
		return newNodeLE;
	}

	/*
	 * Setter for new node laboratory examinations
	 */
	public void setNewNodeLE(String newNodeLE) {
		this.newNodeLE = newNodeLE;
	}

	/*
	 * Getter for new node general measures
	 */
	public String getNewNodeGM() {
		return newNodeGM;
	}

	/*
	 * Setter for new node general measures
	 */
	public void setNewNodeGM(String newNodeGM) {
		this.newNodeGM = newNodeGM;
	}

	/*
	 * Getter for new node specific measures
	 */
	public String getNewNodeSM() {
		return newNodeSM;
	}

	/*
	 * Setter for new node specific measures
	 */
	public void setNewNodeSM(String newNodeSM) {
		this.newNodeSM = newNodeSM;
	}
	
	/*
	 * Getter for the dynamic form model for risk factor
	 */
	public DynaFormModel getRfModel() {
		return rfModel;
	}

	/*
	 * Setter for the dynamic form model for risk factor
	 */
	public void setRfModel(DynaFormModel rfModel) {
		this.rfModel = rfModel;
	}

	/*
	 * Getter for the dynamic form model for stroke
	 */
	public DynaFormModel getStrokeModel() {
		return strokeModel;
	}

	/*
	 * Setter for the dynamic form model for stroke
	 */
	public void setStrokeModel(DynaFormModel strokeModel) {
		this.strokeModel = strokeModel;
	}

	/*
	 * Getter for the set of types of stroke
	 */
	public List<StrokeType> getStrokeTypes() {
		return strokeTypes;
	}

	/*
	 * Setter for the set of types of stroke
	 */
	public void setStrokeTypes(List<StrokeType> strokeTypes) {
		this.strokeTypes = strokeTypes;
	}

	/*
	 * Getter for the selected type of stroke
	 */
	public StrokeType getSelectedType() {
		return selectedType;
	}

	/*
	 * Setter for the selected type of stroke
	 */
	public void setSelectedType(StrokeType selectedType) {
		this.selectedType = selectedType;
	}

	/*
	 * Getter for the new type for stroke
	 */
	public String getNewTypeName() {
		return newTypeName;
	}

	/*
	 * Setter for the new type for stroke
	 */
	public void setNewTypeName(String newTypeName) {
		this.newTypeName = newTypeName;
	}

	/*
	 * Getter for the description of new type for stroke
	 */
	public String getNewTypeDescrip() {
		return newTypeDescrip;
	}

	/*
	 * Setter for the description of new type for stroke
	 */
	public void setNewTypeDescrip(String newTypeDescrip) {
		this.newTypeDescrip = newTypeDescrip;
	}
	
	/*
	 * Getter for 2nd dummy variable
	 */
	public String getDummy2() {
		return dummy2;
	}

	/*
	 * Setter for 2nd dummy variable
	 */
	public void setDummy2(String dummy2) {
		this.dummy2 = dummy2;
	}

	/*
	 * Method to initialize datatables
	 */
	public static void start () {
		rfOfUser = new ArrayList<RiskFactor>();
		populateSKD(rfOfUser);
		rfDataModel = new RiskFactorDataModel(rfOfUser);
	}
	
	/************Adding New Risk Factor Methods*************/	
	/*
	 * Method to update the list of available risk factors once one is selected as parent
	 * in Add New Risk Factor
	 */
	public void onParentTransfer (TransferEvent event) {
		modifySource(event, children);
	}
	
	/*
	 * Method to update the list of available risk factors once one is selected as child
	 * in Add New Risk Factor
	 */
	public void onChildrenTransfer (TransferEvent event) {
		modifySource(event, parents);
	}
	
	/*
	 * Method to accept or reject adding of new node
	 */
	public void saveNewNode () {
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		//Reject if required fields are not filled up
		//Node name and list of states
		if (newNodeName.equals("") || states.size() < 2) {
			context.addMessage(null, new FacesMessage("Adding New Risk Factor Failed", "Error in Node Name and/or States."));
			RequestContext.getCurrentInstance().execute("addDialog.hide()");
			return;
		}
		
		//Check values of states
		double sum = 0;
		for (State i: states)
			sum = sum + i.getStateProb();
		
		//If probabilities are > 1 then reject
		if (sum > 1) {
			context.addMessage(null, new FacesMessage("Adding New Risk Factor Failed", "Error State Probabilities (> 1)."));
			RequestContext.getCurrentInstance().execute("addDialog.hide()");
			return;
		}
		
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			ps = conn.prepareStatement("SELECT rf_id FROM risk_factors WHERE name = ? ");
				ps.setString(1, newNodeName);
			rs = ps.executeQuery();
			
			//Node names should be unique
			if(rs.next()) {
				context.addMessage(null, new FacesMessage("Adding New Risk Factor Failed", "Node Name Already Exists."));
				RequestContext.getCurrentInstance().execute("addDialog.hide()");
				return;
			}
			
			//Add node to the network
			InferenceBean.addNode(newNodeName, parents.getTarget(), children.getTarget(), states);
			
			//Add node to the database
			ps = conn.prepareStatement("INSERT INTO risk_factors " +
											"(name, " +
											"description, " +
											"`range`, " +
											"history, " +
											"signs_symptoms, " +
											"physical_exams, " +
											"laboratory_exams, " +
											"general_measures, " +
											"specific_measures, " +
											"created_by, " +
											"last_update_by, " +
											"created_date, " +
											"last_update_date) " +
										"VALUES " +
											"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE(), SYSDATE())");
				ps.setString(1, newNodeName);
				ps.setString(2, newNodeDescrip);
				ps.setString(3, rangeToDb(states));
				ps.setString(4, newNodeHist);
				ps.setString(5, newNodeSS);
				ps.setString(6, newNodePE);
				ps.setString(7, newNodeLE);
				ps.setString(8, newNodeGM);
				ps.setString(9, newNodeSM);
				ps.setInt(10, AccountMgtBean.getUserId());
				ps.setInt(11, AccountMgtBean.getUserId());
			ps.executeUpdate();
			
			//Modify the database to reflect the affected nodes
			for(String i: children.getTarget()) {
				ps = conn.prepareStatement("UPDATE risk_factors SET to_edit = 'Yes' WHERE name = ? ");
					ps.setString(1, i);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Internal Error:", "Error Occured, Please Refresh Page."));
			RequestContext.getCurrentInstance().execute("addDialog.hide()");
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
		
		//Modify in current list of nodes
		resetValues();
		start();
		InferenceBean.start();
		SPTBean.start();
		SPTBean.addNodeUpdate();
		
		//Close dialog box on successful adding of node
		context.addMessage(null, new FacesMessage("Adding New Risk Factor Successful", "Risk Factor Added; Check Affected (Highlighted) Risk Factors."));
		RequestContext.getCurrentInstance().execute("addDialog.hide()");
	}
	
	/************Editing Risk Factor and Stroke Methods*************/
	/*
	 * Method to populate the attributes of the selected risk factor for editing
	 */
	public void populateEdit () {
		
		//Populate list of parents and children
		populatePickList(selectedRf.getName());
		
		//Get the state names of each node
		List<List<String>> stateNames = new ArrayList<List<String>>();		
		for (String i: parents.getTarget())
			stateNames.add(InferenceBean.retrieveStates(i));
		
		//Copy state names
		statesForEdit = new ArrayList<State>();
		State s;
		for (String i : origStates) {
			s = new State();
			s.setStateName(i);
			s.setStateProb(0.0);
			statesForEdit.add(s);
		}
		
		//Populate the probability table dynamically
		rfModel = new DynaFormModel();
		populateProbTable(rfModel, parents.getTarget(), stateNames, origStates, InferenceBean.retrieveProbs(selectedRf.getName()));
		
		//Retain old name of node
		dummy = selectedRf.getName(); //this is the OLD node name
		
		//Set UI components
		noChangesMade();
	}
	
	/*
	 * Method to populate the attributes of stroke for editing
	 */
	public void populateStrokeEdit () {
		StrokeType st;
		strokeTypes = new ArrayList<StrokeType>();
		origTypes = new ArrayList<StrokeType>();
		
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			ps = conn.prepareStatement("SELECT name, description " +
										"FROM stroke_types");
			rs = ps.executeQuery();
			
			while (rs.next()) {
				st = new StrokeType(rs.getString(1), rs.getString(2));
				
				//Populate Types Table
				strokeTypes.add(st);
				
				//Make copy of types
				origTypes.add(st);
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
		
		//Populate list of parents and children
		populatePickList(disease);
		
		//Get the state names of each node
		List<List<String>> stateNames = new ArrayList<List<String>>();		
		for (String i: parents.getTarget())
			stateNames.add(InferenceBean.retrieveStates(i));
				
		//Populate the probability table dynamically
		strokeModel = new DynaFormModel();
		populateProbTable(strokeModel, parents.getTarget(), stateNames, origStates, InferenceBean.retrieveProbs(disease));
		
		//Set UI Components
		noChangesMade();
	}
	
	/*
	 * Method that adds the new state in the existing set of states in Add new risk factor
	 */
	public void addState () {
		addNewState(newStateName, newStateProb, states);
		newStateName = "";
		newStateProb = 0.0;
	}
	
	/*
	 * Method that adds the new state in the existing set of states in Edit risk factor
	 */
	public void addStateEdit () {
		addNewState(dummy2, 0.0, statesForEdit);
		dummy2 = "";
		changesMadeStates();
	}
	
	/*
	 * Method that adds new type in existing set of types for stroke
	 */
	public void addType () {
		//If new type name is empty, prompt
		if (newTypeName == "") {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Add New Type Error:", "Type Name is Empty."));
			return;
		}
		
		//Type name should be unique
		for (StrokeType i : strokeTypes) {
			if (i.getName().equalsIgnoreCase(newTypeName)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Add New Type Error:", "Type Name should be Unique."));
				return;
			}
		}
		
		//Add new type to stroke
		strokeTypes.add(new StrokeType(newTypeName, newTypeDescrip));
		
		newTypeName = "";
		newTypeDescrip = "";
		
		//Set UI Components
		changesMadeStates();
	}
	
	/*
	 * Method that deletes a state in the existing set of states in Add new risk factor
	 */
	public void delState () {
		delSomeState(selectedState.getStateName(), states);
	}
	
	/*
	 * Method that deletes a state in the existing set of states in Edit risk factor
	 */
	public void delStateEdit () {
		delSomeState(stateToEdit.getStateName(), statesForEdit);
		changesMadeStates();
	}
	
	/*
	 * Method that deletes type in existing set of types for stroke
	 */
	public void delType () {
		for (int i = 0; i < strokeTypes.size(); i++) {
			if (strokeTypes.get(i).getName().equalsIgnoreCase(selectedType.getName())) {
				strokeTypes.remove(i);
				break;
			}
		}
		
		//Set UI Components
		changesMadeStates();
	}
	
	/*
	 * Method to update the list of available risk factors once one is selected as parent
	 * in Edit Risk Factor and Stroke Information
	 */
	public void onEditParentTransfer (TransferEvent event) {
		modifySource(event, children);
		changesMadeConn();
	}
	
	/*
	 * Method to update the list of available risk factors once one is selected as child
	 * in Edit Risk Factor and Stroke Information
	 */
	public void onEditChildrenTransfer (TransferEvent event) {
		modifySource(event, parents);		
		changesMadeConn();
	}
	
	/*
	 * Method to update UI for changes made in the editable datatable
	 */
	public void rowEdit (RowEditEvent event) {
		changesMadeStates();
	}
	
	/*
	 * Method to save changes in states in editing a risk factor
	 */
	public void saveChangesStates () {
		List<String> newStates = new ArrayList<String>();
		List<String> temp = new ArrayList<String>();
		
		//Have a copy of new set of states
		for (State i : statesForEdit) {
			newStates.add(i.getStateName());
			temp.add(i.getStateName());
		}
		
		//Change in network		
			//What remains is what was added
			newStates.removeAll(origStates);
			for (String i : newStates)
				InferenceBean.addThisState(selectedRf.getName(), i);
			
			//What remains is what was deleted
			origStates.removeAll(temp);
			for (String i : origStates)
				InferenceBean.delThisState(selectedRf.getName(), i);
		
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			//Enter new set of ranges in the database
			ps = conn.prepareStatement("UPDATE risk_factors SET " +
											"`range` = ? " +
											"WHERE name = ?");
				ps.setString(1, rangeToDb(statesForEdit));
				ps.setString(2, dummy);
			ps.executeUpdate();
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
		
		//Re-populate the dialog box
		populateEdit();
	}
	
	/*
	 * Method to save changes in types in stroke
	 */
	public void saveChangesType () {
		List<StrokeType> temp = new ArrayList<StrokeType>();
		
		//Make copy of new set of types
		temp.addAll(strokeTypes);
		
		//Change in Network and Database simultaneously
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("INSERT INTO stroke_types " +
											"(name, " +
											"description)" +
											"VALUES " +
											"(?, ?)");
			
			//What remains is what was added
			strokeTypes.removeAll(origTypes);
			for (StrokeType i : strokeTypes) {
				InferenceBean.addThisState(disease, i.getName());
				ps.setString(1, i.getName());
				ps.setString(2, i.getDescription());
				ps.executeUpdate();
			}
			
			ps = conn.prepareStatement("DELETE FROM stroke_types WHERE name = ?");
				
			//What remains is what was deleted
			origTypes.removeAll(temp);
			for (StrokeType i : origTypes) {
				InferenceBean.delThisState(disease, i.getName());
				ps.setString(1, i.getName());
				ps.executeUpdate();
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
		
		//Re-populate the dialog box
		populateStrokeEdit();
	}
	
	/*
	 * Method for saving changes in connections in editing a risk factor
	 */
	public void saveChangesConn () {
		
		saveConnections(selectedRf.getName());
		
		//Re-populate the dialog box
		populateEdit();
	}
	
	/*
	 * Method for saving changes in connections in editing stroke
	 */
	public void saveChangesConnStroke () {
		
		saveConnections(disease);
		
		//Re-populate dialog box
		populateStrokeEdit();
	}
	
	/*
	 * Method to accept or reject update of risk factor
	 */
	public void saveEdit () {
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		//Reject if required fields are not filled up
		//Node name
		if (selectedRf.getName().equals("")) {
			context.addMessage(null, new FacesMessage("Editing Risk Factor Failed", "Error in Node Name."));
			RequestContext.getCurrentInstance().execute("editDialog.hide()");
			return;
		}
		
		//Update name and probabilities in network
		//The parameters actually are OLD node name, NEW node name, NEW node definition
		InferenceBean.updateNode(dummy, selectedRf.getName(), accProbabilities(rfModel));
		
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			//Update node in database
			//Mark current node as already edited in the database
			ps = conn.prepareStatement("UPDATE risk_factors SET " +
											"name = ?, " +
											"to_edit = 'No', " +
											"description = ?, " +
											"history = ?, " +
											"signs_symptoms = ?, " +
											"physical_exams = ?, " +
											"laboratory_exams = ?, " +
											"general_measures = ?, " +
											"specific_measures = ?, " +
											"last_update_by = ?, " +
											"last_update_date = SYSDATE() " +
											"WHERE name = ?");
				ps.setString(1, selectedRf.getName());
				ps.setString(2, selectedRf.getDescription());
				ps.setString(3, selectedRf.getHistory());
				ps.setString(4, selectedRf.getSigns_symptoms());
				ps.setString(5, selectedRf.getPhysical_exams());
				ps.setString(6, selectedRf.getLaboratory_exams());
				ps.setString(7, selectedRf.getGeneral_measures());
				ps.setString(8, selectedRf.getSpecific_measures());
				ps.setInt(9, AccountMgtBean.getUserId());
				ps.setString(10, dummy);
			ps.executeUpdate();
			
			//Modify the database to reflect the affected child nodes
			for(String i: children.getTarget()) {
				ps = conn.prepareStatement("UPDATE risk_factors SET to_edit = 'Yes' WHERE name = ? ");
					ps.setString(1, i);
				ps.executeUpdate();
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
		
		//Modify in current list of nodes
		resetValues();
		statesForEdit.clear();
		start();
		InferenceBean.start();
		SPTBean.start();
		SPTBean.addNodeUpdate();
		
		//Close dialog box on successful update of node
		RequestContext.getCurrentInstance().execute("editDialog.hide()");
	}
	
	/*
	 * Method to accept or reject update of stroke
	 */
	public void saveStrokeEdit () {
		
		//Update probabilities in network
		//The parameters actually are OLD node name, NEW node name, NEW node definition
		InferenceBean.updateNodeProb(disease, accProbabilities(strokeModel));
		
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			//Modify the database to reflect the affected child nodes
			for(String i: children.getTarget()) {
				ps = conn.prepareStatement("UPDATE risk_factors SET to_edit = 'Yes' WHERE name = ? ");
					ps.setString(1, i);
				ps.executeUpdate();
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
		
		//Modify in current list of nodes
		resetValues();
		start();
		InferenceBean.start();
		SPTBean.start();
		SPTBean.addNodeUpdate();
		
		//Close dialog box on successful update of node
		RequestContext.getCurrentInstance().execute("editStrokeInfoDialog.hide()");
	}
	
	/************Deleting Risk Factor Methods*************/
	/*
	 * Method to populate the dummy string as an identifier of the node to be deleted
	 */
	public void populateDelete () {
		dummy = selectedRf.getName();
	}
	
	/*
	 * Method that removes node from the network
	 */
	public void deleteNode () {
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		//Ignore if there is no selected risk factor to delete
		if(selectedRf == null) {
			context.addMessage(null, new FacesMessage("Deleting Risk Factor Failed", "Error in Selected Risk Factor."));
			RequestContext.getCurrentInstance().execute("deleteDialog.hide()");
			return;
		}
		
		//Delete node from network
		List <String> update = InferenceBean.deleteNode(dummy);
		
		//Modify connected nodes to indicate that they need to be updated
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			//modify in the database
			for(String i: update) {
				ps = conn.prepareStatement("UPDATE risk_factors SET to_edit = 'Yes' WHERE name = ? ");
					ps.setString(1, i.replace('_', ' '));
				ps.executeUpdate();
			}			
				
			//delete node from database
			ps = conn.prepareStatement("DELETE FROM risk_factors WHERE name = ? ");
				ps.setString(1, dummy);
			ps.executeUpdate();
		
			//modify in current list of nodes
			initPickList();
			start();
			InferenceBean.start();
			SPTBean.start();
			SPTBean.removeDeleted(dummy);
			context.addMessage(null, new FacesMessage("Deleting Risk Factor Successful", "Risk Factor Deleted; Check Affected (Highlighted) Risk Factors."));
		} catch (SQLException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Internal Error:", "Error Occured, Please Refresh Page."));
			RequestContext.getCurrentInstance().execute("deleteDialog.hide()");
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
		
		//Close dialog box on successful delete
		RequestContext.getCurrentInstance().execute("deleteDialog.hide()");
	}
	
	/************Private Methods*************/
	/*
	 * Method to retrieve all risk factors associated to the current user
	 */
	private static void populateSKD (List<RiskFactor> list) {
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			//get Risk Factors made by the Current User
			ps = conn.prepareStatement("SELECT * FROM risk_factors WHERE created_by = ? ");
				ps.setInt(1, AccountMgtBean.getUserId());
			rs = ps.executeQuery();
			
			System.out.println("Hello User - " + AccountMgtBean.getUserId());
			
			while(rs.next()){
				RiskFactor rf = new RiskFactor();
				list.add(MapToClass.mapRf(rf, rs));
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
	
	/*
	 * Initialize default states
	 */
	private void initStates () {
		states = new ArrayList<State>();
		for (int i = 0; i < 2; i++) {
			State s = new State();
				if(i == 0)
					s.setStateName(InferenceBean.with);
				else
					s.setStateName(InferenceBean.without);
				s.setStateProb(0.5);
			states.add(s);
		}
	}
	
	/*
	 * Initialize Pick List
	 */
	private void initPickList () {
		//Initialize the probable parents of new node
		List<String> psource = new ArrayList<String>();
		List<String> ptarget = new ArrayList<String>();
		retrievePickList(psource);		
		psource.add(disease);	//Add stroke as one of the probable parents
		parents = new DualListModel<String>(psource, ptarget);
		
		//Initialize the probable children of the new node
		List<String> csource = new ArrayList<String>();
		List<String> ctarget = new ArrayList<String>();
		retrievePickList(csource);	
		csource.add(disease);	//Add stroke as one of the probable children
		children = new DualListModel<String>(csource, ctarget);
	}
	
	/*
	 * Method to populate picklist of possible parents and children
	 */
	private void populatePickList (String nodeName) {
		//Get parents and children of node
		List<String> ptarget = InferenceBean.retrieveParents(nodeName);
		List<String> ctarget = InferenceBean.retrieveChildren(nodeName);
		List<String> source = new ArrayList<String>();
				
		for (String i: parents.getSource()) {
			if (!ptarget.contains(i) && !ctarget.contains(i) && !nodeName.equals(i))
				source.add(i);
		}
		
		//Create picklist for parents and children
		parents = new DualListModel<String>(source, ptarget);
		children = new DualListModel<String>(source, ctarget);
		origParents = new ArrayList<String>();
		origChildren = new ArrayList<String>();
		
		//Populate the states
		origStates = InferenceBean.retrieveStates(nodeName);
		
		//Make copies of old values
		origParents.addAll(ptarget);
		origChildren.addAll(ctarget);
	}
	
	/*
	 * Method that will populate the probability table for editing
	 */
	private void populateProbTable (DynaFormModel model, List<String> parents, List<List<String>> pstates, List<String> curStates, List<Double> probs) {
		DynaFormRow row = null;  
		int colPerState = (int)(probs.size()/curStates.size());
		int statesColspan = 0;
		int tableDiv = 1;
		startIdx = 1;
		columnEndIdx = 1;
		outputs = 0;
		
		for (int i = 0; i < parents.size(); i++) {
			row =  model.createRegularRow();
			row.addControl((new Probability(parents.get(i))), "output", 1, 1);
			startIdx++;
			
			statesColspan = (int)(colPerState/(tableDiv*pstates.get(i).size()));
			for (int j = 0; j < tableDiv; j++) {
				for (int k = 0; k < pstates.get(i).size(); k++) {
					row.addControl((new Probability(pstates.get(i).get(k))), "output", statesColspan, 1);
					startIdx++;
				}
			}
			tableDiv = tableDiv*pstates.get(i).size();
			if (i == parents.size()-1)
				columnEndIdx = tableDiv;
		}
		
		int cnt = 0;
		for (int i = 0; i < curStates.size(); i++) {
			row =  model.createRegularRow();
			row.addControl((new Probability(curStates.get(i))), "output", 1, 1);
			
			for (int j = 0; j < colPerState; j++) {
				row.addControl((new Probability(probs.get(cnt))), "input", 1, 1);
				outputs++;
				cnt++;
			}
		}
	}
	
	/*
	 * Method that retrieves the list of risk factor names
	 */
	private void retrievePickList (List<String> list) {
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			//get ALL Risk Factor Names
			ps = conn.prepareStatement("SELECT name FROM risk_factors");
			rs = ps.executeQuery();
			
			while(rs.next())
				list.add(rs.getString(1));
			
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
	
	/*
	 * Method to modify the source of a picklist after a transfer
	 */
	private void modifySource(TransferEvent event, DualListModel<String> list) {
		if (event.isAdd()) {
			for(Object item : event.getItems()) {
				list.getSource().remove(item);
			}
		} else if (event.isRemove()) {
			for(Object item : event.getItems()) {
				list.getSource().add((String)item);
			}
		}
	}
	
	/*
	 * Method for doing calculations in adding new state
	 */
	private void addNewState (String name, double prob, List<State> list) {
		//If new state's probability is greater than 1, less than 0 or name is empty, then prompt
		if (prob >= 1.0 || prob < 0.0 || name.equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Add New State Error:", "Name or Probability Value is Invalid."));
			return;
		}
						
		//State names should be unique
		for (State i: list) {
			if (i.getStateName().equalsIgnoreCase(name)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Add New State Error:", "Name should be Unique."));
				return;
			}
		}
				
		//Adjust probabilities of other existing nodes if it exceeds 1
		double sum = 0;
		for (State i: list)
			sum = sum + i.getStateProb();
		sum = sum + prob;
				
		if (sum > 1) {
			//Get how much will we deduct from each existing state probability given the
			//probability of the new state that will be added
			double deduct = prob / list.size();
			
			//Subtract value from each existing state then
			sum = 0;
			for (State i: list) {
				i.setStateProb(i.getStateProb() - deduct);
				sum = sum + i.getStateProb();
			}
					
			//Update probability of new state that will be added
			prob = (1.0 - sum);
		} else if (sum < 1) {
			System.out.println("ADD - " + (prob + (1.0 - sum)));
			//Update probability of new state that will be added
			prob = (prob + (1.0 - sum));
		}
				
		//Add new state to existing list of states
		State s = new State ();
			s.setStateName(name);
			s.setStateProb(prob);
		list.add(0, s);
		
		name = "";
		prob = 0.0;
	}
	
	/*
	 * Method for deleting a specified state from the list
	 */
	private void delSomeState (String stateToDel, List<State> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getStateName().equalsIgnoreCase(stateToDel)) {
				list.remove(i);
				break;
			}
		}
	}
	
	/*
	 * Method to add/remove connections from parents/children of node
	 */
	private void saveConnections (String nodeName) {
		List<String> dummyList;
		Boolean err = false;
		
		//Remove connection from parents to node being edited in the network
			dummyList = new ArrayList<String>();
			dummyList.addAll(parents.getTarget());
			
			//What remains is what was added
			parents.getTarget().removeAll(origParents);
			for (String i : parents.getTarget())
				err = (err || InferenceBean.addArc(i, nodeName));
			
			//What remains is what was deleted
			origParents.removeAll(dummyList);
			for (String i : origParents)
				InferenceBean.delArc(i, nodeName);
		
		//Remove connection from node being edited to its children in the network
			dummyList.clear();
			dummyList.addAll(children.getTarget());
			
			//What remains is what was added
			children.getTarget().removeAll(origChildren);
			for (String i : children.getTarget())
				err = (err || InferenceBean.addArc(nodeName, i));
			
			//What remains is what was deleted
			origChildren.removeAll(dummyList);
			for (String i : origChildren)
				InferenceBean.delArc(nodeName, i);
		
		//Re-initialize Pick List of Parents and Children
		initPickList();
		
		//Prompt for success/error
		if(err)
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Changes Made:", "Some arc/s is/are not successfully created - results to a cycle."));
		else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Changes Made:", "Successfully saved changes made."));
	}
	
	/*
	 * Accumulate in a list all the probabilities associated with the node being edited
	 */
	private double [] accProbabilities (DynaFormModel model) {
		double [] definition = new double [outputs];
		int cnt = 0, idx = 0;
		
		for (int i = startIdx; i < model.getControls().size(); i++) {
			if (cnt == columnEndIdx) {
				cnt = 0;
				continue;
			}
			//System.out.println("NUM - " + model.getControls().get(i).getData().toString());
			definition[idx] = Double.parseDouble(((Probability) model.getControls().get(i).getData()).getValue().toString());
			idx++;
			cnt++;
		}
		
		return definition;
	}
	
	/*
	 * Method to convert the states into ranges suitable for the database
	 */
	private String rangeToDb (List<State> list) {
		
		//Format states -> state1/state2/state3/.../staten/
		String s = "";
		for (State i: list)
			s = s + i.getStateName() + "/";
		
		return s;
	}
	
	/*
	 * Reset values upon addition of node
	 */
	private void resetValues () {
		newNodeName = "";
		newNodeDescrip = "";
		newNodeHist = "";
		newNodeSS = "";
		newNodePE = "";
		newNodeLE = "";
		newNodeGM = "";
		newNodeSM = "";
		initStates();
		initPickList();
	}
	
	/************GUI Manipulation*************/
	/*
	 * Method to set boolean values for enabling/disabling UI components
	 * To normal values
	 */
	private void noChangesMade () {
		editStates = true;		//Render states tab
		editConn = true;		//Render connections tab
		editProb = true;		//Render probability tab
		editStatesRow = true;	//Disable save changes button for states
		editPick = true;		//Disable save changes button for connections
		editSave = false;		//Render save button
		editCancel = false;		//Render cancel button
	}
	
	/*
	 * Method to set boolean values for enabling/disabling UI components
	 * After changes are made in States Tab
	 */
	private void changesMadeStates () {
		editStates = true;		//Render states tab
		editConn = false;		//Disable connections tab
		editProb = false;		//Disable probability tab
		editStatesRow = false;	//Render save changes button for states
		editPick = true;		//Disable save changes button for connections
		editSave = true;		//Disable save button
		editCancel = true;		//Disable cancel button
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Changes Made:", "Please save changes to enable other tabs."));
	}
	
	/*
	 * Method to set boolean values for enabling/disabling UI components
	 * After changes are made in Connections Tab
	 */
	private void changesMadeConn () {
		editStates = false;		//Disable states tab
		editConn = true;		//Render connections tab
		editProb = false;		//Disable probability tab
		editStatesRow = true;	//Disable save changes button for states
		editPick = false;		//Render save changes button for connections
		editSave = true;		//Disable save button
		editCancel = true;		//Disable cancel button
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Changes Made:", "Please save changes to enable other tabs."));
	}
}
