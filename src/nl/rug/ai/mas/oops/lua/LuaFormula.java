package nl.rug.ai.mas.oops.lua;

import org.luaj.vm.LFunction;
import org.luaj.vm.LuaState;

import nl.rug.ai.mas.oops.FormulaParser;
import nl.rug.ai.mas.oops.Prover;
import nl.rug.ai.mas.oops.formula.Formula;

public class LuaFormula {
	FormulaParser d_parser;
	Prover d_prover;

	public LuaFormula(FormulaParser adapter, Prover prover) {
		d_parser = adapter;
		d_prover = prover;
	}
	
	/**
	 * Register LuaFormula with the Lua VM. Leaves a constructor method on the stack.
	 * @param L
	 */
	public void register(LuaState L) {
		L.pushfunction(new LFunction() {
			public int invoke(LuaState L) {
				L.error("Not Implemented");
				return 0;
			}
		});
	}
	
	public Formula checkFormula(LuaState L, int pos) {
		String s = L.tostring(pos);

		if (!d_parser.parse(s)) {
			L.error(d_parser.getErrorCause().toString());
		}

		Formula f = d_parser.getFormula();
		if (!d_prover.validate(f)) {
			L.error("Formula failed to validate");
		}

		return f;
	}
}
