package de.neo.rankbridge.shared.util;

import java.util.ArrayList;

/**
 * MultiVar is an ArrayList and is constructed with the arguments.
 * 
 * @author Neo8
 * @version 1.0
 */
public class MultiVar {
	
	public ArrayList<String> args;
	
	/**
	 * New instance.
	 * 
	 * @param vars the Arguments
	 */
	public MultiVar(String... vars) {
		this.args = new ArrayList<>();
		for(String var : vars) {
			this.args.add(var);
		}
	}
	
	/**
	 * Returns the Argument.
	 * 
	 * @param i number of the Argument
	 * @return the Argument.
	 */
	public String get(Integer i) {
		return this.args.get(i);
	}
}
