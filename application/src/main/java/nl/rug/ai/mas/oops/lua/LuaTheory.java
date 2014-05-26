package nl.rug.ai.mas.oops.lua;

import java.awt.FontFormatException;
import java.io.IOException;

import nl.rug.ai.mas.oops.AxiomSystem;
import nl.rug.ai.mas.oops.ObserveProver;
import nl.rug.ai.mas.oops.Prover;
import nl.rug.ai.mas.oops.TableauErrorException;
import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.model.ModelConstructingObserver;
import nl.rug.ai.mas.oops.parser.FormulaParser;
import nl.rug.ai.mas.oops.render.TableauObserverSwing;
import nl.rug.ai.mas.oops.tableau.SystemOutObserver;
import nl.rug.ai.mas.oops.theory.Theory;

import org.luaj.vm.LFunction;
import org.luaj.vm.LTable;
import org.luaj.vm.LUserData;
import org.luaj.vm.LuaState;

public class LuaTheory {
	private LuaFormula d_formula;

	public LuaTheory(LuaFormula formula) {
		d_formula = formula;
	}
	
	private static class TheoryData {
		private Theory d_theory;
		private FormulaParser d_parser;
		private ModelConstructingObserver d_constructor;

		public TheoryData(Prover prover) {
			d_theory = new Theory(prover);
			d_parser = new FormulaParser(prover.getContext());
			d_constructor = new ModelConstructingObserver(prover.getModel());
		}
	
		public Theory getTheory() {
			return d_theory;
		}
		
		public FormulaParser getParser() {
			return d_parser;
		}
		
		public ModelConstructingObserver getModelConstructor() {
			return d_constructor;
		}
		
		public String toString() {
			return d_theory.getProver().getAxiomSystem().toString() + d_theory.toString();
		}
	}

	public static TheoryData checkTheory(LuaState L, int pos) {
		Object o = L.touserdata(pos);
		
		if (!(o instanceof TheoryData)) {
			L.error("Expected a TheoryAndParser");
		}
		TheoryData theory = (TheoryData)o;
		return theory;
	}
	
	private Formula checkFormula(LuaState L, int pos, TheoryData theory) {
		Formula f = d_formula.checkFormula(L, pos, theory.getParser());
		if (!theory.getTheory().getProver().validate(f)) {
			L.error("Formula failed to validate");
		}
		return f;
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
		table.put("attachTableauVisualizer", new FunctionAttachTableauVisualizer());
		table.put("attachTableauObserver", new FunctionAttachTableauObserver());
		table.put("attachModelConstructor", new FunctionAttachModelConstructor());
		table.put("getModel", new FunctionGetModel());
		table.put("showModel", new FunctionShowModel());
		
		L.pushfunction(new Constructor(table));
	}
	
	private final class Constructor extends LFunction {
		private final LTable table;

		private Constructor(LTable table) {
			this.table = table;
		}

		public int invoke(LuaState L) {
			AxiomSystem system = AxiomSystem.S5;
			if (!L.isnil(1)) {
				String systemStr = L.tostring(1);
				system = AxiomSystem.valueOf(systemStr);
			}
			Prover prover = system.buildProver();
			L.pushlvalue(new LUserData(new TheoryData(prover), table));
			return 1;
		}
	}

	private final class FunctionConsistent extends LFunction {
		public int invoke(LuaState L) {
			TheoryData theory = LuaTheory.checkTheory(L, 1);
		
			try {
				L.pushboolean(theory.getTheory().consistent());
			} catch (TableauErrorException e) {
				L.error(e.toString());
			}
			
			return 1;
		}
	}

	private final class FunctionSatisfiable extends LFunction {
		public int invoke(LuaState L) {
			TheoryData theory = LuaTheory.checkTheory(L, 1);
			Formula f = checkFormula(L, 2, theory);
		
			try {
				L.pushboolean(theory.getTheory().satisfiable(f));
			} catch (TableauErrorException e) {
				L.error(e.toString());
			}
			
			return 1;
		}
	}

	private final class FunctionProvable extends LFunction {
		public int invoke(LuaState L) {
			TheoryData theory = LuaTheory.checkTheory(L, 1);
			Formula f = checkFormula(L, 2, theory);
		
			try {
				L.pushboolean(theory.getTheory().provable(f));
			} catch (TableauErrorException e) {
				L.error(e.toString());
			}
			
			return 1;
		}
	}

	private final class FunctionAdd extends LFunction {
		public int invoke(LuaState L) {
			TheoryData theory = LuaTheory.checkTheory(L, 1);
			Formula f = checkFormula(L, 2, theory);

			theory.getTheory().add(f);
			
			return 0;
		}
	}

	private final class FunctionGetModel extends LFunction {
		public int invoke(LuaState L) {
			TheoryData data = LuaTheory.checkTheory(L, 1);
			L.pushlvalue(new LUserData(data.getModelConstructor().getModel()));
			return 1;
		}
	}
	
	private final class FunctionShowModel extends LFunction {
		public int invoke(LuaState L) {
			TheoryData data = LuaTheory.checkTheory(L, 1);
			ObserveProver.showModel(data.getModelConstructor().getModel());
			return 0;
		}
	}
	
	private final class FunctionAttachModelConstructor extends LFunction {
		public int invoke(LuaState L) {
			TheoryData data = LuaTheory.checkTheory(L, 1);
			data.getTheory().getProver().getTableau().attachObserver(data.getModelConstructor());
			return 0;
		}
	}

	private final class FunctionAttachTableauVisualizer extends LFunction {
		public int invoke(LuaState L) {
			try {
				TheoryData data = LuaTheory.checkTheory(L, 1);
				Prover prover = data.getTheory().getProver();
				prover.getTableau().attachObserver(new TableauObserverSwing());
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (FontFormatException e) {
				throw new RuntimeException(e);
			}
			return 0;
		}
	}
	
	private final class FunctionAttachTableauObserver extends LFunction {
		public int invoke(LuaState L) {
			TheoryData data = LuaTheory.checkTheory(L, 1);
			Prover prover = data.getTheory().getProver();
			prover.getTableau().attachObserver(new SystemOutObserver());
			return 0;
		}
	}
}