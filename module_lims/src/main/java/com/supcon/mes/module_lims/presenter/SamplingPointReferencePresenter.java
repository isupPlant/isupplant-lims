package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_lims.model.bean.SamplingPointListEntity;
import com.supcon.mes.module_lims.model.contract.SamplingPointApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/14
 * class name 采样点列表参照Presenter
 */
public class SamplingPointReferencePresenter extends SamplingPointApi.Presenter {
    @Override
    public void getSamplingPointList(int pageNo, Map<String, Object> params) {
        String viewCode = "LIMSBasic_1.0.0_pickSite_pickSiteRefLayout";
        String modelAlias = "pickSite";

        FastQueryCondEntity fastQuery = BAPQueryHelper.createSingleFastQueryCond(params);
        fastQuery.viewCode = viewCode;
        fastQuery.modelAlias = modelAlias;

//        if (params.size() > 0){
//            params.put("fastQueryCond",fastQuery);
//        }
        params.put("fastQueryCond",fastQuery);
        params.put("pageNo",pageNo);
        params.put("paging",true);
        params.put("pageSize",10);

        mCompositeSubscription.add(BaseLimsHttpClient.getSamplingPointList(params).onErrorReturn(new Function<Throwable, SamplingPointListEntity>() {
            @Override
            public SamplingPointListEntity apply(Throwable throwable) throws Exception {
                SamplingPointListEntity entity = new SamplingPointListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<SamplingPointListEntity>() {
            @Override
            public void accept(SamplingPointListEntity entity) throws Exception {
                if (entity.success){
                    getView().getSamplingPointListSuccess(entity);
                }else {
                    getView().getSamplingPointListFailed(entity.msg);
                }
            }
        }));
    }
}
