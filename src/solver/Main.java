package solver;
import java.util.ArrayList;
class Main {

    public static void main(String[] args) {
        // Variables for the population
        int POP_SIZE = 100;
        // amount of connections
        int CONNECT = 600;
        // connectivity between agents
        int VAC_RATE = 80;

        //Variables for the disease
        int TRANSMISSION_RATE = 100;


        ArrayList<Node> HospitalPopulation = new ArrayList<>();
        double Hdegree = 6.5;
        double Hsd = 5.3;
        double Hclustering = 0.29;
        ArrayList<Node> SocialPopulation = new ArrayList<>();
        double Sdegree = 6.5;
        double Ssd = 6.8;
        double Sclustering = 0.68;
        ArrayList<Node> SmallWorldPopulation = new ArrayList<>();
        double SWdegree = 4.0;
        double SWsd = 0;
        double SWclustering = 0.001;
        ArrayList<Node> RandomPopulation = new ArrayList<>();
        double Rdegree = 4.0;
        double Rsd = 6.8;
        double Rclustering = 0.49;
        ArrayList<Node> UniformPopulation = new ArrayList<>();
        double Udegree = 4.0;
        double Usd = 0;
        double Uclustering = 0.0001;

        for (int i = 0; i < POP_SIZE; i++) {
            HospitalPopulation.add(new Node(i, new ArrayList<>(), false, "suceptible"));
            SocialPopulation.add(new Node(i, new ArrayList<>(), false, "suceptible"));
            SmallWorldPopulation.add(new Node(i, new ArrayList<>(), false, "suceptible"));
            RandomPopulation.add(new Node(i, new ArrayList<>(), false, "suceptible"));
            UniformPopulation.add(new Node(i, new ArrayList<>(), false, "suceptible"));
        }
        // All populations will have the same initial vacciantion rates
        HospitalPopulation = Methods.generateVaccinated(HospitalPopulation, VAC_RATE, POP_SIZE);
        SocialPopulation = Methods.generateVaccinated(SocialPopulation, VAC_RATE, POP_SIZE);
        SmallWorldPopulation = Methods.generateVaccinated(SmallWorldPopulation, VAC_RATE, POP_SIZE);
        RandomPopulation = Methods.generateVaccinated(RandomPopulation, VAC_RATE, POP_SIZE);
        UniformPopulation = Methods.generateVaccinated(UniformPopulation, VAC_RATE, POP_SIZE);

        // Add the edges NOTE1 parameters:
        UniformPopulation = Methods.generateUniformConnections(UniformPopulation, POP_SIZE, CONNECT);
        RandomPopulation = Methods.generateRandomConnections(RandomPopulation, POP_SIZE, CONNECT);
        SmallWorldPopulation = Methods.generateSmallWorldConnections(SmallWorldPopulation, POP_SIZE, CONNECT);

        double[] UniformNetworkSpecifics = Methods.networkSpecs(UniformPopulation, CONNECT, POP_SIZE);
        double[] RandomNetworkSpecifics = Methods.networkSpecs(RandomPopulation, CONNECT, POP_SIZE);
        double[] SmallWorldNetworkSpecifics = Methods.networkSpecs(SmallWorldPopulation, CONNECT, POP_SIZE);


        // Write to .csv

        System.out.println("Unif ClusteringsCoefficient " + UniformNetworkSpecifics[2]);
        System.out.println("Rand ClusteringsCoefficient " + RandomNetworkSpecifics[2]);
        System.out.println("SW ClusteringsCoefficient " + SmallWorldNetworkSpecifics[2]);

        System.out.println("Unif sd degree " + UniformNetworkSpecifics[1]);
        System.out.println("Rand sd degree " + RandomNetworkSpecifics[1]);
        System.out.println("SW sd degree " + SmallWorldNetworkSpecifics[1]);
        System.out.println("");

        double cleanUniform = Methods.statistics(UniformPopulation, POP_SIZE);
        System.out.println("at init: " + cleanUniform + "% of the population is diseased ");

        printPop(UniformPopulation);
        UniformPopulation = Methods.spreadDisease(UniformPopulation);


        //RandomPopulation = Methods.spreadDisease(RandomPopulation);
        //SmallWorldPopulation = Methods.spreadDisease(SmallWorldPopulation);

printPop(UniformPopulation);
        double diseasedUniform = Methods.statistics(UniformPopulation, POP_SIZE);
        System.out.println("at peak: " + diseasedUniform + "% of the population is diseased ");
/*
        double diseasedRandom = Methods.statistics(RandomPopulation, POP_SIZE);
        System.out.println("at init: " + diseasedRandom + "% of the population is diseased ");

        double diseasedSW = Methods.statistics(SmallWorldPopulation, POP_SIZE);
        System.out.println("at init: " + diseasedSW + "% of the population is diseased ");

        Methods.toCsv("C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\UNodes1.csv",
                "C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\UEdges1.csv",
                UniformPopulation);
        Methods.toCsv("C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\RNodes1.csv",
                "C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\REdges1.csv",
                RandomPopulation);
        Methods.toCsv("C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\SWNodes1.csv",
                "C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\SWEdges1.csv",
                SmallWorldPopulation);

*/    }

    static private void printPop(ArrayList<Node> population) {
        for (Node node : population) {
            System.out.println(node);
        }
    }
}