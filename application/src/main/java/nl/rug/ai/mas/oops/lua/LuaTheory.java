package nl.rug.ai.mas.oops.lua;

import nl.rug.ai.mas.oops.Prover;
import nl.rug.ai.mas.oops.TableauErrorException;
import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.theory.Theory;

import org.luaj.vm.LFunction;
import org.luaj.vm.LTable;
import org.luaj.vm.LUserData;
import org.luaj.vm.LuaState;

public class LuaTheory {
	private LuaFormula d_formula;
	private Prover d_prover;

	public LuaTheory(LuaFormula formula, Prover prover) {
		d_formula = formula;
		d_prover = prover;
	}

	public static Theory checkTheory(LuaState L, int pos) {
		Object o = L.touserdata(pos);
		
		if (!(o instanceof Theory)) {
			L.error("Expected a Theory");
		}
		Theory theory = (Theory)o;
		return theory;
	}
	
	/**
	 * Register LuaTheory with the Lua VM. Leaves a constructor method on the stack.
	 * @param L
	 */
	public void register(LuaState L) {
		final LTable table = new LTable();
		table.put("__index", table);
		table.put("add", new FunctionAdd());
		table.put("provable", new FunctionProvable());
		table.put("satisfiable", new FunctionSatisfiable());
		table.put("consistent", new FunctionConsistent());
		
		L.pushfunction(new Constructor(table));
	}
	
	private final class Constructor extends LFunction {
		private final LTable table;

		private Constructor(LTable table) {
			this.table = table;
		}

		public int invoke(LuaState L) {
			L.pushlvalue(new LUserData(new Theory(d_prover), table));
			return 1;
		}
	}

	private final class FunctionConsistent extends LFunction {
		public int invoke(LuaState L) {
			Theory theory = LuaTheory.checkTheory(L, 1);
		
			try {
				L.pushboolean(theory.consistent());
			} catch (TableauErrorException e) {
				L.error(e.toString());
			}
			
			return 1;
		}
	}

	private final class FunctionSatisfiable extends LFunction {
		public int invoke(LuaState L) {
			Theory theory = LuaTheory.checkTheory(L, 1);
			Formula f = d_formula.checkFormula(L, 2);
		
			try {
				L.pushboolean(theory.satisfiable(f));
			} catch (TableauErrorException e) {
				L.error(e.toString());
			}
			
			return 1;
		}
	}

	private final class FunctionProvable extends LFunction {
		public int invoke(LuaState L) {
			Theory theory = LuaTheory.checkTheory(L, 1);
			Formula f = d_formula.checkFormula(L, 2);
		
			try {
				L.pushboolean(theory.provable(f));
			} catch (TableauErrorException e) {
				L.error(e.toString());
			}
			
			return 1;
		}
	}

	private final class FunctionAdd extends LFunction {
		public int invoke(LuaState L) {
			Theory theory = LuaTheory.checkTheory(L, 1);
			Formula f = d_formula.checkFormula(L, 2);

			theory.add(f);
			
			return 0;
		}
	}
}