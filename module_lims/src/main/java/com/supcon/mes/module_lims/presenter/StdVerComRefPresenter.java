package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.StdVerComIdEntity;
import com.supcon.mes.module_lims.model.contract.StdVerComRefContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/11/11
 * class name
 */
public class StdVerComRefPresenter extends StdVerComRefContract.Presenter {
    @Override
    public void getStdVerComRefList(String stdVerId, String reportNames, String pageNo, Map<String, Object> map) {
        String viewCode = "LIMSBasic_1.0.0_qualityStd_stdVerComRef";
        String modelAlias = "stdVerCom";
        FastQueryCondEntity fastQuery;
        fastQuery = BAPQueryHelper.createSingleFastQueryCond(map);
        fastQuery.viewCode = viewCode;
        fastQuery.modelAlias = modelAlias;

        Map<String, Object> params = new HashMap<>();
        params.put("pageNo",pageNo);
        params.put("paging",true);
        params.put("pageSize",10);
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("stdVerId",stdVerId);
        conditionMap.put("reportNames",reportNames);
        params.put("customCondition",conditionMap);
        if (map.size() > 0){
            params.put("fastQueryCond",fastQuery.toString());
        }

        mCompositeSubscription.add(BaseLimsHttpClient.getStdVerComList(params).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonBAPListEntity<StdVerComIdEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonBAPListEntity<StdVerComIdEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonBAPListEntity<StdVerComIdEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonBAPListEntity<StdVerComIdEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getStdVerComRefListSuccess(entity.data);
                }else {
                    getView().getStdVerComRefListFailed(entity.msg);
                }
            }
        }));

    }
}
