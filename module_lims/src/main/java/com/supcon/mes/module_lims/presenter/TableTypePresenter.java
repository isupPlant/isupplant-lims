package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.TableTypeIdEntity;
import com.supcon.mes.module_lims.model.contract.TableTypeContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/10/28
 * class name
 */
public class TableTypePresenter extends TableTypeContract.Presenter {

    @Override
    public void getTableTypeByCode(String code) {
        mCompositeSubscription.add(BaseLimsHttpClient.getTableTypeByCode(code).onErrorReturn(new Function<Throwable, BAP5CommonEntity<TableTypeIdEntity>>() {
            @Override
            public BAP5CommonEntity<TableTypeIdEntity> apply(Throwable throwable) throws Exception {
                BAP5CommonEntity entity = new BAP5CommonEntity();
                entity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<BAP5CommonEntity<TableTypeIdEntity>>() {
            @Override
            public void accept(BAP5CommonEntity<TableTypeIdEntity> entity) throws Exception {
                if (entity.success){
                    getView().getTableTypeByCodeSuccess(entity.data);
                }else {
                    getView().getTableTypeByCodeFailed(entity.msg);
                }
            }
        }));
    }

}
