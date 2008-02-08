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

package nl.rug.ai.mas.oops.formula;

public class Proposition implements PropositionalF {
	private String d_name;
	
	public Proposition(String name) {
		d_name = name;
	}

	public String getName() {
		return d_name;
	}

	public String toString() {
		return d_name;
	}

	/**
	 * Propositions are only considered equal if they are references to the same
	 * Proposition object.
	 */
	public boolean equals(Object other) {
		return this == other;
	}

	public FullSubstitution match(Formula f) {
		if (equals(f)) {
			return new FullSubstitution();
		}
		return null;
	}

	public Formula substitute(FullSubstitution s) {
		return this;
	}

	public Formula opposite() {
		return new Negation(this);
	}

	public boolean isSimple() {
		return true;
	}

	public boolean isConcrete() {
		return true;
	}
}
