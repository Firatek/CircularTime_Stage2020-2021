package gui;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ICC_ColorSpace;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import javax.naming.NamingEnumeration;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.mysql.jdbc.Driver;

import bdd.Database;

public class CircularTimeLayer{
	private static final double GROSSISSEMENT = 3.0;
	private static final int NB_ITEMS = 12;
	private static final int GAP = 1;
	Ellipse2D.Double max, min;
	private MyButton myButton;
	private Database db;
	Area area;
	Random r;
	
	State state;
	CircularTimeItem currentItem;


	ArrayList<CircularTimeItem> items;
	Connection conn;
	int rank = 0;
	
	public CircularTimeLayer(Ellipse2D.Double max, Ellipse2D.Double min, int rank, MyButton myButton){
		this.max = max;
		this.min = min;
		this.rank = rank;
		this.myButton = myButton;
		
		this.db = new Database();
		try {
			Class.forName("com.mysql.jdbc.Driver"); //Important
			conn = DriverManager.getConnection(Database.CONN,Database.USER_PS, Database.USER_PS);
		} catch (SQLException | ClassNotFoundException  e) {
			e.printStackTrace();
		}
		state = State.NEUTRE;
		area = new Area(max);
		area.subtract(new Area(min));
		items = new ArrayList<>();
		currentItem = null;
		r = new Random();
		


	}
	private String createQueryField(String field) {
		return field+"(Horaire) = ";
	}
	
	public String makeCond(int rankSelected, int formatLayer, int month) {
		String cond = "";
		switch(formatLayer) {
			case Calendar.DAY_OF_MONTH :
				cond = createQueryField("MONTH");
				cond = cond.concat(String.valueOf(month));
				
				break;
				
			case Calendar.HOUR :
				cond = createQueryField("MONTH") + month + " AND "+  createQueryField("DAY");
				cond = cond.concat(String.valueOf(rankSelected + 1));
				break;
				
			case Calendar.MONTH :
			default :
				break;
				
		}
		
		return cond;
	}
	
	public void createItems(int nbItems, int rankSelected, int formatLayer, int month) {
		this.items.clear();
		double deb = 90;
		double angle = (double)(360-nbItems*GAP)/nbItems;
		String cond = makeCond(rankSelected, formatLayer, month);
		List<Double> valArray = db.createValArray(myButton.getDateStart(),myButton.getDateEnd(), this.conn, formatLayer, cond);
		for(int i=0;i<nbItems;i++){
			double val = Double.NEGATIVE_INFINITY;	
			if(i < valArray.size()) 
				val = valArray.get(i);
			items.add(new CircularTimeItem(max, min, deb, -angle, val, i, formatLayer));
			myButton.incrementMonth(myButton.getDateStart());
			deb = deb-angle-GAP;
		}
	}
	
	
	
	public void focus(){
		max.x -= GROSSISSEMENT;
		max.y -= GROSSISSEMENT;
		max.width += GROSSISSEMENT*2;
		max.height += GROSSISSEMENT*2;
		
		min.x += GROSSISSEMENT;
		min.y += GROSSISSEMENT;
		min.width -= GROSSISSEMENT*2;
		min.height -= GROSSISSEMENT*2;
		
		area = new Area(max);
		area.subtract(new Area(min));
	}
	
	public void normal(){
		max.x += GROSSISSEMENT;
		max.y += GROSSISSEMENT;
		max.width -= GROSSISSEMENT*2;
		max.height -= GROSSISSEMENT*2;
		
		min.x -= GROSSISSEMENT;
		min.y -= GROSSISSEMENT;
		min.width += GROSSISSEMENT*2;
		min.height += GROSSISSEMENT*2;
		
		area = new Area(max);
		area.subtract(new Area(min));
	}
	
	public void paint(Graphics2D g2){
		for(CircularTimeItem item : items)
			if(item.state == State.NEUTRE)
				item.paint(g2);
		
		if(state==State.SURVOL){
			g2.setColor(new Color(200,200,200,150));
			g2.fill(area);
		}
		
		if(currentItem != null)
			currentItem.paint(g2);
	}


	public State layerState(MouseEvent arg0){
		currentItem = null;
		for(CircularTimeItem item : items)
			if(item.changeState(arg0)!=State.NEUTRE)
				currentItem = item;
		
		switch (state) {
		case NEUTRE:
			if(area.contains(arg0.getPoint())){
				state = State.SURVOL;	
				focus();
			}
			break;
		case SELECT:
			break;
		case SURVOL:
			if(!area.contains(arg0.getPoint())){
				state = State.NEUTRE;
				normal();
			}
			break;
		default:
			break;
		}	
		return state;
			
	}
}
