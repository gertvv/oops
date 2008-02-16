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
import java.awt.Dimension;

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

	//protected void paintChildren(Graphics g) {
	//	super.paintChildren(g);
	//	// draw edges
	//}
}
