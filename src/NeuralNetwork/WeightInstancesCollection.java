package NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Weight Instances Collection
 * 
 * @author user
 *
 */
public class WeightInstancesCollection {

	/**
	 * Weight Instance
	 * 
	 * @author user
	 *
	 */
	class WInstance {

		float[] weigths;

		/**
		 * Constructor
		 * 
		 * @param w
		 */
		public WInstance(float[] w) {
			weigths = w;
		}
	}

	//
	List<WInstance> mInstance = new ArrayList<WInstance>();

	/**
	 * Add instance
	 * 
	 * @param w
	 */
	public void add(float[] w) {
		mInstance.add(new WInstance(w));
	}

	/**
	 * Size
	 * 
	 * @return
	 */
	public int size() {
		int size = 0;
		for (WInstance instance : mInstance) {
			size += instance.weigths.length;
		}
		return size;
	}

	/**
	 * Get value
	 * 
	 * @param i
	 * @return
	 */
	public float get(int i) {
		int j = 0;
		for (WInstance instance : mInstance) {
			for (int k = 0; k < instance.weigths.length; k++) {
				if (j == i)
					return instance.weigths[k];
				j++;
			}
		}
		return 0;
	}

	/**
	 * Set Value
	 * 
	 * @param i
	 * @param v
	 */
	public void set(int i, float v) {
		int j = 0;
		for (WInstance instance : mInstance) {
			for (int k = 0; k < instance.weigths.length; k++) {
				if (j == i) {
					instance.weigths[k] = v;
					return;
				}
				j++;
			}
		}
	}
}