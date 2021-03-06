/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jem.internal.proxy.remote;
/*


 */

import java.lang.reflect.Array;
import java.util.logging.Level;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.jem.internal.proxy.common.CommandException;
import org.eclipse.jem.internal.proxy.common.remote.CommandErrorException;
import org.eclipse.jem.internal.proxy.common.remote.Commands;
import org.eclipse.jem.internal.proxy.common.remote.Commands.ValueObject;
import org.eclipse.jem.internal.proxy.core.*;
/**
 * IDE VM version of the Array proxy
 */

public final class REMArrayBeanProxy extends REMBeanProxy implements IArrayBeanProxy {
	private int fLength = -1;
	
	REMArrayBeanProxy(REMProxyFactoryRegistry aRegistry, Integer anID, IBeanTypeProxy aType) {
		super(aRegistry, anID, aType);
	}

	public boolean equals(Object anObject) {
		if (!(anObject instanceof IArrayBeanProxy))
			return false;
		if (sameAs((IBeanProxy) anObject))
			return true;	// Identity
		if (((IBeanProxy) anObject).getProxyFactoryRegistry() == fFactory)
			try {
				// The other is an array also, let the server do the check. We will use Arrays.equals.
				return ((IBooleanBeanProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperEquals().invoke(null, new IBeanProxy[] {this, (IBeanProxy) anObject})).booleanValue();
			} catch (ThrowableProxy e) {
				ProxyPlugin.getPlugin().getLogger().log(e, Level.INFO);
			}
		return false;
	}	
	
	/**
	 * Get the object at the specified index.
	 */
	public IBeanProxy get(int index) throws ThrowableProxy {
		// Format of getter parms is: (Object (array), int (index)).
		Object[] parms = new Object[] {this, new Integer(index)};
		// If the component type is primitive, we need to use the correct getter so that a valid value is returned,
		// if we just used the standard get, if the type was "int" the value returned would be an java.lang.Integer.
		IBeanTypeProxy compType = ((IArrayBeanTypeProxy) getTypeProxy()).getComponentType();
		if (!compType.isPrimitive()) {
			// Use standard getter.
			return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayGet()).invokeWithParms(null, parms);
		} else {
			// Use the correct primitive getter.
			int id = ((IREMBeanProxy) compType).getID().intValue();
			switch (id) {
				case Commands.BYTE_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayGetByte()).invokeWithParms(null, parms);
				case Commands.BOOLEAN_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayGetBoolean()).invokeWithParms(null, parms);
				case Commands.CHARACTER_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayGetChar()).invokeWithParms(null, parms);
				case Commands.DOUBLE_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayGetDouble()).invokeWithParms(null, parms);
				case Commands.FLOAT_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayGetFloat()).invokeWithParms(null, parms);
				case Commands.INTEGER_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayGetInt()).invokeWithParms(null, parms);
				case Commands.LONG_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayGetLong()).invokeWithParms(null, parms);
				case Commands.SHORT_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayGetShort()).invokeWithParms(null, parms);
			}
		}
		
		return null;
	}

	/**
	 * Get the object at the specified multi-dimensional index.
	 * The array must be at least the number of dimensions specified,
	 * and each index along the way must exist.
	 * The number of dimensions can't be greater than the number
	 * of dimensions of the real object.
	 */
	public IBeanProxy get(int [] indexes) throws ThrowableProxy {
		// Format of getter parms is: (Object (array), int[] (indexes)).
		Object[] parms = new Object[] {this, indexes};
		// If the component type at the specified dimension is primitive, we need to use the correct getter so that a valid value is returned,
		// if we just used the standard get, if the type was "int" the value returned would be an java.lang.Integer.
		int dims = ((IArrayBeanTypeProxy) getTypeProxy()).getDimensions();
		if (dims < indexes.length)
			throw new IllegalArgumentException();	// Too many dimensions
		IBeanTypeProxy finalCompType = ((IArrayBeanTypeProxy) getTypeProxy()).getFinalComponentType();			
		if (dims == indexes.length && finalCompType.isPrimitive()) {
			// Use the correct primitive getter because we are accessing the final component and it is primitive.
			int id = ((IREMBeanProxy) finalCompType).getID().intValue();
			switch (id) {
				case Commands.BYTE_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperGetByte()).invokeWithParms(null, parms);
				case Commands.BOOLEAN_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperGetBoolean()).invokeWithParms(null, parms);
				case Commands.CHARACTER_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperGetChar()).invokeWithParms(null, parms);
				case Commands.DOUBLE_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperGetDouble()).invokeWithParms(null, parms);
				case Commands.FLOAT_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperGetFloat()).invokeWithParms(null, parms);
				case Commands.INTEGER_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperGetInt()).invokeWithParms(null, parms);
				case Commands.LONG_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperGetLong()).invokeWithParms(null, parms);
				case Commands.SHORT_TYPE:
					return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperGetShort()).invokeWithParms(null, parms);
			}
		} else {
			// It is not the final component, or it is not primitive, use the standard accessor.
			return ((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperGet()).invokeWithParms(null, parms);
		}
		
		return null;
	}
	
	/**
	 * Set the object at the specified index.
	 */
	public void set(IBeanProxy value, int index) throws ThrowableProxy {
		// Format of setter parms is: (Object (array), int (index), Object (value)).
		Object[] parms = new Object[] {this, new Integer(index), value};
		((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArraySet()).invokeWithParms(null, parms);
	}
		
	/**
	 * Set the object at the specified multi-dimensional index.
	 * The array must be at least the number of dimensions specified,
	 * and each index along the way must exist.
	 * The number of dimensions can't be greater than the number
	 * of dimensions of the real object.
	 */
	public void set(IBeanProxy value, int [] indexes) throws ThrowableProxy {
		// Format of setter parms is: (Object (array), int[] (indexes), Object (value)).
		Object[] parms = new Object[] {this, indexes, value};
		((IREMMethodProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayHelperSet()).invokeWithParms(null, parms);
	}
	
	/**
	 * Get the length of the first dimension of this array.
	 * If there are multi-dimensions, you must get the appropriate
	 * dimension from the get method to see the size of that dimension.
	 *
	 * e.g.
	 *    int [3] returns 3
	 *    int [3][2] returns 3
	 *
	 *    ((IArrayBeanProxy) get(1)).getLength() returns 2
	 *    Since arrays do not have to be homogenous, there could
	 *    be a different length array for each of the arrays 
	 *    returned from the first dimension, the returned length
	 *    from get(2) and get(3) could result in a different value
	 *    from get(1).
	 */
	public int getLength() {
		if (fLength == -1) {
			// It hasn't yet been quiered.
			fLength = ((IIntegerBeanProxy) REMStandardBeanProxyConstants.getConstants(fFactory).getArrayGetLength().invokeCatchThrowableExceptions(null, this)).intValue();
		}
		return fLength;
	}	
	
	public IBeanProxy getCatchThrowableException(int index){
		try {
			return get(index);
		} catch (ThrowableProxy e) {
			ProxyPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, ProxyPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e));		 //$NON-NLS-1$
			fFactory.releaseProxy(e);	// Since it's no longer needed, get rid of now instead of GC time.
			return null;
		}		
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.core.IArrayBeanProxy#getSnapshot()
	 */
	public IBeanProxy[] getSnapshot() throws ThrowableProxy {
		IREMConnection connect = fFactory.getFreeConnection();
		REMStandardBeanProxyFactory proxyFactory = (REMStandardBeanProxyFactory) fFactory.getBeanProxyFactory();
		proxyFactory.startTransaction(); // This is definately a transaction, so start it.
		try {
			Commands.ValueObject returnValue = new Commands.ValueObject();
			try {
				try {
					connect.getArrayContents(getID().intValue(), returnValue);
					return processReturnValue(connect, returnValue);
				} catch (CommandErrorException e) {
					proxyFactory.processErrorReturn(e);
				}
			} catch (CommandException e) {
				if (!e.isRecoverable()) {
					// Close the connection and try again.
					fFactory.closeConnection(connect);
					connect = null;
					connect = fFactory.getFreeConnection();
					try {
						connect.getArrayContents(getID().intValue(), returnValue);
						return processReturnValue(connect, returnValue);
					} catch (CommandException eAgain) {
						// Failed again. Just close and print trace.
						fFactory.closeConnection(connect);
						connect = null;
						ProxyPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, ProxyPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", eAgain)); //$NON-NLS-1$
						return null;
					}
				} else {
					// A recoverable error, print trace and return
					ProxyPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, ProxyPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e)); //$NON-NLS-1$
					return null;
				}
			}
		} finally {
			proxyFactory.stopTransaction();
			if (connect != null)
				fFactory.returnConnection(connect);
		}
		return null;
	}


	/*
	 * @param returnValue
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private IBeanProxy[] processReturnValue(IREMConnection connection, ValueObject returnValue) throws CommandException, ThrowableProxy {
		// It is an array containing IDs, as it normally would be.
		// However it will become IBeanProxy[]. That is because if ID's
		// they must be proxies over here.
		BeanProxyValueSender valueSender = new BeanProxyValueSender((REMStandardBeanProxyFactory) fFactory.getBeanProxyFactory(), returnValue);
		connection.readProxyArrayValues(returnValue, valueSender, false);
		Exception e = valueSender.getException();
		if (e != null) {
			if (e instanceof ThrowableProxy)
				throw (ThrowableProxy) e;
			else
				throw (CommandException) e;
		}
		Object vals = valueSender.getArray();
		IBeanProxy[] proxyArray = new IBeanProxy[Array.getLength(vals)];
		System.arraycopy(vals, 0, proxyArray, 0, proxyArray.length);
		return proxyArray;
	}
	
}


