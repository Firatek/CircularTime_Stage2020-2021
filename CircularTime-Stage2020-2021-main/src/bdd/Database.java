package bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import com.mysql.jdbc.SingleByteCharsetConverter;

import gui.CircularTimeGUI;
import gui.CircularTimeItem;
import gui.CircularTimeLayer;
import gui.MyButton;


public class Database {
	public static final String TABLE_NAME = "elec4Mois";
	public static final String CONN = "jdbc:mysql://localhost:3306/donneeStage";
	public static final String USER_PS = "root";
	public static final int NO_SELECT = -1;
	
	private int getIndexFormatLayer(int formatLayer, Calendar cal, ArrayList<Integer> knownHour) {
		int ret = cal.get(formatLayer);
		switch(formatLayer) {
			case Calendar.DAY_OF_MONTH:
				ret--;
				break;
			case Calendar.HOUR :
				Integer val = Integer.valueOf(ret);
				if(knownHour != null) {
					
					//Condition for pm
					if(knownHour.contains(val))
						ret +=12;
					
					knownHour.add(val);
				}
				break;

			case Calendar.MONTH:
			default :
				break;
		}
		//System.out.println(ret+ " "+ cal.getTime());
		return ret;
		
	}
	
	private Double initSum(Double oldIndex, Double newIndex, int formatLayer) throws SQLException {
		if(formatLayer == Calendar.HOUR && oldIndex != 0 && newIndex != 0 ) 
				return Math.abs(newIndex - oldIndex);
		return 0.0;
	}
	
	
	private  ArrayList<Double> computeRs(ResultSet rs, int formatLayer) throws SQLException {
		//TODO : changer taille collections
		ArrayList<Double> valArray = new ArrayList<>(Collections.nCopies(60, Double.NEGATIVE_INFINITY));
		ArrayList<Integer> knownHour = (formatLayer == Calendar.HOUR) ? new ArrayList<>() : null;
		Date oldDate = null;
		Double oldIndex = 0.0;

		if(rs.next()) {
			oldDate = rs.getTimestamp(1);
			oldIndex = rs.getDouble(2);

		}
		while(rs.next()) {
			
			Double length = 1.0;
			Date newDate = rs.getTimestamp(1);
			Double newIndex = rs.getDouble(2);
			Double sum = initSum(oldIndex, newIndex, formatLayer);
			GregorianCalendar cal1 = new GregorianCalendar();
			GregorianCalendar cal2 = new GregorianCalendar();
		    cal1.setTime(oldDate);
		    cal2.setTime(newDate);
		    
			while(cal1.get(formatLayer) ==  cal2.get(formatLayer) && rs.next()){
				if(oldIndex != 0 && newIndex != 0)
					sum = sum + Math.abs(newIndex - oldIndex);
				length++;

				oldDate = newDate;
				oldIndex = newIndex;
				newDate = rs.getTimestamp(1);
				newIndex = rs.getDouble(2);
			    cal1.setTime(oldDate);
			    cal2.setTime(newDate);


			}
			
			oldDate = newDate;
			oldIndex = newIndex;
			
			Double mean = (sum / length);
			
			int indexArray = getIndexFormatLayer(formatLayer, cal1, knownHour);
			
			valArray.add(indexArray ,mean); //cause cal 1---31
			
	}
		return valArray;
	}

	
	public  List<Double> createValArray( Date start, Date end, Connection conToDb, int formatLayer, String cond) {
		Statement stmt = null ;
		ArrayList<Double> ret = null;
		try {
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			stmt = conToDb.createStatement();
			String startFormated = formater.format(start);
			String endFormated = formater.format(end);
			String query = (cond.equals("")) ? "select * from elec4Mois WHERE  Horaire BETWEEN '"+startFormated+"' AND '"+endFormated+"';" :
				"select * from elec4Mois WHERE ( Horaire BETWEEN '"+startFormated+"' AND '"+endFormated+ "') AND " +cond +" ;";

			ResultSet rs = stmt.executeQuery(query);
			ret = computeRs(rs, formatLayer);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;

	}
	

}