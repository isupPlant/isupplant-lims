package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.DeviceTypeReferenceEntity;
import com.supcon.mes.module_lims.model.contract.DeviceTypeReferenceApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/8/13
 * class name
 */
public class DeviceTypeReferencePresenter extends DeviceTypeReferenceApi.Presenter {
    @Override
    public void getDeviceTypeReference(int pageNo, Map<String, Object> map) {
        String viewCode = "BaseSet_1.0.0_eamType_eamTypeRefLayout";
        String modelAlias = "eamType";
        FastQueryCondEntity fastQuery;

        fastQuery = BAPQueryHelper.createSingleFastQueryCond(map);
        fastQuery.viewCode = viewCode;
        fastQuery.modelAlias = modelAlias;

        Map<String, Object> params = new HashMap<>();
        params.put("fastQueryCond",fastQuery.toString());
        params.put("pageNo",pageNo);
        params.put("paging",true);
        params.put("pageSize",10);
        mCompositeSubscription.add(BaseLimsHttpClient.getDeviceTypeReference(params).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonListEntity<DeviceTypeReferenceEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonListEntity<DeviceTypeReferenceEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonListEntity<DeviceTypeReferenceEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonListEntity<DeviceTypeReferenceEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getDeviceTypeReferenceSuccess(entity.data);
                }else {
                    getView().getDeviceTypeReferenceFailed(entity.msg);
                }
            }
        }));
    }
}
