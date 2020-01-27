package com.network.array;

import java.util.HashSet;
import java.util.Set;

public class Random{
	public static Random random = new Random() ;
	
	public static Array random(int row , int colum) {
		Array result = new Array(colum) ;
		for(int i = 0 , len = row*colum ; i < len ; i++) {
			result.push(i%colum, i/colum, Math.random()); 
		}
		return result ;
	}
	
	// 从矩阵中获取一个大小为batch行的子矩阵
	public static int[] choice(int max , int batch) throws Exception {
		if(max < batch) {
			throw new Exception("数据越界了") ;
		}
		Set<Integer> set = new HashSet<Integer>() ;
		while(true) {
			set.add((int)(Math.random()*max)) ;
			if(set.size() >= batch) {
				break ;
			}
		}
		return set.stream().mapToInt(Number::intValue).toArray() ;
	}

	//将一个数组完全乱序
	public static <T>T[] randomArray(T[] arr){
		int length = arr.length ;
		for(int i = length-1 ; i >= 0 ; i--){
			int rand = (int)(Math.random()*i) ;
			T temp = arr[i] ;
			arr[i] = arr[rand] ;
			arr[rand] = temp ;
		}
		return arr ;
	} 
}
