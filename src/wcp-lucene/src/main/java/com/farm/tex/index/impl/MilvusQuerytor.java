package com.farm.tex.index.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.dashscope.exception.ApiException;
import com.farm.llm.FarmEmbedding;
import com.farm.llm.FarmEmbeddings;
import com.farm.llm.FarmLlmInter;
import com.farm.tex.AiClientFactory;
import com.farm.tex.domain.Shorttext;
import com.farm.tex.domainex.AiQuestorMessage;
import com.farm.tex.domainex.EmbSearchResults;
import com.farm.tex.domainex.EmbeddingDbNode;
import com.farm.tex.domainex.EmbSearchResults.INDEX_MODEL;
import com.farm.tex.index.ShortTextIndexInter;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.CheckHealthResponse;
import io.milvus.grpc.DataType;
import io.milvus.grpc.MutationResult;
import io.milvus.grpc.SearchResults;
import io.milvus.param.ConnectParam;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.highlevel.dml.DeleteIdsParam;
import io.milvus.param.index.CreateIndexParam;

public class MilvusQuerytor implements ShortTextIndexInter {
	private static final Logger log = Logger.getLogger(MilvusQuerytor.class);
	private String COLLECTION_NAME;
	private String SERVER_IP;
	private int SERVER_PORT;
	private String SERVER_DATABASE;
	private String SERVER_NAME;
	private String SERVER_PASSWORD;

	/**
	 * @param collection_name 数据库集合名称
	 */
	public MilvusQuerytor(String server_ip, int server_port, String server_name, String server_password,
			String server_database, String collection_name) {
		this.COLLECTION_NAME = collection_name;
		this.SERVER_IP = server_ip;
		this.SERVER_PORT = server_port;
		this.SERVER_NAME = server_name;
		this.SERVER_PASSWORD = server_password;
		this.SERVER_DATABASE = server_database;
	}

	private MilvusServiceClient getClient() {
		ConnectParam connectParam = ConnectParam.newBuilder().withHost(SERVER_IP).withPort(SERVER_PORT)
				.withAuthorization(SERVER_NAME, SERVER_PASSWORD).withDatabaseName(SERVER_DATABASE).build();
		MilvusServiceClient client = new MilvusServiceClient(connectParam);
		return client;
	}

	@Override
	public void initIndex() {
		if (!isExitCollection()) {
			creatCollection();
			createIndex(EmbeddingDbNode.FIELD_INDEX_KEY);
		}
		MilvusServiceClient milvusClient = getClient();
		R<RpcStatus> response = milvusClient
				.loadCollection(LoadCollectionParam.newBuilder().withCollectionName(COLLECTION_NAME).build());
		isSuccess(response);
	}

	@Override
	public void appendIndex(Shorttext text) {
		// 向量嵌入
		List<EmbeddingDbNode> datas = new ArrayList<EmbeddingDbNode>();
		datas.add(new EmbeddingDbNode(text));
		insert(datas);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void remove(String id) {
		MilvusServiceClient milvusClient = getClient();
		try {
			List<String> ids = new ArrayList<String>();
			ids.add(id);
			milvusClient.delete(
					DeleteIdsParam.newBuilder().withCollectionName(COLLECTION_NAME).withPrimaryIds(ids).build());
		} finally {
			milvusClient.close();
		}
	}

	@Override
	public void finalsIndex() {
		// TODO Auto-generated method stub

	}

	@Override
	public EmbSearchResults search(AiQuestorMessage aiMessage) {
		FarmEmbedding quest;
		EmbSearchResults result = null;
		try {
			FarmLlmInter client = AiClientFactory.getClient();
			quest = client.getEmbedding(aiMessage.getUserQuestion());
			List<Float> queryparam = FarmEmbeddings.toFloatList(quest.getEmbeddingFloats());
			result = search(queryparam, client.getEmbeddingPassScore());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	// ---------------------------------------------------------------------------

	public enum Status {
		Success(0), UnexpectedError(1), ConnectFailed(2), PermissionDenied(3), CollectionNotExists(4),
		IllegalArgument(5), IllegalDimension(7), IllegalIndexType(8), IllegalCollectionName(9), IllegalTOPK(10),
		IllegalRowRecord(11), IllegalVectorID(12), IllegalSearchResult(13), FileNotFound(14), MetaFailed(15),
		IllegalResponse(-6);

		private int val;

		Status(int val) {
			this.val = val;
		}

		public int getVal() {
			return val;
		}
	}

	@SuppressWarnings("rawtypes")
	private static boolean isSuccess(R response) {
		if (response != null && response.getStatus() != null && Status.Success.val == response.getStatus()) {
			log.info("milvus return success！");
			return true;
		}
		throw new RuntimeException(response.getException().getMessage());
	}

	public boolean creatCollection() {
		String collectionName = COLLECTION_NAME;
		MilvusServiceClient milvusClient = getClient();
		try {
			FieldType id = FieldType.newBuilder().withName(EmbeddingDbNode.FIELD_ID_KEY).withDescription("主键id")
					.withDataType(DataType.VarChar).withMaxLength(32).withPrimaryKey(true).withAutoID(false).build();
			FieldType embd = FieldType.newBuilder().withName(EmbeddingDbNode.FIELD_INDEX_KEY).withDescription("特征值")
					.withDataType(DataType.FloatVector).withDimension(1536).build();
			CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
					.withCollectionName(collectionName).withDescription("特征集合").addFieldType(id)//
					.addFieldType(embd)//
					.build();
			R<RpcStatus> response = milvusClient.createCollection(createCollectionReq);
			return isSuccess(response);
		} finally {
			milvusClient.close();
		}
	}

	public boolean isLive() {
		MilvusServiceClient milvusClient = getClient();
		try {
			R<CheckHealthResponse> response = milvusClient.checkHealth();
			return isSuccess(response);
		} finally {
			milvusClient.close();
		}
	}

	public boolean isExitCollection() {
		MilvusServiceClient milvusClient = getClient();
		R<Boolean> response = milvusClient
				.hasCollection(HasCollectionParam.newBuilder().withCollectionName(COLLECTION_NAME).build());
		return isSuccess(response) && response.getData();
	}

	public boolean insert(List<EmbeddingDbNode> datas) {
		List<InsertParam.Field> fields = new ArrayList<>();
		List<String> pids = new ArrayList<>();
		List<List<Float>> allList = new ArrayList<List<Float>>();
		for (EmbeddingDbNode data : datas) {
			// --------------
			pids.add(data.getId());
			// --------------
			allList.add(data.getVectorList());
		}
		// --------------------
		fields.add(new InsertParam.Field(EmbeddingDbNode.FIELD_ID_KEY, pids));
		// --------------------
		fields.add(new InsertParam.Field(EmbeddingDbNode.FIELD_INDEX_KEY, allList));
		// --------------------
		MilvusServiceClient milvusClient = getClient();
		try {
			// 插入
			InsertParam insertParam = InsertParam.newBuilder().withCollectionName(COLLECTION_NAME).withFields(fields)
					.build();
			R<MutationResult> insert = milvusClient.insert(insertParam);
			return isSuccess(insert);
		} finally {
			milvusClient.close();
		}
	}

	public boolean loadCollection(String collectionName) {
		MilvusServiceClient milvusClient = getClient();
		try {
			R<RpcStatus> response = milvusClient.loadCollection(LoadCollectionParam.newBuilder()
					// 集合名称
					.withCollectionName(collectionName).build());
			return isSuccess(response);
		} finally {
			milvusClient.close();
		}
	}

	public boolean createIndex(String fieldName) {
		MilvusServiceClient milvusClient = getClient();
		try {
			R<RpcStatus> response = milvusClient
					.createIndex(CreateIndexParam.newBuilder().withCollectionName(COLLECTION_NAME)
							.withFieldName(fieldName).withIndexType(IndexType.IVF_FLAT).withMetricType(MetricType.L2)
							.withExtraParam("{\"nlist\":512}").withSyncMode(Boolean.FALSE).build());
			return isSuccess(response);
		} finally {
			milvusClient.close();
		}

	}

	public EmbSearchResults search(List<Float> questEmbedding, Integer embeddingPassScore) {
		MilvusServiceClient milvusClient = getClient();
		try {
			List<List<Float>> search_vectors = Arrays.asList(questEmbedding);
			SearchParam searchParam = SearchParam.newBuilder().withCollectionName(COLLECTION_NAME)
					.withMetricType(MetricType.L2).withTopK(100).withVectors(search_vectors)
					.withVectorFieldName(EmbeddingDbNode.FIELD_INDEX_KEY)
					// .withParams(SEARCH_PARAM)
					.build();
			R<SearchResults> respSearch = milvusClient.search(searchParam);
			if (respSearch.getStatus() != 0) {
				throw new ApiException(respSearch.getException());
			}
			EmbSearchResults result = new EmbSearchResults(INDEX_MODEL.MILVUS);
			int n = 0;
			for (float scores : respSearch.getData().getResults().getScoresList()) {
				String id = respSearch.getData().getResults().getIds().getStrId().getData(n);
				// 小于及格分数强力推荐
				if (embeddingPassScore != null) {
					if (scores < embeddingPassScore) {
						result.addExcellentId(id);
					}
					result.add(id, scores);
				} else {
					result.add(id, scores);
				}
				n++;
			}
			isSuccess(respSearch);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("embedding search:向量检索服务异常，请联系管理员");
		} finally {
			milvusClient.close();
		}
	}

	@Override
	public boolean isLive(boolean isThrowException) {
		isExitCollection();
		return true;
	}

}
