package NeuralNetwork;

/**
 * Perceptron
 * 
 * @author user
 *
 */
public class Perceptron {

	float[] mWeight;

	/**
	 * Copy Constructor
	 * 
	 * @param copy
	 */
	public Perceptron(Perceptron copy) {
		mWeight = new float[copy.mWeight.length];
		System.arraycopy(copy.mWeight, 0, mWeight, 0, copy.mWeight.length);
	}

	/**
	 * Constructor
	 * 
	 * @param inputs
	 */
	public Perceptron(int inputs) {
		mWeight = new float[inputs + 1];
	}

	/**
	 * Transfer Function
	 * 
	 * @param val
	 * @return
	 */
	public float transfer(float val) {
		return (float) (1.0f / (1 + Math.pow(Math.E, -val)));
	}

	/**
	 * Output
	 * 
	 * @param inputs
	 * @return
	 */
	public float output(float[] inputs) {
		float out = 0;
		for (int i = 0; i < inputs.length; i++) {
			out += mWeight[i + 1] * inputs[i];
		}
		out += mWeight[0];
		return transfer(out);
	}
}