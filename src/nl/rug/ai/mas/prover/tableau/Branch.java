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

package nl.rug.ai.mas.prover.tableau;

import java.util.*;
import nl.rug.ai.mas.prover.formula.*;

public class Branch {
	private Branch d_parent;
	private Vector<Formula> d_current;

	public Branch(Branch parent) {
		d_parent = parent;
		d_current = new Vector<Formula>();
	}

	public boolean contains(Formula f) {
		if (d_current.contains(f))
			return true;
		if (d_parent != null)
			return d_parent.contains(f);
		return false;
	}

	public void add(Formula f) {
		d_current.add(f);
	}

	public String toString() {
		String s = new String();
		if (d_parent == null)
			s = "\t***\n";
		else
			s = d_parent.toString() + "\t---\n";
		for (Formula f : d_current) {
			s += f.toString() + "\n";
		}
		return s;
	}
}
