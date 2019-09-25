package nnvisualizer.nn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FFNeuralNetwork implements NeuralNetwork {
	private double[][] activation;
	private double[][][] weights;

	private double lowestWeightValue = Double.MAX_VALUE;
	private double highestWeightValue = Double.MIN_VALUE;

	public static NeuralNetwork[] buildNetworks(final String filePath) {
		try {
			final List<String> lines = Files.readAllLines(Paths.get(filePath));
			final FFNeuralNetwork[] neuralNetworks = new FFNeuralNetwork[lines.size()];
			for (int line = 0; line < lines.size(); line++) {
				neuralNetworks[line] = new FFNeuralNetwork();
				final String[] networkValues = lines.get(line).split(" ");

				final FFNeuralNetwork currentNetwork = neuralNetworks[line];

				int layers = Integer.parseInt(networkValues[0]);
				currentNetwork.activation = new double[layers][];
				currentNetwork.weights = new double[layers - 1][][];

				for (int l = 0; l < layers; l++)
					currentNetwork.activation[l] = new double[Integer.parseInt(networkValues[l + 1])];

				int weightsRead = 0;
				for (int l = 0; l < layers - 1; l++) {
					currentNetwork.weights[l] = new double[Integer.parseInt(networkValues[l + 1])][Integer.parseInt(networkValues[l + 2])];
					for (int n = 0; n < currentNetwork.weights[l].length; n++) {
						for (int nn = 0; nn < currentNetwork.weights[l][n].length; nn++) {
							currentNetwork.weights[l][n][nn] = Double.parseDouble(networkValues[1 + layers + weightsRead++]);
							if (currentNetwork.weights[l][n][nn] > currentNetwork.highestWeightValue)
								currentNetwork.highestWeightValue = currentNetwork.weights[l][n][nn];
							if (currentNetwork.weights[l][n][nn] < currentNetwork.lowestWeightValue)
								currentNetwork.lowestWeightValue = currentNetwork.weights[l][n][nn];
						}
					}
				}
			}

			return neuralNetworks;
		} catch (IOException e) {
			System.err.println("Error reading the file");
			return null;
		}
	}

	public void forward(final double[] input) {

	}

	public double[][] getActivation() {
		return activation;
	}

	public double[][][] getWeights() {
		return weights;
	}

	public double getHighestWeightValue() {
		return highestWeightValue;
	}

	public double getLowestWeightValue() {
		return lowestWeightValue;
	}
}
