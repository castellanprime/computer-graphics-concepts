package common;
public class Line<T>{
	public Point<T> firstPoint, Point<T> secondPoint;
	public Point<Float> unitVector;

	Line(Point<T> firstPoint, Point<T> secondPoint){
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
	}

	/**
	 * Gets the distance between two points
	 * @return float. Returns the distance squared between two point
	 */
	public float getDistance(){
		float diffX = (float) secondPoint.x - firstPoint.x;
		float diffY = (float) secondPoint.y - firstPoint.y;
		return diffX * diffX + diffY * diffY;
	}

	public float getUnitVector(){
		float diffX = (float) secondPoint.x - firstPoint.x;
		float diffY = (float) secondPoint.y - firstPoint.y;

		float distance = (float) Math.sqrt(getDistance());

		this.unitVector = new Point<Float>(diffX / distance, diffY / distance);
		return this.unitVector;
	}
}