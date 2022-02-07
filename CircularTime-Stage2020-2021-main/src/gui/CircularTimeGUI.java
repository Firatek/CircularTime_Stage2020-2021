package gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import bdd.Database;
import utilities.FileFormatter;

public class CircularTimeGUI{
	private JFrame frame;
	private CircularTimePanel circularTimePanel;
	private MyTimeChooser myTimeChooser;

	public CircularTimeGUI(int nbLayers){
		frame = new JFrame("Circular Time");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(900,900));
		
		

		this.myTimeChooser = new MyTimeChooser(frame);
		MyButton myButton = myTimeChooser.getButtonVal();
		circularTimePanel = new CircularTimePanel(nbLayers, myButton, 0);
		circularTimePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		circularTimePanel.add(myTimeChooser);

		frame.add(circularTimePanel, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	

	public JFrame getFrame() {
		return this.frame;
	}
	
	


	public static void main(String[] args){
		new CircularTimeGUI(1);
	}
}
