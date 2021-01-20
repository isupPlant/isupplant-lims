package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.SubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_sample.model.bean.FileAnalyseListEntity;
import com.supcon.mes.module_sample.model.contract.SampleFileAnalyseContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

import static com.supcon.mes.middleware.constant.Constant.BAPQuery.BE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE_OPT_BLUR;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE_OPT_Q;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TYPE_NORMAL;

/**
 * Created by wanghaidong on 2021/1/18
 * Email:wanghaidong1@supcon.com
 * desc:
 */
public class SampleFileAnalyseListPresenter extends SampleFileAnalyseContract.Presenter {
    @Override
    public void getAnalyseList(int pageNo,Map<String, Object> queryMap) {
        Map<String,Object> queryParams=new HashMap<>();
        JSONObject customCondition=new JSONObject();
        queryParams.put("customCondition",customCondition);
        if (!queryMap.isEmpty()){
            FastQueryCondEntity fastQueryCondEntity=new FastQueryCondEntity();
            fastQueryCondEntity.viewCode="LIMSDC_5.1.0.1_analysisFile_analysisFileRef";
            fastQueryCondEntity.modelAlias="analysisFile";
            fastQueryCondEntity.subconds=new ArrayList<>();
            if (queryMap.containsKey(LimsConstant.BAPQuery.C_NAME)){
                fastQueryCondEntity.subconds.add(parseKey(LimsConstant.BAPQuery.C_NAME,queryMap.get(LimsConstant.BAPQuery.C_NAME)));
            }
            if (queryMap.containsKey(LimsConstant.BAPQuery.C_MD5)){
                fastQueryCondEntity.subconds.add(parseKey(LimsConstant.BAPQuery.C_MD5,queryMap.get(LimsConstant.BAPQuery.C_MD5)));
            }
            if (queryMap.containsKey(LimsConstant.BAPQuery.LIMS_REFED)){
                fastQueryCondEntity.subconds.add(parseKey(LimsConstant.BAPQuery.LIMS_REFED,queryMap.get(LimsConstant.BAPQuery.LIMS_REFED)));
            }
            queryParams.put("fastQueryCond",fastQueryCondEntity.toString());
        }
        queryParams.put("permissionCode","LIMSDC_5.1.0.1_analysisFile_analysisFileRef");
        queryParams.put("pageNo",pageNo);
        queryParams.put("paging",true);
        queryParams.put("pageSize",20);

        mCompositeSubscription.add(
                SampleHttpClient.getAnalyseList(queryParams)
                                .onErrorReturn(throwable -> {
                                    FileAnalyseListEntity entity=new FileAnalyseListEntity();
                                    entity.msg= ErrorMsgHelper.msgParse(throwable.getMessage());
                                    entity.success=false;
                                    return entity;
                                })
                                .subscribe(new Consumer<FileAnalyseListEntity>() {
                                    @Override
                                    public void accept(FileAnalyseListEntity entity) throws Exception {
                                        if (entity.success){
                                            getView().getAnalyseListSuccess(entity);
                                        }else {
                                            getView().getAnalyseListFailed(entity.msg);
                                        }
                                    }
                                })
        );
    }
    private  BaseSubcondEntity parseKey(String key, Object value) {
        SubcondEntity subcondEntity = null;
        switch (key) {
            case LimsConstant.BAPQuery.C_NAME:
            case LimsConstant.BAPQuery.C_MD5:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case LimsConstant.BAPQuery.LIMS_REFED:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = Constant.BAPQuery.BOOLEAN;
                subcondEntity.operator = BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
        }
        return subcondEntity;
    }

}
