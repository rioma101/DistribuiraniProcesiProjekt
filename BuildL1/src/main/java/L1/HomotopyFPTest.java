package L1;/*
% Java code for Homotopy Fixed-Point l1-minimization

% Copyright ©2010. The Regents of the University of California (Regents).
% All Rights Reserved. Contact The Office of Technology Licensing,
% UC Berkeley, 2150 Shattuck Avenue, Suite 510, Berkeley, CA 94720-1620,
% (510) 643-7201, for commercial licensing opportunities.

% Written by by Victor Shia, Allen Y. Yang, Department of EECS, University of California,
% Berkeley.

% IN NO EVENT SHALL REGENTS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
% SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
% ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
% REGENTS HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

% REGENTS SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED
% TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
% PARTICULAR PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
% PROVIDED HEREUNDER IS PROVIDED "AS IS". REGENTS HAS NO OBLIGATION TO
% PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

import java.util.Random;
import java.util.Calendar;

import L1.FixedPointMath.FPMath;
import L1.JamaFixedPoint.Array;
import L1.JamaFixedPoint.Matrix;


public class HomotopyFPTest {
	
	// creates a matrix and a vector and solves the l1 min problem
	public static void test1(){
		double[][] matrix = new double[][]

		Matrix M1 = new Matrix(m1);

		double[] a1 = {-0.4004669189453125,0.2458343505859375,0.409881591796875,-0.4720611572265625,-0.0824737548828125,-0.3102264404296875,0.1744842529296875,0.107452392578125,0.492645263671875,-0.420806884765625,0.615997314453125,1.430267333984375,-0.348419189453125,-0.0941619873046875,-1.019439697265625,-0.4713592529296875,-0.2584686279296875,-0.2645416259765625,-0.05059814453125,0.855133056640625};
		Array A2 = new Array(a1);

		Array Q1 = HomotopyFP.SolveHomotopy(M1, A2);

		//System.out.println("Estimated X");
		System.out.print("x1 = [");
		Q1.printArray();
		System.out.println("];");
		System.out.println("plot( x1, 'b')");
	}

	// generates a random matrix M1 and sparse vector A1
	// computes A2 = M1*A1;
	// solves L1 using A2 and M1.  
	// computes the l1 norm diff between output of Homotopy and A1.
	public static void test2(){
		Random rand = new Random();
		int m = 50;
		int n = 200;
		int t;
		int[][] m1 = new int[m][n];
		int[] a1 = new int[n];  // x0 ground truth

		for(int i= 0; i < m; i++){
			for(int j = 0; j < n; j++){
				m1[i][j] = FPMath.FloatToFP(rand.nextDouble()*2-1);
			}
		}
		int temp = 0;
		for(int i = 0 ; i < 5; i++){
			temp = (temp + rand.nextInt(a1.length))%100;
			a1[temp] = FPMath.FPMul(FPMath.IntToFP(rand.nextInt(10)), FPMath.IntToFP(1));
		}

		Matrix M1 = new Matrix(m1);
		Array A1 = new Array(a1);  // ground truth
		
		A1.normalize();
		
		Array A2 = M1.times(A1);
		int[] a2 = A2.getArray(); // projected vector


		//System.out.println("ANSWER");
		Array Q1 = HomotopyFP.SolveHomotopy(M1, A2);

		float norm1diff = 0;
		for(int i = 0 ; i < Q1.getLength(); i++){
			norm1diff += Math.abs(FPMath.FPToFloat(Q1.getElement(i))-FPMath.FPToFloat(A1.getElement(i)));
		}
		

		if(norm1diff > 1.0){

			System.out.println("norm1diff " + norm1diff);
			
			System.out.println("MATLAB");
			printToMatlab(m1);
			printToMatlab(a2);
			System.out.println("JAVA");
			printToJava(m1);
			printToJava(a2);

			//System.out.println("Ground Truth");
			System.out.print("x0 = [");
			A1.printArray();
			System.out.println("];");
			//System.out.println("Estimated X");
			System.out.print("x1 = [");
			Q1.printArray();
			System.out.println("];");
			System.out.println("hold on; plot(x0, 'r'); plot( x1, 'b')");

		}
	}

	// runs test2 10,100,1000 times
	public static void main(String[] args){
		//test2();
		Calendar cal = Calendar.getInstance();
		long start = cal.getTimeInMillis();
		for(int i = 0 ; i < 10; i++){
			test2();
		}
		cal = Calendar.getInstance();
		long elapsed = cal.getTimeInMillis() - start;
		System.out.println("Run Homotopy 10 times takes " + (elapsed) + " milliseconds");

		start = cal.getTimeInMillis();
		for(int i = 0 ; i < 100; i++){
			test2();
		}
		cal = Calendar.getInstance();
		elapsed = cal.getTimeInMillis() - start;
		System.out.println("Run Homotopy 100 times takes " + (elapsed) + " milliseconds");

		start = cal.getTimeInMillis();
		for(int i = 0 ; i < 1000; i++){
			test2();
		}
		cal = Calendar.getInstance();
		elapsed = cal.getTimeInMillis() - start;
		System.out.println("Run Homotopy 1000 times takes " + (elapsed) + " milliseconds");
	}

	public static void printToJava(int[][] m1){
		System.out.print("int[][] m1 = {");
		for(int i = 0; i < m1.length; i++){
			if (i != 0 )
				System.out.print(",");
			System.out.print("{");

			for(int j = 0; j < m1[0].length; j++){
				if (j!=0)
					System.out.print(",");
				System.out.print(FPMath.FPToFloat(m1[i][j]));
			}

			System.out.print("}");
		}
		System.out.println("};");
	}


	public static void printToJava(int[] a1){
		System.out.print("int[] a1 = {");
		for(int i = 0; i < a1.length; i++){
			if (i != 0 )
				System.out.print(",");
			System.out.print(FPMath.FPToFloat(a1[i]));
		}
		System.out.println("};");
	}

	public static void printToMatlab(int[][] m1){
		System.out.print("A = [");
		for(int i = 0; i < m1.length; i++){
			if (i != 0 )
				System.out.print(";");
			for(int j = 0; j < m1[0].length; j++){
				if (j!=0)
					System.out.print(",");
				System.out.print(FPMath.FPToFloat(m1[i][j]));
			}
		}
		System.out.println("]");
	}

	public static void printToMatlab(int[] a1){
		System.out.print("y = [");
		for(int i = 0; i < a1.length; i++){
			if (i != 0 )
				System.out.print(";");
			System.out.print(FPMath.FPToFloat(a1[i]));
		}
		System.out.println("]");
	}
}
