package com.tinkerpop.gvz;

import java.util.List;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public interface IGvzModel {
	Graph getGraph();

	List<String> getExprNames();

	List<Vertex> getMarkedVertices(String exprName);

	List<Edge> getMarkedEdges(String exprName);
}
