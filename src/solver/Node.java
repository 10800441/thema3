package solver;

import javax.sql.ConnectionEventListener;
import java.util.ArrayList;

/**
 * contains: ID
 *           List with directed connections
 *           boolean value for antibiotica usage
 *           SIS deterministic state
 *           network specific information (degree clusteringscoefficient
 */


class Node {

        final int number;
        ArrayList<Connection> connections;
        boolean vaccinated;
        String state;
        double degree;
        double clusteringC;
        int infectionTime;
        int recoverTime;

        public Node(int number, ArrayList<Connection> connections, boolean vaccinated,
                    String state,double degree, double clusteringC, int infectionTime, int recoverTime) {
            this.number = number;
            this.connections = connections;
            this.vaccinated = vaccinated;
            this.state = state;
            this.degree = degree;
            this.clusteringC = clusteringC;
            this.infectionTime = infectionTime;
            this.recoverTime = recoverTime;
        }

    public Node(int number, ArrayList<Connection> connections, boolean vaccinated, String state) {
        this.number = number;
        this.connections = connections;
        this.vaccinated = vaccinated;
        this.state = state;
    }

        public String toString(){
            return "Agent " + number + " connected to " + connections + " is  vaccinated: " + vaccinated +
                    ", has disease: " + state + "; Ti " + infectionTime + ", Tr " + recoverTime;
        }
    }
