package com.tinkerpop.gvz.jung;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.oupls.jung.GraphJung;
import com.tinkerpop.gvz.IGvzModel;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class JungViewer {
	private static final int MAX_PROP_VALUE_LENGTH = 20;
	IGvzModel model;
	int index;
	int size;
	JFrame frame;
	VisualizationViewer<Vertex, Edge> viz;
	boolean disposed = true; // Need to open the window the first time 

	public JungViewer(IGvzModel mdl) {
		this.model = mdl;
		this.index = 0;
		this.size = model.getExprNames().size();

		GraphJung<Graph> graph = new GraphJung<Graph>(model.getGraph());

		Layout<Vertex, Edge> layout = new FRLayout<Vertex, Edge>(graph);
		layout.setSize(new Dimension(800, 600));

		this.viz = new VisualizationViewer<Vertex, Edge>(layout, new Dimension(700, 500));
		viz.setPreferredSize(new Dimension(900, 700));

//		Transformer<Vertex, String> vertexLabelTransformer = new Transformer<Vertex, String>() {
//			public String transform(Vertex vertex) {
//				return (String) vertex.getProperty("name");
//			}
//		};
		
		Transformer<Vertex, String> vertexTooltipTransformer = new Transformer<Vertex, String>() {
			  public String transform(Vertex vertex) {
			    return tooltip("V", vertex);
			  }
			};

		Transformer<Edge, String> edgeLabelTransformer = new Transformer<Edge, String>() {
			public String transform(Edge edge) {
				return edge.getLabel();
			}
		};

		Transformer<Edge, String> edgeTooltipTransformer = new Transformer<Edge, String>() {
			  public String transform(Edge edge) {
			    return tooltip("E", edge);
			  }
			};

		Transformer<Vertex, Paint> vertexPaintTransformer = new Transformer<Vertex, Paint>() {
			public Paint transform(Vertex v) {
				if (model.getMarkedVertices(getExprName()).contains(v)) {
					return Color.YELLOW;
				} else {
					return Color.GREEN;
				}
			}
		};

		Transformer<Edge, Stroke> edgeStrokeTransformer = new Transformer<Edge, Stroke>() {
			public Stroke transform(Edge e) {
				if (model.getMarkedEdges(getExprName()).contains(e)) {
					return new BasicStroke(2);
					//return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f }, 0.0f);
				} else {
					return new BasicStroke(1);
					//return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f }, 0.0f);
				}
			}
		};

		viz.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
		viz.setVertexToolTipTransformer(vertexTooltipTransformer);
		viz.setEdgeToolTipTransformer(edgeTooltipTransformer);
		viz.getRenderContext().setVertexFillPaintTransformer(vertexPaintTransformer);
		viz.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);

		display();
	}
	
	private String tooltip(String type, Element elem) {
		StringBuffer ans = new StringBuffer();
		
		ans.append(type + "[id = " + elem.getId());
		
		for (String key : elem.getPropertyKeys()) {
			String value = elem.getProperty(key).toString();
			if (value.length() > MAX_PROP_VALUE_LENGTH) {
				value = value.substring(0, MAX_PROP_VALUE_LENGTH) + "...";
			}

			ans.append(", ");
			ans.append(key);
			ans.append(" = ");
			ans.append(value);
		}

		return ans.toString();
	}
	
	public int getIndex() {
		return index;
	}

	public int getSize() {
		return size;
	}
	
	public String getExprName() {
		return model.getExprNames().get(index);
	}

	public boolean next() {
		if (index < size - 1) {
			index++;
			display();
			return true;
		} else {
			return false;
		}
	}

	public boolean prev() {
		if (index > 0) {
			index--;
			display();
			return true;
		} else {
			return false;
		}
	}
	
	public void start() {
		index = 0;
		display();
	}
	
	public void end() {
		index = size - 1;
		display();
	}

	private void display() {
	    if (disposed) {
            frame = new JFrame("Gremlin Visualizer") {
                @Override
    	        public void dispose() {
                    disposed = true;
    	            super.dispose();
    	        }
    	    };
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(viz);
            
            disposed = false;
	    }

        frame.pack();
        frame.repaint();
        frame.setVisible(true);

	    System.out.println("Showing " + (getIndex()+1) + " of " + getSize() + ": " + getExprName());
		viz.repaint();
	}

	public void slideshow() throws InterruptedException {
		slideshow(5);
	}
	
	public void slideshow(int waitInSecs) throws InterruptedException {
		if (index != 0) {
			start();
		}

		boolean more;
		do {
			Thread.sleep(waitInSecs * 1000);
			more = next();
		} while (more);
	}
}
