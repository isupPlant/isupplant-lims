package com.supcon.mes.module_lims.model.bean;
import com.google.gson.JsonObject;
import com.supcon.common.com_http.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghaidong on 2020/7/20
 * Email:wanghaidong1@supcon.com
 */
public class LimSubmitEntity extends BaseEntity {
    public JsonObject workFlowVar;
    public String operateType;
    public String deploymentId;
    public String taskDescription;
    public String activityName;
    public String pendingId;
    public JsonObject dgList=new JsonObject();
    public JsonObject dgDeletedIds=new JsonObject();
    public List uploadFileFormMap=new ArrayList();
    public String ids2del="";
    public String viewCode;
}
