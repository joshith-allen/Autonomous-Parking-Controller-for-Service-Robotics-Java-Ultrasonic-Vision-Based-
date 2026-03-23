# Autonomous-Parking-Controller-for-Service-Robotics-Java-Ultrasonic-Vision-Based-
An Autonomous Parking System for a service robot using Java within the IRobCon framework. The controller enables a robotic vehicle to detect, evaluate, and execute parking in real time using sensor and valid parking conditions.

This project implements an autonomous parking controller for a robotic vehicle using Java within the IRobCon framework.

The system enables the robot to:
1. Detect parking spaces using ultrasonic sensors
2. Evaluate slot size dynamically
3. Identify parking restrictions using vision (blob detection)
4. Perform a fully automated parking maneuver

# Features
1. Slot Detection
   Detects open parking slots using side ultrasonic sensor
   Confirms slot availability using threshold-based logic
   Measures slot length in real time
2. Parking Restriction Detection
   Uses camera blob detection
   Identifies red/blue markers indicating restricted zones
3. Parking Decision Making
   Chooses parking only if:
   Slot size ≥ required threshold &
   No restriction signs detected
4. Operation Mode
   Tracking Mode - Maintains safe distance while scanning
   Scanning Mode - Measures and evaluates parking slot
5. Collision Avoidance
   Uses front ultrasonic sensor to:
   - Stop when obstacle detected in the pathway and resumes motion when it cleared
   Uses side ultrasonic sensor to:
   - To maintain a clearance from right side of its pathway
6. Parking Routine
   Performs a sequence of :
   i)Reverse Movement
   ii)Steering Adjustments
   iii)Final Alignment

# How it works
1. Robot moves forward while maintaining side distance
2. Detects an opening using side sensor
3. Measures slot length using speed & time
4. Checks for restriction signs via camera
5. Evaluates slot: if
   i)Valid - Perform Parking
   ii)Invalid - Continue Searching



   
