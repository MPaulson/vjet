/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.core.tests.model;

import java.util.ArrayList;

import junit.framework.Test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.mod.core.BufferChangedEvent;
import org.eclipse.dltk.mod.core.IBuffer;
import org.eclipse.dltk.mod.core.IBufferChangedListener;
import org.eclipse.dltk.mod.core.ISourceModule;


public class BufferTests extends ModifyingResourceTests implements IBufferChangedListener {

	final static String[] TEST_NATURES = new String[] {
		ModelTestsPlugin.TEST_NATURE
	};
	
	protected ArrayList events = null;
	
	public BufferTests(String name) {
		super(ModelTestsPlugin.PLUGIN_NAME, name);
	}

	public static Test suite() {
		return new Suite(BufferTests.class);
	}
	
	/**
	 * @see AbstractModelTests#setUpSuite()
	 */
	public void setUpSuite() throws Exception {
		super.setUpSuite();
		try {
			this.createScriptProject("P", TEST_NATURES, new String[] {""});
			this.createFolder("P/x/y");
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see AbstractModelTests#tearDownSuite()
	 */
	public void tearDownSuite() throws Exception {
		super.tearDownSuite();
		this.deleteProject("P");
	}

	
	protected IBuffer createBuffer(String path, String content) throws CoreException {
		waitUntilIndexesReady(); // ensure that the indexer is not reading the file
		this.createFile(path, content);
		ISourceModule sm = this.getSourceModule(path);
		IBuffer buffer = sm.getBuffer();
		buffer.addBufferChangedListener(this);
		this.events = new ArrayList();
		return buffer;
	}

	/**
	 * Verify the buffer changed event.
	 * The given text must contain '\n' line separators.
	 */
	protected void assertBufferEvent(int offset, int length, String text) {
		assertTrue("events should not be null", this.events != null);
		assertTrue("events should not be empty", !this.events.isEmpty());
		BufferChangedEvent event = (BufferChangedEvent) this.events.get(0);
		assertEquals("unexpected offset", offset, event.getOffset());
		assertEquals("unexpected length", length, event.getLength());
		if (text == null) {
			assertTrue("text should be null", event.getText() == null);
		} else {
			assertSourceEquals("unexpected text", text, event.getText());
		}
	}
	
	protected void deleteBuffer(IBuffer buffer) throws CoreException {
		buffer.removeBufferChangedListener(this);
		IResource resource = buffer.getUnderlyingResource();
		if (resource != null) {
			deleteResource(resource);
		}
	}
	
	
	/**
	 * Tests appending to a buffer.
	 */
	public void testAppend() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			int oldLength= buffer.getLength();
			buffer.append("\nclass B {}");
			assertBufferEvent(oldLength, 0, "\nclass B {}");
			assertSourceEquals(
				"unexpected buffer contents",
				"package x.y;\n" +
				"public class A {\n" +
				"}\n" +
				"class B {}",
				buffer.getContents()
			);
			assertTrue("should have unsaved changes", buffer.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	
	public void testClose() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			buffer.close();
			assertBufferEvent(0, 0, null);
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	
	/**
	 * Tests getting the underlying resource of a buffer.
	 */
	public void testGetUnderlyingResource() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		ISourceModule copy = null;
		try {
			IFile file = this.getFile("P/x/y/A.txt");
			assertEquals("Unexpected underlying resource", file, buffer.getUnderlyingResource());
			
			copy = this.getSourceModule("P/x/y/A.txt").getWorkingCopy(null);
			assertEquals("Unexpected underlying resource 2", file, copy.getBuffer().getUnderlyingResource());
		} finally {
			this.deleteBuffer(buffer);
			if (copy != null) {
				copy.discardWorkingCopy();
			}
		}
	}
	
	/**
	 * Tests deleting text at the beginning of a buffer.
	 */
	public void testDeleteBeginning() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			buffer.replace(0, 13, "");
			assertBufferEvent(0, 13, null);
			assertSourceEquals(
				"unexpected buffer contents",
				"public class A {\n" +
				"}",
				buffer.getContents()
			);
			assertTrue("should have unsaved changes", buffer.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests deleting text in the middle of a buffer.
	 */
	public void testDeleteMiddle() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			// delete "public "
			buffer.replace(13, 7, "");
			assertBufferEvent(13, 7, null);
			assertSourceEquals(
				"unexpected buffer contents",
				"package x.y;\n" +
				"class A {\n" +
				"}",
				buffer.getContents()
			);
			assertTrue("should have unsaved changes", buffer.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests deleting text at the end of a buffer.
	 */
	public void testDeleteEnd() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			// delete "public class A {\n}"
			buffer.replace(13, 18, "");
			assertBufferEvent(13, 18, null);
			assertSourceEquals(
				"unexpected buffer contents",
				"package x.y;\n",
				buffer.getContents()
			);
			assertTrue("should have unsaved changes", buffer.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests the buffer char retrieval via source position 
	 */
	public void testGetChar() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			assertEquals("Unexpected char at position 17", 'i', buffer.getChar(17));
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests the buffer char retrieval via source position doesn't throw an exception if the buffer is closed.
	 * (regression test for bug 46040 NPE in Eclipse console)
	 */
	public void testGetChar2() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		buffer.close();
		try {
			assertEquals("Unexpected char at position 17", Character.MIN_VALUE, buffer.getChar(17));
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests the buffer getLength() 
	 */
	public void testGetLength() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			assertEquals("Unexpected length", 31, buffer.getLength());
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests the buffer text retrieval via source position 
	 */
	public void testGetText() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			assertSourceEquals("Unexpected text (1)", "p", buffer.getText(0, 1));
			assertSourceEquals("Unexpected text (2)", "public", buffer.getText(13, 6));
			assertSourceEquals("Unexpected text (3)", "", buffer.getText(10, 0));
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests inserting text at the beginning of a buffer.
	 */
	public void testInsertBeginning() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			buffer.replace(0, 0, "/* copyright mycompany */\n");
			assertBufferEvent(0, 0, "/* copyright mycompany */\n");
			assertSourceEquals(
				"unexpected buffer contents",
				"/* copyright mycompany */\n" +
				"package x.y;\n" +
				"public class A {\n" +
				"}",
				buffer.getContents()
			);
			assertTrue("should have unsaved changes", buffer.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests replacing text at the beginning of a buffer.
	 */
	public void testReplaceBeginning() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			buffer.replace(0, 13, "package other;\n");
			assertBufferEvent(0, 13, "package other;\n");
			assertSourceEquals(
				"unexpected buffer contents",
				"package other;\n" +
				"public class A {\n" +
				"}",
				buffer.getContents()
			);
			assertTrue("should have unsaved changes", buffer.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests replacing text in the middle of a buffer.
	 */
	public void testReplaceMiddle() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			// replace "public class A" after the \n of package statement
			buffer.replace(13, 14, "public class B");
			assertBufferEvent(13, 14, "public class B");
			assertSourceEquals(
				"unexpected buffer contents",
				"package x.y;\n" +
				"public class B {\n" +
				"}",
				buffer.getContents()
			);
			assertTrue("should have unsaved changes", buffer.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests replacing text at the end of a buffer.
	 */
	public void testReplaceEnd() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			// replace "}" at the end of cu with "}\n"
			int end = buffer.getLength();
			buffer.replace(end-1, 1, "}\n");
			assertBufferEvent(end-1, 1, "}\n");
			assertSourceEquals(
				"unexpected buffer contents",
				"package x.y;\n" +
				"public class A {\n" +
				"}\n",
				buffer.getContents()
			);
			assertTrue("should have unsaved changes", buffer.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests inserting text in the middle of a buffer.
	 */
	public void testInsertMiddle() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			// insert after the \n of package statement
			buffer.replace(13, 0, "/* class comment */\n");
			assertBufferEvent(13, 0, "/* class comment */\n");
			assertSourceEquals(
				"unexpected buffer contents",
				"package x.y;\n" +
				"/* class comment */\n" +
				"public class A {\n" +
				"}",
				buffer.getContents()
			);
			assertTrue("should have unsaved changes", buffer.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}
	/**
	 * Tests inserting text at the end of a buffer.
	 */
	public void testInsertEnd() throws CoreException {
		IBuffer buffer = this.createBuffer(
			"P/x/y/A.txt",
			"package x.y;\n" +
			"public class A {\n" +
			"}"
		);
		try {
			// insert after the \n of package statement
			int end = buffer.getLength();
			buffer.replace(end, 0, "\nclass B {}");
			assertBufferEvent(end, 0, "\nclass B {}");
			assertSourceEquals(
				"unexpected buffer contents",
				"package x.y;\n" +
				"public class A {\n" +
				"}\n" +
				"class B {}",
				buffer.getContents()
			);
			assertTrue("should have unsaved changes", buffer.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	public void bufferChanged(BufferChangedEvent bufferChangedEvent) {
		this.events.add(bufferChangedEvent);
	}
}
