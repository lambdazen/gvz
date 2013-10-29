package com.tinkerpop.gvz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.IdentityPipe;
import com.tinkerpop.pipes.Pipe;

public class GremlinPipelineAnalyzer<S, E> implements IGvzPipeListener, IGvzModel {
	GremlinPipeline<S, E> pipeline;
	GremlinPipeline<S, E> gvzPipeline;
	Graph vizGraph;
	Map<String, List<Object>> markedObjects;
	List<String> exprNameList;
	
	public GremlinPipelineAnalyzer(GremlinPipeline<S, E> pipeline) {
		this.pipeline = pipeline;
		this.gvzPipeline = new GremlinPipeline<S, E>(pipeline.getStarts());
		GremlinPipeline<S, E> dummyPipeline = new GremlinPipeline<S, E>(pipeline.getStarts());  
		this.vizGraph = new TinkerGraph();
		this.markedObjects = new HashMap<String, List<Object>>();
		this.exprNameList = new ArrayList<String>();

		for (Pipe pipe : pipeline.getPipes()) {
			if (pipe instanceof IdentityPipe) {
				continue;
			}

			// Use dummy pipeline to get the expression name
			dummyPipeline.add(pipe);
			String exprName = dummyPipeline.toString();
			exprNameList.add(exprName);

			// Build gvz pipeline using the GvzPipe (a meta-pipe)  
			gvzPipeline.add(new GvzPipe(this, exprName, pipe));
		}

		// Iterate through the pipeline to load the graph, marked vertices, edges
		gvzPipeline.iterate();
		
		// Remove expression steps that don't have any matches
		exprNameList.retainAll(markedObjects.keySet());
	}

	@Override
	public Graph getGraph() {
		return vizGraph;
	}

	@Override
	public List<String> getExprNames() {
		return exprNameList;
	}
	
	@Override
	public List<Vertex> getMarkedVertices(String exprName) {
		List<Vertex> ans = new ArrayList<Vertex>();
		
		List<Object> markedObjs = markedObjects.get(exprName);
		for (Object o : markedObjs) {
			if (o instanceof Vertex) {
				ans.add((Vertex)o);
			}
		}
		
		return ans;
	}

	@Override
	public List<Edge> getMarkedEdges(String exprName) {
		List<Edge> ans = new ArrayList<Edge>();

		List<Object> markedObjs = markedObjects.get(exprName);
		for (Object o : markedObjs) {
			if (o instanceof Edge) {
				ans.add((Edge)o);
			}
		}

		return ans;
	}

	/** Add a vertex to the visualization graph */
	public void addVertex(Vertex gv) {
		// Use the toString() of the ID for lookups
		String vid = gv.getId().toString();

		// Is there a TinkerGraph vertex?
		Vertex tv = vizGraph.getVertex(vid);

		if (tv == null) {
			// Nope. Need to add it. TinkerGraph can take any string ID
			tv = vizGraph.addVertex(vid);
			//System.out.println("Adding vertex with ID: " + vid);

			for (String key : gv.getPropertyKeys()) {
				Object value = gv.getProperty(key);
				tv.setProperty(key, value);
				//System.out.println("  Property: " + key + " = " + value);
			}

			// Now add edges to known vertices
			for (Edge ge : gv.getEdges(Direction.BOTH)) {
				addEdge(ge);
			}
		}
	}

	/** Add an edge to the visualization graph with one known vertex endpoint */ 
	public void addEdge(Edge ge) {
		Vertex outVertex = vizGraph.getVertex(ge.getVertex(Direction.OUT).getId().toString());
		Vertex inVertex = vizGraph.getVertex(ge.getVertex(Direction.IN).getId().toString());

		if ((outVertex != null) && (inVertex != null)) {
			// Only add the edge if both endpoints are known
			Edge te = vizGraph.addEdge(ge.getId().toString(), outVertex, inVertex, ge.getLabel());
			//System.out.println("Adding edge with ID: " + te.getId() + ", label: " + ge.getLabel());

			for (String key : ge.getPropertyKeys()) {
				String value = ge.getProperty(key);
				te.setProperty(key, value);
				//System.out.println("  Property: " + key + " = " + value);
			}
		}
	}

	@Override
	public void markVertex(String exprName, Vertex gv) {
		Vertex tv = vizGraph.getVertex(gv.getId().toString());
		if (tv != null) {
			markObject(exprName, tv);
		}
	}

	@Override
	public void markEdge(String exprName, Edge ge) {
		Edge te = vizGraph.getEdge(ge.getId().toString());
		if (te != null) {
			markObject(exprName, te);
		}
	}
	
	private void markObject(String exprName, Object obj) {
		List<Object> value = markedObjects.get(exprName);
		if (value == null) {
			value = new ArrayList<Object>();
		}

		value.add(obj);
		markedObjects.put(exprName, value);
	}
}
