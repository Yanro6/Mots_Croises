package controller;

import java.sql.SQLException;
import java.util.Random;
import java.util.stream.IntStream;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import models.MotsCroisesTP6;

public class ControleurTP6 {

	private MotsCroisesTP6 modele;

	private ChargerGrille cg;

	private TextField modeleTF;

	@FXML // pour rendre la variable visible depuis SceneBuilder
	private GridPane grilleMC;

	@FXML
	private Button nouvelleGrille;

	@FXML
	private void initialize() throws SQLException {
		cg = new ChargerGrille();
		nouvelleGrille = new Button("nouvelle grille");
		// modele = MotsCroisesFactory.creerMotsCroises2x3();
		initTF();
		grilleMC.add(nouvelleGrille, grilleMC.getRowCount(), 0);
		for (Node n : grilleMC.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n;
				/*
				 * tf.setOnMouseClicked((e) -> { this.clicLettre(e); });
				 */
				tf.setOnKeyPressed((e) -> {
					this.keyLettre(e);
				});
			}
			if (n instanceof Button) {
				Button bt = (Button) n;
				bt.setOnAction(actionEvent -> {
					try {
						initialize();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});
			}
		}
	}

	@FXML
	public void keyLettre(KeyEvent e) {
		TextField nextTf = new TextField();
		TextField tf = (TextField) e.getSource();
		switch (e.getCode()) {
		case UP:
			break;
		case DOWN:
			break;
		case LEFT:
			break;
		case RIGHT:
			break;
		default:
			int lig = ((int) tf.getProperties().get("gridpane-row")) + 1;
			int col = ((int) tf.getProperties().get("gridpane-column")) + 1;
			ObservableList<Node> tfs = grilleMC.getChildren();
			int i = 0;

			nextTf = (TextField) tfs.get(tfs.size() - 2);
			while (!((((int) nextTf.getProperties().get("gridpane-row") + 1) == lig)
					&& (((int) nextTf.getProperties().get("gridpane-column") + 1) == col))) {
				nextTf = (TextField) tfs.get(i);
				i++;
			}
			nextTf = (TextField) tfs.get(i);
		}
		tf.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			tf.setText(e.getCode().toString());
		});
		tf.setStyle("-fx-focus-color: blue; -fx-faint-focus-color: blue; -fx-text-fill: black;");
		nextTf.requestFocus();
		;
	}

	/*
	 * @FXML public void clicLettre(MouseEvent e) { if (e.getButton() ==
	 * MouseButton.MIDDLE) { // C'est un clic "central" TextField tf = (TextField)
	 * e.getSource(); int lig = // n° ligne de la case (cf. boucle du 1.2) int col =
	 * // n° colonne de la case (cf. boucle du 1.2) // appel de montrerSolution()
	 * sur le modèle // modele.montrerSolution(lig, col); } }
	 */

	private void initTF() throws SQLException {
		modeleTF = (TextField) grilleMC.getChildren().get(0);
		grilleMC.getChildren().clear();
		Random random = new Random();
		IntStream numGrille = random.ints(1, 1, 11);
		int[] numG = numGrille.toArray();
		System.out.println("grille numero " + numG[0]);
		modele = cg.extraireGrille(numG[0]);
		for (int lig = 0; lig < modele.getHauteur(); lig++) {
			for (int col = 0; col < modele.getLargeur(); col++) {
				if (!(modele.estCaseNoire(lig + 1, col + 1))) {
					TextField tf = new TextField();
					tf = setTF();
					tf.textProperty().bindBidirectional(modele.propositionProperty(lig + 1, col + 1));
					String defH = modele.getDefinition(lig + 1, col + 1, true);
					String defV = modele.getDefinition(lig + 1, col + 1, false);
					String texte = "";
					if (defH == null) {
						if (defV == null) {
							texte = "il n'y a pas de definition";
						} else {
							texte = defV;
						}
					} else {
						if (defV == null) {
							texte = defH;
						} else {
							texte = defH + " / " + defV;
						}
					}
					tf.setTooltip(new Tooltip(texte));
					tf.clear();
					grilleMC.add(tf, col, lig);
				}
			}
		}
	}

	private TextField setTF() {
		TextField tf = new TextField();
		tf.setPrefWidth(modeleTF.getPrefWidth());
		tf.setPrefHeight(modeleTF.getPrefHeight());
		for (Object cle : modeleTF.getProperties().keySet()) {
			tf.getProperties().put(cle, modeleTF.getProperties().get(cle));
		}
		tf.setTextFormatter(new TextFormatter<String>((Change change) -> {
			String newText = change.getControlNewText();
			if (newText.length() > 1) {
				return null;
			} else {
				return change;
			}
		}));
		tf.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\sa-zA-Z*")) {
				tf.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
			}
		});
		tf.setStyle("-fx-focus-color: blue; -fx-faint-focus-color: blue; -fx-text-fill: white;");
		return tf;
	}
}
