package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.SubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;
import com.supcon.mes.module_lims.utils.Util;
import com.supcon.mes.module_sample.model.bean.SampleAccountEntity;
import com.supcon.mes.module_sample.model.contract.SampleAccountContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;



/**
 * author huodongsheng
 * on 2020/10/15
 * class name
 */
public class SampleAccountPresenter extends SampleAccountContract.Presenter {
    @Override
    public void getSampleAccountList(int pageNo, Map<String, Object> params) {
        String joinInfo = "LIMSBA_PICKSITE,ID,LIMSSA_SAMPLE_INFOS,PS_ID";
        String viewCode = "LIMSSample_5.0.0.0_sample_sampleInfoLayout";
        String modelAlias = "sampleInfo";
        FastQueryCondEntity fastQuery = null;

        if (params.containsKey(Constant.BAPQuery.NAME) || params.containsKey(Constant.BAPQuery.CODE)
                || params.containsKey(Constant.BAPQuery.BATCH_CODE) || params.containsKey(Constant.BAPQuery.SAMPLE_STATE)){
            fastQuery = BAPQueryHelper.createSingleFastQueryCond(params);
            fastQuery.viewCode = viewCode;
            fastQuery.modelAlias = modelAlias;
        }

        if (params.containsKey("point-name")){
            String value = (String) params.get("point-name");
            params.clear();
            params.put(Constant.BAPQuery.NAME,value);
            fastQuery = BAPQueryHelper.createSingleFastQueryCond(new HashMap<>());
            fastQuery.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(params,joinInfo));
            fastQuery.viewCode = viewCode;
            fastQuery.modelAlias = modelAlias;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("pageNo",pageNo);
        map.put("paging",true);
        map.put("pageSize",10);
        map.put("permissionCode","LIMSSample_5.0.0.0_sample_sampleInfoLayout");
        if (null != fastQuery){
            map.put("fastQueryCond",fastQuery.toString());
        }
        mCompositeSubscription.add(SampleHttpClient.getSampleAccountList(map).onErrorReturn(new Function<Throwable, CommonBAP5ListEntity<SampleAccountEntity>>() {
            @Override
            public CommonBAP5ListEntity<SampleAccountEntity> apply(Throwable throwable) throws Exception {
                CommonBAP5ListEntity commonBAP5ListEntity = new CommonBAP5ListEntity();
                commonBAP5ListEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                commonBAP5ListEntity.success = false;
                return commonBAP5ListEntity;
            }
        }).subscribe(new Consumer<CommonBAP5ListEntity<SampleAccountEntity>>() {
            @Override
            public void accept(CommonBAP5ListEntity<SampleAccountEntity> entity) throws Exception {
                if (entity.success){
                    getView().getSampleAccountListSuccess(entity.data);
                }else {
                    getView().getSampleAccountListFailed(entity.msg);
                }
            }
        }));
    }
}
