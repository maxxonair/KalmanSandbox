package output;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class writeToFile {
	
	private int dimension;
	ArrayList<String> steps;
	
	public writeToFile(int dimension){
		this.dimension=dimension;
		steps = new ArrayList<String>();
	}
	
	public void addLine(double[] dataLine) {
		String step = "";
		if(dataLine.length > dimension || dataLine.length < dimension) {
			// reject
			System.err.println("Filewriter Error: Input Line dimension mismatch.");
		} else {
			for(int ii=0;ii<dimension;ii++) {
				step += String.valueOf(dataLine[ii]) + " ";
			}
			steps.add(step);
		}
	}
	
	public void addLine(double[] dataLine1, double[] dataLine2) {
		String step = "";

			for(int ii=0;ii<dataLine1.length;ii++) {
				step += String.valueOf(dataLine1[ii]) + " ";
			}
			
			for(int ii=0;ii<dataLine2.length;ii++) {
				step += String.valueOf(dataLine2[ii]) + " ";
			}
			
			steps.add(step);

	}
	
	public void addLine(double[] dataLine1, double[] dataLine2, double singleValue) {
		String step = "";

			for(int ii=0;ii<dataLine1.length;ii++) {
				step += String.valueOf(dataLine1[ii]) + " ";
			}
			
			for(int ii=0;ii<dataLine2.length;ii++) {
				step += String.valueOf(dataLine2[ii]) + " ";
			}
			step += String.valueOf(singleValue) + " ";
			steps.add(step);

	}
	
	public void write() {
	    try{
	        String resultpath="";
	        	String dir = System.getProperty("user.dir");
	        	resultpath = dir + "/results.txt";
	        PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
	        for(String step: steps) {
	            writer.println(step);
	        }
	        System.out.println("Writing: Result file completed."); 
	        System.out.println("------------------------------------------");
	        writer.close();
	    } catch(Exception e) {System.out.println("ERROR: Writing result file failed");System.out.println(e);};
	}

}
