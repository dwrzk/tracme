/*
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


public class MainTestCalPoly {
	static int numAnchors;
	static String rawFileName;
	static double p;
	static String trainFile;
	static String testFile;
	
	
	private static void testCalPoly() {
		rawFileName = "DexterLawnNthUbFix2.txt";
		numAnchors  = 10;
		p = 0;
		trainFile = "train_p" + p + "_sub_0.8.txt";
		testFile = "test_p" + p + ".txt";
	}
	
	
	public static void main(String[] args) {
		testCalPoly();
		Testing test = new Testing(rawFileName, trainFile);
		
		double[]  newSample = { 20, 18, 19, 21, 21, 26, 25, 24, 30, 40};
		// your program will be like
		//for each reading newSample {
		//	double[] estLoc = new double[2];
		//	estLoc = test.getEstLocation(newSample);
		//}
		
		double[] estLoc = new double[2];
		test.setNumClasses( 100, 100 );
		estLoc = test.getEstLocation(newSample);
		System.out.println("(" + estLoc[0] + ", " + estLoc[1] + ")");
	}
}
*/
