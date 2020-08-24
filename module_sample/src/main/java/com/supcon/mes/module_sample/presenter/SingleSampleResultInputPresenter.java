package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.module_sample.model.bean.SampleInspectItemEntity;
import com.supcon.mes.module_sample.model.bean.SingleInspectionItemListEntity;
import com.supcon.mes.module_sample.model.contract.SingleSampleResultInputContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/8/14
 * Email:wanghaidong1@supcon.com
 */
public class SingleSampleResultInputPresenter extends SingleSampleResultInputContract.Presenter {
    @Override
    public void getSampleCom(Long id) {
        mCompositeSubscription.add(
                SampleHttpClient
                        .getSampleCom(id)
                        .onErrorReturn(error->{
                            CommonBAP5ListEntity commonBAP5ListEntity=new CommonBAP5ListEntity();
                            commonBAP5ListEntity.msg=error.getMessage();
                            commonBAP5ListEntity.success=false;
                            return commonBAP5ListEntity;
                        })
                       .subscribe(new Consumer<CommonBAP5ListEntity<SampleInspectItemEntity>>() {
                           @Override
                           public void accept(CommonBAP5ListEntity<SampleInspectItemEntity> sampleInspectListEntity) throws Exception {
                               if (sampleInspectListEntity.success){
                                   getView().getSampleComSuccess(sampleInspectListEntity);
                               }else {
                                   getView().getSampleComFailed(sampleInspectListEntity.msg);
                               }
                           }
                       })
        );
    }

    @Override
    public void getSampleInspectItem(Long sampleId) {
        mCompositeSubscription.add(
                SampleHttpClient.getSampleInspectItem(sampleId)
                                .onErrorReturn(error->{
                                    SingleInspectionItemListEntity entity=new SingleInspectionItemListEntity();
                                    entity.msg=error.getMessage();
                                    entity.success=false;
                                    return entity;
                                })
                                .subscribe(singleInspectionItemListEntity -> {
                                    if (singleInspectionItemListEntity.success){
                                        getView().getSampleInspectItemSuccess(singleInspectionItemListEntity);
                                    }else {
                                        getView().getSampleInspectItemFailed(singleInspectionItemListEntity.msg);
                                    }
                                })
        );
    }
}
