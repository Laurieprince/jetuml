/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2020 by McGill University.
 *     
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *******************************************************************************/
package org.jetuml.viewers.edges;

import static org.jetuml.viewers.FontMetrics.DEFAULT_FONT_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

import org.jetuml.application.UserPreferences;
import org.jetuml.application.UserPreferences.IntegerPreference;
import org.jetuml.diagram.Diagram;
import org.jetuml.diagram.DiagramType;
import org.jetuml.diagram.Edge;
import org.jetuml.diagram.edges.DependencyEdge;
import org.jetuml.diagram.nodes.ClassNode;
import org.jetuml.geom.Rectangle;
import org.jetuml.viewers.RenderingFacade;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TestDependencyEdgeViewer
{
	private static int userDefinedFontSize;
	
	private ClassNode aNode1;
	private ClassNode aNode2;
	private DependencyEdge aEdge;
	private Diagram aDiagram;
	private DependencyEdgeViewer aDependencyEdgeViewer = new DependencyEdgeViewer();
	
	@BeforeAll
	public static void setupClass()
	{
		userDefinedFontSize = UserPreferences.instance().getInteger(UserPreferences.IntegerPreference.fontSize);
		UserPreferences.instance().setInteger(IntegerPreference.fontSize, DEFAULT_FONT_SIZE);
	}
	
	@AfterAll
	public static void restorePreferences()
	{
		UserPreferences.instance().setInteger(IntegerPreference.fontSize, userDefinedFontSize);
	}
	
	@BeforeEach
	public void setup()
	{
		aNode1 = new ClassNode(); // Bounds [x=0,y=0, w=100, h=60]
		aNode2 = new ClassNode(); // Bounds [x=200,y=0, w=100, h=60]
		aEdge = new DependencyEdge();
		aDiagram = new Diagram(DiagramType.CLASS);
		
		aDiagram.addRootNode(aNode1);
		aDiagram.addRootNode(aNode2);
		aEdge.connect(aNode1, aNode2, aDiagram);
		aDiagram.addEdge(aEdge);
	}
	
	@Test
	public void testEdgeViewBounds()
	{
		aNode2.translate(200, 0);
		assertEquals(new Rectangle(99,23,102,12), RenderingFacade.getBounds(aEdge));
	}
	
	@ParameterizedTest
	@CsvSource(value = {
			"apple banana orange kiwi peach grape raspberry, 1000, 100, 1", 
			"apple banana orange kiwi peach grape, 250, 100, 2",
			"apple banana orange kiwi peach grape, 200, 200, 3",
			"apple banana orange kiwi peach grape raspberry, 100, 0, 4"
	})
	public void testWrapLabel(String pString, int pDistanceInX, int pDistanceInY, int pExpectedNumberOfLines)
	{
		aNode2.translate(pDistanceInX, pDistanceInY);
		aEdge.setMiddleLabel(pString);
		String label = wrapLabel(aEdge);
		int numberOfLines = (int)label.chars().filter(c -> c == '\n').count() + 1;
		assertEquals(pExpectedNumberOfLines, numberOfLines);
	}
	
	private String wrapLabel(Edge pEdge) 
	{
		try 
		{
			Method method = LabeledStraightEdgeViewer.class.getDeclaredMethod("wrapLabel", Edge.class);
			method.setAccessible(true);
			String label = (String)method.invoke(aDependencyEdgeViewer, pEdge);
			return label;
		} 
		catch (ReflectiveOperationException e)
		{
			assert false;
			fail();
			return "";
		}
	}
}
