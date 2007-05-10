/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.j2ee.internal.archive;

import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class JavaComponentArchiveLoadAdapter extends ComponentArchiveLoadAdapter {

	public JavaComponentArchiveLoadAdapter(IVirtualComponent vComponent) {
		super(vComponent);
	}
	
	public JavaComponentArchiveLoadAdapter(IVirtualComponent vComponent, boolean includeClasspathComponents) {
		super(vComponent, includeClasspathComponents);
	}
	
}
