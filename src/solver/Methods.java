package solver;

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
        ArrayList<Connection> connctionList= Statistics.makeConnctionList(population);
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
    public static ArrayList<Node> spreadDisease(ArrayList<Node> healthyPopulation,
                                                double TRANSMISSION_RATE,
                                                double RECOVER_RATE,
                                                int INFECTIOUS_TIME) {
        System.out.println("initialising disease sequence");
        Random rand = new Random();
        rand.nextInt();
        Node infectionSource = healthyPopulation.get(rand.nextInt(healthyPopulation.size()));
        infectionSource.infectionTime = 1;
        // This agent mutates (even if vaccinated is true)
        infectionSource.state = "infected";

        // Recursive step breadth first search
        Queue<Node> memory = new LinkedList<Node>();
        ArrayList<Node> diseasedPopulation = spreadBFS(infectionSource, healthyPopulation, 1,
                memory, TRANSMISSION_RATE, RECOVER_RATE, INFECTIOUS_TIME);

        //ArrayList<Node> diseasedPopulation = healthyPopulation;
        return diseasedPopulation;
    }

    // if a person is not vaccinated and is suceptible this person will get infected.
    public static ArrayList<Node> spreadBFS(Node diseasedNode,
                                            ArrayList<Node> healthyPopulation,
                                            int time, Queue<Node> memory,
                                            double TRANSMISSION_RATE,
                                            double RECOVER_RATE, int INFECTIOUS_TIME) {
        time += 1;
        Random rand = new Random();
        // From suceptible to infectious
        for (Connection connection : diseasedNode.connections) {
            Node targetNode = healthyPopulation.get(connection.nodeNumber2);
            double boundry = rand.nextDouble();
            if (!targetNode.vaccinated && targetNode.state.equals("suceptible") && boundry <= TRANSMISSION_RATE) {
                targetNode.infectionTime = time;
                targetNode.state = "infected";
                memory.add(targetNode);
            }

        }

        //from infectious to recovered
        for (Node node: healthyPopulation){
            double recoverBoundry = rand.nextDouble();
            if(node.state.equals("infected") &&  recoverBoundry < RECOVER_RATE && node.infectionTime <=  (node.infectionTime + INFECTIOUS_TIME)) {
                node.state ="recovered";
                node.recoverTime =time;
                //for a SIS-model:  node.state.equals("suceptible")
            }
        }
        while(memory.size() > 0){
            spreadBFS(memory.poll(), healthyPopulation, time, memory, TRANSMISSION_RATE, RECOVER_RATE, INFECTIOUS_TIME);
        }
        return healthyPopulation;
    }






}
