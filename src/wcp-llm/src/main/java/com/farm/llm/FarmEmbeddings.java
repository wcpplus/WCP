package com.farm.llm;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 向量到byte数组的转换
 * 
 * @author Wd
 *
 */
public class FarmEmbeddings {
	public static void main(String[] args) {
		// 假设有一个1024维的浮点数向量
		double[] vector = new double[1024];
		vector[0] = (double) 1.3;
		byte[] bytes = doubleArrayToByteArray(vector);
		// 转换回1024维浮点数向量
		vector = byteArrayToDoubleArray(bytes, 1024);
		//System.out.println(vector[0]);
	}

	public static float[] toFloatArrays(double[] doubleArray) {
		// 创建一个与Double数组长度相同的float数组
		float[] floatArray = new float[doubleArray.length];
		// 遍历Double数组并转换元素到float数组
		for (int i = 0; i < doubleArray.length; i++) {
			floatArray[i] = (float) doubleArray[i];
		}
		return floatArray;
	}

	public static double[] convertListToDoubleArray(List<Double> list) {
		double[] array = new double[list.size()];
		int i = 0;
		for (Double value : list) {
			array[i++] = value.doubleValue();
		}
		return array;
	}

	// 将double数组转换为字节数组
	public static byte[] doubleArrayToByteArray(double[] array) {
		ByteBuffer buffer = ByteBuffer.allocate(array.length * Double.BYTES);
		for (double value : array) {
			buffer.putDouble(value);
		}
		return buffer.array();
	}

	// 从字节数组中还原double数组
	public static double[] byteArrayToDoubleArray(byte[] byteArray, int length) {
		double[] array = new double[length];
		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
		for (int i = 0; i < length; i++) {
			array[i] = buffer.getDouble();
		}
		return array;
	}

	public static List<Float> toFloatList(double[] doubleArray) {
		// 创建一个与Double数组长度相同的float数组
		List<Float> floatArray = new ArrayList<Float>();
		// 遍历Double数组并转换元素到float数组
		for (int i = 0; i < doubleArray.length; i++) {
			floatArray.add((float) doubleArray[i]);
		}
		return floatArray;
	}

	public static List<Float> toFloatList(float[] embeddingFloats) {
		// 创建一个与Double数组长度相同的float数组
		List<Float> floatArray = new ArrayList<Float>();
		// 遍历Double数组并转换元素到float数组
		for (int i = 0; i < embeddingFloats.length; i++) {
			floatArray.add(embeddingFloats[i]);
		}
		return floatArray;
	}
}
