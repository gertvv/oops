package nl.rug.ai.mas.oops.theory.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import nl.rug.ai.mas.oops.SimpleProver;
import nl.rug.ai.mas.oops.formula.Conjunction;
import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.formula.Proposition;
import nl.rug.ai.mas.oops.parser.PropositionMap;
import nl.rug.ai.mas.oops.theory.Theory;

public class TheoryTest {
	Theory d_th;
	PropositionMap d_propMap;
	
	@Before
	public void setUp() {
		d_th = new Theory(SimpleProver.build());
		d_propMap = new PropositionMap();
	}
	
	@Test
	public void initialValuesTest() {
		assertEquals(Collections.EMPTY_SET, d_th.getFormulas());
	}

	@Test
	public void addTest() {
		d_th.add(d_propMap.getOrCreate("p"));
		assertEquals(Collections.singleton(d_propMap.getOrCreate("p")), d_th.getFormulas());
		d_th.add(d_propMap.getOrCreate("p"));
		assertEquals(Collections.singleton(d_propMap.getOrCreate("p")), d_th.getFormulas());
	}
	
	@Test
	public void removeTest() {
		d_th.add(d_propMap.getOrCreate("p"));
		assertFalse(d_th.remove(d_propMap.getOrCreate("q")));
		assertTrue(d_th.remove(d_propMap.getOrCreate("p")));
		assertTrue(d_th.getFormulas().isEmpty());
	}
	
	@Test
	public void emptyAsFormulaTest() {
		assertEquals(null, d_th.asFormula());
	}
	
	@Test
	public void asFormulaTest() {
		d_th.add(d_propMap.getOrCreate("p"));
		d_th.add(d_propMap.getOrCreate("q"));
		d_th.add(d_propMap.getOrCreate("r"));
		Formula f = d_th.asFormula();
		
		// top-level formula should be a conjunction
		assertTrue(f instanceof Conjunction);
		Conjunction c = (Conjunction)f;
		
		// One of the children should be a Proposition, the other a Conjunction
		assertTrue(c.getLeft() instanceof Conjunction || c.getRight() instanceof Conjunction);
		Conjunction cn = (c.getLeft() instanceof Conjunction) ?
				(Conjunction)c.getLeft() : (Conjunction)c.getRight();
		assertTrue(c.getLeft() instanceof Proposition || c.getRight() instanceof Proposition);
		List<Proposition> prop = new ArrayList<Proposition>();
		prop.add((c.getLeft() instanceof Proposition) ?
				(Proposition)c.getLeft() : (Proposition)c.getRight());
		
		// Nested children should be propositions
		assertTrue(cn.getLeft() instanceof Proposition && cn.getRight() instanceof Proposition);
		prop.add((Proposition)cn.getLeft());
		prop.add((Proposition)cn.getRight());
		
		// All propositions in the theory should be present
		assertTrue(prop.containsAll(d_th.getFormulas()));
	}
}
