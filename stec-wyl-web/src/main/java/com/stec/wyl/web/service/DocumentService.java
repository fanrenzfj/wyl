package com.stec.wyl.web.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.framework.metadata.usage.bean.FileInfoBean;
import com.stec.framework.metadata.usage.bean.ImageInfoBean;
import com.stec.masterdata.entity.basic.DocAttach;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.handler.basic.DocAttachHandler;
import com.stec.masterdata.handler.basic.DocInfoHandler;
import com.stec.utils.*;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.io.FileNameUtil;
import jodd.io.FileUtil;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import stec.framework.excel.ExcelTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author jim.xie
 */
@Service
public class DocumentService {

	@Autowired
	private ExcelTemplate excelTemplate;

	@Autowired
	private SessionUtils sessionUtils;

	@Reference
	private DocInfoHandler docInfoHandler;

	@Reference
	private DocAttachHandler docAttachHandler;

	@Value("${doc.service.rest.url}")
	private String DOC_SERVER_URL;

	@Value("${doc.tmp.path}")
	private String DOC_TMP_PATH;

	@Value("${doc.app.key}")
	private String DOC_APP_KEY;

	public String getServerUrl() {
		return DOC_SERVER_URL;
	}

	public <T> List<T> restUploadExcel(MultipartRequest request, String fileName, Class<T> clazz) throws Exception {
		MultipartFile file = request.getFile(fileName);
		if (ObjectUtils.isNull(file) || file.isEmpty()) {
			throw new FileUploadException("file is null");
		}
		return excelTemplate.readExcel2Objects(file.getInputStream(), clazz);
	}

	private <T extends FileInfoBean> T restUploadFile(MultipartFile file, Class<T> clazz) throws Exception {
		if (ObjectUtils.isNull(file)) {
			throw new FileUploadException("file is null");
		}
//		// 文件保存路径
//		String filePath = file.getOriginalFilename();
//		String tempPath = DOC_TMP_PATH;
//		if (StringUtils.isEmpty(tempPath)) {
//			tempPath = getClass().getResource("/").getFile();
//		}
////		File localFile = new File(tempPath + filePath);

		File dir;
		dir = FileUtil.createTempDirectory();
		File localFile = new File(FileNameUtil.concat(dir.getPath(), file.getOriginalFilename(), true));
		FileUtils.writeByteArrayToFile(localFile, file.getBytes());


		// 转存文件
		file.transferTo(localFile);
		// 对图片进行旋转处理
		ImageRotateUtils.judgeRotate(localFile.getPath());
		HttpResponse httpResponse = HttpRequest
				.post(DOC_SERVER_URL + "api/upload")
				.form("file", localFile)
				.form("filename", localFile.getName())
				.form("appKey", DOC_APP_KEY)
				.send();
		String fileResponse = JSON.parseObject(httpResponse.body(), HashMap.class).get("result").toString();
		localFile.delete();
		List<T> beans = JSON.parseArray(fileResponse, clazz);
		return beans.get(0);
	}

	public FileInfoBean uploadFile(MultipartRequest request, String fileName) throws Exception {
		MultipartFile file = request.getFile(fileName);
		if (ObjectUtils.isNull(file) || file.isEmpty()) {
			throw new ServiceException("file error, file is empty");
		}
		return restUploadFile(file, FileInfoBean.class);
	}

	public ImageInfoBean uploadImage(MultipartRequest request, String fileName) throws Exception {
		MultipartFile file = request.getFile(fileName);
		if (ObjectUtils.isNull(file) || file.isEmpty()) {
			throw new ServiceException("file error, file is empty");
		}
		return restUploadFile(file, ImageInfoBean.class);
	}

	public List<ImageInfoBean> uploadImages(MultipartRequest request, String filename) throws Exception {
		List<MultipartFile> files = request.getFiles(filename);
		if (ObjectUtils.isNull(files) || files.size() <= 0) {
			throw new ServiceException("file error, file is empty");
		}
		List<ImageInfoBean> saveList = new ArrayList<>();
		for (MultipartFile file : files) {
			if (file != null && !file.isEmpty()) {
				saveList.add(restUploadFile(file, ImageInfoBean.class));
			}
		}
		return saveList;
	}

	public List<FileInfoBean> uploadFiles(MultipartRequest request, String fileName) throws Exception {
		List<MultipartFile> files = request.getFiles(fileName);
		if (ObjectUtils.isNull(files) || files.size() <= 0) {
			return new ArrayList<>();
//            throw new ServiceException("file error, file is empty");
		}
		List<FileInfoBean> saveList = new ArrayList<>();
		for (MultipartFile file : files) {
			if (file != null && !file.isEmpty()) {
				saveList.add(restUploadFile(file, FileInfoBean.class));
			}
		}
		return saveList;
	}

	public DocInfo uploadFiles(MultipartRequest request, String fileName, Long docId, Long[] delIds) throws Exception {
		List<FileInfoBean> files = this.uploadFiles(request, fileName);
		DocInfo docInfo;
		//判断DocInfo对象是否是空 是空表示新增，非空代表修改
		if (ObjectUtils.isNull(docId)) {
			docInfo = new DocInfo();
			docInfo.setCreateDate(TimeUtil.now());
			//获取当前用户id
			docInfo.setUpdateUserId(sessionUtils.getCurrentUser().getId());
			docInfo = docInfoHandler.save(docInfo);
			docId = docInfo.getId();
		} else {
			docInfo = docInfoHandler.selectByPrimaryKey(docId);
			docInfo.setUpdateUserId(sessionUtils.getCurrentUser().getId());
			docInfo.setUpdateDate(TimeUtil.now());
		}
		List<DocAttach> saveList = new ArrayList<>();
		for (FileInfoBean file : files) {
			DocAttach info = new DocAttach();
			info.setContentType(file.getContentType());
			info.setOriginalFileName(file.getOriginalName());
			info.setFileName(this.getFileUrl(file));
			info.setLength(file.getLength());
			info.setDocInfoId(docId);
			info.setUpdateDate(TimeUtil.now());
			saveList.add(info);
		}
		docAttachHandler.save(saveList);
		//如果是修改那么之前保存的文档 都应该删除
		if (ArrayUtils.isNotEmpty(delIds)) {
			for (Long delId : delIds) {
				docAttachHandler.deleteByPrimaryKey(delId);
			}
		}
		return docInfo;
	}

	public String getFullDocUrl(FileInfoBean bean) {
		String url = DOC_SERVER_URL;
		url += ((bean instanceof ImageInfoBean) ? "img/" : "doc/");
		return url + getFileUrl(bean);
	}

	public String getFileUrl(FileInfoBean bean) {
		return bean.getId() + "." + bean.getFileName().split("\\.")[bean.getFileName().split("\\.").length - 1];
	}

	public String serverUrl() {
		return DOC_SERVER_URL;
	}

	public ImageInfoBean uploadLocalPic(String path){
		File localFile = new File(path);
		// 转存文件
//		file.transferTo(localFile);
		// 对图片进行旋转处理
		ImageRotateUtils.judgeRotate(localFile.getPath());
		HttpResponse httpResponse = HttpRequest
				.post(DOC_SERVER_URL + "api/upload")
				.form("file", localFile)
				.form("filename", localFile.getName())
				.form("appKey", DOC_APP_KEY)
				.send();
		String fileResponse = JSON.parseObject(httpResponse.body(), HashMap.class).get("result").toString();
		localFile.delete();
		List<ImageInfoBean> beans = JSON.parseArray(fileResponse, ImageInfoBean.class);
		return beans.get(0);
	}
}
