package com.farm.llm;

public class FarmEmbedding {
	private String modelkey;
	private byte[] embeddingBytes;
	private float[] embeddingFloats;

	public FarmEmbedding(String modelkey, byte[] embeddingBytes, float[] embeddingFloats) {
		super();
		this.modelkey = modelkey;
		this.embeddingBytes = embeddingBytes;
		this.embeddingFloats = embeddingFloats;
	}

	public String getModelkey() {
		return modelkey;
	}

	public void setModelkey(String modelkey) {
		this.modelkey = modelkey;
	}

	public byte[] getEmbeddingBytes() {
		return embeddingBytes;
	}

	public void setEmbeddingBytes(byte[] embeddingBytes) {
		this.embeddingBytes = embeddingBytes;
	}

	public float[] getEmbeddingFloats() {
		return embeddingFloats;
	}

	public void setEmbeddingFloats(float[] embeddingFloats) {
		this.embeddingFloats = embeddingFloats;
	}

}
