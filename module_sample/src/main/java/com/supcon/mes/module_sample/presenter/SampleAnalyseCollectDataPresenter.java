package com.supcon.mes.module_sample.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_sample.model.contract.SampleAnalyseCollectDataContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2021/1/18
 * Email:wanghaidong1@supcon.com
 * desc:
 */
public class SampleAnalyseCollectDataPresenter extends SampleAnalyseCollectDataContract.Presenter {
    String error;
    @Override
    public void getFormatDataByCollectCode(String url) {
        error=null;
        mCompositeSubscription.add(
                SampleHttpClient.getFormatDataByCollectCode(url)
                                .onErrorReturn(throwable -> {
                                    List<Map<String,Object>> list=new ArrayList<>();
                                    error=throwable.getMessage();
                                    if ("End of input at line 1 column 1 path $".equals(error)){
                                        error=null;
                                    }
                                    return list;
                                })
                .subscribe(new Consumer<List<Map<String, Object>>>() {
                    @Override
                    public void accept(List<Map<String, Object>> maps) throws Exception {
                        if (TextUtils.isEmpty(error)){
                            getView().getFormatDataByCollectCodeSuccess(maps);
                        }else {
                            getView().getFormatDataByCollectCodeFailed(error);
                        }
                    }
                })

        );
    }
}
