/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.j2ee.webservice.wsdd;

import org.eclipse.jst.j2ee.internal.common.XMLResource;

public interface WsddResource extends XMLResource
{
  public static final int WEB_SERVICE_TYPE = 6;

  WebServices getWebServices();
  public boolean isWebService1_0();
  public boolean isWebService1_1();
  
}
