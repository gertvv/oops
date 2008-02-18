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

package nl.rug.ai.mas.oops.render.tree;

import java.util.Observer;
import java.util.Observable;
import java.awt.Point;
import java.awt.Dimension;

public class TreeStructuredDiagram<CellType extends Cell> implements Observer {
	private TreeStructure<CellType> d_tree;
	private Placement<CellType> d_placement;
	private boolean d_valid;
	private int d_coherence; // j from C3(j)

	public TreeStructuredDiagram(TreeStructure<CellType> tree,
			int coherence) {
		d_tree = tree;
		d_placement = null;
		d_valid = false;
		d_coherence = coherence;

		d_tree.addObserver(this);
	}

	public void update(Observable o, Object arg) {
		if (o == d_tree && arg == null) {
			d_valid = false;
		}
	}

	public Placement<CellType> getPlacement() {
		if (d_valid == false) {
			layout();
		}
		return d_placement;
	}

	public Dimension getSize() {
		if (d_valid == false) {
			layout();
		}

		int maxX = 0; int maxY = 0;
		int minX = d_placement.get(d_tree.getRoot()).x;
		int minY = d_placement.get(d_tree.getRoot()).y;
		for (CellType c : new IterableImpl<CellType>(d_tree.cellIterator())) {
			Point p = d_placement.get(c);
			if (p.x + c.getWidth() > maxX) {
				maxX = p.x + c.getWidth();
			}
			if (p.y + c.getHeight() > maxY) {
				maxY = p.y + c.getHeight();
			}
			if (p.x < minX) {
				minX = p.x;
			}
			if (p.y < minY) {
				minY = p.y;
			}
		}

		return new Dimension(maxX - minX, maxY - minY);
	}

	private void layout() {
		d_placement = subOpt(init());
		// d_valid = true;
	}

	private int d_freeX;

	private Placement<CellType> init() {
		d_freeX = 0;
		Placement<CellType> placement = new Placement<CellType>();
		init(d_tree.getRoot(), placement, 0);
		return placement;
	}

	private void init(CellType v, Placement<CellType> p, int freeY) {
		if (d_tree.childCount(v) == 0) {
			p.put(v, new Point(d_freeX, freeY));
			d_freeX += v.getWidth();
		} else {
			CellType vFirst = d_tree.firstChild(v);
			for(CellType s : new IterableImpl<CellType>(
					d_tree.childIterator(v))) {
				init(s, p, freeY + v.getHeight());
				p.put(v, new Point(
					p.get(vFirst).x +
					Math.min(d_coherence, p.get(s).x - p.get(vFirst).x),
					freeY));
				d_freeX = Math.max(d_freeX, p.get(v).x + v.getWidth());
			}
			// according to Miyadera the following should be within the
			// for-loop, but that does not seem to make sense (how would
			// p(vLast) ever be initialized?
			/*
			CellType vFirst = d_tree.firstChild(v);
			CellType vLast = d_tree.lastChild(v);
			p.put(v, new Point(
				p.get(vFirst).x +
				Math.min(d_coherence, p.get(vLast).x - p.get(vFirst).x),
				freeY));
			d_freeX = Math.max(d_freeX, p.get(v).x + v.getWidth());
			*/
		}
	}

	private Placement<CellType> subOpt(Placement<CellType> init) {
		return init;
	}
}
