package com.examples.test.kettle;

import com.examples.test.util.DateUtils;
import com.examples.test.util.KettleUtils;
import com.examples.test.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.job.JobHopMeta;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.special.JobEntrySpecial;
import org.pentaho.di.job.entries.success.JobEntrySuccess;
import org.pentaho.di.job.entries.trans.JobEntryTrans;
import org.pentaho.di.job.entry.JobEntryCopy;

import java.util.Date;

/**
 * @Author: cz
 * @Date: 2020/11/13
 * @Description:
 */
@Slf4j
public class JobDemo {

    public static void main(String[] args) {
        String metaName = "job" + DateUtils.format(new Date(), DateUtils.DATE_FOMATE_YYYYMMDDHHMMSS);
        KettleUtils.initEnviroment();
        JobDemo jobDemo = new JobDemo();
        JobMeta jobMeta = jobDemo.generateJob(metaName, KettleUtils.KETTLE_ENTRY_CURRENT_DIR + "/trans20201116104835.ktr");
        XmlUtils.xmlStrToFile("D:\\linux\\kettle\\kettle8.2\\file\\" + metaName + ".kjb", jobMeta.getXML());
    }

    /**
     * 生成一个作业
     *
     * @param metaName
     * @return
     */
    public JobMeta generateJob(String metaName, String transFileName) {
        JobMeta jobMeta = new JobMeta();
        jobMeta.setName(metaName);
        JobEntryCopy startEntry = initJobEntryStart(jobMeta, "START", 100, 100);
        JobEntryCopy transEntry = initJobEntryTrans(jobMeta, "转换", transFileName, 200, 100);
        jobMeta.addJobHop(new JobHopMeta(startEntry, transEntry));
        return jobMeta;
    }

    /**
     * 初始化一个作业开始入口
     *
     * @param jobMeta
     * @param name
     * @return
     */
    private JobEntryCopy initJobEntryStart(JobMeta jobMeta, String name, int x, int y) {
        JobEntrySpecial start = new JobEntrySpecial();
        start.setName(name);
        start.setStart(true);
        start.setSchedulerType(JobEntrySpecial.INTERVAL);
        start.setIntervalMinutes(0);
        start.setIntervalSeconds(3);
        JobEntryCopy startEntry = KettleUtils.initJobEntryCopy(start, x, y);
        jobMeta.addJobEntry(startEntry);
        return startEntry;
    }

    /**
     * 初始化一个转换开始入口
     *
     * @param jobMeta
     * @param name
     * @return
     */
    private JobEntryCopy initJobEntryTrans(JobMeta jobMeta, String name, String fileName, int x, int y) {
        JobEntryTrans trans = new JobEntryTrans();
        trans.setName(name);
        trans.setFileName(fileName);
        JobEntryCopy transEntry = KettleUtils.initJobEntryCopy(trans, x, y);
        jobMeta.addJobEntry(transEntry);
        return transEntry;
    }

    /**
     * This method generates a job definition from scratch.
     * <p>
     * It demonstrates the following:
     * <p>
     * - Creating a new job
     * - Creating and connecting job entries
     *
     * @return the generated job definition
     */
    public JobMeta generateJob2() {
        try {
            System.out.println("Generating a job definition");
            // create empty transformation definition
            JobMeta jobMeta = new JobMeta();
            jobMeta.setName("Generated Demo Job");
            // ------------------------------------------------------------------------------------
            // Create start entry and put it into the job
            // ------------------------------------------------------------------------------------
            System.out.println("- Adding Start Entry");

            // Create and configure start entry
            JobEntrySpecial start = new JobEntrySpecial();
            start.setName("START");
            start.setStart(true);

            // wrap into JobEntryCopy object, which holds generic job entry information
            JobEntryCopy startEntry = new JobEntryCopy(start);

            // place it on Spoon canvas properly
            startEntry.setDrawn(true);
            startEntry.setLocation(100, 100);

            jobMeta.addJobEntry(startEntry);

            // ------------------------------------------------------------------------------------
            // Create "write to log" entry and put it into the job
            // ------------------------------------------------------------------------------------
            System.out.println("- Adding Write To Log Entry");

            // Create and configure entry
//            JobEntryWriteToLog writeToLog = new JobEntryWriteToLog();
//            writeToLog.setName("Output PDI Stats");
//            writeToLog.setLogLevel(LogLevel.MINIMAL);
//            writeToLog.setLogSubject("Logging PDI Build Information:");
//            writeToLog.setLogMessage("Version: ${Internal.Kettle.Version}\n" +
//                    "Build Date: ${Internal.Kettle.Build.Date}");

            // wrap into JobEntryCopy object, which holds generic job entry information
//            JobEntryCopy writeToLogEntry = new JobEntryCopy(writeToLog);

            // place it on Spoon canvas properly
//            writeToLogEntry.setDrawn(true);
//            writeToLogEntry.setLocation(200, 100);

//            jobMeta.addJobEntry(writeToLogEntry);

            // connect start entry to logging entry using simple hop
//            jobMeta.addJobHop(new JobHopMeta(startEntry, writeToLogEntry));

            // ------------------------------------------------------------------------------------
            // Create "success" entry and put it into the job
            // ------------------------------------------------------------------------------------
            System.out.println("- Adding Success Entry");

            // crate and configure entry
            JobEntrySuccess success = new JobEntrySuccess();
            success.setName("Success");

            // wrap into JobEntryCopy object, which holds generic job entry information
            JobEntryCopy successEntry = new JobEntryCopy(success);

            // place it on Spoon canvas properly
            successEntry.setDrawn(true);
            successEntry.setLocation(400, 100);

            jobMeta.addJobEntry(successEntry);

            // connect logging entry to success entry on TRUE evaluation
//            JobHopMeta greenHop = new JobHopMeta(writeToLogEntry, successEntry);
//            greenHop.setEvaluation(true);
//            jobMeta.addJobHop(greenHop);
            jobMeta.addJobHop(new JobHopMeta(startEntry, successEntry));

            // ------------------------------------------------------------------------------------
            // Create "abort" entry and put it into the job
            // ------------------------------------------------------------------------------------
//            System.out.println("- Adding Abort Entry");

            // crate and configure entry
//            JobEntryAbort abort = new JobEntryAbort();
//            abort.setName("Abort Job");

            // wrap into JobEntryCopy object, which holds generic job entry information
//            JobEntryCopy abortEntry = new JobEntryCopy(abort);

            // place it on Spoon canvas properly
//            abortEntry.setDrawn(true);
//            abortEntry.setLocation(400, 300);

//            jobMeta.addJobEntry(abortEntry);

            // connect logging entry to abort entry on TRUE evaluation
//            JobHopMeta redHop = new JobHopMeta(writeToLogEntry, abortEntry);
//            redHop.setEvaluation(true);
//            jobMeta.addJobHop(redHop);

            return jobMeta;
        } catch (Exception e) {
            // something went wrong, just log and return
            e.printStackTrace();
            return null;
        }
    }

}
