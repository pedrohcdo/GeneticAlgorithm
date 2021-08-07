import processing.core.PApplet;
import simulators.race.GCar;
import simulators.race.ObstacleRace;

/**
 * Main
 *
 * @author user
 *
 */
public class Main extends PApplet {
	
	// Race
	ObstacleRace race;

	/**
	 * Main
	 */
	static public void main(String[] args) {
		PApplet.main(Main.class.getName());
	}

	/**
	 * Settings
	 */
	public void settings() {
		super.settings();
		size(Configurations.WINDOW_WIDTH, Configurations.WINDOW_HEIGHT);
	}

	/**
	 * Setup
	 */
	public void setup() {
		super.setup();
		//
		race = new ObstacleRace(this,
				Configurations.WINDOW_WIDTH, Configurations.WINDOW_HEIGHT,
				Configurations.SENSOR_LAYERS,
				Configurations.TRACK_BLOCK_SIZE, Configurations.TRACK_SIZE, Configurations.TRACK_SEGMENT);
		//
		race.start();
	}

	
	/**
	 * Draw
	 */
	public void draw() {
		background(0);

		race.update();
		race.draw();
	}
}
