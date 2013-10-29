## Overview

gvz (pronounced gee-whiz) is a visualization tool for Gremlin. It is useful to debug Gremlin expressions by looking at the portion of the graph database accessed in each step of the Gremlin expression. 

## Installation

To install gvz, you can build the project, and copy target/gvz-0.1-SNAPSHOT.jar to the lib/ folder in the gremlin installation. 

After this, you need to execute the following command inside the Gremlin shell:

```
gvz = { GremlinPipeline pipeline -> new com.tinkerpop.gvz.jung.JungViewer(new com.tinkerpop.gvz.GremlinPipelineAnalyzer(pipeline)) }
```

## Usage

You can launch gvz using the following command:

```
gremlin> x = gvz [any Gremlin expression]
```

This will pop-up a window with a sub-graph consisting of all vertices discovered by the expression's steps. The visualization highlights the vertices selected by the first step of the expression. You can mouse over the vertices and edges to view their properties. 

**Note:** Highlighted vertices are yellow, and other vertices are green. Highlighed edges are shown with a bold edge line. 

Once the visualization window is open, you can use the following commands to step through the various steps of the Gremlin expression:

```
gremlin> x.next() // shows next step
gremlin> x.prev() // shows previous step
gremlin> x.start() // shows the first step
gremlin> x.end() // shows the last step
```

You can also use the following command to animate the steps. 

```
gremlin> x.slideshow() // shows a slideshow from first to last step with a 5 sec gap
gremlin> x.slideshow(10) // same with a 10 second gap
```

## Limitations of this implementation

This is a proof-of-concept. 

- UI is very basic (very little effort has gone in)
- The goal is to show the graphs using GraphStream, but I ran into issues with Maven. The design allows for multiple viewers. The code for GraphStream should go into the now empty com.tinkerpop.gvz.graphstream.GraphStreamViewer. 

## Limitations by design

gvz is designed to be a debugging tool. From a design perspective, there is no need to visualize a mega graph with 1000+ nodes. The target user is looking to find the 10-100 nodes that reflect a bug in the Gremlin expression or the graph. The target user is not looking for a screenshot for his/her presentation -- there are other tools for that. 
