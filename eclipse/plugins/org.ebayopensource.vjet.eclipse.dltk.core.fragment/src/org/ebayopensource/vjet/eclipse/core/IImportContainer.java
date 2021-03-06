/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.ebayopensource.vjet.eclipse.core;

import org.eclipse.dltk.mod.core.IModelElement;
import org.eclipse.dltk.mod.core.IParent;
import org.eclipse.dltk.mod.core.ISourceReference;

/**
 * Represents an import container is a child of a Java compilation unit that
 * contains all (and only) the import declarations. If a compilation unit has no
 * import declarations, no import container will be present.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 */
public interface IImportContainer extends IModelElement, IParent,
		ISourceReference {
	/**
	 * Constant representing all import declarations within a compilation unit.
	 * A Java element with this type can be safely cast to
	 * <code>IImportContainer</code>.
	 */
	int ELEMENT_TYPE = 12;

	/**
	 * Returns the first import declaration in this import container with the
	 * given name. This is a handle-only method. The import declaration may or
	 * may not exist.
	 * 
	 * @param name
	 *            the given name
	 * 
	 * @return the first import declaration in this import container with the
	 *         given name
	 */
	IImportDeclaration getImport(String name);
}
