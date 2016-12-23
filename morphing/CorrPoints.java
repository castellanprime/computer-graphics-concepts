import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Map;

public class CorrPoints{
	private ArrayList<Point<Integer>> sourcePoly, targetPoly;
	private Point<Integer> sourePolyCenter, targetPolyCenter, firstPoint, secondPoint;
	private LinkedHashMap<Point<Integer> , Point<Integer>> correspondList = new LinkedHashMap<>();
	private final int eps = 15;

	CorrPoints(ArrayList<Point<Integer>> sourcePoly, ArrayList<Point<Integer>> targetPoly, 
						Point<Integer> sourePolyCenter, Point<Integer> targetPolyCenter){
		this.sourcePoly = sourcePoly;
		this.targetPoly = targetPoly;
		this.sourePolyCenter = sourePolyCenter;
		this.targetPolyCenter = targetPolyCenter;
	}

	public void findCorrespondingPoints(){
		int sourceIndex = 0, targetIndex = 0;
		Point<Integer> refPoint = new Point<Integer>(sourePolyCenter.x + 20, sourePolyCenter.y);

		int sourceSize = sourcePoly.size();
		int targetSize = targetPoly.size(); 

		while (sourceIndex < sourceSize  && targetIndex < targetSize){

			sourcePoint = sourcePoly.get(sourceIndex);
			targetPoint = targetPoly.get(targetIndex);

			// Instead of using the sine we use the angle itself
			sourceAngle = Utils.getAngleBtwVectors(sourePolyCenter, refPoint, sourcePoint);
			targetAngle = Utils.getAngleBtwVectors(sourePolyCenter, refPoint, targetPoint);

			if (Math.abs(sourceAngle - targetAngle) < eps){
				refPoint = sourcePoly.get(sourceIndex);
				this.correspondList.put(sourcePoly.get(sourceIndex), targetPoly.get(targetIndex));
				sourceIndex++;
				targetIndex++;
			}else if (sourceAngle > targetAngle){
				firstPoint = sourcePoint;
				Line line1 = new Line(sourePolyCenter, sourcePoint);

				int diff = targetIndex - 1;
				Point<Integer> newPoint = new Point<Integer>(0, 0);
				if (diff < 0){
					newPoint = targetPoly.get(targetSize - 1);
				} else {
					newPoint = targetPoly.get(diff % targetSize);
				}

				Line line2 = new Line(targetPoint, newPoint);

				secondPoint = line1.getIntersectionPointWith(line2, 0);

				this.correspondList.put(firstPoint, secondPoint);
				refPoint = firstPoint;
				sourceIndex++; 

			}else if (sourceAngle < targetAngle){
				firstPoint = targetPoint;
				Line line1 = new Line(sourePolyCenter, targetPoint);

				int diff = sourceIndex - 1;
				Point<Integer> newPoint = new Point<Integer>(0, 0);
				if (diff < 0){
					newPoint = sourcePoly.get(sourceSize - 1);
				} else {
					newPoint = sourcePoly.get(diff % sourceSize);
				}

				Line line2 = new Line(sourcePoint, newPoint);

				secondPoint = line1.getIntersectionPointWith(line2, 0);

				this.correspondList.put(secondPoint, firstPoint);
				refPoint = firstPoint;
				targetIndex++;
			}
		}

		// Cleanup
		while (targetIndex < targetSize){
			firstPoint = targetPoly.get(targetIndex);
			Line line1 = new Line(sourePolyCenter, firstPoint);
			Line line2 = new Line(refPoint, sourcePoly.get(0));
			secondPoint = line1.getIntersectionPointWith(l2, 0);
			this.correspondList.put(secondPoint, firstPoint);
			targetIndex++;
		}

		while (sourceIndex < sourceSize){
			firstPoint = sourcePoly.get(sourceIndex);
			Line line1 = new Line(sourePolyCenter, firstPoint);
			Line line2 = new Line(refPoint, targetPoly.get(0));
			secondPoint = line1.getIntersectionPointWith(l2, 0);
			this.correspondList.put(secondPoint, firstPoint);
			sourceIndex++;
		}
	}

	public void findMorphingPoints(int numOfStages){
		
	}
}