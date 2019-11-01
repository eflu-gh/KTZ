/**  
* ProbabilityModel.java - class to set values and call MyMatrix objects.  
* @author  Edgar Lizarraga
* @version 1.0 04/22/2019
*/ 
package cuny.queens.college.probability.selection;
public class ProbabilityModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyMatrix objMatrix = new MyMatrix();

		// Number of variables P as P0, P1, etc, number of slack variables S, number of
		// equations Z',Z0,Z1, etc.
		objMatrix.codeLabelsMatrix(4, 1, 4);
		// Initialize the matrix with values of the equations Z',Z0,Z1,Z2, etc.
		double[][] matrix = { { -2.17, 3, 2, 2, 1, 1 }, 
							  { 0.65, -1, -1, 0, 0, -1 }, 
							  { 0.52, -1, 0, -1, 0, 0 },
				              { 1, -1, -1, -1, -1, 0 } };
		
		// Initialize the matrix with values of the equations Z',Z0,Z1,Z2, etc.
		/*
		 * objMatrix.codeLabelsMatrix(4,1,3); double[][] matrix = { {-1.65, 1,2,1,2,1 },
		 * { 0.65,0,-1,0,-1,-1 }, { 1,-1,-1,-1,-1,0 } };
		 */

		objMatrix.createMatrix();
		objMatrix.initMatrix(matrix);
		objMatrix.calcProbModel();
	}
}