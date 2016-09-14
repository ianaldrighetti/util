package me.ianaldrighetti.util.collection.graph;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Provides an implementation of a graph.<br />
 * <br />
 * <strong>Note:</strong> Graph's are not thread-safe.
 *
 * @param <V> The value stored within each vertex (node).
 * @param <W> The class which will be used to describe the weight of an edge.
 */
public class Graph<V, W extends Comparable<W>>
{
	/**
	 * A set containing all vertices within the graph.
	 */
	private final Map<Integer, Vertex<V, W>> vertices;

	/**
	 * A map containing the edge's for each vertex. The key is the vertex's ID, the value being a set containing the
	 * vertexEdges that are going from the vertex.
	 */
	private final Map<Integer, Set<Edge<V, W>>> vertexEdges;

	/**
	 * A map containing all edge's within this graph. The key is the edge's unique ID and the value is the edge itself.
	 */
	private final Map<Integer, Edge<V, W>> edges;

	/**
	 * The next ID to assign to a new vertex.
	 */
	private int nextVertexId;

	/**
	 * The next ID to assign to a new edge.
	 */
	private int nextEdgeId;

	/**
	 * Initializes an empty graph.
	 */
	public Graph()
	{
		vertices = new TreeMap<>();
		vertexEdges = new TreeMap<>();
		edges = new TreeMap<>();
	}

	/**
	 * Creates a new {@link Vertex} within the graph.
	 *
	 * @param value The value to put in the new vertex.
	 * @return The {@link Vertex} that was created.
	 */
	public Vertex<V, W> createVertex(final V value)
	{
		final Vertex<V, W> vertex = new Vertex<>(this, nextVertexId++, value);
		vertices.put(vertex.getId(), vertex);
		vertexEdges.put(vertex.getId(), new TreeSet<Edge<V, W>>());

		return vertex;
	}

	/**
	 * Returns the {@link Vertex}, located by it's unique ID.
	 *
	 * @param id The unique ID of the {@link Vertex}.
	 * @return The {@link Vertex} with the specified ID, or null if the ID does not exist.
	 */
	public Vertex<V, W> getVertex(final int id)
	{
		return vertices.get(id);
	}

	/**
	 * Deletes the {@link Vertex} from the graph, which includes removing any {@link Edge}'s that go from or to this
	 * {@link Vertex}.
	 *
	 * @param vertex The vertex to remove.
	 */
	public void delete(final Vertex<V, W> vertex)
	{
		if (vertex.getGraph() != this)
		{
			throw new IllegalArgumentException("The specified Vertex does not exist within this graph.");
		}

		deleteVertex(vertex.getId());
	}

	/**
	 * Deletes the {@link Vertex} from the graph, using it's unique ID. This will also remove any {@link Edge}'s which
	 * go to or from this {@link Vertex}.
	 *
	 * @param id The ID of the vertex.
	 */
	public void deleteVertex(int id)
	{
		if (!vertices.containsKey(id))
		{
			return;
		}

		// Remove the vertex.
		vertices.remove(id);

		// Then remove any edges for this vertex.
		for (Edge<V, W> edge : vertexEdges.get(id))
		{
			int fromId = edge.getFrom().getId();
			int toId = edge.getTo().getId();

			// We're going to remove the id's entry in the hash map, but we need to remove this edge from the connecting
			// vertex as well.
			int removeId = fromId == id ? toId : fromId;
			vertexEdges.get(removeId).remove(edge);
		}

		// Now remove this entry.
		vertexEdges.remove(id);
	}

	/**
	 * Returns the edges which originate from the specified Vertex, including those which are undirected.
	 *
	 * @param vertex The vertex.
	 * @return The edges from this vertex, if any.
	 */
	public Set<Edge<V, W>> getVertexEdges(final Vertex<V, W> vertex)
	{
		if (vertex.getGraph() != this)
		{
			throw new IllegalArgumentException("The vertex is not in this graph.");
		}

		return getVertexEdges(vertex.getId());
	}

	/**
	 * Returns the edges which originate from the specified Vertex, including those which are undirected.
	 *
	 * @param id The ID of the vertex.
	 * @return The edges from this vertex, if any.
	 */
	public Set<Edge<V, W>> getVertexEdges(final int id)
	{
		if (!vertexEdges.containsKey(id))
		{
			return Collections.emptySet();
		}

		final Set<Edge<V, W>> edges = new TreeSet<>();
		for (final Edge<V, W> edge : vertexEdges.get(id))
		{
			// If the edge is not directed, add it.
			if (!edge.isDirected())
			{
				edges.add(edge);
			}
			// Otherwise if it is directed, the from ID must match.
			else if (edge.getFrom().getId() == id)
			{
				edges.add(edge);
			}
		}

		return edges;
	}

	/**
	 * Creates an edge between to {@link Vertex}'s.
	 *
	 * @param from     The vertex from which the edge begins.
	 * @param to       The vertex to which the edge ends.
	 * @param weight   The weight of the edge.
	 * @param directed Whether the edge is directed.
	 * @return The {@link Edge} representing the connection between the two {@link Vertex}'s.
	 *
	 * @throws IllegalArgumentException if either the from or to {@link Vertex} do not exist within this graph.
	 */
	public Edge<V, W> connect(Vertex<V, W> from, Vertex<V, W> to, W weight, boolean directed)
	{
		if (from.getGraph() != this)
		{
			throw new IllegalArgumentException("The from Vertex does not exist within this graph.");
		}
		else if (to.getGraph() != this)
		{
			throw new IllegalArgumentException("The to Vertex does not exist within this graph.");
		}

		final Edge<V, W> edge = createEdge(from, to, weight, directed);
		edges.put(edge.getId(), edge);

		// For performance reasons of removeVertex, we add this to the to and from's set.
		vertexEdges.get(from.getId()).add(edge);
		vertexEdges.get(to.getId()).add(edge);

		return edge;
	}

	/**
	 * Deletes this edge by disconnecting the to and from vertex's.
	 *
	 * @param edge The edge to disconnect.
	 * @throws IllegalArgumentException if this edge is not within this graph.
	 */
	public void disconnect(Edge<V, W> edge)
	{
		if (edge.getGraph() != this)
		{
			throw new IllegalArgumentException("The Edge does not exist within this graph.");
		}

		disconnect(edge.getId());
	}

	/**
	 * Deletes the edge, as located by it's unique ID, from the graph.
	 *
	 * @param id The unique ID of the edge.
	 */
	public void disconnect(int id)
	{
		if (vertexEdges.containsKey(id))
		{
			return;
		}

		// Get the edge, then remove it.
		final Edge<V, W> edge = edges.get(id);
		edges.remove(id);

		// Now remove this edge from it's vertices.
		vertexEdges.get(edge.getFrom().getId()).remove(edge);
		vertexEdges.get(edge.getTo().getId()).remove(edge);
	}

	/**
	 * Indicates whether the two {@link Vertex}'s are connected.
	 *
	 * @param from The originating Vertex.
	 * @param to   The destination Vertex.
	 * @return Returns true if there is any edge connecting the two Vertex's. This means there was either an edge which
	 * is directed with the from on the edge equal to the specified from or an undirected edge.
	 */
	public boolean isConnected(Vertex<V, W> from, Vertex<V, W> to)
	{
		if (from.getGraph() != this || to.getGraph() != this)
		{
			return false;
		}

		// Because vertexEdges contains any edge, to or from (directed or not), we only need to check one.
		for (final Edge<V, W> edge : vertexEdges.get(from.getId()))
		{
			if (!edge.connects(from, to))
			{
				continue;
			}

			return true;
		}

		return false;
	}

	/**
	 * Returns a set containing all the {@link Edge}'s that connect the two {@link Vertex}'s.
	 *
	 * @param from The originating Vertex.
	 * @param to   The destination Vertex.
	 * @return Returns all {@link Edge}'s connecting the two {@link Vertex}'s, including undirected {@link Edge}'s.
	 */
	public Set<Edge<V, W>> getConnectingEdges(Vertex<V, W> from, Vertex<V, W> to)
	{
		if (from.getGraph() != this || to.getGraph() != this)
		{
			return Collections.emptySet();
		}

		final Set<Edge<V, W>> edges = new TreeSet<>();
		for (final Edge<V, W> edge : vertexEdges.get(from.getId()))
		{
			if (!edge.connects(from, to))
			{
				continue;
			}

			edges.add(edge);
		}

		return edges;
	}

	/**
	 * Creates an {@link Edge} connecting to {@link Vertex}'s.
	 *
	 * @param from     The from Vertex.
	 * @param to       The to Vertex.
	 * @param weight   The weight of this edge.
	 * @param directed Whether this edge is directed.
	 * @return The {@link Edge}.
	 */
	private Edge<V, W> createEdge(final Vertex<V, W> from, Vertex<V, W> to, W weight, boolean directed)
	{
		return new Edge<>(this, nextEdgeId++, from, to, directed, weight);
	}
}
