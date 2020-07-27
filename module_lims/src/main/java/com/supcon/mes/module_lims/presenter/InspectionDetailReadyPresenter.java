package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_lims.model.bean.BusinessTypeListEntity;
import com.supcon.mes.module_lims.model.bean.IfUploadEntity;
import com.supcon.mes.module_lims.model.contract.InspectionDetailReadyApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/14
 * class name  业务类型参照 Presenter
 */
public class InspectionDetailReadyPresenter extends InspectionDetailReadyApi.Presenter {

    @Override
    public void getBusinessTypeList() {

        Map<String, Object> tableTypeCodeMap = new HashMap<>();
        tableTypeCodeMap.put("tableTypeCode","manu");

        Map<String, Object> map = new HashMap<>();
        map.put("customCondition",tableTypeCodeMap);
        map.put("pageNo",1);
        map.put("paging",true);
        map.put("pageSize",10);

        mCompositeSubscription.add(BaseLimsHttpClient.getBusinessTypeList(map).onErrorReturn(new Function<Throwable, BusinessTypeListEntity>() {
            @Override
            public BusinessTypeListEntity apply(Throwable throwable) throws Exception {
                BusinessTypeListEntity entity = new BusinessTypeListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BusinessTypeListEntity>() {
            @Override
            public void accept(BusinessTypeListEntity entity) throws Exception {
                if (entity.success){
                    getView().getBusinessTypeListSuccess(entity);
                }else {
                    getView().getBusinessTypeListFailed(entity.msg);
                }
            }
        }));
    }

    @Override
    public void getIfUpload() {
        mCompositeSubscription.add(BaseLimsHttpClient.getIfUpload("LIMSSample").onErrorReturn(new Function<Throwable, BAP5CommonEntity<IfUploadEntity>>() {
            @Override
            public BAP5CommonEntity<IfUploadEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<IfUploadEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<IfUploadEntity> entity) throws Exception {
                if (entity.success){
                    getView().getIfUploadSuccess(entity.data);
                }else {
                    getView().getIfUploadFailed(entity.msg);
                }
            }
        }));
    }
}
