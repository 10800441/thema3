package solver;
import com.sun.org.apache.bcel.internal.generic.POP;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Methods {
    // Gives a specified percentage of nodes the property of being 'vaccinated'
    public static ArrayList<Node> generateVaccinated(ArrayList<Node> population, int VAC_RATE, int POP_SIZE) {

        // Make sure indexes only occur once
        int vac = (int) (POP_SIZE / 100) * VAC_RATE;
        Integer[] arr = new Integer[POP_SIZE];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        Collections.shuffle(Arrays.asList(arr));

        // Set property vaccinated to true
        for (int i = 0; i < vac; i++) {
            Node node = population.get(arr[i]);
            node.vaccinated = true;
        }
        return population;
    }

    // Check if an equivalent relation already existst
    public static boolean alreadyExists(Connection newConnect, ArrayList<Node> population) {
        ArrayList<Connection> connctionList= makeConnctionList(population);
        for (Connection oldConnection : connctionList) {
            Connection reverseConnect = new Connection(oldConnection.nodeNumber2, oldConnection.nodeNumber1);
            if (oldConnection == newConnect || reverseConnect == newConnect) {
                return true;
            }
        }
        return false;
    }




    public static ArrayList<Node> generateRandomConnections(ArrayList<Node> population,
                                                      int POP_SIZE,
                                                      int CONNECT) {

        Random rand = new Random();
        for (int i = 0; i < CONNECT; i++) {
            int from = rand.nextInt(POP_SIZE);
            int to = rand.nextInt(POP_SIZE);
            while (from == to) {
                to = rand.nextInt(POP_SIZE);
            }
            Connection connection = new Connection(from, to);
            Node node = population.get(from);

            // Type of network
            if (!alreadyExists(connection, population)) {
                node.connections.add(connection);
            }
        } return population;
    }

public static ArrayList<Node> generateUniformConnections(ArrayList<Node> population,
                                                          int POP_SIZE, int CONNECT) {
    int bound = (int) CONNECT/ POP_SIZE +1;
    ArrayList<Integer >index = new ArrayList<>();

    int conCount = 0;
    ArrayList<Connection> connArray = new ArrayList<>();
    while(conCount < CONNECT) {
        for (int k = 0; k < POP_SIZE; k++) {
            index.add(k);
        }
        Collections.shuffle(index);
        for (int i = 0; i < POP_SIZE; i++) {

            if (i != index.get(i) && conCount < CONNECT) {

                connArray.add(new Connection(i, index.get(i)));
                conCount++;
            } else if (conCount < CONNECT){

                i--;
                Collections.shuffle(index);
            }
        }
    }

    for(Connection connection: connArray){

        Node node  =population.get(connection.nodeNumber1);
        node.connections.add(connection);
    }
    return population;
}


    // Watts and Strogatz model performed on a uniform population
    public static ArrayList<Node> generateSmallWorldConnections(ArrayList<Node> population,
                                                             int POP_SIZE, int CONNECT) {
        int beta = (POP_SIZE / 8);
        int hubbNodes = 4;

        Random random = new Random();
        population = generateUniformConnections(population, POP_SIZE, (CONNECT - beta));

        for (int k = 0; k < hubbNodes ; k++) {
            int i = random.nextInt(POP_SIZE);

            for (int j = 0; j < (int) (beta /hubbNodes); j++) {
                int to = random.nextInt(POP_SIZE);
                Connection hubbCon = new Connection(i, to);
                while (alreadyExists(hubbCon, population)) {
                    to = random.nextInt(POP_SIZE);
                    hubbCon = new Connection(i, to);
                }
                population.get(i).connections.add(hubbCon);
            }
        }

        return population;
    }


    // Disease initialization
    public static ArrayList<Node> spreadDisease(ArrayList<Node> healthyPopulation) {
        System.out.println("initialising disease sequence");
        Random rand = new Random();
        rand.nextInt();
        Node infectionSource = healthyPopulation.get(rand.nextInt(healthyPopulation.size()));

        // This agent mutates (even if vaccinated is true)
        infectionSource.state = "infected";

        // Recursive step breadth first search
        Queue<Node> memory = new LinkedList<Node>();
        ArrayList<Node> diseasedPopulation = spreadBFS(infectionSource, healthyPopulation, 1, memory);

        //ArrayList<Node> diseasedPopulation = healthyPopulation;
        return diseasedPopulation;
    }

    // if a person is not vaccinated and is suceptible this person will get infected.
    public static ArrayList<Node> spreadBFS(Node diseasedNode, ArrayList<Node> healthyPopulation ,int time, Queue<Node> memory) {
        time += 10;
        System.out.println(time);
        for (Connection connection : diseasedNode.connections) {
            Node targetNode = healthyPopulation.get(connection.nodeNumber2);
            if (!targetNode.vaccinated && targetNode.state.equals("suceptible")) {
                targetNode.infectionTime = time;
                targetNode.state = "infected";
                memory.add(targetNode);
            }
        }
        while(memory.size() > 0){
            spreadBFS(memory.poll(), healthyPopulation, time, memory);
        }
        return healthyPopulation;
    }

    public static ArrayList<Node> spread(Node diseasedNode, ArrayList<Node> healthyPopulation ,int time) {
        time += 10;
        System.out.println(time);
        for (Connection connection : diseasedNode.connections) {
            Node targetNode = healthyPopulation.get(connection.nodeNumber2);
            if (!targetNode.vaccinated && targetNode.state.equals("suceptible")) {
                targetNode.infectionTime = time;
                targetNode.state = "infected";
                spread(targetNode, healthyPopulation, time);
            }

        }
        return healthyPopulation;
    }







    // Calculations
    public static double statistics(ArrayList<Node> population, int POP_SIZE) {
        double count = 0;
        for (Node node : population) {
            if (node.state.equals("infected")) count++;
        }
        double statistic = (count / POP_SIZE) * 100;
        return statistic;
    }
    static private ArrayList<Connection> makeConnctionList(ArrayList<Node> population){
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

    public static void toCsv(String nodesName, String edgesName, ArrayList<Node> population) {
        try{
            FileWriter nodesWriter = new FileWriter(nodesName);
            FileWriter edgesWriter = new FileWriter(edgesName);
            for (Node node:population) {
                nodesWriter.append(node.number + "," + node.vaccinated + ","+ node.state + "\n");
                for( Connection connection: node.connections) {
                    edgesWriter.append("" + connection.nodeNumber1 +"," + connection.nodeNumber2 + "\n");
                }
            }

            edgesWriter.close();
            nodesWriter.close();
            }catch(IOException ex){
                System.out.println(ex);
        }
    }
}
