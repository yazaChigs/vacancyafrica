/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.util;

import java.nio.charset.Charset;
import java.util.Date;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

/**
 *
 * @author roy
 */
public class RestTemplateUtil {
    public static HttpHeaders addBasicAuthentication(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }
}
