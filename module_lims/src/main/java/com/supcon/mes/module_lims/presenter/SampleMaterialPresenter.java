package com.supcon.mes.module_lims.presenter;

import android.provider.ContactsContract;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.SampleMaterialEntity;
import com.supcon.mes.module_lims.model.contract.SampleMaterialListApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
public class SampleMaterialPresenter extends SampleMaterialListApi.Presenter {
    @Override
    public void getSampleMaterialReference(int pageNo, Map<String, Object> params,String matInfoCodeList) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(System.currentTimeMillis());
        String currentDate = simpleDateFormat.format(date);


        String viewCode = "LIMSMaterial_5.1.0.1_mATInfo_matInfoRef";
        String modelAlias = "matInfo";
        String joinInfo = "BASESET_MATERIALS,ID,LIMSM_MAT_INFOS,PRODUCT_ID";
        FastQueryCondEntity fastQuery;

        Map<String, Object> codeParam = new HashMap();
        //从外层传进的集合中取出 检索条件 CODE | NAME | BATCH_CODE | TABLE_NO
        if (params.containsKey(Constant.BAPQuery.CODE)) {
            codeParam.put(Constant.BAPQuery.CODE, params.get(Constant.BAPQuery.CODE));
        }
        if (params.containsKey(Constant.BAPQuery.NAME)) {
            codeParam.put(Constant.BAPQuery.NAME, params.get(Constant.BAPQuery.NAME));
        }
        if (params.containsKey(Constant.BAPQuery.BATCH_CODE)){
            codeParam.put(Constant.BAPQuery.BATCH_CODE, params.get(Constant.BAPQuery.BATCH_CODE));
        }


        if (params.containsKey(Constant.BAPQuery.NAME)){
            //创建一个空的实体类
            fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
            fastQuery.modelAlias = modelAlias;
            fastQuery.viewCode = viewCode;
            //创建fastQuery.subconds内部该有的属性并且赋值
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(codeParam, joinInfo);
            fastQuery.subconds.add(joinSubcondEntity);
        }else {
            //创建一个空的实体类
            fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(codeParam);
            fastQuery.modelAlias = modelAlias;
            fastQuery.viewCode = viewCode;
        }

        Map<String, Object> map = new HashMap<>();

        if (codeParam.size()>0){
            map.put("fastQueryCond",fastQuery.toString());
        }
        Map<String, Object> customConditionMap = new HashMap<>();
        customConditionMap.put("matInfoCodeList",matInfoCodeList);
        customConditionMap.put("currentDate",currentDate);
        map.put("customCondition",customConditionMap);

        map.put("permissionCode","LIMSMaterial_5.1.0.1_mATInfo_matInfoRef");
        map.put("pageNo",pageNo);
        map.put("paging",true);
        map.put("pageSize",10);

        mCompositeSubscription.add(BaseLimsHttpClient.getSampleMaterialReference(map).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonListEntity<SampleMaterialEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonListEntity<SampleMaterialEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonListEntity<SampleMaterialEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonListEntity<SampleMaterialEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getSampleMaterialReferenceSuccess(entity.data);
                }else {
                    getView().getSampleMaterialReferenceFailed(entity.msg);
                }
            }
        }));

    }
}
