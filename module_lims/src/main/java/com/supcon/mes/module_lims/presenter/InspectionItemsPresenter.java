package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.module_lims.model.bean.InspectionItemsListEntity;
import com.supcon.mes.module_lims.model.contract.InspectionItemsContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name
 */
public class InspectionItemsPresenter extends InspectionItemsContract.Presenter {
    @Override
    public void getInspectionItemsList( String stdVersionId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stdVersionId",stdVersionId);
        mCompositeSubscription.add(BaseLimsHttpClient.getInspectionItemsList(map).onErrorReturn(new Function<Throwable, InspectionItemsListEntity>() {
            @Override
            public InspectionItemsListEntity apply(Throwable throwable) throws Exception {
                InspectionItemsListEntity entity = new InspectionItemsListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<InspectionItemsListEntity>() {
            @Override
            public void accept(InspectionItemsListEntity entity) throws Exception {
                if (entity.success){
                    getView().getInspectionItemsListSuccess(entity);
                }else {
                    getView().getInspectionItemsListFailed(entity.msg);
                }
            }
        }));
    }

    @Override
    public void getInspectComDataByInspectStdId(String inspectStdId) {
        Map<String, Object> map = new HashMap<>();
        map.put("inspectStdId",inspectStdId);
        mCompositeSubscription.add(BaseLimsHttpClient.getInspectComDataByInspectStdId(map).onErrorReturn(new Function<Throwable, InspectionItemsListEntity>() {
            @Override
            public InspectionItemsListEntity apply(Throwable throwable) throws Exception {
                InspectionItemsListEntity entity = new InspectionItemsListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<InspectionItemsListEntity>() {
            @Override
            public void accept(InspectionItemsListEntity entity) throws Exception {
                if (entity.success){
                    getView().getInspectComDataByInspectStdIdSuccess(entity);
                }else {
                    getView().getInspectComDataByInspectStdIdFailed(entity.msg);
                }
            }
        }));
    }

    @Override
    public void getInspectComDataByInspectProjId(String inspectProjId) {
        Map<String, Object> map = new HashMap<>();
        map.put("inspectProjId",inspectProjId);
        mCompositeSubscription.add(BaseLimsHttpClient.getInspectComDataByInspectProjId(map).onErrorReturn(new Function<Throwable, InspectionItemsListEntity>() {
            @Override
            public InspectionItemsListEntity apply(Throwable throwable) throws Exception {
                InspectionItemsListEntity entity = new InspectionItemsListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<InspectionItemsListEntity>() {
            @Override
            public void accept(InspectionItemsListEntity entity) throws Exception {
                if (entity.success){
                    getView().getInspectComDataByInspectProjIdSuccess(entity);
                }else {
                    getView().getInspectComDataByInspectProjIdFailed(entity.msg);
                }
            }
        }));
    }
}
