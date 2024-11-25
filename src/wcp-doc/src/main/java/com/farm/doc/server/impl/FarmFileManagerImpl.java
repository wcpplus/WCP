package com.farm.doc.server.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DBRule;
import com.farm.core.time.TimeTool;
import com.farm.doc.dao.FarmDocfileDaoInter;
import com.farm.doc.dao.FarmRfDoctextfileDaoInter;
import com.farm.doc.domain.FarmDocfile;
import com.farm.doc.domain.FarmRfDoctextfile;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.doc.server.commons.DocumentConfig;
import com.farm.doc.server.commons.FarmDocFiles;
import com.farm.parameter.FarmParameterService;

@Service
public class FarmFileManagerImpl implements FarmFileManagerInter {
	@Resource
	private FarmDocfileDaoInter farmDocfileDao;
	@Resource
	private FarmRfDoctextfileDaoInter farmRfDoctextfileDao;
	private static final Logger log = Logger.getLogger(FarmFileManagerImpl.class);
	/**
	 * 附件信息缓存
	 */
	private static final Map<String, FarmDocfile> fileCatch = new HashMap<String, FarmDocfile>();

	@Override
	@Transactional
	public String saveFile(File file, FILE_TYPE type, String title, LoginUser user) {
		String exName = FarmDocFiles.getExName(title);
		if (exName.trim().toUpperCase().replace(".", "").equals("ZIP")) {
			type = FILE_TYPE.RESOURCE_ZIP;
		}
		String userId = null;
		String userName = null;
		if (user == null || user.getName() == null) {
			userId = "none";
			userName = "none";
		} else {
			userId = user.getId();
			userName = user.getName();
		}
		FarmDocfile docfile = new FarmDocfile(FarmDocFiles.generateDir(),
				UUID.randomUUID().toString().replaceAll("-", ""), type.getValue(), title, file.getName(),
				TimeTool.getTimeDate14(), TimeTool.getTimeDate14(), userName, userId, userName, userId, "0", null,
				exName, Float.valueOf(String.valueOf(file.length())));
		if (user == null || user.getName() == null) {
			docfile.setCusername("none");
			docfile.setEusername("none");
		}
		FarmDocFiles.copyFile(file, FarmDocFiles.getFileDirPath() + docfile.getDir());
		docfile = farmDocfileDao.insertEntity(docfile);

		return docfile.getId();
	}

	public String saveFile(InputStream inStream, String filename, String title, FILE_TYPE type, LoginUser user) {
		String exName = FarmDocFiles.getExName(title);
		if (exName.trim().toUpperCase().replace(".", "").equals("ZIP")) {
			type = FILE_TYPE.RESOURCE_ZIP;
		}
		String userId = null;
		String userName = null;
		if (user == null || user.getName() == null) {
			userId = "none";
			userName = "none";
		} else {
			userId = user.getId();
			userName = user.getName();
		}
		FarmDocfile docfile = new FarmDocfile(FarmDocFiles.generateDir(),
				UUID.randomUUID().toString().replaceAll("-", ""), type.getValue(), title, filename,
				TimeTool.getTimeDate14(), TimeTool.getTimeDate14(), userName, userId, userName, userId, "0", null,
				exName, Float.valueOf(String.valueOf(0)));
		if (user == null || user.getName() == null) {
			docfile.setCusername("none");
			docfile.setEusername("none");
		}
		long length = FarmDocFiles.saveFile(inStream, filename,
				DocumentConfig.getString("config.doc.dir") + docfile.getDir());
		docfile.setLen(Float.valueOf(String.valueOf(length)));
		docfile = farmDocfileDao.insertEntity(docfile);
		return docfile.getId();
	}

	@Override
	public String getFileURL(String fileid) {
		String url = DocumentConfig.getString("config.doc.download.url") + fileid;
		return url;
	}

	@Override
	@Transactional
	public FarmDocfile getFile(String fileid) {
		if (fileCatch.containsKey(fileid)) {
			log.debug("load file from catch");
			return fileCatch.get(fileid);
		}
		FarmDocfile file = farmDocfileDao.getEntity(fileid);
		if (file == null) {
			return null;
		}
		file.setFile(new File(FarmDocFiles.getFileDirPath() + File.separator + file.getDir() + file.getFilename()));
		// 如果文件是大小是0的话就刷新文件大小
		if (file.getLen() == 0) {
			file.setLen(Float.valueOf(String.valueOf(file.getFile().length())));
			if (file.getLen() == 0) {
				file.setLen(Float.valueOf(-1));
			}
			farmDocfileDao.editEntity(file);
		}
		if (fileCatch.size() < 10000) {
			fileCatch.put(fileid, file);
		}
		return file;
	}

	@Override
	public File getNoneImg() {
		String imgpath = FarmParameterService.getInstance().getParameter("config.doc.none.img.path");
		return new File(FarmParameterService.getInstance().getParameter("farm.constant.webroot.path") + File.separator
				+ imgpath.replaceAll("\\\\", File.separator).replaceAll("//", File.separator));
	}

	@Override
	@Transactional
	public void submitFile(String fileId) {
		FarmDocfile file = farmDocfileDao.getEntity(fileId);
		file.setPstate("1");
		file.setEtime(TimeTool.getTimeDate14());
		farmDocfileDao.editEntity(file);
	}

	@Override
	@Transactional
	public void cancelFile(String fileId) {
		FarmDocfile file = farmDocfileDao.getEntity(fileId);
		if (file == null) {
			return;
		}
		file.setPstate("0");
		farmDocfileDao.editEntity(file);
	}

	@Override
	@Transactional
	public void delFile(String fileId, LoginUser user) {
		FarmDocfile docfile = farmDocfileDao.getEntity(fileId);
		if (docfile == null) {
			return;
		}
		File file = this.getFile(fileId).getFile();
		farmDocfileDao.deleteEntity(docfile);
		if (file.exists()) {
			if (file.delete()) {
				log.info("删除成功！");
			} else {
				log.error("文件删除失败,未能删除请手动删除！");
			}
		}
	}

	@Override
	@Transactional
	public FarmDocfile openFile(String exname, String content, LoginUser user) {
		FILE_TYPE type = FILE_TYPE.OHTER;
		String filename = UUID.randomUUID().toString();
		FarmDocfile docfile = new FarmDocfile(FarmDocFiles.generateDir(),
				UUID.randomUUID().toString().replaceAll("-", ""), type.getValue(), filename + "." + exname,
				filename + ".tmp", TimeTool.getTimeDate14(), TimeTool.getTimeDate14(), user.getName(), user.getId(),
				user.getName(), user.getId(), "0", content, exname, Float.valueOf(0));

		File file = new File(FarmDocFiles.getFileDirPath() + docfile.getDir() + File.separator + docfile.getFilename());
		try {
			if (!file.createNewFile()) {
				throw new RuntimeException("文件创建失败!");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		docfile = farmDocfileDao.insertEntity(docfile);
		docfile.setFile(file);
		return docfile;
	}

	@Override
	@Transactional
	public void addFileForDoc(String docid, String fileId, LoginUser user) {
		farmRfDoctextfileDao.insertEntity(new FarmRfDoctextfile(docid, fileId));
	}

	@Override
	@Transactional
	public void delFileForDoc(String docid, String fileId, LoginUser user) {
		List<DBRule> list = new ArrayList<DBRule>();
		list.add(new DBRule("FILEID", fileId, "="));
		list.add(new DBRule("DOCID", docid, "="));
		farmRfDoctextfileDao.deleteEntitys(list);
	}

	@Override
	public List<FarmDocfile> getAllFileForDoc(String docid) {
		List<FarmDocfile> refiles = farmDocfileDao.getEntityByDocId(docid);
		for (FarmDocfile file : refiles) {
			file = getFile(file.getId());
		}
		return refiles;
	}

	@Override
	public List<FarmDocfile> getAllTypeFileForDoc(String docid, String exname) {
		List<FarmDocfile> refiles = farmDocfileDao.getEntityByDocId(docid);
		List<FarmDocfile> newrefiles = new ArrayList<FarmDocfile>();
		for (FarmDocfile file : refiles) {
			if (file.getExname().toUpperCase().equals(exname.toUpperCase())) {
				file = getFile(file.getId());
				newrefiles.add(file);
			}
		}
		return newrefiles;
	}

	@Override
	public boolean containFileByDoc(String docid, String fileId) {
		List<FarmDocfile> list = farmDocfileDao.getEntityByDocId(docid);
		for (FarmDocfile node : list) {
			if (node.getId().equals(fileId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void delAllFileForDoc(String docid, String exname, LoginUser aloneUser) {
		List<FarmDocfile> refiles = farmDocfileDao.getEntityByDocId(docid);
		for (FarmDocfile file : refiles) {
			if (file.getExname().toUpperCase().equals(exname.toUpperCase())) {
				delFileForDoc(docid, file.getId(), aloneUser);
			}
		}

	}

	@Override
	@Transactional
	public void delFileForDoc(String docid, LoginUser user) {
		List<DBRule> list = new ArrayList<DBRule>();
		list.add(new DBRule("DOCID", docid, "="));
		farmRfDoctextfileDao.deleteEntitys(list);
	}
}
