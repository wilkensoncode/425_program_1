import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class SchedulingAlgorithms {
    String dataFilePath;
    String dataFileName;
    int nProcesses;
    int nPriorities;

    int[] processTime;
    int[] priority;
    int[] arrivalTime;
    int[] waitTime;
    int[] turnaroundTime;
    int[] processed;        // 0 (not started)  1(started)
    int[] listAverageWait;
    int[] listAverageTurnaround;

    int[] processedId;

    SortedList[] sl;

    // statistics
    double[] averageWaitTime;

    double[] averageTRTime;
    int[] processedTime;
    int time;

    public SchedulingAlgorithms() {
        nProcesses = 0;
        nPriorities = 0;
    }

    public void openDataFile() {
        dataFilePath = null;
        dataFileName = null;

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setDialogTitle("Open Data File");

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dataFilePath = chooser.getSelectedFile().getPath();
            dataFileName = chooser.getSelectedFile().getName();
        }
        // read data file and copy it to original array
        try {
            readFileIntoArray(dataFilePath);
        } catch (IOException ioe) {
            System.out.println("Exception " + ioe.getMessage());
            System.exit(0);
        }
    }

    public void readFileIntoArray(String filePath) throws IOException {
        if (filePath != null) {
            int index = 0;
            averageWaitTime = new double[5];
            averageTRTime = new double[5];

            Scanner integerTextFile = new Scanner(new File(filePath));
            nProcesses = integerTextFile.nextInt();
            processTime = new int[nProcesses];
            priority = new int[nProcesses];
            arrivalTime = new int[nProcesses];
            waitTime = new int[nProcesses];
            turnaroundTime = new int[nProcesses];
            processed = new int[nProcesses];

            while (integerTextFile.hasNext()) {
                int i = integerTextFile.nextInt();

                priority[index] = integerTextFile.nextInt();

                if (priority[index] > nPriorities)
                    nPriorities = priority[index];
                arrivalTime[index] = integerTextFile.nextInt();
                processTime[index] = integerTextFile.nextInt();

                index++;
            }
            //  end of file detected
            integerTextFile.close();
        }
    }

    public void FCFS() {
        time = 0;
        double av = 0;
        double tr = 0;
        processedTime = new int[nProcesses];
        processedId = new int[nProcesses];

        for (int i = 0; i < nProcesses; i++) {
            waitTime[i] = time - arrivalTime[i];
            av += waitTime[i];
            turnaroundTime[i] = time + processTime[i] - arrivalTime[i];
            tr += turnaroundTime[i];

            time = processTime[i] + time;

            // Gantt chart

            if (i == 0)
                processedTime[i] = processTime[i];
            else
                processedTime[i] = processedTime[i - 1] + processTime[i];
            processedId[i] = i;


        }
        averageWaitTime[0] = Math.round(av / nProcesses);
        averageTRTime[0] = Math.round(tr / nProcesses);
    }

    public void SJN() {

        time = 0;
        java.util.Arrays.fill(processed, 0);
        double av = 0;
        double tr = 0;

        // set wait time and turnaround time to 0
        for (int i = 0; i < nProcesses; i++) {
            waitTime[i] = 0;
            turnaroundTime[i] = 0;
        }

        SortedList sl = new SortedList();
        addNewArrivals(sl, time);
        processedId = new int[nProcesses];
        // Gantt chart
        processedTime = new int[nProcesses];

        for (int i = 0; i < nProcesses; i++) {
            //******** find the next process to run,
            Node nextProcess = sl.delete();
            processedId[i] = nextProcess.processId;
            ganttChart(i, nextProcess);
            //******** update wait time and turnaround time
            waitTime[nextProcess.processId] = time - nextProcess.processArrivalTime;
            av += waitTime[nextProcess.processId];

            turnaroundTime[nextProcess.processId] = time
                    + nextProcess.processRunTime
                    - nextProcess.processArrivalTime;

            tr += turnaroundTime[nextProcess.processId];
            //******** update time

            time += nextProcess.processRunTime;
            //******** make changes accordingly
            addNewArrivals(sl, time);

        }

        averageWaitTime[1] = Math.round(av / nProcesses);
        averageTRTime[1] = Math.round(tr / nProcesses);
    }

    private void ganttChart(int i, Node nextProcess) {
        if (i == 0)
            processedTime[i] = nextProcess.processRunTime;
        else
            processedTime[i] = processedTime[i - 1]
                    + nextProcess.processRunTime;
    }

    public void addNewArrivals(SortedList sl, int time) {
        //****** add processes that have arrived by argument time, add them to list
        for (int i = 0; i < nProcesses; i++) {

            if (arrivalTime[i] <= time && processed[i] == 0) {
                sl.add(new Node(i, priority[i], arrivalTime[i], processTime[i]));
                processed[i] = i + 1;
            }
        }
    }

    public void displayList(SortedList sl) {
        Node t = new Node();
        t = sl.head;
        while (t != null) {
            System.out.print(" " + t.processId + " ,");
            t = t.next;
        }
        System.out.println();
    }

    public void priorityQueue() {
        // create priority Queues
        double av = 0;
        double tr = 0;
        sl = new SortedList[nPriorities + 1];

        for (int i = 0; i < sl.length; i++) {
            sl[i] = new SortedList();
        }

        for (int i = 0; i < nProcesses; i++) {
            waitTime[i] = 0;
            turnaroundTime[i] = 0;
        }

        java.util.Arrays.fill(processed, 0);

        int finished = 0;
        time = 0;
        Node current = null;

        findNewProcesses(time);
        processedTime = new int[nProcesses];  // Gantt chart
        processedId = new int[nProcesses];    // Gantt chart
        while (finished < nProcesses) {
            // ******pick a new process  from lists
            current = locateNextProcess();
            // ******update wait time and turnaround time
            waitTime[current.processId] = time - current.processArrivalTime;
            av += waitTime[current.processId];
            averageWaitTime[2] += waitTime[current.processId];

            turnaroundTime[current.processId] = time +
                    current.processRunTime - current.processArrivalTime;

            tr += turnaroundTime[current.processId];
            averageTRTime[2] += turnaroundTime[current.processId];
            // ******update time
            time += current.processRunTime;
            // ******add new arrivals
            findNewProcesses(time);
            // ******process it
            processedId[finished] = current.processId; // Gantt chart
            ganttChart(finished, current);
            finished++;
        } // end of  while

        averageWaitTime[2] = Math.round(av / nProcesses);
        averageTRTime[2] = Math.round(tr / nProcesses);

        // list averages
    }

    public void findNewProcesses(int time) {
        for (int i = 0; i < nProcesses; i++) {
            if (arrivalTime[i] <= time && processed[i] == 0) {
                sl[priority[i]].add(new Node(i, priority[i], arrivalTime[i], processTime[i]));
                processed[i] = i + 1;
            }
        }
    }

    public Node locateNextProcess() {
        Node t = null;
        for (int i = 0; i < sl.length; i++) {
            if (sl[i].head != null) {
                t = sl[i].delete();
                return t;
            }
        }
        return t;
    }

    public void displayLists() {
        for (int i = 0; i < sl.length; i++) {
            Node t = sl[i].head;
            System.out.print("List[" + i + "] = ");
            while (t != null) {
                System.out.print(t.processId + " , ");
                t = t.next;
            }
            System.out.println(" size = " + sl[i].size);
        }
    }
    public void runAll() {
        FCFS();
        SJN();
        priorityQueue();
    }

}

