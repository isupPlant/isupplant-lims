package com.supcon.mes.module_sample.presenter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;

import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.PicUtil;
import com.supcon.mes.module_lims.model.bean.AttachmentSampleInputEntity;
import com.supcon.mes.module_lims.model.contract.FileUpContract;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.FileDataEntity;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * Created by wanghaidong on 2020/8/25
 * Email:wanghaidong1@supcon.com
 */
public class FileUpLoadPresenter extends FileUpContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void downloadFile(List<String> ids, List<String> fileNames) {
        List<AttachmentSampleInputEntity> localFilePaths = new ArrayList<>();
        Flowable.fromIterable(ids)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String id) throws Exception {
                        int index = ids.indexOf(id);
                        String fileName = fileNames.get(index);
                        mCompositeSubscription.add(
                                SampleHttpClient
                                        .getSampleInspectItemFile(id)
                                        .onErrorReturn(throwable -> {
                                            getView().downloadFileFailed(throwable.getMessage());
                                            return null;
                                        })
                                        .observeOn(Schedulers.io())
                                        .filter(responseBody -> responseBody != null)
                                        .map(new Function<ResponseBody, File>() {
                                            @Override
                                            public File apply(ResponseBody responseBody) throws Exception {
                                                File file = null;
                                                try {
                                                    String path = Environment.getExternalStorageDirectory() + "/lims/";
                                                    file = new File(path, fileName);
                                                    File parent = file.getParentFile();
                                                    if (!parent.exists()) {
                                                        parent.mkdirs();
                                                    }
                                                    if (!file.exists())
                                                        file.createNewFile();
                                                    FileOutputStream fos = new FileOutputStream(file);
                                                    InputStream inputStream = responseBody.byteStream();
                                                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                                                    byte[] buffer = new byte[1024];
                                                    int len;
                                                    while ((len = bis.read(buffer)) != -1) {
                                                        fos.write(buffer, 0, len);
                                                    }
                                                    fos.flush();
                                                    fos.close();
                                                    inputStream.close();
                                                    bis.close();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                return file;
                                            }
                                        })
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<File>() {
                                            @Override
                                            public void accept(File file) throws Exception {
                                                AttachmentSampleInputEntity entity=new AttachmentSampleInputEntity();
                                                entity.setId(id);
                                                entity.setName(fileName);
                                                entity.setFile(file);
                                                localFilePaths.add(entity);
                                                if (localFilePaths.size() == ids.size() && FileUpLoadPresenter.this.getView() != null) {
                                                    getView().downloadFileSuccess(localFilePaths);
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                getView().downloadFileFailed(throwable.getMessage());
                                            }
                                        })
                        );
                    }
                });
    }
}
