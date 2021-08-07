package ia.heuristic.geneticalgorithm;

import ia.neuralnetwork.MLP;

/**
 * Genoma
 *
 * @author Pedro H. (pedrohcd@hotmail.com)
 *
 */
public abstract class Genoma {

	//
	float mFitness = -1;
	float mStagnant = 0;
	int mTime = 0;

	/**
	 * Reset Stats
	 */
	public void resetStats() {
		mFitness = 0;
		mStagnant = 0;
		mTime = 0;
	}
	
	/**
	 * Increase Fitness
	 *
	 * @return
	 */
	public void setFitness(float set) {
		mFitness = set;
	}

	/**
	 * Get Fitness
	 *
	 * @return
	 */
	public float getFitness() {
		return mFitness;
	}


	/**
	 * Set Stagnant
	 *
	 * @return
	 */
	public void setStagnant(float set) {
		if(set > mStagnant)
			mStagnant = set;
	}

	/**
	 * Get Stagnant
	 * 
	 * @return
	 */
	public float getStagnant() {
		return mStagnant;
	}

	/**
	 * Set Stagnant Time
	 * @param time
	 */
	public void setStagnantTime(int time) {
		this.mTime = time;
	}

	/**
	 * Return Stagnant Time
	 *
	 * @return
	 */
	public int getStagnantTime() {
		return mTime;
	}

	/**
	 * Reset Stats
	 */
	public void reset() {
		this.setFitness(0);
		this.setStagnantTime(0);
		this.setFitness(0);
	}

	/** Copy Function */
	public abstract <T extends  Genoma> T copy();

	/** Get MLP */
	public abstract MLP getNeural();
}