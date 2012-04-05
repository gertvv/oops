package nl.rug.ai.mas.oops.lua;

import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import nl.rug.ai.mas.oops.DynamicProver;
import nl.rug.ai.mas.oops.DynamicProver.AxiomSystem;
import nl.rug.ai.mas.oops.ObserveProver;
import nl.rug.ai.mas.oops.Prover;
import nl.rug.ai.mas.oops.model.ModelConstructingObserver;
import nl.rug.ai.mas.oops.parser.FormulaParser;
import nl.rug.ai.mas.oops.render.TableauObserverSwing;

import org.luaj.platform.J2sePlatform;
import org.luaj.vm.LFunction;
import org.luaj.vm.LString;
import org.luaj.vm.LTable;
import org.luaj.vm.LUserData;
import org.luaj.vm.LuaState;
import org.luaj.vm.Platform;

public class LuaProver {
	/**
	 * The parser instance.
	 */
	private FormulaParser d_parser;

	/**
	 * The prover instance.
	 */
	private Prover d_prover;
	private LuaState d_vm;
	private ModelConstructingObserver d_modelConstructor;
	
	public LuaProver(String proverName) {
	
		// If the system does not exist, this will throw an exception
		AxiomSystem system = DynamicProver.AxiomSystem.valueOf(proverName);
		
		d_prover = system.build();
		d_parser = new FormulaParser(d_prover.getContext());

		
		Platform.setInstance(new J2sePlatform());
		d_vm = Platform.newLuaState();
		org.luaj.compiler.LuaC.install();
		 
		d_modelConstructor = new ModelConstructingObserver(d_prover.getModel());
		registerNameSpace();
	}
	
	private void registerNameSpace() {
		LuaFormula formula = new LuaFormula(d_parser, d_prover);
		LuaTheory theory = new LuaTheory(formula, d_prover);
		
		d_vm.pushlvalue(new LTable());
		
		d_vm.pushlvalue(new FunctionAttachTableauVisualizer());
		d_vm.setfield(-2, new LString("attachTableauVisualizer"));
		
		d_vm.pushlvalue(new FunctionAttachModelConstructor());
		d_vm.setfield(-2, new LString("attachModelConstructor"));
		
		d_vm.pushlvalue(new FunctionSetProver());
		d_vm.setfield(-2, new LString("setProver"));
		
		d_vm.pushlvalue(new FunctionGetProvers());
		d_vm.setfield(-2, new LString("getProvers"));
		
		d_vm.pushlvalue(new FunctionGetModel());
		d_vm.setfield(-2, new LString("getModel"));
		
		d_vm.pushlvalue(new FunctionShowModel());
		d_vm.setfield(-2, new LString("showModel"));
		
		formula.register(d_vm);
		d_vm.setfield(-2, new LString("Formula")); // Give the constructor a name
		
		theory.register(d_vm);
		d_vm.setfield(-2, new LString("Theory")); // Give the constructor a name
		
		d_vm.setglobal("oops");
	}
	
	private final class FunctionGetModel extends LFunction {
		public int invoke(LuaState L) {
			L.pushlvalue(new LUserData(d_modelConstructor.getModel()));
			return 1;
		}
	}
	
	private final class FunctionShowModel extends LFunction {
		public int invoke(LuaState L) {
			ObserveProver.showModel(d_modelConstructor.getModel());
			return 0;
		}
	}
	
	private final class FunctionAttachModelConstructor extends LFunction {
		public int invoke(LuaState L) {
			d_prover.getTableau().attachObserver(d_modelConstructor);
			return 0;
		}
	}

	private final class FunctionAttachTableauVisualizer extends LFunction {
		public int invoke(LuaState L) {
			try {
				d_prover.getTableau().attachObserver(new TableauObserverSwing());
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (FontFormatException e) {
				throw new RuntimeException(e);
			}
			return 0;
		}
	}
	
	private final class FunctionGetProvers extends LFunction {
		public int invoke(LuaState L) {
			LTable result = new LTable();
			int i = 1;
			for (AxiomSystem system : DynamicProver.AxiomSystem.values())
			{
				result.put(i++, new LString(system.name()));
			}
			
			d_vm.pushlvalue(result);
			
			return 1;
		}
	}
	
	
	private final class FunctionSetProver extends LFunction {
		public int invoke(LuaState L) {
			String systemId = d_vm.checkstring(1);
			AxiomSystem system = null;
			try {
				system = DynamicProver.AxiomSystem.valueOf(systemId);
			} catch (Exception e) {
				system = null;
			}
			if (system == null) {
				d_vm.error("Unknown axiom system specified");
			}
			
			Prover prover = system.build();
			
			// Change the prover
			d_prover = prover;
			
			// Change parser
			d_parser = new FormulaParser(d_prover.getContext());
			
			// Change the model
			d_modelConstructor = new ModelConstructingObserver(prover.getModel());
					
			// Redefine the Theory and Formula functions to use the new prover
			
			LuaFormula formula = new LuaFormula(d_parser, d_prover);
			LuaTheory theory = new LuaTheory(formula, d_prover);
			
			d_vm.getglobal("oops");
			
			formula.register(d_vm);
			d_vm.setfield(-2, new LString("Formula")); // Give the constructor a name
			
			theory.register(d_vm);
			d_vm.setfield(-2, new LString("Theory")); // Give the constructor a name
			
			d_vm.setglobal("oops");
						
			return 0;
		}
	}

	public void doFile(String file) {
		d_vm.getglobal("dofile");
		d_vm.pushstring(file);
		d_vm.call(1, 0);
	}
	
	public void doStream(InputStream is, String name) {
		int result = d_vm.load(is, name);
		if (result == 0) {
			d_vm.call(0,0);
		} else if (result == LuaState.LUA_ERRMEM){
			System.err.println("ERROR: Lua ran out of memory while parsing");
			d_vm.pop(1);
		} else {
			String error = d_vm.tostring(-1);
			System.err.println("ERROR: Lua encountered a syntax error: " + error);
			d_vm.pop(1);
		}
	}

	public void interactive() {
		doStream(System.in, "stdin");
	}

	public static void main(String[] args) {
		// TODO: Handle command-line argument to set axiom system
		int argNum = 0;
		
		// Default to the S5 prover
		AxiomSystem system = AxiomSystem.S5;
		String file = null;
		
		while (argNum < args.length)
		{
			String arg = args[argNum++];
			
			if (arg.equals("--prover")) {
				try {
					system = AxiomSystem.valueOf(args[argNum++]);
				} catch (Exception e) {
					System.out.println("Invalid prover specified");
					return;
				}
			} else if (file == null && !arg.startsWith("--") && !arg.startsWith("-")) {
				file = arg;
			}
			
		}
		LuaProver prover = new LuaProver(system.name());
		if (file == null) {
			prover.interactive();
		} else {
			prover.doFile(file);
		}
	}

	public Prover getProver() {
		return d_prover;
	}
}