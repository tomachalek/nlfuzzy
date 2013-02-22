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

import org.nlogo.api.*;

/**
 * 
 * @author Tomas Machalek <tomas.machalek@gmail.com>
 *
 */
public class FuzzyEvalReporter extends DefaultReporter {

	/**
	 * 
	 */
	private final NLFuzzyExtension extension;
	
	/**
	 * 
	 */
	public FuzzyEvalReporter(NLFuzzyExtension extension) {
		this.extension = extension;
	}
	
	/**
	 * 
	 */
	@Override
	public Object report(Argument[] args, Context context)
			throws ExtensionException, LogoException {
		this.extension.getFIS(context).evaluate();
		return this.extension.getFIS(context).getVariable(args[0].getString())
				.getLatestDefuzzifiedValue();
	}
	
	/**
	 * 
	 */
	public Syntax getSyntax() {
		return Syntax.reporterSyntax(
				new int[] {Syntax.StringType()}, Syntax.NumberType());
	}

}
