package com.jackie.sftp.util;


import java.util.Map;
import java.util.Properties;

import com.jcraft.jsch.*;

public class SFTPChannel {
    Session session = null;
    Channel channel = null;


    public ChannelSftp getChannel(Map<String, String> sftpDetails, int timeout) throws JSchException {

        String ftpHost = sftpDetails.get("host");
        String port = sftpDetails.get("port");
        String ftpUserName = sftpDetails.get("username");
        String ftpPassword = sftpDetails.get("password");

        int ftpPort = 22;
        if (port != null && !port.equals("")) {
            ftpPort = Integer.valueOf(port);
        }

        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
        System.out.println("Session created.");
        if (ftpPassword != null) {
            session.setPassword(ftpPassword); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        //设置代理
        //ProxyHTTP proxyhttp = new  ProxyHTTP ("10.176.65.180",808);
        //session.setProxy(proxyhttp );

        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        System.out.println("Session connected.");

        System.out.println("Opening Channel.");
        channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        System.out.println("Connected successfully to ftpHost = " + ftpHost + ",as ftpUserName = " + ftpUserName
                + ", returning: " + channel);
        return (ChannelSftp) channel;
    }

    public void closeChannel() throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
}
