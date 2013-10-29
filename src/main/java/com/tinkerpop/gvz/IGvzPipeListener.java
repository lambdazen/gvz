package com.tinkerpop.gvz;

import java.util.List;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public interface IGvzPipeListener {
	public void addVertex(Vertex gv);

	public void markVertex(String exprName, Vertex vertex);

	public void markEdge(String exprName, Edge edge);
}
