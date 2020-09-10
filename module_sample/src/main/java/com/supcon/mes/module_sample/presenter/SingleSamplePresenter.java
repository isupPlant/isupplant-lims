package com.supcon.mes.module_sample.presenter;


import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.SubcondEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;
import com.supcon.mes.module_sample.model.bean.SampleEntity;
import com.supcon.mes.module_sample.model.contract.SampleListApi;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.supcon.mes.middleware.constant.Constant.BAPQuery.BE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.GE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE_OPT_BLUR;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE_OPT_Q;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TYPE_NORMAL;

/**
 * Created by wanghaidong on 2020/8/14
 * Email:wanghaidong1@supcon.com
 */
public class SingleSamplePresenter extends SampleListApi.Presenter {
    @Override
    public void getSampleList(Map<String,Object> timeMap,Map<String, Object> params) {
        String viewCode = "LIMSSample_5.0.0.0_sample_recordBySingleSample";
        String modelAlias = "sampleInfo";
        Map<String, Object> map = new HashMap<>();
        FastQueryCondEntity fastQueryCondEntity=null;
        if (!params.isEmpty()){
            fastQueryCondEntity=getFastQueryEntity(params);
            fastQueryCondEntity.viewCode = viewCode;
            fastQueryCondEntity.modelAlias = modelAlias;
            map.put("fastQueryCond",fastQueryCondEntity.toString());
        }
        if (!timeMap.isEmpty()){
            fastQueryCondEntity=getFastQueryEntity(timeMap);
            if (fastQueryCondEntity!=null){
            }else {
                fastQueryCondEntity.viewCode = viewCode;
                fastQueryCondEntity.modelAlias = modelAlias;
            }
            map.put("fastQueryCond",fastQueryCondEntity.toString());
        }
        map.put("datagridCode","LIMSSample_5.0.0.0_sample_recordBySingleSampledg1592183350560");
        map.put("pageNo",1);
        map.put("pageSize",65535);

        mCompositeSubscription.add(SampleHttpClient.getSampleList("recordBySingleSample",map).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonListEntity<SampleEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonListEntity<SampleEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new  BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonListEntity<SampleEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonListEntity<SampleEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getSampleListSuccess(entity.data);
                }else {
                    getView().getSampleListFailed(entity.msg);
                }
            }
        }));
    }
    FastQueryCondEntity getFastQueryEntity(Map<String,Object> params){
        FastQueryCondEntity fastQueryCondEntity=new FastQueryCondEntity();
        fastQueryCondEntity.subconds=new ArrayList<>();
        for(Map.Entry<String,Object> entry:params.entrySet()){
            fastQueryCondEntity.subconds.add(parseKey(entry.getKey(),entry.getValue()));
        }
        return fastQueryCondEntity;
    }
    private static BaseSubcondEntity parseKey(String key, Object value) {
        SubcondEntity subcondEntity = null;
        switch (key) {
            case Constant.BAPQuery.IN_DATE_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = BusinessType.BAPQuery.REGISTER_TIME;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.IN_DATE_END:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = BusinessType.BAPQuery.REGISTER_TIME;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.CODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = Constant.BAPQuery.BAPCODE;
                subcondEntity.operator = BE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.NAME:
            case Constant.BAPQuery.BATCH_CODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = Constant.BAPQuery.BAPCODE;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
        }
        return subcondEntity;
    }
}
