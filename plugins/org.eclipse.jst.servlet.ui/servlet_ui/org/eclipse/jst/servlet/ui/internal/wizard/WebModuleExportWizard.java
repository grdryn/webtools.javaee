/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/ 
package org.eclipse.jst.servlet.ui.internal.wizard;

import org.eclipse.jst.j2ee.internal.plugin.J2EEUIPlugin;
import org.eclipse.jst.j2ee.internal.plugin.J2EEUIPluginIcons;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebModuleExportDataModelProvider;
import org.eclipse.jst.j2ee.internal.wizard.J2EEArtifactExportWizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelProvider;

/**
 * <p>
 * Wizard used to export J2EE Web Application module structures 
 * from the Eclipse Workbench to a deployable Web Application 
 * Archive *.war file.  
 * </p>
 */
public final class WebModuleExportWizard extends J2EEArtifactExportWizard implements IExportWizard {

	/**
	 * <p>
	 * The default constructor. Creates a wizard with no selection, 
	 * no model instance, and no operation instance. The model and 
	 * operation will be created as needed.
	 * </p>
	 */
	public WebModuleExportWizard() {
		super();
	}
	
	/**
	 * <p>
	 * The model is used to prepopulate the wizard controls
	 * and interface with the operation.
	 * </p>
	 * @param model The model parameter is used to pre-populate wizard controls and interface with the operation
	 */
	public WebModuleExportWizard(IDataModel model) {
		super(model);
	}
    
    protected IDataModelProvider getDefaultProvider() {
        return new WebModuleExportDataModelProvider();
    }
	/**
	 * <p>
	 * Adds the following pages:
	 * <ul>
	 * 	<li> {@link WARExportPage} as the main wizard page ({@link #MAIN_PG}) 
	 * </ul>
	 * </p>
	 */
	public void doAddPages() {
		addPage(new WARExportPage(getDataModel(), MAIN_PG, getSelection()));
	}

	/**
	 * {@inheritDoc}   
	 * 
	 * <p>
	 * Sets up the default wizard page image. 
	 * </p>
	 */
	protected void doInit() {
		setDefaultPageImageDescriptor(J2EEUIPlugin.getDefault().getImageDescriptor(J2EEUIPluginIcons.WEB_EXPORT_WIZARD_BANNER));
	}
}