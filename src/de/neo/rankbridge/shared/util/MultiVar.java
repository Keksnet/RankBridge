package de.neo.rankbridge.shared.util;

import java.util.ArrayList;

public class MultiVar {
	
	public ArrayList<String> args;
	
	public MultiVar(String... vars) {
		this.args = new ArrayList<>();
		for(String var : vars) {
			this.args.add(var);
		}
	}
	
	public String get(Integer i) {
		return this.args.get(i);
	}
}
