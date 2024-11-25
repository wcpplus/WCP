package com.farm.tex.index;

import com.farm.tex.domain.Shorttext;
import com.farm.tex.domainex.AiQuestorMessage;
import com.farm.tex.domainex.EmbSearchResults;

/**
 * 短文本索引接口
 * 
 * @author Wd
 *
 */
public interface ShortTextIndexInter {

	/**
	 * 初始化数据库状态（比如创建数据等，保证后续可以直接插入数据）
	 */
	public void initIndex();

	/**
	 * 添加索引
	 * 
	 * @param text
	 */
	public void appendIndex(Shorttext text);

	/**
	 * 完成索引后的后置方法
	 */
	public void finalsIndex();

	/**
	 * 刪除索引
	 * 
	 * @param id 短文本索引id
	 */
	public void remove(String id);

	/**
	 * 查询索引
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public EmbSearchResults search(AiQuestorMessage message);

	/**
	 * 服务是否在线
	 * 
	 * @param isThrowException
	 */
	public boolean isLive(boolean isThrowException);
}
