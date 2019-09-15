package nnvisualizer.renderer;

import javafx.scene.layout.Pane;
import nnvisualizer.nn.NeuralNetwork;

public interface NNRenderer {
	void renderNetwork(final Pane pane, final NeuralNetwork neuralNetwork);
}
