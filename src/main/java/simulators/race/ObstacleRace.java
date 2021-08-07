package simulators.race;

import java.util.ArrayList;
import java.util.List;

import ia.neuralnetwork.transferfunctions.TransferFunctions;
import processing.core.PApplet;
import ia.heuristic.geneticalgorithm.Genoma;
import ia.heuristic.geneticalgorithm.ReinforcementLearning;
import ia.neuralnetwork.MLP;

/**
 * ObstacleRace
 *
 * @author Pedro H. (pedrohcd@hotmail.com)
 *
 */
public class ObstacleRace {

	//
	private PApplet mPApplet;
	private int mWindowWidth;
	private int mWindowHeight;

	boolean mStarted = false;
	int mGeneration = 0;
	int[] mSensorLayers;
	float mCurrentDistanceToGoal=0;

	//
	ReinforcementLearning mUEvo = new ReinforcementLearning(0.5f, 0.5f, 0.5f, 0.5f, 100);
	List<GCar> mGenomas = new ArrayList<GCar>();
	Track mTrack;

	/**
	 *
	 */
	public ObstacleRace(PApplet applet, int windowWidth, int windowHeight, int[] sensorLayers, int blockSize, int trackSize, int trackSegment) {
		mPApplet = applet;
		mWindowWidth = windowWidth;
		mWindowHeight = windowHeight;
		mSensorLayers = sensorLayers;
		mTrack = new Track(this, blockSize, trackSize, trackSegment);
		reset();
	}

	/**
	 * Get Applet
	 *
	 * @return
	 */
	protected PApplet getApplet() {
		return mPApplet;
	}

	/**
	 * Get Window Width
	 *
	 * @return
	 */
	protected int getWindowWidth() {
		return mWindowWidth;
	}

	/**
	 * Get Window Height
	 *
	 * @return
	 */
	protected int getWindowHeight() {
		return mWindowHeight;
	}

	/**
	 * Reset
	 */
	public void reset() {
		MLP neuralModel = new MLP(5, mSensorLayers, TransferFunctions.SIGMOID);
		// Set last layer to interpolate on complete turn (2pi)
		neuralModel.setLayerTF(2, x -> TransferFunctions.SIGMOID.transfer(x) * (float)(Math.PI * 2));
		//
		prepareGeneration();
		addGenoma(neuralModel);
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
			genoma.resetToPosition(startPosition[0], startPosition[1], startDirection);
		}
		mTrack.reset();
	}

	/**
	 * Add Genoma
	 *
	 * @param neural
	 */
	public void addGenoma(MLP neural) {
		float[] startPosition = mTrack.getStartPosition();
		float startDirection = mTrack.getStartDirection();
		GCar genoma = new GCar(neural, mPApplet, mTrack);
		genoma.resetToPosition(startPosition[0], startPosition[1], startDirection);
		mGenomas.add(genoma);
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
	 * Update
	 */
	public void draw() {
		mTrack.draw();
		for(GCar genoma : mGenomas)
			genoma.draw();
		//
		GCar bestGenoma = getBestGenoma();

		//
		mPApplet.fill(255, 50);
		mPApplet.rect(20, 20, 200, 115);
		mPApplet.fill(255);
		mPApplet.textSize(20);
		mPApplet.text("Geração " + getGeneration() + ":", 30, 50);
		mPApplet.textSize(15);
		mPApplet.text("  População: " + getPopulationSize(), 40, 70);
		mPApplet.text("  Melhor Gene: " + getBestGenomaId(), 40, 85);
		mPApplet.text("  Melhor Resultado: " + (int)bestGenoma.getFitness(), 40, 100);
		mPApplet.text("  Distancia Atual: " + (int)getCurrentDistanceToGoal(), 40, 115);
	}

	/**
	 * Update
	 */
	public void update() {
		if(!mStarted)
			return;




		boolean endGeneration = true;
		mCurrentDistanceToGoal = Float.MAX_VALUE;

		for(GCar genoma : mGenomas) {
			if(genoma.isBlocked()) {
				float distance = mTrack.getDistanceToGoal(genoma.getX(), genoma.getY());
				mCurrentDistanceToGoal = Math.min(mCurrentDistanceToGoal, distance);
				continue;
			} else {
				genoma.update();
				//
				if(genoma.getStagnantTime() >= 80) {
					genoma.block();
					continue;
				}

				float distance = mTrack.getDistanceToGoal(genoma.getX(), genoma.getY());
				mCurrentDistanceToGoal = Math.min(mCurrentDistanceToGoal, distance);

				if(mTrack.getTotalDistance() - distance > genoma.getFitness())
					genoma.setFitness(mTrack.getTotalDistance() - distance);

				genoma.setBlockId(mTrack.getBlockId(genoma.getX(), genoma.getY()));


			}
			if(!genoma.isBlocked())
				endGeneration = false;
		}

		//
		GCar bestCar = getBestGenoma();
		float maxScrollX = bestCar.getX() - (mWindowWidth / 2);
		float maxScrollY = bestCar.getY() - (mWindowHeight / 2);

		// Set Scroll
		for(GCar genoma : mGenomas)
			genoma.setScroll(maxScrollX, maxScrollY);
		mTrack.setScroll(maxScrollX, maxScrollY);

		if(endGeneration)
			newGeneration();
	}

}
