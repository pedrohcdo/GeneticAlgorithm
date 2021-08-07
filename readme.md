# Neural Network and Generic Algorithms Simulation
This application aims to demonstrate an application of the theory of evolution in the field of artificial intelligence.
The implementations included in the project are:

- **Neural Networks**: MLPs were used as the main implementation
- **Genetic Algorithm**: The main processes were mutation and crossover between genes.
- **Processing**: The Processing library was used to render all simulation objects.

Project Details:

1. Is implemented in Java
2. Uses `Gradle` as build system
3. Uses the `Processing` library to render simulation

## The Simulation

To simulate the Genetic Algorithm, a race simulation was created, where each gene represents a car and all genes need to reach the end of the race to complete the challenge.

Each gene has a neural network that is used to make the gene think of an action. 
Each gene also has a certain fitness that in this application represents the distance until the end of the race. To navigate in the map the gene has 5 sensors, each sensor is triggered from the current position of the gene to the edge of the track, the sensors are used as inputs to the neural network and the output from the neural network represents the angle the gene should look at and and so keep walking in the new direction.

At the beginning of the race x genes are generated in the population, each gene has an initial neural network, at each generation the best genes are selected to carry out crossings and mutations in them and thus new genes are generated.

### Video Sample:
[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/qS6BORzQWkY/0.jpg)](https://www.youtube.com/watch?v=qS6BORzQWkY)

## Installation

```bash
# Download repository
git clone https://github.com/pedrohcdo/Net.git
```

## Getting Started

Cd into the directory and run:

    ./gradlew run

This will start the app on toolbar.  
You can also run the `src.main.java.Main` class as a Java application.

## License

[Apache License 2.0](LICENSE)
