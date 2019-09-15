package nnvisualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import nnvisualizer.nn.FFNeuralNetwork;
import nnvisualizer.nn.NeuralNetwork;
import nnvisualizer.renderer.FFNNRenderer;
import nnvisualizer.renderer.NNRenderer;

public class Main extends Application {

	private static NNRenderer nnRenderer;

	static NeuralNetwork[] neuralNetworks;

	private Pane pane;

	public static void main(final String[] args) {
		if (args.length == 2 && Integer.parseInt(args[0]) == 0) {
			neuralNetworks = FFNeuralNetwork.buildNetworks(args[1]);
			nnRenderer = new FFNNRenderer();
		}
		launch();
	}

	@Override
	public void start(final Stage stage) throws Exception {
		final Scene scene = new Scene(pane = new Pane(), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight(), Color.WHITE);
		stage.setScene(scene);
		stage.setTitle("NNVisualizer");

		nnRenderer.renderNetwork(pane, neuralNetworks[0]);

		stage.show();
	}
}
