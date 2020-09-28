package com.supcon.mes.module_lims.presenter;

import com.google.gson.Gson;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.model.bean.SampleInquiryEntity;
import com.supcon.mes.module_lims.model.bean.SampleInquiryListEntity;
import com.supcon.mes.module_lims.model.contract.SampleInquiryContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/6
 * class name 收样取样列表公用Presenter
 */
public class SampleInquiryPresenter extends SampleInquiryContract.Presenter {
    @Override
    public void getSampleInquiryList(String type, int pageNo, Map<String, Object> params) {
        String query = "";
        Map<String,String> customCondition = new HashMap<>();
        String viewCode = "";
        String modelAlias = "sampleInfo";
        String joinInfo = "LIMSBA_PICKSITE,ID,LIMSSA_SAMPLE_INFOS,PS_ID";
        FastQueryCondEntity fastQuery;
        if (type.equals(LimsConstant.Sample.SAMPLE_COLLECTION)){
            query = "receiveListPart-query";
            //customCondition = new Gson().toJson(new ConditionEntity("sampleReceive"));
            customCondition.put("menuCode","sampleReceive");
            viewCode = "LIMSSample_5.0.0.0_sample_receiveListLayout";
        }else if (type.equals(LimsConstant.Sample.SAMPLING)){
            query = "collectListPart-query";
            //customCondition = new Gson().toJson(new ConditionEntity("sampleCollect"));
            customCondition.put("menuCode","sampleCollect");
            viewCode = "LIMSSample_5.0.0.0_sample_collectListLayout";
        }

        Map<String, Object> firstParams = new HashMap();
        //从外层传进的集合中取出 检索条件 CODE | NAME | BATCH_CODE
        if (params.containsKey(Constant.BAPQuery.CODE)) {
            firstParams.put(Constant.BAPQuery.CODE, params.get(Constant.BAPQuery.CODE));

        }
        if (params.containsKey(Constant.BAPQuery.NAME)){
            firstParams.put(Constant.BAPQuery.NAME, params.get(Constant.BAPQuery.NAME));

        }
        if (params.containsKey(Constant.BAPQuery.BATCH_CODE)){
            firstParams.put(Constant.BAPQuery.BATCH_CODE, params.get(Constant.BAPQuery.BATCH_CODE));
        }
        fastQuery = BAPQueryHelper.createSingleFastQueryCond(firstParams);

        Map<String,Object> secondParams=new HashMap<>();
        if (params.containsKey(Constant.BAPQuery.PICKSITE)){
           secondParams.put(Constant.BAPQuery.PICKSITE,params.get(Constant.BAPQuery.PICKSITE));
            fastQuery.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(secondParams,joinInfo));
        }
        fastQuery.viewCode = viewCode;
        fastQuery.modelAlias = modelAlias;

        Map<String, Object> map = new HashMap<>();
        map.put("fastQueryCond", fastQuery.toString());
        map.put("customCondition",customCondition);
       // map.put("permissionCode","LIMSSample_5.0.0.0_sample_collectListLayout");
        //map.put("classifyCodes","");
        map.put("pageNo", pageNo);
        map.put("pageSize", 10);
        map.put("paging", true);

        mCompositeSubscription.add(BaseLimsHttpClient.sampleInquiryList(query,map).onErrorReturn(new Function<Throwable, SampleInquiryListEntity>() {
            @Override
            public SampleInquiryListEntity apply(Throwable throwable) throws Exception {
                SampleInquiryListEntity entity = new SampleInquiryListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<SampleInquiryListEntity>() {
            @Override
            public void accept(SampleInquiryListEntity entity) throws Exception {
                if (entity.success){
                    getView().getSampleInquiryListSuccess(entity);
                }else {
                    getView().getSampleInquiryListFailed(entity.msg);
                }
            }
        }));

    }

    @Override
    public void sampleSubmit(String type, String time, String ids, List<SampleInquiryEntity> submitList) {
        String dealType = "";
        if (type.equals(LimsConstant.Sample.SAMPLING)){ //取样 ？ pc的取样对应的就是collectSampleSubmit。。有点怪异
            dealType = "collectSampleSubmit";
        }else if (type.equals(LimsConstant.Sample.SAMPLE_COLLECTION)){ //收样
            dealType = "receiveSample";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("dealType",dealType);
        map.put("sampleInfoListJson",new Gson().toJson(submitList));
        map.put("dealTime",time);
        map.put("dealerId",ids);

        mCompositeSubscription.add(BaseLimsHttpClient.sampleSubmit(map).onErrorReturn(new Function<Throwable, BAP5CommonEntity>() {
            @Override
            public BAP5CommonEntity apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity>() {
            @Override
            public void accept(BAP5CommonEntity entity) throws Exception {
                if (entity.success){
                    getView().sampleSubmitSuccess(entity);
                }else {
                    getView().sampleSubmitFailed(entity.msg);
                }
            }
        }));
    }
}
