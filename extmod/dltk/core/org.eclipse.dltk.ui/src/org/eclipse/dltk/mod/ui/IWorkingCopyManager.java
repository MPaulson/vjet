/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.mod.core.ISourceModule;
import org.eclipse.ui.IEditorInput;


/**
 * Interface for accessing working copies of <code>ISourceModule</code> objects. The original compilation unit is only given indirectly by means
 * of an <code>IEditorInput</code>. The life cycle is as follows:
 * <ul>
 * <li> <code>connect</code> creates and remembers a working copy of the compilation unit which is encoded in the given editor input</li>
 * <li> <code>getWorkingCopy</code> returns the working copy remembered on <code>connect</code></li>
 * <li> <code>disconnect</code> destroys the working copy remembered on <code>connect</code></li>
 * </ul>
 * <p>
 * In order to provide backward compatibility for clients of <code>IWorkingCopyManager</code>, extension interfaces are used to provide a means of
 * evolution. The following extension interfaces exist:
 * <ul>
 * <li> {@link org.eclipse.dltk.mod.ui.IWorkingCopyManagerExtension} since version 2.1 introducing API to set and remove the working copy for a given
 * editor input.</li>
 * </ul>
 * </p>
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 * 
 * @see DLTK UI#getWorkingCopyManager()
 * @see org.eclipse.dltk.mod.ui.IWorkingCopyManagerExtension
 */
public interface IWorkingCopyManager
{

	/**
	 * Connects the given editor input to this manager. After calling this method, a working copy will be available for the compilation unit
	 * encoded in the given editor input (does nothing if there is no encoded compilation unit).
	 * 
	 * @param input
	 *                the editor input
	 * @exception CoreException
	 *                    if the working copy cannot be created for the compilation unit
	 */
	void connect( IEditorInput input ) throws CoreException;

	/**
	 * Disconnects the given editor input from this manager. After calling this method, a working copy for the compilation unit encoded in the
	 * given editor input will no longer be available. Does nothing if there is no encoded compilation unit, or if there is no remembered working
	 * copy for the compilation unit.
	 * 
	 * @param input
	 *                the editor input
	 */
	void disconnect( IEditorInput input );

	/**
	 * Returns the working copy remembered for the compilation unit encoded in the given editor input.
	 * 
	 * @param input
	 *                the editor input
	 * @return the working copy of the compilation unit, or <code>null</code> if the input does not encode an editor input, or if there is no
	 *         remembered working copy for this compilation unit
	 */
	ISourceModule getWorkingCopy( IEditorInput input );
	ISourceModule getWorkingCopy( IEditorInput input, boolean primaryOnly );

	/**
	 * Shuts down this working copy manager. All working copies still remembered by this manager are destroyed.
	 */
	void shutdown( );
}