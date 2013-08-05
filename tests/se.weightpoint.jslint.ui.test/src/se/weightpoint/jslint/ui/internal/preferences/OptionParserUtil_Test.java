/*******************************************************************************
 * Copyright (c) 2012, 2013 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial implementation and API
 ******************************************************************************/
package se.weightpoint.jslint.ui.internal.preferences;

import java.util.List;

import org.junit.Test;

import se.weightpoint.jslint.ui.internal.preferences.OptionParserUtil;
import se.weightpoint.jslint.ui.internal.preferences.OptionParserUtil.Entry;

import se.weightpoint.jslint.json.JsonObject;
import se.weightpoint.jslint.json.JsonValue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class OptionParserUtil_Test {

  @Test
  public void createConfiguration() {
    JsonObject result = OptionParserUtil.createConfiguration( "opt1: true", "predef1: false" );

    assertEquals( "{\"opt1\":true,\"predef\":{\"predef1\":false}}",result.toString() );
  }

  @Test
  public void createConfiguration_withEmptyParameters() {
    JsonObject result = OptionParserUtil.createConfiguration( "", "" );

    assertEquals( "{}", result.toString() );
  }

  @Test( expected = NullPointerException.class )
  public void createConfiguration_withOptionsNull() {
    OptionParserUtil.createConfiguration( null, "" );
  }

  @Test( expected = NullPointerException.class )
  public void createConfiguration_withGlobalsNull() {
    OptionParserUtil.createConfiguration( "", null );
  }

  @Test( expected = NullPointerException.class )
  public void parseNull() {
    OptionParserUtil.parseOptionString( null );
  }

  @Test
  public void parseEmpty() {
    List<Entry> result = OptionParserUtil.parseOptionString( "" );

    assertTrue( result.isEmpty() );
  }

  @Test
  public void parseSingle() {
    List<Entry> result = OptionParserUtil.parseOptionString( "foo: true" );

    assertEquals( 1, result.size() );
    assertEquals( "foo", result.get( 0 ).name );
    assertEquals( JsonValue.TRUE, result.get( 0 ).value );
  }

  @Test
  public void parseTwoOptions() {
    List<Entry> result = OptionParserUtil.parseOptionString( "foo: true, bar: false" );

    assertEquals( 2, result.size() );
    assertEquals( "foo", result.get( 0 ).name );
    assertEquals( JsonValue.TRUE, result.get( 0 ).value );
    assertEquals( "bar", result.get( 1 ).name );
    assertEquals( JsonValue.FALSE, result.get( 1 ).value );
  }

  @Test
  public void parseAdditionalWhitespace() {
    List<Entry> result = OptionParserUtil.parseOptionString( "\tfoo : true  ,\n\t bar  : false  " );

    assertEquals( 2, result.size() );
    assertEquals( "foo", result.get( 0 ).name );
    assertEquals( JsonValue.TRUE, result.get( 0 ).value );
    assertEquals( "bar", result.get( 1 ).name );
    assertEquals( JsonValue.FALSE, result.get( 1 ).value );
  }

  // TODO check for illegal names
  @Test
  public void parseWhitespaceInName() {
    List<Entry> result = OptionParserUtil.parseOptionString( "foo bar: true" );

    assertEquals( 1, result.size() );
    assertEquals( "foo bar", result.get( 0 ).name );
    assertEquals( JsonValue.TRUE, result.get( 0 ).value );
  }

  @Test
  public void parseNumericValue() {
    List<Entry> result = OptionParserUtil.parseOptionString( "foo: 23" );

    assertEquals( 1, result.size() );
    assertEquals( "foo", result.get( 0 ).name );
    assertEquals( JsonValue.valueOf( 23 ), result.get( 0 ).value );
  }

  @Test
  public void parseIllegalValue() {
    List<Entry> result = OptionParserUtil.parseOptionString( "foo: (invalidJSON)" );

    assertEquals( 0, result.size() );
  }

}
