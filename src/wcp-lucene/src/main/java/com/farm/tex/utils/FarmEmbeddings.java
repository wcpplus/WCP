package com.farm.tex.utils;

import java.nio.ByteBuffer;

/**向量到byte数组的转换
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

	// 将double数组转换为字节数组
	private static byte[] doubleArrayToByteArray(double[] array) {
		ByteBuffer buffer = ByteBuffer.allocate(array.length * Double.BYTES);
		for (double value : array) {
			buffer.putDouble(value);
		}
		return buffer.array();
	}

	// 从字节数组中还原double数组
	private static double[] byteArrayToDoubleArray(byte[] byteArray, int length) {
		double[] array = new double[length];
		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
		for (int i = 0; i < length; i++) {
			array[i] = buffer.getDouble();
		}
		return array;
	}
}
