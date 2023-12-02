
// GUI-related imports

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// File-related imports

import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;

public class ProcessManagement extends Frame implements ActionListener {
    SchedulingAlgorithms s;

    //Array
    Font font1 = new Font("Calibri", Font.BOLD, 16);
    Font font2 = new Font("Calibri", Font.BOLD, 12);

    String command = "";

    public static void main(String[] args) {
        Frame frame = new ProcessManagement();
        frame.setResizable(false);
        frame.setSize(1250, 800);
        frame.setVisible(true);
        Font f1 = new Font("Calibri", Font.BOLD, 12);
        frame.setFont(f1);

    }

    public ProcessManagement() {
        setTitle("Process Management Algorithms");

        // Create Menu Bar

        MenuBar mb = new MenuBar();
        setMenuBar(mb);
        //Create Menu about
        Menu about = new Menu("About");
        mb.add(about);

        // Create Menu Group Labeled "File"
        Menu FileMenu = new Menu("File");

        // Add it to Menu Bar

        mb.add(FileMenu);

        // Create Menu Items
        // Add action Listener
        // Add to "File" Menu Group

        MenuItem miOpen = new MenuItem("Open Data File");
        miOpen.addActionListener(this);
        FileMenu.add(miOpen);

        MenuItem miExit = new MenuItem("Exit");
        miExit.addActionListener(this);
        FileMenu.add(miExit);

        // Create Menu Group Labeled "File"

        Menu AlgMenu = new Menu("Algorithms");

        // Add it to Menu Bar

        mb.add(AlgMenu);

        // Create Menu Items
        // Add action Listener
        // Add to "Search" Menu Group
        MenuItem aboutEvent = new MenuItem("About This Program");
        aboutEvent.addActionListener(this);
        about.add(aboutEvent);

        MenuItem miFCFS = new MenuItem("First Come First Served");
        miFCFS.addActionListener(this);
        AlgMenu.add(miFCFS);

        MenuItem miSJN = new MenuItem("Shortest Job Next");
        miSJN.addActionListener(this);
        AlgMenu.add(miSJN);

        MenuItem miPq = new MenuItem("Priority Queues");
        miPq.addActionListener(this);
        AlgMenu.add(miPq);

        MenuItem miAll = new MenuItem("Run All");
        miAll.addActionListener(this);
        AlgMenu.add(miAll);

        WindowListener l = new WindowAdapter() {

            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }

            public void windowActivated(WindowEvent ev) {
                repaint();
            }

            public void windowStateChanged(WindowEvent ev) {
                repaint();
            }

        };

        ComponentListener k = new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        };

        // register listeners

        this.addWindowListener(l);
        this.addComponentListener(k);

    }

//******************************************************************************
//  called by windows manager whenever the application window performs an action
//  (select a menu item, close, resize, ....
//******************************************************************************

    public void actionPerformed(ActionEvent ev) {
        // figure out which command was issued

        command = ev.getActionCommand();

        // take action accordingly
        switch (command) {
            case "About This Program": {
                JOptionPane.showMessageDialog(null, "This program uses different scheduling algorithm and record the time it takes to complete\n"
                        + "1. First Come First Serve (schedule processes as the come into the queue)\n"
                        + "2. Shortest Job Next (schedule the process that takes the least amount of time to process)\n"
                        + "3. Priority Queue (schedule processes base on priority [high-low priority])\n"
                        + "4. Runs all (scheduling all algorithms mentioned above)\n\n"
                        + "Author : Wilkenson Hilarion\n"
                        + "Date: Fall 2023\n", "About this Program", JOptionPane.PLAIN_MESSAGE);
                break;
            }

            case "Open Data File": {
                s = new SchedulingAlgorithms();
                s.openDataFile();
                repaint();
                break;
            }

            case "Exit": {
                System.exit(0);
            }
            case "First Come First Served": {
                s.FCFS();
                repaint();
                break;
            }
            case "Shortest Job Next": {
                s.SJN();
                repaint();
                break;
            }

            case "Priority Queues": {
                s.priorityQueue();
                repaint();
                break;
            }

            case "Run All": {
                s.runAll();
                repaint();
                break;
            }

        }
    }
//********************************************************
// called by repaint() to redraw the screen
//********************************************************

    public void paint(Graphics g) {

        switch (command) {
            case "Open Data File": {
                g.setFont(font1);
                g.setColor(Color.RED);
                // Acknowledge that file was opened
                if (s.dataFileName != null) {
                    g.drawString("File --  " + s.dataFileName + "  -- was successfully opened", 400, 200);
                    g.drawString("Number of Processes = " + Integer.toString(s.nProcesses), 500, 250);
                } else {
                    g.drawString("NO Data File is Open", 300, 200);
                }

                return;
            }

            case "First Come First Served": {
                DisplayResults(g, "First Come First Served (FCFS)", s.averageWaitTime[0], s.averageTRTime[0]);
                break;
            }

            case "Shortest Job Next": {
                DisplayResults(g, "Shortest Job Next (SJN)", s.averageWaitTime[1], s.averageTRTime[1]);
                break;
            }

            case "Priority Queues": {
                DisplayResults(g, "Priority Queue", s.averageWaitTime[2], s.averageTRTime[2]);
                break;
            }

            case "Run All": {
                // Check if the data file is successfully opened
                if (s.dataFileName == null) {
                    System.out.println("Error: No Data File is Open");
                    return;
                }
                // Display table header
                g.setFont(font1);
                g.setColor(Color.BLACK);
                g.drawString("Schedule Policy", 400, 200);
                g.drawString("Average Wait Time", 600, 200);
                g.drawString("Average TurnAround Time", 800, 200);
                //			draw line
                int x = this.getWidth() / 3 + 20;
                int y = 200;
                y = y + 15;
                g.drawLine(x - 50, y - 5, x + 600, y - 5);


                // Display results for each algorithm
                displayResultInTable(g, "FCFS", s.averageWaitTime[0], s.averageTRTime[0], 400, 230);
                displayResultInTable(g, "SJN", s.averageWaitTime[1], s.averageTRTime[1], 400, 245);
                displayResultInTable(g, "Priority Queue", s.averageWaitTime[2], s.averageTRTime[2], 400, 260);

                repaint();
                break;

            }

        }
    }

    private void displayResultInTable(Graphics g, String policy, double avgWait, double avgTrnd, int x, int y) {
        Font font1 = new Font("Calibri", Font.BOLD, 16);
        g.setFont(font1);
        g.setColor(Color.red);
        g.drawString("Number Of Processes ", 490, 90);
        g.drawString(" : " + s.nProcesses, 655, 90);

        Font font2 = new Font("Calibri", Font.BOLD, 12);

        g.setFont(font2);
        g.setColor(Color.BLACK);

        g.drawString(policy, x, y);
        g.drawString(String.format("%.1f", avgWait), x + 250, y);
        g.drawString(String.format("%.1f", avgTrnd), x + 500, y);
    }

    private void displayGanttChart(Graphics g, int[] processId, int[] processedTime, int x, int y) {
        g.setFont(font2);
        g.setColor(Color.BLACK);

        // Draw Gantt chart for each process
        int barHeight = 40;
        int timeUnit = 80;

        for (int i = 0; i < processedTime.length; i++) {
            int startX = x + i * timeUnit;
            int endX = startX + processedTime[i] * timeUnit;

            // Draw the solid line Gantt bar
            g.drawRect(startX, y, timeUnit, barHeight);

            // Label the process and display runtime inside the cell
            String processLabel = "P" + (processId[i] + 1);
            String runtimeLabel = String.valueOf(processedTime[i]);

            int labelX = startX + 5; // Adjust for label position
            int labelY = y + barHeight / 2 + 5; // Adjust for label position

            g.drawString(processLabel, labelX, labelY + 10);
            g.drawString(runtimeLabel, labelX + 40, labelY - 10); // Adjust for label position

            // Display runtime in upper right corner
            g.drawString(String.valueOf(processedTime[i]), endX - 15, y - 5);
        }
    }


    public void DisplayResults(Graphics g, String title, double avgWait, double avgTrnd) {
        Font font1 = new Font("Calibri", Font.BOLD, 16);
        g.setFont(font1);
        g.setColor(Color.red);
        g.drawString("Number Of Processes ", 490, 95);
        g.drawString(" : " + s.nProcesses, 655, 95);

        g.drawString("Total Period of Time ", 500, 110);
        g.drawString(" : " + s.time, 655, 110);

        g.drawString("Scheduling Policy: " + title, 500, 135);
        Font font2 = new Font("Calibri", Font.BOLD, 12);
        g.setFont(font2);
        g.setColor(Color.BLACK);
        if (s.nProcesses > 30) {
            int x = 50;
            int y = 200;
            for (int i = 0; i < 3; i++) {
                g.drawString("ID", x, y);
                g.drawString("Priority", x + 35, y);

                g.drawString("Arrival", x + 90, y);
                g.drawString("RunTime", x + 150, y);
                g.drawString("Wait ", x + 225, y);
                g.drawString("TurnAround", x + 280, y);
                y = y + 15;
                g.drawLine(x, y, x + 350, y);

                for (int j = 0; j < 30; j++) {
                    y = y + 15;
                    g.drawString(Integer.toString(i * 30 + j), x, y);
                    g.drawString(Integer.toString(s.priority[i * 30 + j]), x + 40, y);
                    g.drawString(Integer.toString(s.arrivalTime[i * 30 + j]), x + 100, y);
                    g.drawString(Integer.toString(s.processTime[i * 30 + j]), x + 160, y);
                    g.drawString(Integer.toString(s.waitTime[i * 30 + j]), x + 230, y);
                    g.drawString(Integer.toString(s.turnaroundTime[i * 30 + j]), x + 300, y);
                }
                x = x + 400;
                y = 200;
            }
            g.setFont(font1);
            g.setColor(Color.RED);
            g.drawString("Average Wait Time ", 470, 730);
            g.drawString("Average TurnAround Time", 700, 730);
            g.drawLine(400, 745, 950, 745);
            g.setColor(Color.BLACK);
            g.drawString(Double.toString(avgWait), 510, 780);
            g.drawString(Double.toString(avgTrnd), 750, 780);

        } else {
            int x = this.getWidth() / 3 + 20;
            int y = 200;

            g.drawString("ID", x, y);
            g.drawString("Priority", x + 35, y);

            g.drawString("Arrival", x + 90, y);
            g.drawString("RunTime", x + 150, y);
            g.drawString("Wait ", x + 225, y);
            g.drawString("TurnAround", x + 280, y);
            y = y + 15;
            g.drawLine(x - 10, y, x + 340, y);

            for (int j = 0; j < s.nProcesses; j++) {
                y = y + 15;
                g.drawString(Integer.toString(j + 1), x, y);
                g.drawString(Integer.toString(s.priority[j]), x + 40, y);
                g.drawString(Integer.toString(s.arrivalTime[j]), x + 100, y);
                g.drawString(Integer.toString(s.processTime[j]), x + 160, y);
                g.drawString(Integer.toString(s.waitTime[j]), x + 230, y);
                g.drawString(Integer.toString(s.turnaroundTime[j]), x + 300, y);

            }

            y = y + 30;

            g.setFont(font1);
            g.setColor(Color.RED);
            g.drawString("Average Wait Time ", x, y);
            g.drawString("Average TurnAround Time", x + 170, y);
            y = y + 20;
            g.drawLine(x - 10, y, x + 340, y);
            g.setColor(Color.BLACK);
            y = y + 20;
            g.drawString(Double.toString(avgWait), x + 50, y);
            g.drawString(Double.toString(avgTrnd), x + 250, y);
            // display Gantt Chart
            if (s.processedTime.length < 30) {
                displayGanttChart(g, s.processedId, s.processedTime, x - 200, y + 50);
            }

        }
    }
}