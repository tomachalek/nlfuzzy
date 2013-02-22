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

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

/**
 * 
 * @author Tomas Machalek <tomas.machalek@gmail.com>
 *
 */
public class FCLLoader extends DefaultCommand {

	/**
	 * 
	 */
	private final NLFuzzyExtension extension;
	
	/**
	 * 
	 * @param extension
	 */
	public FCLLoader(NLFuzzyExtension extension) {
		this.extension = extension;
	}
	
	/**
	 * 
	 */
	@Override
	public void perform(Argument[] args, Context context)
			throws ExtensionException, LogoException {
		String fileName = args[0].getString();		
		this.extension.attachFIS(context, fileName);
	}
	
	/**
	 * 
	 */
	public Syntax getSyntax() {
		return Syntax.commandSyntax(new int[] { Syntax.StringType() });
	}

}
