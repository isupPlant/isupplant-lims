package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.model.bean.IfUploadEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtListEntity;
import com.supcon.mes.module_lims.model.contract.InspectionApplicationDetailApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name
 */
public class InspectionApplicationDetailPresenter extends InspectionApplicationDetailApi.Presenter {
    @Override
    public void getInspectionDetailHeaderData(String id, String paddingId) {
        if (paddingId.equals("null")){
            paddingId = "";
        }
        mCompositeSubscription.add(BaseLimsHttpClient.getInspectionDetailHeaderData(id,paddingId).onErrorReturn(new Function<Throwable, BAP5CommonEntity<InspectionApplicationDetailHeaderEntity>>() {
            @Override
            public BAP5CommonEntity<InspectionApplicationDetailHeaderEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<InspectionApplicationDetailHeaderEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<InspectionApplicationDetailHeaderEntity> entity) throws Exception {
                if (entity.success){
                    getView().getInspectionDetailHeaderDataSuccess(entity.data);
                }else {
                    getView().getInspectionDetailHeaderDataFailed(entity.msg);
                }

            }
        }));
    }

    @Override
    public void getInspectionDetailPtData(String type, String level, String id) {
        String dg = "";
        String datagridCode = "";
        if (type.equals(BusinessType.PleaseCheck.PRODUCT_PLEASE_CHECK)){
            if (level.equals("one")){
                dg = "data-dg1591080786501";
                datagridCode = "QCS_5.0.0.0_inspect_manuInspectEditdg1591080786501";
            }else if (level.equals("two")){
                dg = "data-dg1591080792032";
                datagridCode = "QCS_5.0.0.0_inspect_manuInspectEditdg1591080792032";
            }
        }else if (type.equals(BusinessType.PleaseCheck.INCOMING_PLEASE_CHECK)){
            if (level.equals("one")){

            }else if (level.equals("two")){

            }
        }else if (type.equals(BusinessType.PleaseCheck.OTHER_PLEASE_CHECK)){
            if (level.equals("one")){

            }else if (level.equals("two")){

            }
        }

        mCompositeSubscription.add(BaseLimsHttpClient.getInspectionDetailPtData(dg,datagridCode,id).onErrorReturn(new Function<Throwable, InspectionDetailPtListEntity>() {
            @Override
            public InspectionDetailPtListEntity apply(Throwable throwable) throws Exception {
                InspectionDetailPtListEntity entity = new InspectionDetailPtListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<InspectionDetailPtListEntity>() {
            @Override
            public void accept(InspectionDetailPtListEntity entity) throws Exception {
                if (entity.success){
                    getView().getInspectionDetailPtDataSuccess(entity);
                }else {
                    getView().getInspectionDetailHeaderDataFailed(entity.msg);
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
