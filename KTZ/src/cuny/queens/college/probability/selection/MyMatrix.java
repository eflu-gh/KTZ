/**  
* MyMatrix.java - class to calculate the probability model selection in a matrix.
* @author  Edgar Lizarraga
* @version 1.0 04/22/2019
*/ 
package cuny.queens.college.probability.selection;
import java.text.DecimalFormat;
import java.util.HashMap;

public class MyMatrix {

	private double[][] matrix;
	private int numberVariablesP;
	private int numberVariablesS;
	private int numberEquations;
	private String nameVariable;
	HashMap<Integer, String> labelUp = new HashMap<>();
	HashMap<Integer, String> labelLeft = new HashMap<>();
	private DecimalFormat df = new DecimalFormat("0.00");

	public void codeLabelsMatrix(int varP, int varS, int numEq) {
		// How many variables without slack variable are there?
		numberVariablesP = varP;
		nameVariable = "P";
		labelUp.put(5, "Const");
		int k = 0;
		for (k = 0; k < numberVariablesP; k++)
			labelUp.put(10 + k, nameVariable + k);

		// How man slack variables are there?
		if (varS > 0) {
			numberVariablesS = varS;
			nameVariable = "S";
			for (int j = 0; j < numberVariablesS; j++) {
				labelUp.put(10 + k, nameVariable + j);
				k++;
			}
		}
		// how many equations?
		numberEquations = numEq;
		nameVariable = "Z";
		labelLeft.put(19, "Z'");
		for (int j = 0; j < numberEquations; j++)
			labelLeft.put(20 + j, nameVariable + j);
	}

	// Build the matrix.
	public void createMatrix() {
		matrix = new double[1 + numberEquations][2 + numberVariablesP + numberVariablesS];
		// Initialize the matrix
		matrix[1][0] = 19;// Z'
		matrix[0][1] = 5;// Const
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (i == 0 && j >= 2)
					matrix[i][j] = 8 + j;
				if ((i != 0 && i != 1) && j == 0) {
					matrix[i][j] = matrix[i - 1][j] + 1;
				}
			}
		}
	}

	public void initMatrix(double[][] mat) {
		//Copying only the values of mat regardless of the labels values such as: P0, P1, Z0, Z1, etc.
		for (int i = 1; i < matrix.length; i++) {
			for (int j = 1; j < matrix[0].length; j++) {
				matrix[i][j] = mat[i - 1][j - 1];
			}
		}
	}

	public void calcProbModel() {
		double minimun = 0, temporal = 0;
		int rowPivot = 0, colPivot = 0;
		printMatrix();
		//Verify if there exist a positive value in the first row (and column 2 to final) to continue the process.
		boolean somePositive = false;
		for (int j = 2; j < matrix[0].length; j++) {
			if (matrix[1][j] > 0) { //If the first row and its columns have some positive value.
				somePositive = true;
				break;
			}
		}
		
		while (somePositive) {//While there is a positive value in Z' row but not in constant value.
			for (int i = 2; i < matrix.length; i++) {
				for (int j = 2; j < matrix[0].length; j++) {
					/*
					 * Examine Z' or row1 to determine the entry/entries below a positive Z’-row
					 * that is/are negative.
					 */
					//It is used to get the pivot.
					if (matrix[i][j] < 0 && matrix[1][j] > 0) {
						temporal = (matrix[1][j] / Math.abs(matrix[i][j])) * matrix[i][1];
						if (temporal < minimun || minimun == 0) {
							// A value that is small in magnitude is most restrictive.
							minimun = temporal;
							rowPivot = i;
							colPivot = j;
						}
					}

				}
			}

			//Calculate values in the first row Z'
			for (int j = 1; j < matrix[0].length; j++) {
				// If the variable exist in the column pivot, and skip the row of Pivot
				if (matrix[1][colPivot] != 0 && colPivot != j) {
					matrix[1][j] = matrix[1][j] + matrix[rowPivot][j];
				}
				if (j == colPivot) //Set the value of the pivot.
					matrix[1][colPivot] = matrix[rowPivot][colPivot];
			}

			// update variables (Change the position of variables P0. P1, etc)
			double tempVar;
			tempVar = matrix[0][colPivot];
			matrix[0][colPivot] = matrix[rowPivot][0];
			matrix[rowPivot][0] = tempVar;

			//Calculate values in the different rows of Z'
			for (int i = 2; i < matrix.length; i++) {
				for (int j = 1; j < matrix[0].length; j++) {
					// If the variable exist in the column pivot, and skip the row of Pivot
					if (i != rowPivot && (int) matrix[i][colPivot] != 0) {
						if (colPivot != j) {
							if (labelUp.containsKey((int) matrix[rowPivot][0]))
								matrix[i][j] = matrix[i][j] + (matrix[rowPivot][j] * -1);
							else
								matrix[i][j] = matrix[i][j] + (matrix[rowPivot][j]);
						} else
							//Interchange the value of the pivot.
							matrix[i][colPivot] = matrix[rowPivot][colPivot];
					}
				}
			}

			System.out.println("");
			System.out.println("Minimun: " + df.format(minimun));
			System.out.println("Row pivot: " + rowPivot);
			System.out.println("Col pivot: " + colPivot);
			minimun = 0;
			temporal = 0;
			rowPivot = 0;
			colPivot = 0;
			somePositive = false;

			System.out.println("");
			printMatrix();

			//Verify if there exist a positive value in the first row to continue the process.
			for (int j = 2; j < matrix[0].length; j++) {
				if (matrix[1][j] > 0) {
					somePositive = true;
					break;
				}
			}
		}
	}

	public void printMatrix() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (i == 0 || j == 0) {
					if (labelUp.containsKey((int) matrix[i][j]))
						System.out.print(labelUp.get((int) matrix[i][j]) + "\t");
					else if (labelLeft.containsKey((int) matrix[i][j]))
						System.out.print(labelLeft.get((int) matrix[i][j]) + "\t");
					else
						System.out.print("" + "\t");
				} else
					System.out.print(df.format(matrix[i][j]) + "\t");
			}
			System.out.println();
		}
	}
}