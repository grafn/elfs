package com.ru.usty.elevator;

public class Person implements Runnable{

	int sourceFloor, destinationFloor;
	public Person(int sourceFloor, int destinationFloor){
		this.sourceFloor = sourceFloor;
		this.destinationFloor = destinationFloor;
	}
		
	
	@Override
	public void run() {
		try {
			ElevatorScene.elevatorWaitMutex.acquire();
				ElevatorScene.floorQueue.acquire(); //wait for elevator to arrive
			ElevatorScene.elevatorWaitMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//Person is through
		ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(sourceFloor);
		
		System.out.println("Person thread released");
	}

}


