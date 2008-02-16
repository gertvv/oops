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

import nl.rug.ai.mas.oops.render.tree.Cell;
import java.awt.Dimension;
import javax.swing.JComponent;

public class ComponentCell implements Cell {
	private JComponent d_component;
	private int d_paddingX;
	private int d_paddingY;

	public ComponentCell(JComponent c, int paddingX, int paddingY) {
		d_component = c;
		d_paddingX = paddingX;
		d_paddingY = paddingY;
	}

	public ComponentCell(JComponent c) {
		this(c, 30, 30);
	}

	public int getWidth() {
		Dimension d = d_component.getPreferredSize();
		return d.width + d_paddingX;
	}

	public int getHeight() {
		Dimension d = d_component.getPreferredSize();
		return d.height + d_paddingY;
	}

	public JComponent getComponent() {
		return d_component;
	}
}
