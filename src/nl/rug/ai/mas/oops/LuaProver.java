package nl.rug.ai.mas.oops;

import org.luaj.platform.J2sePlatform;
import org.luaj.vm.LFunction;
import org.luaj.vm.LuaState;
import org.luaj.vm.Platform;

public class LuaProver {
	private Prover d_prover = Prover.build();
	private LuaState d_vm;
	
	public LuaProver() {
		Platform.setInstance(new J2sePlatform());
		d_vm = Platform.newLuaState();
		org.luaj.compiler.LuaC.install();
		register();
	}
	
	private void register() {
		d_vm.pushfunction(new LFunction() {
			public int invoke(LuaState L) {
				String formula = L.tostring(1);
				Boolean result = null;
				try {
					result = d_prover.provable(formula);
				} catch (TableauErrorException e) {
					System.err.println(e);
				}
				L.pushboolean(result);
				return 1;
			}
		});
		d_vm.setglobal("provable");
		
		d_vm.pushfunction(new LFunction() {
			public int invoke(LuaState L) {
				String formula = L.tostring(1);
				Boolean result = null;
				try {
					result = d_prover.satisfiable(formula);
				} catch (TableauErrorException e) {
					System.err.println(e);
					System.out.flush();
				}
				L.pushboolean(result);
				return 1;
			}
		});
		d_vm.setglobal("satisfiable");
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
	public static void main(String[] args) {
		LuaProver prover = new LuaProver();
		if (args.length == 0) {
			prover.interactive();
		} else {
			prover.doFile(args[0]);
		}
	}
}
