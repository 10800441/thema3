package solver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by marty_000 on 28-11-2016.
 */
public class Statistics {

    // Calculations
    public static double statistics(ArrayList<Node> population, int POP_SIZE) {
        double count = 0;
        for (Node node : population) {
            if (node.state.equals("infected")) count++;
        }
        double statistic = (count / POP_SIZE) * 100;
        return statistic;
    }
    static public ArrayList<Connection> makeConnctionList(ArrayList<Node> population){
        ArrayList<Connection> connectionList = new ArrayList<>();
        for (Node node:population) {
            for (Connection connection : node.connections)
                connectionList.add(connection);
        }
        return connectionList;
    }

    static public double[] networkSpecs(ArrayList<Node> population, int connections, int POP_SIZE) {

        double[] specs = new double[3];
        double averageDegree = (double) (connections*2) / POP_SIZE;
        ArrayList<Connection> connectionList = makeConnctionList(population);


        double sumSquaredError = 0;
        double clusteringSum = 0;

        for(int i = 0; i < POP_SIZE; i++) {

            double nodeDegree = 0;
            ArrayList<Integer> secondaryNodes = new ArrayList<>();

            for (Connection connection : connectionList) {

                if(i == connection.nodeNumber1) {
                    nodeDegree++;
                    secondaryNodes.add(connection.nodeNumber2);

                } else if(i == connection.nodeNumber2){
                    nodeDegree++;
                    secondaryNodes.add(connection.nodeNumber1);
                }
            }
            population.get(i).degree = nodeDegree;
            double squaredError = Math.pow((double)(nodeDegree-averageDegree),2);
            sumSquaredError += squaredError;

            // clustering coeff
            double clustering = 0;
            int triangles = 0;
            Set<Integer> set = new HashSet<Integer>(secondaryNodes);
            for( Connection conn: connectionList){
                for (int k: set){
                    for(int l: set){
                        if(conn.nodeNumber1 == k && conn.nodeNumber2 == l ||
                                conn.nodeNumber2 == k && conn.nodeNumber1 == l){
                            triangles++;

                        }
                    }
                }
            }
            clustering = (double) (triangles*2)/(nodeDegree*(nodeDegree-1));
            population.get(i).clusteringC = clustering;
            clusteringSum += clustering;


        }

        double standardDeviationDegree = Math.sqrt(sumSquaredError/POP_SIZE);

        double clusteringsCoefficient = clusteringSum/POP_SIZE;
        specs[0]= averageDegree;
        specs[1]= standardDeviationDegree;
        specs[2]=clusteringsCoefficient;
        return specs;
    }

    public static int[][] get_timeHistogram(ArrayList<Node> population){
         int finalTime = 1;
        for (Node node:population) {
            if(node.infectionTime > finalTime) { finalTime = node.infectionTime;}
            if(node.recoverTime > finalTime){finalTime = node.recoverTime;}
        }
        int[][] infectiousData = new int[4][finalTime+1];
        for(int i = 0; i < finalTime; i++) infectiousData[0][i] = i;
        for (Node node:population) {
            if(node.infectionTime !=0) {
                for(int index = node.infectionTime; index < finalTime; index++){
                    infectiousData[1][index] ++;
                    infectiousData[2][index] ++;

                }

            }
            if(node.recoverTime !=0) {
                for (int index = node.recoverTime; index < finalTime; index++) {
                    infectiousData[3][index]++;
                    infectiousData[2][index]--;
                }
            }
        }
        return infectiousData;
    }

    public static void toCsv(String nodesName, String edgesName,String statName, ArrayList<Node> population, int[][] data) {
        try{
            FileWriter nodesWriter = new FileWriter(nodesName);
            FileWriter edgesWriter = new FileWriter(edgesName);
            FileWriter statWriter = new FileWriter(statName);

            for (Node node:population) {
                nodesWriter.append(node.number + "," + node.vaccinated + ","+ node.state + "\n");
                for( Connection connection: node.connections) {
                    edgesWriter.append("" + connection.nodeNumber1 +"," + connection.nodeNumber2 + "\n");
                }
            }

            edgesWriter.close();
            nodesWriter.close();
            for (int k = 0; k < data[0].length; k++) {
                statWriter.append(data[0][k] + "," + data[1][k] + "," + data[2][k] + "," + data[3][k] + "\n");
            }
            statWriter.close();


        }catch(IOException ex){
            System.out.println(ex);
        }
    }
}
