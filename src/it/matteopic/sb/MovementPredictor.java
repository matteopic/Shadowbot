package it.matteopic.sb;

import java.util.ArrayList;
import java.util.List;

import robocode.Robot;
import robocode.ScannedRobotEvent;

public class MovementPredictor {

	private List<Snapshot> events;
	
	public MovementPredictor(){
		events = new ArrayList<Snapshot>();
	}

	public void addEvent(Robot robot, ScannedRobotEvent evt){
		Snapshot s = new Snapshot(robot, evt);
		if(events.size() == 0)events.add(s);
		else{
			events.add(0, s);
		}
	}

	public Prediction predict(double time){
		if(events.size() < 3)return null;

		Snapshot s0 = events.get(0);
		Snapshot s1 = events.get(1);
		Snapshot s2 = events.get(2);
		ScannedRobotEvent evt0 = s0.event;
		ScannedRobotEvent evt1 = s1.event;
		ScannedRobotEvent evt2 = s2.event;
		double deltaH =  - evt1.getHeading();
		if(evt0.getHeading() != evt1.getHeading() || evt0.getHeading() != evt2.getHeading())return null;

		//double delta1 = evt0.getBearing() - evt1.getBearing();
		//double deltaT1 = evt0.getTime() - evt1.getTime();

		//double delta2 = evt0.getBearing() - evt1.getBearing();
		//double deltaT2 = evt0.getTime() - evt1.getTime();
		
		
		
		//double deltaS = evt1.getVelocity() - evt2.getVelocity();

		double deltaT = time - s0.event.getTime();
		double predictedSpace = deltaT * evt0.getVelocity();
	}

	private static class Snapshot {
		public Snapshot(Robot robot, ScannedRobotEvent evt){
			x = robot.getX();
			y = robot.getY();
			velocity = robot.getVelocity();
			heading = robot.getHeading();
			event = evt;
		}

		private double x, y, velocity, heading;
		private ScannedRobotEvent event;
	}

	private class Prediction{
		public int x, y;
		private int time;
	}
}
