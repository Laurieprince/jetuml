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
package org.jetuml.rendering.nodes;

import org.jetuml.diagram.DiagramElement;
import org.jetuml.diagram.Node;
import org.jetuml.diagram.nodes.NoteNode;
import org.jetuml.geom.Dimension;
import org.jetuml.geom.Rectangle;
import org.jetuml.rendering.DiagramRenderer;
import org.jetuml.rendering.StringRenderer;
import org.jetuml.rendering.ToolGraphics;
import org.jetuml.rendering.StringRenderer.Alignment;
import org.jetuml.rendering.StringRenderer.TextDecoration;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * An object to render a NoteNode.
 */
public final class NoteNodeRenderer extends AbstractNodeRenderer
{
	private static final int DEFAULT_WIDTH = 60;
	private static final int DEFAULT_HEIGHT = 40;
	private static final int FOLD_LENGTH = 8;
	private static final Color NOTE_COLOR = Color.color(0.9f, 0.9f, 0.6f); // Pale yellow
	private static final StringRenderer NOTE_VIEWER = StringRenderer.get(Alignment.TOP_LEFT, TextDecoration.PADDED);
	
	public NoteNodeRenderer(DiagramRenderer pParent)
	{
		super(pParent);
	}
	
	@Override
	public Dimension getDefaultDimension(Node pNode)
	{
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	@Override
	public void draw(DiagramElement pElement, GraphicsContext pGraphics)
	{
		Node node = (Node) pElement;
		ToolGraphics.strokeAndFillSharpPath(pGraphics, createNotePath(node), NOTE_COLOR, true);
		ToolGraphics.strokeAndFillSharpPath(pGraphics, createFoldPath(node), Color.WHITE, false);
		NOTE_VIEWER.draw(((NoteNode)node).getName(), pGraphics, 
				new Rectangle(node.position().getX(), node.position().getY(), DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}
	
	private Path createNotePath(Node pNode)
	{
		Path path = new Path();
		Rectangle bounds = getBounds(pNode);		
		path.getElements().addAll(
				new MoveTo(bounds.getX(), bounds.getY()),
				new LineTo(bounds.getMaxX() - FOLD_LENGTH, bounds.getY()),
				new LineTo(bounds.getMaxX(), bounds.getY() + FOLD_LENGTH),
				new LineTo(bounds.getMaxX(), bounds.getMaxY()),
				new LineTo(bounds.getX(), bounds.getMaxY()),
				new LineTo(bounds.getX(), bounds.getY()));
		return path;
	}
	
	/**
	 * Fills in note fold.
	 * @param pGraphics GraphicsContext in which to fill the fold
	 */
	private Path createFoldPath(Node pNode)
	{
		Rectangle bounds = getBounds(pNode);
		Path path = new Path();
		path.getElements().addAll(
				new MoveTo(bounds.getMaxX() - FOLD_LENGTH, bounds.getY()),
				new LineTo(bounds.getMaxX() - FOLD_LENGTH, bounds.getY() + FOLD_LENGTH),
				new LineTo(bounds.getMaxX(), bounds.getY() + FOLD_LENGTH),
				new LineTo(bounds.getMaxX() - FOLD_LENGTH, bounds.getY())
		);
		return path;
	}
	
	@Override
	protected Rectangle internalGetBounds(Node pNode)
	{
		Dimension textBounds = NOTE_VIEWER.getDimension(((NoteNode)pNode).getName()); 
		return new Rectangle(pNode.position().getX(), pNode.position().getY(), 
				Math.max(textBounds.width() + FOLD_LENGTH, DEFAULT_WIDTH), Math.max(textBounds.height(), DEFAULT_HEIGHT));
	}
}
