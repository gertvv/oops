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

public class NullAgent implements Agent {
	private static final int s_code = 0;

	public NullAgent() {
	}

	public Substitution<Agent> match(Agent other) {
		if (equals(other)) {
			return new Substitution<Agent>();
		}
		return null;
	}

	public Agent substitute(Substitution<Agent> s) {
		return this;
	}

	public boolean equals(Object o) {
		if (o instanceof NullAgent) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "NullAgent";
	}

	public boolean isConcrete() {
		return true;
	}

	public int code() {
		return s_code;
	}

	public int hashCode() {
		return s_code;
	}
}
