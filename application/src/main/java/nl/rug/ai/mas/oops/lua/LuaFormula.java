package nl.rug.ai.mas.oops.lua;

import nl.rug.ai.mas.oops.formula.Agent;
import nl.rug.ai.mas.oops.formula.AgentId;
import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.formula.FullSubstitution;
import nl.rug.ai.mas.oops.formula.Substitution;
import nl.rug.ai.mas.oops.formula.Variable;
import nl.rug.ai.mas.oops.parser.Context;
import nl.rug.ai.mas.oops.parser.FormulaParser;

import org.luaj.vm.LFunction;
import org.luaj.vm.LTable;
import org.luaj.vm.LUserData;
import org.luaj.vm.LuaState;

public class LuaFormula {
	FormulaParser d_parser;
	private LTable d_table;

	public LuaFormula() {
		d_parser = new FormulaParser(new Context());
	}
	
	/**
	 * Register LuaFormula with the Lua VM. Leaves a constructor method on the stack.
	 * @param L
	 */
	public void register(LuaState L) {
		d_table = new LTable();
		d_table.put("__index", d_table);
		d_table.put("substitute", new FunctionSubstitute());
		L.pushfunction(new Constructor());
	}
	
	private final class Constructor extends LFunction {
		public int invoke(LuaState L) {
			Formula f = checkFormula(L, 1);
			L.pushlvalue(new LUserData(f, d_table));
			return 1;
		}
	}
	
	private final class FunctionSubstitute extends LFunction {
		public int invoke(LuaState L) {
			Formula f = checkFormula(L, 1);
			LTable fTable =  L.totable(2);
			LTable aTable =  L.totable(3);
			Substitution<Formula> fSubst = checkFormulaSubstitution(L, fTable);
			Substitution<Agent> aSubst = checkAgentSubstitution(L, aTable);
			
			L.pushlvalue(new LUserData(f.substitute(new FullSubstitution(aSubst, fSubst)), d_table));
			return 1;
		}
	}
	
	public Formula checkFormula(LuaState L, int pos, FormulaParser parser) {
		String s = L.tostring(pos);

		return checkFormula(L, s, parser);
	}
	
	private Formula checkFormula(LuaState L, int pos) {
		return checkFormula(L, pos, d_parser);
	}

	private Formula checkFormula(LuaState L, String s, FormulaParser parser) {
		if (!parser.parse(s)) {
			L.error(parser.getErrorCause().toString());
		}

		return parser.getFormula();
	}
	
	private final class SubConstrFuncA extends LFunction {
		private Substitution<Agent> d_sub = new Substitution<Agent>();
		
		public int invoke(LuaState L) {
			Variable<Agent> v = d_parser.getContext().getAgentVarMap().getOrCreate(L.tostring(1));
			AgentId id = d_parser.getContext().getAgentIdMap().getOrCreate(L.tostring(2));
			d_sub.put(v, id);
			return 0;
		}
		
		public Substitution<Agent> getSubstitution() {
			return d_sub;
		}
	}

	public Substitution<Agent> checkAgentSubstitution(LuaState L, LTable table) {
		SubConstrFuncA scf = new SubConstrFuncA();
		table.foreach(L, scf, false);
		return scf.getSubstitution();
	}
	
	private final class SubConstrFunc extends LFunction {
		private Substitution<Formula> d_sub = new Substitution<Formula>();
		
		public int invoke(LuaState L) {
			Variable<Formula> v = d_parser.getContext().getFormulaVarMap().getOrCreate(L.tostring(1));
			Formula f = checkFormula(L, 2);
			d_sub.put(v, f);
			return 0;
		}
		
		public Substitution<Formula> getSubstitution() {
			return d_sub;
		}
	}

	public Substitution<Formula> checkFormulaSubstitution(LuaState L, LTable table) {
		SubConstrFunc scf = new SubConstrFunc();
		table.foreach(L, scf, false);
		return scf.getSubstitution();
	}
}