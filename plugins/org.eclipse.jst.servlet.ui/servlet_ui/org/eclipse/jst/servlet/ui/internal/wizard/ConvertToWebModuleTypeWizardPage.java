/*
 * Created on Jun 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.servlet.ui.internal.wizard;

import org.eclipse.jst.j2ee.internal.web.archive.operations.WebModuleCreationDataModelOld;
import org.eclipse.jst.servlet.ui.internal.plugin.WEBUIMessages;
import org.eclipse.swt.widgets.Composite;

/**
 * @author fatty
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConvertToWebModuleTypeWizardPage extends WebModuleCreationPage {

	/**
	 * @param model
	 * @param pageName
	 */
	protected ConvertToWebModuleTypeWizardPage(WebModuleCreationDataModelOld model, String pageName) {
		// TODO use flexible project
		//super(model, pageName);
		super(null, pageName);
		setDescription(WEBUIMessages.getResourceString(WEBUIMessages.WEB_CONVERT_MAIN_PG_DESC)); //$NON-NLS-1$
		setTitle(WEBUIMessages.getResourceString(WEBUIMessages.WEB_CONVERT_MAIN_PG_TITLE)); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.wtp.j2ee.ui.wizard.J2EEProjectCreationPage#createProjectNameGroup(org.eclipse.swt.widgets.Composite)
	 */
	protected void createProjectNameGroup(Composite parent) {
		super.createProjectNameGroup(parent);
		// projectNameGroup.projectNameField.setEnabled(false);
	}
}