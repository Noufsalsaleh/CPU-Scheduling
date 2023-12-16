import java.io.PrintStream;

public class Process {

	int id;
	int priority;
	int arrivalTime;
	int burstTime;	
	int startTime;
	int terminateTime;
	int executedTime;

	public Process(int id, int priority, int arrivalTime, int burstTime) {
		this.id = id;
		this.priority = priority;	
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;			
		this.startTime = 0;
		this.terminateTime = 0;
		this.executedTime = 0;
	}
	
	public void init() {
		this.startTime = 0;
		this.terminateTime = 0;
		this.executedTime = 0;
	}
	
	public int getWaitTime() {
		return terminateTime - arrivalTime - burstTime;
	}

	public int getTurnAroundTime() {
		return getWaitTime() + burstTime;
	}
	
	public int getResponseTime() {
		return startTime - arrivalTime;
	}

	public void print(PrintStream output) {
		output.println("ID: PN" + this.id);
		output.println("Priority: " + this.priority);
		output.println("Arrival Time: " + this.arrivalTime);
		output.println("CPU burst: " + this.burstTime);		
		output.println("Start Time: " + this.startTime);
		output.println("Termination Time: " + this.terminateTime);
		output.println("Turn around Time: " + getTurnAroundTime());
		output.println("Waiting Time: " + getWaitTime());		
		output.println("Response Time: " + getResponseTime());
	}
	
}
