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
	
	public static Semaphore elevatorCountMutex;
	
	public static ElevatorScene scene;
	
	public static boolean elevatorMayDie;
	
	public static int[] elevatorLocation; // Gera array af location v�sum til a� geta unni� me� margar lyftur � einu
	
	public static int[] elevatorRiders;// Gera array fyrir riders
	
	private Thread elevatorThread = null;
	
	public static void MoveElevatorUp(int elevator) {
		elevatorLocation[elevator]++;
	}
	
	public void MoveElevatorDown(int elevator) {
		elevatorLocation[elevator]--;
	}
	
	public static int[] PeopleCountForDestFloor;
	
	
	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 1000;  //milliseconds

	private static int numberOfFloors;
	private int numberOfElevators;	
	
	public static Elevator[] elevators;
	
	static ArrayList<Integer> personCount; //use if you want but
									//throw away and
									//implement differently
									//if it suits you

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {
		
		elevatorMayDie = true;
		
		if(elevatorThread != null){   			
			if(elevatorThread.isAlive()){
				
				try {
					elevatorThread.join();
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		elevatorMayDie = false;

		scene = this;
		
		elevators = new Elevator[numberOfElevators];
		PeopleCountForDestFloor = new int[numberOfFloors];
		elevatorLocation = new int[numberOfElevators];
		elevatorRiders = new int[numberOfElevators];
		
		//elevatorThread = new Thread();
		
		floorQueueInSemaphore = new Semaphore[numberOfFloors];
		GetTheHellOutSemaphore = new Semaphore[numberOfFloors];
		
		for(int i = 0; i < numberOfElevators; i++) {
			elevatorLocation[i] = 0;
		}
		
		for(int i = 0; i < numberOfElevators; i++) {
			elevatorRiders[i] = 0;
		}
		
		for(int i = 0; i < numberOfFloors; i++) {
			floorQueueInSemaphore[i] = new Semaphore(0);
		}
		
		for(int i = 0; i < numberOfFloors; i++) {
			GetTheHellOutSemaphore[i] = new Semaphore(0);
		}

		for(int i = 0; i < numberOfFloors; i++) {
			PeopleCountForDestFloor[i] = 0;
		}
		
		int q = 0;
		for( int n = 0; n < numberOfElevators; n++) {
			elevators[n] = addElevator(n);
			elevatorLocation[n] = 0;
//			if ( numberOfElevators <= numberOfFloors ) {
//				elevatorLocation[n] = n;
//			}
//			else {
//				elevatorLocation[n] = q;
//				q++;
//				if(q == numberOfFloors) {
//					q = 0;
//				}
//			}
//			System.out.println("Elevator nr.: " + n + " b�in til");
		}
		
		
		//elevatorMayDie = true;
		
		elevatorMayDie = false;  
 
		
		//floorQueue = new Semaphore(0); //l�st � upphafi
		personCountMutex = new Semaphore(1); //einn kemst i gegn og l�sist svo
		elevatorCountMutex = new Semaphore(1);
		
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
		
		ElevatorScene.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;

		personCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfFloors; i++) {
			ElevatorScene.personCount.add(0);
		}
		
		if(exitedCount == null) {
			exitedCount = new ArrayList<Integer>();
		}
		else {
			exitedCount.clear();
		}
		for(int i = 0; i < getNumberOfFloors(); i++) {
			ElevatorScene.exitedCount.add(0);
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
		//System.out.println("Person b�in til � h�� " + sourceFloor + " � lei�inni � h��: "  + destinationFloor );
 		
		return thread;  //this means that the testSuite will not wait for the threads to finish
	}
	
	public Elevator addElevator(int i) {
		
		Elevator elevator = new Elevator(6,i);
		elevatorThread = new Thread(elevator);
		
		elevatorThread.start();
		
		// halda utan um fj�lda lyfta...
		
		
		return elevator;
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {

		return elevatorLocation[elevator];
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {
		
		return elevatorRiders[elevator];
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {

		return personCount.get(floor);
	}
	
	public static void decrementNumberOfPeopleWaitingAtFloor(int floor){
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
	
	public void incrementNumberOfPeopleInElevator(int elevator){
		try {
			ElevatorScene.elevatorCountMutex.acquire();
				elevatorRiders[elevator]++;
			ElevatorScene.elevatorCountMutex.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void decrementNumberOfPeopleInElevator(int elevator){
		try {
			ElevatorScene.elevatorCountMutex.acquire();
				elevatorRiders[elevator]--;
			ElevatorScene.elevatorCountMutex.release();
			
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
		ElevatorScene.numberOfFloors = numberOfFloors;
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
	
	public int findAvailableElevator(int sourceFloor) {
		for(int i = 0; i < numberOfElevators; i++) {
			if(elevators[i].CurrentFloor == sourceFloor) {
				if(elevatorRiders[i] < elevators[i].ElevatorSize) {
					return i;
				}
			}
		}
		return -1;
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {

		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}

	static ArrayList<Integer> exitedCount = null;
	public static Semaphore exitedCountMutex;

	public static void personExitsAtFloor(int floor) {
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
