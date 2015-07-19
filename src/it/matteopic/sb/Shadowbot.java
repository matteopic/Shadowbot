package it.matteopic.sb;
import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.CustomEvent;
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
	Coordinates enemyCoordinates;
	NearWallCondition nearWall;
	HitByBulletEvent hitEvent;

	/**
	 * run: Shadowbot's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		setColors(Color.black,Color.black,Color.black); // body,gun,radar
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);

		addCustomEvent(new NearWallCondition(this));

		// Robot main loop
		double radarAngleAddons = +5;
		while(true) {
			if(lastScan == null || getTime() - lastScan.getTime() > 10){
				turnRadarRight(360);
			}else{
				Coordinates enemyCoords = pointToEnemy();
				//Coordinates nearestWall = getNearestWall();

				if(nearWall != null){
					escape(nearWall.getCoords());
					nearWall = null;
				}else if(hitEvent != null){
					escape(enemyCoords);
				}
				else{
					double distance = enemyCoords.getRadius();
					if(distance > 100){
						turn(enemyCoords.getTheta());
					}else{
						turnAround(lastScan);
					}
				}

				setAhead(10);
				
				if(getRadarTurnRemaining() == 0){
					turnRadar(enemyCoords.getTheta() + radarAngleAddons);
					radarAngleAddons *= -1;
				}
				if(getGunTurnRemaining() == 0){
					turnGun(enemyCoords.getTheta());
				}
				tuneFirePower(lastScan.getDistance(), lastScan.getVelocity());
				execute();
			}

			
		}
	}

	Coordinates getNearestWall(){
		double w = getBattleFieldWidth();
		double h = getBattleFieldHeight();
		double centerX = w / 2;
		double centerY = h / 2;
		double x = getX();
		double y = getY();
		
		double deltaX = x > centerX ? w - x : x;
		double deltaY = y > centerY ? h - y : y;
		double targetAngle;
		double radius;
		if(deltaX < deltaY){
			radius = deltaX;
			targetAngle = x > centerX ? 90 : 270;
		}else{
			radius = deltaY;
			targetAngle = y > centerY ? 0 : 180;
		}
		//System.out.printf("x:%s y:%s w:%s h:%s \u237a%s°\n", x, y, w, h, targetAngle);
		//System.out.printf("\u0394x:%s \u0394y:%s \u237a:%s\n", deltaX, deltaY, targetAngle);
		double theta = 360 - Math.abs(getHeading() - targetAngle); 
		
		return Coordinates.polar(radius, theta);
	}

	private Coordinates pointToEnemy() {
		double x = enemyCoordinates.getX() - getX();
		double y = enemyCoordinates.getY() - getY();
		Coordinates c = Coordinates.cartesian(x, y);
		return c;
	}

	private void tuneFirePower(double targetDistance, double targetSpeed){
		if(getGunTurnRemaining() > 5 || targetSpeed >= 5)return;
		if(targetDistance < 150 || targetSpeed == 0){
			setFire(Rules.MAX_BULLET_POWER);
		}else if(targetSpeed < 4){
			setFire(1);
		}
	}
	
	private void turnAround(ScannedRobotEvent evt){
		double bearing = evt.getBearing();
		if(bearing > 90 || bearing < -90 ){
			setTurnRight(bearing - 90);
		}else{
			setTurnLeft(90 - bearing);
		}
	}
	
	private void escape(Coordinates coords){
		double bearing = coords.getTheta();
		if(bearing < 90){
			setTurnLeft(90);
		}else if(bearing > 270){
			setTurnRight(90);
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
	
	@Override
	public void onCustomEvent(CustomEvent event) {
		nearWall = (NearWallCondition)event.getCondition();
	}
	

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		//System.out.println("scanned");
		this.lastScan = e;
		Coordinates enemyScanCoords = Coordinates.polar(e.getDistance(), getHeading() + e.getBearing());
		enemyCoordinates = Coordinates.cartesian(enemyScanCoords.getX() + getX(), enemyScanCoords.getY() + getY()); 
		//System.out.println(enemyCoordinates);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		this.hitEvent = e;
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		System.out.printf("Hit Wall ; %s°\n",e.getBearing());
//		Coordinates nearestWall = getNearestWall();
//		System.out.printf("Nearest Wall: %s°\n",  nearestWall);
		// Replace the next line with any behavior you would like
		//back(20);
	}	
}
