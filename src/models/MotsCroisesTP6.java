package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MotsCroisesTP6 implements SpecifMotsCroises {
	public Grille<Character> solution;
	private Grille<StringProperty> proposition;
	private Grille<String> horizontal;
	private Grille<String> vertical;

	public MotsCroisesTP6(int hauteur, int largeur) {
		solution = new Grille<Character>(hauteur, largeur);
		proposition = new Grille<StringProperty>(hauteur, largeur);

		horizontal = new Grille<String>(hauteur, largeur);
		vertical = new Grille<String>(hauteur, largeur);
		for (int lig = 1; lig <= getHauteur(); lig++) {
			for (int col = 1; col <= getLargeur(); col++) {
				setProposition(lig, col, ' ');
				setCaseNoire(lig, col, true);
			}
		}
	}

	public int getHauteur() {
		return solution.getHauteur();
	}

	public int getLargeur() {
		return solution.getLargeur();
	}

	public boolean coordCorrectes(int lig, int col) {
		return 1 <= lig && lig <= getHauteur() && 1 <= col && col <= getLargeur();
	}

	public boolean estCaseNoire(int lig, int col) {
		assert coordCorrectes(lig, col);
		return (solution.getCellule(lig, col) == null);
	}

	public void setCaseNoire(int lig, int col, boolean noire) {
		assert coordCorrectes(lig, col);
		if (noire) {
			solution.setCellule(lig, col, null);
		} else if (solution.getCellule(lig, col) == null) {
			solution.setCellule(lig, col, new Character(' '));
		}
	}

	public void montrerSolution(int lig, int col) { // TODO

	}

	public char getSolution(int lig, int col) {
		assert coordCorrectes(lig, col);
		assert !estCaseNoire(lig, col);
		return solution.getCellule(lig, col);
	}

	public void setSolution(int lig, int col, char sol) {
		assert coordCorrectes(lig, col);
		assert !estCaseNoire(lig, col);
		setSol(lig, col, sol);
	}

	private void setSol(int lig, int col, char sol) {
		assert coordCorrectes(lig, col);
		assert !estCaseNoire(lig, col);
		solution.setCellule(lig, col, sol);
	}

	public StringProperty propositionProperty(int lig, int col) {
		assert coordCorrectes(lig, col);
		assert !estCaseNoire(lig, col);
		return proposition.getCellule(lig, col);
	}

	public char getProposition(int lig, int col) {
		assert coordCorrectes(lig, col);
		assert !estCaseNoire(lig, col);
		return proposition.getCellule(lig, col).toString().charAt(0);
	}

	public void setProposition(int lig, int col, char prop) {
		assert coordCorrectes(lig, col);
		assert !estCaseNoire(lig, col);
		StringProperty var = new SimpleStringProperty(Character.toString(prop));
		proposition.setCellule(lig, col, var);
	}

	public String getDefinition(int lig, int col, boolean horiz) {
		assert coordCorrectes(lig, col);
		assert !estCaseNoire(lig, col);
		if (horiz) {
			return horizontal.getCellule(lig, col);
		} else {
			return vertical.getCellule(lig, col);
		}
	}

	public void setDefinition(int lig, int col, boolean horiz, String def) {
		assert coordCorrectes(lig, col);
		assert !estCaseNoire(lig, col);
		if (horiz) {
			horizontal.setCellule(lig, col, def);
		} else {
			vertical.setCellule(lig, col, def);
		}
	}

	@Override
	public String toString() {
		return "Solution\n" + solution + "\nProposition\n" + proposition + "\nHorizontal\n" + horizontal
				+ "\nVertical\n" + vertical;
	}

}
