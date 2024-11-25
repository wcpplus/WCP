package com.farm.tex;

import java.util.List;

import org.apache.log4j.Logger;

import com.farm.tex.domain.Shorttext;
import com.farm.tex.index.ShortTextIndexFactory;
import com.farm.tex.index.ShortTextIndexInter;

/**
 * 将向量数据插入到向量数据库
 * 
 * @author Wd
 *
 */
public class IndexingStarter {
	private static boolean isStarted = false;
	private static final Logger log = Logger.getLogger(IndexingStarter.class);
	public static boolean isStarted() {
		return isStarted;
	}

	private static int cnum = 0;

	public synchronized static void start() {
		if (!isStarted) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					isStarted = true;
					try {
						Thread.sleep(2000);
						ShortTextIndexInter index = ShortTextIndexFactory.getInstance();
						index.initIndex();
						while (true) {
							List<Shorttext> texts = ShortTextFactory.getInctance().getNoEmbDbTexts(100);
							if (texts.size() <= 0) {
								break;
							} else {
								// 向量化
								for (Shorttext text : texts) {
									try {
										cnum++;
										index.appendIndex(text);
										ShortTextFactory.getInctance().loadedIndexSuccess(text);
										Thread.sleep(100);
									} catch (Exception e) {
										log.error(e.getMessage(), e);
										ShortTextFactory.getInctance().errorHandle(text.getId(), e.getMessage());
									}
								}
							}
							index.finalsIndex();
							Thread.sleep(1000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						isStarted = false;
					}
				}
			}).start();
		} else {
			throw new RuntimeException("索引生成中" + cnum + "...");
		}
	}
}