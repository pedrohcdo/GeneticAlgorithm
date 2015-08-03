package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import NeuralNetwork.WeightInstancesCollection;

/**
 * Unsupervised Evo
 * 
 * @author user
 *
 */
public class UEvo {

	//
	float mMutationProbability;
	float mMaxMutation;
	float mCrossoverProbability;
	float mDiscardGenomaProbability;
	int mMaxPopulationSize;

	//
	Random mRandom = new Random();

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
	public UEvo(float mp, float mm, float cp, float dgp, int ms) {
		mMutationProbability = mp;
		mMaxMutation = mm;
		mCrossoverProbability = cp;
		mDiscardGenomaProbability = dgp;
		mMaxPopulationSize = ms;
	}

	/**
	 * Evolves Genomas
	 * 
	 * @param genomas
	 * @return
	 */
	public <T extends Genoma> List<T> evolves(List<T> genomas) {
		// Generic genomas list
		List<T> newGenomas = new ArrayList<T>();

		// Sort
		Collections.sort(genomas, new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				if (o2.getFitness() < o1.getFitness())
					return -1;
				else if (o2.getFitness() > o1.getFitness())
					return +1;
				else
					return 0;
			}
		});

		//
		int mutates = 0;
		int crossMutates = 0;
		int cross = 0;
		int creates = 0;

		// Melhores
		for (int i = 0; i < Math.min(8, genomas.size()); i++)
			newGenomas.add(genomas.get(i));

		for (int i = 0; i < Math.min(10, genomas.size()); i++) {
			T genoma = genomas.get(mRandom.nextInt(i+1)%genomas.size());
			if(newGenomas.contains(genoma))
				continue;
			newGenomas.add(genoma);
		}
		
		// Melhores (Mutate)
		for (int i = 0; i < Math.min(8, genomas.size()); i++) {
			if (mRandom.nextBoolean() || mRandom.nextBoolean() || mRandom.nextBoolean()) {
				T genoma = genomas.get(i).copy();
				mutate(genoma);
				newGenomas.add(genoma);
				mutates++;
			}
		}

		// Melhores (Crssover x Mutate)
		for (int i = 0; i < Math.min(3, genomas.size()); i++) {
			for (int j = 0; j < Math.min(3, genomas.size()); j++) {
				if (mRandom.nextBoolean() || mRandom.nextBoolean()) {
					T genomaA = genomas.get(i).copy();
					T genomaB = genomas.get(j).copy();
					crossover(genomaA, genomaB);
					mutate(genomaA);
					mutate(genomaB);
					newGenomas.add(genomaA);
					newGenomas.add(genomaB);
					crossMutates++;
				}
			}
		}

		// Melhores (Crssover)
		for (int i = 0; i < Math.min(3, genomas.size()); i++) {
			for (int j = 0; j < Math.min(3, genomas.size()); j++) {
				if (mRandom.nextBoolean() || mRandom.nextBoolean() || mRandom.nextBoolean()) {
					T genomaA = genomas.get(i).copy();
					T genomaB = genomas.get(j).copy();
					crossover(genomaA, genomaB);
					newGenomas.add(genomaA);
					newGenomas.add(genomaB);
					cross++;
				}
			}
		}

		for (int i = 0; i < Math.min(mRandom.nextInt(10), genomas.size()); i++) {
			T genoma = genomas.get(i).copy();
			random(genoma);
			newGenomas.add(genoma);
			creates++;
		}

		/**
		 * int killed = 0;
		 * 
		 * if(mMaxPopulationSize > 0) { while(newGenomas.size() >
		 * mMaxPopulationSize) { GameGenoma worse = worseGenome(newGenomas);
		 * newGenomas.remove(worse); killed++; } }
		 */

		// println("Killed: " + killed);
		System.out.println("Mutates: " + mutates);
		System.out.println("Cross Mutates: " + crossMutates);
		System.out.println("Cross: " + cross);
		System.out.println("Creates: " + creates);
		System.out.println();

		// discargBadGenomas(newGenomas);

		return newGenomas;
	}

	/**
	 * Discard Bad Genomas
	 * 
	 * @param genomas
	 */
	public <T extends Genoma> void discargBadGenomas(List<T> genomas) {
		T best = getBestGenome(genomas);
		float bestFitness = best.getFitness();

		Iterator<T> itr = genomas.iterator();
		while (itr.hasNext()) {
			T genoma = itr.next();
			if (genoma.getFitness() == -1)
				continue;
			float err = genoma.getFitness() / bestFitness;
			if (err < mDiscardGenomaProbability)
				itr.remove();
		}
	}

	/**
	 * Crossover Genomas
	 * 
	 * @param a
	 * @param b
	 */
	public <T extends Genoma> void crossover(T a, T b) {

		WeightInstancesCollection wic1 = a.getNeural().collectWeigths();
		WeightInstancesCollection wic2 = b.getNeural().collectWeigths();

		int cut = wic1.size() / 20 + mRandom.nextInt(wic1.size() / 10);

		for (int i = (int) Math
				.floor(mRandom.nextFloat() * (wic1.size() - cut)); i < cut; i++) {
			float va = wic1.get(i);
			float vb = wic2.get(i);
			wic1.set(i, vb);
			wic2.set(i, va);
		}

	}

	/**
	 * Mutate Genoma
	 * 
	 * @param genoma
	 */
	public void mutate(Genoma genoma) {
		WeightInstancesCollection wic = genoma.getNeural().collectWeigths();

		int max = (int) (wic.size() * mMaxMutation);
		int attributes = max / 3 + mRandom.nextInt((max * 2) / 3);

		float value = mRandom.nextFloat() * 20 - mRandom.nextFloat() * 20;

		for (int i = 0; i < attributes; i++) {
			int attribute = mRandom.nextInt(wic.size());
			float weigth = wic.get(attribute);
			float adjust = value;

			wic.set(attribute, weigth + adjust);
		}
	}

	/**
	 * Random Genoma
	 * 
	 * @param genoma
	 */
	public void random(Genoma genoma) {
		WeightInstancesCollection wic = genoma.getNeural().collectWeigths();
		for (int i = 0; i < wic.size(); i++) {

			boolean s = mRandom.nextBoolean() || mRandom.nextBoolean();
			float v = wic.get(i);

			wic.set(i, s ? mRandom.nextFloat() * v - mRandom.nextFloat() * v
					: v);
		}
	}

	/**
	 * Get Best Genoma
	 * 
	 * @param genomas
	 * @return
	 */
	public <T extends Genoma> T getBestGenome(List<T> genomas) {
		T best = null;
		float bestFitness = Float.MIN_VALUE;
		for (T genoma : genomas) {
			if (genoma.getFitness() == -1 && best == null) {
				best = genoma;
				continue;
			}
			if (genoma.getFitness() > bestFitness) {
				best = genoma;
				bestFitness = genoma.getFitness();
			}
		}
		return best;
	}

	/**
	 * Get Worse Genoma
	 * 
	 * @param genomas
	 * @return
	 */
	public <T extends Genoma> T worseGenome(List<T> genomas) {
		T worse = null;
		float worseFitness = Float.MAX_VALUE;
		for (T genoma : genomas) {
			if (genoma.getFitness() == -1 && worse == null) {
				worse = genoma;
				continue;
			}
			if (genoma.getFitness() < worseFitness) {
				worse = genoma;
				worseFitness = genoma.getFitness();
			}
		}
		return worse;
	}
}
