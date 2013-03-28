package testPack;

import java.sql.Connection;

import smile.Network;
import utilities.*;

public class SomeBean {
  public static void main (String args []) {
	  //Connection conn = JdbcUtil.startConnection();	  
	  //System.out.println("Connected!");
	  
	  
	  Network net = new Network();
	  
	  net.addNode(Network.NodeType.Cpt, "Hypertension");
	  net.setOutcomeId("Hypertension", 0, "Present");
	  net.setOutcomeId("Hypertension", 1, "Absent");
	  
	  net.addNode(Network.NodeType.Cpt, "Diabetes");
	  net.setOutcomeId("Diabetes", 0, "Present");
	  net.setOutcomeId("Diabetes", 1, "Absent");
	  
	  net.addNode(Network.NodeType.Cpt, "TIA");
	  net.setOutcomeId("TIA", 0, "Present");
	  net.setOutcomeId("TIA", 1, "Absent");
	  
	  net.addNode(Network.NodeType.Cpt, "Heredity");
	  net.setOutcomeId("Heredity", 0, "Present");
	  net.setOutcomeId("Heredity", 1, "Absent");
	  
	  net.addNode(Network.NodeType.Cpt, "BloodSugar");
	  net.setOutcomeId("BloodSugar", 0, "High");
	  net.setOutcomeId("BloodSugar", 1, "Low");
	  
	  net.addNode(Network.NodeType.Cpt, "Age");
	  net.setOutcomeId("Age", 0, "Old");
	  net.setOutcomeId("Age", 1, "Young");
	  
	  net.addNode(Network.NodeType.Cpt, "Obese");
	  net.setOutcomeId("Obese", 0, "Yes");
	  net.setOutcomeId("Obese", 1, "No");
	  
	  net.addNode(Network.NodeType.Cpt, "SleepApnea");
	  net.setOutcomeId("SleepApnea", 0, "Present");
	  net.setOutcomeId("SleepApnea", 1, "Absent");
	  
	  net.addNode(Network.NodeType.Cpt, "CigaretteAbuse");
	  net.setOutcomeId("CigaretteAbuse", 0, "Smoker");
	  net.setOutcomeId("CigaretteAbuse", 1, "Nonsmoker");
	  
	  net.addNode(Network.NodeType.Cpt, "HeartDisease");
	  net.setOutcomeId("HeartDisease", 0, "Present");
	  net.setOutcomeId("HeartDisease", 1, "Absent");
	  
	  net.addNode(Network.NodeType.Cpt, "EconomicFactors");
	  net.setOutcomeId("EconomicFactors", 0, "Low");
	  net.setOutcomeId("EconomicFactors", 1, "High");
	  
	  net.addNode(Network.NodeType.Cpt, "CVD");
	  net.setOutcomeId("CVD", 0, "Present");
	  net.setOutcomeId("CVD", 1, "Absent");
	  
	  net.addNode(Network.NodeType.Cpt, "Gender");
	  net.setOutcomeId("Gender", 0, "Male");
	  net.setOutcomeId("Gender", 1, "Female");
	  
	  net.addNode(Network.NodeType.Cpt, "Stroke");
	  net.setOutcomeId("Stroke", 0, "Present");
	  net.setOutcomeId("Stroke", 1, "Absent");
	  
	  double[] probs = {0.5, 0.5}; 
	  net.setNodeDefinition("EconomicFactors", probs);
	  net.setNodeDefinition("CigaretteAbuse", probs);
	  net.setNodeDefinition("BloodSugar", probs);
	  net.setNodeDefinition("Gender", probs);
	  net.setNodeDefinition("Age", probs);
	  
	  net.addArc("EconomicFactors", "Obese");
	  double[] probs2 = {0.4, 0.6, 0.7, 0.2}; 
	  net.setNodeDefinition("Obese", probs2);
	  
	  net.addArc("EconomicFactors", "Diabetes");
	  net.addArc("BloodSugar", "Diabetes");
	  net.addArc("CigaretteAbuse", "Diabetes");
	  net.addArc("Obese", "Diabetes");
	  double[] probs3 = {0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2}; 
	  net.setNodeDefinition("Diabetes", probs3);
	  
	  net.addArc("EconomicFactors", "Hypertension");
	  net.addArc("CigaretteAbuse", "Hypertension");	
	  double[] probs4 = {0.6, 0.4, 0.2, 0.7, 0.6, 0.4, 0.2, 0.7}; 
	  net.setNodeDefinition("Hypertension", probs4);
	  
	  net.addArc("Obese", "HeartDisease");	  	 
	  net.addArc("CigaretteAbuse", "HeartDisease");
	  double[] probs5 = {0.4, 0.6, 0.7, 0.2, 0.4, 0.6, 0.7, 0.2}; 
	  net.setNodeDefinition("HeartDisease", probs5);
	    
	  net.addArc("Hypertension", "Stroke");
	  net.addArc("Age", "Stroke");
	  net.addArc("Gender", "Stroke");
	  net.addArc("SleepApnea", "Stroke");
	  net.addArc("TIA", "Stroke");
	  net.addArc("Heredity", "Stroke");
	  net.addArc("CVD", "Stroke");
	  double[] probs6 = {0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2, 0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2, 0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2, 0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2, 0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2, 0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2, 0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2, 0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2,0.4, 0.6, 0.7, 0.2}; 
	  net.setNodeDefinition("Stroke", probs6);
	  
	  net.updateBeliefs();
	  net.getNode("Stroke");
	  double[] aValues = net.getNodeValue("Stroke");
	  double P_StrokeIsPresent = aValues[0];			
	  System.out.println("P(\"Stroke\" = Present) = " + P_StrokeIsPresent);
	  
	  net.setEvidence("EconomicFactors", "Low");
	  net.updateBeliefs();
	  aValues = net.getNodeValue("Obese");
	  double dum = aValues[0];		
	  System.out.println("P(\"Obese\" = Yes | \"EconomicFactors\" = Low) = " + dum);
	  
	  net.setEvidence("CigaretteAbuse", "Smoker");
	  net.updateBeliefs();
	  aValues = net.getNodeValue("Hypertension");
	  double dum1 = aValues[0];		
	  System.out.println("P(\"Hypertension\" = Present | \"CigaretteAbuse\" = Yes) = " + dum1);
 
	  net.setEvidence("Age", "Old");
	  net.setEvidence("Gender", "Female");
	  net.setEvidence("CVD", "Present");
	  net.updateBeliefs();
	  aValues = net.getNodeValue("Stroke");
	  double dum2 = aValues[0];		
	  System.out.println("P(\"Stroke\" = Present | \"Age\" = Old & \"Gender\" = Female & \"CVD\" = Present) = " + dum2);
  }
}
