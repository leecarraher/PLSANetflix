package edu.uc.netflix.coocurrance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class SimpleDocReader extends AbstractCoocurrence
{
	
	public SimpleDocReader(String url){
		super(new File(url));
	}
	
	
	public void createDocumentList(BufferedReader reader)throws IOException
	{
		StringBuffer buffer = new StringBuffer(100);
		topics = 0;
		char c;
		while((c=(char)reader.read()) !=(char)-1)
		{
			if(c!='\n'){
				if(c=='.'||c=='?'||c=='!'){
					String topic = buffer.toString().toLowerCase().replaceAll("[.,;?!]", "");
					documentlist.put(topic,topics);
					revDocumentList.put(topics++, topic);
					buffer = new StringBuffer(100);
				}
				else
					buffer.append(c);
			}
		}
	}
	
	//Store a list of all words
	public void createTermList(BufferedReader reader)throws IOException
	{
		String s = "";
		terms = 0;
		while(( s = reader.readLine())!=null)
		{
			String[] spaceSplit = s.split("[\\s-_()\\[\\]\"']");
			for(String word:spaceSplit)
			{
				String cleaned = word.toLowerCase().replaceAll("[.,;?!]", "");
				if(!termlist.containsKey(cleaned))
				{
					termlist.put(cleaned,terms);
					revTermList.put(terms++, cleaned);
				}
			}
		}
	}
	
	public float[][] generateTermDocMatrix()throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(input));
		termlist = new HashMap<String,Integer>();
		revTermList = new HashMap<Integer,String>();
		createTermList(reader);
		
		reader = new BufferedReader(new FileReader(input));
		documentlist = new HashMap<String, Integer>();
		revDocumentList = new HashMap<Integer,String >();
		createDocumentList(reader);
		termtopic = new float[topics][terms];
		
		correlateLists();
		System.out.println(Arrays.toString(termtopic));
		return termtopic;
	}

	public void correlateLists()
	{
		for(int i = 0;i<termtopic.length;i++){
			Arrays.fill(termtopic[i], 0f);
		}
		
		for(String topic: documentlist.keySet())
		{
			int topicindex = documentlist.get(topic).intValue(); 
			String[] spaceSplit = topic.split("[\\s-_()\\[\\]\"']");
			for(String s: spaceSplit)
			{
				if(termlist.containsKey(s))
					termtopic[topicindex][termlist.get(s).intValue()]++;
			}		
		}
		
	}
	
	public static void main(String[] args)
	{
		if(args.length!=4){
			System.out.println("usage: document groupsize k epsilon" +
				"\n\tdocument - text document with period delimmited topics" +
				"\n\tgroupsize - number of items to output per latent class " +
				"\n\tk - number of latent classes " +
				"\n\tepsilon - cutoff convergence parameter");
		 
			return;
		}
		SimpleDocReader sdr = new SimpleDocReader(args[0]);
		
		try{
			sdr.doPLSA(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Float.parseFloat(args[3]));
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("done!");


	}
}
