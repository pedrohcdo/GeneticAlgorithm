package NeuralNetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Multilayer Perceptron
 * 
 * @author user
 *
 */
public class MLP {

	/**
	 * Layer
	 * 
	 * @author user
	 *
	 */
	class Layer {
		Perceptron[] perceptrons;
	}

	//
	List<Layer> mLayers = new ArrayList<Layer>();

	/**
	 * Copy Constructor
	 * 
	 * @param copy
	 */
	public MLP(MLP copy) {
		for (Layer layer : copy.mLayers) {
			Layer nLayer = new Layer();
			nLayer.perceptrons = new Perceptron[layer.perceptrons.length];
			for (int i = 0; i < layer.perceptrons.length; i++) {
				nLayer.perceptrons[i] = new Perceptron(layer.perceptrons[i]);
			}
			mLayers.add(nLayer);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param inputs
	 * @param layersInfo
	 * @param transferFunction
	 */
	public MLP(int inputs, int[] layersInfo, int transferFunction) {
		for (int layerInfo : layersInfo) {
			Layer layer = new Layer();
			layer.perceptrons = new Perceptron[layerInfo];
			for (int i = 0; i < layerInfo; i++)
				layer.perceptrons[i] = new Perceptron(inputs, transferFunction);
			mLayers.add(layer);
			inputs = layerInfo;
		}
	}
	
	/**
	 * Set Layer Transfer Function
	 * @param layer
	 * @param function
	 */
	public void setLayerTF(int layer, int function) {
		for(Perceptron perceptron : mLayers.get(layer).perceptrons) {
			perceptron.setTF(function);
		}
	}

	/**
	 * Output
	 * 
	 * @param inputs
	 * @return
	 */
	public float[] output(float[] inputs) {
		for (Layer layer : mLayers) {
			float[] out = new float[layer.perceptrons.length];
			for (int i = 0; i < layer.perceptrons.length; i++)
				out[i] = layer.perceptrons[i].output(inputs);
			inputs = out;
		}
		return inputs;
	}

	/**
	 * Error Function
	 * 
	 * @param inputs
	 * @param outputs
	 * @return
	 */
	public float error(float[] inputs, float[] outputs) {
		float[] o2 = output(inputs);
		float error = 0;
		for (int i = 0; i < outputs.length; i++)
			error += Math.pow(outputs[i] - o2[i], 2);
		return error;
	}

	/**
	 * Weight Collection
	 * 
	 * @return
	 */
	public WeightInstancesCollection collectWeigths() {
		WeightInstancesCollection wic = new WeightInstancesCollection();
		for (Layer layer : mLayers) {
			for (Perceptron p : layer.perceptrons)
				wic.add(p.mWeight);
		}
		return wic;
	}

	/**
	 * Save To File
	 * 
	 * @param filename
	 */
	public void saveToFIle(String filename) {
		WeightInstancesCollection wic = collectWeigths();
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			DataOutputStream dos = new DataOutputStream(fos);

			for (int i = 0; i < wic.size(); i++) {
				dos.writeFloat(wic.get(i));
			}

			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load From File
	 * 
	 * @param filename
	 */
	public void loadFromFile(String filename) {
		WeightInstancesCollection wic = collectWeigths();
		try {
			FileInputStream fin = new FileInputStream(filename);
			DataInputStream din = new DataInputStream(fin);

			for (int i = 0; i < wic.size(); i++) {
				wic.set(i, din.readFloat());
			}

			din.close();
		} catch (Exception e) {
		}
	}
}