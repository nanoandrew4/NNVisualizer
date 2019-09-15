package nnvisualizer.renderer;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import nnvisualizer.nn.FFNeuralNetwork;
import nnvisualizer.nn.NeuralNetwork;

public class FFNNRenderer implements NNRenderer {

	private final static double SCREEN_WIDTH = Screen.getPrimary().getBounds().getWidth();
	private final static double SCREEN_HEIGHT = Screen.getPrimary().getBounds().getHeight();

	@Override
	public void renderNetwork(final Pane pane, final NeuralNetwork neuralNetwork) {
		pane.getChildren().clear();
		final FFNeuralNetwork ffNeuralNetwork = (FFNeuralNetwork) neuralNetwork;

		for (int l = 0; l < ffNeuralNetwork.getActivation().length; l++) {
			for (int n = 0; n < ffNeuralNetwork.getActivation()[l].length; n++) {
				final Circle neuron = generateCircle(ffNeuralNetwork, l, n);
				pane.getChildren().add(neuron);
				// Draw weights as lines
			}
		}
	}

	private Circle generateCircle(final FFNeuralNetwork ffNeuralNetwork, int layer, int neuronInLayer) {
		final Circle neuron = new Circle(SCREEN_HEIGHT / (ffNeuralNetwork.getActivation()[0].length * 4d));
		final double circleRadiiMaxWidth = (SCREEN_WIDTH - neuron.getRadius() * 2);
		final double circleRadiiMaxHeight = (SCREEN_HEIGHT - neuron.getRadius() * 2);

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
