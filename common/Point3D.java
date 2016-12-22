
public class Point3D<T extends Number> extends Point<T>{
	public T z;

	Point3D(T x, T y, T z){
		super(x, y);
		this.z = z;
	}

	@Override
	public String toString(){
		return toString() + " Z Coord: " + z.toString();	
	}
}