/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain.util;
import com.totalit.smarthealth.domain.Email;
import java.util.Properties;

/**
 *
 * @author kanaz
 */
public class EmailProperties {
    public static Properties getProperties(Email setting){
                Properties mailProps = new Properties();
                mailProps.put("mail.smtp.starttls.enable", "true");
                mailProps.put("mail.smtp.auth", "true");
                mailProps.put("mail.transport.protocol", "smtp");
                mailProps.put("mail.smtp.host", setting.getHost()); // smtp.gmail.com?
                mailProps.put("mail.smtp.port", setting.getPort());
                mailProps.put("mail.smtp.debug", "true");
                return mailProps;
    }
}
