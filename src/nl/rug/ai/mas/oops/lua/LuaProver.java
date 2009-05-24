package nl.rug.ai.mas.oops.lua;

import nl.rug.ai.mas.oops.Prover;
import nl.rug.ai.mas.oops.FormulaParser;
import nl.rug.ai.mas.oops.TableauErrorException;
import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.formula.Context;
import nl.rug.ai.mas.oops.theory.Theory;
import nl.rug.ai.mas.oops.SimpleProver;
import nl.rug.ai.mas.oops.tableau.Rule;
import nl.rug.ai.mas.oops.tableau.PropositionalRuleFactory;
import nl.rug.ai.mas.oops.tableau.ModalRuleFactory;

import org.luaj.platform.J2sePlatform;
import org.luaj.vm.LFunction;
import org.luaj.vm.LString;
import org.luaj.vm.LTable;
import org.luaj.vm.LUserData;
import org.luaj.vm.LuaState;
import org.luaj.vm.Platform;

import java.util.Vector;

public class LuaProver {
	/**
	 * The parser instance.
	 */
	private FormulaParser d_formulaAdapter;
	/**
	 * The (parser) context.
	 */
	private Context d_context;
	/**
	 * The prover instance.
	 */
	private Prover d_prover;
	private LuaState d_vm;
	private LTable d_theoryMeta;
	
	public LuaProver() {
		d_context = new Context();

		// build rules
		Vector<Rule> rules = PropositionalRuleFactory.build(d_context);
		rules.addAll(ModalRuleFactory.build(d_context));

		d_prover = new Prover(rules);
		d_formulaAdapter = new FormulaParser(d_context);

		Platform.setInstance(new J2sePlatform());
		d_vm = Platform.newLuaState();
		org.luaj.compiler.LuaC.install();
		
		registerNameSpace();
	}
	
	private void registerNameSpace() {
		d_theoryMeta = new LTable();
		d_theoryMeta.put("__index", d_theoryMeta);
		d_theoryMeta.put("add", new LFunction() {
			public int invoke(LuaState L) {
				Theory theory = checkTheory(L, 1);
				Formula f = checkFormula(L, 2);

				theory.add(f);
				
				return 0;
			}
		});
		d_theoryMeta.put("provable", new LFunction() {
			public int invoke(LuaState L) {
				Theory theory = checkTheory(L, 1);
				Formula f = checkFormula(L, 2);
			
				try {
					L.pushboolean(theory.provable(f));
				} catch (TableauErrorException e) {
					L.error(e.toString());
				}
				
				return 1;
			}
		});
		d_theoryMeta.put("satisfiable", new LFunction() {
			public int invoke(LuaState L) {
				Theory theory = checkTheory(L, 1);
				Formula f = checkFormula(L, 2);
			
				try {
					L.pushboolean(theory.satisfiable(f));
				} catch (TableauErrorException e) {
					L.error(e.toString());
				}
				
				return 1;
			}
		});
		d_theoryMeta.put("consistent", new LFunction() {
			public int invoke(LuaState L) {
				Theory theory = checkTheory(L, 1);
			
				try {
					L.pushboolean(theory.consistent());
				} catch (TableauErrorException e) {
					L.error(e.toString());
				}
				
				return 1;
			}
		});
		
		d_vm.pushlvalue(new LTable());
		d_vm.pushfunction(new LFunction() {
			public int invoke(LuaState L) {
				L.pushlvalue(new LUserData(new Theory(d_prover), d_theoryMeta));
				return 1;
			}
		});
		d_vm.setfield(-2, new LString("Theory"));
		d_vm.setglobal("oops");
	}
	
	public void doFile(String file) {
		d_vm.getglobal("dofile");
		d_vm.pushstring(file);
		d_vm.call(1, 0);
	}

	public void interactive() {
		if (d_vm.load(System.in, "stdin") == 0) {
			d_vm.call(0, 0);
		}
	}
	private Theory checkTheory(LuaState L, int pos) {
		Object o = L.touserdata(pos);
		
		if (!(o instanceof Theory)) {
			L.error("Expected a Theory");
		}
		Theory theory = (Theory)o;
		return theory;
	}

	private Formula checkFormula(LuaState L, int pos) {
		String s = L.tostring(pos);

		if (!d_formulaAdapter.parse(s)) {
			L.error(d_formulaAdapter.getErrorCause().toString());
		}

		return d_formulaAdapter.getFormula();
	}

	public static void main(String[] args) {
		LuaProver prover = new LuaProver();
		if (args.length == 0) {
			prover.interactive();
		} else {
			prover.doFile(args[0]);
		}
	}
}
