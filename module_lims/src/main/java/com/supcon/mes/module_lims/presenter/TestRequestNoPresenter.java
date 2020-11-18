package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.TestRequestNoEntity;
import com.supcon.mes.module_lims.model.contract.TestRequestNoContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/11/16
 * class name
 */
public class TestRequestNoPresenter extends TestRequestNoContract.Presenter {
    @Override
    public void getTestRequestNo(int type, String pageNo, String query, Map<String, Object> params) {
        String modelAlias = "inspect";
        String joinInfo = "BASESET_MATERIALS,ID,QCS_INSPECTS,PROD_ID";
        FastQueryCondEntity fastQuery = null;


        if (params.containsKey("NAME") || params.containsKey("CODE")){
            fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
            fastQuery.modelAlias = modelAlias;
            fastQuery.viewCode = "QCS_5.0.0.0_inspect_"+getViewCode(type);
            //创建fastQuery.subconds内部该有的属性并且赋值
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(params, joinInfo);
            fastQuery.subconds.add(joinSubcondEntity);
        }else if (params.containsKey("TABLE_NO") || params.containsKey("BATCH_CODE") || params.containsKey("CHECK_STATE")){
            fastQuery = BAPQueryHelper.createSingleFastQueryCond(params);
            fastQuery.viewCode = "QCS_5.0.0.0_inspect_"+getViewCode(type);
            fastQuery.modelAlias = modelAlias;
        }else if (params.containsKey("supplier")){ //供应商
            fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
            fastQuery.viewCode = "QCS_5.0.0.0_inspect_"+getViewCode(type);
            fastQuery.modelAlias = modelAlias;
            joinInfo = "BASESET_COOPERATES,ID,QCS_INSPECTS,VENDOR_ID";

            Map<String, Object> supMap = new HashMap<>();
            supMap.put(Constant.BAPQuery.NAME,params.get("supplier"));
            params.clear();
            
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(supMap, joinInfo);
            fastQuery.subconds.add(joinSubcondEntity);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNo",pageNo);
        map.put("paging",true);
        map.put("pageSize",10);
        map.put("permissionCode","QCS_5.0.0.0_inspect_"+query);
        if (null != fastQuery){
            map.put("fastQueryCond",fastQuery.toString());
        }
        mCompositeSubscription.add(BaseLimsHttpClient.getTestRequestNo(query, map).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonBAPListEntity<TestRequestNoEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonBAPListEntity<TestRequestNoEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonBAPListEntity<TestRequestNoEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonBAPListEntity<TestRequestNoEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getTestRequestNoSuccess(entity.data);
                }else {
                    getView().getTestRequestNoFailed(entity.msg);
                }
            }
        }));
    }

    private String getViewCode(int type){
        String viewCode = "";
        if (type == 1){
            viewCode = "manuInspectRef";
        }else if (type == 2){
            viewCode = "purchInspectRef";
        }else if (type == 3){
            viewCode = "otherInspectRef";
        }
        return viewCode;
    }
}
