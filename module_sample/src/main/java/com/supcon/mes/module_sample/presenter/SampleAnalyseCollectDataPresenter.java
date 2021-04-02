package com.supcon.mes.module_sample.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.contract.SampleAnalyseCollectDataContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.io.EOFException;
import java.net.SocketTimeoutException;
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
    public void getFormatDataByCollectCode(String url,boolean isAuto,String value) {
        error=null;
        Map<String,Object> map = new HashMap<>();
        if (isAuto){
            map.put("collectCode",value);
        }else {
            map.put("fileId",value);
        }
        mCompositeSubscription.add(
                SampleHttpClient.getFormatDataByCollectCode(url,map)
                                .onErrorReturn(throwable -> {
                                    List<Map<String,Object>> list=new ArrayList<>();
                                    error= HttpErrorReturnUtil.getErrorInfo(throwable);
                                    if ("End of input at line 1 column 1 path $".equals(error)){
                                        error=null;
                                    }
                                    if (throwable instanceof SocketTimeoutException){
                                        error = SupPlantApplication.getAppContext().getString(R.string.lims_analysis_location_connection_timeout);
                                    }
                                    if (throwable instanceof EOFException && error.equals("java.io.EOFException: End of input at line 1 column 1 path $")){
                                        error = SupPlantApplication.getAppContext().getString(R.string.lims_the_resolution_result_does_not_exist);
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
