package com.tinkerpop.gvz;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.lambdazen.bitsy.BitsyGraph;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.gvz.jung.JungViewer;
import com.tinkerpop.pipes.PipeFunction;

import junit.framework.TestCase;

public class GremlinPipelineAnalyzerTest extends TestCase {
	public String[][] KENNEDY_FAMILY_TREE = new String[][] {
			new String[] {"Joseph Patrick Kennedy, Sr.", 
					"male", "1888", "1969", "Joe",
					null, null, "Rose Elizabeth Fitzgerald"},
			new String[] {"Rose Elizabeth Fitzgerald",
					"female", "1890", "1995", null,
					null, null, null},
			new String[] {"Joseph Patrick Kennedy, Jr.",
					"male", "1915", "1944", "Joe Jr.",
					"Joseph Patrick Kennedy, Sr.", "Rose Elizabeth Fitzgerald",
					null},
			new String[] {"John Fitzgerald Kennedy, Sr.",
					"male", "1917", "1963", "Jack",
					"Joseph Patrick Kennedy, Sr.", "Rose Elizabeth Fitzgerald",
					"Jacqueline Lee Bouvier"},
			new String[] {"John Vernou Bouvier III", 
					"male", "1891", "1957", "Black Jack",
					null, null, "Janet Lee Bouvier"},
			new String[] {"Janet Lee Bouvier",
					"female", "1907", "1989", null,
					null, null, null},
			new String[] {"Jacqueline Lee Bouvier",
					"female", "1929", "1994", "Jackie",
					"John Vernou Bouvier III", "Janet Lee Bouvier",
					null},
			new String[] {"Rose Marie Kennedy", 
					"female", "1918", "2005", "Rosemary",
					"Joseph Patrick Kennedy, Sr.", "Rose Elizabeth Fitzgerald",
					null},
			new String[] {"Kathleen Agnes Kennedy",
					"female", "1920", "1948", "Kick",
					"Joseph Patrick Kennedy, Sr.", "Rose Elizabeth Fitzgerald",
					null},
			new String[] {"William John Robert Cavendish",
					"male", "1917", "1944", "Billy",
					"Edward William Spencer Cavendish", "Mary Cavendish",
					"Kathleen Agnes Kennedy"},
			new String[] {"Edward William Spencer Cavendish",
					"male", "1895", "1950", null,
					null, null, "Mary Cavendish"},
			new String[] {"Mary Cavendish", 
					"female", "1895", "1988", null,
					null, null, null},
			new String[] {"Eunice Mary Kennedy", 
					"female", "1921", "2009", null,
					"Joseph Patrick Kennedy, Sr.", "Rose Elizabeth Fitzgerald",
					null},
			new String[] {"Robert Sargent Shriver, Jr.",
					"male", "1915", "2011", "Sarge",
					"Robert Sargent Shriver, Sr.", "Hilda Shriver",
					"Eunice Mary Kennedy"},
			new String[] {"Robert Sargent Shriver, Sr.",
					"male", "1878", "1942", null,
					null, null, "Hilda Shriver"},
			new String[] {"Hilda Shriver",
					"female", "1883", "1977", null,
					null, null, null},
			new String[] {"Patricia Helen Kennedy",
					"female", "1924", "2006", "Pat",
					"Joseph Patrick Kennedy, Sr.", "Rose Elizabeth Fitzgerald",
					null},
			new String[] {"Peter Sydney Ernest Lawford",
					"male", "1923", "1984", null,
					"Sydney Turing Barlow Lawford", "May Sommerville Bunny",
					"Patricia Helen Kennedy"},
			new String[] {"Sydney Turing Barlow Lawford",
					"male", "1865", "1953", null, 
					null, null, "May Sommerville Bunny"},
			new String[] {"May Sommerville Bunny", 
					"female", "1883", "1972", null,
					null, null, null},
			new String[] {"Robert Francis Kennedy, Sr.",
					"male", "1925", "1968", "Bobby",
					"Joseph Patrick Kennedy, Sr.", "Rose Elizabeth Fitzgerald",
					"Ethel Skakel Kennedy"},
			new String[] {"Ethel Skakel Kennedy", 
					"female", "1928", null, null,
					"George Skakel", "Ann Brannack",
					null},
			new String[] {"George Skakel",
					"male", "1892", "1955", null,
					null, null, "Ann Brannack"},
			new String[] {"Ann Brannack",
					"female", "1892", "1955", null,
					null, null, null},
			new String[] {"Jean Ann Kennedy",
					"female", "1928", null, null,
					"Joseph Patrick Kennedy, Sr.", "Rose Elizabeth Fitzgerald",
					null},
			new String[] {"Stephen Edward Smith Sr.",
					"male", "1927", "1990", null,
					null, null,
					"Jean Ann Kennedy"},
			new String[] {"Edward Moore Kennedy, Sr.",
					"male", "1932", "2009", "Ted",
					"Joseph Patrick Kennedy, Sr.", "Rose Elizabeth Fitzgerald",
					"Virginia Joan Kennedy"},
			new String[] {"Virginia Joan Kennedy",
					"female", "1936", null, "Joan",
					"Henry Wiggin Bennett, Jr.", "Virginia Joan Bennet",
					null},
			new String[] {"Henry Wiggin Bennett, Jr.",
					"male", "1895", "1981", null,
					null, null, "Virginia Joan Bennet"},
			new String[] {"Virginia Joan Bennet",
					"female", "1900", "1975", null,
					null, null, null},
			new String[] {"Caroline Bouvier Kennedy",
					"female", "1957", null, null,
					"John Fitzgerald Kennedy, Sr.", "Jacqueline Lee Bouvier",
					null},
			new String[] {"Edwin Arthur Schlossberg",
					"male", "1945", null, null,
					null, null,
					"Caroline Bouvier Kennedy"},
			new String[] {"John Fitzgerald Kennedy, Jr.",
					"male", "1960", "1999", "John-John",
					"John Fitzgerald Kennedy, Sr.", "Jacqueline Lee Bouvier",
					"Carolyn Jeanne Bessette"},
			new String[] {"Carolyn Jeanne Bessette", 
					"female", "1966", "1999", null,
					null, null, null},
			new String[] {"Robert Sargent Shriver III",
					"male", "1954", null, "Bobby",
					"Robert Sargent Shriver, Jr.", "Eunice Mary Kennedy",
					"Malissa Feruzzi"},
			new String[] {"Malissa Feruzzi",
					"female", "1963", null, "Mary Elizabeth",
					null, null, null},
			new String[] {"Maria Owings Shriver",
					"female", "1955", null, null,
					"Robert Sargent Shriver, Jr.", "Eunice Mary Kennedy",
					null},
			new String[] {"Arnold Schwarzenegger",
					"male", "1947", null, null,
					null, null,
					"Maria Owings Shriver"},
			new String[] {"Timothy Perry Shriver",
					"male", "1959", null, null,
					"Robert Sargent Shriver, Jr.", "Eunice Mary Kennedy",
					"Linda Sophia Potter"},
			new String[] {"Linda Sophia Potter", 
					"female", "1956", null, null,
					null, null, null},
			new String[] {"Mark Kennedy Shriver",
					"male", "1964", null, null,
					"Robert Sargent Shriver, Jr.", "Eunice Mary Kennedy",
					"Jeanne Eileen Ripp"},
			new String[] {"Jeanne Eileen Ripp", 
					"female", "1965", null, "Jeannie",
					null, null, null},
			new String[] {"Anthony Paul Kennedy Shriver",
					"male", "1965", null, null,
					"Robert Sargent Shriver, Jr.", "Eunice Mary Kennedy",
					"Alina Mojica"},
			new String[] {"Alina Mojica",
					"female", "1965", null, null,
					null, null, null},
			new String[] {"Christopher Kennedy Lawford",
					"male", "1955", null, null,
					"Peter Sydney Ernest Lawford", "Patricia Helen Kennedy",
					null},
			new String[] {"Sydney Maleia Kennedy Lawford",
					"female", "1956", null, null,
					"Peter Sydney Ernest Lawford", "Patricia Helen Kennedy",
					null},
			new String[] {"James Peter McKelvy, Sr",
					"male", "1955", null, null,
					null, null,
					"Sydney Maleia Kennedy Lawford"},
			new String[] {"Victoria Francis Lawford",
					"female", "1958", null, null,
					"Peter Sydney Ernest Lawford", "Patricia Helen Kennedy",
					null},
			new String[] {"Robert Beebe Pender, Jr.",
					"male", "1953", null, null,
					null, null,
					"Victoria Francis Lawford"},
			new String[] {"Robin Elizabeth Lawford",
					"female", "1961", null, null,
					"Peter Sydney Ernest Lawford", "Patricia Helen Kennedy",
					null},
			new String[] {"Stephen Edward Smith, Jr.",
					"male", "1957", null, null,
					"Stephen Edward Smith Sr.", "Jean Ann Kennedy",
					null},
			new String[] {"William Kennedy Smith", 
					"male", "1960", null, null,
					"Peter Sydney Ernest Lawford", "Patricia Helen Kennedy",
					"Anne Henry"},
			new String[] {"Anne Henry",
					"female", "1975", null, null,
					null, null, null},
			new String[] {"Kara Anne Kennedy",
					"female", "1960", null, null,
					"Edward Moore Kennedy, Sr.", "Virginia Joan Kennedy",
					null},
			new String[] {"Edward Moore Kennedy, Jr.",
					"male", "1961", null, "Ted",
					"Edward Moore Kennedy, Sr.", "Virginia Joan Kennedy",
					"Katherine Anne Gershman"},
			new String[] {"Katherine Anne Gershman",
					"female", "1959", null, "Kiki",
					null, null, null},
			new String[] {"Patrick Joseph Kennedy II",
					"male", "1967", null, null,
					"Edward Moore Kennedy, Sr.", "Virginia Joan Kennedy",
					"Amy Savell"},
			new String[] {"Amy Savell",
					"female", "1979", null, null,
					null, null, null}
	};
	
	BitsyGraph graph;
	
	@Override
	public void setUp() {
		graph = new BitsyGraph();
		graph.createKeyIndex("name", Vertex.class);
		
		loadGraph();
	}
	
	@Override
	public void tearDown() {
		if (graph != null) {
			graph.shutdown();
		}
		graph = null;
	}
	
	public void loadGraph() {
		for (String[] kennedy : KENNEDY_FAMILY_TREE) {
			Vertex v = graph.addVertex(null);
			
			v.setProperty("name", kennedy[0]);
			v.setProperty("sex", kennedy[1]);
			v.setProperty("born", new Integer(kennedy[2]));
			
			if (kennedy[3] != null) {
				v.setProperty("died", new Integer(kennedy[3]));
			}
			
			if (kennedy[4] != null) {
				v.setProperty("nickname", kennedy[4]);
			}
		}
		
		for (String[] kennedy : KENNEDY_FAMILY_TREE) {
			Vertex v = graph.getVertices("name", kennedy[0]).iterator().next();
			
			assertTrue(v != null);
			
			addRelationship(v, "father", kennedy[5]);
			addRelationship(v, "mother", kennedy[6]);
			addRelationship(v, "wife", kennedy[7]);
		}
		
		graph.commit();		
	}

	private void addRelationship(Vertex v, String label, String name) {
		if (name != null) {
			try {
				Vertex other = graph.getVertices("name", name).iterator().next();			
				assertTrue(other != null);
			
				if (label.equals("father")) {
					assertEquals("for " + name, "male", other.getProperty("sex"));
				} else if (label.equals("mother")) {
					assertEquals("for " + name, "female", other.getProperty("sex"));
				} else if (label.equals("wife")) {
					assertEquals("for " + name + " with " + other.getProperty("sex").getClass(), "female", other.getProperty("sex"));
				}
	
				v.addEdge(label, other);
			} catch (NoSuchElementException e) {
				throw new RuntimeException("Unable to add " + label + " link to " + name, e);
			}
		}
	}

	public void testOneInStep() {
		GremlinPipeline pipeline = new GremlinPipeline(graph).V("name", "Joseph Patrick Kennedy, Sr.").in("father");
		System.out.println("Pipeline: " + pipeline);
		
		GremlinPipelineAnalyzer pa = new GremlinPipelineAnalyzer(pipeline);
		
		Iterator<Vertex> iter = pa.getGraph().getVertices("name", "Joseph Patrick Kennedy, Sr.").iterator();
		assertTrue(iter.hasNext());
		Vertex jfkSr = iter.next();
		assertEquals("Joseph Patrick Kennedy, Sr.", jfkSr.getProperty("name"));
		assertEquals(new Integer(1969), jfkSr.getProperty("died"));

		// The sub-graph should not pick up the wife
		iter = new GremlinPipeline(pa.getGraph()).V("name", "Joseph Patrick Kennedy, Sr.").out("wife").iterator();
		assertFalse(iter.hasNext());
		
		// The sub-graph should have all of JFK Sr. children
		iter = new GremlinPipeline(pa.getGraph()).V("name", "Joseph Patrick Kennedy, Sr.").both().iterator();

		int counter = 0;
		while (iter.hasNext()) {
			Vertex v = iter.next();
			counter++;
		}

		assertEquals(9, counter);

		assertEquals(2, pa.getExprNames().size());
		System.out.println(pa.getExprNames());
		
		assertEquals(1, pa.getMarkedVertices((String)(pa.getExprNames().get(0))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(0))).size());
		assertEquals(9, pa.getMarkedVertices((String)(pa.getExprNames().get(1))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(1))).size());
	}

	public void testTwoInEOutVSteps() {
		GremlinPipeline pipeline = new GremlinPipeline(graph).V("name", "Joseph Patrick Kennedy, Sr.").inE("father").outV();
		System.out.println("Pipeline: " + pipeline);
		
		GremlinPipelineAnalyzer pa = new GremlinPipelineAnalyzer(pipeline);
		
		Iterator<Vertex> iter = pa.getGraph().getVertices("name", "Joseph Patrick Kennedy, Sr.").iterator();
		assertTrue(iter.hasNext());
		Vertex jfkSr = iter.next();
		assertEquals("Joseph Patrick Kennedy, Sr.", jfkSr.getProperty("name"));
		assertEquals(new Integer(1969), jfkSr.getProperty("died"));

		// The sub-graph should not pick up the wife
		iter = new GremlinPipeline(pa.getGraph()).V("name", "Joseph Patrick Kennedy, Sr.").out("wife").iterator();
		assertFalse(iter.hasNext());
		
		// The sub-graph should have all of JFK Sr. children
		iter = new GremlinPipeline(pa.getGraph()).V("name", "Joseph Patrick Kennedy, Sr.").both().iterator();

		int counter = 0;
		while (iter.hasNext()) {
			Vertex v = iter.next();
			counter++;
		}

		assertEquals(9, counter);

		assertEquals(3, pa.getExprNames().size());
		System.out.println(pa.getExprNames());
		
		assertEquals(1, pa.getMarkedVertices((String)(pa.getExprNames().get(0))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(0))).size());
		assertEquals(0, pa.getMarkedVertices((String)(pa.getExprNames().get(1))).size());
		assertEquals(9, pa.getMarkedEdges((String)(pa.getExprNames().get(1))).size());
		assertEquals(9, pa.getMarkedVertices((String)(pa.getExprNames().get(2))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(2))).size());
	}

	public void testFourInEOutVFilterCollectSteps() throws Exception {
		GremlinPipeline pipeline = new GremlinPipeline(graph).V("name", "Joseph Patrick Kennedy, Sr.").inE("father").outV()
				.filter(new PipeFunction<Vertex, Boolean>() {
					@Override
					public Boolean compute(Vertex v) {
						return v.getProperty("sex").equals("male");
					}
				}).property("name");
		System.out.println("Pipeline: " + pipeline);
		
		GremlinPipelineAnalyzer pa = new GremlinPipelineAnalyzer(pipeline);
		
		Iterator<Vertex> iter = pa.getGraph().getVertices("name", "Joseph Patrick Kennedy, Sr.").iterator();
		assertTrue(iter.hasNext());
		Vertex jfkSr = iter.next();
		assertEquals("Joseph Patrick Kennedy, Sr.", jfkSr.getProperty("name"));
		assertEquals(new Integer(1969), jfkSr.getProperty("died"));

		// The sub-graph should not pick up the wife
		iter = new GremlinPipeline(pa.getGraph()).V("name", "Joseph Patrick Kennedy, Sr.").out("wife").iterator();
		assertFalse(iter.hasNext());
		
		// The sub-graph should have all of JFK Sr. children
		iter = new GremlinPipeline(pa.getGraph()).V("name", "Joseph Patrick Kennedy, Sr.").both().iterator();

		int counter = 0;
		while (iter.hasNext()) {
			Vertex v = iter.next();
			counter++;
		}

		assertEquals(9, counter);

		assertEquals(4, pa.getExprNames().size());
		System.out.println(pa.getExprNames());
		
		assertEquals(1, pa.getMarkedVertices((String)(pa.getExprNames().get(0))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(0))).size());
		assertEquals(0, pa.getMarkedVertices((String)(pa.getExprNames().get(1))).size());
		assertEquals(9, pa.getMarkedEdges((String)(pa.getExprNames().get(1))).size());
		assertEquals(9, pa.getMarkedVertices((String)(pa.getExprNames().get(2))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(2))).size());
		assertEquals(4, pa.getMarkedVertices((String)(pa.getExprNames().get(3))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(3))).size());
		
//		JungViewer jv = new JungViewer(pa);
//		jv.slideshow();
	}

	public void testFourInEOutVWifeCollectSteps() throws Exception {
		GremlinPipeline pipeline = new GremlinPipeline(graph).V("name", "Joseph Patrick Kennedy, Sr.").inE("father").outV()
				.filter(new PipeFunction<Vertex, Boolean>() {
					@Override
					public Boolean compute(Vertex v) {
						return v.getProperty("sex").equals("male");
					}
				}).out("wife");
		System.out.println("Pipeline: " + pipeline);
		
		GremlinPipelineAnalyzer pa = new GremlinPipelineAnalyzer(pipeline);
		
		Iterator<Vertex> iter = pa.getGraph().getVertices("name", "Joseph Patrick Kennedy, Sr.").iterator();
		assertTrue(iter.hasNext());
		Vertex jfkSr = iter.next();
		assertEquals("Joseph Patrick Kennedy, Sr.", jfkSr.getProperty("name"));
		assertEquals(new Integer(1969), jfkSr.getProperty("died"));

		// The sub-graph should not pick up the wife
		iter = new GremlinPipeline(pa.getGraph()).V("name", "Joseph Patrick Kennedy, Sr.").out("wife").iterator();
		assertFalse(iter.hasNext());
		
		// The sub-graph should have all of JFK Sr. children
		iter = new GremlinPipeline(pa.getGraph()).V("name", "Joseph Patrick Kennedy, Sr.").both().iterator();

		int counter = 0;
		while (iter.hasNext()) {
			Vertex v = iter.next();
			counter++;
		}

		assertEquals(9, counter);

		assertEquals(5, pa.getExprNames().size());
		System.out.println(pa.getExprNames());
		
		assertEquals(1, pa.getMarkedVertices((String)(pa.getExprNames().get(0))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(0))).size());
		assertEquals(0, pa.getMarkedVertices((String)(pa.getExprNames().get(1))).size());
		assertEquals(9, pa.getMarkedEdges((String)(pa.getExprNames().get(1))).size());
		assertEquals(9, pa.getMarkedVertices((String)(pa.getExprNames().get(2))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(2))).size());
		assertEquals(4, pa.getMarkedVertices((String)(pa.getExprNames().get(3))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(3))).size());
		assertEquals(3, pa.getMarkedVertices((String)(pa.getExprNames().get(4))).size());
		assertEquals(0, pa.getMarkedEdges((String)(pa.getExprNames().get(4))).size());

		JungViewer jv = new JungViewer(pa);
		jv.slideshow();
	}
}
