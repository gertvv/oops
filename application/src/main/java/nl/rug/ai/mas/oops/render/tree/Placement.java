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

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

@SuppressWarnings("serial")
public class Placement<CellType extends Cell> 
extends HashMap<CellType, Point> {
	@SuppressWarnings("unchecked")
	public Placement<CellType> copy() {
		return (Placement<CellType>)clone();
	}

	/**
	 * Retreive the set U_y of cells with the same y coordinate in the
	 * placement. */
	public List<CellType> getByY(int y) {
		List<CellType> list = new ArrayList<CellType>();
		for (Map.Entry<CellType, Point> entry : entrySet()) {
			if (entry.getValue().y == y) {
				list.add(entry.getKey());
			}
		}
		Collections.sort(list, new CompareByX());
		return list;
	}

	private class CompareByX implements Comparator<CellType> {
		public int compare(CellType o1, CellType o2) {
			int x1 = Placement.this.get(o1).x;
			int x2 = Placement.this.get(o2).x;
			return x1 - x2;
		}
	}
}
