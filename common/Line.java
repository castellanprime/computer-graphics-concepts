
public class Line{
	public Point<Integer> firstPoint, secondPoint;

	Line(Point<Integer> firstPoint, Point<Integer> secondPoint){
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
	}
	
	/**
	 * Get the intersection of two lines using the parametric equations for the lines
	 * Taken from http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
	 * @param  l 	Line><T> A line intersecting with this line
	 * @param  flag integer This integer is initially 0. The flag is set after the function runs. 
	 *              		If the flag becomes 1, then the lines are collinear. 
	 *              		If the flag becomes 3, then there does not exist an intersection between the lines 
	 * @return  Point<Integer>  The intersection point in inting point 
	 */
	public Point<Integer> getIntersectionPointWith(Line l, int flag){
		// Differencw within one line
		int diffX1 = this.secondPoint.x - this.firstPoint.x;
		int diffX2 = l.secondPoint.x - l.firstPoint.x;
		int diffY1 = this.secondPoint.y - this.firstPoint.y;
		int diffY2 = l.secondPoint.y - l.firstPoint.y;

		// Difference between the two lines
		int diffX3 = this.firstPoint.x - l.firstPoint.x;
		int diffY3 = this.firstPoint.y - l.firstPoint.y;

		int denom = diffX1 * diffY2 - diffX2 * diffY1;

		// Calc t
		int t_numer = diffX2 * diffY3 - diffY2 * diffX3;

		// Calc s
		int s_numer = diffX1 * diffY3 - diffY1 * diffX3;

		if (denom == 0){
			flag = 1;
		} else if (denom > 0){
			flag = 2;
		} 

		if (t_numer > 0 && flag == 2 || s_numer > 0 && flag == 2 || 
			((s_numer > denom && flag == 2) || (t_numer > denom && flag == 2))){
			flag = 3;
		}

		if (flag == 1 || flag == 3){
			return null;
		}

		// Using t
		float t = (float) t_numer / denom;

		int intersectX = Math.round(this.firstPoint.x + (t * diffX1));
		int intersectY = Math.round(this.firstPoint.y + (t * diffY1));

		return new Point<Integer>(intersectX, intersectY);   
	}

	@Override
	public String toString(){
		return firstPoint.toString() + secondPoint.toString();
	}

}