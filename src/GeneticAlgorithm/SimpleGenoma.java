package GeneticAlgorithm;

import NeuralNetwork.MLP;

public class SimpleGenoma extends Genoma {
	
	MLP mNeural;
	
	/**
	 * Constructor
	 * @param neural
	 */
	public SimpleGenoma(MLP neural) {
		mNeural = neural;
	}
	
	/**
	 * Copy Constructor
	 * @param copy
	 */
	public SimpleGenoma(SimpleGenoma copy) {
		mNeural = new MLP(copy.mNeural);
	}
	
	/**
	 * Copy
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T copy() {
		return (T)(new SimpleGenoma(this));
	}

	/**
	 * Get Neural
	 */
	@Override
	public MLP getNeural() {
		return mNeural;
	}
}