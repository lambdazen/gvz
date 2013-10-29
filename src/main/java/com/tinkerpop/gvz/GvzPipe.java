package com.tinkerpop.gvz;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;

public class GvzPipe<S, E> extends AbstractMetaPipe<S, E> implements MetaPipe {
    private IGvzPipeListener listener;
    private String exprName;
    private Pipe<S, E> pipe;

    public GvzPipe(IGvzPipeListener listener, String exprName, Pipe<S, E> pipe) {
        this.listener = listener;
        this.exprName = exprName;
        this.pipe = pipe;
    }

    @Override
    public void setStarts(final Iterator<S> starts) {
        this.pipe.setStarts(starts);
        this.starts = starts;
    }

    @Override
    protected List getPathToHere() {
        return this.pipe.getCurrentPath();
    }

    @Override
    public E processNextStart() {
        E ans = this.pipe.next();
        
        if (ans instanceof Vertex) {
        	Vertex vertex = (Vertex)ans;
        	listener.addVertex(vertex);
        	listener.markVertex(exprName, vertex);
        } else if (ans instanceof Edge) {
        	Edge edge = (Edge)ans;
        	listener.addVertex(edge.getVertex(Direction.IN));
        	listener.addVertex(edge.getVertex(Direction.OUT));
        	listener.markEdge(exprName, edge);
        }

        return ans;
    }

    @Override
    public List<Pipe> getPipes() {
        return (List)Arrays.asList(this.pipe);
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipe);
    }
}
