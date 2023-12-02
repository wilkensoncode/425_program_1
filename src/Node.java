
public class Node {

    int processId;
    int processPriority;
    int processArrivalTime;
    int processRunTime;
    Node next;

    public Node() {
        processId = 0;
        processPriority = 0;
        processArrivalTime = 0;
        processRunTime = 0;
        next = null;
    }

    /**
     * create a new node with processID, processPriority, processarrivalTime, processRunTime
     *
     * @param processId
     * @param processPriority
     * @param processArrivalTime
     * @param processRunTime
     */
    public Node(int processId, int processPriority, int processArrivalTime, int processRunTime) {
        this.processId = processId;
        this.processPriority = processPriority;
        this.processArrivalTime = processArrivalTime;
        this.processRunTime = processRunTime;

    }

}
