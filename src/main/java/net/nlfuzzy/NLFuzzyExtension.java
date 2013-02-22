/*
 * Copyright (C) 2012 Tomas Machalek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nlfuzzy;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jFuzzyLogic.FIS;

import org.nlogo.api.Agent;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.Observer;
import org.nlogo.api.Patch;
import org.nlogo.api.PrimitiveManager;
import org.nlogo.api.Turtle;
import org.nlogo.api.World;

/**
 * 
 * @author Tomas Machalek <tomas.machalek@gmail.com>
 *
 */
public class NLFuzzyExtension extends DefaultClassManager {
	
	/**
	 * 
	 */
	private final Map<Integer, FIS> agentFis;
	
	/**
	 * 
	 */
	private final Map<String, FIS> fisPool;
	
	/**
	 * 
	 */
	private FIS globalFis;
	
	/**
	 * 
	 */
	private static final int CLEAN_KEYS_TICK_INTERVAL = 20;
	
	/**
	 * 
	 */
	public NLFuzzyExtension() {
		this.agentFis = new HashMap<Integer, FIS>();
		this.fisPool = new HashMap<String, FIS>();
	}
	
	/**
	 * @param context
	 * @throws ExtensionException 
	 */
	public FIS getFIS(Context context) throws ExtensionException {
		
		if (context.getAgent() instanceof Turtle 
				|| context.getAgent() instanceof Patch) {
			return this.agentFis.get(context.getAgent().hashCode());
			
		} else if (context.getAgent() instanceof Observer) {
			return this.globalFis;
			
		} else {
			throw new ExtensionException(
				String.format(
						"Only Turtle, Patch and Observer agents may work with Fuzzy Inference Engine. "
						+ "You have provided %s", context.getAgent().getClass().getName()));
		}
	}

	/**
	 * @param agent
	 * @param inferenceEngine
	 * @throws ExtensionException 
	 */
	public void attachFIS(Context context, String fileName) throws ExtensionException {
		if (context.getAgent() instanceof Turtle 
				|| context.getAgent() instanceof Patch) {
			this.agentFis.put(context.getAgent().hashCode(), loadFIS(fileName, context));
			
			if ((int)context.getAgent().world().ticks() % CLEAN_KEYS_TICK_INTERVAL == 0) {
				cleanUp(context.getAgent().world());
			}
			
		} else if (context.getAgent() instanceof Observer) {
			this.globalFis = loadFIS(fileName, context);
			
		} else {
			throw new ExtensionException(
				String.format(
						"Only Turtle, Patch and Observer agents may work with Fuzzy Inference Engine. "
						+ "You have provided %s", context.getAgent().getClass().getName()));
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void load(PrimitiveManager primitiveManager) throws ExtensionException {
		primitiveManager.addPrimitive("use-fcl", new FCLLoader(this));
		primitiveManager.addPrimitive("set-value", new Fuzzify(this));
		primitiveManager.addPrimitive("eval", new FuzzyEvalReporter(this));
		primitiveManager.addPrimitive("init", new NLFuzzyReset(this));
	}
	
	/**
	 * 
	 */
	public void reset() {
		this.agentFis.clear();
		this.fisPool.clear();
	}
	
	/**
	 * @throws ExtensionException 
	 * 
	 */
	private FIS loadFIS(String fileName, Context context) throws ExtensionException {
		FIS fis;
		
		if (!this.fisPool.containsKey(fileName)) {
			try {
				fis = FIS.load(context.attachModelDir(fileName));
			
			} catch (MalformedURLException e) {
				throw new ExtensionException("Failed to initialize Fuzzy Inference Engine: " + e.getMessage(), 
					e);
			}
			if (fis == null) {
				throw new ExtensionException("Failed to initialize Fuzzy Inference Engine");
			}
			this.fisPool.put(fileName, fis);
			
		} else {
			fis = this.fisPool.get(fileName);
		}
		return fis;
	}
	
	/**
	 * 
	 */
	private void cleanUp(World world) {
		Set<Integer> agentIds = new HashSet<Integer>();
		Set<Integer> idsToRemove = new HashSet<Integer>();
		for (Agent turtle : world.turtles().agents()) {
			agentIds.add(turtle.hashCode());
		}
		for (Agent turtle : world.patches().agents()) {
			agentIds.add(turtle.hashCode());
		}
		
		for (Integer key : this.agentFis.keySet()) {
			if (!agentIds.contains(key)) {
				idsToRemove.add(key);
			}
		}
		for (Integer key : idsToRemove) {
			this.agentFis.remove(key);
		}
	}
}
