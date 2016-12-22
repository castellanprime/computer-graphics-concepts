
public class Point<T extends Number>{
	public T x, y;

	Point(T x, T y){
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString(){
		return " X Coord: " + this.x.toString() + " Y Coord:" + this.y.toString();
	}
}