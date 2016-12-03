package solver;
import java.util.ArrayList;
class Main {

    public static void main(String[] args) {
        // Variables for the population
        final int POP_SIZE = 100;

        // percentage of the population which is resistant to the disease
        final int VAC_RATE = 0;

        //Variables for the disease
        // chance of spreading the disease
        final double TRANSMISSION_RATE = 0.9;
        // the chance of recovery after the infectious time
        final double RECOVER_RATE = 0.01;
        // the duration of being infectious
        final int INFECTIOUS_TIME = 1;

        //

        int CONNECT = 600;
        // connectivity between agents
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
        // All populations will have the same initial antibiotic usage rates
        // Empirical population...
        HospitalPopulation = Methods.generateVaccinated(HospitalPopulation, VAC_RATE, POP_SIZE);
        SocialPopulation = Methods.generateVaccinated(SocialPopulation, VAC_RATE, POP_SIZE);
       ///
        SmallWorldPopulation = Methods.generateVaccinated(SmallWorldPopulation, VAC_RATE, POP_SIZE);
        RandomPopulation = Methods.generateVaccinated(RandomPopulation, VAC_RATE, POP_SIZE);
        UniformPopulation = Methods.generateVaccinated(UniformPopulation, VAC_RATE, POP_SIZE);

        // Add the edges NOTE1 parameters:
        UniformPopulation = Methods.generateUniformConnections(UniformPopulation, POP_SIZE, CONNECT);
        RandomPopulation = Methods.generateRandomConnections(RandomPopulation, POP_SIZE, CONNECT);
        SmallWorldPopulation = Methods.generateSmallWorldConnections(SmallWorldPopulation, POP_SIZE, CONNECT);

        double[] UniformNetworkSpecifics = Statistics.networkSpecs(UniformPopulation, CONNECT, POP_SIZE);
        double[] RandomNetworkSpecifics = Statistics.networkSpecs(RandomPopulation, CONNECT, POP_SIZE);
        double[] SmallWorldNetworkSpecifics = Statistics.networkSpecs(SmallWorldPopulation, CONNECT, POP_SIZE);


        // Write to .csv

        System.out.println("Unif ClusteringsCoefficient " + UniformNetworkSpecifics[2]);
        System.out.println("Rand ClusteringsCoefficient " + RandomNetworkSpecifics[2]);
        System.out.println("SW ClusteringsCoefficient " + SmallWorldNetworkSpecifics[2]);

        System.out.println("Unif sd degree " + UniformNetworkSpecifics[1]);
        System.out.println("Rand sd degree " + RandomNetworkSpecifics[1]);
        System.out.println("SW sd degree " + SmallWorldNetworkSpecifics[1]);
        System.out.println("");

        UniformPopulation = Methods.spreadDisease(UniformPopulation, TRANSMISSION_RATE, RECOVER_RATE, INFECTIOUS_TIME);
        RandomPopulation = Methods.spreadDisease(RandomPopulation, TRANSMISSION_RATE, RECOVER_RATE, INFECTIOUS_TIME);
        SmallWorldPopulation = Methods.spreadDisease(SmallWorldPopulation, TRANSMISSION_RATE, RECOVER_RATE, INFECTIOUS_TIME);

        double diseasedUniform = Statistics.statistics(UniformPopulation, POP_SIZE);
        System.out.println("at peak U: " + diseasedUniform + "% of the population is diseased ");

        double diseasedRandom = Statistics.statistics(RandomPopulation, POP_SIZE);
        System.out.println("at peak R: " + diseasedRandom + "% of the population is diseased ");

        double diseasedSW = Statistics.statistics(SmallWorldPopulation, POP_SIZE);
        System.out.println("at peak WS: " + diseasedSW + "% of the population is diseased ");

        int[][] uniformData = Statistics.get_timeHistogram(UniformPopulation);
        int[][] randomData = Statistics.get_timeHistogram(RandomPopulation);
        int[][] sWData = Statistics.get_timeHistogram(SmallWorldPopulation);


        Statistics.toCsv("C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\UNodes1.csv",
                "C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\UEdges1.csv",
                "C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\UStats1.csv",
                UniformPopulation, uniformData);
        Statistics.toCsv("C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\RNodes1.csv",
                "C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\REdges1.csv",
                "C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\RStats1.csv",
                RandomPopulation, randomData);
        Statistics.toCsv("C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\SWNodes1.csv",
                "C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\SWEdges1.csv",
                "C:\\Users\\marty_000\\PycharmProjects\\ESBLspread\\SWStats1.csv",
                SmallWorldPopulation, sWData);
    }

    static private void printPop(ArrayList<Node> population) {
        for (Node node : population) {
            System.out.println(node);
        }
    }
}