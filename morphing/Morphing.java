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
		if (args.length != 1){
			System.out.println("Usage: java Morphing <num of stages:Stages should be between 1 and 6>");
			System.exit(1);
		}

		int st = Integer.parseInt(args[0]);
		if (st <= 1 || st >= 6){
			st = 3;
		}
		new Morphing(st);
	}

	Morphing(int stages) {
		super("Polygon Morphing");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(700, 600);
		setResizable(false);
		add("North", statusLabel);
		add("Center", new PolygonMorph(statusLabel, stages));
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

	LinkedHashMap<Point<Integer> , Point<Integer>> corresList =  new LinkedHashMap<>();
	LinkedHashMap<Integer, ArrayList<Point<Integer>>> morphList = new LinkedHashMap();

	private int polyOrientation;

	private Point<Integer> currentPoint;

	private JLabel status;
	private int numOfStages;
	private int counter = 1;

	PolygonMorph(JLabel statusLabel, int numOfStages){
		super();
		status = statusLabel;
		this.numOfStages = numOfStages;
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
				} else if (morphBegin == true){
					if (counter < numOfStages){
						counter++;
					}else if (counter >= numOfStages){
						counter = 1;
					}
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
		if (morphBegin == false){
			drawUserPolygon(g, sourcePolyPoints, isSourcePolyComplete, Color.magenta);
			drawUserGuideLines(g, sourcePolyPoints, isSourcePolyComplete, sourcePolyCenter);
		}

		if (targetPolyCenter != null){
			Utils.markPoint(g, targetPolyCenter, 2);
		}

		// Draw targetPolygon
		if (targetPolyPoints.isEmpty()) return;
		if (hasTranslated == false){
			drawUserPolygon(g, targetPolyPoints, isTargetPolyComplete, Color.magenta);
			drawUserGuideLines(g, targetPolyPoints, isTargetPolyComplete, targetPolyCenter);
		}

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

			System.out.print("Source PolyPoints:");
			System.out.println("\n");

			for (Point<Integer> point: sourcePolyPoints){
				System.out.println("Point: " + point);
			}

			System.out.println("\n");
			System.out.print("Target PolyPoints:");
			System.out.println("\n\n");

			for (Point<Integer> point: targetPolyPoints){
				System.out.println("Point: " + point);
			}	

			System.out.println("PolyOrientation" + polyOrientation);

			// Sort the points in the polygon
			if (morphBegin == false){
				status.setText("Computing corresponding points");
				Point<Integer> refpoint = new Point<Integer>(sourcePolyCenter.x + 20, sourcePolyCenter.y);

				SortedMap<Double, Point<Integer>> sourceAnglePoints = new TreeMap<>();
				SortedMap<Double, Point<Integer>> targetAnglePoints = new TreeMap<>();

				for (Point<Integer> point: sourcePolyPoints){
					double angle = Utils.getAngleBtwVectors(sourcePolyCenter, refpoint, point);
					//double angle = Utils.getAngleFromPoint(point, refpoint);
					sourceAnglePoints.put(angle, point);
				}

				for (Point<Integer> point: targetPolyPoints){
					double angle = Utils.getAngleBtwVectors(sourcePolyCenter, refpoint, point);
					//double angle = Utils.getAngleFromPoint(point, refpoint);
					targetAnglePoints.put(angle, point);
				}			

				// Print sortedPoints
				for (Map.Entry<Double, Point<Integer>> en: sourceAnglePoints.entrySet()){
					System.out.println("Angle: " + en.getKey() + " Point: " + en.getValue());
				}

				System.out.println("\n\n");

				for (Map.Entry<Double, Point<Integer>> en: targetAnglePoints.entrySet()){
					System.out.println("Angle: " + en.getKey() + " Point: " + en.getValue());
				}

				//System.out.println("\n\n");

				/*
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

				corresList = corrPoints.findCorrespondingPoints();
				for (Map.Entry<Point<Integer>, Point<Integer>> en: corresList.entrySet()){
					System.out.println("FirstPoint: " + en.getKey() + " SecondPoint: " + en.getValue());
				}
				//morphList = corrPoints.findMorphingPoints(numOfStages);
				//morphBegin = true;
				*/
			}

			// Draw points and polygons
			if (morphBegin == true){
				status.setText("Drawing lines between corresponding points");
				for (Map.Entry<Point<Integer>, Point<Integer>> entry: corresList.entrySet()){
					Utils.markPoint(g, entry.getKey(), 3);
					Utils.markPoint(g, entry.getValue(), 3);
					g.setColor(new Color(67, 186, 216));
					g.drawLine(entry.getKey().x, entry.getKey().y, entry.getValue().x, entry.getValue().y);  
				}

				status.setText("Morphing the source Polygon");
				g.setColor(Color.BLACK);
				ArrayList<Point<Integer>> morphedPolygon = new ArrayList<>();
				for (ArrayList<Point<Integer>> list: morphList.values()){
					for (Point<Integer> point: list){
						Utils.markPoint(g, point, 3);
					}
					morphedPolygon.add(list.get(counter - 1));
				}
				drawUserPolygon(g, morphedPolygon, true, new Color(153, 107, 196));	// Light purple
				status.setText("Drawing stage " + counter + " of " + numOfStages + " stages");
			}
		}

	}
}