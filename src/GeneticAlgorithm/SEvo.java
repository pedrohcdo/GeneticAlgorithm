package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import NeuralNetwork.MLP;
import NeuralNetwork.WeightInstancesCollection;

/**
 * Supervised Evo
 * 
 * @author user
 *
 */
class SEvo {

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

	//
	List<MLP> mMLPs = new ArrayList<MLP>();
	List<Sample> mSamples = new ArrayList<Sample>();
	Random mRandom = new Random();

	//
	float mMutationProbability;
	float mMaxMutation;
	float mCrossoverProbability;
	float mDiscardGenomaProbability;
	int mMaxPopulationSize;

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
	public SEvo(float mp, float mm, float cp, float dgp, int ms) {
		mMutationProbability = mp;
		mMaxMutation = mm;
		mCrossoverProbability = cp;
		mDiscardGenomaProbability = dgp;
		mMaxPopulationSize = ms;
	}

	/**
	 * Add Genoma to population
	 * 
	 * @param mlp
	 */
	public void addToPopulation(MLP mlp) {
		mMLPs.add(mlp);
	}

	/**
	 * Add Sample
	 * 
	 * @param inputs
	 * @param outputs
	 */
	public void addSample(float[] inputs, float[] outputs) {
		mSamples.add(new Sample(inputs, outputs));
	}

	/**
	 * Population Size
	 * 
	 * @return
	 */
	public int populationSize() {
		return mMLPs.size();
	}

	/**
	 * Next Generation
	 * 
	 * @param count
	 */
	public void nextGeneration(int count) {
		for (int i = 0; i < count; i++) {

			// Mutate
			List<MLP> sons = new ArrayList<MLP>();
			for (MLP mlp : mMLPs) {
				if (samplesError(mlp) <= Float.MIN_VALUE)
					continue;
				boolean mutate = mRandom.nextInt(10000) > (1000 - 10000 * mMutationProbability);
				if (mutate) {
					MLP son = new MLP(mlp);
					mutate(son);
					sons.add(son);
				}
			}

			mMLPs.addAll(sons);

			if (mMaxPopulationSize > 0) {
				while (mMLPs.size() >= mMaxPopulationSize)
					mMLPs.remove(worseGenome());
			}

			discargBadGenomas();
		}
	}

	/**
	 * Discard Bad Genomas
	 * 
	 */
	public void discargBadGenomas() {
		List<MLP> newMLPs = new ArrayList<MLP>();
		float topError = samplesError(bestGenome());

		for (MLP mlp : mMLPs) {
			float err = topError / samplesError(mlp);
			if (err > mDiscardGenomaProbability) {
				newMLPs.add(mlp);
			}
		}
		mMLPs = newMLPs;
	}

	/**
	 * Mutate Genoma
	 * 
	 * @param mlp
	 */
	public void mutate(MLP mlp) {
		WeightInstancesCollection wic = mlp.collectWeigths();

		int attributes = (int) (wic.size() * mMaxMutation);
		for (int i = 0; i < attributes; i++) {
			int attribute = mRandom.nextInt(wic.size());
			float weigth = wic.get(attribute);
			float adjust = mRandom.nextFloat() * 10 - mRandom.nextFloat() * 10;

			wic.set(attribute, weigth + adjust);

		}

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
	 * Best Genome
	 * 
	 * @return
	 */
	public MLP bestGenome() {
		MLP best = null;
		float minError = Float.MAX_VALUE;
		for (MLP mlp : mMLPs) {
			float error = samplesError(mlp);
			if (error < minError) {
				best = mlp;
				minError = error;
			}
		}
		return best;
	}

	/**
	 * Worse Genome
	 * 
	 * @return
	 */
	public MLP worseGenome() {
		MLP worse = null;
		float maxError = Float.MIN_VALUE;
		for (MLP mlp : mMLPs) {
			float error = samplesError(mlp);
			if (error > maxError) {
				worse = mlp;
				maxError = error;
			}
		}
		return worse;
	}
}