package NeuralNetwork;

/**
 * Transfer Function
 * @author user
 *
 */
public class TransferFunction {

	//
	final public static int SIGMOID = 0;
	final public static int GAUSSIAN = 1;
	final public static int LINEAR = 2;
	
	
	/**
	 * Transfer Function
	 * 
	 * @param func
	 * @param x
	 * @return
	 */
	public static float transfer(int func, float x) {
		switch(func) {
		case SIGMOID:
			return sigmoid(x);
		case GAUSSIAN:
			return gaussian(x);
		default:
		case LINEAR:
			return linear(x);
		}
	}
	/**
	 * Sigmoidal Transfer
	 * 
	 * @param x
	 * @return
	 */
	public static float sigmoid(float x) {
		return (float) (1.0f / (1 + Math.pow(Math.E, -x)));
	}
	
	/**
	 * Gaussian Transfer
	 * @param x
	 * @return
	 */
	public static float gaussian(float x) {
		float a = 1;
		return (float)(1.0f / Math.sqrt(2 * Math.PI * a)) * (float)Math.pow(Math.E, - ((x*x) / (2*a*a)));
	}

	/**
	 * Linear transfer
	 * 
	 * @param x
	 * @return
	 */
	public static float linear(float x) {
		return x;
	}
}
