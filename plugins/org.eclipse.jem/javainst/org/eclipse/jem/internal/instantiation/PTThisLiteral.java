/*******************************************************************************
 * Copyright (c)  2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PTThisLiteral.java,v $
 *  $Revision: 1.4 $  $Date: 2005/02/15 22:36:09 $ 
 */
package org.eclipse.jem.internal.instantiation;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>This Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Represents "this", e.g. this.getX() will be a MethodInvocation with the receiver being a ThisLiteral. We can't handle the format XYZ.this because that is for inner classes and we don't support that right now.
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.jem.internal.instantiation.InstantiationPackage#getPTThisLiteral()
 * @model 
 * @generated
 */
public interface PTThisLiteral extends PTExpression{
} // ThisLiteral
