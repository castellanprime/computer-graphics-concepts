public class Transformation{
	public float [][] kernel = new float[3][3];

	Transformation(String flag, String order, float slope, float xOffset, float yOffset){
		if (flag.equalsIgnoreCase("rotate")){
			this.kernel = getRotMatrix(slope, order);
		} else {
			this.kernel = getTransMatrix(xOffset, yOffset, flag, order);
		}
	}

	private float [][] getRotMatrix(float slope, String order){

		float sinThetaInSlopeForm = slope / (float) Math.sqrt(1 + (slope * slope));
		float cosThetaInSlopeForm = 1 / (float) Math.sqrt(1 + (slope * slope));

		if (order.equalsIgnoreCase("inverse")){		
			kernel[0][1] = -sinThetaInSlopeForm;
			kernel[1][0] = sinThetaInSlopeForm;
		} else if (order.equalsIgnoreCase("normal")){
			kernel[0][1] = sinThetaInSlopeForm;
			kernel[1][0] = -sinThetaInSlopeForm;
		}
		kernel[0][0] = cosThetaInSlopeForm;
		kernel[1][1] = cosThetaInSlopeForm;
		kernel[0][2] = 0;
		kernel[1][2] = 0;
		kernel[2][0] = 0;
		kernel[2][1] = 0;
		kernel[2][2] = 1;

		return kernel;
	}

	private float [][] getTransMatrix(float xOffset, float yOffset, String flag, String order){
		if (flag.equalsIgnoreCase("translate")){
			if (order.equalsIgnoreCase("inverse")){
				kernel[2][0] = -xOffset;
				kernel[2][1] = -yOffset;
			}else if (order.equalsIgnoreCase("normal")){
				kernel[2][0] = xOffset;
				kernel[2][1] = yOffset;
			}
			kernel[0][0] = 1;
			kernel[0][1] = 0;
			kernel[0][2] = 0;
			kernel[1][0] = 0;
			kernel[1][1] = 1;
			kernel[1][2] = 0;
			kernel[2][2] = 1;	
		} else if (flag.equalsIgnoreCase("reflect")) {
			/*
				About the y-axis	About the x-axis	About the origin
				[-1 0 0]			[1 0 0]				[-1 0 0]			
				[0 1 0]				[0 -1 0]			[0 -1 0]
				[0 0 1]				[0 0 1]				[0 0 1]
			 */
			if (order.equalsIgnoreCase("y")){
				kernel[0][0] = -xOffset;
				kernel[1][1] = yOffset;
			}else if (order.equalsIgnoreCase("x")){
				kernel[0][0] = xOffset;
				kernel[1][1] = -yOffset;	
			}else if (order.equalsIgnoreCase("origin")){
				kernel[0][0] = -xOffset;
				kernel[1][1] = -yOffset;
			}			
			kernel[0][1] = 0;
			kernel[0][2] = 0;
			kernel[1][0] = 0;
			kernel[1][2] = 0;
			kernel[2][0] = 0;
			kernel[2][1] = 0;
			kernel[2][2] = 1;
		}

		return kernel;
	}

	@Override 
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < 3; row++){
			for (int col = 0; col < 3; col++){
				sb.append(" " + kernel[row][col]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}