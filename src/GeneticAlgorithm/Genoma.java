package GeneticAlgorithm;

import NeuralNetwork.MLP;

/**
 * Genoma
 * 
 * @author user
 *
 */
public abstract class Genoma {

	//
	float mFitness = -1;
	float mStagnant = 0;
	
	/**
	 * Reset Stats
	 */
	public void resetStats() {
		mFitness = 0;
		mStagnant = 0;
	}
	
	/**
	 * Increase Fitness
	 * 
	 * @param add
	 */
	public void setFitness(float set) {
		if (set > mFitness)
			mFitness = set;
	}
	
	/**
	 * Set Stagnant
	 * @param set
	 */
	public void setStagnant(float set) {
		if(set > mStagnant)
			mStagnant = set;
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
	 * Get Stagnant
	 * 
	 * @return
	 */
	public float getStagnant() {
		return mStagnant;
	}
	
	/** Copy Function */
	public abstract <T> T copy();

	/** Get MLP */
	public abstract MLP getNeural();
}