/**
  * This program (working title: MAS Prover) is an automated tableaux prover
  * for epistemic logic (S5n).
  * Copyright (C) 2007  Elske van der Vaart and Gert van Valkenhoef

  * This program is free software; you can redistribute it and/or modify it
  * under the terms of the GNU General Public License version 2 as published
  * by the Free Software Foundation.

  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.

  * You should have received a copy of the GNU General Public License along
  * with this program; if not, write to the Free Software Foundation, Inc.,
  * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
  */

package nl.rug.ai.mas.oops.render;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Tree extends JComponent {
	public Tree() {
		super();
		setLayout(new TreeLayout());
	}

	public LayoutComponent addComponent(JComponent c, LayoutComponent parent) {
		LayoutComponent component = new LayoutComponent(c);
		add(component);
		if (parent != null)
			parent.addRelation(component);
		return component;
	}

	protected void paintChildren(Graphics g) {
		super.paintChildren(g);
		if (g instanceof Graphics2D) {
			((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
			((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
			((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}

		// draw edges
		for (java.awt.Component c : getComponents()) {
			LayoutComponent component = (LayoutComponent)c;
			for (LayoutRelation r : component.getRelations()) {
				LayoutComponent es = (LayoutComponent)r.getSourceInLayout();
				LayoutComponent ed = (LayoutComponent)r.getDestinationInLayout();
				int xs = (int)(es.getXInLayout() + es.getWidthInLayout() / 2);
				int ys = (int)(es.getYInLayout() + es.getHeightInLayout());
				int xd = (int)(ed.getXInLayout() + ed.getWidthInLayout() / 2);
				int yd = (int)(ed.getYInLayout());
				g.drawLine(xs, ys, xd, yd);
			}
		}
	}
}
