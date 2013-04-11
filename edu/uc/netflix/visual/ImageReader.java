package edu.uc.netflix.visual;

public class ImageReader {
	
	
	public ImageReader(){
		
	}
	
	public static void main(String[] args)
	{
		
		System.out.println(ImageReader.hex2decimal(args[0]));
	}
	
	   public static int hex2decimal(String s) 
	   {
	        String digits = "0123456789ABCDEF";
	        s = s.toUpperCase();
	        int val = 0;
	        for (int i = 0; i < s.length(); i++) {
	            char c = s.charAt(i);
	            int d = digits.indexOf(c);
	            val = 16*val + d;
	        }
	        return val;
	    }

}
