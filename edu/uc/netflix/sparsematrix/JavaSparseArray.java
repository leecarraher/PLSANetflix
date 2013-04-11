package edu.uc.netflix.sparsematrix;

import java.util.Arrays;

/**
  * This class implements a sparse matrix as 
  * Java Sparse Arrays.
  * @author Geir Gundersen
  * @version 1.0
  */
  public class JavaSparseArray{
	/**The 2D array that stores the values*/
  	private float[][] Avalue;
	/**The 2D array that stores the indexes*/
  	private int[][] Aindex;
	/**The dimension of the matrix*/
  	private int dimension;
  	
	private int m;
  	private int n;
   	
  	/**Constructor for the class
  	  */
  	  public JavaSparseArray(float[][] Avalue,int[][] Aindex)
  	  {
  	  	this.Avalue = Avalue;
  	  	this.Aindex = Aindex;
  	  	n = 0;
  	  	for(int i =0;i<Aindex.length;i++ ){
  	  		for(int j=0;j<Aindex[i].length;j++)
  	  		{
  	  			if(Aindex[i][j]>n)n=Aindex[i][j];
  	  		}
  	  	}
  	  	n++;
  	  	
  	  	dimension = Avalue.length;
  	  	m=dimension;
  	  }
  	  
    	/**Constructor for the class
   	  */
   	  public JavaSparseArray(float[][] dense)
   	  {
   	  	Avalue = new float[dense.length][];
   	  	Aindex = new int[dense.length][];
   	  	
   	  	dimension = dense.length;
   	  	m=dimension;
   	  	if(m>0)
   	  		n=dense[0].length;
   	  	else return;
   	  	
   	  	for(int i = 0;i<dense.length;i++)
   	  	{
   	  		int nonzeros =0;
   	  		float[] denseRow = dense[i];

   	  		
   	  		for(int j = 0;j<denseRow.length;j++)
   	  		{
   	  			if(denseRow[j]!=0.0)nonzeros++;
   	  		}
   	  		
   	  		Avalue[i] = new float[nonzeros];
   	  		Aindex[i] = new int[nonzeros];
   	  		float[] sparseValue = Avalue[i];
   	  		int[] sparseIndex = Aindex[i];
   	  		nonzeros = 0;
   	  		
   	  		for(int j = 0;j<denseRow.length;j++)
   	  		{
   	  			if(denseRow[j]!=0.0)
   	  			{
   	  				sparseValue[nonzeros] = denseRow[j];
   	  				sparseIndex[nonzeros] = j;
   	  				nonzeros++;
   	  			}
   	  		}
   	  		
   	  		
   	  	}
   	  }
  	  
  	  /**A method for setting the value array
	  @param float[][] value
  	  */
  	  public void setValueArray(float[][] value){Avalue = value;}
  	  
  	  /**A method for setting the index array
	  @param int[][] index
  	  */
  	  public void setIndexArray(int[][] index){Aindex = index;} 
  	
  	
	 /**A method for getting the value array
	 @return float[][]
  	  */
  	  public float[][] getValueArray(){return Avalue;}
  	  
  	  /**A method for getting the index array
	  @return float[][]
  	  */
  	  public int[][] getIndexArray(){return Aindex;} 
	  
	  /**Returns an element from the matrix at position (i,j)
	  @param int i, int j
   	  @return float element
	  */
	  public float elementAt(int i, int j){
	  	float element = (float)0.0;
		boolean test = true;
		float[] value = Avalue[i];
		int[] index = Aindex[i];
		for(int k = 0;k<value.length&&test;k++){
			if(j==index[k]){
				test = false;
				element = value[k];
			}
		}
		return element;
	  }
	  
	  /**Sets an element from the matrix at position (i,j)
	  @param int i, int j, float v
	  */
	  public void setElementAt(int i, int j, float v){
	  	float element = (float)0.0;
		boolean test = true;
		float[] value = Avalue[i];
		int[] index = Aindex[i];
		for(int k = 0;k<value.length&&test;k++){
			if(j==index[k]){
				test = false;
				element = value[k];
			}
		}
		if(test)
			addElementAt(i, j, v);
		else
			value[j] = v;
	  }
	  
	  /**Removes an element from the matrix at position (i,j)
	  @param int i, int j, float v
	  */
	  public void removeElementAt(int i, int j){
	  	int stop = 0;
	  	float element = (float)0.0;
		boolean test = true;
		float[] value = Avalue[i];
		int[] index = Aindex[i];
		for(int k = 0;k<value.length&&test;k++){
			if(j==index[k]){
				test = false;
				stop = k;
				element = value[k];
			}
		}
		float[] value1 = new float[value.length-1];
		int[] index1 = new int[value.length-1];
		System.arraycopy(value, 0, value1, 0, stop-1);
		System.arraycopy(index, 0, index1, 0, stop-1);
		System.arraycopy(value, stop+1, value1, stop, value1.length);
		System.arraycopy(index, stop+1, index1, stop, value1.length);
		Avalue[i] = value1;
		Aindex[i] = index1;
	  }
	  
	  /**Adds an element at the matrix at position (i,j)
	  @param int i, int j, float v
	  */
	  public void addElementAt(int i, int j, float v){
	  	int stop = 0;
	  	float element = (float)0.0;
		boolean test = true;
		float[] value = Avalue[i];
		int[] index = Aindex[i];
		for(int k = 0;k<value.length&&test;k++){
			if(j==index[k]){
				test = false;
				element = value[k];
			}
		}
		float[] value1 = new float[value.length+1];
		int[] index1 = new int[value.length+1];
		System.arraycopy(value, 0, value1, 0, stop-1);
		System.arraycopy(index, 0, index1, 0, stop-1);
		value1[stop] = v;
		index1[stop] = j;
		System.arraycopy(value, stop, value1, stop+1, value1.length);
		System.arraycopy(index, stop, index1, stop+1, value1.length);
		Avalue[i] = value1;
		Aindex[i] = index1;
	  }

  	  /**A method for multiplying a matrix with a vector returning a vector
	  @param float[] b
   	  @return float[] c
  	  */
  	  public float[] matrixvector(float[] b){
  	  	float x = (float)0.0;
  	  	float[] value;
  	  	int[] index;
  	  	int k = 0;
  	  	int vlength = 0;
  	  	float[] c = new float[b.length];
  	  	int alength = Avalue.length;
  	  	for(int i=0;i<alength;i++){
  	  		value = Avalue[i];
  	  		index = Aindex[i];
  	  		for(int j=0;j<value.length;j++){
  	  			x += value[j]*b[index[j]];
  	  		}
  	  		c[i]=x;
  	  		x = 0;
  	  	}
    	  	return c;
  	   }
  	  
  	  
  	   
	/**A method for multiplying a vector with a matrix returning a vector
	@param float[] bvalue
   	@return float[] cvalue
  	*/   
	public float[] vectormatrix(float[] bvalue){
		float[] valuerow = null;
		int[] indexrow = null;
		int alength = Avalue.length;
		float[] cvalue = new float[bvalue.length];
		float value = 0;
		for(int i = 0;i<alength;i++){
			valuerow = Avalue[i];
			indexrow = Aindex[i];
			float val = bvalue[i];
			int vrow = valuerow.length;
			for(int j = vrow-1;j>0;--j){
				cvalue[indexrow[j]] += value*valuerow[j];
			}
		}
		return cvalue;
	}

	/**A method for multiplying two matrices returning the result matrix
	@param JavaSparseArray B
   	@return JavaSparseArray C
  	*/   
	public JavaSparseArray times(JavaSparseArray B){
		float[][] Cvalue = new float[dimension][1];
		int[][] Cindex = new int[dimension][1];
		int[] temp = new int[dimension];
		float[] tempValue = new float[dimension];
		int[] tempIndex = new int[dimension];
		float[][] Bvalue = B.getValueArray();
		int[][] Bindex = B.getIndexArray();
		float scalar = 0;
		int len = -1;
		int index = 0;
		int jcol = 0;
		int jpos = 0;
		int nonzero = 0;
		float[] avalue;
		int[] aindex;
		float[] bvalue;
		int[] bindex;
		float[] cvalue;
		int[] cindex;
		for(int i = 0;i<temp.length;i++){temp[i]=-1;}
		for(int i = 0;i<Avalue.length;i++){
			avalue = Avalue[i];
			aindex = Aindex[i];
			for(int j = 0;j<avalue.length;j++){
				scalar = avalue[j];
				index = aindex[j];
				bvalue = Bvalue[index];
				bindex = Bindex[index];
				for(int k = 0;k<bvalue.length;k++){
					jcol = bindex[k];
					jpos = temp[jcol];
					if(jpos == -1){
						len++;
						nonzero++;
						tempIndex[len] = jcol;
						temp[jcol] = len;
						tempValue[len] = scalar*bvalue[k];

					}else{
						tempValue[jpos]+=scalar*bvalue[k];
					}
				}
			}
			cvalue = new float[len+1];
			cindex = new int[len+1];
			System.arraycopy(tempValue, 0, cvalue, 0, len+1);
			System.arraycopy(tempIndex, 0, cindex, 0, len+1);
			Cvalue[i] = cvalue;
			Cindex[i] = cindex;
			for(int ii = 0;ii<len+1;ii++){temp[tempIndex[ii]]=-1;}
			len = -1;
		}
		return new JavaSparseArray(Cvalue, Cindex);
	}
	
	/**Pairwise divide this matrix by the B matrix.
	 * @param B
	 * @return dotDivide result A./B
	 */
	public JavaSparseArray dotDivide(JavaSparseArray B)
	{
		int[][] Bindex = B.getIndexArray();
		float[][] Bvalue = B.getValueArray();
		int[][] Cindex = new int[dimension][];
		float[][] Cvalue = new float[dimension][];
		int nonzero = 0;
		
		for(int i = 0;i<dimension;i++){
			int num = 0;
			int[] bindex = Bindex[i];
			float[] bvalue = Bvalue[i];
			int[] aindex = Aindex[i];
			float[] avalue = Avalue[i];
			
			boolean[] switchArray = new boolean[aindex.length];
			float[] tempValue = new float[aindex.length];
			
			int j=0;
			for(int ii = 0;ii<aindex.length;ii++)
			{
				while(j<bindex.length&&aindex[ii]>bindex[j])j++;
				
				if(j<bindex.length&&aindex[ii]==bindex[j])
				{
					tempValue[ii] =avalue[ii]/bvalue[j];
					switchArray[ii] = true;
					num++;
				}else switchArray[ii] = false;
			}
			
			
			
			int[] cindex = new int[num];
			float[] cvalue = new float[num];
			
			for(int ii = 0, c = 0;ii<switchArray.length;ii++){
				
				if(switchArray[ii])
				{
					cindex[c]=aindex[ii];
					cvalue[c]=tempValue[ii];
					c++;
				}
			}

			Cindex[i] = cindex;
			Cvalue[i] = cvalue;
			nonzero += num;
		}
		return new JavaSparseArray(Cvalue, Cindex);	
		
	}
	
	/**Dot multiply this sparse matrix and the B matrix.
	 * dot multiply is a pairwise multiply
	 * @param B
	 * @return dotmultiply product
	 */
	public JavaSparseArray dotMultiply(JavaSparseArray B)
	{
		int[][] Bindex = B.getIndexArray();
		float[][] Bvalue = B.getValueArray();
		int[][] Cindex = new int[dimension][];
		float[][] Cvalue = new float[dimension][];
		int nonzero = 0;
		
		for(int i = 0;i<dimension;i++){
			int num = 0;
			int[] bindex = Bindex[i];
			float[] bvalue = Bvalue[i];
			int[] aindex = Aindex[i];
			float[] avalue = Avalue[i];
			
			boolean[] switchArray = new boolean[aindex.length];
			float[] tempValue = new float[aindex.length];
			
			int j=0;
			for(int ii = 0;ii<aindex.length;ii++)
			{
				while(j<bindex.length&&aindex[ii]>bindex[j])j++;
				
				if(j<bindex.length&&aindex[ii]==bindex[j])
				{
					tempValue[ii] =avalue[ii]*bvalue[j];
					switchArray[ii] = true;
					num++;
				}else switchArray[ii] = false;
			}
			
			
			
			int[] cindex = new int[num];
			float[] cvalue = new float[num];
			
			for(int ii = 0, c = 0;ii<switchArray.length;ii++){
				
				if(switchArray[ii])
				{
					cindex[c]=aindex[ii];
					cvalue[c]=tempValue[ii];
					c++;
				}
			}

			Cindex[i] = cindex;
			Cvalue[i] = cvalue;
			nonzero += num;
		}
		return new JavaSparseArray(Cvalue, Cindex);	
		
	}
	
	/**Multiply the current matrix by the log base e of the elements of
	 * the B matrix
	 * @param B
	 * @return A.*log(B)
	 */
	public JavaSparseArray logDotMultiply(JavaSparseArray B)
	{
		int[][] Bindex = B.getIndexArray();
		float[][] Bvalue = B.getValueArray();
		int[][] Cindex = new int[dimension][];
		float[][] Cvalue = new float[dimension][];
		int nonzero = 0;
		
		for(int i = 0;i<dimension;i++){
			int num = 0;
			int[] bindex = Bindex[i];
			float[] bvalue = Bvalue[i];
			int[] aindex = Aindex[i];
			float[] avalue = Avalue[i];
			
			boolean[] switchArray = new boolean[aindex.length];
			float[] tempValue = new float[aindex.length];
			
			int j=0;
			for(int ii = 0;ii<aindex.length;ii++)
			{
				while(j<bindex.length&&aindex[ii]>bindex[j])j++;
				
				if(j<bindex.length&&aindex[ii]==bindex[j])
				{
					tempValue[ii] =avalue[ii]*(float)Math.log(bvalue[j]);
					switchArray[ii] = true;
					num++;
				}else switchArray[ii] = false;
			}
			
			
			
			int[] cindex = new int[num];
			float[] cvalue = new float[num];
			
			for(int ii = 0, c = 0;ii<switchArray.length;ii++){
				
				if(switchArray[ii])
				{
					cindex[c]=aindex[ii];
					cvalue[c]=tempValue[ii];
					c++;
				}
			}

			Cindex[i] = cindex;
			Cvalue[i] = cvalue;
			nonzero += num;
		}
		return new JavaSparseArray(Cvalue, Cindex);	
		
	}
	
	/**Sum all the elements in the matrix
	 * @return sum
	 */
	public float sum()
	{
		float rtrn = 0;
		for(float[] ff: Avalue)
			for(float f:ff)rtrn+=f;
		return rtrn;
	}

	/**get the column vector sum of all the elements of a matrix
	 * @return column totals
	 */
	public float[] sum1D()
	{
		float[] totals = new float[n];
		
		for(int i = 0; i<Aindex.length;i++){
			for(int j = 0; j<Aindex[i].length;j++)
			{
				totals[Aindex[i][j]]+=Avalue[i][j];
			}
		}
		return totals;
	}
	
	/**Normalize the columns of this matrix
	 * @return normalized matrix
	 */
	public JavaSparseArray normalize()
	{
		float[] totals = sum1D();
		
		for(int i = 0; i<Aindex.length;i++){
			for(int j = 0; j<Aindex[i].length;j++)
			{
				Avalue[i][j] = Avalue[i][j]/totals[Aindex[i][j]];
			}
		}
		
		return this;
	}
	
	/**A method for adding two matrices returning the result matrix
	@param JavaSparseArray B
   	@return JavaSparseArray C
  	*/      
	public JavaSparseArray addition(JavaSparseArray B){
		int[][] Bindex = B.getIndexArray();
		float[][] Bvalue = B.getValueArray();
		int[][] Cindex = new int[dimension][1];
		float[][] Cvalue = new float[dimension][1];
		int nonzero = 0;
		int num = 0;
		for(int i = 0;i<dimension;i++){
			int[] bindex = Bindex[i];
			float[] bvalue = Bvalue[i];
			int[] aindex = Aindex[i];
			float[] avalue = Avalue[i];
			boolean[] switchArray = new boolean[dimension];
			float[] tempValue = new float[dimension];
			for(int ii = 0;ii<aindex.length;ii++){
				switchArray[aindex[ii]] = true;
				tempValue[aindex[ii]] = avalue[ii];
			}
			num = aindex.length;
			for(int ii = 0;ii<bindex.length;ii++){
				if(!switchArray[bindex[ii]]){
					switchArray[bindex[ii]] = true;
					tempValue[bindex[ii]] = bvalue[ii];
					num++; 
				}
				else{
					tempValue[bindex[ii]] += bvalue[ii];
				}
			}
			int[] cindex = new int[num];
			float[] cvalue = new float[num];
			for(int ii = 0;ii<aindex.length;ii++){
				cindex[ii] = aindex[ii];
				switchArray[aindex[ii]] = false;
			}
			for(int ii = aindex.length,jj=0;ii<bindex.length;ii++,jj++){
				if(switchArray[bindex[jj]]){
					cindex[ii] = bindex[jj];
				}
			}
			for(int ii = 0;ii<cvalue.length;ii++){
				cvalue[ii] = tempValue[cindex[ii]];
			}
			Cindex[i] = cindex;
			Cvalue[i] = cvalue;
			nonzero += num;
		}
		return new JavaSparseArray(Cvalue, Cindex);	
	}

	/**A method for updating a matrix returning
	@param JavaSparseArray A
   	@return JavaSparseArray A (updated)
  	*/   
	public JavaSparseArray update(JavaSparseArray B){
		int[][] Bindex = B.getIndexArray();
		float[][] Bvalue = B.getValueArray();
		float[] tempValue = new float[dimension];
		int[] tempIndex = new int[dimension];
		int[] temp = new int[dimension];
		int len = -1;
		int index = 0;
		int jcol = 0;
		int jpos = 0;
		int nonzero = 0;
		int num = 0;
		for(int i = 0;i<temp.length;i++){temp[i] = -1;}
		for(int i = 0;i<dimension;i++){
			int[] aindex = Aindex[i];
			float[] avalue = Avalue[i];
			if(Bindex[i]!=null){
			for(int ii = 0;ii<aindex.length;ii++){
				len++;
				jcol = aindex[ii];
				tempIndex[len] = aindex[ii];
				temp[jcol] = len;
				tempValue[len] = avalue[ii];
			}
			int[] bindex = Bindex[i];
			float[] bvalue = Bvalue[i];
			for(int ii = 0;ii<bindex.length;ii++){
				jcol = bindex[ii];
				jpos = temp[jcol];
				if(jpos == -1){
					len++;
					tempIndex[len] = jcol;
					temp[jcol] = len;
					tempValue[len] = bvalue[ii]; 
				}
				else{
					tempValue[jpos] += bvalue[ii];
				}
			}
			int[] cindex = new int[len+1];
			float[] cvalue = new float[len+1];
			System.arraycopy(tempValue, 0, cvalue, 0,len+1);
			System.arraycopy(tempIndex, 0, cindex, 0,len+1);
			Aindex[i] = cindex;
			Avalue[i] = cvalue;
			for(int ii = 0;ii<len+1;ii++){temp[tempIndex[ii]] = -1;}
			len = -1;
			}
		}
		return new JavaSparseArray(Avalue, Aindex);	
	}
	
	/**Transpose the sparse matrix
	 * @return transposed SparseMatrix
	 */
	public JavaSparseArray transpose()
	{
		int[] sizes = new int[n];
		
		//the sizes of the transposed arrays	
		for(int i =0;i<m;i++)
		{
			for(int j =0;j<Aindex[i].length;j++)
			{
					sizes[Aindex[i][j]]++;
			}
		}
		
		int[][] Bindex = new int[n][];
		float[][] Bvalue = new float[n][];
		
		//create the transposed columns	
		for(int i =0;i<n;i++)
		{
			Bindex[i] = new int[sizes[i]];
			Bvalue[i] = new float[sizes[i]];
			sizes[i]=0;
			
		}
		
		int index = 0;
		for(int i =0;i<m;i++)
		{
			for(int j =0;j<Aindex[i].length;j++)
			{
					index = Aindex[i][j];
					Bindex[index][sizes[index]] = i;
					Bvalue[index][sizes[index]++] = Avalue[i][j];
			}
		}
		Aindex = new int[Bindex.length][];
		System.arraycopy(Bindex, 0, Aindex, 0,Bindex.length);
		System.arraycopy(Bvalue, 0, Avalue, 0,Bvalue.length);
		
		int temp = m;
		m = n;
		n=temp;
		
		return this;
	}
	
	public String toString(){
		StringBuffer output = new StringBuffer(m*n*3*8);
		for(int i =0;i<m;i++){
			for(int j =0;j<n;j++)
			{
				output.append(this.elementAt(i, j)+", ");
			}
			output.delete(output.length()-2, output.length());
			output.append('\n');
		}
		return output.toString();
	}
	
	/**Get the dense form of this matrix with zeros
	 * @return dense form of the Sparse Matrix
	 */
	public float[][] getDense(){
		float[][] dense = new float[m][n];
		for(int i =0;i<m;i++){
			for(int j =0;j<n;j++)
			{
				dense[i][j]=this.elementAt(i, j);
			}
		}
		return dense;
	}
	
	
	
}
  
