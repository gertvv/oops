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

public class LabelSubstitution {
	Substitution<Label> d_lsub;
	Substitution<World> d_wsub;
	Substitution<Agent> d_asub;

	public FullSubstitution() {
		d_lsub = new Substitution<Label>();
		d_wsub = new Substitution<World>();
		d_asub = new Substitution<Agent>();
	}

	public FullSubstitution(Substitution<World> w, Substitution<Agent> a) {
		d_lsub = new Substitution<Label>();
		d_wsub = w;
		d_asub = a;
	}

	public boolean merge(LabelSubstitution other) {
		if (!d_lsub.merge(other.d_lsub))
			return false;
		if (!d_wsub.merge(other.d_wsub))
			return false;
		if (!d_asub.merge(other.d_asub))
			return false;
		return true;
	}

	public Label put(Variable<Label> k, Label v) {
		return d_lsub.put(k, v);
	}

	public Label get(Variable<Label> k) {
		return d_lsub.get(k);
	}

	public World put(Variable<World> k, World v) {
		return d_wsub.put(k, v);
	}

	public World get(Variable<World> k) {
		return d_wsub.get(k);
	}

	public Agent put(Variable<Agent> k, Agent v) {
		return d_asub.put(k, v);
	}

	public Agent get(Variable<Agent> k) {
		return d_asub.get(k);
	}

	public String toString() {
		return d_lsub.toString() + d_wsub.toString() + d_asub.toString();
	}
}
