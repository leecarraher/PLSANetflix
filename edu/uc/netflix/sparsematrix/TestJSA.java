package edu.uc.netflix.sparsematrix;

import java.util.Arrays;

import edu.uc.netflix.plsa.PLSA2;

public class TestJSA{
	private float[][] values = {
					  {1.0f},
					  {3.0f,3.0f},
					  {7.0f,8.0f,7.0f},
					  {3.0f,8.0f,7.0f,5.0f},
					  {8.0f,9.0f,9.0f,13.0f},
					  {3.0f,9.0f}};

	private int[][] indexes = {
					   {4},
					   {0,4},
					   {1,2,3},
					   {0,2,3,4},
					   {0,2,3,4},
					   {1,2}};

	private float[][] densematrix = {{0f,0f,2,1.0f,0f,1.0f},{1.0f,1.0f,2,1.0f,1.0f,1.0f},{0f,1.0f,0,0f,0f,1.0f},
			{0f,0f,2,1.0f,0f,1.0f},{1.0f,0f,2,1.0f,1.0f,1.0f}};
	
	public static void main(String[] args){
		TestJSA testjsa = new TestJSA();
		testjsa.matrixvector();
	}

	public void matrixvector()
	{
		JavaSparseArray jsa = new JavaSparseArray(values,indexes);
		JavaSparseArray jsb = new JavaSparseArray(densematrix);

		System.out.println("jsa=");
		System.out.println(jsa);
		System.out.println("jsb=");
		System.out.println(jsb);
//		for(int ii[] : jsb.getIndexArray()) System.out.println(Arrays.toString(ii));
//		for(float ii[] : jsb.getValueArray()) System.out.println(Arrays.toString(ii));
		System.out.println(jsa.times(jsb));
		

	}

	public void write(double[] vec){
		for(int i = 0;i<vec.length;i++)System.out.print(vec[i]+" ");
		System.out.print("\n");
	}	
}
