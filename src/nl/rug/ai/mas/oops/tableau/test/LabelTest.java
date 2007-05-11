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

package nl.rug.ai.mas.oops.tableau.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import nl.rug.ai.mas.oops.formula.*;
import nl.rug.ai.mas.oops.tableau.*;

/**
 * Test functionality related to Label.
 */
public class LabelTest {
	/**
	 * Label s.k_i should match null.(w0,null).(w1,1)
	 */
	@Test public void matchTest0() {
		// build template
		Variable<Label> l = new Variable<Label>("s");
		LabelReference lref = new LabelReference(l);
		Variable<World> k = new Variable<World>("k");
		WorldReference kref = new WorldReference(k);
		Variable<Agent> i = new Variable<Agent>("i");
		AgentReference iref = new AgentReference(i);
		Label tpl = new LabelInstance(lref, kref, iref);

		// build label
		World w0 = new WorldInstance();
		World w1 = new WorldInstance();
		Agent a0 = new AgentId("NoAgent");
		Agent a1 = new AgentId("1");
		Label lbl = new LabelInstance(
			new LabelInstance(new NullLabel(), w0, a0),
			w1, a1);

		assertNotNull(tpl.match(lbl));
	}
}
