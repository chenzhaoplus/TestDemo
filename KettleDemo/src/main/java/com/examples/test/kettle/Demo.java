package com.examples.test.kettle;

import com.examples.test.util.DateUtils;
import com.examples.test.util.KettleUtils;
import com.examples.test.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.TransMeta;

import java.util.Date;

/**
 * @Author: cz
 * @Date: 2020/11/29
 * @Description:
 */
@Slf4j
public class Demo {

    public static void main(String[] args) {
        Demo demo = new Demo();
        String ktrFileName = demo.getKtr();
        demo.getKjb(ktrFileName);
    }

    private String getKtr() {
        String metaName = "trans" + DateUtils.format(new Date(), DateUtils.DATE_FOMATE_YYYYMMDDHHMMSS);
        KettleUtils.initEnviroment();
        TransDemo transDemo = new TransDemo();
        TransMeta transMeta = transDemo.generateTrans(metaName);
        try {
            String ktrFileName = metaName + ".ktr";
            XmlUtils.xmlStrToFile("D:\\linux\\kettle\\kettle8.2\\file\\" + ktrFileName, transMeta.getXML());
            return ktrFileName;
        } catch (KettleException e) {
            log.error("[kettle转换脚本获取xml失败], e = {}", e.getMessage());
            return null;
        }
    }

    private void getKjb(String ktrFileName) {
        String metaName = "job" + DateUtils.format(new Date(), DateUtils.DATE_FOMATE_YYYYMMDDHHMMSS);
        KettleUtils.initEnviroment();
        JobDemo jobDemo = new JobDemo();
        JobMeta jobMeta = jobDemo.generateJob(metaName, KettleUtils.KETTLE_ENTRY_CURRENT_DIR + "/" + ktrFileName);
        XmlUtils.xmlStrToFile("D:\\linux\\kettle\\kettle8.2\\file\\" + metaName + ".kjb", jobMeta.getXML());
    }

}
