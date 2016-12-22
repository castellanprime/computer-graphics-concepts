package common;
public class Point3D<T> extends Point<T>{
	public T z;

	Point3D(T x, T y, T z){
		super(x, y);
		this.z = z;
	}
}