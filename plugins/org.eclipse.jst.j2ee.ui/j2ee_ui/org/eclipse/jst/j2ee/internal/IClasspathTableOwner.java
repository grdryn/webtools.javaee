/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.j2ee.internal;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * @author jialin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IClasspathTableOwner {
	Button primCreatePushButton(String label, Composite buttonColumn);
	Button primCreateRadioButton(String label, Composite parent);
	CheckboxTableViewer createAvailableJARsViewer(Composite parent);
	Composite createButtonColumnComposite(Composite parent);
	Button createHideEJBClientJARsButton(Composite parent);
	Group createGroup(Composite parent);
}
