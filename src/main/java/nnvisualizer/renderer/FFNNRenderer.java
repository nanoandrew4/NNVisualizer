package nnvisualizer.renderer;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import nnvisualizer.Main;
import nnvisualizer.nn.FFNeuralNetwork;
import nnvisualizer.nn.NeuralNetwork;

public class FFNNRenderer implements NNRenderer {

	@Override
	public void renderNetwork(final Pane pane, final NeuralNetwork neuralNetwork) {
		pane.getChildren().clear();
		final FFNeuralNetwork ffNeuralNetwork = (FFNeuralNetwork) neuralNetwork;

		final Circle[][] neurons = new Circle[ffNeuralNetwork.getActivation().length][];
		for (int l = 0; l < ffNeuralNetwork.getActivation().length; l++) {
			neurons[l] = new Circle[ffNeuralNetwork.getActivation()[l].length];
			for (int n = 0; n < ffNeuralNetwork.getActivation()[l].length; n++) {
				neurons[l][n] = generateCircle(ffNeuralNetwork, l, n);
				pane.getChildren().add(neurons[l][n]);

				if (l > 0) {
					for (int pn = 0; pn < ffNeuralNetwork.getActivation()[l - 1].length; pn++) {
						final Circle startNeuron = neurons[l - 1][pn];
						final Circle endNeuron = neurons[l][n];
						final Line line = new Line(startNeuron.getCenterX() + startNeuron.getRadius(), startNeuron.getCenterY(),
												   endNeuron.getCenterX() - endNeuron.getRadius(), endNeuron.getCenterY());

						final double normalizedWeight = ffNeuralNetwork.getWeights()[l - 1][pn][n] / ffNeuralNetwork.getHighestWeightValue();
						line.setStroke(Color.gray(1 - normalizedWeight));
						pane.getChildren().add(line);
					}
				}
			}
		}
	}

	private Circle generateCircle(final FFNeuralNetwork ffNeuralNetwork, int layer, int neuronInLayer) {
		final Circle neuron = new Circle(Main.SCREEN_HEIGHT / (ffNeuralNetwork.getActivation()[0].length * 4d));
		final double circleRadiiMaxWidth = (Main.SCREEN_WIDTH - neuron.getRadius() * 2);
		final double circleRadiiMaxHeight = (Main.SCREEN_HEIGHT - neuron.getRadius() * 2);

		double cx = (((double) layer / ffNeuralNetwork.getActivation().length)) * circleRadiiMaxWidth;
		double cy = (((double) neuronInLayer / ffNeuralNetwork.getActivation()[layer].length)) * circleRadiiMaxHeight;

		final double lastCircleRadiiX = (((double) (ffNeuralNetwork.getActivation().length - 1) / (ffNeuralNetwork.getActivation().length)) * circleRadiiMaxWidth);
		double xOffset = (circleRadiiMaxWidth - lastCircleRadiiX + neuron.getRadius()) / 2d;

		final double lastCircleRadiiY = (((double) (ffNeuralNetwork.getActivation()[layer].length - 1) / (ffNeuralNetwork.getActivation()[layer].length)) * circleRadiiMaxHeight);
		double yOffset = (circleRadiiMaxHeight - lastCircleRadiiY) / 2d;

		neuron.setCenterX(cx + xOffset);
		neuron.setCenterY(cy + yOffset);
		neuron.setFill(Paint.valueOf("blue"));

		return neuron;
	}
}
