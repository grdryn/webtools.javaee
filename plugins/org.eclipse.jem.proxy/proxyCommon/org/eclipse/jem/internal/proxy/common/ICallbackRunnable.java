/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ICallbackRunnable.java,v $
 *  $Revision: 1.4 $  $Date: 2005/02/15 22:54:34 $ 
 */
package org.eclipse.jem.internal.proxy.common;

/**
 * Users would implement this as a runnable to 
 * send a callback request.
 */
public interface ICallbackRunnable {
	
	/**
	 * The actual run code.
	 */
	public Object run(ICallbackHandler handler) throws CommandException;

}
