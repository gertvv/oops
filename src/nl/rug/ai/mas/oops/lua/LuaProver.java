package nl.rug.ai.mas.oops.lua;

import java.io.InputStream;
import java.util.Vector;

import nl.rug.ai.mas.oops.Prover;
import nl.rug.ai.mas.oops.parser.Context;
import nl.rug.ai.mas.oops.parser.FormulaParser;
import nl.rug.ai.mas.oops.tableau.ModalRuleFactory;
import nl.rug.ai.mas.oops.tableau.MultiModalValidator;
import nl.rug.ai.mas.oops.tableau.PropositionalRuleFactory;
import nl.rug.ai.mas.oops.tableau.Rule;

import org.luaj.platform.J2sePlatform;
import org.luaj.vm.LString;
import org.luaj.vm.LTable;
import org.luaj.vm.LuaState;
import org.luaj.vm.Platform;

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
	
	public LuaProver() {
		d_context = new Context();

		// build rules
		Vector<Rule> rules = PropositionalRuleFactory.build(d_context);
		rules.addAll(ModalRuleFactory.build(d_context));

		d_prover = new Prover(rules, new MultiModalValidator());
		d_formulaAdapter = new FormulaParser(d_context);

		Platform.setInstance(new J2sePlatform());
		d_vm = Platform.newLuaState();
		org.luaj.compiler.LuaC.install();
		
		registerNameSpace();
	}
	
	private void registerNameSpace() {
		LuaFormula formula = new LuaFormula(d_formulaAdapter, d_prover);
		LuaTheory theory = new LuaTheory(formula, d_prover);
		
		d_vm.pushlvalue(new LTable());
		
		formula.register(d_vm);
		d_vm.setfield(-2, new LString("Formula")); // Give the constructor a name
		
		theory.register(d_vm);
		d_vm.setfield(-2, new LString("Theory")); // Give the constructor a name
		
		d_vm.setglobal("oops");
	}

	
	public void doFile(String file) {
		d_vm.getglobal("dofile");
		d_vm.pushstring(file);
		d_vm.call(1, 0);
	}
	
	public void doStream(InputStream is, String name) {
		if (d_vm.load(is, name) == 0) {
			d_vm.call(0,0);
		}
	}

	public void interactive() {
		doStream(System.in, "stdin");
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
