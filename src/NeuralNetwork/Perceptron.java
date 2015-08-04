package NeuralNetwork;

/**
 * Perceptron
 * 
 * @author user
 *
 */
public class Perceptron {

	//
	float[] mWeight;
	int mTF = TransferFunction.SIGMOID;
	
	/**
	 * Copy Constructor
	 * 
	 * @param copy
	 */
	public Perceptron(Perceptron copy) {
		mWeight = new float[copy.mWeight.length];
		System.arraycopy(copy.mWeight, 0, mWeight, 0, copy.mWeight.length);
		mTF = copy.mTF;
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
	 * Constructor
	 * 
	 * @param inputs
	 */
	public Perceptron(int inputs, int transfer) {
		mWeight = new float[inputs + 1];
		mTF = transfer;
	}
	
	/**
	 * Set Transfer Function
	 * @param function
	 */
	public void setTF(int function) {
		mTF = function;
	}

	/**
	 * Transfer Function
	 * 
	 * @param val
	 * @return
	 */
	public float transfer(float x) {
		return TransferFunction.transfer(mTF, x);
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