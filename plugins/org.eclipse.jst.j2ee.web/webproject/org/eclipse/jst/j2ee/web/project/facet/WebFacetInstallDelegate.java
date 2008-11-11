/******************************************************************************
 * Copyright (c) 2005 BEA Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial API and implementation
 ******************************************************************************/

package org.eclipse.jst.j2ee.web.project.facet;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.common.frameworks.CommonFrameworksPlugin;
import org.eclipse.jst.common.project.facet.WtpUtils;
import org.eclipse.jst.common.project.facet.core.ClasspathHelper;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.j2ee.internal.common.J2EEVersionUtil;
import org.eclipse.jst.j2ee.internal.web.classpath.WebAppLibrariesContainer;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.J2EEFacetInstallDelegate;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.datamodel.FacetDataModelProvider;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.project.facet.ProductManager;

import org.eclipse.jst.j2ee.internal.common.classpath.J2EEComponentClasspathContainer;
import org.eclipse.jst.j2ee.internal.common.classpath.J2EEComponentClasspathContainerUtils;
import org.eclipse.jst.javaee.core.DisplayName;
import org.eclipse.jst.javaee.core.JavaeeFactory;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebFactory;
import org.eclipse.jst.javaee.web.WelcomeFileList;

public final class WebFacetInstallDelegate extends J2EEFacetInstallDelegate implements IDelegate {

	public void execute(final IProject project, final IProjectFacetVersion fv, final Object cfg, final IProgressMonitor monitor) throws CoreException {
		if (monitor != null) {
			monitor.beginTask("", 1); //$NON-NLS-1$
		}

		try {
			final IDataModel model = (IDataModel) cfg;

			final IJavaProject jproj = JavaCore.create(project);

			// Add WTP natures.

			WtpUtils.addNatures(project);

			// Create the directory structure.

			final IWorkspace ws = ResourcesPlugin.getWorkspace();
			final IPath pjpath = project.getFullPath();

			final IPath contentdir = setContentPropertyIfNeeded(model, pjpath, project);
			mkdirs(ws.getRoot().getFolder(contentdir));

			final IPath webinf = contentdir.append("WEB-INF"); //$NON-NLS-1$
			IFolder webinfFolder = ws.getRoot().getFolder(webinf);
			mkdirs(webinfFolder);

			final IPath webinflib = webinf.append("lib"); //$NON-NLS-1$
			mkdirs(ws.getRoot().getFolder(webinflib));

			// Setup the flexible project structure.

			final IVirtualComponent c = ComponentCore.createComponent(project, false);

			c.create(0, null);

			String contextRoot = model.getStringProperty(IWebFacetInstallDataModelProperties.CONTEXT_ROOT);
			setContextRootPropertyIfNeeded(c, contextRoot);
			setJavaOutputPropertyIfNeeded(model,c);

			final IVirtualFolder webroot = c.getRootFolder();
			if (webroot.getProjectRelativePath().equals(new Path("/"))) //$NON-NLS-1$
				webroot.createLink(new Path("/" + model.getStringProperty(IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER)), 0, null); //$NON-NLS-1$

			if( fv == WebFacetUtils.WEB_25 )
			{
		         if(model.getBooleanProperty(IJ2EEFacetInstallDataModelProperties.GENERATE_DD)){
		                createWeb25DeploymentDescriptor(project, fv, webinfFolder, monitor);
		            }
			}
			else
			{
    			// Create the deployment descriptor (web.xml) if one doesn't exist
    			if (!webinfFolder.getFile("web.xml").exists()) { //$NON-NLS-1$
    				String ver = fv.getVersionString();
    				int nVer = J2EEVersionUtil.convertVersionStringToInt(ver);
    				WebArtifactEdit.createDeploymentDescriptor(project, nVer);
    			}
			}
			
			// Set entries for src folders
			final IVirtualFolder jsrc = c.getRootFolder().getFolder("/WEB-INF/classes"); //$NON-NLS-1$
			final IClasspathEntry[] cp = jproj.getRawClasspath();
			for (int i = 0; i < cp.length; i++) {
				final IClasspathEntry cpe = cp[i];
				if (cpe.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					if( cpe.getPath().removeFirstSegments(1).segmentCount() > 0 )
						jsrc.createLink(cpe.getPath().removeFirstSegments(1), 0, null);
				}
			}
			
			IVirtualFile vf = c.getRootFolder().getFile(new Path(J2EEConstants.MANIFEST_URI));
			IFile manifestmf = vf.getUnderlyingFile();
			if (manifestmf == null || !manifestmf.exists()) {
				try {
					createManifest(project, c.getRootFolder().getUnderlyingFolder(), monitor);
				} catch (InvocationTargetException e) {
					Logger.getLogger().logError(e);
				} catch (InterruptedException e) {
					Logger.getLogger().logError(e);
				}
			}

			// Setup the classpath.

			ClasspathHelper.removeClasspathEntries(project, fv);

			if (!ClasspathHelper.addClasspathEntries(project, fv)) {
				// TODO: Support the no runtime case.
				// ClasspathHelper.addClasspathEntries( project, fv, <something> );
			}

			// Add the web libraries container.

						
			if(J2EEComponentClasspathContainerUtils.getDefaultUseWebAppLibraries()){
				final IPath webLibContainer = new Path(WebAppLibrariesContainer.CONTAINER_ID);
				addToClasspath(jproj, JavaCore.newContainerEntry(webLibContainer));
			}

			if(J2EEComponentClasspathContainerUtils.getDefaultUseEARLibraries()){
				final IPath earLibContainer = new Path(J2EEComponentClasspathContainer.CONTAINER_ID);
				addToClasspath(jproj, JavaCore.newContainerEntry(earLibContainer));
			}

			try {
				((IDataModelOperation) model.getProperty(FacetDataModelProvider.NOTIFICATION_OPERATION)).execute(monitor, null);
			} catch (ExecutionException e) {
				Logger.getLogger().logError(e);
			}

			if (monitor != null) {
				monitor.worked(1);
			}
		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}
	}

	private void setJavaOutputPropertyIfNeeded(IDataModel model, final IVirtualComponent c) {
		// Make sure output folder is set properly for web projects, and the product setting for single root structure is maintained.
		// We may need to change the existing setup

		if (ProductManager.shouldUseSingleRootStructure()) {
			String outputFolder = model.getStringProperty(IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER)+"/"+J2EEConstants.WEB_INF_CLASSES;
			
			IJavaProject jproj = JavaCore.create(c.getProject());
			IClasspathEntry[] current = null;
			boolean webinf = false;
			IPath pjpath = c.getProject().getFullPath();
			try {
				current = jproj.getRawClasspath();
				List updatedList = new ArrayList();
				IPath sourcePath = null;
				boolean changeNeeded = false;
				for (int i = 0; i < current.length; i++) {
					IClasspathEntry entry = current[i];
					if ((entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) && (entry.getOutputLocation() != null && entry.getOutputLocation().toString().indexOf(J2EEConstants.WEB_INF_CLASSES) == -1)) {
						//output different than J2EEConstants.WEB_INF_CLASSES
						sourcePath = entry.getPath();
						updatedList.add(JavaCore.newSourceEntry(sourcePath));
						changeNeeded = true;
					}
					else
						updatedList.add(entry);
				}
				IPath currentDefaultOutput = null;
				currentDefaultOutput = jproj.getOutputLocation();
				if (currentDefaultOutput.toString().indexOf(J2EEConstants.WEB_INF_CLASSES) == -1)
					changeNeeded = true;
				if (changeNeeded) {
					IClasspathEntry[] updated = (IClasspathEntry[])updatedList.toArray(new IClasspathEntry[updatedList.size()]);
					IPath outdir = pjpath.append(outputFolder); 
					jproj.setRawClasspath(updated,outdir ,null);
					jproj.save(null, true);
				}
			} catch (JavaModelException e) {
				Logger.getLogger().logError(e);
			}
		}
		// Now just set the property
		String existing = c.getMetaProperties().getProperty("java-output-path"); //$NON-NLS-1$
		if (existing == null)
			setOutputFolder(model, c);
	}
	
	/**
	 * This overrides the default J2EE set output folder which sets the output folder to the content root
	 * if the optimized single root structure is used.  For web projects, we need to switch this to
	 * set the output folder to "<contentRoot>/WEB-INF/classes"
	 * 
	 * @param model
	 * @param component
	 */
	protected void setOutputFolder(IDataModel model, IVirtualComponent component) {
		String outputFolder = null;
		// If using single root structure, set the output folder to "<contentRoot>/WEB-INF/classes"
		if (ProductManager.shouldUseSingleRootStructure())
			outputFolder = model.getStringProperty(IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER)+"/"+J2EEConstants.WEB_INF_CLASSES;
		// Otherwise set the output folder to the product setting default
		else
			outputFolder = CommonFrameworksPlugin.getDefault().getPluginPreferences().getString(CommonFrameworksPlugin.OUTPUT_FOLDER);

		component.setMetaProperty("java-output-path", outputFolder ); //$NON-NLS-1$
	}

	private void setContextRootPropertyIfNeeded(final IVirtualComponent c, String contextRoot) {
		String existing = c.getMetaProperties().getProperty("context-root"); //$NON-NLS-1$
		if (existing == null)
			c.setMetaProperty("context-root", contextRoot); //$NON-NLS-1$
	}

	private IPath setContentPropertyIfNeeded(final IDataModel model, final IPath pjpath, IProject project) {
		IVirtualComponent c = ComponentCore.createComponent(project, false);
		if (c.exists()) {
			if( !c.getRootFolder().getProjectRelativePath().isRoot() ){
				return c.getRootFolder().getUnderlyingResource().getFullPath();
			}
		}
		return pjpath.append(model.getStringProperty(IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER));
	}
	
    private void createWeb25DeploymentDescriptor(final IProject project, final IProjectFacetVersion fv, 
                                               IFolder webinfFolder, IProgressMonitor monitor) throws CoreException {
       // Create the deployment descriptor (web.xml) if one doesn't exist
       IFile webxmlFile = webinfFolder.getFile("web.xml"); //$NON-NLS-1$
       if (!webxmlFile.exists()) {
           try {
               // Create a minimal web.xml file, so the model can be initialized
               final String webXmlContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<web-app id=\"WebApp_ID\" version=\"2.5\" xmlns=\"http://java.sun.com/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\">\n</web-app>"; //$NON-NLS-1$
               webxmlFile.create(new ByteArrayInputStream(webXmlContents.getBytes("UTF-8")), true, monitor); //$NON-NLS-1$
           
               final IModelProvider provider = ModelProviderManager.getModelProvider(project, fv);
               Runnable runnable = new Runnable(){
   
                   public void run() {
                       WebApp webApp = (WebApp) provider.getModelObject();
                       
                       // Add the display-name tag
                       DisplayName displayName = (DisplayName) JavaeeFactory.eINSTANCE.createDisplayName();
                       displayName.setValue(project.getName());
                       webApp.getDisplayNames().add(displayName);
                       
                       // welcome file list
                       List<String> welcomeFiles = Arrays.asList(
                               "index.html", //$NON-NLS-1$
                               "index.htm", //$NON-NLS-1$
                               "index.jsp", //$NON-NLS-1$
                               "default.html", //$NON-NLS-1$
                               "default.htm", //$NON-NLS-1$
                               "default.jsp" //$NON-NLS-1$
                       );
                       
                       // Add the welcome-file-list tag
                       WelcomeFileList welcomeFileList = (WelcomeFileList) WebFactory.eINSTANCE.createWelcomeFileList();
                       welcomeFileList.getWelcomeFiles().addAll(welcomeFiles); 
                       webApp.getWelcomeFileLists().add(welcomeFileList);
                   }
               };
               provider.modify(runnable, null);
           } catch (UnsupportedEncodingException e) {
               Logger.getLogger().logError(e);
           }
       }
   }
	

	private static void mkdirs(final IFolder folder)

	throws CoreException

	{
		if (!folder.exists()) {
			if (folder.getParent() instanceof IFolder) {
				mkdirs((IFolder) folder.getParent());
			}

			folder.create(true, true, null);
		}
		else
		{
		    IContainer x = folder;
		    
		    while( x instanceof IFolder && x.isDerived() )
		    {
		        x.setDerived( false );
		        x = x.getParent();
		    }
		}
	}
}