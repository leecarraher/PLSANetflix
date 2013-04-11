package edu.uc.netflix.visual;

import java.util.Random;

public class ThreeDGraffics 
{

	float[][] pts;
	
	float[] cosinStore;
	int storageAcc = 626;//.01 accuracy for sin and cos lookups
	
	public ThreeDGraffics(float[][] pts)
	{
		loadCosins();
		this.pts = pts;
		
	}
	
	/**
	 * This will load the sins array. Using prebuffered trig functions
	 * is advisable since we are allow using arbitrary precision rotations.
	 * And the function to calc trig functions is a slowly converging summation  
	 */
	private void loadCosins()
	{
		cosinStore = new float[storageAcc];
		float step =((float)2*(float)Math.PI)/storageAcc;
		
		for(int i = 0;i<storageAcc;i++)
		{
			cosinStore[i]=(float)Math.cos(step*(float)i);
		}
		
	}

	private float cos(float r)
	{
		if(r<0)r = Math.abs(r);
		if(r>2*Math.PI)r=r-((int)(r/(2*(float)Math.PI))*(float)Math.PI *2);//downshift to domain 0 to 2pi
		int i = (int)(r/(2F*(float)Math.PI)*storageAcc)%storageAcc;
		return cosinStore[(int)i];
	}
	
	//cot = 1/tan = sin/cos
	// at current storage value it agree within err=1    0.042
	//											err=.1   0.1325
	//											err=.01  0.4943
	// this is due to the propagated error for sin and cos
	// as well as assymptotic cos behavior near at pi/2 
	private float cot(float r){
		return cos(r)/sin(r);
	}
	
	//sin = cos(pi/2-theta)
	private float sin(float r)
	{
		return (float)cos((float)Math.PI/2-r);
	}
	
	public static void main(String[] args)
	{
		Random r = new Random(2<<13-1);
		float[][] pts = new float[20][3];
		for(int i = 0; i<20;i++)
		{
			for(int j = 0;j<3;j++)
				pts[i][j] = (float)r.nextGaussian();
		}
		
		//ThreeDGraffics.output(pts);
		
		//System.out.println();
		ThreeDGraffics tdg = new ThreeDGraffics(pts);
		tdg.rotate((float)Math.PI,(float) Math.PI/3f, (float)Math.PI/4f);
		tdg.perspectiveMatrix((float)Math.PI/2, (float)Math.PI/3, 1000F, 0F);
	}
	
	//World matrix, s_x,s_y,s_z are scaling, gamma, alpha, beta are rotation angles
	//s_xcos(gamma)cos(beta)                                           -s_ysin(gamma)cos(beta)                              s_zsin(beta)  			x
	//s_xcos(gamma)sin(beta)sin(alpha)+s_xsin(gamma)cos(alpha)   s_ycos(gamma)cos(alpha)-s_ysin(gamma)sin(beta)sin(alpha)	-s_zcos(beta)sin(alpha) y
	//s_xsin(gamma)sin(alpha)-s_xcos(gamma)sin(beta)cos(alpha)   s_ysin(gamma)sin(beta)cos(alpha)+s_ysin(alpha)cos(gamma)	s_zcos(beta)cos(alpha)	z
	//					0														0													0				1 
	/**This matrix represents a worldmatrix for any rotation of the points x,y,z about their respective axis
	 * x->alpha (rads), y->beta (rads), z->gamma (rads); also scaling factors s_x,y,z can be used for scaling
	 * @param alpha
	 * @param beta
	 * @param gamma
	 * @param s_x
	 * @param s_y
	 * @param s_z
	 * @param x -> translation along
	 * @param y
	 * @param z
	 * @return
	 */
	private float[][] worldMatrixR(float alpha,float beta,float gamma,float s_x, float s_y, float s_z, float x, float y, float z)
	{
		float[][] worldMat = new float[4][4];
		worldMat[0][0]=s_x*cos(gamma)*cos(beta);
		worldMat[0][1]=-s_y*sin(gamma)*cos(beta);
		worldMat[0][2]=s_z*sin(beta);
		worldMat[0][3]=z;
		
		worldMat[1][0]=s_x*cos(gamma)*sin(beta)*sin(alpha)+s_x*sin(gamma)*cos(alpha);
		worldMat[1][1]=-s_y*cos(gamma)*cos(alpha)-s_y*sin(gamma)*sin(beta)*sin(alpha);
		worldMat[1][2]=-s_z*cos(beta)*sin(alpha);
		worldMat[1][3]=y;
		
		worldMat[2][0]=s_x*sin(gamma)*sin(alpha)-s_x*cos(gamma)*sin(beta)*cos(alpha);
		worldMat[2][1]=s_y*sin(gamma)*sin(beta)*cos(alpha)+s_y*sin(alpha)*cos(gamma);
		worldMat[2][2]=s_z*cos(beta)*cos(alpha);
		worldMat[2][3]=z;
		
		worldMat[3][0]=0;
		worldMat[3][1]=0;
		worldMat[3][2]=0;
		worldMat[3][3]=1;

		return worldMat;
	}
	
	//World matrix, s_x,s_y,s_z are scaling, gamma, alpha, beta are rotation angles
	//s_xcos(gamma)cos(beta)                                           -s_ysin(gamma)cos(beta)                              s_zsin(beta)  			x
	//s_xcos(gamma)sin(beta)sin(alpha)+s_xsin(gamma)cos(alpha)   s_ycos(gamma)cos(alpha)-s_ysin(gamma)sin(beta)sin(alpha)	-s_zcos(beta)sin(alpha) y
	//s_xsin(gamma)sin(alpha)-s_xcos(gamma)sin(beta)cos(alpha)   s_ysin(gamma)sin(beta)cos(alpha)+s_ysin(alpha)cos(gamma)	s_zcos(beta)cos(alpha)	z
	//					0														0													0				1 
	/**This matrix represents a worldmatrix for any rotation of the points x,y,z about their respective axis
	 * x->alpha (rads), y->beta (rads), z->gamma (rads); also scaling factors s_x,y,z can be used for scaling
	 * @param alpha
	 * @param beta
	 * @param gamma
	 * @param s_x
	 * @param s_y
	 * @param s_z
	 * @param x -> translation along
	 * @param y
	 * @param z
	 * @return
	 */
	private float[][] worldMatrixQ(float phi,float theta,float psi,float s_x, float s_y, float s_z, float x, float y, float z)
	{
		float[][] worldMat = new float[4][4];
		float e0 = cos(.5f*(phi+psi))*cos(.5f*theta);
		float e1 = cos(.5f*(phi-psi))*sin(.5f*theta);
		float e2 = sin(.5f*(phi-psi))*sin(.5f*theta);
		float e3 = sin(.5f*(phi+psi))*cos(.5f*theta);
		
		worldMat[0][0]=s_x*(e0*e0+e1*e1-e2*e2-e3*e3);
		worldMat[0][1]=-s_y*(2*(e1*e2+e0*e3));
		worldMat[0][2]=s_z*(2*(e1*e3-e0*e2));
		worldMat[0][3]=z;
		
		worldMat[1][0]=s_x*(2*(e1*e2-e0*e3));
		worldMat[1][1]=-s_y*(e0*e0-e1*e1+e2*e2-e3*e3);
		worldMat[1][2]=-s_z*(2*(e2*e3+e0*e1));
		worldMat[1][3]=y;
		
		worldMat[2][0]=s_x*(2*(e1*e3+e0*e2));
		worldMat[2][1]=s_y*(2*(e2*e3-e0*e1));
		worldMat[2][2]=s_z*(e0*e0-e1*e1-e2*e2+e3*e3);
		worldMat[2][3]=z;
		
		worldMat[3][0]=0;
		worldMat[3][1]=0;
		worldMat[3][2]=0;
		worldMat[3][3]=1;

		return worldMat;
	}
	
	
	
	public float[][] rotate(float alpha, float beta, float gamma){
		
		float[][] world = worldMatrixQ(alpha, beta, gamma,1,1,1,0,0,0); 
		
		System.out.println("Quaternions");
		output(worldMatrixQ(alpha, beta, gamma,1,1,1,0,0,0));
		System.out.println("Angles");
		output(worldMatrixR(alpha, beta, gamma,1,1,1,0,0,0));
		
		for(int ii = 0;ii<pts.length;ii++)
		{
			pts[ii] = vecMatMult(pts[ii],world);
		}
		
		return pts;
			
	}
	
	//[x']										[x]
	//[y'] = [perspective]X [WorldTransform]  X	[y]
	//[z']										[z]
	//[w']										[1]
	// final coordinates are (x,y)-> (x'/w',y'/w')
	
	//perspective
	//cot(mu)	0		0			0
	//0			cot(v)	0			0
	//0			0		(B+F)/(B-F)	0
	//0			0		1			0
	//tan(mu) = (4/3)tan(v) tan(mu) = 1-5 
	
	/**Calculate the perspective transform. B and F are the distance of the max foreground, and background.
	 * mu is the horizontal visual angle, v is the vertical visual angle 
	 * @param mu
	 * @param v
	 * @param B
	 * @param F
	 * @return
	 */
	private float[][] perspectiveMatrix(float mu, float v, float B, float F)
	{
		float[][] perspective = new float[4][4];
		
		
		perspective[0][0]=cot(mu);
		perspective[0][1]=0;
		perspective[0][2]=0;
		perspective[0][3]=1;
		
		perspective[1][0]=0;
		perspective[1][1]=cot(v);
		perspective[1][2]=0;
		perspective[1][3]=1;
		
		perspective[2][0]=0;
		perspective[2][1]=0;
		perspective[2][2]=(B+F)/(B-F);
		perspective[2][3]=-2*B*F/(B-F);
		
		perspective[3][0]=0;
		perspective[3][1]=0;
		perspective[3][2]=1;
		perspective[3][3]=0;
		
		output(this.pts);
		
		return perspective;
		
	}
	
//	output matrix	
	public static void output(float[][] d){
		for(float dd[]:d)
		{
			System.out.print("\n\t");
			for(float ddd:dd)
			{
				String element = String.valueOf(ddd);
				if(element.length()<5)
				{
					for(int i = element.length();i<6;i++)element+="0";
				}
				else element = element.substring(0,6);
				System.out.print(element + " ");
			}
				
		}		
		System.out.println();
	}

	//naive multiply for testing
	public float[][] multiply(float[][] in1, float[][] in2){
		float[][] rtrn = new float[in1.length][in2[0].length];		
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
	
	//naive multiply for testing
	public float[] vecMatMult(float[] in1, float[][] in2){
		float[] rtrn = new float[in1.length];
		
		for(int i = 0; i<in1.length;i++)
		{
			for(int j = 0; j<in2[0].length;j++)
			{
				rtrn[i] = 0f;
				for(int k=0; k<in2.length;k++)
				{
					rtrn[i]+=in1[i]*in2[k][j];	
				}	
			}
		}
		return rtrn;
	}
	
}
