package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * The base function definitions of this class must stay the same
 * for the test suite and graphics to use.
 * You can add functions and/or change the functionality
 * of the operations at will.
 *
 */

public class ElevatorScene {

	public static Semaphore[] floorQueueInSemaphore;
	
	public static Semaphore[] GetTheHellOutSemaphore;
	
	public static Semaphore personCountMutex;
	
	public static Semaphore elevatorWaitMutex;
	
	public static ElevatorScene scene;
	
	public boolean elevatorMayDie;
	
	public int elevatorLocation = 0; // Gera array af location vísum til að geta unnið með margar lyftur í einu
	
	public int ElevatorRiders = 0;// Gera array fyrir riders
	
	public void MoveElevatorUp() {
		elevatorLocation++;
	}
	
	public void MoveElevatorDown() {
		elevatorLocation--;
	}
	
	public int[] PeopleCountForDestFloor;
	
	
	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 2000;  //milliseconds

	private int numberOfFloors;
	private int numberOfElevators;
	
	//private Thread elevatorThread = null	;
	
	
	
	private Elevator[] elevators; //= new Elevator[numberOfElevators];
	
	ArrayList<Integer> personCount; //use if you want but
									//throw away and
									//implement differently
									//if it suits you

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {
		
//		if(elevatorThread != null){   			// TODO: LOKA ÞEIM ELEVATOR ÞRÁÐUM SEM TIL ERU
//			if(elevatorThread.isAlive()){
//				
//				try {
//					elevatorThread.join();
//					
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		
		elevatorMayDie = true;

		scene = this;
		
		elevators = new Elevator[numberOfElevators];
		//elevatorThread = new Thread();
		
		floorQueueInSemaphore = new Semaphore[numberOfFloors];
		GetTheHellOutSemaphore = new Semaphore[numberOfFloors];
		
		for( int n = 0; n < numberOfElevators; n++) {
			elevators[n] = addElevator();
			System.out.println("Elevator nr.: " + n + " búin til");
		}
		
		for(int i = 0; i < numberOfFloors; i++) {
			floorQueueInSemaphore[i] = new Semaphore(0);
		}
		
		for(int i = 0; i < numberOfFloors; i++) {
			GetTheHellOutSemaphore[i] = new Semaphore(0);
		}

		elevatorMayDie = false;  
 
		
		//floorQueue = new Semaphore(0); //læst í upphafi
		personCountMutex = new Semaphore(1); //einn kemst i gegn og læsist svo
		elevatorWaitMutex = new Semaphore(1);
		
//		elevatorThread = new Thread();
//		elevatorThread.start();
		
		/**
		 * Important to add code here to make new
		 * threads that run your elevator-runnables
		 * 
		 * Also add any other code that initializes
		 * your system for a new run
		 * 
		 * If you can, tell any currently running
		 * elevator threads to stop
		 */
		
		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;

		personCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfFloors; i++) {
			this.personCount.add(0);
		}
		
		if(exitedCount == null) {
			exitedCount = new ArrayList<Integer>();
		}
		else {
			exitedCount.clear();
		}
		for(int i = 0; i < getNumberOfFloors(); i++) {
			this.exitedCount.add(0);
		}
		exitedCountMutex = new Semaphore(1);
	}

	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {
 
		Person person = new Person(sourceFloor, destinationFloor);
		Thread thread = new Thread(person);
		thread.start();
		
		/**
		 * Important to add code here to make a
		 * new thread that runs your person-runnable
		 * 
		 * Also return the Thread object for your person
		 * so that it can be reaped in the testSuite
		 * (you don't have to join() yourself)
		 */	
		
		

		//dumb code, replace it!
		incrementNumberOfPeopleWaitingAtFloor(sourceFloor);
		//System.out.println("Person búin til á hæð " + sourceFloor + " á leiðinni á hæð: "  + destinationFloor );
 		
		return thread;  //this means that the testSuite will not wait for the threads to finish
	}
	
	public Elevator addElevator() {
		
		Elevator elevator = new Elevator(6);
		Thread thread = new Thread(elevator);
		elevator.CurrentFloor = 0;
		
		//if( (personCount[elevator.CurrentFloor]) > 0)
		thread.start();
		
		// halda utan um fjölda lyfta...
		
		
		return elevator;
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {

		return elevatorLocation;
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {
		
		return ElevatorRiders;
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {

		return personCount.get(floor);
	}
	
	public void decrementNumberOfPeopleWaitingAtFloor(int floor){
		try {
			ElevatorScene.personCountMutex.acquire();
				personCount.set(floor,(personCount.get(floor)-1));
			ElevatorScene.personCountMutex.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void incrementNumberOfPeopleWaitingAtFloor(int floor){
		try {
			ElevatorScene.personCountMutex.acquire();
				personCount.set(floor,(personCount.get(floor)+1));
			ElevatorScene.personCountMutex.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Base function: definition must not change, but add your code if needed
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	//Base function: no need to change unless you choose
	//				 not to "open the doors" sometimes
	//				 even though there are people there
	public boolean isElevatorOpen(int elevator) {

		return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {

		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}

	ArrayList<Integer> exitedCount = null;
	public static Semaphore exitedCountMutex;

	public void personExitsAtFloor(int floor) {
		try {
			
			exitedCountMutex.acquire();
				exitedCount.set(floor, (exitedCount.get(floor) + 1));
			exitedCountMutex.release();

		} 
		catch (InterruptedException e) {
				e.printStackTrace();
		}
	}

	public int getExitedCountAtFloor(int floor) {
		if(floor < getNumberOfFloors()) {
			return exitedCount.get(floor);
		}
		else {
			return 0;
		}
	}
}
