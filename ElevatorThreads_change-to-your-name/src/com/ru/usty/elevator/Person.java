package com.ru.usty.elevator;

public class Person implements Runnable{

	int sourceFloor, destinationFloor, elevator;
	
	public Person(int sourceFloor, int destinationFloor){
		
		this.sourceFloor = sourceFloor;
		this.destinationFloor = destinationFloor;
	}
		
	
	@Override
	public void run() {
		try {
				ElevatorScene.floorQueueInSemaphore[this.sourceFloor].acquire(); 
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//Person is through
		ElevatorScene.decrementNumberOfPeopleWaitingAtFloor(this.sourceFloor);
		
		//Person enters elevator
		ElevatorScene.scene.incrementNumberOfPeopleInElevator(0); //finna út hvaða lyfta er að hleypa inní sig
		ElevatorScene.PeopleCountForDestFloor[this.destinationFloor]++;
		
		//Person waits to get out
		try {
			ElevatorScene.GetTheHellOutSemaphore.clone()[this.destinationFloor].acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Person exits elevator
		ElevatorScene.scene.decrementNumberOfPeopleInElevator(0);
		ElevatorScene.PeopleCountForDestFloor[this.destinationFloor]--;
		
		ElevatorScene.personExitsAtFloor(this.destinationFloor);
	}

}
