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

import nl.rug.ai.mas.oops.formula.*;

/**
 * A reference to a Label variable. May be used in place of a Label instance.
 */
public class LabelReference extends VariableReference<Label>
implements Label {
	public LabelReference(Variable<Label> l) {
		super(l);
	}

	/**
	 * A LabelReference matches any other Label. A substitution is created in
	 * which other is substituted for this.
	 */
	public NodeSubstitution match(Label other) {
		NodeSubstitution s = new NodeSubstitution();
		s.getLabelSubstitution().put(get(), other);
		return s;
	}

	/**
	 * Return the substitute for this variable, if such a substitute is present
	 * in s. Otherwise the variable itself is returned.
	 */
	public Label substitute(NodeSubstitution s) {
		Label l = s.getLabelSubstitution().get(get());
		return (l != null ? l : this);
	}

	public void accept(LabelVisitor v) {
		v.visitLabelReference(this);
	}

	/**
	 * A reference to a variable is not concrete.
	 * @return false
	 */
	public boolean isConcrete() {
		return false;
	}
}
