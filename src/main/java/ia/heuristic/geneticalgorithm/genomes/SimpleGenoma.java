package ia.heuristic.geneticalgorithm.genomes;

import ia.heuristic.geneticalgorithm.Genoma;
import ia.neuralnetwork.MLP;

/**
 * SimpleGenoma
 *
 * @author Pedro H. (pedrohcd@hotmail.com)
 *
 */
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
	public SimpleGenoma copy() {
		return new SimpleGenoma(this);
	}

	/**
	 * Get Neural
	 */
	@Override
	public MLP getNeural() {
		return mNeural;
	}
}