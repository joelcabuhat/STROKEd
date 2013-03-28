package testPack;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;

import smile.*;

public class SmileServlet extends HttpServlet {
	
	private PrintWriter out;
	
	//----------------------------------------------------------------------------------------
	/**
	 * 
	 */
	public void init(ServletConfig config) {
	}
	
	//----------------------------------------------------------------------------------------
	/**
	 * 
	 */
	public void destroy() {
	}
	
	//----------------------------------------------------------------------------------------
	/**
	 * rhiza comments here, please please please
	 * Tutorial 1
	 */
	public void CreateNetwork() {
		try {
			Network net = new Network();
			
			// Creating node "Success" and setting/adding outcomes:
			net.addNode(Network.NodeType.Cpt, "Success");
			net.setOutcomeId("Success", 0, "Success");
			net.setOutcomeId("Success", 1, "Failure");
			
			// Creating node "Forecast" and setting/adding outcomes:
			net.addNode(Network.NodeType.Cpt, "Forecast");
			net.addOutcome("Forecast", "Good");
			net.addOutcome("Forecast", "Moderate");
			net.addOutcome("Forecast", "Poor");
			net.deleteOutcome("Forecast", 0);
			net.deleteOutcome("Forecast", 0);
			
			// Adding an arc from "Success" to "Forecast":
			net.addArc("Success", "Forecast");
			
			// Filling in the conditional distribution for node "Success". The 
			// probabilities are:
			// P("Success" = Success) = 0.2
			// P("Success" = Failure) = 0.8
			double[] aSuccessDef = {0.2, 0.8}; 
			net.setNodeDefinition("Success", aSuccessDef);
			
			// Filling in the conditional distribution for node "Forecast". The 
			// probabilities are:
			// P("Forecast" = Good | "Success" = Success) = 0.4
			// P("Forecast" = Moderate | "Success" = Success) = 0.4
			// P("Forecast" = Poor | "Success" = Success) = 0.2
			// P("Forecast" = Good | "Success" = Failure) = 0.1
			// P("Forecast" = Moderate | "Success" = Failure) = 0.3
			// P("Forecast" = Poor | "Success" = Failure) = 0.6
			double[] aForecastDef = {0.4, 0.4, 0.2, 0.1, 0.3, 0.6}; 
			net.setNodeDefinition("Forecast", aForecastDef);
			
			// Changing the nodes' spacial and visual attributes:
			net.setNodePosition("Success", 20, 20, 80, 30);
			net.setNodeBgColor("Success", Color.red);
			net.setNodeTextColor("Success", Color.white);
			net.setNodeBorderColor("Success", Color.black);
			net.setNodeBorderWidth("Success", 2);
			net.setNodePosition("Forecast", 30, 100, 60, 30);
			
			// Writting the network to a file:
			net.writeFile("tutorial_a.xdsl");
		}
		catch (SMILEException e) {
			out.println(e.getMessage());
		}
	}
	
	//----------------------------------------------------------------------------------------
	/**
	 * Tutorial 2
	 */
	public void InfereceWithBayesianNetwork() {
		try {
			Network net = new Network();
			net.readFile("tutorial_a.xdsl"); 
			
			
			// ---- We want to compute P("Forecast" = Moderate) ----
			// Updating the network:
			net.updateBeliefs();
			
			// Creating the node "Forecast":
			net.getNode("Forecast");
			
			// Getting the index of the "Moderate" outcome:
			String[] aForecastOutcomeIds = net.getOutcomeIds("Forecast");
			int outcomeIndex;
			for (outcomeIndex = 0; outcomeIndex < aForecastOutcomeIds.length; outcomeIndex++)
				if ("Moderate".equals(aForecastOutcomeIds[outcomeIndex]))
					break;
			
			// Getting the value of the probability:
			double[] aValues = net.getNodeValue("Forecast");
			double P_ForecastIsModerate = aValues[outcomeIndex];
			
			out.println("P(\"Forecast\" = Moderate) = " + P_ForecastIsModerate);
			
			
			// ---- We want to compute P("Success" = Failure | "Forecast" = Good) ----
			// Introducing the evidence in node "Forecast":
			net.setEvidence("Forecast", "Good");
			
			// Updating the network:
			net.updateBeliefs();
			
			// Getting the index of the "Failure" outcome:
			String[] aSuccessOutcomeIds = net.getOutcomeIds("Success");
			for (outcomeIndex = 0; outcomeIndex < aSuccessOutcomeIds.length; outcomeIndex++)
				if ("Failure".equals(aSuccessOutcomeIds[outcomeIndex]))
					break;
			
			// Getting the value of the probability:
			aValues = net.getNodeValue("Success");
			double P_SuccIsFailGivenForeIsGood = aValues[outcomeIndex];
			
			out.println("P(\"Success\" = Failure | \"Forecast\" = Good) = " + P_SuccIsFailGivenForeIsGood);
			
			
			// ---- We want to compute P("Success" = Success | "Forecast" = Poor) ----
			// Clearing the evidence in node "Forecast":
			net.clearEvidence("Forecast");
			
			// Introducing the evidence in node "Forecast":
			net.setEvidence("Forecast", "Good");
			
			// Updating the network:
			net.updateBeliefs();
			
			// Getting the index of the "Failure" outcome:
			aSuccessOutcomeIds = net.getOutcomeIds("Success");
			for (outcomeIndex = 0; outcomeIndex < aSuccessOutcomeIds.length; outcomeIndex++)
				if ("Failure".equals(aSuccessOutcomeIds[outcomeIndex]))
					break;
			
			// Getting the value of the probability:
			aValues = net.getNodeValue("Success");
			double P_SuccIsSuccGivenForeIsPoor = aValues[outcomeIndex];
			
			out.println("P(\"Success\" = Success | \"Forecast\" = Poor) = " + P_SuccIsSuccGivenForeIsPoor);
		}
		catch (SMILEException e) {
			out.println(e.getMessage());
		}
	}
	
	//----------------------------------------------------------------------------------------
	/**
	 * Tutorial 3
	 */
	public void UpgradeToInfluenceDiagram() {
		try {
			Network net = new Network();
			net.readFile("tutorial_a.xdsl");
			
			// Creating node "Invest" and setting/adding outcomes:
			net.addNode(Network.NodeType.List, "Invest");
			net.setOutcomeId("Invest", 0, "Invest");
			net.setOutcomeId("Invest", 1, "DoNotInvest");
			
			// Creating the value node "Gain":
			net.addNode(Network.NodeType.Table, "Gain");
			
			// Adding an arc from "Invest" to "Gain":
			net.addArc("Invest", "Gain");
			
			// Adding an arc from "Success" to "Gain":
			net.getNode("Success");
			net.addArc("Success", "Gain");
			
			// Filling in the utilities for the node "Gain". The utilities are:
			// U("Invest" = Invest, "Success" = Success) = 10000
			// U("Invest" = Invest, "Success" = Failure) = -5000
			// U("Invest" = DoNotInvest, "Success" = Success) = 500
			// U("Invest" = DoNotInvest, "Success" = Failure) = 500
			double[] aGainDef = {10000, -5000, 500, 500};
			net.setNodeDefinition("Gain", aGainDef);
			
			net.writeFile("tutorial_b.xdsl");
		}
		catch (SMILEException e) {
			out.println(e.getMessage());
		}
	}
	
	//----------------------------------------------------------------------------------------
	/**
	 * Tutorial 4
	 * 
	 * In the following code we assume there will is only one decision node indexing the 
	 * interesting utility node.
	 */
	public void InferenceWithInfluenceDiagram() {
		try {
			// Loading and updating the influence diagram: 
			Network net = new Network();
			net.readFile("tutorial_b.xdsl");
			net.updateBeliefs();
			
			// Getting the handle and the name of value indexing parent (decision node):
			int[] aValueIndexingParents = net.getValueIndexingParents("Gain");
			int nodeDecision = aValueIndexingParents[0];
			String decisionName = net.getNodeName(nodeDecision);
			
			// Displaying the possible expected values:
			out.println("These are the expected utilities:");
			for (int i = 0; i < net.getOutcomeCount(nodeDecision); i++) {
				String parentOutcomeId = net.getOutcomeId(nodeDecision, i);
				double expectedUtility = net.getNodeValue("Gain")[i];

				out.print("  - \"" + decisionName + "\" = " + parentOutcomeId + ": ");
				out.println("Expected Utility = " + expectedUtility);
			}
		}
		catch (SMILEException e) {
			out.println(e.getMessage());
		}
	}
	
	//----------------------------------------------------------------------------------------
	/**
	 * Tutorial 5
	 */
	public void ComputeValueOfInformation() {
		try {
			Network net = new Network();
			net.readFile("tutorial_b.xdsl");
			
			ValueOfInfo voi = new ValueOfInfo(net);
			
			// Getting the handles of nodes "Forecast" and "Invest":
			net.getNode("Forecast");
			net.getNode("Invest");
			
			voi.addNode("Forecast");
			voi.setDecision("Invest");
			voi.update();
			
			double[] results = voi.getValues();
			double EVIForecast = results[0];
			
			out.println("Expected Value of Information (\"Forecast\") = " + EVIForecast);
		}
		catch (SMILEException e) {
			out.println(e.getMessage());
		}
	}
	
	//----------------------------------------------------------------------------------------
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		response.setContentType("text/html");
		out = response.getWriter();
		
		// Openning a page:
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		out.println("<html>");
		out.println("<head>");
		out.println("  <title>jSMILE Servlet Test Page</title>");
		out.println("</head>");
		out.println("<style type=\"text/css\">");
		out.println("  body {font-family: Verdana, Tahoma; font-size: 11px;}");
		out.println("</style>");
		out.println("<body>");
		out.println("<b>This page is an example of the usage of jSMILE library together with Java servlets</b><br />");
		out.println("<br />");
		out.println("<br />");
		out.println("Output of all tutorials:<br />");
		out.println("<textarea rows=\"20\" cols=\"80\">");
		
		// Calling tutorials:
		out.println("*** Tutorial 1 ***");
		CreateNetwork();

		out.println("\n*** Tutorial 2 ***");
		InfereceWithBayesianNetwork();
		
		out.println("\n*** Tutorial 3 ***");
		UpgradeToInfluenceDiagram();
		
		out.println("\n*** Tutorial 4 ***");
		InferenceWithInfluenceDiagram();
		
		out.println("\n*** Tutorial 5 ***");
		ComputeValueOfInformation();
		
		// Closing a page:
		out.println("</textarea><br />");
		out.println("<br />");
		out.println("[The servlet source code can be found inside the jSMILE archive available on the <a href=\"http://www.sis.pitt.edu/~genie/\">GeNIe & SMILE Site</a>.]");
		out.println("</body>");
		out.println("</html>");
		
		out.close();
	}
}