package com.ru.usty.elevator;

public class Person implements Runnable{

	int sourceFloor, destinationFloor, elevatorIndex ;
	
	public Person(int sourceFloor, int destinationFloor) {
		
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
		elevatorIndex = 0;
		
		for( int n = 0; n < ElevatorScene.scene.getNumberOfElevators(); n++ ) {
			if( (ElevatorScene.elevatorRiders[elevatorIndex] < 6) ) {
				elevatorIndex = n;
				break;
			}
			if( (ElevatorScene.elevatorRiders[elevatorIndex] >= 6 ) && (( (elevatorIndex + 1) < ElevatorScene.elevators.length))  ) {
				elevatorIndex++;
			}
		}
		
		//&& (elevatorIndex < ElevatorScene.elevators.length)
		
		//Person is through
		ElevatorScene.decrementNumberOfPeopleWaitingAtFloor(this.sourceFloor);
		
		//Person enters elevator
		
		//elevatorIndex = ElevatorScene.scene.findAvailableElevator(this.sourceFloor);
		
		//if (elevatorIndex >= 0) {
		
			ElevatorScene.scene.incrementNumberOfPeopleInElevator(elevatorIndex); 
			ElevatorScene.PeopleCountForDestFloor[this.destinationFloor]++;
			
			//Person waits to get out
			try {
				ElevatorScene.GetTheHellOutSemaphore.clone()[this.destinationFloor].acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			//Person exits elevator
			ElevatorScene.scene.decrementNumberOfPeopleInElevator(elevatorIndex);
			ElevatorScene.PeopleCountForDestFloor[this.destinationFloor]--;
			
			//ElevatorScene.exitedCount.set(this.destinationFloor, (ElevatorScene.exitedCount.get(this.destinationFloor) + 1) );   //[this.destinationFloor]++;
			
			ElevatorScene.personExitsAtFloor(this.destinationFloor);
		
		}
	}


