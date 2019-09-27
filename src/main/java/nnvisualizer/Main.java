package nnvisualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import nnvisualizer.nn.FFNeuralNetwork;
import nnvisualizer.nn.NeuralNetwork;
import nnvisualizer.renderer.FFNNRenderer;
import nnvisualizer.renderer.NNRenderer;

import java.io.File;
import java.util.Objects;

public class Main extends Application {

	public final static double SCREEN_WIDTH = Screen.getPrimary().getBounds().getWidth();
	public final static double SCREEN_HEIGHT = Screen.getPrimary().getBounds().getHeight();

	private static NNRenderer nnRenderer;

	private static NeuralNetwork[] neuralNetworks;

	private int renderedNetwork = 0;

	private int generationStepSize = 1;

	private Pane globalPane, neuralNetworkPane;

	private Scene scene;

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
		basicJFXSetup(stage);
		setupMenuBar(stage);

		nnRenderer.renderNetwork(neuralNetworkPane, neuralNetworks[0]);

		setupKeyEvents();
		stage.show();
	}

	private void setupMenuBar(final Stage stage) {
		final Menu fileMenu = new Menu("File");
		final MenuItem openFile = new MenuItem("Open file");
		openFile.setOnAction(actionEvent -> {
			final FileChooser fileChooser = new FileChooser();
			final File fileToOpen = fileChooser.showOpenDialog(stage);
			if (fileToOpen != null) {
				neuralNetworks = FFNeuralNetwork.buildNetworks(fileToOpen.getAbsolutePath());
				nnRenderer.renderNetwork(neuralNetworkPane, Objects.requireNonNull(neuralNetworks)[0]);
				renderedNetwork = 0;
				generationCounter.setText("Generation: 0");
			}
		});
		fileMenu.getItems().addAll(openFile);

		globalPane.getChildren().add(new MenuBar(fileMenu));
	}

	private void basicJFXSetup(final Stage stage) {
		globalPane = new Pane(generationCounter);
		generationCounter.setFont(new Font(SCREEN_HEIGHT / 50d));

		neuralNetworkPane = new Pane();
		globalPane.getChildren().add(neuralNetworkPane);
		scene = new Scene(globalPane, SCREEN_WIDTH, SCREEN_HEIGHT, Color.WHITE);
		stage.setScene(scene);
		stage.setTitle("NNVisualizer");
	}

	private void renderNetwork() {
		generationCounter.setText("Generation: " + renderedNetwork);
		nnRenderer.renderNetwork(neuralNetworkPane, neuralNetworks[renderedNetwork]);
	}

	private void setupKeyEvents() {
		scene.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.UP && renderedNetwork - generationStepSize >= 0) {
				renderedNetwork -= generationStepSize;
			} else if (keyEvent.getCode() == KeyCode.DOWN && renderedNetwork + generationStepSize < neuralNetworks.length) {
				renderedNetwork += generationStepSize;
			}
			renderNetwork();
		});

		scene.setOnKeyReleased(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.S) {
				do {
					generationStepSize = getInputFromDialog("Step size", "Set the generation step size when using up and down arrows: ");
				} while (generationStepSize < 0 || generationStepSize >= neuralNetworks.length);
			}

			if (keyEvent.getCode() == KeyCode.G) {
				do {
					renderedNetwork = getInputFromDialog("Go to generation...", "Select the generation you wish to go to: ");
				} while (renderedNetwork < 0 || renderedNetwork >= neuralNetworks.length);
				renderNetwork();
			}
		});
	}

	private int getInputFromDialog(final String title, final String headerText) {
		final TextInputDialog stepSizeDialog = new TextInputDialog();
		stepSizeDialog.setTitle(title);
		stepSizeDialog.setHeaderText(headerText);
		try {
			return Integer.parseInt(stepSizeDialog.showAndWait().orElse("1"));
		} catch (NumberFormatException ignored) {
			return -1;
		}
	}
}
