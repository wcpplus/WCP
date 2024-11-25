package com.farm.llm.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.farm.llm.utils.FarmLlmTools;
import com.farm.llm.utils.LlmMessage;
import com.farm.llm.utils.LlmMessage.M_TYPE;

/**
 * 大模型对话
 * 
 * @author Wd
 *
 */
public class LlmMessages {
	/**
	 * 用户问题
	 */
	private String cmsg;
	/**
	 * 历史消息
	 */
	private List<LlmMessage> hismsgs;

	/**
	 * 参考资料
	 */
	private LlmRefMaterials materials;

	private LlmMessages() {

	}

	/**
	 * 获取参考资料
	 * 
	 * @return
	 */
	public LlmRefMaterials getMaterials() {
		if (materials == null) {
			return new LlmRefMaterials();
		}
		return materials;
	}

	public void setMaterials(LlmRefMaterials materials) {
		this.materials = materials;
	}

	public String getCmsg() {
		return cmsg;
	}

	public void setCmsg(String cmsg) {
		this.cmsg = cmsg;
	}

	public static LlmMessages getInstance(String cmsg) {
		LlmMessages lm = new LlmMessages();
		lm.setCmsg(cmsg);
		return lm;
	}

	public static LlmMessages getInstance(String cmsg, List<LlmMessage> his) {
		LlmMessages lm = new LlmMessages();
		lm.setCmsg(cmsg);
		lm.hismsgs = his;
		return lm;
	}

	/**
	 * 获取最近几轮历史消息
	 * 
	 * @param i
	 * @return
	 */
	public List<List<LlmMessage>> getHisMessage(int num, int hisMsgUnitCharNum) {
		if (hismsgs == null) {
			return new ArrayList<List<LlmMessage>>();
		}
		// 克隆原始List
		List<LlmMessage> clonedList = new ArrayList<LlmMessage>(hismsgs);
		// 逆序克隆后的List
		Collections.reverse(clonedList);
		List<List<LlmMessage>> list = new ArrayList<List<LlmMessage>>();
		// -----
		LlmMessage userm = null;
		LlmMessage serverm = null;
		int n = 0;
		for (LlmMessage lmOld : clonedList) {
			LlmMessage lm = new LlmMessage(FarmLlmTools.getLimitStr(lmOld.getMessage(), hisMsgUnitCharNum, "历史消息"),
					lmOld.getType());
			if (n++ == 0) {
				continue;
			}
			if (userm == null) {
				if (lm.getType().equals(M_TYPE.USER)) {
					userm = lm;
				}
			}
			if (serverm == null) {
				if (lm.getType().equals(M_TYPE.ASSISTANT)) {
					serverm = lm;
				}
			}
			if (userm != null && serverm != null) {
				List<LlmMessage> node = new ArrayList<LlmMessage>();
				node.add(userm);
				node.add(serverm);
				list.add(node);
				userm = null;
				serverm = null;
				num--;
				if (num <= 0) {
					break;
				}
			}
		}
		Collections.reverse(list);
		return list;
	}

}
