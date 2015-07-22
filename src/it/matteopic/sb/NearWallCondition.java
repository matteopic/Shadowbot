package it.matteopic.sb;

import robocode.Condition;

public class NearWallCondition extends Condition{

	private Shadowbot robot;
	private Coordinates coords;

	public NearWallCondition(Shadowbot robot){
		super("NearWall");
		setPriority(1);
		this.robot = robot;
	}

	@Override
	public boolean test() {
		coords = robot.getNearestWall();
		return coords.getRadius() < 75;
	}

	public Coordinates getCoords() {
		return coords;
	}
	
}
