package com.network.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import com.network.array.Array;

/**
 * 实现简单的两层神经网络
 * @author root
 *
 */
public class SimpleNet {
	Logger log = Logger.getLogger("SimpleNet") ;
	
	// 第一层的权重
	private Array w1 ;
	private Array b1 ;
	
	// 第二层的权重
	private Array w2 ;
	private Array b2 ;
	
	// 输入数量
	private int input_size ;
	// 隐藏层数量
	private int hidden_size ;
	// 输出层数量
	private int output_size ;
	
	// 学习率
	private double learningRate = 0.1 ;
	// 将初始权值变小
	private double initRate = 0.01 ;
	// 训练次数
	private int iters_num = 1000 ;
	// 最小批量大小
	private int batch_size = 100 ;
	
	public SimpleNet(int input_size , int hidden_size , int output_size) {
		init(input_size , hidden_size , output_size , this.learningRate , this.iters_num , this.batch_size) ;
	}
	
	public SimpleNet(int input_size , int hidden_size , int output_size , double learningRate) {
		init(input_size , hidden_size , output_size , learningRate , this.iters_num , this.batch_size) ;
	}
	
	public SimpleNet(int input_size , int hidden_size , int output_size , double learningRate , int iters_num) {
		init(input_size , hidden_size , output_size , learningRate , iters_num , this.batch_size) ;
	}
	
	public SimpleNet(int input_size , int hidden_size , int output_size , double learningRate , int iters_num , int batch_size) {
		init(input_size , hidden_size , output_size , learningRate , iters_num , batch_size) ;
	}
	
	// 初始化
	public void init(int input_size , int hidden_size , int output_size , double learningRate , int iters_num , int batch_size) {
		this.input_size = input_size ;
		this.hidden_size = hidden_size ;
		this.output_size = output_size ;
		this.learningRate = learningRate ;
		this.iters_num = iters_num ;
		this.batch_size = batch_size ;
		
		this.w1 = Array.random.random(input_size, hidden_size).mul(initRate) ;
		this.b1 = Array.random.random(1 , hidden_size) ;
		
		this.w2 = Array.random.random(hidden_size, output_size).mul(initRate) ;
		this.b2 = Array.random.random(1, output_size) ;
	}
	
	// 加载模型方法
	public static SimpleNet load(String modelPath) throws Exception {
		StringBuffer buffer = new StringBuffer() ;
		File file = new File(modelPath) ;
		BufferedReader reader = null ;
		try {
			reader = new BufferedReader(new FileReader(file)) ;
			
			String line = "" ;
			while((line = reader.readLine()) != null) {
				buffer.append(line) ;
			}
			
			Model model = parse(buffer.toString()) ;
			
			SimpleNet net = new SimpleNet(model.getInput_size(), model.getHidden_size(), model.getOutput_size(), model.getLearningRate(), model.getIters_num(), model.getBatch_size()) ;
			net.setW1(model.getW1());
			net.setB1(model.getB1());
			net.setW2(model.getW2());
			net.setB2(model.getB2());
			
			return net ;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("模型加载失败") ;
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 保存模型 数据格式 input_size|hidden_size|output_size|learningRate|initRate|iters_num|batch_size|w1|b1|w2|b2 
	public void save(String modelPath) throws Exception {
		log.info("正在保存。。。");
		FileWriter write = null ;
		try {
			write = new FileWriter(new File(modelPath)) ;
			String data = this.input_size+"|"+this.hidden_size+"|"+output_size+"|"+this.learningRate+"|"+this.initRate+"|"+this.iters_num+"|"+this.batch_size+"|" ;
			double[] dataW1 = this.w1.getData() ;
			String strw1 = array2String(dataW1 , this.w1.shape()[0]*this.w1.shape()[1]) ;
			data += strw1 ;
			double[] dataB1 = this.b1.getData() ;
			String strb1 = array2String(dataB1, this.b1.shape()[0]*this.b1.shape()[1]) ;
			data += "|"+strb1 ;
			double[] dataW2 = this.w2.getData() ;
			String strw2 = array2String(dataW2, this.w2.shape()[0]*this.w2.shape()[1]) ;
			data += "|"+strw2 ;
			double[] dataB2 = this.b2.getData() ;
			String strb2 = array2String(dataB2, this.b2.shape()[0]*this.b2.shape()[1]) ;
			data += "|"+strb2 ;
			write.write(data);
		} catch(Exception e) {
			throw new Exception("文件写入失败") ;
		} finally {
			if(write != null) {
				try {
					write.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		log.info("保存完成");
	}
	
	// 将数组转string，将数组1，2，3
	private String array2String(double[] data , int len) {
		StringBuffer buffer = new StringBuffer() ;
		for(int i = 0 ; i < len ; i++) {
			buffer.append(data[i]+",") ;
		}
		buffer.replace(buffer.length() - 1, buffer.length(), "") ;
		return buffer.toString() ;
	}
	
	// 格式化数组
	private static double[] parseArray(String data) {
		String[] strs = data.split(",") ;
		double[] result = new double[strs.length] ;
		for(int i = 0 ; i < strs.length ; i++) {
			result[i] = Double.parseDouble(strs[i]) ;
		}
		return result ;
	}
	
	// 解析模型 数据格式 input_size|hidden_size|output_size|learningRate|initRate|iters_num|batch_size|w1|b1|w2|b2 
	private static Model parse(String data) {
		String[] strs = data.split("\\|" , 11) ;
		int input_size = Integer.parseInt(strs[0]);
		int hidden_size = Integer.parseInt(strs[1]);
		int output_size = Integer.parseInt(strs[2]);
		
		double learningRate = Double.parseDouble(strs[3]) ;
		double initRate = Double.parseDouble(strs[4]) ;
		int iters_num = Integer.parseInt(strs[5]) ;
		int batch_size = Integer.parseInt(strs[6]) ;
		Array w1 = new Array(hidden_size) ;
		Array b1 = new Array(hidden_size) ;
		Array w2 = new Array(output_size) ;
		Array b2 = new Array(output_size) ;
		w1.setData(parseArray(strs[7]));
		b1.setData(parseArray(strs[8]));
		w2.setData(parseArray(strs[9]));
		b2.setData(parseArray(strs[10]));
		return new Model(w1, b1, w2, b2, input_size, hidden_size, output_size, learningRate, initRate, iters_num, batch_size) ;
	}
	
	// 小批量梯度下降法训练 , Array xTest , Array tTest
	public void fit(Array xTrain , Array tTrain , Array xTest , Array tTest) throws Exception {
		// 每隔多少次求一次准确率
		int temp = xTrain.shape()[1] / this.batch_size ;
		int iter_per_epoch = temp == 0 ? 1 : temp ;
		
		for(int i = 0 ; i < this.iters_num ; i++) {
			int[] batchMask = Array.random.choice(xTrain.shape()[1], this.batch_size) ;
			Array batch_x = xTrain.I(batchMask) ;
			Array batch_t = tTrain.I(batchMask) ;
			
			// 1.前向传播
			Array a1 = batch_x.dot(this.w1).add(this.b1) ;
			Array z1 = a1.sigmoid() ;
			Array a2 = z1.dot(this.w2).add(this.b2) ;
			Array y  = a2.softmax() ;
			
			// 2.反向传播
			// 2.1求softmax函数的反向传播(y - t) / len(y)
			Array dy = y.sub(batch_t).div(batch_x.shape()[1]) ;
			// 矩阵点乘的反向传播 输入信号的转置点乘softmax反向传播传过来的值
			Array dw2 = z1.T().dot(dy) ;
			Array db2 = dy.sum(0) ;
			// 求dx
			Array dx = dy.dot(this.w2.T()) ;
			// 2.2求sigmoid的反向传播 （1 - y）*y
			Array dz = z1.sub(1).mul(-1).mul(z1).mul(dx) ;
			Array dw1 = batch_x.T().dot(dz) ;
			Array db1 = dz.sum(0) ;
			
			// 更新权值
			this.w1 = this.w1.sub(dw1.mul(this.learningRate)) ;
			this.b1 = this.b1.sub(db1.mul(this.learningRate)) ;
			this.w2 = this.w2.sub(dw2.mul(this.learningRate)) ;
			this.b2 = this.b2.sub(db2.mul(this.learningRate)) ;
			
			// 计算准确度
			if(i % iter_per_epoch == 0) {
				Array s = predict(xTrain) ; 
				Array m = predict(xTest) ;
				System.out.println("训练数据准确率 | 测试数据准确率 "+Array.samePer(s.max(), tTrain.max())+" | "+Array.samePer(m.max(), tTest.max()));
//				System.out.println(this.w1) ;
			}
		}
		Array s = predict(xTrain) ; 
		Array m = predict(xTest) ;
		System.out.println("训练完成：\r\n\t训练数据准确率："+Array.samePer(s.max(), tTrain.max())+"\r\n\t测试数据准确率："+Array.samePer(m.max(), tTest.max()));
	}
	
	// 预测方法
	public Array predict(Array source) throws Exception {
		source = source.dot(this.w1).add(this.b1) ;
		source = source.sigmoid();
		source = source.dot(this.w2).add(this.b2) ;
		source = source.softmax() ;
		return source ;
	}
	
	public Array getW1() {
		return w1 ;
	}

	private void setW1(Array w1) {
		this.w1 = w1 ;
	}

	public Array getB1() {
		return b1 ;
	}

	private void setB1(Array b1) {
		this.b1 = b1 ;
	}

	public Array getW2() {
		return w2 ;
	}

	private void setW2(Array w2) {
		this.w2 = w2 ;
	}

	public Array getB2() {
		return b2 ;
	}

	private void setB2(Array b2) {
		this.b2 = b2;
	}
}
