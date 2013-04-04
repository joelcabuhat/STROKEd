package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.RiskFactor;
import model.State;
import model.StrokeType;

import smile.Network;
import smile.SMILEException;

/**
 * @author Arisa C. Ochavez
 *
 * Connects the application to the Bayesian Network through the functionalities of the jSMILE library
 */
@ManagedBean
@SessionScoped
public class InferenceBean implements Serializable {

	/**
	 * Global variables
	 */
	private static final long serialVersionUID = 1L;
	//To change the network to be read, change networkFile variable
	//and align it with the database
	private static final String networkFile = "ESSD_Files/System_Network/STROKEd_Network.xdsl";
	private static final Network net = new Network();
	public static final String with = "Present";
	public static final String without = "Absent";
	public static final String normal = "None";
	
	/**
	 * Constructor 
	 */
	public InferenceBean () {
		
	}
	
	/**
	 * Open the Bayesian network file 
	 */
	public static void start () {
		net.readFile(networkFile);
	}
	
	/**
	 * Perform Bayesian inference by setting selected risk factors with their respective range values as evidences
	 * 
	 * @param selectedRfs - list of observed risk factors as selected by the user
	 * @param st - list of stroke types
	 * @throws SMILEException
	 */
	public static void doInference (RiskFactor[] selectedRfs, List<StrokeType> st) throws SMILEException {
		String nodeName;
		
		//Clear all previous evidences - reset network to its initial state
		net.clearAllEvidence();
		
		//Read current network from file
		net.updateBeliefs();
	
		//Iterate over the selected risk factors to be able to modify the network accordingly
		for (RiskFactor rf : selectedRfs) {
			nodeName = rf.getName().replace(' ', '_');		
			System.out.println("NETWORKRANGE: "+rf.getSelectedRange());
			net.setEvidence(nodeName, rf.getSelectedRange().replace(' ', '_'));	
		}
		
		//Update network after setting the evidences found
		net.updateBeliefs();
	
		//Update the probabilities of each type of stroke to be displayed
		double [] typeValues = net.getNodeValue("Stroke");
		
		for (StrokeType i : st)
			i.setProbability(typeValues[getIndex(net, "Stroke", i.getName().replace(' ', '_'))]);
	}
	
	/**
	 * Method for retrieving the probability value from the Bayesian network 
	 * 
	 * @param nodeName - name of the node where to retrieve the probability value from
	 * @param stateName - name of the node's state where to retrieve the probability value from
	 * @return probability value (double data type)
	 * @throws SMILEException
	 */
	public static double getProbValue (String nodeName, String stateName) throws SMILEException {
		
		//Reset network to its initial state
		net.clearAllEvidence();
		
		double [] typeValues = null;
		nodeName = nodeName.replace(' ', '_');
		stateName = stateName.replace(' ', '_');

		//Read current network from file
		net.updateBeliefs();
		
		typeValues = net.getNodeValue(nodeName);
		
		return typeValues[getIndex(net, nodeName, stateName)];
	}
	
	/**
	 * Add (insert) new node to the Bayesian network
	 * 
	 * @param nodeName - name of the node to be added
	 * @param parents - antecedent of the new node
	 * @param children - precedent of the new node
	 * @param states - state names (range values) of the new node
	 * @throws SMILEException
	 */
	public static void addNode (String nodeName, List<String> parents, List<String> children, List<State> states) throws SMILEException {
		
		nodeName = nodeName.replace(' ', '_');
		
		//Read current network from file
		net.updateBeliefs();
		
		//Create new node
		net.addNode(Network.NodeType.Cpt, nodeName);
		
		//Set states
		double [] definition = new double [states.size()];
		for (int i = 0; i < states.size(); i++) {
			if (i > 1)
				net.addOutcome(nodeName, states.get(i).getStateName().replace(' ', '_'));
			else
				net.setOutcomeId(nodeName, i, states.get(i).getStateName().replace(' ', '_'));
			
			definition[i] = states.get(i).getStateProb();
		}
		net.setNodeDefinition(nodeName, definition);
		
		//Set up connection from parents
		for(String i: parents) 
			net.addArc(i.replace(' ', '_'), nodeName);
		
		//Set up connection to children
		for(String i: children)
			net.addArc(nodeName, i.replace(' ', '_'));
		
		//Update the current network
		net.updateBeliefs();
		net.writeFile(networkFile);
	}
	
	/**
	 * Remove the specified node from the Bayesian Network
	 * 
	 * @param nodeName - name of the node to be deleted
	 * @return ret - list of connections to the deleted node
	 * @throws SMILEException
	 */
	public static List<String> deleteNode (String nodeName) throws SMILEException {
		
		List<String> ret = new ArrayList<String>();
		nodeName = nodeName.replace(' ', '_');
		
		//Read current network from file
		net.updateBeliefs();
		
		//Get names of nodes connected to the node that is about to be deleted
		Collections.addAll(ret, net.getParentIds(nodeName));
		Collections.addAll(ret, net.getChildIds(nodeName));
		
		//Delete node from the network
		net.deleteNode(nodeName);
		net.writeFile(networkFile);
		
		return ret;
	}
	
	/**
	 * Method for getting the index of the state from the list of states of the node
	 * 
	 * @param net - pointer to the Bayesian network
	 * @param nodeName - name of node that contains the state
	 * @param stateName - name of specific state being looked up
	 * @return idx - index of the specified state
	 */
	private static int getIndex (Network net, String nodeName, String stateName) {
		
		String[] outcomeIds = net.getOutcomeIds(nodeName);
		int idx;

		for (idx = 0; idx < outcomeIds.length; idx++) {
			if (stateName.equals(outcomeIds[idx])){
				break;
			}
		}
		
		return idx;
	}
	
	/**
	 * Method to retrieve parents of specified node
	 * 
	 * @param nodeName - name of node who's parents are being retrieved
	 * @return ret2 - list of parents of the specified node
	 * @throws SMILEException
	 */
	public static List<String> retrieveParents (String nodeName) throws SMILEException {
		
		List<String> ret = new ArrayList<String>();
		List<String> ret2 = new ArrayList<String>();
		
		//Read current network from file
		net.updateBeliefs();
		
		//Get names of parent nodes
		Collections.addAll(ret, net.getParentIds(nodeName.replace(' ', '_')));
		
		for (String i: ret)
			ret2.add(i.replace('_', ' '));
		
		return ret2;
	}
	
	/**
	 * Method to retrieve children of specified node
	 * 
	 * @param nodeName - name of node who's children are being retrieved
	 * @return ret2 - list of children of the specified node
	 * @throws SMILEException
	 */
	public static List<String> retrieveChildren (String nodeName) throws SMILEException {
		
		List<String> ret = new ArrayList<String>();
		List<String> ret2 = new ArrayList<String>();
		
		//Read current network from file
		net.updateBeliefs();
		
		//Get names of parent nodes
		Collections.addAll(ret, net.getChildIds(nodeName.replace(' ', '_')));
		
		for (String i: ret)
			ret2.add(i.replace('_', ' '));
		
		return ret2;
	}
	
	/**
	 * Method for retrieving the states of a specific node
	 * 
	 * @param nodeName - name of node who's states are being retrieved
	 * @return ret2 - list of states of the specified node
	 * @throws SMILEException
	 */
	public static List<String> retrieveStates (String nodeName) throws SMILEException {
		
		List<String> ret = new ArrayList<String>();
		List<String> ret2 = new ArrayList<String>();
		
		//Read current network from file
		net.updateBeliefs();
		
		//Get names of states
		Collections.addAll(ret, net.getOutcomeIds(nodeName.replace(' ', '_')));
		
		for (String i: ret)
			ret2.add(i.replace('_', ' '));
		
		return ret2;
	}
	
	/**
	 * Method for adding a state to a specified node
	 * 
	 * @param nodeName - name of node to be modified
	 * @param stateName - name of state to be added to the node
	 */
	public static void addThisState (String nodeName, String stateName) {
		//Read current network from file
		net.updateBeliefs();
		
		//Add state
		net.insertOutcome(nodeName.replace(' ', '_'), 0, stateName.replace(' ', '_'));
		net.updateBeliefs();
		net.writeFile(networkFile);
	}
	
	/**
	 * Method for deleting a state from a specified node
	 * 
	 * @param nodeName - name of node to be modified
	 * @param stateName - name of state to be removed from the node
	 */
	public static void delThisState (String nodeName, String stateName) {
	
		//Read current network from file
		net.updateBeliefs();
		
		//Delete state
		net.deleteOutcome(nodeName.replace(' ', '_'), stateName.replace(' ', '_'));
		net.updateBeliefs();
		net.writeFile(networkFile);
	}
	
	/**
	 * Method for retrieving the probabilities of the specified node
	 * 
	 * @param nodeName - name of node who's probabilities are being retrieved
	 * @return ret - list of probabilities from the node's truth table
	 * @throws SMILEException
	 */
	public static List<Double> retrieveProbs (String nodeName) throws SMILEException {
		
		List<Double> ret = new ArrayList<Double>();
		
		//Read current network from file
		net.updateBeliefs();
		
		//Get node probabilities
		for (double i: net.getNodeDefinition(nodeName.replace(' ', '_')))
			ret.add(i);	
		
		return ret;
	}
	
	/**
	 * Method for adding a connection from a node to another
	 * 
	 * @param parentName - name of parent to establish a connection from
	 * @param childName - name of child to establish a connection to
	 * @return false if adding an arc is successful, otherwise, true
	 */
	public static boolean addArc (String parentName, String childName) {
		//Read current network from file
		net.updateBeliefs();
		
		//Add connection
		try {
			net.addArc(parentName.replace(' ', '_'), childName.replace(' ', '_'));
		} catch (SMILEException e) {
			return true;	//error occurred - prompt user
		}
		
		//Write changes to the network
		net.updateBeliefs();
		net.writeFile(networkFile);
		
		return false; //successful adding of connection
	}
	
	/**
	 * Method for deleting the connection of 2 nodes
	 * 
	 * @param parentName - name of parent to remove a connection from
	 * @param childName - name of child to remove a connection to
	 */
	public static void delArc (String parentName, String childName) {
		//Read current network from file
		net.updateBeliefs();
		
		//Delete connection 
		net.deleteArc(parentName.replace(' ', '_'), childName.replace(' ', '_'));
		net.updateBeliefs();
		net.writeFile(networkFile);
	}
	
	/**
	 * Method for updating the node with new node name and set of probabilities
	 * 
	 * @param oldNodeName - old name of the node to be modified
	 * @param newNodeName - new name for the node to be modified
	 * @param definition - list of probabilities for the node's truth table
	 */
	public static void updateNode (String oldNodeName, String newNodeName, double [] definition) {
		//Read current network from file
		net.updateBeliefs();
		
		//Update node name
		net.setNodeId(oldNodeName.replace(' ', '_'), newNodeName.replace(' ', '_'));
		net.setNodeName(newNodeName.replace(' ', '_'), newNodeName.replace(' ', '_'));
		
		//Update the probabilities of the network
		net.setNodeDefinition(newNodeName.replace(' ', '_'), definition);
		net.updateBeliefs();
		net.writeFile(networkFile);
	}
	
	/**
	 * Method for updating the set of probabilities of a node
	 * 
	 * @param nodeName - name of node to be modified
	 * @param definition - list of probabilities for the node's truth table
	 */
	public static void updateNodeProb (String nodeName, double [] definition) {
		//Read current network from file
		net.updateBeliefs();
		
		//Update the probabilities of the network
		net.setNodeDefinition(nodeName.replace(' ', '_'), definition);
		net.updateBeliefs();
		net.writeFile(networkFile);
	}
}
