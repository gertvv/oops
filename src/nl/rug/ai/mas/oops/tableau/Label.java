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

/**
 * A semantic tableau Label.
 */
public interface Label {
	/**
	 * Match this Label to another Label, returning a substitution for the
	 * variables occuring in this Label.
	 */
	public NodeSubstitution match(Label other);
	/**
	 * Apply a substitution for variables to this Label, replacing occurences
	 * of variables with their substitution. This function should not modify
	 * existing objects, but create new ones instead.
	 */
	public Label substitute(NodeSubstitution s);
	/**
	 * Accept a Visitor to this label (Visitor pattern).
	 */
	public void accept(LabelVisitor v);
	/**
	 * Determine wether this contains any free variables.
	 * @return true if the label contains no free variables
	 */
	public boolean isConcrete();
}
