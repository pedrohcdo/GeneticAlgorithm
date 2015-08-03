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
	float mFitness = 0;
	
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
	 * Get Fitness
	 * 
	 * @return
	 */
	public float getFitness() {
		return mFitness;
	}

	/** Copy Function */
	public abstract <T> T copy();

	/** Get MLP */
	public abstract MLP getNeural();
}