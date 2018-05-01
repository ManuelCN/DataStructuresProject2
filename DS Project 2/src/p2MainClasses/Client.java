package p2MainClasses;
/**
 * Implementation for clients. Each will contain information of when they will
 * arrive, how long service will need to be provided, when they began to be serviced
 * among other important details.
 * @author	Manuel E. Castañeda
 * 			802-15-1272
 * 			Section 090
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
	/**
	 * @return	Returns whether the client is being served (true) or not (false).
	 */
	public boolean isBeingServed() {
		return beingServed;
	}
	/**
	 * Indicates that the client is now receiving service.
	 */
	public void receivingService() {
		beingServed = true;
	}
	/**
	 * @return	Returns the client's ID.
	 */
	public int getClientID() {
		return clientID;
	}
	/**
	 * @return	Returns the client's arrival time.
	 */
	public int getArrivalTime() {
		return arrivalTime;
	}
	/**
	 * @return	Returns how much service time is left for the client.
	 */
	public int getRemainingTime() {
		return remainingTime;
	}
	/**
	 * Services the client by the given amount. In other words, reduces
	 * the client's remaining time by the given amount.
	 * @param q	The amount of service that was provided to the client.
	 */
	public void isServed(int q) {
		if(q < 0) {	return;	}
		remainingTime -= q;
	}
	/**
	 * @return	Returns the amount of time the client has waited for it
	 * to being receiving service.
	 */
	public int getWaitTime() {
		return this.waitTime;
	}
	/**
	 * @return	Returns the time when the client began to receive service.
	 */
	public int getServiceInitTime() {
		return this.initServiceTime;
	}
	
	/**
	 * Sets when the client began receiving service. Immediately calculates 
	 * the amount of time the client was waiting.
	 * @param servInit	The time unit when the client began receiving
	 * service.
	 */
	public void setServiceInitTime(int servInit) {
		this.initServiceTime = servInit;
		this.waitTime = this.initServiceTime - this.arrivalTime;
	}
	/**
	 * Increases the amount of other clients that have overpassed this client.
	 * In this case, a client overpasses another when it arrived later than the
	 * current client but began receiving service first.
	 */
	public void overpassed() {
		overpassedCustomers++;
	}
	/**
	 * @return	Returns the amount of clients that overpassed this client.
	 */
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
