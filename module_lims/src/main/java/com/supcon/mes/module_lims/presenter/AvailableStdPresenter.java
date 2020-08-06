package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.AvailableStdEntity;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdListEntity;
import com.supcon.mes.module_lims.model.bean.TemporaryQualityStandardEntity;
import com.supcon.mes.module_lims.model.contract.AvailableStdIdApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name 通过物料id 查询得到可用标准 id
 */
public class AvailableStdPresenter extends AvailableStdIdApi.Presenter {
    @Override
    public void getAvailableStdId(String productId) {
        Map<String, Object> map = new HashMap<>();
        map.put("productId",productId);
        mCompositeSubscription.add(BaseLimsHttpClient.getAvailableStdId(map).onErrorReturn(new Function<Throwable, BAP5CommonEntity<AvailableStdEntity>>() {
            @Override
            public BAP5CommonEntity<AvailableStdEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<AvailableStdEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<AvailableStdEntity> entity) throws Exception {
                if (entity.success){
                    getView().getAvailableStdIdSuccess(entity.data);
                }else {
                    getView().getAvailableStdIdFailed(entity.msg);
                }

            }
        }));
    }

    @Override
    public void getDefaultStandardById(String productId) {
        Map<String, Object> map = new HashMap<>();
        map.put("productId",productId);
        mCompositeSubscription.add(BaseLimsHttpClient.getDefaultStandardById(map).onErrorReturn(new Function<Throwable, BAP5CommonEntity<TemporaryQualityStandardEntity>>() {
            @Override
            public BAP5CommonEntity<TemporaryQualityStandardEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<TemporaryQualityStandardEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<TemporaryQualityStandardEntity> entity) throws Exception {
                if (entity.success){
                    getView().getDefaultStandardByIdSuccess(entity.data);
                }else {
                    getView().getDefaultStandardByIdFailed(entity.msg);
                }
            }
        }));
    }

    @Override
    public void getDefaultItems(String stdVerId) {
        mCompositeSubscription.add(BaseLimsHttpClient.getDefaultItems(stdVerId).onErrorReturn(new Function<Throwable, StdVerComIdListEntity>() {
            @Override
            public StdVerComIdListEntity apply(Throwable throwable) throws Exception {
                StdVerComIdListEntity entity = new StdVerComIdListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<StdVerComIdListEntity>() {
            @Override
            public void accept(StdVerComIdListEntity entity) throws Exception {
                if (entity.success){
                    getView().getDefaultItemsSuccess(entity);
                }else {
                    getView().getDefaultItemsFailed(entity.msg);
                }
            }
        }));
    }

    @Override
    public void getDefaultInspProjByStdVerId(InspectionDetailPtEntity mEntity, String stdVerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stdVersionId",stdVerId);
        mCompositeSubscription.add(BaseLimsHttpClient.getDefaultInspProjByStdVerId(map).onErrorReturn(new Function<Throwable, BAP5CommonEntity<InspectionDetailPtEntity>>() {
            @Override
            public BAP5CommonEntity<InspectionDetailPtEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<InspectionDetailPtEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<InspectionDetailPtEntity> entity) throws Exception {
                if (entity.success){
                    entity.data.setStdVerId(mEntity.getStdVerId());
                    entity.data.setInspStdVerCom(GsonUtil.gsonString(entity.data.getStdVerComList()));
                    if (null != entity.data.getInspectProj()){   //如果接口中请求回来的请检方案不为空的话   就把请检方案放入adapter能解析的实体中
                        entity.data.setInspectProjId(entity.data.getInspectProj());
                    }
                    getView().getDefaultInspProjByStdVerIdSuccess(entity.data);
                }else {
                    getView().getDefaultInspProjByStdVerIdFailed(entity.msg);
                }
            }
        }));
    }
}
