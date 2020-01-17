/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.jasper;

import com.totalit.smarthealth.domain.Sale;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 *
 * @author roy
 */
@Service
public class ReportService {

    public ByteArrayResource generate(Sale sale, InputStream image) throws IOException, JRException {
        ClassPathResource reportResource = new ClassPathResource("jasper/SaleInvoice.jrxml");
        InputStream targetStream = reportResource.getInputStream();
        // Get your data source
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(sale.getSaleItems());

        // Add parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Image", image);
        parameters.put("ds", jrBeanCollectionDataSource);
        JasperPrint jasperPrint = JasperFillManager.fillReport(targetStream, parameters, jrBeanCollectionDataSource);
        
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        byte[] reportContent = outputStream.toByteArray();
        return new ByteArrayResource(reportContent);
    }
}
