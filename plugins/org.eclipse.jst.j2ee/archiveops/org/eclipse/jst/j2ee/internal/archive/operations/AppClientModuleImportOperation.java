/***************************************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
/*
 * Created on Dec 4, 2003
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code
 * Generation - Code and Comments
 */
package org.eclipse.jst.j2ee.internal.archive.operations;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.j2ee.application.operations.J2EEArtifactCreationDataModelOld;
import org.eclipse.jst.j2ee.applicationclient.creation.AppClientModuleImportDataModel;
import org.eclipse.jst.j2ee.applicationclient.creation.AppClientModuleCreationDataModelOld;
import org.eclipse.jst.j2ee.applicationclient.creation.AppClientModuleCreationOperationOld;
import org.eclipse.jst.j2ee.commonarchivecore.internal.strategy.SaveStrategy;

public class AppClientModuleImportOperation extends J2EEArtifactImportOperation {

	public AppClientModuleImportOperation(AppClientModuleImportDataModel model) {
		super(model);
	}

	protected SaveStrategy createSaveStrategy(IProject project) {
		ApplicationClientProjectSaveStrategyImpl saveStrat = new ApplicationClientProjectSaveStrategyImpl(project);
		return saveStrat;
	}

	protected void modifyStrategy(SaveStrategy saveStrat) {
		ApplicationClientProjectSaveStrategyImpl strategy = (ApplicationClientProjectSaveStrategyImpl) saveStrat;
		if (null != strategy.getOverwriteHandler()) {
			strategy.getOverwriteHandler().setAppClientSaveStrategy(strategy);
		}
	}

	protected void createModuleProject(J2EEArtifactCreationDataModelOld model, IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
		AppClientModuleCreationOperationOld op = new AppClientModuleCreationOperationOld((AppClientModuleCreationDataModelOld) model);
		op.run(monitor);
	}

}