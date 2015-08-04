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
	

	
	/**
	 * Setup
	 */
	public void setup() {
		size(640, 480);
		/**
		
		AEvo evo = new AEvo(0.5f, 0.5f, 0.5f, 0.5f, 100);
		
		MLP mlp = new MLP(2, new int[] {8, 8, 8, 1}, TransferFunction.LINEAR);
		mlp.setLayerTF(0, TransferFunction.SIGMOID);
		mlp.setLayerTF(1, TransferFunction.LINEAR);
		mlp.setLayerTF(2, TransferFunction.LINEAR);
		
		evo.addGenoma(new SimpleGenoma(mlp));
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				evo.addSample(new float[] {i, j},  new float[] {i*j});
			}
		}
		for(int i=0; i<1000; i++) {
			evo.evolves();
		}
		
		println((int)Math.floor(evo.getBestGenoma().getNeural().output(new float[] {3.5f, 2.9f})[0]));
		
		exit();*/
		
		race = new ObstacleRace(Configurations.SENSOR_LAYERS, this);
		race.start();
	}

	/**
	 * Save Gene
	 */
	public void mouseClicked() {
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
		}
	}
	
	/**
	 * Draw
	 */
	public void draw() {
		background(0);

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
