package com.ru.usty.elevator;

public class Elevator implements Runnable {

	int CountOfCurrentElevatorRiders = 0;
	int ElevatorSize = 6;
	int CurrentFloor;
	
	private Person[] TheElevator = new Person[ElevatorSize];
	
/*	public Elevator(int ElevatorSize) {
		
		this.ElevatorSize = ElevatorSize;
		for(int i = 0; i < ElevatorSize; i++) {
			TheElevator[i] = null; // B�um til t�man array � byrjun 
			
		}
	}*/
	
    public Elevator(int ElevatorSize) {
		
		this.ElevatorSize = ElevatorSize;
//		for(int i = 0; i < ElevatorSize; i++) {
//			TheElevator[i] = null; // B�um til t�man array � byrjun 	
//		}
	}

	@Override
    public void run() {
        while(true) {
            
        	if(ElevatorScene.scene.elevatorMayDie){
                return;
            }
            
            int AvailableSpace = this.ElevatorSize - ElevatorScene.scene.ElevatorRiders;
            
            // Adding to the elevator while there is room in it
            for(int i = 0; i < (AvailableSpace); i++ ) {
            	ElevatorScene.floorQueueInSemaphore[ElevatorScene.scene.elevatorLocation].release();
            }
            
            try {
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
            
            if( (AvailableSpace) > 0 ) {
            	// Closing the release with an acquire if the elevator is leaving with empty spaces in it
            	for( int n = 0; n < AvailableSpace; n++) {
            		try {
						ElevatorScene.floorQueueInSemaphore[ElevatorScene.scene.elevatorLocation].acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
            
            if(ElevatorScene.scene.elevatorLocation == ( ElevatorScene.scene.getNumberOfFloors() - 1) ) {
            	ElevatorScene.scene.elevatorLocation = 0;
            }
            else {
            	ElevatorScene.scene.MoveElevatorUp();
            }
            
            
            for(int m = 0; m < (ElevatorScene.scene.PeopleCountForDestFloor[ElevatorScene.scene.elevatorLocation]); m++ )  {
            	ElevatorScene.scene.GetTheHellOutSemaphore[m].release();
            }
        }
    }
}
