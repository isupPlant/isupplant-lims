package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.SubcondEntity;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_lims.model.bean.WareStoreEntity;
import com.supcon.mes.module_sample.model.contract.WareStoreRefContract;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE_OPT_BLUR;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TYPE_NORMAL;

/**
 * Created by wanghaidong on 2021/2/2
 * Email:wanghaidong1@supcon.com
 * desc:
 */
public class WareStoreRefPresenter extends WareStoreRefContract.Presenter {
    @Override
    public void getWareStoreRefInfo(int pageNo,Map<String, Object> params) {

        String viewCode = "BaseSet_1.0.0_warehouse_storeSetFilterRef";
        String modelAlias = "storeSet";
        Map<String, Object> map = new HashMap<>();
        try {
            if (!params.isEmpty()) {
                FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
                fastQueryCondEntity.subconds=new ArrayList<>();
                if (params.containsKey(Constant.BAPQuery.CODE)){
                    fastQueryCondEntity.subconds.add(parseKey(Constant.BAPQuery.CODE,params.get(Constant.BAPQuery.CODE)));
                }
                if (params.containsKey(Constant.BAPQuery.NAME)){
                    fastQueryCondEntity.subconds.add(parseKey(Constant.BAPQuery.NAME,params.get(Constant.BAPQuery.NAME)));
                }
                fastQueryCondEntity.viewCode = viewCode;
                fastQueryCondEntity.modelAlias = modelAlias;
                map.put("fastQueryCond",fastQueryCondEntity.toString());
            }
            Map<String, Object> customCondition=new HashMap();
            customCondition.put("wareClassCode","Lims_WMS");
            map.put("customCondition",customCondition);
            map.put("permissionCode","BaseSet_1.0.0_warehouse_storeSetFilterRef");
            map.put("pageNo",pageNo);
            map.put("pageSize",20);
            map.put("paging",true);

            mCompositeSubscription.add(
                    SampleHttpClient.getWareStoreRefInfo(map)
                                    .onErrorReturn(throwable -> {
                                        BAP5CommonEntity<CommonListEntity<WareStoreEntity>> entity=new BAP5CommonEntity<>();
                                        entity.success=false;
                                        entity.msg= ErrorMsgHelper.msgParse(throwable.getMessage());
                                        return entity;
                                    })
                                    .subscribe(new Consumer<BAP5CommonEntity<CommonListEntity<WareStoreEntity>>>() {
                                        @Override
                                        public void accept(BAP5CommonEntity<CommonListEntity<WareStoreEntity>> entity) throws Exception {
                                            if (entity.success){
                                                getView().getWareStoreRefInfoSuccess(entity.data.result);
                                            }else {
                                                getView().getWareStoreRefInfoFailed(entity.msg);
                                            }
                                        }
                                    })

            );

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private  BaseSubcondEntity parseKey(String key, Object value) {
        SubcondEntity subcondEntity = null;
        switch (key) {
            case Constant.BAPQuery.CODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = Constant.BAPQuery.CODE;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.NAME:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
        }
        return subcondEntity;
    }
}
