package nl.rug.ai.mas.oops.test;

import static org.junit.Assert.*;
import nl.rug.ai.mas.oops.FormulaParser;
import nl.rug.ai.mas.oops.formula.BiImplication;
import nl.rug.ai.mas.oops.formula.Conjunction;
import nl.rug.ai.mas.oops.formula.Context;
import nl.rug.ai.mas.oops.formula.Disjunction;
import nl.rug.ai.mas.oops.formula.FormulaReference;
import nl.rug.ai.mas.oops.formula.Implication;
import nl.rug.ai.mas.oops.formula.Negation;
import nl.rug.ai.mas.oops.formula.Proposition;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
	private Context d_context;
	private FormulaParser d_parser;
	
	@Before
	public void setUp() {
		d_context = new Context();
		d_parser = new FormulaParser(d_context);
	}
	
	@Test
	public void testParseProposition() {
		assertTrue(d_parser.parse("p"));
		assertEquals(d_context.getPropositionMap().getOrCreate("p"), d_parser.getFormula());
	}
	
	@Test
	public void testParseVariable() {
		assertTrue(d_parser.parse("V"));
		assertTrue(d_parser.getFormula() instanceof FormulaReference);
		FormulaReference r = (FormulaReference) d_parser.getFormula();
		assertEquals("V", r.toString());
	}
	
	@Test
	public void testParseNegation() {
		Proposition p = d_context.getPropositionMap().getOrCreate("p");
		
		assertTrue(d_parser.parse("~ p"));
		assertEquals(new Negation(p), d_parser.getFormula());
	}
	
	@Test
	public void testParseBinary() {
		Proposition p = d_context.getPropositionMap().getOrCreate("p");
		Proposition q  = d_context.getPropositionMap().getOrCreate("q");
		
		assertTrue(d_parser.parse("p | q"));
		assertEquals(new Disjunction(p, q), d_parser.getFormula());
		
		assertTrue(d_parser.parse("p & q"));
		assertEquals(new Conjunction(p, q), d_parser.getFormula());
		
		assertTrue(d_parser.parse("p > q"));
		assertEquals(new Implication(p, q), d_parser.getFormula());
		
		assertTrue(d_parser.parse("p = q"));
		assertEquals(new BiImplication(p, q), d_parser.getFormula());
	}
	
	// TODO: test precedence of operators, etc
	
	@Test
	public void testParseModal() {
		assertTrue(d_parser.parse("# p"));
		assertTrue(d_parser.parse("#_1 p"));
		assertTrue(d_parser.parse("#_V p"));
		assertFalse(d_parser.parse("#_a p"));
	}
}
