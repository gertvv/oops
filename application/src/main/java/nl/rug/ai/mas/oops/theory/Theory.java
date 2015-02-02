package nl.rug.ai.mas.oops.theory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nl.rug.ai.mas.oops.Prover;
import nl.rug.ai.mas.oops.formula.Conjunction;
import nl.rug.ai.mas.oops.formula.Implication;
import nl.rug.ai.mas.oops.formula.Formula;
import nl.rug.ai.mas.oops.TableauErrorException;

/**
 * Represent a logical theory.
 */
public class Theory {
	private Set<Formula> d_formulas = new HashSet<Formula>();
	private Prover d_prover;

	public Theory(Prover p) {
		d_prover = p;
	}
	
	/**
	 * Get the formulas in this Theory. Returns an unmodifiable view on the Set contained in the Theory.
	 * @return The set of formulas contained by this theory.
	 */
	public Set<Formula> getFormulas() {
		return Collections.unmodifiableSet(d_formulas);
	}
	
	public Prover getProver() {
		return d_prover;
	}
	
	/**
	 * Add a formula to this Theory.
	 * @param f The formula to add.
	 */
	public void add(Formula f) {
		d_formulas.add(f);
	}
	
	/**
	 * Remove a formula from this Theory.
	 * @param f The formula to remove.
	 */
	public boolean remove(Formula f) {
		return d_formulas.remove(f);
	}
	
	/**
	 * Convert the theory into a formula.
	 * TODO: Let this return a balanced binary tree or something like that.
	 */
	public Formula asFormula() {
		Formula result = null;
		for (Formula f : d_formulas) {
			if (result == null) {
				result = f;
			} else {
				result = new Conjunction(result, f);
			}
		}
		return result;
	}
	
	public String toString() {
		return d_formulas.toString();
	}
	
	/**
	 * Attempts to prove (theory -> formula)
	 * @param formula The Formula to prove.
	 * @return true if (theory -> formula) is provable.
	 * @throws TableauErrorException
	 */
	public boolean provable(Formula formula)
	throws TableauErrorException {
		if (d_formulas.isEmpty()) {
			return d_prover.provable(formula);
		}
		return d_prover.provable(new Implication(asFormula(), formula));
	}
	
	/**
	 * Checks the satisfiability of (theory & formula)
	 * @param formula The Formula to check SAT of.
	 * @return true if formula is satisfiable in the theory.
	 * @throws TableauErrorException
	 */
	public boolean satisfiable(Formula formula)
	throws TableauErrorException {
		if (d_formulas.isEmpty()) {
			return d_prover.satisfiable(formula);
		}
		return d_prover.satisfiable(new Conjunction(asFormula(), formula));
	}
	
	/**
	 * Checks consistency of theory (i.e. theory is satisfiable)
	 * @return true if the theory is consistent.
	 * @throws TableauErrorException
	 */
	public boolean consistent()
	throws TableauErrorException {
		if (d_formulas.isEmpty()) {
			return true;
		}
		return d_prover.satisfiable(asFormula());
	}
}
