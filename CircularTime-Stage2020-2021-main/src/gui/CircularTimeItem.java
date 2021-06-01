package gui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JWindow;
import javax.swing.event.MouseInputListener;

public class CircularTimeItem{
	Area item;
	Double valeur;
	Color c;
	private int rank = 0;
	State state;
	
	public static final Color WHITE_NULL = new Color(255,255,255);
	public static final Color RED = new Color(240,80,80);
	public static final Color ORANGE = new Color(250,180,50);
	public static final Color YELLOW = new Color(240,240,0);
	public static final Color GREEN = new Color(50,250,50);
	public static final Color BLACK = new Color(0,0,0);
	private int formatLayer;
	Arc2D.Double arc;

	
	public CircularTimeItem(Ellipse2D.Double max, Ellipse2D.Double min, double angleMin, double angleMax, double val, int rank, int formatLayer){
		arc = new Arc2D.Double(max.getBounds2D(),angleMin,angleMax,Arc2D.PIE);
		item = new Area(arc);
		item.subtract(new Area(min));
		
		this.valeur = Double.valueOf(val);
		this.rank = rank;
		this.formatLayer = formatLayer;

		if(val == Double.NEGATIVE_INFINITY) {
			this.c = WHITE_NULL;
		}
		else if(val<25)
			this.c = RED;
		else{
			if(val<50)
				this.c = ORANGE;
			else{
				if(val<75)
					this.c = YELLOW;
				else
					this.c = GREEN;
			}
		}
		
		state = State.NEUTRE;
	}
	
	

	public int getRank() {
		return rank;
	}



	public void setValeur(double valeur) {
		this.valeur = valeur;
	}
	

	public double getValeur() {
		return valeur;
	}

	public State changeState(MouseEvent arg0) {
		switch (state) {
		case NEUTRE:
			if(item.contains(arg0.getPoint()))
				state = State.SURVOL;
			break;
		case SELECT:
			break;
		case SURVOL:
			if(!item.contains(arg0.getPoint()))
				state = State.NEUTRE;
			break;
		default:
			break;
		}	
		return state;
	}
	
	

	
	public void paint(Graphics2D g2){
		g2.setColor(c);
		g2.fill(item);
		String valStr = String.format("%.2f", this.valeur);
		if(this.valeur == Double.NEGATIVE_INFINITY)
			valStr = "Inconnu";
		afficheTexteCenter(g2, valStr);
		switch (state) {
		case SELECT:
			g2.setStroke(new BasicStroke(2.0f));
			g2.setColor(Color.BLACK);
			g2.draw(item);
			break;
		case SURVOL:
			g2.setStroke(new BasicStroke(1.0f));
			g2.setColor(Color.BLACK);
			g2.draw(item);
			break;
		default:
			break;
		}
	}
	
	
    
	public void afficheTexteCenter(Graphics2D g2, String texte){
        Font f = new Font("Verdana Ref",Font.BOLD,12);
        
        Rectangle2D box = this.item.getBounds();
        
        FontRenderContext frc = g2.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(texte,frc);
        LineMetrics metrics = f.getLineMetrics(texte,frc);
        float width = (float)bounds.getWidth(); 
        float lineheight = metrics.getHeight(); 
        float ascent = metrics.getAscent();     
        
    
        float x0 = (float)(box.getX() + (box.getWidth() - width)/2);
        float y0 = (float)(box.getY() + (box.getHeight() - lineheight)/2 + ascent);
        g2.setColor(RED);

        g2.setColor(CircularTimeItem.BLACK);
        g2.setFont(f);
        g2.drawString(texte,x0,y0);
	}

}
