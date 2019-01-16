package com.hxlc.backstageapp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.mapper.AppFaceMapper;
import com.hxlc.backstageapp.mapper.CustomerMapper;
import com.hxlc.backstageapp.mapper.MediaMapper;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.pojo.AppFace;
import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.service.ProjectService;
import com.hxlc.backstageapp.util.FileUtil;
import com.hxlc.backstageapp.util.WordUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private MediaMapper mediaMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private AppFaceMapper appFaceMapper;
    @Value("${fileDir.projectFile}")
    private String projectFile;
    @Value("${fileDir.tempFile}")
    private String tempFile;

    @Override
    public List<Project> findProjectList() {
        List<Project> projectList = projectMapper.getAllProject();
        for (int i = 0;i < projectList.size();i++){
            Integer disnum = projectList.get(i).getDisnum() == null ? 0 : projectList.get(i).getDisnum();
            projectList.get(i).setDisnum(disnum + 80);//分销商数加上80
            Integer count = customerMapper.selectCount(new EntityWrapper<Customer>().eq("project_id", projectList.get(i).getGid()));
            projectList.get(i).setReportNum(count);
        }
        return projectList;
    }

    @Override
    public List<Project> findProjectByName(String name) {
        return projectMapper.selectList(new EntityWrapper<Project>().like("name",name));
    }

    @Override
    public List<Media> getCapAndQueByProId(Integer projectId) {
        return projectMapper.findCapAndQueByProId(projectId);
    }

    @Override
    public List<Media> getProMediaByProId(Integer projectId) {
        return mediaMapper.selectList(new EntityWrapper<Media>().eq("project_id",projectId));
    }

    @Override
    public List<Map> getRecommendPro() {
        return projectMapper.findRecommendPro();
    }

    @Override
    public List<Project> findProjectByProjectName(String projectName) {
        List<Project> projectList = projectMapper.findProjectByProjectName(projectName);
        for (int i = 0;i < projectList.size();i++){
            Integer disnum = projectList.get(i).getDisnum() == null ? 0 : projectList.get(i).getDisnum();
            projectList.get(i).setDisnum(disnum + 80);//分销商数技术80
            Integer count = customerMapper.selectCount(new EntityWrapper<Customer>().eq("project_id", projectList.get(i).getGid()));
            projectList.get(i).setReportNum(count);
        }
        return projectList;
    }

    @Transactional
    @Override
    public Integer deleteProById(Integer proId) {
        return projectMapper.delete(new EntityWrapper<Project>().eq("gid",proId));
    }

    @Override
    public List<Project> queryRecommendPro() {
        List<AppFace> faces = appFaceMapper.selectList(new EntityWrapper<AppFace>());
        List<Project> projects = projectMapper.selectList(new EntityWrapper<Project>().eq("state","在售"));
        for (int i = 0;i < faces.size();i++){
            for (int j = 0;j < projects.size();j++){
                if (faces.get(i).getProjectId() == projects.get(j).getGid()){
                    projects.remove(j);
                }
            }
        }
        return projects;
    }

    @Transactional
    @Override
    public Integer addProjectRecomm(Integer proId) {
        Project project = new Project();
        project.setGid(proId);
        Project pro = projectMapper.selectOne(project);
        Integer count = appFaceMapper.selectCount(new EntityWrapper<AppFace>());
        AppFace appFace = new AppFace();
        appFace.setProjectId(proId);
        appFace.setIndex(count + 1);
        appFace.setPrice(pro.getPrice()); // 待定
        appFace.setPublishTime("6月-12月");// 待定
        Integer row = appFaceMapper.insert(appFace);
        return row;
    }

    @Transactional
    @Override
    public Integer editProjectInfo(Project project) {
        project.setDisnum(project.getDisnum() - 80);//分销商数减上80
        return projectMapper.updateById(project);
    }

    @Transactional
    @Override
    public void addProject(Project project, MultipartFile xmtb, MultipartFile spjs, MultipartFile xswd, MultipartFile hxt, MultipartFile xgt, MultipartFile other) {
        Date date = new Date();
        project.setBackTime(new java.sql.Date(date.getTime()));
        projectMapper.addProject(project);
        Integer gid = project.getGid();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(date);
        String round = "";
        for (int i = 0;i < 4;i++){
            round += new Random().nextInt(9);
        }
        // 建目录
        String fileDir = project.getName() + format + "_" + round;
        File parentFile = new File(projectFile + "/" + fileDir);
        parentFile.mkdirs();

        // 执行
        if (StringUtils.isNotBlank(xmtb.getOriginalFilename())){
            approvalFile(xmtb,parentFile.getPath(),gid);
        }
        approvalFile(spjs, parentFile.getPath(), gid);
        approvalFile(xswd, parentFile.getPath(), gid);
        approvalFile(hxt,parentFile.getPath(),gid);
        approvalFile(xgt,parentFile.getPath(),gid);
        if (StringUtils.isNotBlank(other.getOriginalFilename())){
            approvalFile(other,parentFile.getPath(),gid);
        }
    }

    @Override
    public List<Project> existPro(String pro_name) {
        return projectMapper.selectList(new EntityWrapper<Project>().eq("name",pro_name));
    }

    @Override
    public SysObject downloadProData(HttpServletResponse response, Integer proId) {
        String url = mediaMapper.selectUrlByProId(proId);
        if (StringUtils.isBlank(url)){
            return new SysObject(201,"暂无该项目资料!",null);
        }
        String u = null;
        String filePath = null;   // 目标文件路径
        try {
            String path = projectFile.substring(projectFile.lastIndexOf("/"), projectFile.length()) + "/";
            u = url.substring(path.length());
            filePath = projectFile + "/" + u.substring(0, u.indexOf("/"));
        } catch (Exception e) {
            e.printStackTrace();
            return new SysObject(201,"文件路径异常!",null);
        }
        File zipFile = new File(tempFile + "/" + System.currentTimeMillis()+".zip");    // 压缩文件路径
        ZipOutputStream zos = null;
        try {
            File f = new File(filePath);
            if (!f.exists()){
                return new SysObject(201,"该项目文件已不存在!",null);
            }
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            String baseDir = u.substring(0, u.indexOf("/") + 1);
            FileUtil.compress(f,baseDir,zos);   // 递归压缩
            zos.close();
            FileUtil.download(response,zipFile.getAbsolutePath());
            zipFile.delete();   // 刪除生成的zip文件
            return SysObject.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(zos!=null)
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return SysObject.build(201,"服务器异常!");
    }

    @Override
    public Project getProjectByID(Integer proId) {
        return projectMapper.selectProById(proId);
    }

    @Transactional
    @Override
    public Integer deleteRecomById(Integer recommId,Integer index) {
        Integer row = appFaceMapper.deleteById(recommId);
        if (row > 0){
            appFaceMapper.updateIndexUp(index);
        }
        return row;
    }

    @Transactional
    @Override
    public Integer upRecomm(Integer index) {
        AppFace appFace0 = new AppFace();
        appFace0.setIndex(0);
        appFaceMapper.update(appFace0,new EntityWrapper<AppFace>().eq("index",index));

        AppFace appFace2 = new AppFace();
        appFace2.setIndex(index);
        appFaceMapper.update(appFace2, new EntityWrapper<AppFace>().eq("index", index - 1));
        AppFace appFace1 = new AppFace();
        appFace1.setIndex(index - 1);
        Integer row = appFaceMapper.update(appFace1,new EntityWrapper<AppFace>().eq("index",0));
        return row;
    }

    @Transactional
    @Override
    public Integer downRecomm(Integer index) {
//        Integer count = appFaceMapper.selectCount(new EntityWrapper<AppFace>());
//        if (count == index){
//            return 1;
//        }
        AppFace appFace0 = new AppFace();
        appFace0.setIndex(0);
        appFaceMapper.update(appFace0,new EntityWrapper<AppFace>().eq("index",index));

        AppFace appFace2 = new AppFace();
        appFace2.setIndex(index);
        appFaceMapper.update(appFace2, new EntityWrapper<AppFace>().eq("index", index + 1));
        AppFace appFace1 = new AppFace();
        appFace1.setIndex(index + 1);
        Integer row = appFaceMapper.update(appFace1,new EntityWrapper<AppFace>().eq("index",0));
        return row;
    }

    @Transactional
    @Override
    public Integer topRecomm(Integer index) {
        AppFace appFace = new AppFace();
        appFace.setIndex(0);
        appFaceMapper.update(appFace,new EntityWrapper<AppFace>().eq("index",index));

        appFaceMapper.updateIndexDown(index);
        AppFace appFace1 = new AppFace();
        appFace1.setIndex(1);
        Integer row = appFaceMapper.update(appFace1, new EntityWrapper<AppFace>().eq("index", 0));
        return row;
    }

    @Override
    public List<Map> getRecommendImg() {
        return projectMapper.getRecommendImg();
    }

    @Transactional
    @Override
    public Integer editProAD(Integer currentIndex, Integer repalceIndex) {
        // 取出两个对象
        AppFace appFace0 = new AppFace();
        appFace0.setIndex(currentIndex);
        AppFace one0 = appFaceMapper.selectOne(appFace0);
        // 下架
        if (repalceIndex == 0){     // 表示下架,不是替换
//            AppFace appFace = new AppFace();
//            appFace.setAdIndex(null);
            Integer rs = appFaceMapper.updateAdIndex(null,one0.getGid());
            appFaceMapper.updateAdIndexUp(one0.getAdIndex());
            return rs;
        }

        AppFace appFace1 = new AppFace();
        appFace1.setIndex(repalceIndex);
        AppFace one1 = appFaceMapper.selectOne(appFace1);
        //换位置
//        AppFace appFace2 = new AppFace();
//        appFace2.setAdIndex(one0.getAdIndex());//互换
//        Integer row = appFaceMapper.update(appFace2,new EntityWrapper<AppFace>().eq("gid",one0.getGid()));
        Integer row = appFaceMapper.updateAdIndex(one1.getAdIndex(), one0.getGid());
        Integer res = 0;
        if (row > 0){
//            AppFace appFace3 = new AppFace();
//            appFace3.setAdIndex(one0.getAdIndex());
//            res = appFaceMapper.update(appFace3, new EntityWrapper<AppFace>().eq("gid", one1.getGid()));
            res = appFaceMapper.updateAdIndex(one0.getAdIndex(),one1.getGid());
        }
        return res;
    }

    @Override
    public List<Map> queryUnRecommPro() {
        return projectMapper.queryUnRecommPro();
    }

    @Transactional
    @Override
    public Integer addProRecomAD(Integer gid) {
        Integer count = appFaceMapper.selectRecomAD();
        AppFace appFace = new AppFace();
        appFace.setAdIndex(count + 1);
        appFace.setGid(gid);
        Integer row = appFaceMapper.updateById(appFace);
        return row;
    }

    /**
     * 将MultipartFile文件保存到本地
     * @param filecontent
     * @param savePath
     */
    public void approvalFile( MultipartFile filecontent,String savePath,Integer gid){
        OutputStream os = null;
        InputStream inputStream = null;
        String fileName = null;
        int buffer = 2048;
        try {
            inputStream = filecontent.getInputStream();
            fileName = filecontent.getOriginalFilename();
            // 保存到临时文件
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            os = new FileOutputStream(savePath + File.separator + fileName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            File zf = new File(savePath + File.separator + fileName);
            // doc转pdf
            if (fileName.endsWith(".doc") || fileName.endsWith("docx")){
                WordUtil.wordSaveAs(savePath + File.separator + fileName,savePath + File.separator + fileName.substring(0,fileName.lastIndexOf(".")) + ".pdf");
                addMedia(filecontent.getName(), savePath, fileName, gid);
                addMedia(filecontent.getName(), savePath, fileName.substring(0,fileName.lastIndexOf(".")) + ".pdf", gid);
                return;
            }
            // 若是zip文件就进行解压
            if (fileName.endsWith(".zip")){
                ZipFile zFile = new ZipFile(zf, "GBK");
                Enumeration emu = zFile.getEntries();
                while (emu.hasMoreElements()){
                    ZipEntry entry = (ZipEntry)emu.nextElement();
                    if(entry.isDirectory()){
                        new File(savePath  + File.separator + entry.getName()).mkdirs();
                        continue;
                    }
                    BufferedInputStream bis = new BufferedInputStream(zFile.getInputStream(entry));
                    File file = new File(savePath + File.separator + entry.getName());
                    File pf = file.getParentFile();
                    if(pf!=null&&(!pf.exists())){
                        pf.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedOutputStream bos = new BufferedOutputStream(fos,buffer);
                    int count;
                    byte[] data = new byte[buffer];
                    while((count=bis.read(data, 0, buffer))!=-1){
                        bos.write(data, 0, count);
                    }
                    bos.flush();
                    bos.close();
                    bis.close();
                    addMedia(filecontent.getName(),savePath,entry.getName(),gid);
                }
                zFile.close();
                System.out.println("zip压缩文件解压成功");
                return;
            }

            if (fileName.endsWith(".rar")){
                Archive a = null;
                try {
                    a = new Archive(zf);
                    if (a != null) {
                        a.getMainHeader().print(); // 打印文件信息.
                        FileHeader fh = a.nextFileHeader();
                        while (fh != null) {
                            // 判断是否有中文
                            String path = fh.getFileNameW().trim();
                            if (!existZH(path)){
                                path = fh.getFileNameW();
                            }
                            if (fh.isDirectory()) { // 文件夹
                                File fol = new File(savePath + File.separator + path);
                                fol.mkdirs();
                                continue;
                            } else { // 文件
                                File out = new File(savePath + File.separator + path);
                                try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
                                    if (!out.exists()) {
                                        if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
                                            out.getParentFile().mkdirs();
                                        }
                                        out.createNewFile();
                                    }
                                    FileOutputStream fos = new FileOutputStream(out);
                                    a.extractFile(fh, fos);
                                    fos.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            addMedia(filecontent.getName(),savePath,path,gid);
                            fh = a.nextFileHeader();
                        }
                        a.close();
                        System.out.println("rar压缩文件解压成功");
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            addMedia(filecontent.getName(), savePath, fileName, gid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否有中文
     * @param str
     * @return
     */
    public boolean existZH(String str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }

    private Integer addMedia(String name, String savePath, String fileName, Integer gid) {
        String type = "";
        if ("xmtb".equals(name)){
            type = "图标";
        }else if ("spjs".equals(name)){
            type = "沙盘说辞";
        }else if ("xswd".equals(name)){
            type = "销售问答";
        }else if ("hxt".equals(name)){
            type = "户型图";
        }else if ("xgt".equals(name)){
            type = "效果图";
        }else {
            type = "其他文件";
        }
        savePath = savePath.replaceAll("\\\\","/");
        fileName = fileName.replaceAll("\\\\","/");
        String mediaName = fileName.substring(0,fileName.lastIndexOf("."));
        String format = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
        String url = projectFile.substring(projectFile.lastIndexOf("/"),projectFile.length()) + savePath.substring(savePath.lastIndexOf("/"),savePath.length()) + "/" + fileName;
        Media media = new Media();
        media.setName(mediaName);
        media.setFormat(format);
        media.setProjectId(gid);
        media.setType(type);
        media.setUrl(url);
        mediaMapper.addMedia(media);
        return media.getGid();  // 返回新增ID
    }

}
















