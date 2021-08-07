package ia.neuralnetwork;

import ia.neuralnetwork.interfaces.TransferFunction;
import ia.neuralnetwork.transferfunctions.TransferFunctions;

/**
 * Perceptron
 *
 * @author Pedro H. (pedrohcd@hotmail.com)
 *
 */
public class Perceptron {

	//
	float[] mWeight;
	TransferFunction mTF = TransferFunctions.SIGMOID;
	
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
	public Perceptron(int inputs, TransferFunction transfer) {
		mWeight = new float[inputs + 1];
		mTF = transfer;
	}
	
	/**
	 * Set Transfer Function
	 * @param function
	 */
	public void setTF(TransferFunction function) {
		mTF = function;
	}

	/**
	 * Transfer Function
	 * 
	 * @param x
	 * @return
	 */
	public float transfer(float x) {
		return mTF.transfer(x);
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