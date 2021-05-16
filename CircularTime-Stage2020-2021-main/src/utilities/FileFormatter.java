package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import gui.CircularTimeGUI;

public class FileFormatter {

	
	public static void main(String[] args){
		final String oldFormat = "dd/MM/yyyy hh:mm:ss";
		final String newFormat = "yyyy-MM-dd hh:mm:ss";
		List<String> lines = new ArrayList<>();
		String line = null;		
		 try {
				File myFile = new File("elec4Mois.csv");
	            FileReader fr = new FileReader(myFile);
	            BufferedReader br = new BufferedReader(fr);
	            while ((line = br.readLine()) != null) {
	            	if(!line.contains("Horaire")) {
		            	String[] splittedLine = line.split("[;]");
		            	SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
		            	Date date = sdf.parse(splittedLine[0]);
		            	sdf.applyPattern(newFormat);
		            	String newDateString = sdf.format(date);
		            	line = line.replace(splittedLine[0], newDateString);
	            	}
	            	line = line.concat("\n");
	                lines.add(line);
	            }
	            fr.close();
	            br.close();
	            FileWriter fw = new FileWriter(myFile);
	            BufferedWriter out = new BufferedWriter(fw);
	            for(String s : lines)
	                 out.write(s);
	            out.flush();
	            out.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        } finally {
	        	lines.clear();
	        }
	    }

	}
		
		
			
		
