package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.service.ProjectService;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/getAllProject")
    public SysObject getAllProject() {
        List<Project> list = projectService.findProjectList();
        return new SysObject(list);
    }

    @RequestMapping("/getProjectByName")
    public SysObject getProjectByName(String name) {
        List<Project> list = projectService.findProjectByName(name);
        return new SysObject(list);
    }

    /**
     * 根据项目ID得到楼盘解说与销售问答PDF文件
     *
     * @param projectId
     * @return
     */
    @RequestMapping("/getCapAndQueByProId")
    public SysObject getCapAndQueByProId(Integer projectId) {
        List<Media> list = projectService.getCapAndQueByProId(projectId);
        return SysObject.ok(list);
    }

    /**
     * 根据项目ID得到所有与项目相关的资料
     *
     * @param projectId
     * @return
     */
    @RequestMapping("/getProMediaByProId")
    public SysObject getProMediaByProId(Integer projectId) throws IOException {
        List<Media> list = projectService.getProMediaByProId(projectId);
//        File file = new File("D:\\" ,list.get(1).getName() + "." + list.get(1).getFormat());
//        file.createNewFile();
//        FileOutputStream fis = new FileOutputStream(file);
//        fis.write(list.get(1).getContent());
//        fis.close();
        return SysObject.ok(list);
    }

    /**
     * 首页推荐
     *
     * @return
     */
    @RequestMapping("/getRecommendPro")
    public SysObject getRecommendPro() {
//        return SysObject.ok(projectService.getRecommendPro());
        return SysObject.ok(projectService.getRecommendImg());
    }

    @RequestMapping("/getRecommendImg")
    public SysObject getRecommendImg(){
//        return SysObject.ok(projectService.getRecommendImg());
        return SysObject.ok(projectService.getRecommendPro());
    }

    @RequestMapping("/queryRecommendPro")
    public SysObject queryRecommendPro() {
        return SysObject.ok(projectService.queryRecommendPro());
    }

    @RequestMapping("/addProjectRecomm")
    public SysObject addProjectRecomm(Integer proId) {
        try {
            Integer row = projectService.addProjectRecomm(proId);
            if (row > 0) {
                return new SysObject(200, "添加成功!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SysObject(201, "添加失败!", null);
    }

    @RequestMapping("/deleteRecomById")
    public SysObject deleteRecomById(Integer recommId,Integer index){
        try {
            Integer rows = projectService.deleteRecomById(recommId,index);
            if (rows > 0){
                return  SysObject.build(200,"删除成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  SysObject.build(201,"删除失败!");
    }

    @RequestMapping("/topRecomm")
    public SysObject topRecomm(Integer index){
        try {
            Integer row = projectService.topRecomm(index);
            if (row > 0){
                return SysObject.build(200,"置顶成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SysObject.build(201,"服务器内部错误!");
    }

    @RequestMapping("/upRecomm")
    public SysObject upRecomm(Integer index){
        try {
            Integer row = projectService.upRecomm(index);
            if (row > 0){
                return SysObject.build(200,"上移成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SysObject.build(201,"服务器内部错误!");
    }

    @RequestMapping("/downRecomm")
    public SysObject downRecomm(Integer index){
        try {
            Integer row = projectService.downRecomm(index);
            if (row > 0){
                return SysObject.build(200,"下移成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SysObject.build(201,"服务器内部错误!");
    }

    @RequestMapping("/queryUnRecommPro")
    public  SysObject queryUnRecommPro(){
        return SysObject.ok(projectService.queryUnRecommPro());
    }

    @RequestMapping("/addProRecomAD")
    public SysObject addProRecomAD(Integer gid){
        try {
            Integer row = projectService.addProRecomAD(gid);
            if (row > 0){
                return SysObject.build(200,"添加成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SysObject.build(500,"服务器内部错误!");
    }

    @RequestMapping("/editProAD")
    public SysObject editProAD(Integer currentIndex,Integer repalceIndex){
        try {
            Integer row = projectService.editProAD(currentIndex,repalceIndex);
            if (row > 0){
                return SysObject.build(200,"修改成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SysObject.build(201,"服务器内部错误!");
    }


    //项目查询
    @RequestMapping("/queryPro")
    public SysObject queryPro(String projectName) {
        return SysObject.ok(projectService.findProjectByProjectName(projectName));
    }

    /**
     * 修改项目信息
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "/editProjectInfo", method = RequestMethod.POST)
    public SysObject editProjectInfo(Project project) {
        Integer row = projectService.editProjectInfo(project);
        if (row > 0) {
            return new SysObject(200, "修改成功!", null);
        }
        return new SysObject(201, "修改失败!", null);
    }

    /**
     * 根据ID删除项目
     *
     * @param proId
     * @return
     */
    @RequestMapping("/deleteProById")
    public SysObject deleteProById(Integer proId) {
        Integer rows = projectService.deleteProById(proId);
        if (rows > 0) {
            return new SysObject(200, "删除成功!", null);
        } else {
            return new SysObject(201, "删除失败!", null);
        }
    }

    @RequestMapping(value = "/addProject", method = RequestMethod.POST)
    public SysObject addProject(Project project, @RequestParam(value = "xmtb") MultipartFile xmtb,
                                @RequestParam(value = "spjs") MultipartFile spjs,
                                @RequestParam(value = "xswd") MultipartFile xswd,
                                @RequestParam(value = "hxt") MultipartFile hxt,
                                @RequestParam(value = "xgt") MultipartFile xgt,
                                @RequestParam(value = "other",required = false) MultipartFile other,
                                HttpServletRequest request) {

        try {
            projectService.addProject(project,xmtb,spjs,xswd,hxt,xgt,other);
            return new SysObject(200,"发布成功!",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SysObject(200,"发布失败!",null);
    }

    @RequestMapping("/existPro")
    public SysObject existPro(String pro_name){
        List<Project> list = projectService.existPro(pro_name);
        if(list==null || list.size()==0){
            return new SysObject(200,"项目名可以使用!",null);
        }
        return new SysObject(201,"项目名已被占用!",null);
    }

    /**
     * 下载项目资料
     * @param proId
     * @return
     */
    @RequestMapping("/downloadProData")
    public SysObject downloadProData(HttpServletResponse response,Integer proId){
        return projectService.downloadProData(response,proId);
    }

    /**
     * 根据项目ID查找项目信息
     * @param proId
     * @return
     */
    @RequestMapping("/getProjectByID")
    public SysObject getProjectByID(Integer proId){
        Project project = projectService.getProjectByID(proId);
        return SysObject.ok(project);
    }
}
