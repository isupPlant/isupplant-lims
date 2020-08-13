package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.model.bean.DeviceReferenceEntity;
import com.supcon.mes.module_lims.model.contract.DeviceReferenceApi;
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
public class DeviceReferencePresenter extends DeviceReferenceApi.Presenter {

    @Override
    public void getDeviceReferenceList(int pageNo,String eamClassId, Map<String, Object> params) {
        String viewCode = "BaseSet_1.0.0_eamInfo_eamInfoRefLayout";
        String modelAlias = "eamInfo";
        FastQueryCondEntity fastQuery;

        fastQuery = BAPQueryHelper.createSingleFastQueryCond(params);
        fastQuery.viewCode = viewCode;
        fastQuery.modelAlias = modelAlias;

        Map<String, Object> map = new HashMap<>();
        map.put("fastQueryCond",fastQuery.toString());

        Map<String, Object> customConditionMap = new HashMap<>();
        if (StringUtil.isEmpty(eamClassId)){
            customConditionMap.put("eamClassId","");
        }else {
            customConditionMap.put("eamClassId",eamClassId);
        }
        map.put("customCondition",customConditionMap);
        map.put("pageNo",pageNo);
        map.put("paging",true);
        map.put("pageSize",10);

        mCompositeSubscription.add(BaseLimsHttpClient.getDeviceReference(map).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonListEntity<DeviceReferenceEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonListEntity<DeviceReferenceEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonListEntity<DeviceReferenceEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonListEntity<DeviceReferenceEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getDeviceReferenceListSuccess(entity.data);
                }else {
                    getView().getDeviceReferenceListFailed(entity.msg);
                }
            }
        }));
    }
}
