package p2MainClasses;

public class Client {
	
	private int clientID;			//Id of this job
	private int arrivalTime;	//Arrival time of this job
	private int remainingTime;	//Remaining service time for this job
	private int departureTime;	//Time when the service for this job is completed
	private int averageTime;
	
	public Client(int id, int at, int rt) {
		clientID = id;
		arrivalTime = at;
		remainingTime = rt;
		averageTime = 0;
	}
	
	public int getDepartureTime() {
		return departureTime;
	}
	
	public void setDepartureTime(int departureTime) {
		this.departureTime = departureTime;
		this.averageTime = this.departureTime - this.arrivalTime;
		
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
	
	public int getAverageTime() {
		return this.averageTime;
	}
	
	public String toString() {
		return "Client ID = " + clientID +
				", Arrival Time = " + arrivalTime +
				", Remaining Time = " + remainingTime +
				", Departure Time = " + departureTime +
				", Average Time = " + averageTime;
	}

}
