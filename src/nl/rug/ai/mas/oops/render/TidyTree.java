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
import java.awt.Point;
import java.awt.Dimension;

import nl.rug.ai.mas.oops.render.tree.Edge;

public class TidyTree extends JComponent {
	private TidyTreeLayout d_layout;

	public TidyTree() {
		super();
		d_layout = new TidyTreeLayout();
		setLayout(d_layout);
	}

	public ComponentCell addComponent(JComponent c, ComponentCell parent) {
		add(c);
		ComponentCell cell = new ComponentCell(c);
		if (parent == null) {
			d_layout.setRoot(cell);
		} else {
			d_layout.add(cell, parent);
		}
		return cell;
	}

	public Dimension getMinimumSize() {
		return d_layout.minimumLayoutSize(this);
	}

	public Dimension getPreferredSize() {
		return d_layout.preferredLayoutSize(this);
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
		for (Edge<ComponentCell> edge : d_layout.edgeIterable()) {
			JComponent cs = edge.getSource().getComponent();
			JComponent cd = edge.getDestination().getComponent();

			Point p1 = cs.getLocation();
			Dimension d1 = cs.getSize();
			Point p2 = cd.getLocation();
			Dimension d2 = cd.getSize();

			p1.x += d1.width / 2;
			p1.y += d1.height;
			p2.x += d2.width / 2;
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
}
