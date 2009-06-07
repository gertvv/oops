package nl.rug.ai.mas.oops.tableau.test;

import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.parser.Context;
import nl.rug.ai.mas.oops.parser.FormulaParser;
import nl.rug.ai.mas.oops.tableau.MultiModalValidator;
import nl.rug.ai.mas.oops.tableau.FormulaValidator;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class MultiModalValidatorTest {
	private FormulaParser d_parser;

	@Before
	public void buildParser() {
		d_parser = new FormulaParser(new Context());
	}

	public Formula parse(String s) {
		if (!d_parser.parse(s)) {
			throw new RuntimeException(d_parser.getErrorCause());
		}
		return d_parser.getFormula();
	}

	@Test
	public void testValidate() {
		FormulaValidator v = new MultiModalValidator();
		assertTrue(v.validate(parse("p")));
		assertTrue(v.validate(parse("~p & V")));
		assertTrue(v.validate(parse("#_1 x")));
		assertFalse(v.validate(parse("# x")));
	}
}
