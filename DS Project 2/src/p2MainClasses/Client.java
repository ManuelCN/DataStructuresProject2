package p2MainClasses;
/**
 * Implementation for clients. Each will contain information of when they will
 * arrive, how long service will need to be provided, 
 * @author manny
 *
 */
public class Client {
	
	private int clientID;					//Id of this job
	private int arrivalTime;				//Arrival time of this job
	private int remainingTime;				//Remaining service time for this job
	private int initServiceTime;			//Time at which service began
	private int waitTime;					//Amount of time waited
	private boolean beingServed;			//Indicates whether the client has being receiving service.
	private int overpassedCustomers;
	
	public Client(int id, int at, int rt) {
		clientID = id;
		arrivalTime = at;
		remainingTime = rt;
		initServiceTime = 0;
		waitTime = 0;
		beingServed = false;
		overpassedCustomers = 0;
	}
	
	public boolean isBeingServed() {
		return beingServed;
	}
	
	public void receivingService() {
		beingServed = true;
	}
	
	public int getClientID() {
		return clientID;
	}
	
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	public int getRemainingTime() {
		return remainingTime;
	}
	
	public void isServed(int q) {
		if(q < 0) {	return;	}
		remainingTime -= q;
	}
	
	public int getWaitTime() {
		return this.waitTime;
	}
	
	public int getServiceInitTime() {
		return this.initServiceTime;
	}
	
	public void setServiceInitTime(int servInit) {
		this.initServiceTime = servInit;
		this.waitTime = this.initServiceTime - this.arrivalTime;
	}
	
	public void overpassed() {
		overpassedCustomers++;
	}
	
	public int getOverpassed() {
		return overpassedCustomers;
	}
	
	public String toString() {
		return "Client ID = " + clientID +
				", Arrival Time = " + arrivalTime +
				", Received Service at Time = " + initServiceTime +
				", Waiting Time = " + waitTime +
				", Overpassed Customers = " + overpassedCustomers;
	}

}
