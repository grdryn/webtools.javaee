/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jem.java;

/*


 */

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
/**
 * @generated
 */
public interface JavaRefFactory extends EFactory {

	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	JavaRefFactory eINSTANCE = org.eclipse.jem.java.internal.impl.JavaRefFactoryImpl.init();


	/**
	 * Create a proxy reference to the class name.
	 * @param targetName Classname to create a proxy reference to. This must be fully-qualified.
	 * @return A proxy reference
	 * 
	 * @since 1.0.0
	 */
	public JavaClass createClassRef(String targetName);
	
	/**
	 * reflect - reflect a java type (class or primitive) for a given qualified name.
	 * <p>If the package or type does not exist, one will be created through
	 * the reflection mechanism.
	 * <p>Lookup the JavaClass in the context of the passed object, handling some error cases.
	 * @param aQualifiedName Fully qualified name of class or primitive (e.g. <code>java.lang.Object</code> or <code>int</code>
	 * @param relatedObject EObject that it will be related too (it will look through the resource set's project of the EObject)
	 * @return The type. <code>null</code> if name is not of correct format, relatedObject is not contained by a resource set, or resource set is not associated with a project.
	 * 
	 * @since 1.0.0
	 */
	public JavaHelpers reflectType(String aQualifiedName, EObject relatedObject);
	
	
	/**
	 * reflect - reflect a java type (class or primitive) for a given qualified name.
	 * <p>If the package or class does not exist, one will be created through
	 * the reflection mechanism.
	 * @param aQualifiedName Fully qualified name of class or primitive (e.g. <code>java.lang.Object</code> or <code>int</code>. 
	 * @param set Resource set to use. Its project will be used to find the type.
	 * @return The type. <code>null</code> if name is not of correct format, or resource set is not associated with a project.
	 * 
	 * @since 1.0.0
	 */
	public JavaHelpers reflectType(String aQualifiedName, ResourceSet set);	
	
	
	/**
	 * reflect - reflect a type for a given package name and class name.
	 * <p>If the package or class does not exist, one will be created through
	 * the reflection mechanism.
	 * @param aPackageName Package name
	 * @param aTypeName Type name 
	 * @param set Resource set to use. Its project will be used to find the type.
	 * @return The type. <code>null</code> if name is not of correct format, or resource set is not associated with a project.
	 * 
	 * @since 1.0.0
	 */
	public JavaHelpers reflectType(String aPackageName, String aTypeName, ResourceSet set);
	
	/**
	 * reflect - reflect a Java package for a given package name.
	 * <p>If the package does not exist, one will be created through
	 * the reflection mechanism.
	 * @param packageName Name of package
	 * @param set Resource set to use. Its project will be used to find the package.
	 * @return The package. <code>null</code> if name is not of correct format, or resource set is not associated with a project.
	 * 
	 * @since 1.0.0
	 */
	public JavaPackage reflectPackage(String packageName, ResourceSet set);
	
	/**
	 * The scheme for a java URI.
	 * 
	 * @since 1.2.0
	 */
	public static final String JAVA_PROTOCOL_URI_SCHEME = "java"; //$NON-NLS-1$
	
	/**
	 * Create the URI for accessing a type through {@link ResourceSet#getEObject(URI, boolean)}.
	 * @param aQualifiedName
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public URI createTypeURI(String aQualifiedName);
	
	/**
	 * Create the URI for accessing a type through {@link ResourceSet#getEObject(URI, boolean)}.
	 * @param packageName package name (may be <code>null</code> or <code>""</code> for primitives or default package)
	 * @param typeName type name
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public URI createTypeURI(String packageName, String typeName);
	
	/**
	 * Create the URI for accessing a package through {@link ResourceSet#getEObject(URI, boolean)}.
	 * @param packageName package name (may be <code>null</code> or <code>""</code> for primitives or default package)
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public URI createPackageURI(String packageName);
	
	/**
	 * Get just the type name without the package name.
	 * @param typeURI
	 * @return the type name or <code>""</code> if not a valid type URI
	 * 
	 * @throws IllegalArgumentException if not a valid type URI
	 * @since 1.2.0
	 */
	public String getTypeName(URI typeURI);
	
	/**
	 * Get just the package name.
	 * @param javaURI either a type or a package URI.
	 * @return package name or <code>""</code> if default package or primitive.
	 * 
	 * @throws IllegalArgumentException if not a valid package or type URI
	 * @since 1.2.0
	 */
	public String getPackageName(URI javaURI);
	
	/**
	 * Get the fully qualified type name.
	 * @param typeURI
	 * @return fully qualified name or <code>""</code> if not a valid type URI
	 * 
	 * @throws IllegalArgumentException if not a valid type URI
	 * @since 1.2.0
	 */
	public String getFullTypeName(URI typeURI);
	
	/**
	 * Is this a valid java uri.
	 * @param uri
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public boolean isJavaURI(URI uri);

	/**
	 * Is this a valid type uri.
	 * @param uri
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public boolean isTypeURI(URI uri);
	
	/**
	 * Is this a valid package uri.
	 * @param uri
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public boolean isPackageURI(URI uri);
	
	/**
	 * @return ArrayType value with an array of the component type
	 */
	public ArrayType createArrayType(JavaHelpers componentType);
	/**
	 * @return ArrayType value with an array of the specified dimensions and final component type.
	 */
	public ArrayType createArrayType(JavaHelpers finalComponentType, int dimensions);
	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return Method value
	 */
	Method createMethod();

	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return JavaClass value
	 */
	JavaClass createJavaClass();

	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return Field value
	 */
	Field createField();

	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return Block value
	 */
	Block createBlock();

	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return Comment value
	 */
	Comment createComment();

	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return Statement value
	 */
	Statement createStatement();

	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return Initializer value
	 */
	Initializer createInitializer();

	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return JavaParameter value
	 */
	JavaParameter createJavaParameter();

	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return ArrayType value
	 */
	ArrayType createArrayType();

	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return JavaDataType value
	 */
	JavaDataType createJavaDataType();

	/**
	 * @generated This field/method will be replaced during code generation 
	 * @return JavaPackage value
	 */
	JavaPackage createJavaPackage();

	/**
	 * @generated This field/method will be replaced during code generation.
	 */
	JavaRefPackage getJavaRefPackage();

}






