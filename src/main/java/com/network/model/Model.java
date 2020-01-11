package com.network.model;

import com.network.array.Array;

public class Model {
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
	
	public Model(Array w1, Array b1, Array w2, Array b2, int input_size, int hidden_size, int output_size,
			double learningRate, double initRate, int iters_num, int batch_size) {
		super();
		this.w1 = w1;
		this.b1 = b1;
		this.w2 = w2;
		this.b2 = b2;
		this.input_size = input_size;
		this.hidden_size = hidden_size;
		this.output_size = output_size;
		this.learningRate = learningRate;
		this.initRate = initRate;
		this.iters_num = iters_num;
		this.batch_size = batch_size;
	}
	
	public Model() {}
	
	// 学习率
	private double learningRate = 0.1 ;
	// 将初始权值变小
	private double initRate = 0.01 ;
	// 训练次数
	private int iters_num = 1000 ;
	// 最小批量大小
	private int batch_size = 100 ;
	public Array getW1() {
		return w1;
	}
	public void setW1(Array w1) {
		this.w1 = w1;
	}
	public Array getB1() {
		return b1;
	}
	public void setB1(Array b1) {
		this.b1 = b1;
	}
	public Array getW2() {
		return w2;
	}
	public void setW2(Array w2) {
		this.w2 = w2;
	}
	public Array getB2() {
		return b2;
	}
	public void setB2(Array b2) {
		this.b2 = b2;
	}
	public int getInput_size() {
		return input_size;
	}
	public void setInput_size(int input_size) {
		this.input_size = input_size;
	}
	public int getHidden_size() {
		return hidden_size;
	}
	public void setHidden_size(int hidden_size) {
		this.hidden_size = hidden_size;
	}
	public int getOutput_size() {
		return output_size;
	}
	public void setOutput_size(int output_size) {
		this.output_size = output_size;
	}
	public double getLearningRate() {
		return learningRate;
	}
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}
	public double getInitRate() {
		return initRate;
	}
	public void setInitRate(double initRate) {
		this.initRate = initRate;
	}
	public int getIters_num() {
		return iters_num;
	}
	public void setIters_num(int iters_num) {
		this.iters_num = iters_num;
	}
	public int getBatch_size() {
		return batch_size;
	}
	public void setBatch_size(int batch_size) {
		this.batch_size = batch_size;
	}
}
