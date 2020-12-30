package com.jackie.sftp.controller;

import com.jackie.sftp.util.SFTPChannel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/common/")
public class TestController {
    public static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static int TIMEOUT=60000;

    /**
     * VTM双录文件上传
     *
     * @param file
     * @param request
     * @param ip
     * @param port
     * @param userName
     * @param password
     * @return
     */
    @ApiOperation(value = "工具接口" ,  notes="附件上传到sftp <br /> ip,port,usreName, password, 为sftp服务器的对应信息，base64编码后的值； filePath 文件存储路径,包含文件名称")
    @PostMapping(value = "/sftpUploadFile", produces = "application/json;charset=UTF-8")
    public String vtmSFTPUploadDobleRecord(@RequestParam("file") MultipartFile file, HttpServletRequest request, String ip,String port, String userName, String password,String filePath) {

        String ftpAddr = new String(Base64Utils.decodeFromString(ip));
        String ftpPort = new String(Base64Utils.decodeFromString(port));
        String ftpUser = new String(Base64Utils.decodeFromString(userName));
        String ftpPassword = new String(Base64Utils.decodeFromString(password));
        logger.debug("ftpAddr={}",ftpAddr);
        logger.debug("ftpPort={}",ftpPort);
        logger.debug("ftpUser={}",ftpUser);
        logger.debug("password={}",ftpPassword);

        Map<String , String> map = new HashMap<>();
        map.put("host", ftpAddr);
        map.put("port", ftpPort);
        map.put("username", ftpUser);
        map.put("password", ftpPassword);

        try {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
            SFTPChannel sftpChannel = new SFTPChannel();
            for (MultipartFile m : files) {
                ChannelSftp channelSftp = sftpChannel.getChannel(map, TIMEOUT);

                InputStream is = m.getInputStream();
                String directory = filePath.substring(0,filePath.lastIndexOf(File.separator));
                createDir(directory,channelSftp);
                channelSftp.put(is, filePath);
            }
            sftpChannel.closeChannel();

            return "双录上传成功";

        }catch (Exception e){
            logger.error(e.getMessage(), e);

            return "双录上传失败,接口异常";
        }
    }


    /**
     * 循环创建一个文件目录
     */
    public void createDir(String createpath, ChannelSftp sftp) {
        try {
            if (isDirExist(createpath,sftp)) {
                return;
            }
            String rootPath = sftp.realpath(".");
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer(rootPath+"/");

            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString(),sftp)) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }
            }
            // 返回根目录
            sftp.cd(rootPath);
        } catch (SftpException e) {
           logger.error("创建路径错误：" + createpath,e);
        }
    }



    /**
     * 判断目录是否存在
     */
    public static boolean isDirExist(String directory,ChannelSftp sftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

}
