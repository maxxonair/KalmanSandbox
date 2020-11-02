package filter_01;

import Jama.Matrix;

public class State {

	
	private Matrix EstimatedState;
	private Matrix PredictedState;
	private Matrix Covariance;
	private Matrix Gain;
	private Matrix Measurement;
	
	private double clock;
	
	private Matrix AbsMeasurementError;
	private Matrix AbsEstimationError;
	private Matrix AbsError;
	
	private Matrix TrueState;
	
	
	public State() {
		
	}	


	public double getClock() {
		return clock;
	}


	public void setClock(double clock) {
		this.clock = clock;
	}


	public Matrix getEstimatedState() {
		return EstimatedState;
	}


	public void setEstimatedState(Matrix state) {
		EstimatedState = state;
	}


	public Matrix getCovariance() {
		return Covariance;
	}


	public void setCovariance(Matrix covariance) {
		Covariance = covariance;
	}


	public Matrix getGain() {
		return Gain;
	}


	public void setGain(Matrix gain) {
		Gain = gain;
	}


	public Matrix getMeasurement() {
		return Measurement;
	}


	public void setMeasurement(Matrix measurement) {
		Measurement = measurement;
	}


	public Matrix getAbsMeasurementError() {
		return AbsMeasurementError;
	}


	public void setAbsMeasurementError(Matrix absMeasurementError) {
		AbsMeasurementError = absMeasurementError;
	}


	public Matrix getAbsEstimationError() {
		return AbsEstimationError;
	}


	public void setAbsEstimationError(Matrix absEstimationError) {
		AbsEstimationError = absEstimationError;
	}


	public Matrix getAbsError() {
		return AbsError;
	}


	public void setAbsError(Matrix absError) {
		AbsError = absError;
	}


	public Matrix getTrueState() {
		return TrueState;
	}


	public void setTrueState(Matrix trueState) {
		TrueState = trueState;
	}


	public Matrix getPredictedState() {
		return PredictedState;
	}


	public void setPredictedState(Matrix predictedState) {
		PredictedState = predictedState;
	}
	
	
}
