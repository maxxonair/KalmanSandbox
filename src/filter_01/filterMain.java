package filter_01;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class filterMain {
	
	
	public static void main(String[] args) {
		
		double[][] xInit = {{0.},
				     	     {0.},
				     	     {100.},
				     	     {0.},
				     	     {0.},
				     	     {0.}};
		double[][] Pinit = { {10., 0., 0., 0., 0. ,0.},
							 {0., 10., 0., 0., 0., 0.},
							 {0., 0., 10., 0., 0., 0.},
							 {0., 0., 0., 1., 0., 0.},
							 {0., 0., 0., 0., 1., 0.},
							 {0., 0., 0., 0., 0., 1.}};
		
		double dt =1;
		double tmax = 100;
		double clock = 0;
		
		// Create Filter:
		KalmannFilter_01 filter = new KalmannFilter_01();
		TrueState_01 trueState = new TrueState_01();

		// Init filter state:
		filter.setXxEst(new Matrix(xInit));
		filter.setPpEst(new Matrix(Pinit));
		trueState.setXxTrue(new Matrix(xInit));
		
		List<State> stateHist = new ArrayList<State>();
		
		while(clock < tmax ) {
			
			State state = new State();
			
			// Run Cycle
				trueState.predict(dt);
				filter.cycle(dt, trueState);
				
		    state.setMeasurement(filter.getYy());
			state.setEstimatedState(filter.getXxEst());
			state.setPredictedState(filter.getXxPre());
			state.setCovariance(filter.getPpEst());
			state.setGain(filter.getKK());
			state.setClock(clock);
			state.setAbsError(filter.getXxEst().minus(trueState.getXxTrue()));
			state.setTrueState(trueState.getXxTrue());
			
			stateHist.add(state);
			
			
			clock += dt;
		}
		
		for (State state : stateHist) {
			System.out.println(state.getClock()+" (Xest) "+ 
							   state.getEstimatedState().get(2, 0) +" (Xtrue) "
							 + state.getTrueState().get(2, 0) +" (Xpre) "
							 + state.getPredictedState().get(2, 0) +" (Y) "
							 //+ state.getEstimatedState().get(5, 0) +" (Y) "
							 + state.getMeasurement().get(2, 0) +" (Gain) "
							 + state.getGain().get(2, 2) +" (Cov) "
							 + state.getCovariance().get(2, 2)
							 );
		}
		
	}

}
