package edu.uc.netflix.coocurrance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import Jama.Matrix;

import edu.uc.netflix.plsa.PLSA2;

public abstract class AbstractCoocurrence {

		HashMap<String,Integer> termlist;//userlist
		HashMap<Integer,String> revTermList;
		HashMap<String,Integer> documentlist;//movielist
		HashMap<Integer,String> revDocumentList;

		double [][] termtopic;//movie/user list
		int terms;
		int topics;
		File input;
		PLSA2 process;
		
		public AbstractCoocurrence(File f){
			input = f;
		}
		
		/**This method parses input and loads items into the HashMap documentlist. The integers can simply be accumulated
		 * they are used to relate to the termtopic matrix. To make output easier, the reverse of the list is also stored in the HashMap
		 * revDocumentList.
		 * @param reader
		 * @throws IOException
		 */
		public abstract void createDocumentList(BufferedReader reader)throws IOException;

		/**This method parses input and loads items into the HashMap termlist. The integers can simply be accumulated
		 * they are used to relate to the termtopic matrix. To make output easier, the reverse of the list is also stored in the HashMap
		 * revTermList.
		 * @param reader
		 * @throws IOException
		 */
		public abstract void createTermList(BufferedReader reader)throws IOException;

		
		/**This method will correlate the lists into the termtopic matrix. This matrix forces each item of the termList to be a unique row
		 * in the matrix and every item from documentlist as a column. Coocurrence is the accumulation of the occurences or rating
		 * of an item from one list as it appears in another. See SimpleDocReader for an example
		 */
		public abstract void correlateLists();
		
		public double[][] generateTermDocMatrix()throws IOException{
			BufferedReader reader = new BufferedReader(new FileReader(input));
			termlist = new HashMap<String,Integer>();
			revTermList = new HashMap<Integer,String>();
			createTermList(reader);
			
			reader = new BufferedReader(new FileReader(input));
			documentlist = new HashMap<String, Integer>();
			revDocumentList = new HashMap<Integer,String >();
			createDocumentList(reader);
			termtopic = new double[topics][terms];
			
			correlateLists();
			return termtopic;
		}
		
		/**
		 * @param groupsize number of correlated items to output per latent class
		 * @param k latent classes
		 * @param epsilon update convergence parameter (around .00001f is acceptable)
		 * @throws IOException
		 */
		public void doPLSA(int groupsize, int k, double epsilon)throws IOException
		{
			double[][] termdoc = generateTermDocMatrix();
			process = new PLSA2(new Matrix(termdoc),k,epsilon);
			outputLists(groupsize);
		}

		
		/**Output the highest rated members of each latent class.
		 * @param groupsize number of members to output per class
		 */
		public void outputLists(int groupsize)
		{
			
			StringBuffer row = new StringBuffer(100);
//			bf.write("WT'\n");
			for(double[]ff: PLSA2.transpose(process.wt))
			{
				for(int i = 0;i<groupsize && i< ff.length;i++)
				{
					double greatest = 0;
					int index = 0;
					//output highest probability terms for the latent classes
					for(int j = 0; j<ff.length;j++)
					{
						if(ff[j]>greatest){
							greatest = ff[j];
							index = j;
						}
					}
					
					row.append(this.revTermList.get(new Integer(index)) +" "+ff[index]+", ");
					ff[index] = 0;
				}
//				bf.write(row+"\n");
				System.out.println(row);
				row = new StringBuffer("");
			}
			
//			bf.write("TD\n");
			for(double[]ff: process.td)
			{
				for(int i = 0;i<groupsize && i<ff.length;i++)
				{
					double greatest = 0;
					int index = 0;
					
					////output highest probability documents for the latent classes
					for(int j = 0; j<ff.length;j++)
					{
						if(ff[j]>greatest)
						{
							greatest = ff[j];
							index = j;
						}
					}
					
					row.append(this.revDocumentList.get(new Integer(index)) +" "+ff[index]+", ");
					ff[index] = 0;
				}
				//bf.write(row+"\n");
				System.out.println(row.toString().replaceAll("\n",""));
				row = new StringBuffer("");
			}
		}

}
