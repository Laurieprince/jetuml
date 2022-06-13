/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2020, 2021 by McGill University.
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

package org.jetuml.diagram.builder;

import org.jetuml.diagram.Diagram;
import org.jetuml.diagram.DiagramType;
import org.jetuml.diagram.builder.constraints.ConstraintSet;
import org.jetuml.diagram.builder.constraints.EdgeConstraints;
import org.jetuml.diagram.builder.constraints.StateDiagramEdgeConstraints;

/**
 * A builder for state diagrams.
 */
public class StateDiagramBuilder extends DiagramBuilder
{
	/**
	 * Creates a new builder for state diagrams.
	 * 
	 * @param pDiagram The diagram to wrap around.
	 * @pre pDiagram != null;
	 */
	private static final ConstraintSet CONSTRAINTS = new ConstraintSet(
			
			EdgeConstraints.noteEdge(),
			EdgeConstraints.noteNode(),
			EdgeConstraints.maxEdges(2),
			StateDiagramEdgeConstraints.noEdgeFromFinalNode(),
			StateDiagramEdgeConstraints.noEdgeToInitialNode()
		);
			
	/**
	 * Creates a new builder for state diagrams.
	 * 
	 * @param pDiagram The diagram to wrap around.
	 * @pre pDiagram != null && pDiagram.getType() == DiagramType.STATE
	 */
	public StateDiagramBuilder( Diagram pDiagram )
	{
		super( pDiagram );
		assert pDiagram.getType() == DiagramType.STATE;
	}
	
	@Override
	protected ConstraintSet getEdgeConstraints()
	{
		return CONSTRAINTS;
	}
}