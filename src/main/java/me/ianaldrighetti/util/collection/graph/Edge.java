package me.ianaldrighetti.util.collection.graph;

/**
 * This represents a single edge within a {@link Graph}. The edge is used to represent a connection between two
 * {@link Vertex}'s.<br />
 * <br />
 * <strong>Note:</strong> {@link Edge}'s are immutable.
 *
 * @param <V> The value stored within the vertices.
 * @param <W> The class used to represent the weight of an edge connecting vertices.
 */
public class Edge<V, W extends Comparable<W>> implements Comparable<Edge<V, W>>
{
	/**
	 * The graph this edge is within.
	 */
	private final Graph<V, W> graph;

	/**
	 * The unique ID of this edge, as assigned by the {@link Graph}.
	 */
	private final int id;

	/**
	 * The vertex this edge is going from.
	 */
	private final Vertex<V, W> from;

	/**
	 * The vertex this edge is going to.
	 */
	private final Vertex<V, W> to;

	/**
	 * If this edge is directed (as in you can get from the "from" to the "to" but not from the "to" to the "from" with
	 * this edge).
	 */
	private final boolean directed;

	/**
	 * The weight of this edge.
	 */
	private final W weight;

	/**
	 * Initializes an {@link Edge}.
	 *
	 * @param graph    The graph this edge is within.
	 * @param id       The unique ID of this edge.
	 * @param from     The vertex from which this edge begins.
	 * @param to       The vertex from which this edge ends.
	 * @param directed Whether this edge is directed.
	 * @param weight   The weight of the edge.
	 */
	Edge(Graph<V, W> graph, int id, Vertex<V, W> from, Vertex<V, W> to, boolean directed, W weight)
	{
		if (graph == null)
		{
			throw new IllegalArgumentException("The graph cannot be null.");
		}
		else if (from == null)
		{
			throw new IllegalArgumentException("The from Vertex cannot be null.");
		}
		else if (to == null)
		{
			throw new IllegalArgumentException("The to Vertex cannot be null.");
		}
		else if (weight == null)
		{
			throw new IllegalArgumentException("The weight cannot be null.");
		}

		this.graph = graph;
		this.id = id;
		this.from = from;
		this.to = to;
		this.directed = directed;
		this.weight = weight;
	}

	/**
	 * Returns the graph this edge is within.
	 *
	 * @return The {@link Graph} this edge is within.
	 */
	public Graph<V, W> getGraph()
	{
		return graph;
	}

	/**
	 * The unique ID of this edge, as assigned by it's {@link Graph}.
	 *
	 * @return The edge's unique ID.
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * The {@link Vertex} from which this edge originates.
	 *
	 * @return The {@link Vertex} this edge originates from.
	 */
	public Vertex<V, W> getFrom()
	{
		return from;
	}

	/**
	 * The {@link Vertex} that this edge connects to.
	 *
	 * @return The {@link Vertex} that this edge connects to.
	 */
	public Vertex<V, W> getTo()
	{
		return to;
	}

	/**
	 * Indicates whether this edge is directed.
	 *
	 * @return Returns true if this edge is directed, false otherwise.
	 */
	public boolean isDirected()
	{
		return directed;
	}

	/**
	 * Returns the weight of this edge.
	 *
	 * @return The weight of this edge.
	 */
	public W getWeight()
	{
		return weight;
	}

	/**
	 * Returns true if this {@link Edge} connects the two {@link Vertex}'s, which accounts for whether the edge is
	 * directed or not.
	 *
	 * @param from The originating Vertex.
	 * @param to   The destination Vertex.
	 * @return Returns true if this {@link Edge} connects the two.
	 */
	public boolean connects(final Vertex<V, W> from, Vertex<V, W> to)
	{
		if (from.getGraph() != graph || to.getGraph() != graph)
		{
			return false;
		}

		// If it's directed, it's a simpler check.
		if (directed)
		{
			return this.from.getId() == from.getId() && this.to.getId() == to.getId();
		}

		// Otherwise direction does not matter.
		return (this.from.getId() == from.getId() && this.to.getId() == to.getId()) || (this.from.getId() == to.getId() && this.to.getId() == from.getId());
	}

	/**
	 * Determines whether this {@link Edge} is greater than, less than or equal to another {@link Edge}.<br />
	 * <br />
	 * <strong>Note:</strong> This compares only the weight's of the two {@link Edge}'s. If the supplied object is null
	 * then -1 is always returned.
	 *
	 * @param right The other {@link Edge} to compare to.
	 * @return Returns 0 if the two object's are equal, a number greater than 0 if this object is greater than the other
	 * object and a number less than 0 if this object is less than the other object.
	 */
	public int compareTo(Edge<V, W> right)
	{
		if (right == null)
		{
			return 1;
		}
		else if (this == right)
		{
			return 0;
		}

		return getWeight().compareTo(right.getWeight());
	}
}
