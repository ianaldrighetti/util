package me.ianaldrighetti.util.collection;


public class Tree<K extends Comparable<K>, V>
{
	private Node<K, V> root;

	public Tree()
	{
	}

	public void add(final K key, final V value)
	{
		if (root == null) {
			root = Node.valueOf(key, value);
		}

		add(key, value, root);
	}

	private void add(final K key, final V value, final Node<K, V> node)
	{
		int result = key.compareTo(node.key);
	}

	/**
	 * Represents a node within the tree.
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class Node<K extends Comparable<K>, V>
	{
		private K key;
		private V value;
		private Node<K, V> left;
		private Node<K, V> right;

		public K getKey()
		{
			return key;
		}

		public void setKey(final K key)
		{
			this.key = key;
		}

		public V getValue()
		{
			return value;
		}

		public void setValue(final V value)
		{
			this.value = value;
		}

		public Node<K, V> getLeft()
		{
			return left;
		}

		public void setLeft(final Node<K, V> left)
		{
			this.left = left;
		}

		public Node<K, V> getRight()
		{
			return right;
		}

		public void setRight(final Node<K, V> right)
		{
			this.right = right;
		}

		public static <K extends Comparable<K>, V> Node<K, V> valueOf(final K key, final V value)
		{
			final Node<K, V> node = new Node<>();
			node.setKey(key);
			node.setValue(value);

			return node;
		}

		public static <K extends Comparable<K>, V> Node<K, V> valueOf(final K key, final V value, Node<K, V> left, Node<K, V> right)
		{
			final Node<K, V> node = new Node<>();
			node.setKey(key);
			node.setValue(value);
			node.setLeft(left);
			node.setRight(right);

			return node;
		}
	}
}
