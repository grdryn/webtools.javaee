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
package org.eclipse.jst.j2ee.internal.deployables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.j2ee.internal.earcreation.IEARNatureConstants;
import org.eclipse.jst.j2ee.internal.project.J2EENature;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IModuleType;
import org.eclipse.wst.server.core.IProjectProperties;
import org.eclipse.wst.server.core.ServerCore;

/**
 * @version 1.0
 * @author
 */
public class EnterpriseApplicationDeployableFactory extends J2EEDeployableFactory {

	protected static final String ID = "com.ibm.wtp.server.j2ee.application"; //$NON-NLS-1$

	protected static final IPath[] PATHS = new IPath[]{new Path("META-INF/application.xml"), //$NON-NLS-1$
				new Path("META-INF/.modulemaps") //$NON-NLS-1$
	};

	public EnterpriseApplicationDeployableFactory() {
		super();
	}



	public IModule createModule(J2EENature nature) {
		return null;
	}


	protected IPath[] getListenerPaths() {
		return PATHS;
	}


	protected List createModuleDelegates(IVirtualComponent[] components) {
		EnterpriseApplicationDeployable moduleDelegate = null;
		IModule module = null;
		List moduleList = new ArrayList(components.length);
		// J2EENature nature = (J2EENature)project.getNature(getNatureID());

		for (int i = 0; i < components.length; i++) {
			IVirtualComponent component = components[i];
			try {
				if(IModuleConstants.JST_EAR_MODULE.equals(component.getComponentTypeId())) {
					moduleDelegate = new EnterpriseApplicationDeployable(component.getProject(), ID, component);
					//moduleDelegate.getModules();
					module = createModule(component.getName(), component.getName(), moduleDelegate.getType(), moduleDelegate.getVersion(), moduleDelegate.getProject());
					moduleList.add(module);
					moduleDelegate.initialize(module);
				}
				// adapt(moduleDelegate, (WorkbenchComponent) workBenchModules.get(i));
			} catch (Exception e) {
				Logger.getLogger().write(e);
			} finally {
				if (module != null) {
					if (getModuleDelegate(module) == null)
						moduleDelegates.add(moduleDelegate);
				}
			}
		}
		return moduleList;
	}

	public IModule[] getModules() {
		cacheModules(false);
		ArrayList moduleList = new ArrayList();
		for (Iterator iter = projects.values().iterator(); iter.hasNext();) {
			IModule[] element = (IModule[]) iter.next();
			for (int j = 0; j < element.length; j++) {
				moduleList.add(element[j]);
			}
		}
		IModule[] modules = new IModule[moduleList.size()];
		moduleList.toArray(modules);
		return modules;

	}



	protected String getNatureID() {
		return IModuleConstants.MODULE_NATURE_ID;
	}
	
	protected boolean isValidModule(IProject project) {
		if (isFlexableProject(project)) {
			IProjectProperties properties = ServerCore.getProjectProperties(project);
			if (properties != null || properties.getRuntimeTarget() == null || properties.getRuntimeTarget().getRuntimeType().getModuleTypes() != null) {
				IModuleType[] moduleTypes = properties.getRuntimeTarget().getRuntimeType().getModuleTypes();
				for (int i = 0; i < moduleTypes.length; i++) {
					IModuleType moduleType = moduleTypes[i];
					if (moduleType.getId().equals("j2ee.ear"))
						return true;
				}

			}

		}
		return false;
	}



}
