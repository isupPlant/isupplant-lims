package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_lims.model.bean.PleaseCheckSchemeListEntity;
import com.supcon.mes.module_lims.model.contract.PleaseCheckSchemeReferenceApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/20
 * class name
 */
public class PleaseCheckSchemeReferencePresenter extends PleaseCheckSchemeReferenceApi.Presenter {
    @Override
    public void getPleaseCheckSchemeList(int pageNo, String id, Map<String, Object> params) {
        String viewCode = "LIMSBasic_1.0.0_inspectProj_inspectProjRef";
        String modelAlias = "inspectProj";
        FastQueryCondEntity fastQuery;

        fastQuery = BAPQueryHelper.createSingleFastQueryCond(params);
        fastQuery.viewCode = viewCode;
        fastQuery.modelAlias = modelAlias;

        Map<String, Object> map = new HashMap<>();
        if (params.size()>0){
            map.put("fastQueryCond",fastQuery.toString());
        }

        Map<String,Object> condMap = new HashMap<>();
        condMap.put("stdVerId",id);
        map.put("customCondition",condMap);
        map.put("pageNo",pageNo);
        map.put("paging",true);
        map.put("pageSize",20);

        mCompositeSubscription.add(BaseLimsHttpClient.getPleaseCheckSchemeList(map).onErrorReturn(new Function<Throwable, PleaseCheckSchemeListEntity>() {
            @Override
            public PleaseCheckSchemeListEntity apply(Throwable throwable) throws Exception {
                PleaseCheckSchemeListEntity entity = new PleaseCheckSchemeListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<PleaseCheckSchemeListEntity>() {
            @Override
            public void accept(PleaseCheckSchemeListEntity entity) throws Exception {
                if (entity.success){
                    getView().getPleaseCheckSchemeListSuccess(entity);
                }else {
                    getView().getPleaseCheckSchemeListFailed(entity.msg);
                }
            }
        }));
    }
}
