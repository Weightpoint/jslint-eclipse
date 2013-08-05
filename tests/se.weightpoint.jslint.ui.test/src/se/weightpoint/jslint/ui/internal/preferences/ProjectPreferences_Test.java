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
package se.weightpoint.jslint.ui.internal.preferences;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.prefs.Preferences;

import se.weightpoint.jslint.ui.internal.preferences.EnablementPreferences;
import se.weightpoint.jslint.ui.internal.preferences.OptionsPreferences;
import se.weightpoint.jslint.ui.internal.preferences.PreferencesFactory;
import static org.junit.Assert.*;
import static se.weightpoint.jslint.ui.test.TestUtil.*;


public class ProjectPreferences_Test {
  private IProject project;
  private IFile file;
  private Preferences node;

  @Before
  public void setUp() {
    project = createProject( "test" );
    file = createFile( project, "/test.js", "test content" );
    node = PreferencesFactory.getProjectPreferences( project );
  }

  @After
  public void tearDown() {
    deleteProject( project );
  }

  @Test
  public void defaultPrefsForEmptyProject() {
    EnablementPreferences prefs = new EnablementPreferences( node );

    assertTrue( prefs.getIncludePatterns().isEmpty() );
  }

  @Test
  public void enablementPrefsFromExampleSettingsFile() throws Exception {
    createExampleSettingsFile( project, SETTINGS_FILE_NAME, SETTINGS_TEMPLATE_0_9 );

    EnablementPreferences prefs = new EnablementPreferences( node );

    assertTrue( prefs.getExcludePatterns().contains( "js/test.js" ) );
    assertTrue( prefs.getExcludePatterns().contains( "target" ) );
    assertFalse( prefs.hasChanged() );
  }

  @Test
  public void optionPrefsFromExampleSettingsFile() throws Exception {
    createExampleSettingsFile( project, SETTINGS_FILE_NAME, SETTINGS_TEMPLATE_0_9 );

    OptionsPreferences optionPrefs = new OptionsPreferences( node );

    assertEquals( "org: true, com: false", optionPrefs.getGlobals() );
    assertEquals( "bitwise: true, curly: true, eqnull: true", optionPrefs.getOptions() );
    assertFalse( optionPrefs.hasChanged() );
  }

  @Test
  public void resourcePaths() {
    assertEquals( "", EnablementPreferences.getResourcePath( project ) );
    assertEquals( "test.js", EnablementPreferences.getResourcePath( file ) );
  }

}
