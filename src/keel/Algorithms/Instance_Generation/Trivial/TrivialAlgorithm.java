/***********************************************************************

	This file is part of KEEL-software, the Data Mining tool for regression, 
	classification, clustering, pattern mining and so on.

	Copyright (C) 2004-2010
	
	F. Herrera (herrera@decsai.ugr.es)
    L. Sánchez (luciano@uniovi.es)
    J. Alcalá-Fdez (jalcala@decsai.ugr.es)
    S. García (sglopez@ujaen.es)
    A. Fernández (alberto.fernandez@ujaen.es)
    J. Luengo (julianlm@decsai.ugr.es)

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see http://www.gnu.org/licenses/
  
**********************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package keel.Algorithms.Instance_Generation.Trivial;
import keel.Algorithms.Instance_Generation.Basic.PrototypeSet;
import keel.Algorithms.Instance_Generation.Basic.PrototypeGenerator;
import keel.Algorithms.Instance_Generation.Basic.PrototypeGenerationAlgorithm;
import keel.Algorithms.Instance_Generation.*;
import keel.Algorithms.Instance_Generation.utilities.*;
import java.util.*;


    
/**Main class
 *
 * @author diegoj
 */
public class TrivialAlgorithm extends PrototypeGenerationAlgorithm<PrototypeGenerator>
{
    /**
     * Builds a new PrototypeGenerator.
     * @param train Training data set.
     * @param params Parameters of the method.
     * @return a new PrototypeGenerator.
     */
    protected PrototypeGenerator buildNewPrototypeGenerator(PrototypeSet train, Parameters params)
    {
       return new PrototypeGenerator(train, params);    
    }
    
    /** Main Function.
     * 
     * @param args main args. Configuration filename is needed.
     */
    public static void main(String args[])
    {
        TrivialAlgorithm algo = new TrivialAlgorithm();
        algo.execute(args);
    }
}

