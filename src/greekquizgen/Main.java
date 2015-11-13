package greekquizgen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		//words[0] palavra
		//words[1] explicacao
		//words[2] resposta
		
		List<String[]> words = new ArrayList<String[]>();
		FileReader fr = new FileReader(args[1]);
		BufferedReader rd = new BufferedReader(fr);
		
		String line = rd.readLine();
		
		while(line != null) {
			words.add(line.split("@"));
			line = rd.readLine();
		}
		rd.close();
		
		processWords(args[2], Integer.parseInt(args[0]), words, words.size());
	
		System.out.println("Finished");
	}
	
	public static void processWords(String output, int type, List<String[]> words, int max) throws IOException {
		
		StringBuffer archive = new StringBuffer();
		StringBuffer questions = new StringBuffer();
		StringBuffer answers = new StringBuffer();
		
		for(int i = 0; i < words.size(); i++) {
			
			System.out.println("processando:" + words.get(i)[0]);
			
			if(i>0) {
				questions.append(",");
				answers.append(",");
			}
			
			questions.append("\n").append(createQuestion(type, words.get(i), i, max));
			answers.append(createAnswer(words.get(i)[2], i));
		}
		
		archive.append("{\n").append("questions:[\n").append(questions).append("],\n").
		append("answers:").append("[\n").append(answers).append("],").
		append("randomize: false").append("\n}");
		
		File fp = new File(output);
		
		if(fp.exists()) {
			fp.delete();
		}
		
		BufferedWriter bf = new BufferedWriter(new FileWriter(fp));
		bf.write(archive.toString());
		
		bf.flush();
		bf.close();
	}
	
	public static String createAnswer(String ans, int pos) {
		return "{id:"+pos+", text:\""+ans+"\"}\n";
	}
	
	public static String createQuestion(int type, String[] data, int pos, int max) {
		
		String quest = "Traduza:";
		
		if(type == 1) {
			quest = "Qual é esta forma?";
		}
		
		return "	{"+
		      	"\n		title: \""+quest+" "+data[0]+"\","+
		      	"\n		explanation: \""+data[1]+".\","+
		      	"\n		correct: "+pos+","+
			    "\n		patterns: ["+ createPatterns(max, pos) + "]"+
		    	"\n	}";
	}
	
	public static String createPatterns(int max, int pos) {
		
		boolean gerou = false;
		
		StringBuffer lines = new StringBuffer();
		StringBuffer line = new StringBuffer();
		
		Random rand = new Random();
		
		for(int i = 0; i < 3; i++) {
			
			for(int j = 0; j < 4; j++) {
				
				int num = rand.nextInt(max+1);
				
				if(num == pos) {
					gerou=true;
				}
				
				if(j == 0) {
					line.append("[").append(num);
				}	else {
					line.append(",").append(num);
				}			
			}
			
			if(!gerou) {
				line.append(",").append(pos);
			} else {
				int num = rand.nextInt(max+1);
				line.append(",").append(num);
			}
			
			gerou = false;
			if(i<2) {
				line.append("],");
			} else {
				line.append("]");
			}
			
			lines.append(line);
			
			line = new StringBuffer();
			
		}
		
		return lines.toString();
	}
}
