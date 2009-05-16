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
 * A null Label, used to terminate a label chain in a way that a Label variable
 * will match.
 */
public class NullLabel implements Label {
	public NullLabel() {
	}

	/**
	 * A NullLabel matches only those Label objects it equals().
	 * @param o The Label to match.
	 * @return The empty subsitution if it matches o, null otherwise.
	 */
	public NodeSubstitution match(Label o) {
		if (equals(o)) {
			return new NodeSubstitution();
		}
		return null;
	}

	/**
	 * Substitute all occuring variables.
	 * @return this.
	 */ 
	public Label substitute(NodeSubstitution s) {
		return this;
	}

	public void accept(LabelVisitor v) {
		v.visitNullLabel(this);
	}

	/**
	 * A NullLabel equals() all other NullLabel instances.
	 */
	public boolean equals(Object o) {
		if (o instanceof NullLabel) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * The hashcode for a NullLabel is always 0.
	 */
	public int hashCode() {
		return 0;
	}

	public String toString() {
		return "NullLabel";
	}

	public boolean isConcrete() {
		return true;
	}
}
