package org.eclipse.jst.j2ee.internal.archive;

import org.eclipse.core.runtime.Path;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.jee.archive.IArchive;
import org.eclipse.jst.jee.archive.IArchiveHandler;

public class ConnectorComponentArchiveTypeHandler implements IArchiveHandler {

	public boolean handlesArchive(IArchive archive) {
		if (archive.getLoadAdapter() instanceof ConnectorComponentArchiveLoadAdapter) {
			return true;
		} else if (archive.containsArchiveResource(new Path(J2EEConstants.RAR_DD_URI))) {
			return true;
		} else {
			// TODO handle the no DD case
			return false;
		}
	}

}
