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

import greekquizgen.gui.Question;

public class Generator {
	
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
	
	public static void gerar(ArrayList<Question> questions, String output, int type) throws IOException {
		
		List<String[]> words = new ArrayList<String[]>();
		
		for(Question question: questions) {
			words.add(new String[]{question.word, question.explanation, question.answer});
		}
		
		processWords(output, type, words, words.size());
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
		
//		String quest = "Traduza:";
//		
//		if(type == 1) {
//			quest = "Qual é esta forma?";
//		}
		
		return "	{"+
		      	"\n		title: \""+data[0]+"\","+
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
		Random randGer = new Random();
		
		int[][]gerados = new int[3][5];
		
		for(int i = 0; i < 3; i++) {
			
			for(int j = 0; j < 5; j++) {
				
				int num = rand.nextInt(max);
				
				if(num == pos) {
					gerou=true;
				}
				
				gerados[i][j]=num;
			}
			
			int idx = randGer.nextInt(5);
			gerados[i][idx]=pos;
			gerou = false;
			
		}
		
		for(int i = 0; i < 3; i++) {
			
			for(int j = 0; j < 5; j++) {
				
				if(j == 0) {
					line.append("[").append(gerados[i][j]);
				}	else {
					line.append(",").append(gerados[i][j]);
				}			
			}
			
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
