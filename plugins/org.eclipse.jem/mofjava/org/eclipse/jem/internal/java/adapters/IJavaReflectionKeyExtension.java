package org.eclipse.jem.internal.java.adapters;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IJavaReflectionKeyExtension.java,v $
 *  $Revision: 1.2 $  $Date: 2005/02/15 22:37:02 $ 
 */
/**
 * Interface for JavaReflectionKeyExtensions.
 * They are registered with the JavaXMIFactoryImpl.
 * 
 * @version 	1.0
 * @author R. L. Kulp
 */
public interface IJavaReflectionKeyExtension {

	/**
	 * Return object for the given id. Return null if this extension doesn't
	 * process this type of id.
	 */
	public Object getObject(String id, JavaReflectionKey reflectionKey);
	
}



