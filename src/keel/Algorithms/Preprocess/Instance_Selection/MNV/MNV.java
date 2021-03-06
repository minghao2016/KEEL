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

//
//  MNV.java
//
//  Salvador García López
//
//  Created by Salvador García López 22-2-2008.
//  Copyright (c) 2004 __MyCompanyName__. All rights reserved.
//

package keel.Algorithms.Preprocess.Instance_Selection.MNV;

import keel.Algorithms.Preprocess.Basic.*;

import org.core.*;

import java.util.StringTokenizer;
import java.util.Arrays;

/**
 * 
 * File: MNV.java
 * 
 * The MNV Instance Selection algorithm.
 * 
 * @author Written by Salvador García (University of Granada) 20/07/2004 
 * @version 0.1 
 * @since JDK1.5
 * 
 */
public class MNV extends Metodo {

  /*Own parameters of the algorithm*/
  private int k;

  /**
     * Default constructor. Construct the algoritm by using the superclass builder.
	  * @param ficheroScript Configuration script
     */
  public MNV (String ficheroScript) {
    super (ficheroScript);
  }

  /**
	 * Executes the algorithm
	 */ 
  public void ejecutar () {

    double conjS[][];
    double conjR[][];
    int conjN[][];
    boolean conjM[][];
    int clasesS[];
    double conjS2[][];
    double conjR2[][];
    int conjN2[][];
    boolean conjM2[][];
    int clasesS2[];
	  
    int S[];
    int i, j, l, m;
    int nClases;
    int tamS;
    int claseObt;
    int cont;
    int busq;
    boolean continuar;
    double dist, minDist;
    int instance;
    ReferenciaMNV orderSet[];
    boolean marcas[];
    int nSel, aciertosIni=0, aciertos;

    long tiempo = System.currentTimeMillis();

    /*Inicialization of the candidates set*/
    S = new int[datosTrain.length];
    for (i=0; i<S.length; i++)
      S[i] = Integer.MAX_VALUE;

    /*Getting the number of different classes*/
    nClases = 0;
    for (i=0; i<clasesTrain.length; i++)
      if (clasesTrain[i] > nClases)
        nClases = clasesTrain[i];
    nClases++;
    tamS = 0;

    if (nClases < 2) {
      System.err.println("Input dataset is empty");
      nClases = 0;
    }
    
    orderSet = new ReferenciaMNV[datosTrain.length];
    for (i=0; i<datosTrain.length; i++) {
    	minDist = Double.MAX_VALUE;
    	instance = 0;
    	for (j=0; j<datosTrain.length; j++) {
    		if (clasesTrain[i] != clasesTrain[j]) {
    			dist = KNN.distancia(datosTrain[i], realTrain[i], nominalTrain[i], nulosTrain[i], datosTrain[j], realTrain[j], nominalTrain[j], nulosTrain[j], distanceEu);
    			if (dist < minDist) {
    				minDist = dist;
    				instance = j;
    			}
    		}
    	}
    	cont = 0;
    	for (j=0; j<datosTrain.length; j++) {
    		if (clasesTrain[j] != clasesTrain[instance] && KNN.distancia(datosTrain[instance], realTrain[instance], nominalTrain[instance], nulosTrain[instance], datosTrain[j], realTrain[j], nominalTrain[j], nulosTrain[j], distanceEu) < minDist) {
    			cont++;
    		}
    	}
    	orderSet[i] = new ReferenciaMNV(i, cont, minDist);
    }
    
    Arrays.sort(orderSet);
    S[0] = orderSet[0].entero;
    tamS++;

    /*Algorithm body. We resort randomly the instances of T and compare with the rest of S.
     If an instance doesn´t classified correctly, it is inserted in S*/
    do {
      continuar = false;
      for (i=0; i<datosTrain.length; i++) {
        /*Construction of the S set from the previous vector S*/
        conjS = new double[tamS][datosTrain[0].length];
        conjR = new double[tamS][datosTrain[0].length];
        conjN = new int[tamS][datosTrain[0].length];
        conjM = new boolean[tamS][datosTrain[0].length];
        clasesS = new int[tamS];
        for (j = 0; j < tamS; j++) {
          for (l = 0; l < datosTrain[0].length; l++) {
            conjS[j][l] = datosTrain[S[j]][l];
            conjR[j][l] = realTrain[S[j]][l];
            conjN[j][l] = nominalTrain[S[j]][l];
            conjM[j][l] = nulosTrain[S[j]][l];
          }
          clasesS[j] = clasesTrain[S[j]];
        }
        Arrays.sort(S);
        busq = Arrays.binarySearch(S, orderSet[i].entero);
        if (busq < 0) {
          /*Do KNN to the instance*/
          claseObt = KNN.evaluacionKNN(k, conjS, conjR, conjN, conjM, clasesS, datosTrain[orderSet[i].entero], realTrain[orderSet[i].entero], nominalTrain[orderSet[i].entero], nulosTrain[orderSet[i].entero], nClases, distanceEu);
          if (claseObt != clasesTrain[orderSet[i].entero]) { //fail in the class, it is included in S
            continuar = true;
            S[tamS] = orderSet[i].entero;
            tamS++;
          }
        }
      }
    } while (continuar == true);

    /*Construction of the S set from the previous vector S*/
    conjS = new double[tamS][datosTrain[0].length];
    conjR = new double[tamS][datosTrain[0].length];
    conjN = new int[tamS][datosTrain[0].length];
    conjM = new boolean[tamS][datosTrain[0].length];
    clasesS = new int[tamS];
    for (j=0; j<tamS; j++) {
      for (l=0; l<datosTrain[0].length; l++) {
        conjS[j][l] = datosTrain[S[j]][l];
        conjR[j][l] = realTrain[S[j]][l];
        conjN[j][l] = nominalTrain[S[j]][l];
        conjM[j][l] = nulosTrain[S[j]][l];
      }
      clasesS[j] = clasesTrain[S[j]];
    }
    
    /*RNN Process*/
    /*Inicialización del vector de instancias marcadas para su elminiación posterior*/
    marcas = new boolean[conjS.length];
    for (i=0; i<conjS.length; i++)
      marcas[i] = true;
    nSel = conjS.length;

    /*Calculate the number of correct clasifications considering the same train set using leave-one out.*/
    for (i=0; i<conjS.length; i++) {
      claseObt = KNN.evaluacionKNN2 (k, conjS, conjR, conjN, conjM, clasesS, conjS[i], conjR[i], conjN[i], conjM[i], nClases, distanceEu);
      if (claseObt == clasesS[i])
        aciertosIni++;
    }

    /*Body of the RNN algorithm. Eliminating instances and calculating improves. If
     a remove of an instance not improves classification, the instance is not removed.*/
    for (i=0; i<conjS.length; i++) {
      marcas[i] = false;
      nSel--;
      
      /*Building of the S set from the flags*/
      conjS2 = new double[nSel][conjS[0].length];
      conjR2 = new double[nSel][conjS[0].length];
      conjN2 = new int[nSel][conjS[0].length];
      conjM2 = new boolean[nSel][conjS[0].length];
      clasesS2 = new int[nSel];
      for (m=0, l=0; m<conjS.length; m++) {
        if (marcas[m]) { //the instance must be copied to the solution
          for (j=0; j<conjS[0].length; j++) {
            conjS2[l][j] = conjS[m][j];
            conjR2[l][j] = conjR[m][j];
            conjN2[l][j] = conjN[m][j];
            conjM2[l][j] = conjM[m][j];
          }
          clasesS2[l] = clasesS[m];
          l++;
        }
      }

      /*Get the accuracy considering the S set*/
      aciertos = 0;
      for (j=0; j<conjS.length; j++) {
        claseObt = KNN.evaluacionKNN2 (k, conjS2, conjR2, conjN2, conjM2, clasesS2, conjS[j], conjR[j], conjN[j], conjM[j], nClases, distanceEu);
        if (claseObt == clasesS[j])
          aciertos++;
      }

      /*Is the instance removed?*/
      if (aciertos < aciertosIni) {
        marcas[i] = true;
        nSel++;
      }
    }

    /*Building the final S set from the existents flags*/
    conjS2 = new double[nSel][conjS[0].length];
    conjR2 = new double[nSel][conjS[0].length];
    conjN2 = new int[nSel][conjS[0].length];
    conjM2 = new boolean[nSel][conjS[0].length];
    clasesS2 = new int[nSel];
    for (m=0, l=0; m<conjS.length; m++) {
      if (marcas[m]) {
        for (j=0; j<conjS[0].length; j++) {
          conjS2[l][j] = conjS[m][j];
          conjR2[l][j] = conjR[m][j];
          conjN2[l][j] = conjN[m][j];
          conjM2[l][j] = conjM[m][j];
        }
        clasesS2[l] = clasesS[m];
        l++;
      }
    }    

    System.out.println("MNV "+ relation + " " + (double)(System.currentTimeMillis()-tiempo)/1000.0 + "s");

    OutputIS.escribeSalida(ficheroSalida[0], conjR, conjN, conjM, clasesS, entradas, salida, nEntradas, relation);
    OutputIS.escribeSalida(ficheroSalida[1], test, entradas, salida, nEntradas, relation);
  }	

  public void leerConfiguracion (String ficheroScript) {

    String fichero, linea, token;
    StringTokenizer lineasFichero, tokens;
    byte line[];
    int i, j;
    
    ficheroSalida = new String[2];

    fichero = Fichero.leeFichero (ficheroScript);
    lineasFichero = new StringTokenizer (fichero,"\n\r");

    lineasFichero.nextToken();
    linea = lineasFichero.nextToken();

    tokens = new StringTokenizer (linea, "=");
    tokens.nextToken();
    token = tokens.nextToken();

    /*Getting the names of the training and test files*/
    line = token.getBytes();
    for (i=0; line[i]!='\"'; i++);
    i++;
    for (j=i; line[j]!='\"'; j++);
    ficheroTraining = new String (line,i,j-i);
    for (i=j+1; line[i]!='\"'; i++);
    i++;
    for (j=i; line[j]!='\"'; j++);
    ficheroTest = new String (line,i,j-i);

    /*Getting the path and base name of the results files*/
    linea = lineasFichero.nextToken();
    tokens = new StringTokenizer (linea, "=");
    tokens.nextToken();
    token = tokens.nextToken();

    /*Getting the names of output files*/
    line = token.getBytes();
    for (i=0; line[i]!='\"'; i++);
    i++;
    for (j=i; line[j]!='\"'; j++);
    ficheroSalida[0] = new String (line,i,j-i);
    for (i=j+1; line[i]!='\"'; i++);
    i++;
    for (j=i; line[j]!='\"'; j++);
    ficheroSalida[1] = new String (line,i,j-i);
    
     /*Getting the number of neighbors*/
     linea = lineasFichero.nextToken();
     tokens = new StringTokenizer (linea, "=");
     tokens.nextToken();
     k = Integer.parseInt(tokens.nextToken().substring(1));
  
     /*Getting the type of distance function*/
     linea = lineasFichero.nextToken();
     tokens = new StringTokenizer (linea, "=");
     tokens.nextToken();
     distanceEu = tokens.nextToken().substring(1).equalsIgnoreCase("Euclidean")?true:false;    
  }
}

