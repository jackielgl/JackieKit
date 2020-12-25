package com.jackie.ftp.controller;

import com.jackie.ftp.util.FtpUpload;
import com.jackie.ftp.util.PropertiesUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.function.ServerResponse;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("/ftp")
public class FtpController {
    //多文件上传
    @RequestMapping(value = "/dsfps", produces = "application/json;charset=UTF-8")
    public ServerResponse dsfps(@RequestParam("file") MultipartFile file, HttpServletRequest request, Integer proBasicInforId, Integer userid, Integer deptid, Integer id, Integer psjg, String pszt, String pslx, String pssj, String psdd, String psyj) {
        long ids = new Date().getTime();//时间戳.后缀 的文件
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filename = null;
        for (MultipartFile m : files
        ) {
            FtpUpload.upload(m, request, PropertiesUtil.getValue("ftp.server"),
                    Integer.parseInt(PropertiesUtil.getValue("ftp.port")),
                    PropertiesUtil.getValue("ftp.userName"),
                    PropertiesUtil.getValue("ftp.userPassword"), ids);
            filename = ids + "-" + m.getOriginalFilename() + "," + filename;
        }
        System.out.println(filename);
        //return proDemandProcessService.dsfps(proBasicInforId, userid, deptid, id, psjg, pszt, pslx, pssj, psdd, psyj, filename);
        return null;
    }
    //多文件上传
    @RequestMapping(value = "/uploadFiles", produces = "application/json;charset=UTF-8")
    public String uploadFiles(@RequestParam("file") MultipartFile file, HttpServletRequest request, String id) {
        long ids = new Date().getTime();//时间戳.后缀 的文件
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filename = null;
        for (MultipartFile m : files
        ) {
            FtpUpload.upload(m, request, PropertiesUtil.getValue("ftp.server"),
                    Integer.parseInt(PropertiesUtil.getValue("ftp.port")),
                    PropertiesUtil.getValue("ftp.userName"),
                    PropertiesUtil.getValue("ftp.userPassword"), ids);
            filename = ids + "-" + m.getOriginalFilename() + "," + filename;
            System.out.println("OriginalFilename="+m.getOriginalFilename());
            System.out.println("size="+m.getSize());
            System.out.println("name="+m.getName());
            System.out.println("ContentType="+m.getContentType());
        }
        System.out.println(filename);
        System.out.println(id);
        System.out.println("-------------");
        return null;
    }
    /**
     * 文件的下载
     * @param filename
     * @return
     * @throws Exception
     */
    @RequestMapping("/downFiles")
    public  ServerResponse  downFiles(String filename) throws Exception {
        String[] split = filename.split(",");
        for (String s:split) {
            System.out.println(s);
            if(s!=null){
                FtpUpload.downFileByte("uploadFiles",s,
                        PropertiesUtil.getValue("ftp.server"),
                        Integer.parseInt(PropertiesUtil.getValue("ftp.port")),
                        PropertiesUtil.getValue("ftp.userName"),
                        PropertiesUtil.getValue("ftp.userPassword")
                );
            }
        }


        //return  ServerResponse.createBySuccess();
        return null;
    }

}
