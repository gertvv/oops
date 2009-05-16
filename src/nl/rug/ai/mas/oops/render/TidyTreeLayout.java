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

import nl.rug.ai.mas.oops.render.tree.*;

import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Container;

import java.util.Map;
import java.util.ArrayList;

public class TidyTreeLayout implements LayoutManager {
	private TreeStructure<ComponentCell> d_tree = null;
	private TreeStructuredDiagram<ComponentCell> d_tsd = null;

	public void setRoot(ComponentCell c) {
		d_tree = new TreeStructure<ComponentCell>(c);
		d_tsd = new TreeStructuredDiagram<ComponentCell>(d_tree, 100);
	}

	public void add(ComponentCell c, ComponentCell p) {
		d_tree.addCell(c, p);
	}

	public void layoutContainer(Container parent) {
		if (d_tsd == null) {
			return;
		}
		Placement<ComponentCell> p = d_tsd.getPlacement();
		for (Map.Entry<ComponentCell, Point> e : p.entrySet()) {
			Point q = e.getValue();
			Dimension d = e.getKey().getComponent().getPreferredSize();
			e.getKey().getComponent().setBounds(q.x, q.y, d.width, d.height);
		}
	}

	public Dimension minimumLayoutSize(Container parent) {
		if (d_tsd == null) {
			return new Dimension(0, 0);
		}
		return d_tsd.getSize();
	}

	public Dimension preferredLayoutSize(Container parent) {
		if (d_tsd == null) {
			return new Dimension(0, 0);
		}
		return d_tsd.getSize();
	}

	public Iterable<Edge<ComponentCell>> edgeIterable() {
		if (d_tree != null) {
			return d_tree.edgeIterable();
		}
		return new ArrayList<Edge<ComponentCell>>();
	}

	public void addLayoutComponent(String name, Component comp) {
	}

	public void removeLayoutComponent(Component comp) {
	}
}
