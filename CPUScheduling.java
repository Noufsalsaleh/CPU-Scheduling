import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CPUScheduling {

	private static Scanner scr = new Scanner(System.in);
	private static ArrayList<Process> processes;

	public static void initialize() {
		System.out.print("Enter number of processes: ");
		int numOfProcesses = scr.nextInt();

		processes = new ArrayList<Process>(numOfProcesses);
		
		System.out.println("Enter priority, arrival time and CPU burst of each process.");
		for (int i = 0; i < numOfProcesses; i++) {
			System.out.println("Process PN" + i + ":");
			System.out.println("------------");

			System.out.print("\t Priority  : ");
			int priority = scr.nextInt();
			while (priority < 1 || priority > 5) {
				System.out.println("Process priority range [1-5]");
				System.out.print("\t Priority  : ");
				priority = scr.nextInt();
			}

			System.out.print("\t Arrival Time  : ");
			int arrivalTime = scr.nextInt();

			System.out.print("\t Burst time: ");
			int burstTime = scr.nextInt();

			System.out.println("------------");

			processes.add(new Process(i + 1, priority, arrivalTime, burstTime));
		}

	}
	
	public static String startScheduling(ArrayList<Process> processes) {
		Process cpuProcess = null;
		int cpuClock = 0;
		boolean cpuState = false;
		String chart = "";
		
		ArrayList<Process> jobsQueue = new ArrayList<Process>();
		for (Process p : processes) {
			p.init();

			int i = 0;
			while (i < jobsQueue.size()
					&& p.arrivalTime >= jobsQueue.get(i).arrivalTime)
				i++;

			jobsQueue.add(i, p);
		}
		
		ProcessPriorityQueue readyQueue = new ProcessPriorityQueue();

		while (!jobsQueue.isEmpty() || !readyQueue.isEmpty() || cpuState == true) {

			while (!jobsQueue.isEmpty()
					&& jobsQueue.get(0).arrivalTime <= cpuClock) {
				Process p = jobsQueue.remove(0);
				readyQueue.enqueue(p);
			}

			if (cpuState == false && !readyQueue.isEmpty()) {
				cpuProcess = readyQueue.dequeue();
				cpuState = true;
				if (cpuProcess.executedTime == 0)
					cpuProcess.startTime = cpuClock;
				
				chart += " | PN" + cpuProcess.id;
			}

			if (cpuProcess != null && !readyQueue.isEmpty()
					&& cpuProcess.priority > readyQueue.peek().priority) {
				// Swap
				cpuClock += 1;  // context switch time = 1
				
				while (!jobsQueue.isEmpty() && jobsQueue.get(0).arrivalTime <= cpuClock) {
					Process p = jobsQueue.remove(0);
					readyQueue.enqueue(p);
				}
				
				readyQueue.enqueue(cpuProcess);
				cpuProcess =readyQueue.dequeue();				
				cpuState = true;
				if (cpuProcess.executedTime == 0)
					cpuProcess.startTime = cpuClock;

				chart += " | CS | PN" + cpuProcess.id;
			}

			cpuClock++;

			if (cpuProcess != null) {
				cpuProcess.executedTime++;
				if (cpuProcess.executedTime == cpuProcess.burstTime) {
					if (!jobsQueue.isEmpty() || !readyQueue.isEmpty())
						chart += " | CS";
					cpuState = false;
					cpuProcess.terminateTime = cpuClock;
					cpuClock += 1;  // context switch time = 1
					cpuProcess = null;
				}
			}
		}
		
		return chart;
	}

	public static void printReprot() {
		if (processes == null || processes.size() == 0) {
			System.out.println("Enter processes first.");
			return;
		}

		String chart = startScheduling(processes);

		System.out.println("Report");
		System.out.println("=========================");
		for (Process p : processes) {
			p.print(System.out);
			System.out.println("-----------");
		}

		System.out.println("=========================");
		System.out.println("Gantt Chart : " + chart + " |");
		System.out.println("=========================");

		double avgTurnaround = 0, avgWaiting = 0, avgResponse = 0;
		for (Process p : processes) {
			avgTurnaround += p.getTurnAroundTime();
			avgWaiting += p.getWaitTime();
			avgResponse += p.getResponseTime();
		}
		avgTurnaround = avgTurnaround / processes.size();
		avgWaiting = avgWaiting / processes.size();
		avgResponse = avgResponse / processes.size();

		System.out.println("Avg turnaround time : " + avgTurnaround);
		System.out.println("Avg waiting time    : " + avgWaiting);
		System.out.println("Avg response time   : " + avgResponse);
		
		System.out.println("=========================");
	}

	public static void writeReport() {
		PrintStream output = null;		

		try {
			if (processes == null || processes.size() == 0) {
				System.out.println("Please, enter processes first.");
				return;
			}
			
			String chart = startScheduling(processes);
			
			output = new PrintStream("Repor.txt");

			output.println("Report");
			output.println("=========================");
			for (Process p : processes) {
				p.print(output);
				output.println("-----------");
			}

			output.println("=========================");
			output.println("Gantt Chart : " + chart + " |");
			output.println("=========================");

			double avgTurnaround = 0, avgWaiting = 0, avgResponse = 0;
			for (Process p : processes) {
				avgTurnaround += p.getTurnAroundTime();
				avgWaiting += p.getWaitTime();
				avgResponse += p.getResponseTime();
			}
			avgTurnaround = avgTurnaround / processes.size();
			avgWaiting = avgWaiting / processes.size();
			avgResponse = avgResponse / processes.size();

			output.println("Avg turnaround time : " + avgTurnaround);
			output.println("Avg waiting time    : " + avgWaiting);
			output.println("Avg response time   : " + avgResponse);
			
			output.println("=========================");

		} catch (Exception e) {
		}

		if (output != null)
			output.close();
	}
	
	

	public static void main(String[] args) {

		char ch;
		do {
			System.out.println("------------------------");
			System.out.println("Priority CPU Schedulingt");
			System.out.println("------------------------");
			System.out.println("[1] Initialize.");
			System.out.println("[2] Print Report.");
			System.out.println("[3] Exit.");
			System.out.print("Choice :");

			ch = scr.next().charAt(0);

			switch (ch) {
			case '1':
				initialize();
				break;
			case '2':
				printReprot();
				writeReport();
				break;
			case '3':
				break;
			default:
				System.out.println("Enter choise [1-3].");
			}

			System.out.println("\n");
		} while (ch != '3');
	}

}
