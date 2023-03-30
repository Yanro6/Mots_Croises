package controller;

import java.sql.*;
import java.util.*;

import models.MotsCroisesTP6;

public class ChargerGrille {
	public Connection connexion;
	private Map<Integer, String> grillesDispo;

	public ChargerGrille() {
		try {
			connexion = connecterBD();
			grillesDispo = new HashMap<>();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection connecterBD() throws SQLException {
		Connection connect;
		connect = DriverManager.getConnection(
				"jdbc:mysql://mysql.istic.univ-rennes1.fr:3306/base_bousse?autoReconnect=true&useSSL=false",
				"user_ryang", "jaimelaprga");
		return connect;
	}

	// Retourne la liste des grilles disponibles dans la B.D.
	// Chaque grille est décrite par la concaténation des valeurs
	// respectives des colonnes nom_grille, hauteur et largeur.
	// L’élément de liste ainsi obtenu est indexé par le numéro de
	// la grille (colonne num_grille).
	// Ainsi "Français débutants (7x6)" devrait être associé à la clé 10
	public Map<Integer, String> grillesDisponibles() throws SQLException {
		String requeteGrilles = "SELECT num_grille, nom_grille, largeur, hauteur FROM TP5_GRILLE";
		PreparedStatement preparedStatement = connexion.prepareStatement(requeteGrilles);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			String nom_grille = resultSet.getString("nom_grille");
			int hauteur = resultSet.getInt("hauteur");
			int largeur = resultSet.getInt("largeur");
			int num_grille = resultSet.getInt("num_grille");
			String descriptionGrille = nom_grille + " (" + hauteur + "x" + largeur + ")";
			grillesDispo.put(num_grille, descriptionGrille);
		}

		return grillesDispo;
	}

	public MotsCroisesTP6 extraireGrille(int numGrille) throws SQLException {
		String requete = "SELECT hauteur, largeur FROM TP5_GRILLE WHERE num_grille = ?";
		PreparedStatement preparedStatement = connexion.prepareStatement(requete);
		preparedStatement.setInt(1, numGrille);
		ResultSet resultSet = preparedStatement.executeQuery();
		int hauteur = 0;
		int largeur = 0;
		while (resultSet.next()) {
			hauteur = resultSet.getInt("hauteur");
			largeur = resultSet.getInt("largeur");
		}

		MotsCroisesTP6 motCroises = new MotsCroisesTP6(hauteur, largeur);
		for (int lig = 1; lig <= motCroises.getHauteur(); lig++) {
			for (int col = 1; col <= motCroises.getLargeur(); col++) {
				motCroises.setCaseNoire(lig, col, false);
			}
		}

		requete = "SELECT * FROM TP5_MOT WHERE num_grille = ?";
		preparedStatement = connexion.prepareStatement(requete);
		preparedStatement.setInt(1, numGrille);
		resultSet = preparedStatement.executeQuery();
		String definition, solution;
		int horizontal, ligne, colonne;
		while (resultSet.next()) {
			definition = resultSet.getString("definition");
			horizontal = resultSet.getInt("horizontal");
			ligne = resultSet.getInt("ligne");
			colonne = resultSet.getInt("colonne");
			solution = resultSet.getString("solution");

			motCroises.setDefinition(ligne, colonne, (horizontal == 1), definition);
			if (horizontal == 1) {
				int caractere = 0;
				for (int col = colonne; col <= solution.length(); col++) {
					motCroises.setSolution(ligne, col, solution.charAt(caractere));
					caractere++;
				}

			} else {
				int caractere = 0;
				int lig = 0;
				for (lig = ligne; lig < solution.length(); lig++) {
					motCroises.setSolution(lig, colonne, solution.charAt(caractere));
					caractere++;
				}
				if (lig < hauteur) {
					lig++;
					motCroises.setSolution(lig, colonne, ' ');
					motCroises.setCaseNoire(lig, colonne, true);
				}
			}
		}

		return motCroises;
	}
}