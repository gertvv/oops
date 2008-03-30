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

package nl.rug.ai.mas.oops.formula.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import nl.rug.ai.mas.oops.formula.*;

import java.math.BigInteger;

/**
 * Test functionality related to Agent.
 */
public class AgentTest {
	@Test public void idMapNotNullTest() {
		AgentIdMap map = new AgentIdMap();
		assertNotNull("AgentIdMap.getOrCreate() should not return null",
				map.getOrCreate("someAgent"));
	}

	@Test public void idMapSameNameTest() {
		AgentIdMap map = new AgentIdMap();
		AgentId id0 = map.getOrCreate("someAgent");
		AgentId id1 = map.getOrCreate("someAgent");
		assertEquals("AgentIdMap.getOrCreate() should get the same AgentId for" +
				" equals names", id0, id1);
	}

	@Test public void idMapDifferentNameTest() {
		AgentIdMap map = new AgentIdMap();
		AgentId id0 = map.getOrCreate("someAgent");
		AgentId id1 = map.getOrCreate("otherAgent");
		assertNotSame("AgentIdMap.getOrCreate() should get a different AgentId" +
				" for different names", id0, id1);
	}

	@Test public void referenceMatch() {
		Variable<Agent> avar = new Variable<Agent>("A");
		AgentReference aref = new AgentReference(avar, BigInteger.ONE);
		Agent a = new AgentId("1", BigInteger.ONE);
		Substitution s = aref.match(a);
		assertNotNull("AgentReference.match() should not return null", s);
		assertEquals("AgentReference.match() should substitute for it's own" +
				" variable", a, s.get(avar));
	}

	@Test public void referenceSubstitute() {
	}
}
