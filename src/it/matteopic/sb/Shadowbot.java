package it.matteopic.sb;
import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Shadowbot - a robot by Matteo Piccinini
 */
public class Shadowbot extends AdvancedRobot
{
	ScannedRobotEvent lastScan;


	/**
	 * run: Shadowbot's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		setColors(Color.black,Color.black,Color.black); // body,gun,radar

		turn(0);
		// Robot main loop
		double radarAngleAddons = +10;
		while(true) {
			if(lastScan == null || getTime() - lastScan.getTime() > 10){
				//System.out.println("Refresh");
				//if(lastScan != null )System.out.println(getTime() - lastScan. getTime());
				turnRadarRight(360);
			}else{
				//System.out.println(getHeading() + " " + lastScan. getRadarHeading());
				if(getRadarTurnRemaining() == 0){
					turnRadar(lastScan.getBearing() + radarAngleAddons);
					radarAngleAddons *= -1;
				}
				if(getGunTurnRemaining() == 0)
					turnGun(lastScan.getBearing());

				tuneFirePower(lastScan.getDistance(), lastScan.getVelocity());
				execute();
			}
		}
	}

	private void tuneFirePower(double targetDistance, double targetSpeed){
		if(getGunTurnRemaining() > 5 || targetSpeed > 4)return;
		if(targetDistance < 100){
			setFire(Rules.MAX_BULLET_POWER);
		}else{
			setFire(1);
		}
	}

	private void turn(double targetHeading){
		double fromHeading = getHeading();
		double delta = fromHeading - targetHeading;
		if(delta > 180 || delta < -180){
			setTurnRight(360 - delta); 
		}else{
			setTurnLeft(delta);
		}
	}
	
	private void turnRadar(double targetHeading){
		double fromHeading = getRadarHeading();
		double delta = fromHeading - targetHeading;
		if(delta > 180 || delta < -180){
			setTurnRadarRight(360 - delta); 
		}else{
			setTurnRadarLeft(delta);
		}
	}
	
	private void turnGun(double targetHeading){
		double fromHeading = getGunHeading();
		double delta = fromHeading - targetHeading;
		if(delta > 180 || delta < -180){
			setTurnGunRight(360 - delta); 
		}else{
			setTurnGunLeft(delta);
		}
	}
	
	

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		this.lastScan = e;
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	
}
