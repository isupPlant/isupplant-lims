package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_sample.model.bean.InspectionItemsEntity;
import com.supcon.mes.module_sample.model.contract.InspectionItemsApi;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/30
 * class name
 */
public class InspectionItemsPresenter extends InspectionItemsApi.Presenter {
    @Override
    public void getInspectionItemList(String sampleId, int pageNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo",pageNo);
        map.put("pageSize",10);
        mCompositeSubscription.add(SampleHttpClient.getInspectionItemList("recordResult",sampleId,map).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonListEntity<InspectionItemsEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonListEntity<InspectionItemsEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new  BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonListEntity<InspectionItemsEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonListEntity<InspectionItemsEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getInspectionItemListSuccess(entity.data);
                }else {
                    getView().getInspectionItemListFailed(entity.msg);
                }
            }
        }));
    }
}
