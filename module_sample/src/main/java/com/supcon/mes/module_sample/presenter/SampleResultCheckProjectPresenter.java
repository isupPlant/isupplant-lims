package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectEntity;
import com.supcon.mes.module_sample.model.contract.SampleResultCheckProjectContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by yaobing on 2021/2/2
 * desc:
 */
public class SampleResultCheckProjectPresenter extends SampleResultCheckProjectContract.Presenter {

    @Override
    public void getSampleResultCheckProject(long pageType, Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> customCondition=new HashMap<>();
            map.put("customCondition",customCondition);
            map.put("pageNo",0);
            map.put("pageSize",65535);
            map.put("crossCompanyFlag",true);
            map.put("permissionCode","LIMSSample_5.0.0.0_sample_recordCheckBySample");

            mCompositeSubscription.add(
                    SampleHttpClient.getSampleResultCheckList("testCheck",pageType,map)
                            .onErrorReturn(throwable -> {
                                BAP5CommonEntity<CommonListEntity<SampleResultCheckProjectEntity>> entity=new BAP5CommonEntity<>();
                                entity.success=false;
                                entity.msg= ErrorMsgHelper.msgParse(throwable.getMessage());
                                return entity;
                            })
                            .subscribe(entity -> {
                                if (entity.success){
                                    getView().getSampleResultCheckProjectSuccess(entity.data.result);
                                }else {
                                    getView().getSampleResultCheckProjectFailed(entity.msg);
                                }
                            })

            );

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
