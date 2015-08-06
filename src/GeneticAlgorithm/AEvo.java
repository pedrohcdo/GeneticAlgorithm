package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import NeuralNetwork.MLP;

/**
 * Active Evo
 * @author user
 *
 */
public class AEvo {

	/**
	 * Sample
	 * 
	 * @author user
	 *
	 */ 
	class Sample {

		float[] inputs;
		float[] outputs;

		public Sample(float[] i, float[] o) {
			this.inputs = i;
			this.outputs = o;
		}
	}
	
	/**
	 * Comparator
	 */
	Comparator<Genoma> mGenomaComparator = new Comparator<Genoma>() {

		@Override
		public int compare(Genoma o1, Genoma o2) {
			// Get best genoma
			if( Math.abs(o2.getFitness()-o1.getFitness())  <= 20 ) {
				if (o1.getStagnant() < o2.getStagnant())
					return -1;
				else if (o1.getStagnant() > o2.getStagnant())
					return +1;
			}
			
			// Order best fitness
			if (o2.getFitness() < o1.getFitness())
				return -1;
			else if (o2.getFitness() > o1.getFitness())
				return +1;
			else
				return 0;
		}
	};
	
	//
	UEvo mUEvo;
	List<Sample> mSamples = new ArrayList<Sample>();
	List<Genoma> mGenomas = new ArrayList<Genoma>();
	
	/**
	 * Constructor
	 * 
	 * @param mp
	 *            Mutation Probability
	 * @param mm
	 *            Max Mutation
	 * @param cp
	 *            Crossover Probability
	 * @param dgp
	 *            DiscardGenoma Probability
	 * @param ms
	 *            Max PopulationSize
	 */
	public AEvo(float mp, float mm, float cp, float dgp, int ms) {
		mUEvo = new UEvo(mp, mm, cp, dgp, ms);
	}

	/**
	 * Add Sample
	 * @param inputs
	 * @param outputs
	 */
	public void addSample(float[] inputs, float[] outputs) {
		mSamples.add(new Sample(inputs, outputs));
	}
	
	/**
	 * Add Genoma
	 * @param genoma
	 */
	public void addGenoma(Genoma genoma) {
		mGenomas.add(genoma);
	}
	
	/**
	 * Samples Error
	 * 
	 * @param mlp
	 * @return
	 */
	public float samplesError(MLP mlp) {
		float error = 0;
		for (Sample s : mSamples)
			error += mlp.error(s.inputs, s.outputs);
		error /= mSamples.size();
		if (error == 0)
			error = Float.MIN_VALUE;
		return error;
	}
	
	/**
	 * Evolves
	 * @param genomas
	 * @return
	 */
	public void evolves() {
		for(Genoma genoma : mGenomas) { 
			float error = 1 / (samplesError(genoma.getNeural()) + 1);
			genoma.resetStats();
			genoma.setFitness(error);
		}
		mGenomas = mUEvo.evolves(mGenomas);
		Collections.sort(mGenomas, mGenomaComparator);
	}
	
	/**
	 * Get Population Size
	 * 
	 * @return
	 */
	public int getPopulationSize() {
		return mGenomas.size();
	}
	
	/**
	 * Get Best Genoma
	 */
	public Genoma getBestGenoma() {
		return mGenomas.get(0);
	}
}
