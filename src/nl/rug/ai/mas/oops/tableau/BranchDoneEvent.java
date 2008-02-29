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
package nl.rug.ai.mas.oops.tableau;

/**
 * Event that indicates the tableau system is done processing a Branch. This
 * class is different from BranchOpenEvent and BranchClosedEvent in that it is
 * called for non-leaf branches as well as for leafs.
 */
public class BranchDoneEvent implements TableauEvent {
	private Branch d_branch;

	public BranchDoneEvent(Branch b) {
		d_branch = b;
	}

	public Branch getBranch() {
		return d_branch;
	}

	public String toString() {
		return getClass().getSimpleName() + ": " + d_branch;
	}
}
