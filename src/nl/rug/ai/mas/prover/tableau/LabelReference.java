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

/**
 * A reference to a Label variable.
 */
public class LabelReference extends VariableReference<Label>
implements Label {
	public LabelReference(Variable<Label> l) {
		super(l);
	}

	public LabelSubstitution match(Label other) {
		LabelSubstitution s = new LabelSubstitution();
		s.put(get(), other);
		return s;
	}

	public Label substitute(LabelSubstitution s) {
		Label l = s.get(get());
		return (l != null ? l : this);
	}
}
