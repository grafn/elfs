package com.ru.usty.elevator;

public class Elevator implements Runnable {

	int CountOfCurrentElevatorRiders = 0;
	int ElevatorSize = 6;
	int CurrentFloor;
	
/*	public Elevator(int ElevatorSize) {
		
		this.ElevatorSize = ElevatorSize;
		for(int i = 0; i < ElevatorSize; i++) {
			TheElevator[i] = null; // Búum til tóman array í byrjun 
			
		}
	}*/
	
    public Elevator(int ElevatorSize) {
		
		this.ElevatorSize = ElevatorSize;
//		for(int i = 0; i < ElevatorSize; i++) {
//			TheElevator[i] = null; // Búum til tóman array í byrjun 	
//		}
	}

	@Override
    public void run() {
       
		while(true) {
            
        	if(ElevatorScene.elevatorMayDie){
        		System.out.println("Return í Elevator");
                return;
            }
            
            int AvailableSpace = this.ElevatorSize - ElevatorScene.ElevatorRiders;
            int temp = AvailableSpace;
            
            // Adding to the elevator while there is room in it
            for(int i = 0; i < (AvailableSpace); i++ ) {
            	ElevatorScene.floorQueueInSemaphore[ElevatorScene.elevatorLocation].release();
            	temp--;
            	System.out.println("Person " + i + " added to elevator");
            }
            
            try {
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
           
            if( (temp) > 0 ) {
            	// Closing the release with an acquire if the elevator is leaving with empty spaces in it
            	for( int n = 0; n < temp; n++) {
            		try {
						ElevatorScene.floorQueueInSemaphore[ElevatorScene.elevatorLocation].acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
            
            if(ElevatorScene.elevatorLocation == ( ElevatorScene.scene.getNumberOfFloors() - 1) ) {
            	ElevatorScene.elevatorLocation = 0;
            }
            else {
            	ElevatorScene.MoveElevatorUp();
            }
           
            
            for(int m = 0; m < (ElevatorScene.PeopleCountForDestFloor[ElevatorScene.elevatorLocation]); m++ )  {
            	ElevatorScene.GetTheHellOutSemaphore[ElevatorScene.elevatorLocation].release();
            }
        }
    }
}
