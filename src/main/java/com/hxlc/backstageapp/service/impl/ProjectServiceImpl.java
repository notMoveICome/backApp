package com.hxlc.backstageapp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.hxlc.backstageapp.mapper.AppFaceMapper;
import com.hxlc.backstageapp.mapper.CustomerMapper;
import com.hxlc.backstageapp.mapper.MediaMapper;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.pojo.AppFace;
import com.hxlc.backstageapp.pojo.Customer;
import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.service.ProjectService;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public List<Project> findProjectList() {
        List<Project> projectList = projectMapper.selectList(new EntityWrapper<Project>());
        for (int i = 0;i < projectList.size();i++){
            projectList.get(i).setDisnum(projectList.get(i).getDisnum() + 80);//分销商数加上80
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
        List<Project> projectList = projectMapper.selectList(new EntityWrapper<Project>().like("name", projectName));
        for (int i = 0;i < projectList.size();i++){
            projectList.get(i).setDisnum(projectList.get(i).getDisnum() + 80);//分销商数技术80
            Integer count = customerMapper.selectCount(new EntityWrapper<Customer>().eq("project_id", projectList.get(i).getGid()));
            projectList.get(i).setReportNum(count);
        }
        return projectList;
    }

    @Override
    public Integer deleteProById(Integer proId) {
        return projectMapper.delete(new EntityWrapper<Project>().eq("gid",proId));
    }

    @Override
    public List<Project> queryRecommendPro() {
        List<AppFace> faces = appFaceMapper.selectList(new EntityWrapper<AppFace>());
        List<Project> projects = projectMapper.selectList(new EntityWrapper<Project>());
        for (int i = 0;i < faces.size();i++){
            for (int j = 0;j < projects.size();j++){
                if (faces.get(i).getProjectId() == projects.get(j).getGid()){
                    projects.remove(j);
                }
            }
        }
        return projects;
    }

    @Override
    public Integer addProjectRecomm(Integer proId) {
        Project project = new Project();
        project.setGid(proId);
        Project pro = projectMapper.selectOne(project);
        Integer count = appFaceMapper.selectCount(new EntityWrapper<AppFace>());
        AppFace appFace = new AppFace();
        appFace.setProjectId(proId);
        appFace.setIndex(count + 1);
        appFace.setPrice(pro.getPrice() / 1000); // 待定
        appFace.setPublishTime("6月-12月");// 待定
        Integer row = appFaceMapper.insert(appFace);
        return row;
    }

    @Override
    public Integer editProjectInfo(Project project) {
        project.setDisnum(project.getDisnum() - 80);//分销商数减上80
        return projectMapper.updateById(project);
    }

    @Override
    public void addProject(Project project, MultipartFile spjs, MultipartFile xswd, MultipartFile hxt, MultipartFile xgt, MultipartFile other) {
        Date date = new Date();
        project.setBackTime(new java.sql.Date(date.getTime()));
        projectMapper.addProject(project);
        Integer gid = project.getGid();
//        System.out.println(gid);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(date);
        String round = "";
        for (int i = 0;i < 4;i++){
            round += new Random().nextInt(9);
        }
        String fileDir = project.getName() + format + "_" + round;
        File parentFile = new File(projectFile + "/" + fileDir);
        parentFile.mkdirs();
        Integer spjsId = approvalFile(spjs, parentFile.getPath(), gid);
        Integer xswdId = approvalFile(xswd, parentFile.getPath(), gid);
        approvalFile(hxt,parentFile.getPath(),gid);
        approvalFile(xgt,parentFile.getPath(),gid);
        approvalFile(other,parentFile.getPath(),gid);
        Project pro = new Project();
        pro.setGid(gid);
        pro.setCaptionId(spjsId);
        pro.setQuestionId(xswdId);
        projectMapper.updateById(pro);
    }

    @Override
    public List<Project> existPro(String pro_name) {
        return projectMapper.selectList(new EntityWrapper<Project>().eq("name",pro_name));
    }

    /**
     * 将MultipartFile文件保存到本地
     * @param filecontent
     * @param savePath
     */
    public Integer approvalFile( MultipartFile filecontent,String savePath,Integer gid){
        OutputStream os = null;
        InputStream inputStream = null;
        String fileName = null;
        int buffer = 2048;
        try {
            inputStream = filecontent.getInputStream();
            fileName = filecontent.getOriginalFilename();
            // 2、保存到临时文件
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
            // 若是zip文件就进行解压
            if (fileName.substring(fileName.lastIndexOf("."),fileName.length()).equals(".zip")){
                File zf = new File(savePath + File.separator + fileName);
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
                return 0;
            }
            // 若是rar文件就进行解压
//            if (fileName.substring(fileName.lastIndexOf("."),fileName.length()).equals(".rar")){
//                Archive a = null;
//                try {
//                    a = new Archive(new File(savePath + File.separator + fileName));
//                    if (a != null) {
//                        a.getMainHeader().print(); // 打印文件信息.
//                        FileHeader fh = a.nextFileHeader();
//                        while (fh != null) {
//                            if (fh.isDirectory()) { // 文件夹
//                                File fol = new File(savePath + File.separator + fh.getFileNameW());
//                                fol.mkdirs();
//                            } else { // 文件
//                                File out = new File(savePath + File.separator + fh.getFileNameW().trim());
//                                try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
//                                    if (!out.exists()) {
//                                        if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
//                                            out.getParentFile().mkdirs();
//                                        }
//                                        out.createNewFile();
//                                    }
//                                    FileOutputStream fos = new FileOutputStream(out);
//                                    a.extractFile(fh, fos);
//                                    fos.close();
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                            }
//                            fh = a.nextFileHeader();
//                        }
//                        a.close();
//                        System.out.println("rar压缩文件解压成功");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            Integer mediaId = addMedia(filecontent.getName(), savePath, fileName, gid);
            return mediaId;
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
        return null;
    }

    private Integer addMedia(String name, String savePath, String fileName, Integer gid) {
        String type = "";
        if ("spjs".equals(name)){
            type = "沙盘说辞";
        }else if ("xswd".equals(name)){
            type = "销售问答";
        }else if ("hxt".equals(name)){
            type = "户型图";
        }else if ("xgt".equals(name)){
            type = "效果图";
        }else {
            type = "文件";
        }
        String mediaName = fileName.substring(0,fileName.lastIndexOf("."));
        String format = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
        savePath = savePath.replaceAll("\\\\","/");
        String url = "/resources" + savePath.substring(savePath.lastIndexOf("/"),savePath.length()) + "/" + fileName;
        Media media = new Media();
        media.setName(mediaName);
        media.setFormat(format);
        media.setProjectId(gid);
        media.setType(type);
        media.setUrl(url);
        mediaMapper.addMedia(media);
        return media.getGid();
    }

}
















