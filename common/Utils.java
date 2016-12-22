package common;
public class Utils{
	
	/**
	 *  Gets the area of a triangle formed by three points. 
	 * @param  firstPoint  The firstPoint to the right
	 * @param  secondPoint The secondPoint to the top
	 * @param  thirdPoint  The finalPoint to the left
	 * @return area 	   The area of the triangle
	 */
	static float getTriangleArea(Point<T> firstPoint, Point<T> secondPoint, Point<T> thirdPoint){
		float diffX1 = (float) firstPoint.x - thirdPoint.x;
		float diffX2 = (float) secondPoint.x - thirdPoint.x;
		float diffY1 = (float) firstPoint.y - thirdPoint.y;
		float diffY2 = (float) secondPoint.y - thirdPoint.y;

		return diffX1 * diffY2 - diffX2 * diffY1;
	}

	/**
	 * Get the orientation of the triangle formed by three points
	 * @param  firstPoint  The firstPoint to the right
	 * @param  secondPoint The secondPoint to the top
	 * @param  thirdPoint  The finalPoint to the left
	 * @return boolean 	   False if the orientation is clockwise, True if it is counter-clockwise
	 */
	static boolean getOrientation(Point<T> firstPoint, Point<T> secondPoint, Point<T> thirdPoint){
		if (getTriangleArea(firstPoint, secondPoint, thirdPoint) > 0){
			return true;
		}
		return false;
	}


	/**
	 * Get the intersection of two lines using the parametric equations for the lines
	 * Taken from http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
	 * @param  l1 	Line<T> The first line
	 * @param  l2 	Line><T> The second line
	 * @param  flag integer This integer is initially 0. The flag is set after the function runs. 
	 *              		If the flag becomes 1, then the lines are collinear. 
	 *              		If the flag becomes 3, then there does not exist an intersection between the lines 
	 * @return  Point<T>  The intersection point. 
	 */
	static Point<T> getIntersectionPoint(Line<T> l1, Line<T> l2, int flag){
		// Differencw within one line
		T diffX1 = l1.secondPoint.x - l1.firstPoint.x;
		T diffX2 = l2.secondPoint.x - l2.firstPoint.x;
		T diffY1 = l1.secondPoint.y - l1.firstPoint.y;
		T diffY2 = l2.secondPoint.y - l2.firstPoint.y;

		// Difference between the two lines
		T diffX3 = l1.firstPoint.x - l2.firstPoint.x;
		T diffY3 = l1.firstPoint.y - l2.firstPoint.y;

		T denom = diffX1 * diffY2 - diffX2 * diffY1;

		// Calc t
		T t_numer = diffX2 * diffY3 - diffY2 * diffX3;

		// Calc s
		T s_numer = diffX1 * diffY3 - diffY1 * diffX3;

		if (denom == 0){
			flag = 1;
		} else if (denom > 0){
			flag = 2;
		} 

		if (t_nummer > 0 && flag == 2 || s_numer > 0 && flag == 2 || 
			((s_numer > denom && flag == 2) || (t_numer > denom && flag == 2))){
			flag = 3;
		}

		if (flag == 1 || flag == 3){
			return null;
		}

		// Using t
		t =  t_numer / denom;

		T intersectX = l1.firstPoint.x + (t * diffX1);
		T intersectY = l1.firstPoint.y + (t * diffY1);

		return new Point<T>(intersectX, intersectY);   
	}

	/**
	 * Get the angle between two lines that have a common point
	 * @param  common   Point<T> A point
	 * @param  previous Point<T> A point
	 * @param  current  Point<T> A point
	 * @return float    This returns the angle between the two lines
	 */
	static float getAngleBtwVectors(Point<T> common, Point<T> previous, Point<T> current){
		T diffX1 = current.x - common.x;
		T diffY1 = current.y - common.y;
		T diffX2 = previous.x - common.x;
		T diffY2 = previous.y - common.y;

		float denom1 = (float) Math.sqrt(diffX1 * diffX1 + diffY1 * diffY1);
		float denom2 = (float) Math.sqrt(diffX2 * diffX2 + diffY2 * diffY2);

		float unitVec1X = diffX1 / denom1;
		float unitVec1Y = diffY1 / denom1;

		float unitVec2X = diffX2 / denom2;
		float unitVec2Y = diffY2 / denom2;

		double angleInRad = Math.acos( unitVec1X * unitVec2X + unitVec1Y * unitVec2Y);

		float angleInDeg = (float) Math.toDegrees(angleInRad);
		return angleInDeg;
	}
}