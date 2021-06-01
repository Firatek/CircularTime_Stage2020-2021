package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.awt.geom.Area;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.spi.CalendarNameProvider;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import bdd.Database;

public class CircularTimePanel extends JLayeredPane implements MouseInputListener{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	public static final int BORDER = 20;
	private static final int GAP = 3;
	private int sizeCompo= 0;
	
	ArrayList<CircularTimeLayer> layers;
	CircularTimeLayer currentLayer;
	private TextPanel txtPane;

	private int nbLayers;
	private MyButton myButton;
	private int formatLayer;
	private int month = 0;
	private int day = 0;

	public CircularTimePanel(int nbLayers, MyButton myButton, int formatLayer){
		super();
		this.formatLayer = formatLayer;
		this.myButton = myButton;		
		this.txtPane = new TextPanel(formatLayer, this);
		this.nbLayers = nbLayers;
		this.sizeCompo = WIDTH/(3*nbLayers);
	
		
		layers = new ArrayList<CircularTimeLayer>();
		currentLayer = null;
		
		fillPanel(nbLayers);

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		add(txtPane, 1);
		
	}
	
	public int getYear() {
		return myButton.getYearStart();
	}
	
	public int getMonth() {
		return month;
	}



	public int getDay() {
		return day;
	}



	public void setFormatLayer(int formatLayer) {
		this.formatLayer = formatLayer;
		this.txtPane.setSizeLayer(formatLayer);
	}



	public void fillPanel(int nbLayers) {
		layers.clear();
		this.nbLayers = nbLayers;
		this.sizeCompo = WIDTH/(3*nbLayers); //3 Pour garder un "trou" au milieu du diagramme pour une meilleur sélection, jusqu'à une certaine valeur
		int x0 = BORDER;
		int y0 = BORDER; //TODO enlever le +2 et remplacer par la taille de la police 
		int w = WIDTH-2*BORDER;
		int h = HEIGHT-2*BORDER;
		
		for(int i=0;i<nbLayers;i++){
			Ellipse2D.Double max = new Ellipse2D.Double(x0, y0, w, h);
			x0 += sizeCompo;
			y0 += sizeCompo;
			w -= 2*sizeCompo;
			h -= 2*sizeCompo;
			Ellipse2D.Double min = new Ellipse2D.Double(x0, y0, w, h);
			layers.add(new CircularTimeLayer(max, min, i, myButton));
			x0 += GAP;
			y0 += GAP;
			w -= 2*GAP;
			h -= 2*GAP;
		}
	}
	
	private void showFormat(Graphics2D g2) {
		String txt = "";
		GregorianCalendar cal = new GregorianCalendar();
		switch(formatLayer) {
			case Calendar.MONTH :
				txt = "Mois";
				break;
			case Calendar.DAY_OF_MONTH :
				txt = "Jours de ";
				cal.clear();
				cal.set(Calendar.MONTH, month - 1);
				txt = txt.concat(new SimpleDateFormat("MMMMMMMMMMM").format(cal.getTime()));
				break;
			case Calendar.HOUR :
				txt = "Heure du ";
				cal.clear();
				cal.set(Calendar.MONTH, month - 1);
				cal.set(Calendar.DAY_OF_MONTH, day);
				txt = txt.concat(new SimpleDateFormat("dd MMMMMMMMMMM").format(cal.getTime()));
				break;
			default :
				break;
		}
		afficheTexteCenter(g2, txt);
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		showFormat(g2);

		for(CircularTimeLayer layer : layers)
			if(layer.state==State.NEUTRE)
				layer.paint(g2);
		
		if(currentLayer!=null)
			currentLayer.paint(g2);
		
	  	txtPane.paint(g2);
	}
	

	public void afficheTexteCenter(Graphics2D g2, String texte){
        Font f = new Font("Verdana Ref",Font.BOLD,18);
        
        Rectangle2D box = this.getBounds();
        
        FontRenderContext frc = g2.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(texte,frc);
        LineMetrics metrics = f.getLineMetrics(texte,frc);
        float width = (float)bounds.getWidth(); 
        float lineheight = metrics.getHeight(); 
        float ascent = metrics.getAscent();     
    
        float x0 = (float)(box.getX()  + (box.getWidth() - width)/2) - 2 * BORDER;
        float y0 = (float)(box.getY() + (box.getHeight() - lineheight)/2 + ascent) - 30;
        g2.setColor(CircularTimeItem.BLACK);
        g2.setFont(f);
        g2.drawString(texte,x0,y0);
	}
    
	
	//Probleme potentiel quand utilisé dans un autre parcours de liste pensez à break
	public  void changeDiagram(JFrame root, int nbLayers, int nbItems, int formatLayer, int rankSelected) {
		this.formatLayer = formatLayer;
		fillPanel(nbLayers);
		for(CircularTimeLayer layer : this.layers) {
			layer.createItems(nbItems, rankSelected, formatLayer, this.month);
		}
		txtPane.repaint();
		this.repaint();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Point point = arg0.getPoint();
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
		if(currentLayer != null) {
			CircularTimeItem currItem = currentLayer.currentItem;
			Area itemArea = (currItem != null)? currItem.item : null;
			if(itemArea != null && itemArea.contains(point))
				switch(formatLayer) {
					case Calendar.MONTH :
						this.formatLayer = Calendar.DAY_OF_MONTH;
						YearMonth monthSelected = YearMonth.of(myButton.getYearStart() + currentLayer.rank, currItem.getRank() + 1); // + 1 cause calendar 0 to 11
						this.month = currItem.getRank() + 1;
						this.txtPane.setSizeLayer(formatLayer);
						changeDiagram(frame, nbLayers, monthSelected.lengthOfMonth(),formatLayer, currItem.getRank());
						break;
					case Calendar.DAY_OF_MONTH :
						this.formatLayer = Calendar.HOUR;
						this.day = currItem.getRank() + 1;
						this.txtPane.setSizeLayer(formatLayer);
						changeDiagram(frame, nbLayers, 24,formatLayer, currItem.getRank() );
						break;
					case Calendar.HOUR :
						break;
					default :
						break;
				}
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		currentLayer = null;
		for(CircularTimeLayer layer : layers)
			if(layer.layerState(arg0)!=State.NEUTRE) {
				currentLayer = layer;
			}
		repaint();
	}
}
