package com.examples.test.util;

import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.PluginTypeInterface;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;

import java.util.Set;

/**
 * @Author: cz
 * @Date: 2020/11/13
 * @Description:
 */
@Slf4j
public class KettleUtils {

    /**registry是给每个步骤生成一个标识Id用*/
    private static ThreadLocal<PluginRegistry> registryThreadLocal = new ThreadLocal<PluginRegistry>(){
        @Override
        protected PluginRegistry initialValue() {
            return PluginRegistry.getInstance();
        }
    };

    /**
     * 获取插件id
     * @param pluginType
     * @param pluginClass
     * @return
     */
    public static String getPluginId(Class<? extends PluginTypeInterface> pluginType, Object pluginClass){
        return registryThreadLocal.get().getPluginId(pluginType, pluginClass);
    }

    /**
     * 初始化转换元数据
     * @param transMetaName
     * @param databasesXML
     * @return
     * @throws KettleXMLException
     */
    public static TransMeta initTransMeta(String transMetaName, Set<String> databasesXML) {
        TransMeta transMeta = new TransMeta();
        //设置转化的名称
        transMeta.setName(transMetaName);
        //添加转换的数据库连接
        try {
            for(String p:databasesXML){
                DatabaseMeta databaseMeta = new DatabaseMeta(p);
                transMeta.addDatabase(databaseMeta);
            }
        } catch (KettleXMLException e) {
            log.error("[转换元数据添加数据源失败], e = {}", e.getMessage());
        }
        return transMeta;
    }

    /**
     * 初始化表输入元数据
     * @param dbMeta
     * @param sql
     * @return
     */
    public static TableInputMeta initTableInputMeta(DatabaseMeta dbMeta, String sql){
        TableInputMeta tableInput = new TableInputMeta();
        tableInput.setDatabaseMeta(dbMeta);
        tableInput.setSQL(sql);
        return tableInput;
    }

    /**
     * 初始化步骤元数据
     * @param pluginId
     * @param stepname
     * @param stepMetaInterface
     * @param locationX
     * @param locationY
     * @return
     */
    public static StepMeta initStepMeta(String pluginId, String stepname, StepMetaInterface stepMetaInterface,int locationX,int locationY){
        StepMeta stepMeta = new StepMeta(pluginId,stepname,stepMetaInterface);
        //给步骤添加在spoon工具中的显示位置
        stepMeta.setDraw(true);
        stepMeta.setLocation(locationX, locationY);
        return stepMeta;
    }

    /**
     * 初始化插入/更新元数据
     * @param dbMeta
     * @param tableName
     * @return
     */
    public static InsertUpdateMeta initInsertUpdateMeta(DatabaseMeta dbMeta, String tableName){
        InsertUpdateMeta insertUpdateMeta = new InsertUpdateMeta();
        insertUpdateMeta.setDatabaseMeta(dbMeta);
        insertUpdateMeta.setTableName(tableName);
        return insertUpdateMeta;
    }

    public static String encryPassword(String s){
        return Encr.encryptPasswordIfNotUsingVariables(s);
    }

}
