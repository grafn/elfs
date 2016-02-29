package com.ru.usty.elevator;

public class Elevator implements Runnable {
    private int peopleCount;
    @Override
    public void run() {
        if(ElevatorScene.elevatorsMayDie){
            return;
        }
        while(true) {
            ElevatorScene.semaphore1.release(); // signal
        }
    }
}
