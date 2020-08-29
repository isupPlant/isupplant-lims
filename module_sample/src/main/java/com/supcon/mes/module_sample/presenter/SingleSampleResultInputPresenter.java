package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.model.bean.SampleInspectItemEntity;
import com.supcon.mes.module_sample.model.bean.SingleInspectionItemListEntity;
import com.supcon.mes.module_sample.model.contract.SingleSampleResultInputContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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
                        .onErrorReturn(error -> {
                            CommonBAP5ListEntity bap5ListEntity = new CommonBAP5ListEntity();
                            bap5ListEntity.success = false;
                            bap5ListEntity.msg = error.getMessage();
                            return bap5ListEntity;
                        })
                        .subscribe(new Consumer<CommonBAP5ListEntity<InspectionSubEntity>>() {
                            @Override
                            public void accept(CommonBAP5ListEntity<InspectionSubEntity> bAP5ListEntity) throws Exception {
                                if (bAP5ListEntity.success) {
                                    getView().getSampleComSuccess(bAP5ListEntity);
                                } else {
                                    getView().getSampleComFailed(bAP5ListEntity.msg);
                                }
                            }
                        }));


    }

    @Override
    public void getSampleInspectItem(Long sampleId) {
        mCompositeSubscription.add(
                SampleHttpClient.getSampleInspectItem(sampleId)
                        .onErrorReturn(error -> {
                            SingleInspectionItemListEntity entity = new SingleInspectionItemListEntity();
                            entity.msg = error.getMessage();
                            entity.success = false;
                            return entity;
                        })
                        .subscribe(singleInspectionItemListEntity -> {
                            if (singleInspectionItemListEntity.success) {
                                getView().getSampleInspectItemSuccess(singleInspectionItemListEntity);
                            } else {
                                getView().getSampleInspectItemFailed(singleInspectionItemListEntity.msg);
                            }
                        })
        );
    }
}
