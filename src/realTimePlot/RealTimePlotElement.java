package realTimePlot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import filter_01.State;


public class RealTimePlotElement {

	
	 public static double PI = 3.141592653589793238462643;
		@SuppressWarnings("unused")
		private final static double deg2rad = PI/180.0; 		//Convert degrees to radians
		//private final static double rad2deg = 180/PI; 		//Convert radians to degrees
    
   private List<InputFileSet> resultFile = new ArrayList<InputFileSet>(); 
   
   private XYSeriesCollection resultSet = new XYSeriesCollection();
   
   private ChartPanel chartPanel;
   
   private Color[] seriesColor = new Color[4];
  // seriesColor[0] = Color.RED;
   
   private int ID;
   private Color backgroundColor;
   private Color labelColor;

   private int nPlot; 
   
   public RealTimePlotElement(int plotHorizon, double plotFrequency) {
	   
	   	nPlot = (int) (plotFrequency * plotHorizon); 
	   	
	   InputFileSet q1 = new InputFileSet(nPlot, plotFrequency);
	   q1.setInputDataFileName("q1");
	   q1.setLegend(true);
	   resultFile.add(q1);
	   InputFileSet q2 = new InputFileSet(nPlot, plotFrequency);
	   q2.setInputDataFileName("q2");
	   q2.setLegend(true);
	   resultFile.add(q2);
	   InputFileSet q3 = new InputFileSet(nPlot, plotFrequency);
	   q3.setInputDataFileName("q3");
	   q3.setLegend(true);
	   resultFile.add(q3);
	   InputFileSet q4 = new InputFileSet(nPlot, plotFrequency);
	   q4.setInputDataFileName("q4");
	   q4.setLegend(true);
	   resultFile.add(q4);
	   
	     labelColor = new Color(220,220,220);   
	   	 backgroundColor = new Color(41,41,41);
	   	 
	   	seriesColor[0] = Color.WHITE;
	   	seriesColor[1] = Color.CYAN;
	   	seriesColor[2] = Color.GREEN;
	   	seriesColor[3] = Color.RED;
  	
   }
   
	
	public JPanel createPlotElement() {
	   	 
       JPanel panel = new JPanel();
       panel.setLayout(new BorderLayout());
       panel.setBackground(backgroundColor);

	      try {
			chartPanel = createChartPanel();
		      panel.add(chartPanel, BorderLayout.CENTER);
			} catch (Exception e) {
				System.err.println("ERROR: Chart Panel could not be created.");
			}
       
		return panel;
	}
	
	public ChartPanel createChartPanel() throws IOException {
		try {
			//resultSet = updatePosition(new State());
		} catch(Exception eFNF2) {
			System.out.println("Error: creating initial resultSet failed. "+eFNF2);
		}
	    //-----------------------------------------------------------------------------------
		JFreeChart chart = ChartFactory.createScatterPlot("", "", "", resultSet, PlotOrientation.VERTICAL, false, false, false); 
		XYPlot plot = (XYPlot)chart.getXYPlot(); 
		//try {
		    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
			Font font3 = new Font("Dialog", Font.PLAIN, 12);
		    double size = 2.0;
		    double delta = size / 2.0;
			Shape dot = new Ellipse2D.Double(-delta, -delta, size, size);
		
		    for(int i=0;i<resultFile.size();i++) {
			    plot.setRenderer(i, renderer);
				plot.getDomainAxis().setLabelFont(font3);
				plot.getRangeAxis().setLabelFont(font3);
				plot.getRangeAxis().setLabelPaint(labelColor);
				plot.getDomainAxis().setLabelPaint(labelColor);
				plot.setForegroundAlpha(0.5f);
				plot.setBackgroundPaint(backgroundColor);
				plot.setDomainGridlinePaint(labelColor);
				plot.setRangeGridlinePaint(labelColor); 
				plot.getRangeAxis().setLabel("[-]");
				plot.getDomainAxis().setLabel("time [s]");
			    renderer.setSeriesPaint( i , seriesColor[i]);
				renderer.setSeriesShape(i, dot);
		    }
		
			chart.setBackgroundPaint(backgroundColor); 	
			final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
	
			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setMaximumDrawHeight(50000);
			chartPanel.setMaximumDrawWidth(50000);
			chartPanel.setMinimumDrawHeight(0);
			chartPanel.setMinimumDrawWidth(0);
	   return chartPanel;
	}

	
	public void updateChart(State state, State filteredState){
	   	try {
	   	   	resultSet.removeAllSeries();
	   		resultSet = updateFilterPosition_x(state, filteredState);
	   	} catch(Exception eFNF2) {
	   		
	   	}
	}
	
	
	public XYSeriesCollection updateFilterPosition_x(State state, State filteredState) {
		try {
		   	resultSet.removeAllSeries();
			for(int i=0;i<resultFile.size();i++) {
				XYSeries xySeries = new XYSeries(""+i+""+resultFile.get(i).getInputDataFileName(), false, true); 
					   if(i==0) {
					resultFile.get(i).updatePlotData(state.getEstimatedState().get(2, 0));
				} else if(i==1) {
					resultFile.get(i).updatePlotData(filteredState.getMeasurement().get(2, 0));
				} else if(i==2) {
					//resultFile.get(i).updatePlotData(state.px);
				} else if(i==3) {
					//resultFile.get(i).updatePlotData(quat.z);
				}
				for(int j=0;j<resultFile.get(i).plotData.length;j++) {
					xySeries.add(resultFile.get(i).plotData[j][0] , resultFile.get(i).plotData[j][1]);
				}
			resultSet.addSeries(xySeries); 
			}
		} catch (Exception excp ) {
			
		}
	return resultSet;
	}
	
	public XYSeriesCollection updateGenValues(double ...Value) {
		try {
		   	resultSet.removeAllSeries();
		   	int i =0;
			for(double value: Value) {
				XYSeries xySeries = new XYSeries(""+i+""+resultFile.get(i).getInputDataFileName(), false, true); 
				resultFile.get(i).updatePlotData(value);
				
				for(int j=0;j<resultFile.get(i).plotData.length;j++) {
					xySeries.add(resultFile.get(i).plotData[j][0] , resultFile.get(i).plotData[j][1]);
				}
				
				i+=1;
			resultSet.addSeries(xySeries); 
			}
		} catch (Exception excp ) {
			
		}
	return resultSet;
	}
	
	public XYSeriesCollection updatePosition(State state) {
		try {
		   	resultSet.removeAllSeries();
			for(int i=0;i<resultFile.size();i++) {
				XYSeries xySeries = new XYSeries(""+i+""+resultFile.get(i).getInputDataFileName(), false, true); 
					   if(i==0) {
					resultFile.get(i).updatePlotData(state.getEstimatedState().get(2, 0));
				} else if(i==1) {
					resultFile.get(i).updatePlotData(state.getMeasurement().get(2, 0));
				} else if(i==2) {
					//resultFile.get(i).updatePlotData(state.pz);
				} else if(i==3) {
					//resultFile.get(i).updatePlotData(quat.z);
				}
				for(int j=0;j<resultFile.get(i).plotData.length;j++) {
					xySeries.add(resultFile.get(i).plotData[j][0] , resultFile.get(i).plotData[j][1]);
				}
			resultSet.addSeries(xySeries); 
			}
		} catch (Exception excp ) {
			
		}
	return resultSet;
	}
	

	
	public int getID() {
		return ID;
	}

	public ChartPanel getChartPanel() {
		return chartPanel;
	}

}
