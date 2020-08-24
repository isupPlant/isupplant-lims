package com.supcon.mes.module_sample.presenter;


import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;
import com.supcon.mes.module_sample.model.bean.SampleEntity;
import com.supcon.mes.module_sample.model.contract.SampleListApi;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wanghaidong on 2020/8/14
 * Email:wanghaidong1@supcon.com
 */
public class SingleSamplePresenter extends SampleListApi.Presenter {
    @Override
    public void getSampleList(int pageNo, Map<String, Object> params) {
        String viewCode = "LIMSSample_5.0.0.0_sample_recordBySingleSample";
        String modelAlias = "sampleInfo";
        FastQueryCondEntity fastQuery;

        fastQuery = BAPQueryHelper.createSingleFastQueryCond(params);
        fastQuery.viewCode = viewCode;
        fastQuery.modelAlias = modelAlias;

        Map<String, Object> map = new HashMap<>();
        map.put("fastQueryCond",fastQuery.toString());
        map.put("datagridCode","LIMSSample_5.0.0.0_sample_recordBySingleSampledg1592183350560");
        map.put("pageNo",pageNo);
        map.put("pageSize",10);

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
}
