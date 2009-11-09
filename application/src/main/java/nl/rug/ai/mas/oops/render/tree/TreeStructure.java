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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

public class TreeStructure<CellType extends Cell> extends Observable {
	ArrayList<CellType> d_cells;
	HashMap<CellType, ArrayList<Edge<CellType>>> d_edges;
	HashMap<CellType, CellType> d_reverse;
	CellType d_root;

	public TreeStructure(CellType root) {
		super();
		d_cells = new ArrayList<CellType>();
		d_edges = new HashMap<CellType, ArrayList<Edge<CellType>>>();
		d_reverse = new HashMap<CellType, CellType>();
		d_root = root;
		d_cells.add(root);
	}

	public void addCell(CellType cell, CellType parent) {
		d_cells.add(cell);
		ArrayList<Edge<CellType>> edgeList = d_edges.get(parent);
		if (edgeList == null) {
			edgeList = new ArrayList<Edge<CellType>>();
			d_edges.put(parent, edgeList);
		}
		edgeList.add(new Edge<CellType>(parent, cell));
		d_reverse.put(cell, parent);

		notifyObservers(); // changes have been made
	}

	public CellType getRoot() {
		return d_root;
	}

	public Iterator<CellType> cellIterator() {
		return d_cells.iterator();
	}

	public Iterable<Edge<CellType>> edgeIterable() {
		ArrayList<Edge<CellType>> edges = new ArrayList<Edge<CellType>>();
		for (ArrayList<Edge<CellType>> e : d_edges.values()) {
			edges.addAll(e);
		}
		return edges;
	}

	public Iterator<Edge<CellType>> edgeIterator(CellType parent) {
		return d_edges.get(parent).iterator();
	}

	public Iterator<CellType> childIterator(CellType parent) {
		return new ChildIterator<CellType>(this, parent);
	}

	public int childCount(CellType parent) {
		ArrayList<Edge<CellType>> l = d_edges.get(parent);
		if (l == null) {
			return 0;
		}
		return l.size();
	}

	public CellType firstChild(CellType parent) {
		ArrayList<Edge<CellType>> l = d_edges.get(parent);
		if (l == null || l.size() == 0) {
			return null;
		}
		return l.get(0).getDestination();
	}

	public CellType lastChild(CellType parent) {
		ArrayList<Edge<CellType>> l = d_edges.get(parent);
		if (l == null || l.size() == 0) {
			return null;
		}
		return l.get(l.size() - 1).getDestination();
	}

	public CellType getParent(CellType child) {
		return d_reverse.get(child);
	}

	public int index(CellType cell) {
		CellType parent = getParent(cell);
		int i = 1;
		if (parent == null) {
			return 0;
		} else {
			for (CellType c : new IterableImpl<CellType>(
					childIterator(parent))) {
				if (c == cell) {
					return i;
				}
				++i;
			}
		}
		return i;
	}

	public int level(CellType cell) {
		int i = 0;
		while (cell != d_root) {
			cell = getParent(cell);
			++i;
		}
		return i;
	}
}
