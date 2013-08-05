/*******************************************************************************
 * Copyright (c) 2012 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial implementation and API
 ******************************************************************************/
package se.weightpoint.jslint.ui.internal.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import se.weightpoint.jslint.ui.internal.Activator;


public class JSLintBuilder extends IncrementalProjectBuilder {

  public static final String ID = Activator.PLUGIN_ID + ".builder";
  public static final String ID_OLD = "se.weightpoint.jslint.builder";

  @Override
  protected IProject[] build( int kind, Map<String, String> args, IProgressMonitor monitor )
      throws CoreException
  {
    if( kind == IncrementalProjectBuilder.FULL_BUILD ) {
      fullBuild( monitor );
    } else {
      IResourceDelta delta = getDelta( getProject() );
      if( delta == null ) {
        fullBuild( monitor );
      } else {
        incrementalBuild( delta, monitor );
      }
    }
    return null;
  }

  @Override
  protected void clean( IProgressMonitor monitor ) throws CoreException {
    new MarkerAdapter( getProject() ).removeMarkers();
  }

  private void fullBuild( IProgressMonitor monitor ) throws CoreException {
    IProject project = getProject();
    getProject().accept( new JSLintBuilderVisitor( project, monitor ) );
  }

  private void incrementalBuild( IResourceDelta delta, IProgressMonitor monitor )
      throws CoreException
  {
    IProject project = getProject();
    delta.accept( new JSLintBuilderVisitor( project, monitor ) );
  }

  static class CoreExceptionWrapper extends RuntimeException {

    private static final long serialVersionUID = 2267576736168605043L;

    public CoreExceptionWrapper( CoreException wrapped ) {
      super( wrapped );
    }

  }

}
