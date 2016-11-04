package neuralnetwork;

public class Perceptron {
	
	public static final int[][][] andData = {{{0,0},{0}},// AND gate - Works
											 {{0,1},{0}},
											 {{1,0},{0}},
											 {{1,1},{1}}};
	public static final int[][][] orData =  {{{0,0},{0}},// OR gate - Works
											 {{0,1},{1}},
											 {{1,0},{1}},
											 {{1,1},{1}}};
	public static final int[][][] xorData = {{{0,0},{0}},// XOR gate
											 {{0,1},{1}},
											 {{1,0},{1}},
											 {{1,1},{0}}};
	public static final int[][][] nandData ={{{0,0},{1}},// NAND gate
											 {{0,1},{1}},
											 {{1,0},{1}},
											 {{1,1},{0}}};
	public static final int[][][] norData = {{{0,0},{1}},// NOR gate
											 {{0,1},{0}},
											 {{1,0},{0}},
											 {{1,1},{0}}};
	public static final int[][][] xnorData ={{{0,0},{1}},// XNOR gate
											 {{0,1},{0}},
											 {{1,0},{0}},
											 {{1,1},{1}}};
	
	public static final int[][][] testData = {{{0,1}},// new inputs
											 {{1,1}},
											 {{0,0}},
											 {{0,1}},
											 {{1,0}},
											 {{1,1}}};
	public static final double LEARNING_RATE = 0.005;
	public static final double[] INITIAL_WEIGHTS = {Math.random(),Math.random()};
	public double calculateWeightedSum(int[] data,double[] weights){
		double weightedSum = 0;
		for(int x=0; x< data.length; x++) weightedSum += data[x] * weights[x];//data[x] * 
		return weightedSum;
	}
	public int applyActivationFunction(double weightedSum){
		int result = 0;
		if(weightedSum > 1) result = 1;
		return result;
	}
	public double[] adjustWeights(int[] data, double[] weights, double error){
		double[] adjustedWeights = new double[weights.length];
		for(int x=0; x < weights.length; x++) adjustedWeights[x] = LEARNING_RATE * error + weights[x];// * data[x]
		return adjustedWeights;
	}
}
