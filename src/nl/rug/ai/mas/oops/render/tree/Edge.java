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

public class Edge<CellType extends Cell> {
	CellType d_source;
	CellType d_destination;

	public Edge(CellType source, CellType destination) {
		d_source = source;
		d_destination = destination;
	}

	public CellType getSource() {
		return d_source;
	}

	public CellType getDestination() {
		return d_destination;
	}
}
