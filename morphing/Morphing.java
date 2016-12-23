import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Map;
import javax.swing.*;

public class Morphing extends Frame{

	public JLabel statusLabel = new JLabel();

	public static void main(String[] args) {
		new Morphing();
	}

	Morphing() {
		super("Polygon Morphing");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(700, 600);
		setResizable(false);
		add("North", statusLabel);
		add("Center", new PolygonMorph(statusLabel));
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setVisible(true);
	}
}

class PolygonMorph extends Canvas{
	private Point<Integer> sourcePolyCenter;
	private Point<Integer> targetPolyCenter;

	private boolean isSourcePolyComplete = false;
	private boolean isTargetPolyComplete = false;
	
	private boolean morphBegin = false, hasTranslated = false;

	private ArrayList<Point<Integer>> sourcePolyPoints = new ArrayList<>();
	private ArrayList<Point<Integer>> targetPolyPoints = new ArrayList<>();
	

	//private ArrayList<Line2D> sourcePolyLines = new ArrayList<>();
	//private ArrayList<Line2D> targetPolyLines = new ArrayList<>();

	private int polyOrientation;

	private Point<Integer> currentPoint;

	private JLabel status;

	PolygonMorph(JLabel statusLabel){
		super();
		status = statusLabel;
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent mt){
				int x_coord = mt.getX();
				int y_coord = mt.getY();

				Point<Integer> firstPoint = null;
				currentPoint = new Point<Integer>(x_coord, y_coord);

				if (isSourcePolyComplete == false && isTargetPolyComplete == false) {

					if (sourcePolyCenter == null) {
						sourcePolyCenter = currentPoint;
					} else if (sourcePolyPoints.size() < 2) {
						sourcePolyPoints.add(currentPoint);
						if (sourcePolyPoints.size() == 2) {
							firstPoint = sourcePolyPoints.get(0);
							if (Utils.getOrientation(firstPoint.x, firstPoint.y, currentPoint.x, currentPoint.y, sourcePolyCenter.x, sourcePolyCenter.y) != 0){ 
								polyOrientation = Utils.getOrientation(firstPoint.x, firstPoint.y, currentPoint.x, currentPoint.y, sourcePolyCenter.x, sourcePolyCenter.y); 
							} else if (Utils.getOrientation(firstPoint.x, firstPoint.y, currentPoint.x, currentPoint.y, sourcePolyCenter.x, sourcePolyCenter.y) == 0){
								sourcePolyPoints.remove(targetPolyPoints.size() - 1);	// enforces the rule that three points can not be on a line
							}
						}
					} else if (sourcePolyPoints.size() >= 2) {

						Point<Integer> previousPoint = sourcePolyPoints.get(sourcePolyPoints.size() - 1);
						int currentOrientation = 4;
						if (Utils.getOrientation(previousPoint.x, previousPoint.y, currentPoint.x, currentPoint.y, sourcePolyCenter.x, sourcePolyCenter.y) != 0){ 
							currentOrientation = Utils.getOrientation(previousPoint.x, previousPoint.y, currentPoint.x, currentPoint.y, sourcePolyCenter.x, sourcePolyCenter.y); 
						}
						if (currentOrientation == polyOrientation) {
							firstPoint = sourcePolyPoints.get(0);
							float distance = (float) Math.sqrt(Utils.getDistance(firstPoint.x, firstPoint.y, currentPoint.x, currentPoint.y));
							if (distance > 3.30F) {
								sourcePolyPoints.add(currentPoint);
							} else {
								isSourcePolyComplete = true;
							}
						} else {
							System.out.println("Ignore the point clicked");
						}
					}
				} else if (isSourcePolyComplete == true && isTargetPolyComplete == false) {

					if (targetPolyCenter == null) {
						targetPolyCenter = currentPoint;
					} else if (targetPolyPoints.size() < 2) {
						targetPolyPoints.add(currentPoint);
						if (targetPolyPoints.size() == 2) {
							firstPoint = targetPolyPoints.get(0);
							if (polyOrientation != Utils.getOrientation(firstPoint.x, firstPoint.y, currentPoint.x, currentPoint.y, targetPolyCenter.x, targetPolyCenter.y)){ 
								targetPolyPoints.remove(targetPolyPoints.size() - 1); // forces the two polygons to have the same orientation 
							}
						}
					} else if (targetPolyPoints.size() >= 2) {
						Point<Integer> previousPoint = targetPolyPoints.get(targetPolyPoints.size() - 1);
						int currentOrientation = Utils.getOrientation(previousPoint.x, previousPoint.y, currentPoint.x, currentPoint.y, targetPolyCenter.x, targetPolyCenter.y); 
						if (currentOrientation == polyOrientation) {
							firstPoint = targetPolyPoints.get(0);
							float distance = (float) Math.sqrt(Utils.getDistance(firstPoint.x, firstPoint.y, currentPoint.x, currentPoint.y));
							if (distance > 3.30F) {
								targetPolyPoints.add(currentPoint);
							} else {
								isTargetPolyComplete = true;
							}
						} else {
							System.out.println("Ignore the point clicked");
						}
					}
				} else if (morphBegin == false && isSourcePolyComplete == true && isTargetPolyComplete == true){

				}
				repaint();
			}
		});
	}


	private void drawUserGuideLines(Graphics g, ArrayList<Point<Integer>> polygon, boolean isPolygonComplete, Point<Integer> point){
		if (isPolygonComplete == false){
			Point<Integer> lastPoint = polygon.get(polygon.size() - 1);
			Dimension d = getSize();

			int orientation = Utils.getOrientation(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y, point.x, point.y);
			if (orientation != -1 && currentPoint != lastPoint){ // We are testing for a clockwiswe direction as Java is inverse of the calculation
				g.drawString("It is illegal to place your point there!!!", d.width - 290, d.height - 30);
			}

			Point<Float> unitVector = Utils.getUnitVector(point.x, point.y, lastPoint.x, lastPoint.y);

			// To make the line drawn arbitarily long
			int scaleX = Math.round(1000 * unitVector.x);
            int scaleY = Math.round(1000 * unitVector.y);

            g.setColor(Color.RED);
            g.drawLine(point.x, point.y, point.x + scaleX, point.y + scaleY);
            g.drawLine(point.x, point.y, point.x - scaleX, point.y - scaleY);
		}
	}

	private void drawUserPolygon(Graphics g, ArrayList<Point<Integer>> polygon, boolean isPolygonComplete, Color color){
		g.setColor(color);
		Point<Integer> initialPoint = polygon.get(0);
		Utils.markPoint(g, initialPoint, 5);
		if (polygon.size() > 1){
			for (int i = 1; i < polygon.size(); i++){
				Point<Integer> lastPoint = polygon.get(i % polygon.size());
				g.drawLine(initialPoint.x, initialPoint.y, lastPoint.x, lastPoint.y);
				initialPoint = lastPoint;
			}
			if (isPolygonComplete == true){
				Point<Integer> firstVertex = polygon.get(0);
				Point<Integer> lastVertex = polygon.get(polygon.size() - 1);
				g.drawLine(firstVertex.x, firstVertex.y, lastVertex.x, lastVertex.y);
			}					
		}
		g.setColor(Color.black);
	}

	public void paint(Graphics g){
		status.setText("Define your polygons by clicking on the canvas");

		if (sourcePolyCenter != null){
			Utils.markPoint(g, sourcePolyCenter, 2);
		}

		// Draw sourcePolygon
		if (sourcePolyPoints.isEmpty()) return;
		drawUserPolygon(g, sourcePolyPoints, isSourcePolyComplete, Color.magenta);
		drawUserGuideLines(g, sourcePolyPoints, isSourcePolyComplete, sourcePolyCenter);

		if (targetPolyCenter != null){
			Utils.markPoint(g, targetPolyCenter, 2);
		}

		// Draw targetPolygon
		if (targetPolyPoints.isEmpty()) return;
		if (hasTranslated == false){
			drawUserPolygon(g, targetPolyPoints, isTargetPolyComplete, Color.magenta);
		}
		drawUserGuideLines(g, targetPolyPoints, isTargetPolyComplete, targetPolyCenter);

		if (isSourcePolyComplete == true && isTargetPolyComplete == true){
			status.setText("Polygons drawn");
			// Translate the targetPolygon so that the centers of the two polygons will align
			if (hasTranslated == false){
				int xOffset = targetPolyCenter.x - sourcePolyCenter.x;
				int yOffset = targetPolyCenter.y - sourcePolyCenter.y;

				Transformation transform = new Transformation("translate", "inverse", 0, xOffset, yOffset);

				for (int i = 0; i < targetPolyPoints.size(); i++){
					Point<Integer> point = Utils.translate(transform, targetPolyPoints.get(i));
					targetPolyPoints.set(i, point);
				}

				targetPolyCenter = Utils.translate(transform, targetPolyCenter);
				hasTranslated = true;
				repaint();
			}

			drawUserPolygon(g, targetPolyPoints, isTargetPolyComplete, Color.blue);
			status.setText("Target polyon has translated and is shown in blue");

			// Sort the points in the polygon
			Point<Integer> refpoint = new Point<Integer>(sourcePolyCenter.x + 20, sourcePolyCenter.y);

			SortedMap<Double, Point<Integer>> sourceAnglePoints = new TreeMap<>();
			SortedMap<Double, Point<Integer>> targetAnglePoints = new TreeMap<>();

			for (Point<Integer> point: sourcePolyPoints){
				double angle = Utils.getPointAngleWithXAxis(point, refpoint, polyOrientation);
				sourceAnglePoints.put(angle, point);
			}

			for (Point<Integer> point: targetPolyPoints){
				double angle = Utils.getPointAngleWithXAxis(point, refpoint, polyOrientation);
				targetAnglePoints.put(angle, point);
			}			

			ArrayList<Point<Integer>> sortedSourcePolyPoints = new ArrayList<>();
			ArrayList<Point<Integer>> sortedTargetPolyPoints = new ArrayList<>();

			for (Map.Entry<Double, Point<Integer>> entry: sourceAnglePoints.entrySet()){
				sortedSourcePolyPoints.add(entry.getValue());
			}

			for (Map.Entry<Double, Point<Integer>> entry: targetAnglePoints.entrySet()){
				sortedTargetPolyPoints.add(entry.getValue());
			}

			// Find the corresponding points
			CorrPoints corrPoints = new CorrPoints(sortedSourcePolyPoints, 
												   sortedTargetPolyPoints,
												   sourcePolyCenter,
												   targetPolyCenter);



		}

	}
}