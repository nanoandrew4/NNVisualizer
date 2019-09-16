package nnvisualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import nnvisualizer.nn.FFNeuralNetwork;
import nnvisualizer.nn.NeuralNetwork;
import nnvisualizer.renderer.FFNNRenderer;
import nnvisualizer.renderer.NNRenderer;

public class Main extends Application {

	public final static double SCREEN_WIDTH = Screen.getPrimary().getBounds().getWidth();
	public final static double SCREEN_HEIGHT = Screen.getPrimary().getBounds().getHeight();

	private static NNRenderer nnRenderer;

	static NeuralNetwork[] neuralNetworks;

	private int renderedNetwork = 0;

	private Pane globalPane, neuralNetworkPane;

	private Text generationCounter = new Text(SCREEN_WIDTH - SCREEN_WIDTH / 8d, SCREEN_HEIGHT / 50d, "Generation: 0");

	public static void main(final String[] args) {
		if (args.length == 2 && Integer.parseInt(args[0]) == 0) {
			neuralNetworks = FFNeuralNetwork.buildNetworks(args[1]);
			nnRenderer = new FFNNRenderer();
		}
		launch();
	}

	@Override
	public void start(final Stage stage) {
		globalPane = new Pane(generationCounter);
		generationCounter.setFont(new Font(SCREEN_HEIGHT / 50d));

		neuralNetworkPane = new Pane();
		globalPane.getChildren().add(neuralNetworkPane);

		final Scene scene = new Scene(globalPane, SCREEN_WIDTH, SCREEN_HEIGHT, Color.WHITE);
		stage.setScene(scene);
		stage.setTitle("NNVisualizer");

		nnRenderer.renderNetwork(neuralNetworkPane, neuralNetworks[0]);

		scene.setOnKeyReleased(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.LEFT && renderedNetwork > 0) {
				nnRenderer.renderNetwork(neuralNetworkPane, neuralNetworks[--renderedNetwork]);
			} else if (keyEvent.getCode() == KeyCode.RIGHT && renderedNetwork < neuralNetworks.length - 1) {
				nnRenderer.renderNetwork(neuralNetworkPane, neuralNetworks[++renderedNetwork]);
			}
			generationCounter.setText("Generation: " + renderedNetwork);
		});

		stage.show();
	}
}
