package model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * An implementation of the AbstractTableModel which adds functionality to add,
 * remove & retrieve value from a collection.
 * 
 * @author Alan Sheehan - R00111909
 *
 * @param <E> type the collection will hold
 */
public abstract class CustomAbstractTableModel <E> extends AbstractTableModel {

	private static final long serialVersionUID = -7018571647396004038L;
	
	
	/**
	 * Adds a value to the data
	 * 
	 * @param value to be added
	 */
	public abstract void add(E value);
	
	/**
	 * Retrieves a value from the data at a specified index
	 * 
	 * @param index of the value to be retrieved
	 * @return the value at that index
	 */
	public abstract E get(int index);
	
	/**
	 * Sets the value of the data at a given index to the given value
	 * 
	 * @param index to change
	 * @param value to change to
	 */
	public abstract void set(int index, E value);
	
	/**
	 * Returns the index of the passed in object. If object isn't found, 
	 * returns -1
	 * 
	 * @param obj to search for
	 * @return index of the object, -1 otherwise
	 */
	public abstract int find(E obj);
	
	/**
	 * Removes a value from the data at a specified index
	 * 
	 * @param index of the value to be removed
	 */
	public abstract void remove(int index);
	
	/**
	 * Clears all values from the data
	 */
	public abstract void clear();
	
	/**
	 * Sets the value of the current collection to the data passed in
	 * 
	 * @param data to be set
	 */
	public abstract void setData(ArrayList<E> data);
	
}
