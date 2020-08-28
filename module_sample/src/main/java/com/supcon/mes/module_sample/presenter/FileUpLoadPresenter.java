package com.supcon.mes.module_sample.presenter;

import android.graphics.Bitmap;
import android.os.Environment;

import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.FormDataHelper;
import com.supcon.mes.middleware.util.PicUtil;
import com.supcon.mes.module_lims.model.contract.FileUpContract;
import com.supcon.mes.module_sample.model.bean.FileDataEntity;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

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
    @Override
    public void upFile(File file) {
        List<MultipartBody.Part> parts = FormDataHelper.createFileForm(file);
        mCompositeSubscription.add(
                SampleHttpClient.bapUploadFile(parts)
                        .onErrorReturn(new Function<Throwable, BAP5CommonEntity<FileDataEntity>>() {
                            @Override
                            public BAP5CommonEntity apply(Throwable throwable) throws Exception {
                                BAP5CommonEntity<FileDataEntity> bap5CommonEntity = new BAP5CommonEntity<FileDataEntity>();
                                bap5CommonEntity.success = false;
                                bap5CommonEntity.msg = throwable.getMessage();
                                return bap5CommonEntity;
                            }
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<FileDataEntity>>() {
                            @Override
                            public void accept(BAP5CommonEntity<FileDataEntity> result) throws Exception {
                                if (result.success) {
                                    getView().upFileSuccess(result);
                                } else {
                                    getView().upFileFailed("上传图片失败！");
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                getView().upFileFailed("上传图片失败！");
                            }
                        })
        );
    }

    @Override
    public void loadFile(String id,String fileName) {
        mCompositeSubscription.add(
                SampleHttpClient
                        .getSampleInspectItemFile(id)
                        .onErrorReturn(throwable -> {
                            getView().loadFileFailed(throwable.getMessage());
                            return null;
                        })
                        .observeOn(Schedulers.io())
                        .filter(responseBody -> responseBody != null)
                        .map(new Function<ResponseBody, File>() {
                            @Override
                            public File apply(ResponseBody responseBody) throws Exception {
                                File file=null;
                                try {
                                    String path = Environment.getExternalStorageDirectory() + "/";
                                    file = new File(path, fileName);
                                    if (file.exists())
                                        file.delete();
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
                                if (file != null && FileUpLoadPresenter.this.getView() != null) {
                                    getView().loadFileSuccess(file);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                getView().loadFileFailed(throwable.getMessage());
                            }
                        })
            );
    }
}
