package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Jama.Matrix;
import output.writeToFile;
import realTimePlot.RealTimePlotElement;
import filter_01.KalmannFilter_01;
import filter_01.State;
import filter_01.TrueState_01;

public class PlotFrame {
	private JPanel mainPanel;
	
	//public static Font smallFont	 = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);

	private static RealTimePlotElement realtimePlotElement;

	
	@SuppressWarnings("unused")
	private double plotFrequency;
	
	@SuppressWarnings("unused")
	private State state ;

	private static Random generator = new Random();
	//----------------------------------------------------------------------------------------

	public PlotFrame( int plotHorizon, double plotFrequency) {
		this.plotFrequency=plotFrequency;
		
		state = new State();
		
		int globalX = 1200;
		int globalY = 250;
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(globalX,globalY));
		  
		 
		 realtimePlotElement = new RealTimePlotElement( plotHorizon, plotFrequency);
		 mainPanel.add(realtimePlotElement.createPlotElement(), BorderLayout.CENTER);
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	
	private static void runSimulation(double dt, double tmax){
		double clock = 0;
		
		double[][] xInit = {{0.},
	     	     {0.},
	     	     {100.},
	     	     {0.},
	     	     {0.},
	     	     {0.}};
		double[][] Pinit = {{10., 0., 0., 0., 0. ,0.},
					 {0., 10., 0., 0., 0., 0.},
					 {0., 0., 10., 0., 0., 0.},
					 {0., 0., 0., 1., 0., 0.},
					 {0., 0., 0., 0., 1., 0.},
					 {0., 0., 0., 0., 0., 1.}};
		
		writeToFile filewriter = new writeToFile(8);

		
		KalmannFilter_01 filter = new KalmannFilter_01();
		TrueState_01 trueState = new TrueState_01();
		
		// Init filter state:
		filter.setXxEst(new Matrix(xInit));
		filter.setPpEst(new Matrix(Pinit));
		trueState.setXxTrue(new Matrix(xInit));
		
		List<State> stateHist = new ArrayList<State>();
		
		while(clock < tmax ) {
			//---------------------------------------------------------
				State state = new State();
				Matrix measurement = filter.getYy();
				state.setMeasurement(filter.getYy());
				
				// Run Cycle
					trueState.predict(dt);
					filter.cycle(dt, trueState);
				
				state.setEstimatedState(filter.getXxEst());
				state.setCovariance(filter.getPpEst());
				state.setGain(filter.getKK());
				state.setClock(clock);
				state.setAbsEstimationError(filter.getXxEst().minus(trueState.getXxTrue()));
				state.setAbsMeasurementError(filter.getYy().minus(trueState.getXxTrue()));
				state.setTrueState(trueState.getXxTrue());
				
				stateHist.add(state);
			//---------------------------------------------------------
			// Plot:
			realtimePlotElement.updateGenValues(state.getAbsEstimationError().get(2,0),
												state.getAbsMeasurementError().get(2,0)
												);
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//---------------------------------------------------------

			//filewriter.addLine(timeWrite, sensor1_reading, miniKalman.getU_estimated());

			clock += dt;
		}
		filewriter.write();
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Filter Testbench");
		 frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		frame.setLayout(new BorderLayout());

		int plotHorizon = 100; 
		double plotFrequency = 1;
		PlotFrame dataplot = new PlotFrame(plotHorizon, plotFrequency);
		frame.add(dataplot.getMainPanel(), BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
		
		runSimulation(plotFrequency, plotHorizon);
	}
}
