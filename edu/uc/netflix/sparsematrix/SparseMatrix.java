package edu.uc.netflix.sparsematrix;

import java.util.ArrayList;


public class SparseMatrix {
	int[][]	rowsindices;
	float[][] rowsdata;
	int rows;
	int cols;
	
	/**Creates a deep copy of the input elements
	 * @param rowsindices
	 * @param rowsdata
	 */
	public SparseMatrix(int[][] rowsindices, float[][] rowsdata)
	{
		if(rowsindices.length != rowsdata.length){ 
			System.out.println("Invalid Sparse Matrix Format\n number of rows must be equal");
			return;
		}
		this.rowsdata = new float[rowsdata.length][];
		this.rowsindices = new int[rowsindices.length][];
		
		this.rows = rowsdata.length;
		int colmax = 0;
		for(int i = 0; i< rowsdata.length; i++)
		{
			this.rowsdata[i] = new float[rowsdata[i].length];
			this.rowsindices[i] = new int[rowsindices[i].length];
			if(rowsindices[i].length != rowsdata[i].length){
				System.out.println("Invalid Sparse Matrix Format:\n\tError in row "+i+ ". Column length must be equal");
				return;
			}
			cols = 0;
			for(int j = 0;j< rowsdata[i].length;j++)
			{
				cols++;
				this.rowsindices[i][j] = rowsindices[i][j]; 
				this.rowsdata[i][j] = rowsdata[i][j];
			}
			if(cols > colmax) colmax = cols;
		}
	}
	
	
	
	
	public int getCols() {
		return cols;
	}




	public void setCols(int cols) {
		this.cols = cols;
	}




	public int getRows() {
		return rows;
	}




	public void setRows(int rows) {
		this.rows = rows;
	}




	public float[][] getRowsdata() {
		return rowsdata;
	}




	public void setRowsdata(float[][] rowsdata) {
		this.rowsdata = rowsdata;
	}




	public int[][] getRowsindices() {
		return rowsindices;
	}




	public void setRowsindices(int[][] rowsindices) {
		this.rowsindices = rowsindices;
	}




	/**Creates a deep copy of the input matrix
	 * @param rowsindices
	 * @param rowsdata
	 */
	public SparseMatrix(SparseMatrix s)
	{
		this(s.rowsindices,s.rowsdata); 
	
	}
	
	public SparseMatrix(float[][] denseMatrix)
	{
		this(denseMatrix,0f);
	
	}
	
	public SparseMatrix(float[][] denseMatrix, float approxZero)
	{
		
		this.rowsdata = new float[denseMatrix.length][];
		this.rowsindices = new int[denseMatrix.length][];
		rows = denseMatrix.length;
		int colsmax = 0;
		for(int i = 0;i< denseMatrix.length; i++)
		{
			ArrayList<Integer> rownindices = new ArrayList<Integer>(denseMatrix[i].length);
			ArrayList<Float> rowndata = new ArrayList<Float>(denseMatrix[i].length);
			int j = 0;
			cols=0;
			for(; j< denseMatrix[i].length;j++)
			{
				if(Math.abs(denseMatrix[i][j])> approxZero)
				{	
					cols++;
					rownindices.add(j);
					rowndata.add(denseMatrix[i][j]);
				}
			}
			if(cols > colsmax)colsmax = cols;
			
			
			this.rowsdata[i]= new float[rowndata.size()];
			this.rowsindices[i] = new int[rownindices.size()];
			
			//seperating should avoid paging issues, if any
			j=0;
			for(Float f: rowndata){
				this.rowsdata[i][j] = f.floatValue();
				j++;
			}
			
			j=0;
			for(Integer ind: rownindices){

				this.rowsindices[i][j] = ind.intValue();
				j++;
			}

		}
		cols = colsmax;
		 
	}


	public SparseMatrix transpose()
	{
		float[][] trans = new float[cols][rows];//the rows will be the cols
		for(int i = 0; i<rowsdata.length;i++)//iterate columns
		{
			for(int j = 0;j<rowsdata[i].length;j++)
			{
				trans[rowsindices[i][j]][i] = rowsdata[i][j] ;
			}
		}
		
		SparseMatrix t = new SparseMatrix(trans);
		return t;
	
	}


	@Override
	public String toString() {
		
		String out = new String();
		for(int i = 0; i< this.rowsdata.length;i++) {
			for(int j = 0; j< this.rowsdata[i].length;j++){
				out +=  +this.rowsdata[i][j] + " ";
			}
			out+=';';
		}
//		for(int i = 0; i< this.rowsdata.length;i++) {
//			for(int j = 0; j< this.rowsdata[i].length;j++){
//				out += (" [" +this.rowsdata[i][j] + "," +this.rowsindices[i][j] + "] " );
//			}
//			out+='\n';
//		}
		return out;
	}
	

	
}
