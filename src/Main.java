import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import GeneticAlgorithm.AEvo;
import GeneticAlgorithm.SimpleGenoma;
import NeuralNetwork.MLP;
import NeuralNetwork.TransferFunction;
import ObstacleRace.GCar;
import ObstacleRace.ObstacleRace;

/**
 * 
 * @author user
 *
 */
public class Main extends PApplet {
	
	// Race
	ObstacleRace race;
	float time = 0;
	int saveId = 0;
	
	
	
	AEvo evo;
	
	/**
	 * Setup
	 */
	public void setup() {
		size(640, 480);
		frameRate(Integer.MAX_VALUE);
		
		MLP mlp = new MLP(1, new int[] {8, 8, 8, 1}, TransferFunction.SIGMOID);
		mlp.setLayerTF(0, TransferFunction.SIGMOID);
		mlp.setLayerTF(1, TransferFunction.SIGMOID);
		mlp.setLayerTF(2, TransferFunction.SIGMOID);
		mlp.setLayerTF(3, TransferFunction.LINEAR);
		mlp.loadFromFile("sin.data");

		evo = new AEvo(0.5f, 0.5f, 0.5f, 0.5f, 100);
		evo.addGenoma(new SimpleGenoma(mlp));
		for(float i=0; i<=Math.PI*2; i+=0.01f) {
			evo.addSample(new float[] {i}, new float[] {(float)Math.cos(i)});
		}

		for(int i=0; i<10000; i++) {
			evo.evolves();
		}
		
		//
		evo.getBestGenoma().getNeural().saveToFIle("sin.data");


		
		
		
		
		race = new ObstacleRace(Configurations.SENSOR_LAYERS, this);
		race.start();
	}

	/**
	 * Save Gene
	 */
	public void mouseClicked() {
		evo.getBestGenoma().getNeural().saveToFIle("sin.data");
		/**
		if(mouseButton == 37) {
			for(GCar genoma : race.getGenomas()) {
				println();
				if(genoma.isOver(mouseX, mouseY)) {
					genoma.getNeural().saveToFIle("C:\\Users\\user\\git\\netga\\Net\\genoma" + saveId++ + ".data");
					break;
				}
			}
		} else if(mouseButton == 39) {
			MLP neural = new MLP(5, Configurations.SENSOR_LAYERS, TransferFunction.SIGMOID);
			neural.loadFromFile("C:\\Users\\user\\git\\netga\\Net\\genomaBest.data");
			race.addGenoma(neural);
		}*/
	}
	
	/**
	 * Draw
	 */
	public void draw() {
		background(0);

		stroke(255);
		
		evo.evolves();
		
		text("Error: " + evo.samplesError(evo.getBestGenoma().getNeural()), 50, 50);
		text("Population: " + evo.getPopulationSize(), 50, 65);
		
		float r = 100;
		float lx = 320 + evo.getBestGenoma().getNeural().output(new float[] {0})[0] * r;
		float ly = 240 + evo.getBestGenoma().getNeural().output(new float[] {(float)Math.PI*2-(float)Math.PI/2})[0] * r;
		
		for(float f=0; f<Math.PI*2; f+=0.01f) {
			float x = 320 + evo.getBestGenoma().getNeural().output(new float[] {f})[0] * r;
			float y = 240 + (float)Math.sin(f) * r;
			
			line(lx, ly, x, y);
			
			lx = x;
			ly = y;
			
		}
		
		
		if(true)
			return;
		
		race.draw();
		race.update();
		
		GCar bestGenoma = race.getBestGenoma();
		
		
		//
		fill(255, 50);
		rect(20, 20, 200, 115);
		fill(255);
		textSize(20);
		text("Geração: " + race.getGeneration(), 30, 50);
		textSize(15);
		text("População: " + race.getPopulationSize(), 40, 70);
		text("Melhor Gene: " + race.getBestGenomaId(), 40, 85);
		text("Melhor Resultado: " + (int)bestGenoma.getFitness(), 40, 100);
		text("Distancia Atual: " + (int)race.getCurrentDistanceToGoal(), 40, 115);
	}
}
