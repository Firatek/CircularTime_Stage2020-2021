package gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class MyTimeChooser extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7614628253700263121L;
	private PanelDate start;
	private PanelDate end;
	private DecimalFormat deciFormat;
	private MyButton buttonVal;
	private JFrame root;
	
	public MyTimeChooser(JFrame root) {
		super();
		this.root = root;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		initFormat();
		initPanelDate();

		this.add(start);
		this.add(end);
		this.add(buttonVal);
		
        
		this.setMaximumSize(new Dimension(50,50));		
		this.setVisible(true);
		
	}

	
	private void initFormat() {
		//Uniquement des chiffres ou rien
		this.deciFormat = new DecimalFormat("####");
		//Met la taille du chiffre a 4
		deciFormat.setMaximumIntegerDigits(4);
		//Supprime les virgules
		deciFormat.setGroupingUsed(false);
	}
	
	private void initPanelDate() {
		this.start = new PanelDate("Start");
		this.end = new PanelDate("End");
		this.buttonVal = new MyButton(this ,deciFormat, root);
				
	}

	
	public JFormattedTextField getStart() {
		return start.formatField;
	}

	public JFormattedTextField getEnd() {
		return end.formatField;
	}
	
	


	public MyButton getButtonVal() {
		return buttonVal;
	}





	private class PanelDate extends JPanel{
		private JFormattedTextField formatField;

		public PanelDate(String title) {
			super();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			JLabel l = new JLabel(title);
			
			formatField = new JFormattedTextField(deciFormat);
			formatField.setColumns(5);
			formatField.setValue(Calendar.getInstance().get(Calendar.YEAR));
			formatField.setMaximumSize(new Dimension(Integer.MAX_VALUE, formatField.getMinimumSize().height));
			

			this.add(l);
			this.add(formatField);


			
		}
		
	}
	
}
