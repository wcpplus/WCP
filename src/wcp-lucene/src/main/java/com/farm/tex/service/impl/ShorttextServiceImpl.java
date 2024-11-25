package com.farm.tex.service.impl;

import com.farm.tex.domain.Shorttext;
import com.farm.tex.domainex.EmbSearchResults;
import com.farm.tex.domainex.ShortTextLessInfo;
import com.farm.tex.domainex.EmbSearchResults.INDEX_MODEL;
import com.farm.tex.index.ShortTextIndexFactory;
import com.farm.core.time.TimeTool;
import com.farm.llm.domain.LlmMessages;
import com.farm.llm.domain.LlmRefMaterials;
import com.farm.llm.domain.ReferenceMaterial;
import com.farm.lucene.adapter.DocMap;
import com.farm.parameter.FarmParameterService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.farm.tex.AiClientFactory;
import com.farm.tex.EmbeddingStarter;
import com.farm.tex.dao.ShorttextDaoInter;
import com.farm.tex.service.ShorttextServiceInter;
import com.farm.tex.utils.ParagraphSeparator;
import com.farm.core.sql.query.DBRule;
import com.farm.core.sql.query.DBRuleList;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.query.DataQuerys;
import com.farm.core.sql.result.DataResult;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import com.farm.core.auth.domain.LoginUser;

/* *
 *功能：知识段落文本服务层实现类
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@Service
public class ShorttextServiceImpl implements ShorttextServiceInter {
	@Resource
	private ShorttextDaoInter shorttextDaoImpl;

	private static final Logger log = Logger.getLogger(ShorttextServiceImpl.class);

	@Override
	@Transactional
	public void deleteShorttextEntity(String id, LoginUser user) {
		// TODO 自动生成代码,修改后请去除本注释
		shorttextDaoImpl.deleteEntity(shorttextDaoImpl.getEntity(id));
		ShortTextIndexFactory.getInstance().remove(id);
	}

	@Override
	@Transactional
	public Shorttext getShorttextEntity(String id) {
		// TODO 自动生成代码,修改后请去除本注释
		if (id == null) {
			return null;
		}
		return shorttextDaoImpl.getEntity(id);
	}

	@Override
	@Transactional
	public DataQuery createShorttextSimpleQuery(DataQuery query) {
		DataQuery dbQuery = DataQuery.init(query, "FARM_SHORT_TEXT",
				"ID,CTIME,CUSER,PSTATE,PCONTENT,STYPE,TITLE,DESCRIBES,SID,TYPEID,EMBLEN,PNO ,PALL,LEN,EMBTIME, EMBTMODEL");
		return dbQuery;
	}

	@Override
	@Transactional
	public void append(DocMap doc) {
		if (!FarmParameterService.getInstance().getParameterBoolean("config.index.short.text.able")) {
			return;
		}
		try {
			Shorttext entity = new Shorttext();
			// entity2.setPcontent(null);
			// entity.setCuser(null);
			// entity2.setId(entity.getId());
			entity.setCtime(TimeTool.getTimeDate14());
			entity.setPstate("1");
			String text = null;
			if (doc.getValue("DOMTYPE") != null) {
				entity.setTypeid(doc.getValue("TYPEID"));
				entity.setSid(doc.getValue("ID"));
				text = doc.getValue("TEXT");
			}
			if (StringUtils.isNotBlank(text)) {
				String title = null;
				if (StringUtils.isNotBlank(doc.getValue("TITLE"))) {
					title = doc.getValue("TITLE").length() > 110 ? doc.getValue("TITLE").substring(0, 100) + "..."
							: doc.getValue("TITLE");
				}
				List<String> paragraphs = ParagraphSeparator.doSeparat(text);
				int n = 1;
				for (String txt : paragraphs) {
					Shorttext entityNew = new Shorttext();
					entityNew.setCtime(entity.getCtime());
					entityNew.setPstate(entity.getPstate());
					entityNew.setTypeid(entity.getTypeid());
					entityNew.setSid(entity.getSid());
					// --------------------
					entityNew.setPall(paragraphs.size());
					entityNew.setStype(doc.getValue("DOMTYPE"));
					if (StringUtils.isNotBlank(title)) {
						entityNew.setTitle(title);
					}
					entityNew.setPno(n++);
					entityNew.setLen(txt.length());
					entityNew.setText(txt);
					entityNew.setDescribes(ParagraphSeparator.getDescribe(txt));
					shorttextDaoImpl.insertEntity(entityNew);
				}
			}
			if (!EmbeddingStarter.isStarted()) {
				// 启动语义模型生成
				EmbeddingStarter.start();
			}
			log.info("添加短文本：" + doc);
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	@Transactional
	public void remove(String id) {
		try {
			List<String> textIds = shorttextDaoImpl.getIdBySid(id);
			shorttextDaoImpl.deleteByAnyId(id);
			for (String tid : textIds) {
				ShortTextIndexFactory.getInstance().remove(tid);
			}
			log.info("删除短文本：" + id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	@Override
	@Transactional
	public void deleteEmbedding(String id, LoginUser currentUser) {
		if (!FarmParameterService.getInstance().getParameterBoolean("config.index.short.text.able")) {
			return;
		}
		Shorttext stext = shorttextDaoImpl.getEntity(id);
		stext.setEmbedding(null);
		stext.setEmbtime(null);
		stext.setEmbtmodel(null);
		stext.setPstate("1");
		shorttextDaoImpl.editEntity(stext);
		ShortTextIndexFactory.getInstance().remove(id);
	}

	@Override
	@Transactional
	public List<Shorttext> getNoEmbeddingTexts(int maxnum) {
		return shorttextDaoImpl.selectEntitys(DBRuleList.getInstance().add(new DBRule("EMBTMODEL", "NULL", "IS"))
				.add(new DBRule("PSTATE", "0", "!=")).toList(), maxnum);
	}

	@Override
	@Transactional
	public void editEmbedding(String id, byte[] embedding, int embeddingLen, String modelkey) {
		Shorttext st = shorttextDaoImpl.getEntity(id);
		st.setEmbedding(embedding);
		st.setEmbtime(TimeTool.getTimeDate14());
		st.setEmblen(embeddingLen);
		st.setEmbtmodel(modelkey);
		st.setPstate("2");
		shorttextDaoImpl.editEntity(st);
	}

	@Override
	@Transactional
	public List<Shorttext> getNoEmbDbTexts(int maxnum) {
		return shorttextDaoImpl.selectEntitys(DBRuleList.getInstance().add(new DBRule("PSTATE", "2", "=")).toList(),
				maxnum);
	}

	@Override
	@Transactional
	public void loadedIndexSuccess(Shorttext text) {
		Shorttext st = shorttextDaoImpl.getEntity(text.getId());
		st.setPstate("3");
		shorttextDaoImpl.editEntity(st);
		log.info("向量入库:" + text.getTitle() + "|" + text.getPno() + "/" + text.getPall());
	}

	@Override
	@Transactional
	public void reState(String id) {
		Shorttext st = shorttextDaoImpl.getEntity(id);
		if (st.getEmblen() == null) {
			st.setPstate("1");
		} else {
			st.setPstate("2");
		}
		shorttextDaoImpl.editEntity(st);
	}

	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	@Override
	@Transactional
	public LlmRefMaterials getQuestionsByAi(LlmMessages llmMsgs, EmbSearchResults texts) {
		LlmRefMaterials llmMaterials = new LlmRefMaterials();
		// 找出最接近答案的几条内容
		try {
			log.info("【" + texts.getIndexModel().name() + "|参考资料】-----索引库匹配资料：" + texts.getIds().size());
			// List<ShortTextLessInfo> infos=
			DataQuery query = DataQuery.getInstance(1, "LEN,TYPEID,ID", "FARM_SHORT_TEXT");
			query.addSqlRule(" and ID in (" + DataQuerys.parseSqlValues(texts.getIds()) + ")");
			query.setPagesize(texts.getIds().size());
			query.setNoCount();
			DataResult result = query.search();
			Map<String, Map<String, Object>> alltexts = new HashMap<String, Map<String, Object>>();
			for (Map<String, Object> row : result.getResultList()) {
				alltexts.put((String) row.get("ID"), row);
			}
			List<Map<String, Object>> toptexts = new ArrayList<Map<String, Object>>();
			for (String id : texts.getIds()) {
				// 排序
				Map<String, Object> row = alltexts.get(id);
				if (row != null) {
					toptexts.add(row);
				}
			}
			log.info("【" + texts.getIndexModel().name() + "|参考资料】-----数据库匹配资料：" + toptexts.size());
			// 参考资料序列<参考资料序列，同一知识分段序列>
			List<List<Shorttext>> references = new ArrayList<List<Shorttext>>();
			String titles = "";
			int ableLimitNum = 0;
			{// 合并参考资料
				// 知识字典，可以方便的找到对于知识序列《知识id，知识分段序列》
				Map<String, List<Shorttext>> allreference = new HashMap<String, List<Shorttext>>();
				int alltextNum = 0;
				for (Map<String, Object> row : toptexts) {
					if (true) {
						ableLimitNum++;
						alltextNum++;
						if (alltextNum > 20) {
							break;
						}
						ShortTextLessInfo info = (new ShortTextLessInfo((String) row.get("ID"),
								(Integer) row.get("LEN"), (String) row.get("TYPEID")));
						Shorttext tetst = shorttextDaoImpl.getEntity(info.getId());
						if (allreference.get(tetst.getSid()) == null) {
							// 如果沒有重複就添加
							List<Shorttext> list = new ArrayList<Shorttext>();
							list.add(tetst);
							allreference.put(tetst.getSid(), list);
							references.add(list);
							if (texts.getTitles().size() > 0) {
								// 如果参考知识有知识标题返回就通过AI获得最佳知识
								titles = titles + "<<" + texts.getTitle(tetst.getId()) + ">>" + ",";
							}
						} else {
							// 如果有重複就組合
							List<Shorttext> list = allreference.get(tetst.getSid());
							list.add(tetst);
						}
					}
				}
			}
			log.info("【" + texts.getIndexModel().name() + "|参考资料】-----权限过滤后" + ableLimitNum + "条，合并相同知识后台"
					+ references.size() + "条");
			for (int n = 0; n < 3; n++) {
				if (n < references.size()) {
					log.info("【" + texts.getIndexModel().name() + "|参考资料】-----默认顺序，第" + (n + 1) + "条："
							+ references.get(n).get(0).getTitle() + "["
							+ texts.getScores(references.get(n).get(0).getId()) + "]");
				}
			}

			// 模型推荐知识
			Set<String> recommendReferencesIds = null;

			// 如果是lucene会返回参考资料的title（有title就用AI查找最接近的知识）
			if (titles.trim().length() > 0 && texts.getIndexModel().equals(INDEX_MODEL.LUCENE)) {
				recommendReferencesIds = new HashSet<String>();
				// 选出最优资料
				String qtext = "用户的问题是<<" + llmMsgs.getCmsg()
						+ ">>请从我给你的这些参考材料中选出最多5个参考材料来回答这个问题，仅原样返回给我这些参考材料的标题,参考材料有:[" + titles + "]";
				log.info("【" + texts.getIndexModel().name() + "|参考资料】-----AI优化排序要求："
						+ AiClientFactory.getClient().getModelKey() + "-获取最优参考资料-问题:" + qtext);
				String msg = AiClientFactory.getClient().sendMsg(qtext);
				log.info("【" + texts.getIndexModel().name() + "|参考资料】-----AI优化排序结果："
						+ AiClientFactory.getClient().getModelKey() + "-获取最优参考资料-回答:" + msg);
				int sort = 10000;
				for (List<Shorttext> know_ts : references) {
					if (know_ts != null && know_ts.size() > 0 && ParagraphSeparator.getChatOnly(msg.toLowerCase())
							.indexOf(ParagraphSeparator.getChatOnly(know_ts.get(0).getTitle()).toLowerCase()) >= 0) {
						// 排序号为 标题在AI回答中的位置（這樣保证安装AI的顺序排列）
						know_ts.get(0).setSort(ParagraphSeparator.getChatOnly(msg.toLowerCase())
								.indexOf(ParagraphSeparator.getChatOnly(know_ts.get(0).getTitle()).toLowerCase()));
						// 推荐集合
						recommendReferencesIds.add(know_ts.get(0).getId());
						// 推荐集合与embedding用相同方式存储
						texts.addExcellentId(know_ts.get(0).getId());
					} else {
						// 排序号为10000+本来的排序
						know_ts.get(0).setSort(++sort);
					}
				}
				// references 重新排序
				references.sort(new Comparator<List<Shorttext>>() {
					@Override
					public int compare(List<Shorttext> o1, List<Shorttext> o2) {
						return o1.get(0).getSort() - o2.get(0).getSort();
					}
				});
			}

			{// 拼接问题和参考资料绑定
				int n2 = 0;
				for (List<Shorttext> know_ts : references) {
					String id = know_ts.get(0).getId();
					if (FarmParameterService.getInstance().getParameterBoolean("config.ai.index.search.excellent.only")
							&& !texts.getExcellentIds().contains(id)) {
						// 如果当前有模型推荐的知识，且没有推荐当前知识则不返回该知识
						continue;
					}
					String text = "";
					String mark = "STEXT" + know_ts.get(0).getId();
					know_ts.sort(new Comparator<Shorttext>() {
						@Override
						public int compare(Shorttext o1, Shorttext o2) {
							return o1.getPno() - o2.getPno();
						}
					});
					Set<String> hasid = new HashSet<String>();

					Float score = texts.getScores(know_ts.get(0).getId());

					for (Shorttext stext : know_ts) {
						if (!hasid.contains(stext.getId())) {
							if (text.trim().length() > 0) {
								log.info("【" + texts.getIndexModel().name() + "|参考资料】-----返回<" + (n2 + 1) + ">:"
										+ stext.getTitle() + "[" + score + "]-<合并段落>" + stext.getPno() + "/"
										+ stext.getPall());
							}
							text = text + stext.getText();
							hasid.add(stext.getId());
						}
					}
					// 最多7個
					if (++n2 <= 7) {
						ReferenceMaterial material = new ReferenceMaterial();
						String url = getTextUrl(know_ts.get(0));
						if (url != null) {
							material.setMarkId(mark);
							material.setTitle(know_ts.get(0).getTitle());
							material.setUrl(url);
							material.setText(text);
						}
						if(texts.getExcellentIds().contains(id)) {
							material.setExcellent(true);
						}
						log.info("【" + texts.getIndexModel().name() + "|参考资料】-----返回<" + n2 + ">:" + material.getTitle()
								+ "[" + score + "]|排序" + know_ts.get(0).getSort());
						llmMaterials.getList().add(material);
					} else {
						break;
					}
				}
			}
			return llmMaterials;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得知识的展示地址
	 * 
	 * @param info
	 * @return
	 */
	@Override
	@Transactional
	public String getTextUrl(Shorttext text) {
		// 1.文档知识，5资源知识,6引用知识 4小组首页7专题首页
		if (text.getStype().equals("doc") || text.getStype().equals("1") || text.getStype().equals("5")
				|| text.getStype().equals("6")) {
			return "webdoc/view/Pub" + text.getSid() + ".html";
		}
		if (text.getStype().equals("file")) {
			return "webdoc/view/PubFile" + text.getSid() + ".html";
		}
		if (text.getStype().equals("fqa")) {
			return "webquest/fqa/Pub" + text.getSid() + ".html";
		}
		if (text.getStype().equals("pri")) {
			// 私有知识
			return null;
		}
		return null;
	}

	@Override
	@Transactional
	public void errorHandle(String id, String message) {
		Shorttext st = shorttextDaoImpl.getEntity(id);
		st.setPstate("0");
		if (message != null) {
			st.setPcontent(message.length() > 125 ? message.substring(0, 120) : message);
		}
		shorttextDaoImpl.editEntity(st);
	}
}
