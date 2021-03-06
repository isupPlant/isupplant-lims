package com.supcon.mes.module_lims.presenter;


import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.SubcondEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.SerialDeviceEntity;
import com.supcon.mes.module_lims.model.contract.SerialDeviceRefContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.supcon.mes.middleware.constant.Constant.BAPQuery.BE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE_OPT_Q;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TYPE_NORMAL;

/**
 * author huodongsheng
 * on 2021/1/11
 * class name
 */
public class SerialDeviceRefPresenter extends SerialDeviceRefContract.Presenter {
    @Override
    public void getSerialDeviceRef(int pageNo, Map<String, Object> map) {
        String viewCode = "BaseSet_1.0.0_eamInfo_serialRef";
        String modeAlias = "eamInfo";
        FastQueryCondEntity fastQuery = null;
        if (map.size() > 0){
            fastQuery = BAPQueryHelper.createSingleFastQueryCond(map);
            fastQuery.viewCode = viewCode;
            fastQuery.modelAlias = modeAlias;
        }
        if (fastQuery==null){
            fastQuery=new FastQueryCondEntity();
        }
        if (fastQuery.subconds==null)
            fastQuery.subconds=new ArrayList<>();
        SubcondEntity subcondEntity = new SubcondEntity();
        subcondEntity.type = TYPE_NORMAL;
        subcondEntity.columnName = "SERIAL_TYPE";
        subcondEntity.dbColumnType = Constant.BAPQuery.SYSTEMCODE;
        subcondEntity.operator = BE;
        subcondEntity.paramStr = LIKE_OPT_Q;
        subcondEntity.value = "BaseSet_serialType/serial";
        fastQuery.subconds.add(subcondEntity);

        Map<String, Object> params = new HashMap<>();
        params.put("pageNo",pageNo);
        params.put("pageSize",10);
        params.put("paging",true);
        params.put("fastQueryCond",fastQuery.toString());

        mCompositeSubscription.add(BaseLimsHttpClient.getSerialDeviceRef(params).onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonBAPListEntity<SerialDeviceEntity>>>() {
            @Override
            public BAP5CommonEntity<CommonBAPListEntity<SerialDeviceEntity>> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<CommonBAPListEntity<SerialDeviceEntity>>>() {
            @Override
            public void accept(BAP5CommonEntity<CommonBAPListEntity<SerialDeviceEntity>> entity) throws Exception {
                if (entity.success){
                    getView().getSerialDeviceRefSuccess(entity.data);
                }else {
                    getView().getSerialDeviceRefFailed(entity.msg);
                }
            }
        }));

    }
}
