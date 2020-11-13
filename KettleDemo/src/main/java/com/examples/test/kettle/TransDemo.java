package com.examples.test.kettle;

import com.examples.test.kettle.entity.Connection;
import com.examples.test.util.DateUtils;
import com.examples.test.util.JaxbUtil;
import com.examples.test.util.KettleUtils;
import com.examples.test.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author chenz
 */
@Slf4j
public class TransDemo {
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            String transMetaName = "trans" + DateUtils.format(new Date(), DateUtils.DATE_FOMATE_YYYYMMDDHHMMSS);
            KettleEnvironment.init();
            TransDemo transDemo = new TransDemo();
            TransMeta transMeta = transDemo.generateTrans(transMetaName);
            XmlUtils.xmlStrToFile("D:\\linux\\kettle\\kettle8.2\\file\\" + transMetaName +".ktr", transMeta.getXML());
        } catch (Exception e) {
            log.error("[kettle脚本生成失败], e = {}", e.getMessage());
            return;
        }
    }

    /**
     * 生成一个转化,把一个数据库中的数据转移到另一个数据库中,只有两个步骤,第一个是表输入,第二个是表插入与更新操作
     * @return
     * @throws KettleXMLException
     */
    private TransMeta generateTrans(String transMetaName) throws KettleXMLException{
        log.info("************start to generate my own transformation***********");
        Set<String> databasesXML = initDbConnection();
        TransMeta transMeta = KettleUtils.initTransMeta(transMetaName, databasesXML);
        DatabaseMeta dbMeta = transMeta.findDatabase("dev-mysql-172.16.4.83");
        StepMeta tableInputStep = initTableInputStep(transMeta, dbMeta);
        StepMeta insertUpdateStep = initInsertUpdateStep(transMeta, dbMeta);
        //添加hop把两个步骤关联起来
        transMeta.addTransHop(new TransHopMeta(tableInputStep, insertUpdateStep));
        log.info("***********the end************");
        return transMeta;
    }

    private Set<String> initDbConnection(){
        Set<String> databasesXML = new HashSet<>();
        Connection connection = new Connection();
        connection.setCommit("0");
        connection.setName("dev-mysql-172.16.4.83");
        connection.setServer("172.16.4.83");
        connection.setType("MYSQL");
        connection.setAccess("Native");
        connection.setDatabase("ibmp");
        connection.setPort("3306");
        connection.setUsername("ibmp_test");
        String encryPassword = KettleUtils.encryPassword("XZdfjXIEsrumGrfFTmfltbtAuQCECUdl");
        connection.setPassword(encryPassword);
        try {
            String xml = JaxbUtil.convertToXml(connection);
            databasesXML.add(xml);
        } catch (Exception e) {
            log.error("[数据源链接初始失败], e = {}", e.getMessage());
        }
        return databasesXML;
    }

    /**
     * 第一个表输入步骤(TableInputMeta)
     * @param transMeta
     */
    private StepMeta initTableInputStep(TransMeta transMeta, DatabaseMeta dbMeta){
        TableInputMeta tableInput = KettleUtils.initTableInputMeta(dbMeta, "SELECT * FROM control_task_copy1");
        String tableInputPluginId = KettleUtils.getPluginId(StepPluginType.class, tableInput);
        StepMeta tableInputStep = KettleUtils.initStepMeta(tableInputPluginId, "table_input", tableInput, 100, 100);
        transMeta.addStep(tableInputStep);
        return tableInputStep;
    }

    /**
     * 第二个步骤插入与更新
     * @param transMeta
     * @param dbMeta
     * @return
     */
    private StepMeta initInsertUpdateStep(TransMeta transMeta, DatabaseMeta dbMeta) {
        InsertUpdateMeta insertUpdateMeta = KettleUtils.initInsertUpdateMeta(dbMeta, "control_task_copy2");
        //设置用来查询的关键字
        insertUpdateMeta.setKeyLookup(new String[]{"ID"});
        insertUpdateMeta.setKeyStream(new String[]{"ID"});
        insertUpdateMeta.setKeyStream2(new String[]{""});//一定要加上
        insertUpdateMeta.setKeyCondition(new String[]{"="});
        //设置要更新的字段
        String[] updatelookup = {"ID", "task_name", "task_type", "age_start", "age_end", "similarity", "plate_no"};
        String[] updateStream = {"ID", "task_name", "task_type", "age_start", "age_end", "similarity", "plate_no"};
        Boolean[] updateOrNot = {false, true, true, true, true, true, true};
        insertUpdateMeta.setUpdateLookup(updatelookup);
        insertUpdateMeta.setUpdateStream(updateStream);
        insertUpdateMeta.setUpdate(updateOrNot);
//        String[] lookup = insertUpdateMeta.getUpdateLookup();
        //添加步骤到转换中
        String insertUpdateMetaPluginId = KettleUtils.getPluginId(StepPluginType.class, insertUpdateMeta);
        StepMeta insertUpdateStep = KettleUtils.initStepMeta(insertUpdateMetaPluginId, "insert_update", insertUpdateMeta, 250, 100);
        transMeta.addStep(insertUpdateStep);
        return insertUpdateStep;
    }

}