package com.farm.tex.index;

import com.farm.parameter.FarmParameterService;
import com.farm.tex.index.impl.MilvusQuerytor;
import com.farm.tex.index.impl.ShortTextLuceneIndex;

public class ShortTextIndexFactory {

	public static ShortTextIndexInter getInstance() {
		if (FarmParameterService.getInstance().getParameter("config.ai.index.db.type").equals("milvus")) {
			//FarmParameterService.getInstance().getParameter("config.ai.milves.server.ip")
			return new MilvusQuerytor( FarmParameterService.getInstance().getParameter("config.ai.milves.server.ip")
					,  FarmParameterService.getInstance().getParameterInt("config.ai.milves.server.port")
					,  FarmParameterService.getInstance().getParameter("config.ai.milves.server.loginname")
					,  FarmParameterService.getInstance().getParameter("config.ai.milves.server.password")
					,  FarmParameterService.getInstance().getParameter("config.ai.milves.server.database")
					,  FarmParameterService.getInstance().getParameter("config.ai.milves.collection.name"));
		}
		if (FarmParameterService.getInstance().getParameter("config.ai.index.db.type").equals("lucene")) {
			return new ShortTextLuceneIndex();
		}
		throw new RuntimeException(FarmParameterService.getInstance().getParameter("config.ai.index.db.type")
				+ ": the Index Type is not implementation !");
	}

}
