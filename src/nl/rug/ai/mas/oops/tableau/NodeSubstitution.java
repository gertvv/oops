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

import java.util.*;
import nl.rug.ai.mas.oops.formula.*;

public class NodeSubstitution {
	Substitution<Label> d_lsub;
	Substitution<World> d_wsub;
	Substitution<Agent> d_asub;
	Substitution<Formula> d_fsub;
	Constraint d_constraint;

	public NodeSubstitution() {
		d_lsub = new Substitution<Label>();
		d_wsub = new Substitution<World>();
		d_asub = new Substitution<Agent>();
		d_fsub = new Substitution<Formula>();
		d_constraint = null;
	}

	public NodeSubstitution(Constraint c) {
		this();
		d_constraint = c;
	}

	public boolean merge(LabelSubstitution ls, FullSubstitution fs) {
		if (!d_lsub.merge(ls.getLabelSubstitution()))
			return false;
		if (!d_wsub.merge(ls.getWorldSubstitution()))
			return false;
		if (!d_fsub.merge(fs.getFormulaSubstitution()))
			return false;

		if (!d_asub.merge(ls.getAgentSubstitution()))
			return false;
		if (!d_asub.merge(fs.getAgentSubstitution()))
			return false;

		if (d_constraint != null && !d_constraint.validate(this))
			return false;

		return true;
	}

	public Substitution<Label> getLabelSubstitution() {
		return d_lsub;
	}

	public Substitution<World> getWorldSubstitution() {
		return d_wsub;
	}

	public Substitution<Agent> getAgentSubstitution() {
		return d_asub;
	}

	public Substitution<Formula> getFormulaSubstitution() {
		return d_fsub;
	}

	public String toString() {
		return d_lsub.toString() + d_wsub.toString() + d_asub.toString()
			+ d_fsub.toString();
	}
}

