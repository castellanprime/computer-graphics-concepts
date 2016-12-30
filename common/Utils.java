
import java.awt.*;
import java.util.Random;
class Utils{
	

	public static int getTriangleArea(int firstPointX, int firstPointY, int secondPointX, 
									  int secondPointY, int thirdPointX, int thirdPointY){
		int diffX1 = firstPointX - thirdPointX;
		int diffX2 = secondPointX - thirdPointX;
		int diffY1 = firstPointY - thirdPointY;
		int diffY2 = secondPointY - thirdPointY;

		return (diffX1 * diffY2) - (diffY1 * diffX2);
	}


	public static int getOrientation(int firstPointX, int firstPointY, int secondPointX, 
									  int secondPointY, int thirdPointX, int thirdPointY){
		if (Utils.getTriangleArea(firstPointX, firstPointY, secondPointX, 
							secondPointY, thirdPointX, thirdPointY) > 0){
			return 1;
		} else if (Utils.getTriangleArea(firstPointX, firstPointY, secondPointX, 
								   secondPointY, thirdPointX, thirdPointY) < 0){
			return -1;
		} 
		return 0;
	}

	
	public static int getDistance(int firstPointX, int firstPointY, int secondPointX, int secondPointY){
		int diffX = secondPointX - firstPointX;
		int diffY = secondPointY - firstPointY;
		return diffX * diffX + diffY * diffY;
	}


	public static Point<Float> getUnitVector(int firstPointX, int firstPointY, int secondPointX, int secondPointY){
		int diffX = secondPointX - firstPointX;
		int diffY = secondPointY - firstPointY;

		float distance = (float) Math.sqrt((double) Utils.getDistance(firstPointX, firstPointY, secondPointX, secondPointY));

		Point<Float> unitVector = new Point<Float>(diffX / distance, diffY / distance);
		return unitVector;
	}

	/**
	 * Get the angle between two lines that have a common point
	 * @param  common   Point<T> A point
	 * @param  previous Point<T> A point
	 * @param  current  Point<T> A point
	 * @return float    This returns the angle between the two lines
	 */
	public static float getAngleBtwVectors(Point<Integer> common, Point<Integer> previous, Point<Integer> current){
		int diffX1 = current.x - common.x;
		int diffY1 = current.y - common.y;
		int diffX2 = previous.x - common.x;
		int diffY2 = previous.y - common.y;

		float denom1 = (float) Math.sqrt((double)diffX1 * diffX1 + diffY1 * diffY1);
		float denom2 = (float) Math.sqrt((double)diffX2 * diffX2 + diffY2 * diffY2);

		float unitVec1X = (float) diffX1 / denom1;
		float unitVec1Y = (float) diffY1 / denom1;

		float unitVec2X = (float) diffX2 / denom2;
		float unitVec2Y = (float) diffY2 / denom2;

		double angleInRad = Math.acos( unitVec1X * unitVec2X + unitVec1Y * unitVec2Y);

		float angleInDeg = (float) Math.toDegrees(angleInRad);
		return angleInDeg;
	}

	public static void markPoint(Graphics g, Point<Integer> point, int offset){
		g.drawRect(point.x - offset, point.y - offset, offset * 2, offset * 2); 
	}

	public static Point<Integer> translate(Transformation transform, Point<Integer> point){
		float [][] mat = transform.kernel;

		float firstCol = mat[0][0] * point.x + mat[1][0] * point.y + mat[2][0];
		float secondCol = mat[0][1] * point.x + mat[1][1] * point.y + mat[2][1];
		float thirdCol = mat[0][2] * point.x + mat[1][2] * point.y + mat[2][2];

		float newX = firstCol / thirdCol;
		float newY = secondCol / thirdCol;

		return new Point<Integer>(Math.round(newX), Math.round(newY));
	}

	
	public static double getPointAngleWithXAxis(Point<Integer> firstPoint, Point<Integer> secondPoint){
	
		double angleRad = Math.atan2(firstPoint.y, firstPoint.x) - Math.atan2(secondPoint.y, secondPoint.y);
		double angle = Math.toDegrees(angleRad);

		if (angle < 0){
			angle += 360;
		}

		return angle;
	}

	public static Color generateRandomColour(){

		// Referenced from http://stackoverflow.com/questions/4246351/creating-random-colour-in-java
		Random random = new Random();
		final float hue = random.nextFloat();
		final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
		final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
		return (Color.getHSBColor(hue, saturation, luminance));

	}

	/*	
	public static double getAngleOfPointWithXAxis(Point<Integer> firstPoint, Point<Integer> refPoint){
		double result = 0.0;
		int diffX1 = firstPoint.x - refPoint.x;
		int diffY1 = firstPoint.y - refPoint.y;
		double denom = Math.sqrt(diffX1 * diffX1 + diffY1 * diffY1);
		result = Math.asin((double)(refPoint.y - firstPoint.y) / denom);
		double angle = Math.toDegrees(result);

		if (angle < 0){
			angle = 360 - Math.abs(angle);
		}

		return angle;
	}*/
}