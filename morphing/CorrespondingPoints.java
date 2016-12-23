import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Map;

public class CorrespondingPoints{
	private ArrayList<Point<Integer>> sourcePoly, targetPoly;
	private Point<Integer> sourePolyCenter, targetPolyCenter;

	CorrespondingPoints(ArrayList<Point<Integer>> sourcePoly, ArrayList<Point<Integer>> targetPoly, 
						Point<Integer> sourePolyCenter, Point<Integer> targetPolyCenter){
		this.sourcePoly = sourcePoly;
		this.targetPoly = targetPoly;
		this.sourePolyCenter = sourePolyCenter;
		this.targetPolyCenter = targetPolyCenter;
	}

	
}