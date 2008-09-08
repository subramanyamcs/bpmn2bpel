package translation.analysis.sese;
import java.util.Iterator;

/**
 * Implements a simple iterator without the remove method.
 * 
 * @author mainiero
 * 
 */
public abstract class UnmodifiableIterator implements Iterator {

	/**
	 * Returns <code>true</code> if the iteration has more elements.
	 * 
	 * @return <code>true</code> if the iterator has more elements.
	 */
	public abstract boolean hasNext();

	/**
	 * Returns the next element in the iteration.
	 * 
	 * @exception java.util.NoSuchElementException
	 *                iteration has no more elements.
	 */
	public abstract Object next();

	/**
	 * Always throws an <code>UnsupportedOperationException</code>.
	 * 
	 * @exception UnsupportedOperationException
	 *                always.
	 */
	public final void remove() {
		throw new UnsupportedOperationException("Unmodifiable Iterator");
	}
}
