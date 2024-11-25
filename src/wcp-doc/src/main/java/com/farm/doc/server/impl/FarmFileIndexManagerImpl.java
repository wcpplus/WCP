package com.farm.doc.server.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.farm.doc.domain.FarmDocfile;
import com.farm.doc.domain.FarmDocgroup;
import com.farm.doc.domain.ex.DocEntire;
import com.farm.doc.domain.ex.TypeBrief;
import com.farm.doc.server.FarmDocIndexInter;
import com.farm.doc.server.FarmDocTypeInter;
import com.farm.doc.server.FarmDocgroupManagerInter;
import com.farm.doc.server.FarmFileIndexManagerInter;
import com.farm.doc.server.FarmFileManagerInter;

@Service
public class FarmFileIndexManagerImpl implements FarmFileIndexManagerInter {

	@Resource
	private FarmDocIndexInter farmDocIndexManagerImpl;
	@Resource
	private FarmFileManagerInter farmFileManagerImpl;
	@Resource
	private FarmDocTypeInter farmDocTypeManagerImpl;
	@Resource
	private FarmDocgroupManagerInter farmDocgroupManagerImpl;

	@Override
	@Transactional
	public void addFileLuceneIndex(String fileid, DocEntire doc, String text) {
		farmDocIndexManagerImpl.delLuceneIndex(doc, fileid);
		farmDocIndexManagerImpl.addLuceneIndex(fileid, farmFileManagerImpl.getFile(fileid).getName(), text, doc);
	}

	@Override
	@Transactional
	public void delFileLucenneIndex(String fileid, DocEntire doc) {
		farmDocIndexManagerImpl.delLuceneIndex(doc, fileid);
	}

	@Override
	@Transactional
	public void delFileLucenneIndexs(List<FarmDocfile> originalfiles, List<FarmDocfile> newfiles, DocEntire doc) {
		for (FarmDocfile file : originalfiles) {
			boolean isHave = false;
			for (FarmDocfile filenew : newfiles) {
				if (file.getId().equals(filenew.getId())) {
					isHave = true;
				}
			}
			if (!isHave) {
				delFileLucenneIndex(file.getId(), doc);
			}
		}
	}

	@Override
	@Transactional
	public void delFileLucenneIndex(String fileid) {
		// 在所有分类和小组中删除该知识
		List<TypeBrief> types = farmDocTypeManagerImpl.getPubTypes();
		List<String> types_str = new ArrayList<>();
		for (TypeBrief type : types) {
			types_str.add(type.getId());
		}
		List<FarmDocgroup> groups = farmDocgroupManagerImpl.getAllGroup();
		List<String> groups_str = new ArrayList<>();
		for (FarmDocgroup group : groups) {
			groups_str.add(group.getId());
		}
		farmDocIndexManagerImpl.delLuceneIndex(fileid, types_str, groups_str);
	}

	@Override
	public void handleFileLucenneIndexBymoveType(DocEntire oldDoce, DocEntire newdoc) {
		// 判断知识是否为资源文件，非资源知识不处理
		if (!newdoc.getDoc().getDomtype().equals("5")) {
			return;
		}
		// 判断是否移动了分类,未移动分类不处理
		if (oldDoce.getType().getId().equals(newdoc.getType().getId())) {
			return;
		}
		// 判断新的分类是否是被限制访问权限的,被限制的分类需要删除索引
		if (!newdoc.getType().getReadpop().equals("0")) {
			for (FarmDocfile file : oldDoce.getFiles()) {
				delFileLucenneIndex(file.getId(), oldDoce);
			}
		}
	}
}
