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
package org.eclipse.jem.internal.beaninfo.ui;
/*
 *  $RCSfile: BPSearchListElement.java,v $
 *  $Revision: 1.4 $  $Date: 2005/02/15 23:02:54 $ 
 */

import org.eclipse.jem.internal.beaninfo.core.SearchpathEntry;

/**
 * @version 	1.0
 * @author
 */
public class BPSearchListElement extends BPListElement {

	protected boolean exported;
	protected boolean packageMissing;
	/**
	 * Constructor for BPSearchListElement.
	 * @param entry
	 * @param project
	 * @param inMissing
	 */
	public BPSearchListElement(
		SearchpathEntry entry,
		boolean missing,
		boolean packageMissing,
		boolean exported) {
		super(entry, missing);

		this.packageMissing = packageMissing;
		this.exported = exported;
	}

	/*
	 * @see BPListElement#canExportBeChanged()
	 */
	public boolean canExportBeChanged() {
		return false;
	}

	/*
	 * @see BPListElement#isExported()
	 */
	public boolean isExported() {
		return exported;
	}

	/**
	 * Is the package missing.
	 */
	public boolean isPackageMissing() {
		return packageMissing;
	}
	
	/*
	 * @see BPListElement#setExported(boolean)
	 */
	public void setExported(boolean exported) {
	}

}
