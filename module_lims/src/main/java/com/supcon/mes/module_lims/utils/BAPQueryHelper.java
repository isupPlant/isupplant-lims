package com.supcon.mes.module_lims.utils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.SubcondEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.supcon.mes.middleware.constant.Constant.BAPQuery.BE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.BOOLEAN;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.FALSE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.GE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE_OPT_BLUR;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE_OPT_Q;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.STAFF_NAME;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.SYSTEMCODE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TEXT;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TRUE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TYPE_NORMAL;

/**
 * author huodongsheng
 * on 2020/7/7
 * class name
 */
public class BAPQueryHelper {

    public static FastQueryCondEntity createSingleFastQueryCond(Map<String, Object> queryMap) {

        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
        List<BaseSubcondEntity> subcondEntities = new ArrayList<>();

        for (String key : queryMap.keySet()) {

            if (queryMap.get(key) == null) {
                continue;
            }
            BaseSubcondEntity baseSubcondEntity = parseKey(key, queryMap.get(key));
            subcondEntities.add(baseSubcondEntity);
        }

        fastQueryCondEntity.subconds = subcondEntities;

        return fastQueryCondEntity;
    }

    private static BaseSubcondEntity parseKey(String key, Object value) {
        SubcondEntity subcondEntity = null;
        switch (key) {
            case Constant.BAPQuery.ID:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = Constant.BAPQuery.LONG;
                subcondEntity.operator = BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.CODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = Constant.BAPQuery.BAPCODE;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.SAMPLE_STATE:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = Constant.BAPQuery.SYSTEMCODE;
                subcondEntity.operator = BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = Util.getSampleState(String.valueOf(value));
                break;

            case Constant.BAPQuery.BUSI_VERSION: //版本号
            case Constant.BAPQuery.NAME:
            case STAFF_NAME:
            case Constant.BAPQuery.SPECIFY:
            case Constant.BAPQuery.DELIVER_CODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;


            case Constant.BAPQuery.TABLE_NO:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.TABLE_NO;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.TICKET_NO:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.TICKET_NO;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.BATCH_CODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.BATCH_CODE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.BATCH_TEXT:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.BATCH_TEXT;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.MODEL:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "MODEL";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAP_REQUEST_PARAM.START_TIME:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "APPLY_TIME";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.OUT_DATE_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.OUT_STORAGE_DATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATE;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.OUT_DATE_END:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.OUT_STORAGE_DATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATE;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.IN_DATE_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.IN_STORAGE_DATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATE;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.IN_DATE_END:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.IN_STORAGE_DATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATE;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.START_TIME:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "AJUST_TIME";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.END_TIME:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "AJUST_TIME";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAP_REQUEST_PARAM.END_TIME:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "APPLY_TIME";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAP_REQUEST_PARAM.WORK_TYPE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "WORK_TYPE";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = SYSTEMCODE;
                subcondEntity.operator = BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAP_REQUEST_PARAM.PLAN_STATE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = SYSTEMCODE;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAP_REQUEST_PARAM.PLAN_STATE_GE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAP_REQUEST_PARAM.PLAN_STATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = SYSTEMCODE;
                subcondEntity.operator = BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.XJ_START_TIME_1:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "START_TIME";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.XJ_START_TIME_2:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "START_TIME";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.XJ_END_TIME_1:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "END_TIME";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.XJ_END_TIME_2:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "END_TIME";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            /**
             * 维修工单筛选信息
             */
            case Constant.BAPQuery.INVENTORY_STATUS:  // 盘点状态
            case Constant.BAPQuery.WORK_RECORD_SOURCE:
            case Constant.BAPQuery.WORK_RECORD_STATE:
            case Constant.BAPQuery.STATE: // 设备状态
            case Constant.BAPQuery.FAULT_INFO_TYPE:
            case Constant.BAPQuery.EXE_STATE: // 执行状态
            case Constant.BAPQuery.RECORD_TYPE: // 工单待办记录类型
            case Constant.BAPQuery.FORMULA_SET_PROCESS:  // 配方属性
            case Constant.BAPQuery.XJ_TASK_STATE:  // 巡检任务状态
            case Constant.BAPQuery.WARE_RECORD:
            case Constant.BAPQuery.PRODUCE_BATCH_NUM: // 生产批号
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = TEXT;
                subcondEntity.operator = BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.IS_MORE_OTHER:   // 其他活动
            case Constant.BAPQuery.IS_FOR_TEMP:     // 机动活动
            case Constant.BAPQuery.IS_FOR_ADJUST:   // 调整活动
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = BOOLEAN;
                subcondEntity.operator = BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                if ("false".equals(String.valueOf(value))) {
                    subcondEntity.value = FALSE;
                } else if ("true".equals(String.valueOf(value))) {
                    subcondEntity.value = TRUE;
                }
                break;

            case Constant.BAPQuery.XJ_ID:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = Constant.BAPQuery.ID;
                subcondEntity.dbColumnType = Constant.BAPQuery.LONG;
                subcondEntity.operator = BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;


            case Constant.BAPQuery.REGION_NAME: // 区域位置
            case Constant.BAPQuery.CONTENT:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.PD_DATE_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.INVENTORY_DATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATE;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.PD_DATE_END:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.INVENTORY_DATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATE;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.APPLY_DATE_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName ="APPLY_DATE";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATE;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.APPLY_DATE_END:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName ="APPLY_DATE";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATE;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;



            default:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = SYSTEMCODE;
                subcondEntity.operator = BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

        }

        return subcondEntity;
    }
}
