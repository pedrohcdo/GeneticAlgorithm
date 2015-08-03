package ObstacleRace;

import processing.core.PApplet;
import GeneticAlgorithm.Genoma;
import NeuralNetwork.MLP;

/**
 * Car
 * 
 * @author user
 *
 */
public class GCar extends Genoma {

	//
	float[] mPosition = new float[2];
	float[] mSensorDistances = new float[5];
	float mDirection = 0;
	float mSpeed = 1;
	float mRadius = 20;
	float mScroll = 0;
	int mLastBlockId;
	int mTime;
	boolean mBlock = false;
	float mTravelledDistance = 0;
	
	MLP mNeural;
	PApplet mApplet;
	Track mTrack;
	
	/**
	 * Constructor
	 * 
	 * @param neural
	 * @param sensorDistance
	 */
	public GCar(MLP neural, PApplet applet, Track track) {
		mNeural = neural;
		mApplet = applet;
		mTrack = track;
		
		setSpeed(5);
		setDirection(0);
	}

	/**
	 * Copy Constructor
	 * 
	 * @param copy
	 */
	public GCar(GCar copy) {
		mNeural = new MLP(copy.mNeural);
		mApplet = copy.mApplet;
		mTrack = copy.mTrack;
		
		setSpeed(copy.getSpeed());
		setDirection(copy.getDirection());
	}

	/**
	 * Get X
	 * 
	 * @return
	 */
	public float getX() {
		return mPosition[0];
	}

	/**
	 * Get Y
	 * 
	 * @return
	 */
	public float getY() {
		return mPosition[1];
	}

	/**
	 * Set Position
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		mPosition[0] = x;
		mPosition[1] = y;
	}

	/**
	 * Set Speed
	 * 
	 * @param speed
	 */
	public void setSpeed(float speed) {
		mSpeed = speed;
	}

	/**
	 * Set Scroll
	 * 
	 * @param scroll
	 */
	public void setScroll(float scroll) {
		mScroll = scroll;
	}
	
	/**
	 * Get Speed
	 * 
	 * @return
	 */
	public float getSpeed() {
		return mSpeed;
	}

	/**
	 * Set Direction
	 * 
	 * @param d
	 */
	public void setDirection(float d) {
		mDirection = d;
	}
	
	/**
	 * Set Travelled Distance
	 * @param t
	 */
	public void setTravelledDistance(float t) {
		mTravelledDistance = t;
	}
	
	/**
	 * Get Travelled Distance
	 * @return
	 */
	public float getTravelledDistance() {
		return mTravelledDistance;
	}
	
	/**
	 * Block
	 */
	public void block() {
		mBlock = true;
	}
	
	/**
	 * Unblock
	 */
	public void unblock() {
		mBlock = false;
	}
	
	/**
	 * Is Blocked
	 * @return
	 */
	public boolean isBlocked() {
		return mBlock;
	}
	
	/**
	 * Set Block ID
	 * @param id
	 */
	public void setBlockId(int id) {
		if(id <= mLastBlockId) {
			mTime++;
		} else {
			mLastBlockId = id;
			mTime = 0;
		}
	}
	
	/**
	 * Reset Block
	 */
	public void resetBlock() {
		mLastBlockId = -1;
		mTime = 0;
	}
	
	/**
	 * Return Stagnant Time
	 * @return
	 */
	public int getStagnantTime() {
		return mTime;
	}

	/**
	 * Get Direction
	 * 
	 * @return
	 */
	public float getDirection() {
		return mDirection;
	}

	/**
	 * Update
	 * 
	 */
	public void update() {
		// Upate Direction
		mDirection += (float)(Math.PI * 2) * mNeural.output(mSensorDistances)[0];
		
		// Update Position
		float lx = mPosition[0];
		float ly = mPosition[1];
		float mx = lx + (float) Math.cos(mDirection) * mSpeed;
		float my = ly + (float) Math.sin(mDirection) * mSpeed;
		float[] cp = mTrack.clampPosition(lx, ly, mx, my);
		mPosition[0] = cp[0];
		mPosition[1] = cp[1];
		mTravelledDistance += Math.hypot(lx - mx, ly - my);
		
		// Update Sensor Distances
		float incr = (float)(Math.PI / 4);
		float rad = mDirection + incr * 2;
		float dx = (float) Math.cos(rad-incr/4);
		float dy = (float) Math.sin(rad-incr/4);
		mSensorDistances[0] = mTrack.getRayDistance(getX(), getY(), dx, dy);
		rad -= incr/2;
		for(int i=0; i<3; i++) {
			dx = (float) Math.cos(rad-incr/2);
			dy = (float) Math.sin(rad-incr/2);
			mSensorDistances[i+1] = mTrack.getRayDistance(getX(), getY(), dx, dy);
			rad -= incr;
		}
		dx = (float) Math.cos(rad-incr/4);
		dy = (float) Math.sin(rad-incr/4);
		mSensorDistances[4] = mTrack.getRayDistance(getX(), getY(), dx, dy);
	}

	/**
	 * Draw
	 */
	public void draw() {
		// Draw Sensors
		
		mApplet.noStroke();
		mApplet.fill(255,  100);
		mApplet.stroke(255);
		float incr = (float)(Math.PI / 4);
		float rad = mDirection + incr * 2;
		float dsx = (float) Math.cos(rad-incr/4) ;
		float dsy = (float) Math.sin(rad-incr/4) ;
		mApplet.line(getX() + dsx * mRadius - mScroll, getY() + dsy * mRadius, dsx * mSensorDistances[0] + getX() - mScroll, dsy * mSensorDistances[0] + getY());
		float xStart = (float) Math.cos(rad) * mRadius + mPosition[0];
		float yStart = (float) Math.sin(rad) * mRadius + mPosition[1];
		float xEnd = (float) Math.cos(rad-incr/2) * mRadius + mPosition[0];
		float yEnd = (float) Math.sin(rad-incr/2) * mRadius + mPosition[1];
		mApplet.triangle(getX() - mScroll, getY(), xStart - mScroll, yStart, xEnd - mScroll, yEnd);
		rad -= incr/2;
		for(int i=0; i<3; i++) {
			xStart = (float) Math.cos(rad) * mRadius + mPosition[0];
			yStart = (float) Math.sin(rad) * mRadius + mPosition[1];
			xEnd = (float) Math.cos(rad-incr) * mRadius + mPosition[0];
			yEnd = (float) Math.sin(rad-incr) * mRadius + mPosition[1];
			mApplet.triangle(getX() - mScroll, getY(), xStart - mScroll, yStart, xEnd - mScroll, yEnd);
			
			dsx = (float) Math.cos(rad-incr/2);
			dsy = (float) Math.sin(rad-incr/2);
			mApplet.line(getX() + dsx * mRadius - mScroll, getY() + dsy * mRadius, dsx * mSensorDistances[i+1] + getX() - mScroll, dsy * mSensorDistances[i+1] + getY());
			rad -= incr;
		}
		xStart = (float) Math.cos(rad) * mRadius + mPosition[0];
		yStart = (float) Math.sin(rad) * mRadius + mPosition[1];
		xEnd = (float) Math.cos(rad-incr/2) * mRadius + mPosition[0];
		yEnd = (float) Math.sin(rad-incr/2) * mRadius + mPosition[1];
		mApplet.triangle(getX() - mScroll, getY(), xStart - mScroll, yStart, xEnd - mScroll, yEnd);
		dsx = (float) Math.cos(rad-incr/4) ;
		dsy = (float) Math.sin(rad-incr/4) ;
		mApplet.line(getX() + dsx * mRadius - mScroll, getY() + dsy * mRadius, dsx * mSensorDistances[4] + getX() - mScroll, dsy * mSensorDistances[4] + getY());
		
		// Draw Body
		mApplet.noStroke();;
		mApplet.fill(255, 255, 0);
		mApplet.ellipse(getX() - mScroll, getY(), mRadius, mRadius);
	}

	/**
	 * Copy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T copy() {
		return (T) (new GCar(this));
	}

	/**
	 * Get Neural
	 */
	@Override
	public MLP getNeural() {
		return mNeural;
	}
}
