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
package se.weightpoint.jslint.ui.internal.preferences.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;


public class LayoutUtil {

  public static Composite createMainComposite( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( createGridLayout( 1, false ) );
    composite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    return composite;
  }

  public static Composite createDefaultComposite( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( createGridLayout( 1, false ) );
    composite.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    return composite;
  }

  public static GridLayout createGridLayout( int numColumns, boolean makeColumnsEqualWidth ) {
    GridLayout layout = new GridLayout( numColumns, makeColumnsEqualWidth );
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    return layout;
  }

  public static GridData createSpanGridData() {
    GridData labelData = new GridData( SWT.FILL, SWT.TOP, true, false );
    labelData.horizontalSpan = 2;
    return labelData;
  }

}
