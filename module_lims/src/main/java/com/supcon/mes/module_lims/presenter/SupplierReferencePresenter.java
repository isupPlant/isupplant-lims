package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_lims.model.bean.SupplierReferenceListEntity;
import com.supcon.mes.module_lims.model.contract.SupplierReferenceContract;
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
public class SupplierReferencePresenter extends SupplierReferenceContract.Presenter {
    @Override
    public void getSupplierReferenceList(int pageNo, Map<String, Object> params) {
        String viewCode = "BaseSet_1.0.0_cooperate_cmcLayoutRef";
        String modelAlias = "cooperate";
        FastQueryCondEntity fastQuery;

        fastQuery = BAPQueryHelper.createSingleFastQueryCond(params);
        fastQuery.viewCode = viewCode;
        fastQuery.modelAlias = modelAlias;

        Map<String, Object> map = new HashMap<>();
        if (params.size()>0){
            map.put("fastQueryCond",fastQuery.toString());
        }

        Map<String,Object> condMap = new HashMap<>();
        condMap.put("isSupplier","1");
        map.put("customCondition",condMap);
        map.put("pageNo",pageNo);
        map.put("paging",true);
        map.put("pageSize",20);

        mCompositeSubscription.add(BaseLimsHttpClient.getSupplierReferenceList(map).onErrorReturn(new Function<Throwable, SupplierReferenceListEntity>() {
            @Override
            public SupplierReferenceListEntity apply(Throwable throwable) throws Exception {
                SupplierReferenceListEntity entity = new SupplierReferenceListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<SupplierReferenceListEntity>() {
            @Override
            public void accept(SupplierReferenceListEntity entity) throws Exception {
                if (entity.success){
                    getView().getSupplierReferenceListSuccess(entity);
                }else {
                    getView().getSupplierReferenceListFailed(entity.msg);
                }
            }
        }));
    }
}
