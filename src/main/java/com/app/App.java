package com.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.network.array.Array;
import com.network.model.SimpleNet;

/**
 * Hello world!
 *
 */
public class App 
{
	public static Array parseTarget(char ch) {
		Array result = Array.zero.zero(1, 36) ;
		double[] data = result.getData() ;
		if(ch >= 48 && ch <= 57) {
	        int temp = ch - 48 ;
	        data[temp] = 1 ;
	        result.setData(data);
		}
		
	    if(ch >= 97 && ch <= 122) {
	        int temp = ch - 97 + 10 ;
	        data[temp] = 1 ;
	        result.setData(data);
	    }
	    result.reshape(36, 1);
	    return result ;
	}
	
	public static Array[] loadData(double split) throws Exception {
		File file = new File("./code_data/") ;
		File[] files = Array.random.randomArray(file.listFiles()) ;
		Array train = new Array(680) ;
		Array target = new Array(36) ;
		for(File f : files) {
			Array[] arrays = readImgFile2Array(f) ;
	    	train.push(arrays[0]) ;
	    	target.push(arrays[1]);
		}
		int sizeTrain = train.shape()[1] ;
		int sizeTest = target.shape()[1] ;
		Array xTrain = train.subArray(0, (int)((1-split)*sizeTrain)) ;
		Array tTrain = target.subArray(0, (int)((1-split)*sizeTest)) ;
		Array xTest = train.subArray((int)((1-split)*sizeTrain)) ;
		Array tTest = target.subArray((int)((1-split)*sizeTest)) ;
		return new Array[] {xTrain , tTrain , xTest , tTest} ;
	}
	
	public static Array[] readImgFile2Array(File f) throws IOException {
		Array arr1 = new Array(680) ;
		Array arr2 = new Array(36) ;
		BufferedImage img = ImageIO.read(f) ;
		int width = img.getWidth() ;
    	int height = img.getHeight() ;
    	int[] data = new int[width*height] ;
    	double[] d = new double[width*height] ;
    	img.getRGB(0, 0, width, height, data, 0, width) ;
    	for(int i=0 ; i<data.length ; i++) {
    		if(data[i] == -1) {
    			d[i] = 1.0 ;
    		}else {
    			d[i] = 0.0 ;
    		}
    	}
    	arr1.setData(d);
    	arr2 = parseTarget(f.getName().charAt(0)) ;
    	return new Array[] {arr1 , arr2} ;
	}
	
	// 通过下标确定这是哪一类
	public static char check(int index) {
		if(index < 10) {
			return (char)(index + 48) ;
		}else {
			return (char)(index - 10 + 97) ;
		}
	}
	
    // 训练模型
    public static void main(String[] args) throws Exception {
    	// 加载训练数据
    	Array[] arr = loadData(0.1) ;
    	// 初始化神经网络
		SimpleNet net = new SimpleNet(680 , 15 , 36 , 0.3 , 3000 , 100) ;
		// 训练神经网络
		net.fit(arr[0], arr[1] , arr[2] , arr[3]);
		// 保存模型
		net.save("./model.json");
		//net.load("./model.json");
	}
    
    // 加载模型
    public static void main1(String[] args) throws Exception {
    	// 加载模型
    	SimpleNet net = SimpleNet.load("./model.json") ;
    	// 从code_data读取数据，进行测试
    	File file = new File("./code_data/") ;
		File[] files = file.listFiles() ;
		Array x  ;
    	Array t  ;
		for(File f : files) {
			Array[] arrays = readImgFile2Array(f) ;
	    	x = arrays[0] ;
	    	t = arrays[1] ;
	    	System.out.print("真实结果："+check(t.max()[0])+"\t");
			Array result = net.predict(x) ;
			System.out.print("预测结果："+check(result.max()[0]));
			System.out.println();
			Thread.sleep(100);
			
			// 将预测错误的打印出来
			if(t.max()[0] != result.max()[0]) {
				System.out.println(check(t.max()[0])+"\t"+check(result.max()[0])+"\t"+f.getName());
			}
		}
	}
}
