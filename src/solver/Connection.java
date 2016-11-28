package solver;

/**
 * Created by marty_000 on 2-10-2016.
 */
class Connection {
        final int nodeNumber1;
        final int nodeNumber2;

        public Connection(int nodeNumber1,int nodeNumber2) {
            this.nodeNumber1 = nodeNumber1;
            this.nodeNumber2 = nodeNumber2;
        }
        public String toString(){
            return "c <" + nodeNumber1 + ", " + nodeNumber2 + ">";
        }
}
