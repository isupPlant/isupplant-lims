package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectDetailEntity;
import com.supcon.mes.module_sample.model.contract.SampleResultCheckProjectDetailContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaobing on 2021/2/2
 * desc:
 */
public class SampleResultCheckProjectDetailPresenter extends SampleResultCheckProjectDetailContract.Presenter {
    @Override
    public void getSampleResultCheckProjectDetail(long pageType, Map<String, Object> params) {
        try {
            mCompositeSubscription.add(
                    SampleHttpClient.getSampleResultCheckList(pageType)
                            .onErrorReturn(throwable -> {
                                BAP5CommonEntity<CommonListEntity<SampleResultCheckProjectDetailEntity>> entity=new BAP5CommonEntity<>();
                                entity.success=false;
                                entity.msg= ErrorMsgHelper.msgParse(throwable.getMessage());
                                return entity;
                            })
                            .subscribe(entity -> {
                                if (entity.success){
                                    getView().getSampleResultCheckProjectDetailSuccess(entity.data.result);
                                }else {
                                    getView().getSampleResultCheckProjectDetailFailed(entity.msg);
                                }
                            })

            );

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
