package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class TextPanel extends Panel {
	int format;
	int sizeLayer;
	CircularTimePanel own;
    public TextPanel(int format, CircularTimePanel own) {
    	this.own = own;
    	this.format = format;
    	setSizeLayer(format);
        setVisible(true);
    }
    
    
	public void setSizeLayer(int formatLayer) {
		int ret = 12;
		switch(formatLayer) {
			case Calendar.DAY_OF_MONTH:
				Calendar mycal = new GregorianCalendar(own.getYear(), own.getMonth() - 1, 1);
				ret = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
				this.format = Calendar.DAY_OF_MONTH;
				break;
			case Calendar.HOUR :
				ret = 24;
				this.format = Calendar.HOUR;
				break;
	
			case Calendar.MONTH:
				ret = 12;
				this.format = Calendar.MONTH;
				break;
			default :
				break;
		}
		this.sizeLayer = ret;
		
	}
    
    public void drawStringCenteredAtAngle(Graphics2D g2d, double x1, double y1, double length, double angle, String txt) {
    	FontMetrics fm = g2d.getFontMetrics();
    	g2d.setColor(Color.BLACK);
    	Font f = g2d.getFont();
    	int x = (int) (x1 + length * Math.cos(Math.toRadians(angle)  ));
    	int y = (int)(y1 + length * Math.sin(Math.toRadians(angle))) ;
        g2d.drawString(txt, 
        		(int) (x), 
        		(int) (y + fm.getAscent()));
        g2d.setColor(Color.RED);
        g2d.drawOval(x, y, 1, 1);

    }
    
    public void drawCurvedStringCenteredAtAngle(Graphics2D g2d, double x1, double y1, double length, double angle, String txt) {
    	FontMetrics fm = g2d.getFontMetrics();
    	g2d.setColor(Color.BLACK);
    	Font f = g2d.getFont();
    	FontRenderContext frc = g2d.getFontRenderContext();
    	int x = (int) (x1 + length * Math.cos(Math.toRadians(angle)  ));
    	int y = (int)(y1 + length * Math.sin(Math.toRadians(angle))) ;
    	g2d.translate(x, y + fm.getAscent());
    	
    	GlyphVector gv = f.createGlyphVector(frc, txt);
    	for(int i = 0; i < gv.getNumGlyphs(); ++i) {
    		Point2D p = gv.getGlyphPosition(i);
    		double theta = (double) i / (double) (gv.getNumGlyphs() -1) * Math.PI / 5;
    		AffineTransform at = AffineTransform.getTranslateInstance(p.getX(), p.getY());
    		at.rotate(theta);
    		Shape glyph = gv.getGlyphOutline(i);
    		Shape transformedGlyph = at.createTransformedShape(glyph);
    		g2d.fill(transformedGlyph);
    	}
    	

    }
    
    
    public static void drawLineAtAngle(Graphics2D g2d, double x1,
            double y1, double length, double angle) {
        g2d.drawLine((int) x1, (int) y1,
                (int) (x1 + length * Math.cos(Math.toRadians(angle))),
                (int) (y1 + length * Math.sin(Math.toRadians(angle))));
    }
    
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    /*
     * NOT WORKING CURRENTLY
     * public void paint(Graphics g){    
        Font f = new Font("Verdana Ref",Font.BOLD,15);
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.setFont(f);
    	for(int i = 0 ; i < sizeLayer + 1; i++) {
    		double sub = 360.0/sizeLayer;
    		double angle = 0.0;
    		double start = 0.0;
    		start = (sizeLayer / 4.0);
    		
    		if(format == Calendar.MONTH)
    			start--;
    		
    		start*=sub;
			angle = -(sub/2.0 + start) + sub*i;
    		String txt = "";
    		if(format == Calendar.MONTH) {
        		 txt = getMonth(i+1);
    		}else if(i!=0) {
    			txt = String.valueOf(i);
    		}
        	drawStringCenteredAtAngle(g2d, CircularTimePanel.WIDTH/2.0, CircularTimePanel.HEIGHT/2.0, CircularTimePanel.HEIGHT/2.0 , angle, txt);
        	drawCurvedStringCenteredAtAngle(g2d, CircularTimePanel.WIDTH/2.0, CircularTimePanel.HEIGHT/2.0, (CircularTimePanel.HEIGHT)/2.0 , angle, txt);

    	}**/

    	
    }

