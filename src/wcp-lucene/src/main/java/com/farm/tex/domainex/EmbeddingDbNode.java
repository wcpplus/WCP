package com.farm.tex.domainex;

import java.util.ArrayList;
import java.util.List;

import com.farm.llm.FarmEmbeddings;
import com.farm.tex.domain.Shorttext;

public class EmbeddingDbNode {

	public static final String FIELD_INDEX_KEY = "EMBD";
	public static final String FIELD_ID_KEY = "ID";

	private String id;
	private float[] vector;

	public EmbeddingDbNode(String id, float[] vector) {
		super();
		this.id = id;
		this.vector = vector;
	}

	public EmbeddingDbNode(Shorttext text) {
		super();
		double[] embdb = FarmEmbeddings.byteArrayToDoubleArray(text.getEmbedding(), text.getEmblen());
		this.id = text.getId();
		this.vector = FarmEmbeddings.toFloatArrays(embdb);

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float[] getVector() {
		return vector;
	}

	public void setVector(float[] vector) {
		this.vector = vector;
	}

	public List<Float> getVectorList() {
		List<Float> floatList = new ArrayList<>();
		for (float floatValue : vector) {
			floatList.add(floatValue);
		}
		return floatList;
	}

}
