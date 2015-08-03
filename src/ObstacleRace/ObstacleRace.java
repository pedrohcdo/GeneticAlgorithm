package ObstacleRace;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import GeneticAlgorithm.Genoma;
import GeneticAlgorithm.UEvo;
import NeuralNetwork.MLP;

public class ObstacleRace {

	//
	boolean mStarted = false;
	int mGeneration = 0;
	int[] mSensorLayers;
	PApplet mPApplet;
	float mCurrentDistanceToGoal=0;
	Genoma mBestGenoma;
	
	//
	UEvo mUEvo = new UEvo(0.5f, 0.5f, 0.5f, 0.5f, 100);
	List<GCar> mGenomas = new ArrayList<GCar>();
	Track mTrack;
	
	/**
	 * 
	 */
	public ObstacleRace(int[] sensorLayers, PApplet applet) {
		mSensorLayers = sensorLayers;
		mPApplet = applet;
		mTrack = new Track(mPApplet);
		reset();
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		MLP neuralModel = new MLP(5, mSensorLayers);
		prepareGeneration();
		addGenoma(new GCar(neuralModel, mPApplet, mTrack));
		start();
		mTrack.reset();
		mStarted = false;
	}
	
	/**
	 * Prepare Generations
	 */
	public void prepareGeneration() {
		mStarted = false;
		mGenomas.clear();
	}
	
	/**
	 * Add Genoma
	 * 
	 * @param car
	 */
	public void addGenoma(GCar car) {
		mGenomas.add(car);
	}
	
	/**
	 * Start
	 */
	public void start() {
		mStarted = true;
		startGenomas();
	}
	
	/**
	 * Stop
	 */
	public void stop() {
		mStarted = false;
	}
	
	/**
	 * Start Genomas
	 */
	public void startGenomas() {
		float[] startPosition = mTrack.getStartPosition();
		float startDirection = mTrack.getStartDirection();
		for(GCar genoma : mGenomas) {
			genoma.setPosition(startPosition[0], startPosition[1]);
			genoma.setDirection(startDirection);
			genoma.setScroll(0);
			genoma.resetBlock();
			genoma.unblock();
			genoma.setTravelledDistance(0);
		}
		mTrack.reset();
	}
	
	/**
	 * New Generation
	 */
	public void newGeneration() {
		mGeneration++;
		mGenomas = mUEvo.evolves(mGenomas);
		startGenomas();
	}
	
	/**
	 * Get Generation
	 * @return
	 */
	public int getGeneration() {
		return mGeneration;
	}
	
	/**
	 * Get Population Size
	 * @return
	 */
	public int getPopulationSize() {
		return mGenomas.size();
	}
	
	/**
	 * Get Best Fitness
	 * @return
	 */
	public int getBestFitness() {
		return 0;
		
	}
	
	/**
	 * Get Current Distance to Goal
	 * @return
	 */
	public float getCurrentDistanceToGoal() {
		return mCurrentDistanceToGoal;
	}
	
	/**
	 * Get Current Distance to Goal
	 * @return
	 */
	public GCar getBestGenoma() {
		float fitness = Float.MIN_VALUE;
		GCar best = null;
		for(GCar genoma : mGenomas) {
			if(genoma.getFitness() > fitness || best == null) {
				fitness = genoma.getFitness();
				best = genoma;
			}
		}
		return best;
	}

	/**
	 * Get Current Distance to Goal
	 * @return
	 */
	public int getBestGenomaId() {
		float fitness = Float.MIN_VALUE;
		int best = -1;
		for(int i=0; i<mGenomas.size(); i++) {
			GCar genoma = mGenomas.get(i);
			if(genoma.getFitness() > fitness || best == -1) {
				fitness = genoma.getFitness();
				best = i;
			}
		}
		return best;
	}
	
	/**
	 * Get Total Distance to Goal
	 * @return
	 */
	public float getTotalDistanceToGoal() {
		return mTrack.getTotalDistance();
	}
	
	/**
	 * Update
	 */
	public void draw() {
		mTrack.draw();
		for(GCar genoma : mGenomas)
			genoma.draw();
	}
	
	/**
	 * Update
	 */
	public void update() {
		if(!mStarted)
			return;
		
		float maxScroll = 0;
		boolean endGeneration = true;
		mCurrentDistanceToGoal = Float.MAX_VALUE;
		
		for(GCar genoma : mGenomas) {
			if(genoma.isBlocked()) {
				maxScroll = Math.max(maxScroll, genoma.getX() >= 320 ? genoma.getX() - 320 : 0);
				float distance = mTrack.getDistanceToGoal(genoma.getX(), genoma.getY());
				mCurrentDistanceToGoal = Math.min(mCurrentDistanceToGoal, distance);
				continue;
			} else {
				if(genoma.getStagnantTime() >= 150) {
					genoma.block();
					continue;
				}
				genoma.update();
				
				float distance = mTrack.getDistanceToGoal(genoma.getX(), genoma.getY());
				mCurrentDistanceToGoal = Math.min(mCurrentDistanceToGoal, distance);
				
				genoma.setFitness((mTrack.getTotalDistance() - distance));
				genoma.setBlockId(mTrack.getBlockId(genoma.getX(), genoma.getY()));
				maxScroll = Math.max(maxScroll, genoma.getX() >= 320 ? genoma.getX() - 320 : 0);
				
				
			}
			if(!genoma.isBlocked())
				endGeneration = false;
		}
		
		// Set Scroll
		for(GCar genoma : mGenomas)
			genoma.setScroll(maxScroll);
		mTrack.setScroll(maxScroll);
		
		if(endGeneration)
			newGeneration();
	}

}
