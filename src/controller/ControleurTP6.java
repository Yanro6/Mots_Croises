package controller;

import java.sql.SQLException;
import java.util.Random;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import models.MotsCroisesTP6;

public class ControleurTP6 {

	private MotsCroisesTP6 modele;

	private ChargerGrille cg;

	private TextField modeleTF;

	@FXML // pour rendre la variable visible depuis SceneBuilder
	private GridPane grilleMC;

	@FXML
	private void initialize() throws SQLException {
		cg = new ChargerGrille();

		// modele = MotsCroisesFactory.creerMotsCroises2x3();
		initTF();

		for (Node n : grilleMC.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n;
				int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
				int col = ((int) n.getProperties().get("gridpane-column")) + 1;
				// Initialisation du TextField tf ayant pour coordonnées (lig, col)
				// (cf. sections 1.3, 1.4 et 1.5)
				tf.textProperty().bindBidirectional(modele.propositionProperty(lig, col));
				String defH = modele.getDefinition(lig, col, true);
				String defV = modele.getDefinition(lig, col, false);
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
				tf.setOnMouseClicked((e) -> {
					this.clicLettre(e);
				});
			}
		}

	}

	@FXML
	public void clicLettre(MouseEvent e) {
		if (e.getButton() == MouseButton.MIDDLE) {
			// C'est un clic "central"
			TextField tf = (TextField) e.getSource();
			int lig = ((Integer) tf.getProperties().get("gridpane-row")) + 1;// n° ligne de la case (cf. boucle du 1.2)
			int col = ((Integer) tf.getProperties().get("gridpane-column")) + 1;// n° colonne de la case (cf. boucle du
																				// 1.2)
			// appel de montrerSolution() sur le modèle
			modele.montrerSolution(lig, col);
		}
	}

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
					tf.setPrefWidth(modeleTF.getPrefWidth());
					tf.setPrefHeight(modeleTF.getPrefHeight());
					for (Object cle : modeleTF.getProperties().keySet()) {
						tf.getProperties().put(cle, modeleTF.getProperties().get(cle));
					}
					grilleMC.add(tf, col, lig);
				}
			}
		}
	}
}
