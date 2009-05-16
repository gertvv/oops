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
import java.util.List;

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
		}
	}

	private Placement<CellType> subOpt(Placement<CellType> init) {
		Placement<CellType> placement = new Placement<CellType>();
		subOpt(d_tree.getRoot(), init, placement);
		return placement;
	}

	private void subOpt(CellType q, Placement<CellType> in,
			Placement<CellType> out) {
		if (d_tree.childCount(q) == 0) {
			shift(q, in, out);
		} else {
			for(CellType p : new IterableImpl<CellType>(
					d_tree.childIterator(q))) {
				subOpt(p, in, out);
				in = out.copy();
			}
			// re-evaluate pi_in_x(q) satisfying C3(j)
			int p1x = in.get(d_tree.firstChild(q)).x;
			int pkx = in.get(d_tree.lastChild(q)).x;
			int qx = p1x + Math.min(d_coherence, pkx - p1x);
			in.put(q, new Point(qx, in.get(q).y));
			shift(q, in, out);
		}
	}

	private void shift(CellType q, Placement<CellType> in,
			Placement<CellType> out) {
		int move = 0;
		if (d_tree.index(q) != 1) {
			move = leftMovement(q, in);
		}
		moveLeft(q, move, in, out);
	}

	private int leftMovement(CellType q, Placement<CellType> placement) {
		List<CellType> uj = placement.getByY(placement.get(q).y);
		int idx = uj.indexOf(q);
		if (idx == 0) {
			return placement.get(q).y;
		}
		CellType c = (CellType)uj.get(idx - 1);
		return placement.get(q).y - placement.get(c).y - c.getWidth();
	}

	private void moveLeft(CellType q, int dx,
			Placement<CellType> in, Placement<CellType> out) {
		out.clear();
		out.putAll(in);
		if (dx > 0) {
			moveLeft(q, dx, out);
		}
	}

	private void moveLeft(CellType q, int dx, Placement<CellType> p) {
		Point point = p.get(q);
		p.put(q, new Point(point.x - dx, point.y));
		for (CellType r : new IterableImpl<CellType>(d_tree.childIterator(q))) {
			moveLeft(r, dx, p);
		}
	}
}
