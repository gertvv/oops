package nl.rug.ai.mas.oops.lua;

import java.io.InputStream;

import nl.rug.ai.mas.oops.AxiomSystem;

import org.luaj.platform.J2sePlatform;
import org.luaj.vm.LFunction;
import org.luaj.vm.LString;
import org.luaj.vm.LTable;
import org.luaj.vm.LuaState;
import org.luaj.vm.Platform;

public class LuaProver {
	private LuaState d_vm;
	
	public LuaProver() {
		Platform.setInstance(new J2sePlatform());
		d_vm = Platform.newLuaState();
		org.luaj.compiler.LuaC.install();
		 
		registerNameSpace();
	}
	
	private void registerNameSpace() {
		LuaFormula formula = new LuaFormula();
		LuaTheory theory = new LuaTheory(formula);
		
		d_vm.pushlvalue(new LTable());
		
		d_vm.pushlvalue(new FunctionGetProvers());
		d_vm.setfield(-2, new LString("getProvers"));
		
		formula.register(d_vm);
		d_vm.setfield(-2, new LString("Formula")); // Give the constructor a name
		
		theory.register(d_vm);
		d_vm.setfield(-2, new LString("Theory")); // Give the constructor a name
		
		d_vm.setglobal("oops");
	}
	
	
	private final class FunctionGetProvers extends LFunction {
		public int invoke(LuaState L) {
			LTable result = new LTable();
			int i = 1;
			for (AxiomSystem system : AxiomSystem.values())
			{
				result.put(i++, new LString(system.name()));
			}
			
			d_vm.pushlvalue(result);
			
			return 1;
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
		String file = null;
		
		if (args.length > 0) {
			file = args[0];
		}
		LuaProver prover = new LuaProver();
		if (file == null) {
			prover.interactive();
		} else {
			prover.doFile(file);
		}
	}
}