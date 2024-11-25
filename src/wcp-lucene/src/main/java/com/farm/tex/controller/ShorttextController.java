package com.farm.tex.controller;

import com.farm.tex.AiClientFactory;
import com.farm.tex.EmbeddingStarter;
import com.farm.tex.IndexingStarter;
import com.farm.tex.domain.Shorttext;
import com.farm.tex.index.ShortTextIndexFactory;
import com.farm.tex.index.ShortTextIndexInter;
import com.farm.tex.service.ShorttextServiceInter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import com.farm.web.easyui.EasyUiUtils;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;
import com.farm.core.page.RequestMode;
import com.farm.core.page.OperateType;
import com.farm.core.sql.query.DataQuery;
import com.farm.core.sql.result.DataResult;
import com.farm.core.sql.result.ResultsHandle;
import com.farm.llm.FarmEmbedding;
import com.farm.llm.FarmLlmInter;
import com.farm.llm.aliyun.client.AliyunLlmClient;
import com.farm.parameter.FarmParameterService;
import com.farm.core.page.ViewMode;
import com.farm.web.WebUtils;

/* *
 *功能：知识段落文本控制层
 *详细：
 *
 *版本：v0.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@RequestMapping("/shorttext")
@Controller
public class ShorttextController extends WebUtils {
	private final static Logger log = Logger.getLogger(ShorttextController.class);
	@Resource
	private ShorttextServiceInter shortTextServiceImpl;

	/**
	 * 查询结果集合
	 * 
	 * @return
	 */
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object> queryall(DataQuery query, HttpServletRequest request) {
		try {
			query = EasyUiUtils.formatGridQuery(request, query);
			DataResult result = shortTextServiceImpl.createShorttextSimpleQuery(query).search();
			result.runDictionary("1:文档知识,5:资源知识,6:引用知识,4:小组首页,7:专题首页,fqa:问答,file:附件,PRIVATE:私有文稿", "STYPE");
			result.runDictionary("fqa:问答,doc:知识,pri:私有文稿,file:附件", "STYPE");
			result.runformatTime("CTIME", "yyyy-MM-dd HH:mm");
			result.runformatTime("EMBTIME", "yyyy-MM-dd HH:mm");
			result.runDictionary("1:初始,2:嵌入模型,3:索引完成,0:异常", "PSTATE");
			result.runHandle(new ResultsHandle() {
				@Override
				public void handle(Map<String, Object> row) {
					row.put("PALL", row.get("PNO") + "/" + row.get("PALL"));
					if (StringUtils.isNotBlank((String) row.get("EMBTMODEL"))) {
						row.put("EMBTMODEL", row.get("EMBTMODEL") + ":" + row.get("EMBLEN"));
					}
				}
			});
			return ViewMode.getInstance().putAttrs(EasyUiUtils.formatGridData(result)).returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 删除数据
	 * 
	 * @return
	 */
	@RequestMapping("/del")
	@ResponseBody
	public Map<String, Object> delSubmit(String ids, HttpSession session) {
		try {
			for (String id : parseIds(ids)) {
				shortTextServiceImpl.deleteShorttextEntity(id, getCurrentUser(session));
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 重置数据状态
	 * 
	 * @return
	 */
	@RequestMapping("/restate")
	@ResponseBody
	public Map<String, Object> restate(String ids, HttpSession session) {
		try {
			for (String id : parseIds(ids)) {
				shortTextServiceImpl.reState(id);
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 删除数据
	 * 
	 * @return
	 */
	@RequestMapping("/delembedding")
	@ResponseBody
	public Map<String, Object> delembeddingSubmit(String ids, HttpSession session) {
		try {
			for (String id : parseIds(ids)) {
				shortTextServiceImpl.deleteEmbedding(id, getCurrentUser(session));
			}
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 生成语义向量
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/embedding")
	@ResponseBody
	public Map<String, Object> embeddingStart(HttpSession session) {
		try {
			EmbeddingStarter.start();
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	/**
	 * 插入到向量库
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/insertEmbdb")
	@ResponseBody
	public Map<String, Object> insertEmbdb(HttpSession session) {
		try {
			IndexingStarter.start();
			return ViewMode.getInstance().returnObjMode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ViewMode.getInstance().setError(e.getMessage()).returnObjMode();
		}
	}

	@RequestMapping("/list")
	public ModelAndView index(HttpSession session) {
		return ViewMode.getInstance().returnModelAndView("tex/ShorttextResult");
	}

	/**
	 * 显示详细信息（修改或浏览时）
	 *
	 * @return
	 */
	@RequestMapping("/form")
	public ModelAndView view(RequestMode pageset, String ids) {
		try {
			switch (pageset.getOperateType()) {
			case (0): {// 查看
				ViewMode view = ViewMode.getInstance();
				Shorttext stext = shortTextServiceImpl.getShorttextEntity(ids);
				view.putAttr("url", shortTextServiceImpl.getTextUrl(stext));
				byte[] bytes = stext.getEmbedding();
				FarmLlmInter client = AiClientFactory.getClient();
				if (stext.getEmblen() != null) {
					FarmEmbedding embedding = client.getEmbedding(bytes, stext.getEmblen());
					view.putAttr("floats",
							embedding == null ? null
									: "float[" + embedding.getEmbeddingFloats().length + "]="
											+ Arrays.toString(embedding.getEmbeddingFloats()));
				}
				return view.putAttr("pageset", pageset).putAttr("text", stext.getText().replaceAll("<", "&lt; "))
						.putAttr("entity", stext).returnModelAndView("tex/ShorttextForm");
			}
			case (1): {// 新增
				return ViewMode.getInstance().putAttr("pageset", pageset).returnModelAndView("tex/ShorttextForm");
			}
			case (2): {// 修改
				return ViewMode.getInstance().putAttr("pageset", pageset)
						.putAttr("entity", shortTextServiceImpl.getShorttextEntity(ids))
						.returnModelAndView("tex/ShorttextForm");
			}
			default:
				break;
			}
			return ViewMode.getInstance().returnModelAndView("tex/ShorttextForm");
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e + e.getMessage()).returnModelAndView("tex/ShorttextForm");
		}
	}

	/**
	 * 显示详细信息（修改或浏览时）
	 *
	 * @return
	 */
	@RequestMapping("/indexType")
	public ModelAndView indexType(RequestMode pageset) {
		try {
			ShortTextIndexInter index = ShortTextIndexFactory.getInstance();
			index.isLive(true);
			return ViewMode.getInstance().putAttr("info", "成功").returnModelAndView("tex/ShorttextInfo");
		} catch (Exception e) {
			return ViewMode.getInstance().setError(e.getMessage()).returnModelAndView("tex/ShorttextInfo");
		}
	}
}
