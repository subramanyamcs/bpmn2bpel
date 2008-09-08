package bpelfactory.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import simpel.SimPELLexer;

import bpelfactory.Activator;
import bpelfactory.Translator;

//Copyright (C) 2008 Luciano Garcia-Banuelos <lgbanuelos@gmail.com>
//Licensed under the terms of the GNU GPL; see COPYING for details.
public class BPMN2BPELAction implements IObjectActionDelegate {

	private IFile targetFile;

	/**
	 * Constructor for Action1.
	 */
	public BPMN2BPELAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		try {
			new Translator(targetFile).translate();
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		try {
			targetFile = (IFile)((TreeSelection)selection).getFirstElement();
		} catch (Exception e) {
		}
	}
	
	public void refresh() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		try {
			root.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, "", e));
		}
	}

}
