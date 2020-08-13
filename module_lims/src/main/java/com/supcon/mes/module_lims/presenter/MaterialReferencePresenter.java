package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.module_lims.model.bean.MaterialReferenceListEntity;
import com.supcon.mes.module_lims.model.contract.MaterialReferenceApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/9
 * class name
 */
public class MaterialReferencePresenter extends MaterialReferenceApi.Presenter {
    @Override
    public void getMaterialReferenceList(int pageNo, Map<String, Object> params) {
        String viewCode = "BaseSet_1.0.0_material_materialRefLayout";
        String modelAlias = "material";
        FastQueryCondEntity fastQuery;

        fastQuery = BAPQueryHelper.createSingleFastQueryCond(params);
        fastQuery.viewCode = viewCode;
        fastQuery.modelAlias = modelAlias;

        Map<String, Object> map = new HashMap<>();
        if (params.size()>0){
            map.put("fastQueryCond",fastQuery.toString());
        }
        map.put("pageNo",pageNo);
        map.put("paging",true);
        map.put("customCondition",new MaterialReferenceListEntity());
        map.put("pageSize",10);

        mCompositeSubscription.add(BaseLimsHttpClient.getMaterialReference(map).onErrorReturn(new Function<Throwable, MaterialReferenceListEntity>() {
            @Override
            public MaterialReferenceListEntity apply(Throwable throwable) throws Exception {
                MaterialReferenceListEntity entity = new MaterialReferenceListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<MaterialReferenceListEntity>() {
            @Override
            public void accept(MaterialReferenceListEntity entity) throws Exception {
                if (entity.success){
                    getView().getMaterialReferenceListSuccess(entity);
                }else {
                    getView().getMaterialReferenceListFailed(entity.msg);
                }
            }
        }));
    }
}
