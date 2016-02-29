package com.ru.usty.elevator;

public class Elevator implements Runnable {

    @Override
    public void run() {
        if(ElevatorScene.elevatorMayDie){
            return;
        }
        while(true) {
            ElevatorScene.floorQueue.release(); // signal
        }
    }
}
