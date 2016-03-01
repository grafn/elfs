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
				ElevatorScene.floorQueueInSemaphore[this.sourceFloor].acquire(); 
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//Person is through
		ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(this.sourceFloor);
		
		//Person enters elevator
		ElevatorScene.scene.ElevatorRiders++; //Kannski gera í sér falli með mutex
		ElevatorScene.scene.PeopleCountForDestFloor[this.destinationFloor]++;

		//Person exits elevator
		ElevatorScene.scene.ElevatorRiders--; //Kannski gera í sér falli með mutex
		ElevatorScene.scene.PeopleCountForDestFloor[this.destinationFloor]--;
		
		ElevatorScene.scene.personExitsAtFloor(this.destinationFloor);
	}

}


