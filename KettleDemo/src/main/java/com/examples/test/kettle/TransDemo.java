package com.examples.test.kettle;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.examples.test.kettle.entity.Connection;
import com.examples.test.util.DateUtils;
import com.examples.test.util.JaxbUtil;
import org.apache.commons.io.FileUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;

public class TransDemo {
    public static TransDemo transDemo;

    /**
     * 两个库中的表名
     */
    public static String bjdt_tablename = "control_task_copy1";
    public static String kettle_tablename = "control_task_copy2";

    /**
     * 数据库连接信息,适用于DatabaseMeta其中 一个构造器DatabaseMeta(String xml)
     */
    public static final Set<String> databasesXML = new HashSet<>();
    static {
        Connection connection = new Connection();
        connection.setCommit("0");
        connection.setName("dev-mysql-172.16.4.83");
        connection.setServer("172.16.4.83");
        connection.setType("MYSQL");
        connection.setAccess("Native");
        connection.setDatabase("ibmp");
        connection.setPort("3306");
        connection.setUsername("ibmp_test");
        connection.setPassword("Encrypted 585a64666a5849457372756d47726644eaf5c9a41ec586a5be288d55fda7abd6");
        try {
            String xml = JaxbUtil.convertToXml(connection);
            databasesXML.add(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            KettleEnvironment.init();
            transDemo = new TransDemo();
            TransMeta transMeta = transDemo.generateMyOwnTrans();
            String transXml = transMeta.getXML();
            //System.out.println("transXml:"+transXml);
            String transName = "D:\\trans"+ DateUtils.format(new Date(), DateUtils.DATE_FOMATE_YYYYMMDDHHMMSS) +".ktr";
            File file = new File(transName);
            FileUtils.writeStringToFile(file, transXml, "UTF-8");

            //	System.out.println(databasesXML.length+"\n"+databasesXML[0]+"\n"+databasesXML[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 生成一个转化,把一个数据库中的数据转移到另一个数据库中,只有两个步骤,第一个是表输入,第二个是表插入与更新操作
     * @return
     * @throws KettleXMLException
     */
    public TransMeta generateMyOwnTrans() throws KettleXMLException{
        System.out.println("************start to generate my own transformation***********");
        TransMeta transMeta = new TransMeta();
        //设置转化的名称
        transMeta.setName("insert_update");
        //添加转换的数据库连接
        for(String p:databasesXML){
            DatabaseMeta databaseMeta = new DatabaseMeta(p);
            transMeta.addDatabase(databaseMeta);
        }
//        for (int i=0;i<databasesXML.length;i++){
//            DatabaseMeta databaseMeta = new DatabaseMeta(databasesXML[i]);
//            transMeta.addDatabase(databaseMeta);
//        }

        //registry是给每个步骤生成一个标识Id用
        PluginRegistry registry = PluginRegistry.getInstance();

        //******************************************************************

        //第一个表输入步骤(TableInputMeta)
        TableInputMeta tableInput = new TableInputMeta();
        String tableInputPluginId = registry.getPluginId(StepPluginType.class, tableInput);
        //给表输入添加一个DatabaseMeta连接数据库
        DatabaseMeta database_bjdt = transMeta.findDatabase("dev-mysql-172.16.4.83");
        tableInput.setDatabaseMeta(database_bjdt);
        String select_sql = "SELECT * FROM "+bjdt_tablename;
        tableInput.setSQL(select_sql);

        //添加TableInputMeta到转换中
        StepMeta tableInputMetaStep = new StepMeta(tableInputPluginId,"table input",tableInput);

        //给步骤添加在spoon工具中的显示位置
        tableInputMetaStep.setDraw(true);
        tableInputMetaStep.setLocation(100, 100);

        transMeta.addStep(tableInputMetaStep);
        //******************************************************************

        //******************************************************************
        //第二个步骤插入与更新
        InsertUpdateMeta insertUpdateMeta = new InsertUpdateMeta();
        String insertUpdateMetaPluginId = registry.getPluginId(StepPluginType.class,insertUpdateMeta);
        //添加数据库连接
        DatabaseMeta database_kettle = transMeta.findDatabase("dev-mysql-172.16.4.83");
        insertUpdateMeta.setDatabaseMeta(database_kettle);
        //设置操作的表
        insertUpdateMeta.setTableName(kettle_tablename);

        //设置用来查询的关键字
        insertUpdateMeta.setKeyLookup(new String[]{"ID"});
        insertUpdateMeta.setKeyStream(new String[]{"ID"});
        insertUpdateMeta.setKeyStream2(new String[]{""});//一定要加上
        insertUpdateMeta.setKeyCondition(new String[]{"="});

        //设置要更新的字段
        String[] updatelookup = {"ID","task_name","task_type","age_start","age_end","similarity","plate_no"} ;
        String [] updateStream = {"ID","task_name","task_type","age_start","age_end","similarity","plate_no"};
        Boolean[] updateOrNot = {false,true,true,true,true,true,true};
        insertUpdateMeta.setUpdateLookup(updatelookup);
        insertUpdateMeta.setUpdateStream(updateStream);
        insertUpdateMeta.setUpdate(updateOrNot);
        String[] lookup = insertUpdateMeta.getUpdateLookup();
        //System.out.println("******:"+lookup[1]);
        //System.out.println("insertUpdateMetaXMl:"+insertUpdateMeta.getXML());
        //添加步骤到转换中
        StepMeta insertUpdateStep = new StepMeta(insertUpdateMetaPluginId,"insert_update",insertUpdateMeta);
        insertUpdateStep.setDraw(true);
        insertUpdateStep.setLocation(250,100);
        transMeta.addStep(insertUpdateStep);
        //******************************************************************

        //******************************************************************
        //添加hop把两个步骤关联起来
        transMeta.addTransHop(new TransHopMeta(tableInputMetaStep, insertUpdateStep));
        System.out.println("***********the end************");
        return transMeta;
    }
}