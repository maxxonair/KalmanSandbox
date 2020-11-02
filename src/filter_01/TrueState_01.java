package filter_01;

import java.util.Random;

import Jama.Matrix;

public class TrueState_01 {

	private Matrix xxTrue ;
	
	// State transition matrix
	private Matrix AA ;
	// Input Matrix
	private Matrix BB ;
	// Input vector
	private Matrix uu;
	
	private double dt;
	
	public TrueState_01() {
		xxTrue = new Matrix(new double[6][1]);
	}
	
	private void setStatePredictionMatrices(double dt) {
		double[][] aa = {{1., 0., 0., dt, 0. ,0.},
						 {0., 1., 0., 0., dt, 0.},
						 {0., 0., 1., 0., 0., dt},
						 {0., 0., 0., 1., 0., 0.},
						 {0., 0., 0., 0., 1., 0.},
						 {0., 0., 0., 0., 0., 1.}};
		double[][] bb = {{0.5*dt*dt , 0.        , 0. },
						 {0.5*dt*dt , 0.5*dt*dt , 0. },
						 {0.5*dt*dt , 0.        , 0.5*dt*dt  },
						 {dt, 0., 0. },
						 {0., dt, 0. },
						 {0., 0., dt }
				         };
		double[][] uk = {{0.},
					     {0.},
					     {-9.81}};
		
		AA = new Matrix(aa);
		BB = new Matrix(bb);
		uu = new Matrix(uk);
		
	}
	
	public void predict(double dt) {
		setStatePredictionMatrices(dt);
		Random rand = new Random();
		double[][] noise = {{rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()}};
		xxTrue = ( AA.times(xxTrue) ).plus( BB.times(uu) ).plus(new Matrix(noise));
	}

	public Matrix getXxTrue() {
		return xxTrue;
	}

	public void setXxTrue(Matrix xxTrue) {
		this.xxTrue = xxTrue;
	}
	
}
