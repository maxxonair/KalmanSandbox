package filter_01;

import java.util.Random;

import Jama.Matrix;

public class KalmannFilter_01 {
	
	private int nrStates;
	
	// State transition matrix
	private Matrix AA ;
	// Input Matrix
	private Matrix BB ;
	// Measurement Matrix
	private Matrix CC;
	// Kalman Gain
	private Matrix KK;
	// State prediction vector
	private Matrix xxPre;
	// State estimate vector
	private Matrix xxEst;
	// State covariance Matrix
	private Matrix PpEst;
	// Input vector
	private Matrix uu;
	// Measurement vector
	private Matrix yy;
	// State Noise vector 
	private Matrix ww;
	// Sensor Noise covariance matrix 
	private Matrix RR;
	// Measurement noise vector
	private Matrix zz;
	// Process noise covariance matrix
	private Matrix QQ;
	// Transformation Matrix 
	private Matrix HH;
	// Identity Matrix
	private Matrix II;
	
	public KalmannFilter_01() {
		this.nrStates = 6;
		initMatrices(nrStates);
	}
	
	private void initMatrices(int nrStates) {
		// Init matrices
		AA 	  = new Matrix(new double[nrStates][nrStates]);
		BB 	  = new Matrix(new double[nrStates][3]);
		CC 	  = new Matrix(new double[nrStates][nrStates]);
		KK 	  = new Matrix(new double[nrStates][nrStates]);
		RR 	  = new Matrix(new double[nrStates][nrStates]);
		QQ 	  = new Matrix(new double[nrStates][nrStates]);
		HH 	  = Matrix.identity(nrStates, nrStates);
		II 	  = Matrix.identity(nrStates, nrStates);
		PpEst = new Matrix(new double[nrStates][nrStates]);
		// Init vectors
		xxEst = new Matrix(new double[nrStates][1]);
		xxPre = new Matrix(new double[nrStates][1]);
		uu 	  = new Matrix(new double[3][1]);
		yy 	  = new Matrix(new double[nrStates][1]);
		ww 	  = new Matrix(new double[nrStates][1]);
		zz 	  = new Matrix(new double[nrStates][1]);
		
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
		
		double[][] rr = {{10., 0., 0., 0., 0. ,0.},
						 {0., 10., 0., 0., 0., 0.},
						 {0., 0., 10., 0., 0., 0.},
						 {0., 0., 0., 1., 0., 0.},
						 {0., 0., 0., 0., 1., 0.},
						 {0., 0., 0., 0., 0., 1.}};
		RR = new Matrix(rr);
	}
	
	private void predict(double dt) {
		setStatePredictionMatrices(dt);
		xxPre = ( AA.times(xxEst) ).plus( BB.times(uu) ).plus(ww);
		PpEst = AA.times(PpEst.times(AA.transpose())).plus(QQ);
	}

	
	private void correct() {
		// Update Kalmann gain:
		KK = ( PpEst.times(HH) ).times( ( HH.times(PpEst.times(HH.transpose()).plus(RR) ).inverse() ) );
		// Update state estimate
		xxEst = xxPre.plus(KK.times( yy.minus(HH.times(xxPre) ) ) );
		PpEst =(II.minus(KK.times(HH))).times(PpEst);
	}
	
	public void cycle(double dt) {		
		predict(dt);
		yy = takeMeasurement();
		correct();
	}
	
	public void cycle(double dt, TrueState_01 trueState) {		
		predict(dt);
		yy = takeMeasurement(trueState);
		correct();
	}
	
	private Matrix takeMeasurement() {
		
		//Matrix noise = new Matrix(new Random(6,1)./100);
		Random rand = new Random();
		double[][] noise = { {rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()}};
		yy = ( AA.times(xxPre) ).plus( BB.times(uu) ).plus(new Matrix(noise));

		return yy;
	}
	
	private Matrix takeMeasurement(TrueState_01 trueState) {
		
		Random rand = new Random();
		double[][] noise = {{rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()},
				     	     {rand.nextDouble()}};
		yy = trueState.getXxTrue().plus(new Matrix(noise));

		return yy;
	}

	public Matrix getXxEst() {
		return xxEst;
	}

	public void setXxEst(Matrix xxEst) {
		this.xxEst = xxEst;
	}

	public Matrix getPpEst() {
		return PpEst;
	}

	public void setPpEst(Matrix ppEst) {
		PpEst = ppEst;
	}

	public Matrix getKK() {
		return KK;
	}

	public Matrix getYy() {
		return yy;
	}

	public Matrix getXxPre() {
		return xxPre;
	}
	
}
