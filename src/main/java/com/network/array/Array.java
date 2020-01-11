package com.network.array;

/**
 * 此类主要解决矩阵运算方面的数学问题（仅支持2维的运算）
 * @author root
 */
public class Array {
	// 需要进行运算的数据对象
	private double[] data ;
	// 矩阵的维度 行
	private int h ;
	// 矩阵的维度 列
	private int w ;
	// 用于随即产生矩阵
	public static Random random = Random.random ;
	// 用于产生一个全部为0的矩阵
	public static Zero zero = Zero.zero ;
	
	//给定维度和数据大小创建矩阵
	public Array(int w , int maxLength) {
		this.w = w ;
		this.h = 0 ;
		this.data = new double[maxLength] ;
	}
	
	// 给定维度创建矩阵
	public Array(int w) {
		this.w = w ;
		this.h = 0 ;
		this.data = new double[100] ;
	}
	
	// 复制数组
	private double[] copyArr(double[] source , int len) {
		double[] target = new double[len] ;
		System.arraycopy(source, 0, target, 0, source.length);
		return target ;
	}
	
	// 获取矩阵结构
	public int[] shape() {
		int[] temp = new int[] {w , h} ;
		return temp ;
	}
	
	// 重置矩阵结构
	public void reshape(int w , int h) {
		this.w = w ;
		this.h = h ;
	}
	
	// 此方法时给矩阵乘法提供的 默认时操作矩阵的最后一位
	public void push(int w_index , int h_index , double a) {
		// 判断数组大小是否足够
		if((h_index)*w + w_index + 1 >= data.length) {
			data = copyArr(data, (w*h + 2)*2) ;
		}
		data[h_index*w + w_index] = a ;
		h = h_index+1 ;
	}
	
	// 往矩阵中添加数据
	public void push(double[] a) throws Exception {
		if(a.length != this.w) {
			throw new Exception("维度不匹配");
		}
		// 判断数组大小是否足够
		if(w*h + a.length >= data.length) {
			data = copyArr(data, (w*h + a.length)*2) ;
		}
		for(int i=0 ; i<a.length ; i++) {
			data[w*h+i] = a[i] ;
		}
		h++ ;
	}
	
	// 往矩阵中添加数据
	public void push(Array arr) throws Exception {
		int target_w = arr.shape()[0] ;
		int target_h = arr.shape()[1] ;
		if(target_w != this.w) {
			throw new Exception("维度不匹配");
		}
		double[] target = arr.getData() ;
		int size = this.w*this.h ;
		// 判断数组大小是否足够
		if(w*h + target_w*target_h >= data.length) {
			data = copyArr(data, (w*h + target_w*target_h)*2) ;
		}
		// 将目标数组填入
		for(int i = 0 , len = target_w*target_h ; i < len ; i++) {
			this.data[size+i] = target[i] ;
		}
		h += target_h ;
	}
	
	public double[] getData() {
		return this.data ;
	}
	
	public void setData(double[] data) {
		this.data = data ;
		this.h = this.data.length / this.w ;
	}
	
	// 矩阵点乘
	public Array dot(Array arr) throws Exception {
		// 判断两个矩阵是否能够相乘
		int target_w = arr.shape()[0] ;
		int target_h = arr.shape()[1] ;
		if(this.w != target_h) {
			throw new Exception("矩阵无法相乘");
		}
		// 当前对象的矩阵数据
		double[] source = this.data ;
		// 目标对象的矩阵数据
		double[] target = arr.getData() ;
		// 创建相乘后的矩阵
		Array result = new Array(target_w) ;
		for(int i = 0 , len = this.h * target_w ; i < len ; i++) {
			int temp_w = i%target_w ;
			int temp_h = i/target_w ;
			// 计算1矩阵i行和2矩阵j列的乘积
			double d = 0 ;
			for(int j = 0 ; j < this.w ; j++) {
				d += source[temp_h*this.w+j]*target[temp_w+j*target_w] ;
			}
			result.push(temp_w , temp_h , d);
		}
		return result ;
	}
	
	// 矩阵某个维度求和
	public Array sum(int axios) {
		Array result = null ;
		int lenX ;
		int lenY ;
		if (axios == 0) {
			result = new Array(this.w) ;
			lenX = this.w ;
			lenY = this.h ;
		}else {
			result = new Array(1) ;
			lenX = this.h ;
			lenY = this.w ;
		}
		for(int i = 0 ; i < lenX ; i++) {
			double sum = 0 ;
			for(int j = 0 ; j < lenY ; j++) {
				sum += this.data[axios == 0 ? j*this.w+i : i*this.w+j] ;
			}
			result.push(axios == 0 ? i : 0 , axios == 0 ? 0 : i , sum);
		}
		return result ;
	}
	
	// 矩阵相除,整体除以一个整数
	public Array div(int a) {
		Array result = new Array(this.w) ;
		for(int i = 0 ; i < this.w*this.h ; i++) {
			result.push(i%this.w, i/this.w, this.data[i] / a);
		}
		return result ;
	}
	
	private Array _add_sub(Array arr , int flag) throws Exception {
		int target_w = arr.shape()[0] ;
		int target_h = arr.shape()[1] ;
		if(this.w != target_w) {
			throw new Exception("维度不匹配");
		}
		Array result = new Array(this.w) ;
		double[] source = this.data ;
		double[] target = arr.getData() ;
		for(int i = 0 ; i < this.w*this.h ; i++) {
			double sum = 0.0 ;
			if(target_h == 1) {
				sum = flag == 1 ? source[i] + target[i%target_w] : source[i] - target[i%target_w] ;
			}else {
				sum = flag == 1 ? source[i]+target[i] : source[i]-target[i] ;
			}
			result.push(i%target_w , i/target_w , sum );
		}
		return result ;
	}
	
	//矩阵相加
	public Array add(Array arr) throws Exception {
		return this._add_sub(arr, 1) ;
	}
	
	// 矩阵相减
	public Array sub(Array arr) throws Exception {
		return this._add_sub(arr, 2) ;
	}
	
	// 计算矩阵的反转矩阵
	public Array T() {
		Array result = new Array(this.h) ;
		for(int i = 0 ; i < this.w ; i++) {
			for(int j = 0 ; j < this.h ; j++) {
				result.push(j, i, this.data[j*this.w+i]);
			}
		}
		return result ;
	}
	
	// 矩阵x乘
	public Array mul(Array arr) throws Exception {
		int target_w = arr.shape()[0] ;
		int target_h = arr.shape()[1] ;
		if(this.w != target_w || this.h != target_h) {
			throw new Exception("两个矩阵维度不一致") ;
		}
		double[] target = arr.getData() ;
		Array result = new Array(this.w) ;
		for(int i = 0 , len = this.w*this.h ; i < len ; i++) {
			result.push(i%this.w , i/this.w , this.data[i]*target[i]) ;
		}
		return result ;
	}
	
	// 矩阵所有位乘以某一个数
	public Array mul(int a) {
		Array result = new Array(this.w) ;
		for(int i = 0 , len = this.w*this.h ; i < len ; i++) {
			result.push(i%this.w , i/this.w , this.data[i]*a) ;
		}
		return result ;
	}
	
	// 矩阵所有位乘以某一个数
	public Array mul(double a) {
		Array result = new Array(this.w) ;
		for(int i = 0 , len = this.w*this.h ; i < len ; i++) {
			result.push(i%this.w , i/this.w , this.data[i]*a) ;
		}
		return result ;
	}
	
	// 矩阵相减
	public Array sub(int a) {
		Array result = new Array(this.w) ;
		for(int i = 0 , len = this.w*this.h ; i < len ; i++) {
			result.push(i%this.w , i/this.w , this.data[i]-a) ;
		}
		return result ;
	}
	
	// 求激活函数sigmoid的输出
	public Array sigmoid() {
		Array result = new Array(this.w) ;
		double[] target = this.data ;
		for (int i = 0 , len = this.w*this.h ; i<len ; i++) {
			result.push(i%this.w, i/this.w, 1 / (1 + Math.pow(Math.E, -data[i]))); 
		}
		return result ;
	}
	
	// 求激活函数softmax的输出
	public Array softmax() {
		Array result = new Array(this.w) ;
		// 每一行都要求
		for(int i = 0 ; i < this.h ; i++) {
			int start = i*this.w ;
			int end = start+this.w ;
			// 求出当前行最大值
			double max = max(start, end) ;
			// 求出当前行exp(i)的和
			double sum = sum_exp(start, end, max) ;
			for(int j = 0 ; j < this.w ; j++) {
				result.push(j, i, Math.pow(Math.E, this.data[start+j] - max) / sum) ;
			}
		}
		return result ;
	}
	
	private double sum_exp(int start , int end , double max) {
		double sum = 0.0 ;
		for(int i = start ; i < end ; i++) {
			sum += Math.pow(Math.E, this.data[i] - max) ;
		}
		return sum ;
	}
	
	private double max(int start , int end) {
		double max = this.data[start] ;
		for(int i = start ; i < end ; i++) {
			if(max < this.data[i]) {
				max = this.data[i] ;
			}
		}
		return max ;
	}
	
	// 二维数组转一维数组
	public static double[] arrayConver(double[][] source) {
		int width = source[0].length ;
		int height = source.length ;
		double[] result = new double[width*height] ;
		for(int i = 0 ; i < height ; i++) {
			for(int j = 0 ; j < width ; j++) {
				result[width*i+j] = source[i][j] ;
			}
		}
		return result ;
	}
	
	// 求出每一列最大值的下标，用于确定哪一个的可能性更高
	public int[] max() {
		int[] result = new int[h] ;
		for(int i = 0 ; i < this.h ; i++) {
			double max = this.data[i*this.w] ;
			int index = 0 ;
			for(int j = 0 ; j < this.w ; j++) {
				if(max < this.data[i*this.w + j]) {
					max = this.data[i*this.w + j] ;
					index = j ;
				}
			}
			result[i] = index ;
		}
		return result ;
	}
	
	// 切割矩阵
	public Array subArray(int start , int end) throws Exception {
		if(end > this.h) {
			throw new Exception("下标越界了") ;
		}
		Array result = new Array(this.w) ;
		for(int i = start ; i < end ; i++) {
			for(int j = 0 ; j < this.w ; j++) {
				result.push(j, i - start, this.data[i*this.w+j]) ;
			}
		}
		return result ;
	}
	
	// 切割矩阵
	public Array subArray(int start ) throws Exception {
		Array result = this.subArray(start, this.h) ;
		return result ;
	}
	
	// 根据一个下标数组从矩阵中获取对应的数据，重新组成矩阵
	public Array I(int[] indexs) {
		Array result = new Array(this.w) ;
		double[] d = new double[indexs.length*this.w] ;
		for(int i = 0 ; i < indexs.length ; i++) {
			for(int j = 0 ; j < this.w ; j++) {
				d[i*this.w+j] = this.data[indexs[i]*this.w+j] ;
			}
		}
		result.setData(d);
		return result ;
	}
	
	// 两个矩阵相同的占比多少
	public double samePer(Array arr) throws Exception {
		int target_w = arr.shape()[0] ;
		int target_h = arr.shape()[1] ;
		if(this.w != target_w || this.h != target_h) {
			throw new Exception("两个矩阵无法比较") ;
		}
		double[] target = arr.getData() ;
		int all = this.w*this.h ;
		int count = 0 ;
		for(int i = 0 ; i < this.h ; i++) {
			for(int j = 0 ; j < this.w ; j++) {
				if(this.data[i*this.w+j] == target[i*this.w+j]) {
					count++ ;
				}
			}
		}
		return (double)count / all ;
	}
	
	// 两个矩阵相同的占比多少
	public static double samePer(int[] source , int[] target) throws Exception {
		if(source.length != target.length) {
			throw new Exception("两个矩阵无法比较") ;
		}
		int all = source.length ;
		int count = 0 ;
		for(int i = 0 ; i < source.length ; i++) {
			if(source[i] == target[i]) {
				count++ ;
			}
		}
		return (double)count / all ;
	}
	
	public String toString() {
		StringBuffer buffer  ;

		buffer = new StringBuffer("[("+this.h+" , "+this.w+"),[") ;
		for(int i=0 ; i<w*h ; i++) {
			if(this.h >= 5 && i/this.w == 5) {
				buffer.append("\r\n..........\r\n") ;
				break ;
			}
			if(i != 0 && i % w == 0) {
				buffer = buffer.replace(buffer.length() - 1, buffer.length(), "") ;
				buffer.append("],\r\n[") ;
			}
			buffer.append(data[i]+" ,") ;
		}

		buffer = buffer.replace(buffer.length() - 1, buffer.length(), "") ;
		buffer.append("]]") ;
		return buffer.toString() ;
	}
}
