package com.hxlc.backstageapp.controller;

import com.hxlc.backstageapp.common.SysObject;
import com.hxlc.backstageapp.mapper.ProjectMapper;
import com.hxlc.backstageapp.pojo.Media;
import com.hxlc.backstageapp.pojo.Project;
import com.hxlc.backstageapp.service.ProjectService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;

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
        return SysObject.ok(projectService.getRecommendPro());
    }

    @RequestMapping("/queryRecommendPro")
    public SysObject queryRecommendPro() {
        return SysObject.ok(projectService.queryRecommendPro());
    }

    @RequestMapping("/addProjectRecomm")
    public SysObject addProjectRecomm(Integer proId) {
        Integer row = projectService.addProjectRecomm(proId);
        if (row == 1) {
            return new SysObject(200, "添加成功!", null);
        }
        return new SysObject(201, "添加失败!", null);
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
        if (row == 1) {
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
        if (rows == 1) {
            return new SysObject(200, "删除成功!", null);
        } else {
            return new SysObject(201, "删除失败!", null);
        }
    }

    @RequestMapping(value = "/addProject", method = RequestMethod.POST)
    public SysObject addProject(Project project, @RequestParam(value = "spjs") MultipartFile spjs,
                                @RequestParam(value = "xswd") MultipartFile xswd,
                                @RequestParam(value = "hxt") MultipartFile hxt,
                                @RequestParam(value = "xgt") MultipartFile xgt,
                                @RequestParam(value = "other") MultipartFile other,
                                HttpServletRequest request) {

        try {
            projectService.addProject(project,spjs,xswd,hxt,xgt,other);
            return new SysObject(200,"发布成功!",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SysObject(200,"发布失败!",null);
    }
}
