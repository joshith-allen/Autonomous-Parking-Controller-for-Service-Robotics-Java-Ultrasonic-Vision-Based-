// Name : Joshith Allen M
// Matriculation No: 00164773
package thi.irobcon.app.script;

import thi.irobcon.ecar.ECarDefines;
import thi.irobcon.script.IRobConScript;

public class ServiceRoboticsScripts extends IRobConScript {

	public void script() {
		autoParkingController();
	}

protected void autoParkingController() {

    useRobot(ECarDefines.DX4);

    final int FRONT_ULTRASONIC = 4;
    final int SIDE_ULTRASONIC = 7;   

    final int SIDE_TARGET_DISTANCE = 600;
    final int SIDE_DISTANCE_MARGIN = 50; 
    final int SLOT_OPEN_LIMIT   = 1000;
    final int SLOT_CLOSE_LIMIT  = 800;
    final int OPEN_CONFIRM_HITS = 2;
    final int CLOSE_CONFIRM_HITS = 1; 

    final int REQUIRED_SLOT_SIZE = 900;

    final int FRONT_BRAKE_RANGE = 400;
    final int CRUISING_SPEED   = 150;

    final int MODE_TRACKING = 0;
    final int MODE_SCANNING = 1;

    int currentMode = MODE_TRACKING;

    double slotDistance = 0.0; 
    boolean parkingForbiddenDetected = false; 

    int openHitCounter = 0; 
    int slotCounter = 0; 

    long previousTime = System.currentTimeMillis(); 
    long lastSteeringUpdate = System.currentTimeMillis();
    final long STEERING_INTERVAL_MS = 200;

    long lastStatusPrint = System.currentTimeMillis();
    final long STATUS_INTERVAL_MS = 500;

    panTilt(-90, -5);
    wait(100);

    speed(CRUISING_SPEED);

    while (true) {

        long currentTime = System.currentTimeMillis(); 
        double deltaSeconds = (currentTime - previousTime) / 1000.0; 
        previousTime = currentTime;

        int frontRange = getSonarRange(FRONT_ULTRASONIC);
        int sideRange = getSonarRange(SIDE_ULTRASONIC);

        int redBlobX = getBlobX(1); 
        int blueBlobX = getBlobX(2); 
        boolean signVisible = (redBlobX >= 0 || blueBlobX >= 0); 


        if (frontRange > 0 && frontRange < FRONT_BRAKE_RANGE) { 
            speed(0);
            while (getSonarRange(FRONT_ULTRASONIC) < FRONT_BRAKE_RANGE + 100) { 
                wait(30);
            }
            speed(CRUISING_SPEED);
        }

        if (currentTime - lastSteeringUpdate > STEERING_INTERVAL_MS) { 
            holdSideClearance(sideRange, SIDE_TARGET_DISTANCE, SIDE_DISTANCE_MARGIN); 
            lastSteeringUpdate = currentTime;
        }

        if (currentMode == MODE_TRACKING) { // MODE_TRACKING

            if (sideRange >= SLOT_OPEN_LIMIT) openHitCounter++;
            else openHitCounter = 0;

            if (openHitCounter >= OPEN_CONFIRM_HITS) {
                currentMode = MODE_SCANNING;
                slotCounter++;
                slotDistance = 0;
                parkingForbiddenDetected = false;

                addOutputLn(">>> SLOT " + slotCounter + " DETECTED (side=" + sideRange + ")");
            }

        } else { // MODE_SCANNING

            slotDistance += CRUISING_SPEED * deltaSeconds;

            if (!parkingForbiddenDetected && signVisible) {
                parkingForbiddenDetected = true;
                addOutputLn("SLOT " + slotCounter + ": PARKING RESTRICTION IDENTIFIED");
            }

            if (sideRange <= SLOT_CLOSE_LIMIT) {

                speed(0);
                wait(20);

                addOutputLn("<<< SLOT " + slotCounter + " CLOSED (side=" + sideRange + ")");
                addOutputLn("Measured size: " + (int)slotDistance + " mm");
                addOutputLn("Restricted area: " + (parkingForbiddenDetected ? "YES" : "NO"));

                addOutputLn("Evaluating slot...");
                wait(30);

                if (slotDistance >= REQUIRED_SLOT_SIZE && !parkingForbiddenDetected) {
                    executeParkingRoutine();
                    break;
                } else {
                    currentMode = MODE_TRACKING;
                    openHitCounter = 0;
                    speed(CRUISING_SPEED);
                }
            }
        }


        if (currentTime - lastStatusPrint > STATUS_INTERVAL_MS) {
            addOutputLn("[MODE=" + (currentMode==MODE_TRACKING?"TRACK":"SCAN") +
                "] SIDE=" + sideRange +
                " slotLen=" + (int)slotDistance +
                " redX=" + redBlobX +
                " blueX=" + blueBlobX);
            lastStatusPrint = currentTime;
        }

        wait(20);
    }
}



protected void holdSideClearance(int sideReading, int desiredDistance, int tolerance) {

    int deviation = sideReading - desiredDistance;

    if (deviation > tolerance) {

        turn(2);
        turn(-2);
    }
    else if (deviation < -tolerance) {
        turn(-2);
        turn(2);
    }
}



protected void executeParkingRoutine() {

    addOutputLn("PARKING NOW");


    move(-400);
    wait(5);

    
    turn(45);
    move(-800);
    wait(5);

  
    speed(-100);
    wait(3);
    speed(0);

  
    turn(-45);
    wait(5);

    move(-100);
    wait(5);

   
}
}
