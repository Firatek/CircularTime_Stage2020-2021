package gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import bdd.Database;

public class MyButton extends JButton {
	
	private int yearStart;
	private int yearEnd;
	private  Date dateStart;
	private  Date dateEnd;

	

	public MyButton(MyTimeChooser timeChooser, DecimalFormat deciFormat, JFrame root) {
		super("Valider");
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getMinimumSize().height));
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String textEnd = timeChooser.getEnd().getText();
				String textStart = timeChooser.getStart().getText();
				
				try {
					//Check si la valeur est négative
					yearStart = deciFormat.parse(textStart).intValue();
					yearEnd = deciFormat.parse(textEnd).intValue();
					
					dateStart = createDate(yearStart);
					dateEnd = createDate(yearEnd);
	
	
					if(yearStart > 0 && yearEnd > 0 && yearEnd >= yearStart) {
						int nbLayers = yearEnd - yearStart + 1;
						changeDiagram(root, nbLayers, 12, Calendar.MONTH, Database.NO_SELECT);
					}else 
		                JOptionPane.showMessageDialog (null, "Veuillez rentrez des dates valides", "Error", JOptionPane.ERROR_MESSAGE);
					
					//Débug 
					//System.out.println("Values start :" + valueStart +" End : " +valueEnd);
				} catch (ParseException e) {
					e.printStackTrace();
				}

									
			}
		});	
		}
	
	public  void changeDiagram(JFrame root, int nbLayers, int nbItems, int formatLayer, int rankSelected) {
		Component tab[] = root.getContentPane().getComponents();
		for(int i = 0; i < tab.length; ++i) {
			if(tab[i] instanceof CircularTimePanel) {
				CircularTimePanel panel = (CircularTimePanel) tab[i];
				panel.setFormatLayer(formatLayer);
				panel.fillPanel(nbLayers);
				for(CircularTimeLayer layer : panel.layers) {
					layer.createItems(nbItems, rankSelected, formatLayer, 0);
				}
				panel.repaint();
			}
		}
	}
	
	public int getYearStart() {
		return yearStart;
	}



	public int getYearEnd() {
		return yearEnd;
	}



	public Date getDateStart() {
		return dateStart;
	}



	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}



	public Date getDateEnd() {
		return dateEnd;
	}



	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}



	public static Date createDate(int year) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.clear();
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.YEAR,year);
		return calendar.getTime();
	}
	
	public  Date incrementMonth(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}
	
	


}
