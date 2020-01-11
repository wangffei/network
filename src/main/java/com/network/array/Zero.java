package com.network.array;

public class Zero {
	
	public static Zero zero = new Zero() ;
	
	public static Array zero(int row , int colum) {
		Array result = new Array(colum) ;
		for(int i = 0 , len = row*colum ; i < len ; i++) {
			result.push(i%colum, i/colum, 0); 
		}
		return result ;
	}
}
