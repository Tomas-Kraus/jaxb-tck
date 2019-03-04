/*
 * Copyright (c) 2001, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.tgxml.tools.elgen.javatest;

import java.lang.reflect.Array;

/**
 * A class that manipulates arrays of Objects (no primitives).
 * Currently, you can remove any element and have the array shrink itself,
 * and you can append an element and have it expand.
 * <em>Note: You should either initialize array variables to a zero length array
 * or use the append method that accepts the Class argument to prevent array
 * storage exceptions.  This is an issue when this class is forced to create a
 * new array of unknown type and determine the type from the item being appended.</em>
 * 
 *
 * @author Brian Kurotsuchi
 * @version @(#)DynamicArray.java	1.15 02/01/18
 */

public final class DynamicArray {
    /**
     * Append an object to the end of the array.
     *
     * @param oldArr The original array which we are appending to.  May be zero length or null.
     * @param newObj The new value to append to the oldArr.  Null is not a legal value.
     * @return A new array with the object appended to it.
     * @throws IllegalArgumentException If newObj is null.
     * @throws ArrayStoreException If there is a type mismatch between oldArr and newObj.
     */
    public static Object[] append(Object[] oldArr, Object newObj) {
        Object[] newArr;

        if (oldArr == null) {
            if (newObj != null) {
                newArr = (Object[])(Array.newInstance(newObj.getClass(), 1));
                newArr[0] = newObj;
            }
            else {
                throw new IllegalArgumentException("Cannot add null item to null array.");
            }
        } 
	else {
	    newArr = (Object[])(Array.newInstance(getArrayClass(oldArr), oldArr.length+1));
	    System.arraycopy(oldArr, 0, newArr, 0, oldArr.length);
	    newArr[newArr.length-1] = newObj;
        } 

        return newArr;
    }

    /**
     * Append an object to the end of the array.
     *
     * @param oldArr The original array which we are appending to.  May be zero length or null.
     * @param newObj The new value to append to the oldArr.
     * @param arrayClass If the oldArr is null, a new array of arrayClass will be created.
     *        If arrayClass is null, the type of newObj will be used to create the array.
     * @return A new array with the object appended to it.
     * @throws IllegalArgumentException If newObj is null.
     * @throws ArrayStoreException If there is a type mismatch between oldArr and newObj.
     */
    public static Object[] append(Object[] oldArr, Object newObj, Class arrayClass) {
	Object[] localArr;

	if (oldArr == null && arrayClass != null)
	    localArr = (Object[])(Array.newInstance(arrayClass, 0));
	else
	    localArr = oldArr;

	return append(localArr, newObj);
    }

    /**
     * Insert an object into the array at the specified index.
     *
     * @param oldArr The array to insert into.  May be null, in which case a new array
     *        will be created using the type of <code>newObj</code>.
     * @param newObj The object to insert.
     * @param location The index at which to insert the item.  An exception will occur
     *        if the location in out of range.  This location can be equal to
     *        <code>oldArr.length</code>, in which case this becomes an append
     *        operation.
     * @return A new array with the object inserted into it at the specified location.
     */
    public static Object[] insert(Object[] oldArr, Object newObj, int location) {
        Object[] newArr;

        if (oldArr == null) {
            if (newObj != null) {
                newArr = (Object[])(Array.newInstance(newObj.getClass(), 1));
                newArr[0] = newObj;
            }
            else {
                throw new IllegalArgumentException("Cannot add null item to null array.");
            }
        } 
	else {
            if (location > oldArr.length)
                throw new IllegalArgumentException("Index location too large (" + location +
                                                    ").");
	    newArr = (Object[])(Array.newInstance(getArrayClass(oldArr), oldArr.length+1));

            if (location == 0) {
                newArr[0] = newObj;
                System.arraycopy(oldArr, 0, newArr, 1, oldArr.length);
            } else {
                System.arraycopy(oldArr, 0, newArr, 0, location);
                newArr[location] = newObj;
                System.arraycopy(oldArr, location, newArr, location + 1,
                                 oldArr.length - location);
            }
        } 

        return newArr;
    }

    /**
     * Remove the object at the specified index.
     * The new array type is determined by the type of element 0 in the
     * original array.
     *
     * @param oldArr The original array which we are removing from.  May not be
     *        null or zero length.
     * @param index The index to remove from <code>oldArr</code>.  Exception
     *        will be thrown if it is out of range.
     * @return An array of the same type as the original array (element 0), without the
     *         given index.  Zero length array if the last element is removed.
     * @exception IllegalArgumentException Will occur if the given index is out of range,
     *            or the given array is null.
     * @exception NullPointerException May occur if the source array has null
     *          values, particularly in the first array position.
     * @exception ArrayStoreException May occur if all the objects in the
     *          array do not match.
     */
    public static Object[] remove(Object[] oldArr, int index) {
        Object[] newArr;

	if (oldArr == null)
	    throw new IllegalArgumentException("Cannot remove from null array.");
	else if (index > oldArr.length-1 || index < 0) {
	    // invalid index
	    throw new IllegalArgumentException("Index to remove from array is invalid (too small/large).");
	}
	else if (index == 0) {
	    // chop the head
	    newArr = (Object[])(Array.newInstance(getArrayClass(oldArr), oldArr.length-1));
	    System.arraycopy(oldArr, 1, newArr, 0, oldArr.length-1);
	} 
	else if (index == oldArr.length-1) {
	    // chop the tail
	    newArr = (Object[])(Array.newInstance(getArrayClass(oldArr), oldArr.length-1));
	    System.arraycopy(oldArr, 0, newArr, 0, oldArr.length-1);
	} 
	else {
	    // chop the middle
	    newArr = (Object[])(Array.newInstance(getArrayClass(oldArr), oldArr.length-1));
	    System.arraycopy(oldArr, 0, newArr, 0, index);
	    System.arraycopy(oldArr, index+1, newArr, index,
			     oldArr.length-index-1);
	}

        return newArr;
    }

    /**
     * Remove the object from the array.
     * If the object is not found, the original array is returned unchanged.
     *
     * @param oldArr The array to remove from.
     * @param victim The object to remove from the array.  May be null.
     * @return Zero length array if the last element is removed.
     */
    public static Object[] remove(Object[] oldArr, Object victim) {
        Object[] newArr;

        if (oldArr == null) {
            newArr = oldArr;
        } else {
            int location = find(oldArr, victim);
            if(location != -1) {
                newArr = remove(oldArr, location);    
            } else {
                // not found, return the original array
                newArr = oldArr;
            }
        }

        return newArr;
    }

    /**
     * Find the index of the object that is equal (reference) to the target.
     *
     * @param arr The data to search; should not be null.
     * @param target The reference to search for; can be null.
     * @return The array index of the target.
     */
    public static int find(Object[] arr, Object target) {
        int index = -1;

        for(int i = 0; i < arr.length; i++) {
            if(arr[i] == target) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * Get the type of objects that the array is declared to hold.
     *
     * @param arr The array to examine.
     * @return The class of objects that the given array can hold.
     */
    protected static Class getArrayClass(Object[] arr) {
        if(arr != null) {
            return arr.getClass().getComponentType();
        } else {
            return null;
        }
    }

}
 