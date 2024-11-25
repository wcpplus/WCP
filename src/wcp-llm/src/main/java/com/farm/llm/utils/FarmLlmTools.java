package com.farm.llm.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.farm.llm.domain.LlmMessages;
import com.farm.llm.domain.LlmRefMaterials;
import com.farm.llm.domain.ReferenceMaterial;
import com.farm.llm.utils.LlmMessage.M_TYPE;
import com.google.gson.Gson;

public class FarmLlmTools {
	// 历史消息最大占用当前消息量的百分比
	public static float HISMSGE_MAX_BILV = (float) 0.4;
	private static final Logger log = Logger.getLogger(FarmLlmTools.class);

	/**
	 * @param messages          消息
	 * @param maxCharNum        最大支持字符数
	 * @param hisMsgNum         历史记录数量（1表示一轮会话两个消息）
	 * @param hisMsgUnitCharNum 一条历史记录最多支持多少个字符的内容
	 * @return
	 */
	public static List<LlmMessage> getMessageManagers(LlmMessages messages, int maxCharNum, int hisMsgNum,
			int hisMsgUnitCharNum) {
		// 字符數量微調
		maxCharNum = maxCharNum - 100;
		String TIP_SYSTEM = "你作为一个知识库管理员来回答问题。你可以参考知识库中的参考知识来回答问题，如果对回答问题有帮助可以将[参考知识ID]推荐给我,我会根据[参考知识ID]来了解相关知识的详细信息";
		// 用户当前问题
		LlmMessage MAG_CURRENT_MESSAGE = new LlmMessage(
				getLimitStr(messages.getCmsg(), maxCharNum - TIP_SYSTEM.length(), "当前问题"), M_TYPE.USER);
		// 服务角色定义
		LlmMessage MAG_SYSTEM_TIP = new LlmMessage(TIP_SYSTEM, M_TYPE.SYSTEM);
		// 用户历史消息
		List<LlmMessage> MAG_HIS_MESSAGES = new ArrayList<LlmMessage>();
		for (List<LlmMessage> msgs : messages.getHisMessage(hisMsgNum, hisMsgUnitCharNum)) {
			// 历史消息可用空间
			int limit = (int) ((maxCharNum
					- getAllMsgLenght(getAllMsg(MAG_SYSTEM_TIP, MAG_CURRENT_MESSAGE, MAG_HIS_MESSAGES)))
					* HISMSGE_MAX_BILV);
			int lenght = 0;
			for (LlmMessage ms : msgs) {
				lenght = lenght + new Gson().toJson(ms).length();
			}
			if (lenght < limit) {
				for (LlmMessage hism : msgs) {
					LlmMessage hs = new LlmMessage(
							getLimitStr(LlmMessage.HtmlRemoveTag(hism.getMessage()), hisMsgUnitCharNum, "历史消息"),
							hism.getType());
					MAG_HIS_MESSAGES.add(hs);
				}
			}
		} 
		{// 通过计算剩余字符数两添加参考知识
			boolean isHasReference = false;
			int limit = (int) ((maxCharNum
					- getAllMsgLenght(getAllMsg(MAG_SYSTEM_TIP, MAG_CURRENT_MESSAGE, MAG_HIS_MESSAGES))));
			String materials = "，知识库中的参考知识如下：【";
			for (ReferenceMaterial material : messages.getMaterials().getList()) {
				String n = "";
				if (material.getUrl() != null && material.getMarkId() != null) {
					n = n + "‘参考知识ID‘:‘" + material.getMarkId().replace("‘", "\\‘") + "‘";
				} else {
					// n=n+"‘参考知识ID‘:"+material.getMarkId();
				}
				if (material.getText() != null) {
					n = n + "，‘知识内容‘:‘" + material.getText().replace("‘", "\\‘") + "‘";
				}
				if (material.getTitle() != null) {
					n = n + "，‘知识标题‘:‘" + material.getTitle().replace("‘", "\\‘") + "‘";
				}

				int cnuml = materials.length();
				if (limit - cnuml > 100) {
					materials = materials + "{" + n + "},";
					isHasReference = true;
				} else {
					break;
				}
			}
			if (!isHasReference) {
				materials = "，知识库中无参考知识。";
				isHasReference = true;
			}
			int limitfinal = limit - materials.length();
			if (limitfinal < 0) {
				materials = materials.substring(0, materials.length() + limitfinal);
				materials = materials + "}";
			}
			materials = materials + "】";
			materials = materials.replace(",】", "】");
			if (isHasReference) {
				MAG_CURRENT_MESSAGE
						.setMessage("我的问题是”" + MAG_CURRENT_MESSAGE.getMessage().replace("“", "\"") + "“" + materials+"");
			}
		}

		log.info("消息长度:" + getAllMsgLenght(getAllMsg(MAG_SYSTEM_TIP, MAG_CURRENT_MESSAGE, MAG_HIS_MESSAGES)));
		return getAllMsg(MAG_SYSTEM_TIP, MAG_CURRENT_MESSAGE, MAG_HIS_MESSAGES);
	}

	/**
	 * 构造消息列表
	 * 
	 * @param MAG_SYSTEM_TIP      系统提示
	 * @param MAG_CURRENT_MESSAGE 当前消息
	 * @param MAG_HIS_MESSAGES    历史消息
	 * @return
	 */
	private static List<LlmMessage> getAllMsg(LlmMessage MAG_SYSTEM_TIP, LlmMessage MAG_CURRENT_MESSAGE,
			List<LlmMessage> MAG_HIS_MESSAGES) {
		List<LlmMessage> backList = new ArrayList<LlmMessage>();
		backList.add(MAG_SYSTEM_TIP);
		backList.addAll(MAG_HIS_MESSAGES);
		backList.add(MAG_CURRENT_MESSAGE);
		return backList;
	}

	/**
	 * 获得消息列表长度
	 * 
	 * @param ms
	 * @return
	 */
	private static int getAllMsgLenght(List<LlmMessage> ms) {
		return new Gson().toJson(ms).length();
	}

	public static void main(String[] args) {

		// [{历史消息}{历史消息}...]
		List<LlmMessage> hisMsgs = new ArrayList<LlmMessage>();
		for (int n = 0; n < 5; n++) {
			hisMsgs.add(new LlmMessage(getTestString("历史消息" + n, 700), M_TYPE.USER, "YYYY-MM-dd"));
			hisMsgs.add(new LlmMessage(getTestString("历史消息" + n, 700), M_TYPE.ASSISTANT, "YYYY-MM-dd"));
		}
		hisMsgs.add(new LlmMessage(getTestString("历史消息-当前", 700), M_TYPE.USER, "YYYY-MM-dd"));
		// 当前问题
		LlmMessages lls = LlmMessages.getInstance(getTestString("当前消息", 500), hisMsgs);
		{
			// 《参考资料。。。》}
			LlmRefMaterials lrm = new LlmRefMaterials();
			ReferenceMaterial material = new ReferenceMaterial();
			material.setMarkId("MARKID0000000000000000000000000000000000");
			material.setTitle(getTestString("标题", 10000));
			material.setUrl(getTestString("url", 10050));
			material.setText(getTestString("正文", 10121));
			lrm.getList().add(material);
			lls.setMaterials(lrm);
			// {角色定义}OK
		}
		List<LlmMessage> list = getMessageManagers(lls, 3500, 3, 500);
		String showStr = new Gson().toJson(list);
		// System.out.println(showStr.length());
		// System.out.println(showStr);
	}

	/**
	 * 自动截取限制的字符串
	 * 
	 * @param chats
	 * @param n
	 * @return
	 */
	public static String getLimitStr(String chats, int n, String logFlag) {
		if (chats == null) {
			return null;
		}
		String str = chats.length() > n ? chats.substring(0, n) : chats;
		return str;
	}

	/**
	 * 获取测试字符串
	 * 
	 * @param chats
	 * @param n
	 * @return
	 */
	private static String getTestString(String chats, int n) {

		n = n / 150;

		String str = "";
		for (int i = chats.length(); i < n; i++) {
			str = str + "加";
		}
		return chats + str;
	}

}
