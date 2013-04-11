package edu.uc.netflix.plsa;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

import Jama.Matrix;

import edu.uc.netflix.sparsematrix.JavaSparseArray;

/**
 * @author lee
 * learns plsa model with T topics from words x docs counts data
 */
/*
 * All private methods are so because they for speed reasons do not employ any
 * form of checking for numerical stability. In the case of PLSA matrices this is
 * acceptable as probability matrices are never negative and the dimensions of 
 * the matrices do not change.
 * for a unit test of plsa, plsa will be used to produce the NMF of a matrix, functionality
 * and correctness can be confirmed by finding the product to be equal to the input 
 * matrix
 */

public class PLSA2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double[][] f = {{1d, 0d, 0d, 2d, 0d},{1d, 1d, 1d, 0d, 0d}, {0d, 0d, 1d, 1d , 1d},{2d, 1d, 1d, 0d, 1d},{1d, 0d, 0d, 2d, 0d},{1d, 1d, 1d, 0d, 0d}, {0d, 0d, 1d, 1d , 1d},{2d, 1d, 1d, 0d, 1d}};
		
		Matrix F = new Matrix(f);
		for(double[] ff: f){
			System.out.println();
			for(double fff: ff){
				System.out.print(f + " ");
			}
		}System.out.println();
		
		
		PLSA2 plsa = new PLSA2(F,4,.00001d);
		

		plsa.plsa(.00001f);
		//printmat(F.F.norm1());
		printmat(plsa.wt);printmat(plsa.td);
		printmat( multiply(plsa.wt,plsa.td));

	}
	
	
	int W;//words, rows
	int D;//documents, columns
	int T;// topics or latent classes

	public double[][] td;
	public double[][] wt;
	double[][] counts;
	
	public PLSA2(Matrix m, int T,double epsilon)
	{
		this.counts = counts;
		W=counts.length;
		D = counts[0].length;
		this.T = T;
		//plsa(epsilon);
	}
	
	// use if you want wt initialized to some specific value
	public void plsa( double epsilon)
	{
		double tot = sum(counts);
		td = normalize(ones(T,D));
		wt = normalize(rand(W,T));
		
		
		double[] E = sum1D(logDotProduct(counts,multiply(wt,td)));
		double F = sum(E)/tot;
		double F_new ;
		double rel_ch;
		

		do
		{
			// Expectation Step
			// td = norm(td .* ( wt' * ( counts ./ (wt * td) ) ));
			td = normalize(dotProduct(td,(multiply(transpose(wt),dotDivide(counts,multiply(wt,td))))));
			
			//maximization step
			//wt = normalize( wt .* ( ( counts ./ ( wt * td + eps ) ) * td' ))
			wt = normalize(dotProduct(wt,multiply(dotDivide(counts,multiply(wt,td)),transpose(td))));
			
			//calculate log-likelihood
		/* 
		 *   ___         ___
		 *   \		     \
		 *   /__	     /__     n(d,w) log P(d,w)
		 *  d c D   w c W
		*/
			E = sum1D(logDotProduct(counts,multiply(wt,td)));
			F_new = sum(E)/tot;
			
			//calculate iteration's relative change to determine convergence
			rel_ch = Math.abs((F_new - F))/ Math.abs(F);
			F= F_new;
			
			System.out.println(rel_ch);
			
		}while(rel_ch>epsilon);
		
	}
	
	
/*	// use if you want wt initialized to some specific value
	public void plsaSparse( double epsilon,JavaSparseArray counts)
	{
		double tot = counts.sum();
		JavaSparseArray td = new JavaSparseArray(normalize(ones(T,D)));
		JavaSparseArray wt = new JavaSparseArray(normalize(rand(W,T)));
		
		
		double[] E = counts.logDotMultiply(wt.times(td)).sum1D();
		double F = sum(E)/tot;
		double F_new ;
		double rel_ch;
		

		do
		{
			// Expectation Step
			// td = norm(td .* ( wt' * ( counts ./ (wt * td) ) ));
			td = td.dotMultiply(wt.transpose().times(counts.dotDivide(wt.times(td)))).normalize();

			
			//maximization step
			//wt = normalize( wt .* ( ( counts ./ ( wt * td + eps ) ) * td' ))
			wt = wt.dotMultiply(counts.dotDivide(wt.times(td)).times(td.transpose())).normalize();
			
			//calculate log-likelihood
		 
		 *   ___         ___
		 *   \		     \
		 *   /__	     /__     n(d,w) log P(d,w)
		 *  d c D   w c W
		
			
			E = counts.logDotMultiply(wt.times(td)).sum1D();
			F_new = sum(E)/tot;
			
			//calculate iteration's relative change to determine convergence
			rel_ch = Math.abs((F_new - F))/ Math.abs(F);
			F= F_new;
			
			System.out.println(rel_ch);
			
		}while(rel_ch>epsilon);
		
		this.td = td.getDense();
		this.wt = wt.getDense();
		
	}*/
	
	
	

	//testing status - works
	//gets the pairwise products of two matrices
	//no dimension checking
	private static double[][] dotProduct(double[][] mat1, double[][] mat2)
	{
		double[][] rtrn = new double [mat1.length ][mat1[0].length];
		
		for(int i = 0;i<mat1.length;i++){
			for(int j = 0;j<mat1[0].length;j++)rtrn[i][j] = mat1[i][j]*mat2[i][j];
		}
		return rtrn;
	}
	
	//testing status - works
	//gets the pairwise division of two matrices
	//no dimension checking
	private static double[][] dotDivide(double[][] mat1, double[][] mat2)
	{
		double[][] rtrn = new double [mat1.length ][mat1[0].length];
		
		for(int i = 0;i<mat1.length;i++){
			for(int j = 0;j<mat1[0].length;j++)
				rtrn[i][j] = mat1[i][j]/(mat2[i][j]+Double.MIN_VALUE);
		}
		return rtrn;
	}
	
	
	//testing status - works
	//find the pairwise product of mat1 and log(mat2)
	//no dimension checking
	private static double[][] logDotProduct(double[][] mat1, double[][] mat2)
	{
		double[][] rtrn = new double [mat1.length ][mat1[0].length];
		
		for(int i = 0;i<mat1.length;i++){
			for(int j = 0;j<mat1[0].length;j++)
				rtrn[i][j] = mat1[i][j]*(double)Math.log(mat2[i][j] + Double.MIN_VALUE);
		}
		return rtrn;
	}
	
	//testing status - works
	//create a random matrix to with which we will initialize mle
	//ding, he, zha, simon suggest using in svd reduced matrices to initialize this matrix
	//farahat and chen suggest initializing with svd center's
	//hoffman suggests multiple attempts at plsa with differenent random initializations
	public static double[][] rand(int w,int t)
	{
		Random r= new Random(System.currentTimeMillis());// 8589934591L a mersenne prime
		double[][] rand = new double[w][t];
		for(int i = 0;i<w;i++){
			for(int j = 0;j<t;j++)rand[i][j]=r.nextDouble();
		}
		return rand;
	}
	
	//testing status - works
	//create a matrix of all ones of the specified dimension
	//future improvements will provide a normalized matrix of ones function
	public static double[][] ones(int w,int t)
	{
		double[][] ones = new double[w][t];
		for(int i = 0;i<w;i++)
		{
			for(int j = 0;j<t;j++)ones[i][j]=1;
		}
		return ones;
	}
	
	
	//testing status - works
	//give the sum of all the elements of a matrix
	public static double sum(double[][] A)
	{
		double sum =0;
		for(double[] ff:A){
			for(double f:ff) sum+=f;
		}
		return sum;
	}
	
	//testing status - works
	//give the sum of all the elements of a vector
	public static double sum(double[]A)
	{
		double sum =0;
			for(double f:A) sum+=f;
		return sum;
	}
	
	//testing status - works
	//give the column vector sum of all the elements of a matrix
	public static double[] sum1D(double[][] A)
	{
		double[] sum = new double[A[0].length];
		Arrays.fill(sum, 0f);
		for(double[] ff:A)
		{
			for(int i = 0;i<ff.length;i++)
				sum[i] += ff[i];
		}
		return sum;
	}
	
	
////	testing status - works
//	//beware of caching problems, this is a classic bad array access loop
//	// lee carraher will fix it when it becomes a problem
//	public static double[][] normalize(double[][] A)
//	{
//		for(int i = 0; i<A[0].length;i++)
//		{
//			double sum = 0;
//			for(int j = 0; j<A.length;j++)
//			{
//				sum+=A[j][i];
//			}
//			for(int j = 0; j<A.length;j++)
//			{
//				if(sum==0)
//					A[j][i]=0;
//				else
//					A[j][i] /=sum;
//			}
//		}
//		return A;
//	}
	
	public static double[][] normalize(double[][] A)
	{
		for(int i = 0; i<A[0].length;i++)
		{
			double sum = 0;
			for(int j = 0; j<A.length;j++)
			{
				sum+=A[j][i];
			}
			for(int j = 0; j<A.length;j++)
			{
				if(sum==0)
					A[j][i]=0;
				else
					A[j][i] /=sum;
			}
		}
		return A;
	}
	
	//testing status - works
	//naive multiply for testing, to be replaced with straussen or winograd's
	// this will be a dense multiply (rand(T,K) * ones(K,D))
	//no dimension checking
	private static double[][] multiply(double[][] in1, double[][] in2){
		double[][] rtrn = new double[in1.length][in2[0].length];		
		for(int i = 0; i<in1.length;i++)
		{
			for(int j = 0; j<in2[0].length;j++)
			{
				rtrn[i][j] = 0f;
				for(int k=0; k<in2.length;k++)
				{
					rtrn[i][j]+=in1[i][k]*in2[k][j];	
				}	
			}
		}
		return rtrn;
	}

	//testing status - works
	//simple method to transpose an array
	public static double[][] transpose(double[][] in)
	{
		double[][] rtrn = new double[in[0].length][in.length];
		
		for(int i = 0; i<in.length;i++)
		{
			for(int j=0; j<rtrn.length;j++)
			{
				rtrn[j][i]=in[i][j];
			}
		}
		
		return rtrn;
	}
	
	public static void printmat(double[][] D){
		for(double[] dd: D){
			System.out.println();
			for(double d: dd){
				System.out.print(d + " ");
			}
		}
		System.out.println();
	}
	
}
