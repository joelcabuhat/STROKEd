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

@ManagedBean
@SessionScoped
public class InferenceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Global Variables */
	private static final String networkFile = "ESSD_Files/System_Network/STROKEd_Network.xdsl";
	private static final Network net = new Network();
	public static final String with = "Present";
	public static final String without = "Absent";
	public static final String normal = "None";
	
	/*
	 * Constructor
	 */
	public InferenceBean () {
		
	}
	
	public static void start () {
		net.readFile(networkFile);
	}
	
	/*
	 * Perform inference given the selected risk factors
	 */
	public static void doInference (RiskFactor[] selectedRfs, List<StrokeType> st) throws SMILEException {
		
		String nodeName;
		
		net.clearAllEvidence();
		
		//Read current network from file
		net.updateBeliefs();
	
		//Iterate over the selected risk factors to be able to modify the network accordingly
		for (RiskFactor rf : selectedRfs) {
			nodeName = rf.getName().replace(' ', '_');				
			net.setEvidence(nodeName, rf.getSelectedRange().replace(' ', '_'));	
		}
		
		//Update network after setting the evidences found
		net.updateBeliefs();
	
		//Update the probabilities of each type of stroke to be displayed
		double [] typeValues = net.getNodeValue("Stroke");
		
		for (StrokeType i : st)
			i.setProbability(typeValues[getIndex(net, "Stroke", i.getName().replace(' ', '_'))]);
	}
	
	/*
	 * Method for retrieving the probability value from the network
	 */
	public static double getProbValue (String nodeName, String stateName) throws SMILEException {
		
		net.clearAllEvidence();
		
		double [] typeValues = null;
		nodeName = nodeName.replace(' ', '_');
		stateName = stateName.replace(' ', '_');
		System.out.println("WAAAAAAAAAAA " + net.getName());
		//Read current network from file
		net.updateBeliefs();
		
		typeValues = net.getNodeValue(nodeName);
		
		return typeValues[getIndex(net, nodeName, stateName)];
	}
	
	/*
	 * Method for adding a new node in the existing network
	 */
	public static void addNode (String nodeName, List<String> parents, List<String> children, List<State> states) throws SMILEException {
		
		nodeName = nodeName.replace(' ', '_');
		
		//Read current network from file
		net.updateBeliefs();
		
		//Create new node
		net.addNode(Network.NodeType.Cpt, nodeName);
		
		System.out.println("SIZE - " + states.size());
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
	
	/*
	 * Method for deleting a specified node from the network
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
	
	/*
	 * Method for getting the index of the state from the list of states of the node
	 */
	
	private static int getIndex (Network net, String nodeName, String stateName) {
		
		String[] outcomeIds = net.getOutcomeIds(nodeName);
		int idx;
		System.out.println("LENGTH: "+outcomeIds.length);
		for (idx = 0; idx < outcomeIds.length; idx++) {
			if (stateName.equals(outcomeIds[idx])){
				break;
			}
		}
		
		return idx;
	}
	
	/*
	 * Method to retrieve parents of specified node
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
	
	/*
	 * Method to retrieve children of specified node
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
	
	/*
	 * Method for retrieving the states of a specific node
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
	
	/*
	 * Method for adding a state to a specified node
	 */
	public static void addThisState (String nodeName, String stateName) {
		//Read current network from file
		net.updateBeliefs();
		
		//Add state
		net.insertOutcome(nodeName.replace(' ', '_'), 0, stateName.replace(' ', '_'));
		net.updateBeliefs();
		net.writeFile(networkFile);
	}
	
	/*
	 * Method for deleting a state from a specified node
	 */
	public static void delThisState (String nodeName, String stateName) {
		//Read current network from file
		net.updateBeliefs();
		
		//Delete state
		net.deleteOutcome(nodeName.replace(' ', '_'), stateName.replace(' ', '_'));
		net.updateBeliefs();
		net.writeFile(networkFile);
	}
	
	/*
	 * Method for retrieving the probabilities of the specified node
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
	
	/*
	 * Method for adding a connection from a node to another
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
		
		net.updateBeliefs();
		net.writeFile(networkFile);
		
		return false; //successful adding of connection
	}
	
	/*
	 * Method for deleting the connection of 2 nodes
	 */
	public static void delArc (String parentName, String childName) {
		//Read current network from file
		net.updateBeliefs();
		
		//Delete connection 
		net.deleteArc(parentName.replace(' ', '_'), childName.replace(' ', '_'));
		net.updateBeliefs();
		net.writeFile(networkFile);
	}
	
	/*
	 * Method for updating the node with new node name and set of probabilities
	 */
	public static void updateNode (String oldNodeName, String newNodeName, double [] definition) {
		//Read current network from file
		net.updateBeliefs();
		
		//Update node name
		System.out.println("ID " + oldNodeName + " NEW " + newNodeName);
		net.setNodeId(oldNodeName.replace(' ', '_'), newNodeName.replace(' ', '_'));
		net.setNodeName(newNodeName.replace(' ', '_'), newNodeName.replace(' ', '_'));
		
		//Update the probabilities of the network
		net.setNodeDefinition(newNodeName.replace(' ', '_'), definition);
		net.updateBeliefs();
		net.writeFile(networkFile);
	}
	
	/*
	 * Method for updating the set of probabilities of a node
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
