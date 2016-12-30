/**
* @license: 
* The MIT License (MIT)
*
* Copyright (c) 2016 David Okusanya 
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
* THE SOFTWARE.
* 
* @brief: Drawing a Pythagorean tree
*
*/

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Arrays;

class PythagoreanTree extends Frame{

	public String drawModeOne = "DFS", drawModeTwo = "BFS"; 
	private Button b;
	private int limit_DFS, limit_BFS;


	PythagoreanTree(int limit_DFS, int limit_BFS){
		super("PythagoreanTree");
		this.limit_DFS = limit_DFS;
		this.limit_BFS = limit_BFS;
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});

		setLayout(new BorderLayout());

		PythDisplay display = new PythDisplay(drawModeOne, limit_DFS);
		PythDisplay new_display = new PythDisplay(drawModeTwo, limit_BFS);

		add("Center", display);

		b = new Button("Click to switch to Queue(BFS)");
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (b.getLabel().equalsIgnoreCase("Click to switch to Queue(BFS)")){
					b.setLabel("Click to switch to Stack(DFS)");
					PythagoreanTree.this.remove(display);
					PythagoreanTree.this.revalidate();
					PythagoreanTree.this.repaint();
					PythagoreanTree.this.add("Center", new_display);
					PythagoreanTree.this.revalidate();
					PythagoreanTree.this.repaint();
				} else if (b.getLabel().equalsIgnoreCase("Click to switch to Stack(DFS)")){
					b.setLabel("Click to switch to Queue(BFS)");
					PythagoreanTree.this.remove(new_display);
					PythagoreanTree.this.revalidate();
					PythagoreanTree.this.repaint();
					PythagoreanTree.this.add("Center", display);
					PythagoreanTree.this.revalidate();
					PythagoreanTree.this.repaint();
				}
			}
		});
		setSize(600, 400);
		add("North", b);
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setVisible(true);
	}

	public static void main(String[] args){
		if (args.length != 2){
			System.out.println("Usage: java PythagoreanTree <limit-DFS> <limit-BFS>. The limits must be integers");
			System.exit(1);
		}

		int limit_DFS = Integer.parseInt(args[0]);
		int limit_BFS = Integer.parseInt(args[1]);

		if (limit_DFS < 3 || limit_DFS > 10)
			limit_DFS = 3;
		if (limit_BFS < 3 || limit_BFS > 10)
			limit_BFS = 3;  
		new PythagoreanTree(limit_DFS, limit_BFS);
	}
	
}


class PythDisplay extends Canvas{

	private String displayMode;
	private int limit;
	private ArrayList<Point<Integer>> arr = new ArrayList<>();
	private ArrayList<Point<Integer>> new_arr = new ArrayList<>();

	PythDisplay(String displayMode, int limit){
		this.displayMode = displayMode;
		this.limit = limit;
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evt){
				int xCoord = evt.getX();
				int yCoord = evt.getY();
				if (displayMode.equalsIgnoreCase("DFS")){
					arr.add(new Point<Integer>(xCoord, yCoord));
					System.out.println(arr.get(0).x + " " + arr.get(0).y);
				} else if (displayMode.equalsIgnoreCase("BFS")){
					new_arr.add(new Point<Integer>(xCoord, yCoord));
					System.out.println(new_arr.get(0).x + " " + new_arr.get(0).y);
				}
				repaint();
			}
		});
	}

	
	public boolean hasALine(ArrayList<Point<Integer>> arr){
		return (arr.size() >= 2 ? true : false); 
	}

	// firstPoint = A, secondPoint = B, thirdPoint = C, fourthPoint = D
	private ArrayList<Point<Integer>> computeSquarePoints(Point<Integer> firstPoint, Point<Integer> secondPoint){

		int xCoord = secondPoint.y - firstPoint.y;
		int yCoord = firstPoint.x - secondPoint.x;
		int reverse_yCoord = secondPoint.x - firstPoint.x; 

		Point<Integer> fourthPoint = new Point<Integer>(firstPoint.x + xCoord, firstPoint.y + yCoord);
		Point<Integer> thirdPoint = new Point<Integer>(fourthPoint.x+ reverse_yCoord, fourthPoint.y + xCoord); 

		return new ArrayList<Point<Integer>>(Arrays.asList(fourthPoint, thirdPoint));
	}

	
	private Point<Integer> computeVertex(Point<Integer> firstPoint, Point<Integer> secondPoint){
		
		int xCoord = Math.round( ((float)(secondPoint.y - firstPoint.y) / 2) + ((float)(secondPoint.x - firstPoint.x) / 2) );
		int yCoord = Math.round( ((float)(firstPoint.x - secondPoint.x) / 2) + ((float)(secondPoint.y - firstPoint.y) / 2) );
		return new Point<Integer>(firstPoint.x + xCoord, firstPoint.y + yCoord);   
	}


	private int getDistance(Point<Integer> firstPoint, Point<Integer> secondPoint){
		return Math.abs(Math.round((float)Math.sqrt(Utils.getDistance(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y)))); 
	}

	private void drawPentagon(Graphics g, Point<Integer> firstPoint, Point<Integer> secondPoint, ArrayList<Point<Integer>> draw_arr, Point<Integer> triangleVertex){

		// Draw
		// Point A to B
		g.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
		// Point B to C
		g.drawLine(secondPoint.x, secondPoint.y, draw_arr.get(1).x, draw_arr.get(1).y);
		// Point C to D
		g.drawLine(draw_arr.get(1).x, draw_arr.get(1).y, draw_arr.get(0).x, draw_arr.get(0).y);

		Polygon rect = new Polygon();
		rect.addPoint(firstPoint.x, firstPoint.y);
		rect.addPoint(secondPoint.x, secondPoint.y);
		rect.addPoint(draw_arr.get(1).x, draw_arr.get(1).y);
		rect.addPoint(draw_arr.get(0).x, draw_arr.get(0).y);

		g.setColor(Utils.generateRandomColour());
		g.fillPolygon(rect);
		g.setColor(Color.BLACK);

		try{
			Thread.sleep(500);
		} catch(InterruptedException ex){
			ex.printStackTrace();
		}

		g.drawLine(draw_arr.get(0).x, draw_arr.get(0).y, firstPoint.x, firstPoint.y);
		g.drawLine(draw_arr.get(1).x, draw_arr.get(1).y, triangleVertex.x, triangleVertex.y);
		g.drawLine(draw_arr.get(0).x, draw_arr.get(0).y, triangleVertex.x, triangleVertex.y);

		Polygon triangle = new Polygon();
		triangle.addPoint(draw_arr.get(0).x, draw_arr.get(0).y);
		triangle.addPoint(triangleVertex.x, triangleVertex.y);
		triangle.addPoint(draw_arr.get(1).x, draw_arr.get(1).y);

		g.setColor(Utils.generateRandomColour());
		g.fillPolygon(triangle);
		g.setColor(Color.BLACK);		
	}
	
	public void drawTree(Graphics g, Point<Integer> firstPoint, Point<Integer> secondPoint, int limit){

		if (limit < getDistance(firstPoint, secondPoint)){
			System.out.println(firstPoint.x + " " + firstPoint.y + "  " + secondPoint.x + " " + secondPoint.y 
					+ "    Distance" + getDistance(firstPoint, secondPoint));
			ArrayList<Point<Integer>> draw_arr = computeSquarePoints(firstPoint, secondPoint);
			Point<Integer> triangleVertex = computeVertex(draw_arr.get(0), draw_arr.get(1));
			drawPentagon(g, firstPoint, secondPoint, draw_arr, triangleVertex);
			drawTree(g, triangleVertex, draw_arr.get(1), limit);
			drawTree(g, draw_arr.get(0), triangleVertex, limit);
		}
	
	}

	
	public void drawTreeIter(Graphics g, Point<Integer> firstPoint, Point<Integer> secondPoint, int limit){

		Queue lines = new LinkedList<>();
		lines.add(new ArrayList<Point<Integer>>(Arrays.asList(firstPoint, secondPoint)));
		while(limit < getDistance(((ArrayList<Point<Integer>>)lines.peek()).get(0), ((ArrayList<Point<Integer>>)lines.peek()).get(1))){
			ArrayList<Point<Integer>> li = (ArrayList<Point<Integer>>)lines.poll();
			System.out.println(li.get(0).x + " " + li.get(0).y + "  " + 
					li.get(1).x + " " + li.get(1).y 
					+ "    Distance" + getDistance(li.get(0), li.get(1)));
			ArrayList<Point<Integer>> draw_arr = computeSquarePoints(li.get(0), li.get(1));
			Point<Integer> triangleVertex = computeVertex(draw_arr.get(0), draw_arr.get(1));
			drawPentagon(g, li.get(0), li.get(1), draw_arr, triangleVertex);
			lines.add(new ArrayList<Point<Integer>>(Arrays.asList(triangleVertex, draw_arr.get(1))));
			lines.add(new ArrayList<Point<Integer>>(Arrays.asList(draw_arr.get(0), triangleVertex)));
		}
	}
	

	public void paint(Graphics g){

		// Setup code
		Font c = new Font("Courier", Font.PLAIN, 18);
		g.setFont(c);
		String prompt = "Click two points on the canvas";
		g.drawString(prompt, 20, 40);
		
		if (displayMode.equalsIgnoreCase("DFS")){ 
			if (hasALine(arr) == false) return;
			drawTree(g, arr.get(0), arr.get(1), limit);
		} else if (displayMode.equalsIgnoreCase("BFS")){
			if (hasALine(new_arr) == false) return;
			drawTreeIter(g, new_arr.get(0), new_arr.get(1), limit);
		}

	}
	
}