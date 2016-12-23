
import java.awt.*;
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
}