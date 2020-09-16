package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationEntity;
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
    public void getInspectionDetailPtData(String type, boolean isEdit, String id) {
        String dg = "";
        String datagridCode = "";
        if (type.equals(LimsConstant.PleaseCheck.PRODUCT_PLEASE_CHECK)){
            if (isEdit){
                dg = "data-dg1591080786501";
                datagridCode = "QCS_5.0.0.0_inspect_manuInspectEditdg1591080786501";
            }else{
                dg = "data-dg1591080031851";
                datagridCode = "QCS_5.0.0.0_inspect_manuInspectViewdg1591080031851";
            }
        }else if (type.equals(LimsConstant.PleaseCheck.INCOMING_PLEASE_CHECK)){
            if (isEdit){
                dg = "data-dg1587627206280";
                datagridCode = "QCS_5.0.0.0_inspect_purchInspectEditdg1587627206280";
            }else {
                dg = "data-dg1588124680273";
                datagridCode = "QCS_5.0.0.0_inspect_purchInspectViewdg1588124680273";
            }
        }else if (type.equals(LimsConstant.PleaseCheck.OTHER_PLEASE_CHECK)){
            if (isEdit){
                dg = "data-dg1591595570526";
                datagridCode = "QCS_5.0.0.0_inspect_otherInspectEditdg1591595570526";
            }else {
                dg = "data-dg1591596373455";
                datagridCode = "QCS_5.0.0.0_inspect_otherInspectViewdg1591596373455";
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
    public void getInspectionApplicationByPending(Long moduleId, Long pendingId) {
        mCompositeSubscription.add(BaseLimsHttpClient.getInspectionApplicationByPending(moduleId, pendingId).onErrorReturn(new Function<Throwable, BAP5CommonEntity<InspectionApplicationEntity>>() {
            @Override
            public BAP5CommonEntity<InspectionApplicationEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<InspectionApplicationEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<InspectionApplicationEntity> entity) throws Exception {
                if (entity.success){
                    getView().getInspectionApplicationByPendingSuccess(entity.data);
                }else {
                    getView().getInspectionApplicationByPendingFailed(entity.msg);
                }
            }
        }));
    }


}
