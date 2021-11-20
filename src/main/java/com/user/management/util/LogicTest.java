package com.user.management.util;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;




public class LogicTest {
    public static void main(String[] args) throws IOException {
    	String[] arrStrList = {"kita", "atik", "tika", "aku", "kia", "makan", "kua"};
    	printResult(arrStrList);
    	
    	//printResult(arrStrList);
    }
    
    public static void printResult(String[] arrStrList) {
        List<Integer> listIndex = new ArrayList<>();
    	for(int i=0; i<arrStrList.length; i++ ) {
    		if(!listIndex.contains(i)) {
    			String currStr = arrStrList[i];
        		List<String> strList = new ArrayList<>();
            	strList.add(currStr);
            	listIndex.add(i);
            	for(int x=0; x<arrStrList.length; x++) {
            		String nextStr = arrStrList[x];
        			Boolean isSame = true;
            		if(x!=i && currStr.length()==nextStr.length()) {
            			for(int z=0; z<nextStr.length(); z++) {
            				String charZ = String.valueOf(nextStr.charAt(z));
        	    			if(!currStr.contains(charZ)) {
        	    				isSame = false;
        	    				z=nextStr.length();
        	    			}
        	    		}
            			if(isSame) {
                			if(!listIndex.contains(x)) {
                    			strList.add(nextStr);
                    			listIndex.add(x);
                			}
                		}
            		}
            		
            	}
            	for(String temStr :strList) {
            		System.out.print(temStr+" ");
            	}
            	System.out.println("");
    		}
    		
    	}
	}
    
}