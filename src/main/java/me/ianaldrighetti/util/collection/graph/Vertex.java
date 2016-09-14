package me.ianaldrighetti.util.collection.graph;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Set;

/**
 * This represents a single vertex (or node) within a {@link Graph}. The vertex stores a value which can be changed.
 *
 * @param <V> The value stored within the vertex.
 * @param <W> The class used to represent the weight of an edge connecting vertices.
 */
public class Vertex<V, W extends Comparable<W>>
{
	/**
	 * The graph where this vertex lives.
	 */
	private final Graph<V, W> graph;

	/**
	 * The unique ID the graph assigned this vertex.
	 */
	private final int id;

	/**
	 * The value this vertex contains, which may be null.
	 */
	private V value;

	/**
	 * Creates a new instance of a vertex.
	 *
	 * @param graph The graph this vertex is in.
	 * @param id    The unique ID of the vertex.
	 * @param value The value contained within the vertex.
	 */
	Vertex(final Graph<V, W> graph, final int id, final V value)
	{
		if (graph == null)
		{
			throw new IllegalArgumentException("The graph cannot be null.");
		}

		this.graph = graph;
		this.id = id;
		this.value = value;
	}

	/**
	 * Returns the {@link Graph} this vertex is in.
	 *
	 * @return The {@link Graph} this vertex is in.
	 */
	public Graph<V, W> getGraph()
	{
		return graph;
	}

	/**
	 * The unique ID of the vertex, as assigned by it's {@link Graph}.
	 *
	 * @return The unique ID of the vertex within the {@link Graph}.
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Returns the value stored within the vertex, if anything.
	 *
	 * @return The value stored within the vertex, which may be null.
	 */
	public V getValue()
	{
		return value;
	}

	/**
	 * Sets the value stored within the vertex.
	 *
	 * @param value The value to set the vertex to, which can be null.
	 */
	public void setValue(final V value)
	{
		this.value = value;
	}

	/**
	 * Returns all the {@link Edge}'s which originate from this {@link Vertex}, including those which are not directed.
	 *
	 * @return The {@link Edge}'s originating from this {@link Vertex}, including those which are not directed.
	 */
	public Set<Edge<V, W>> getEdges()
	{
		return graph.getVertexEdges(id);
	}

	/**
	 * Calculates the hash code for this vertex.
	 *
	 * @return The vertex's hash code, which is based on the graph and the vertex's unique ID.
	 */
	@Override
	public int hashCode()
	{
		return (new HashCodeBuilder()).append(graph).append(id).toHashCode();
	}

	/**
	 * Determines whether this {@link Vertex} is equal to another object.
	 *
	 * @param obj The object to check it's equality with.
	 * @return Returns <code>true</code> if the two object's are equal, <code>false</code> otherwise.
	 * Two vertex's are equal only if they are both in the same graph and have the same ID.
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (obj == null || !(obj instanceof Vertex<?, ?>))
		{
			return false;
		}
		else if (this == obj)
		{
			return true;
		}

		@SuppressWarnings("unchecked")
		Vertex<V, W> right = (Vertex<V, W>) obj;
		return (new EqualsBuilder()).append(graph, right.graph).append(id, right.id).isEquals();
	}
}
