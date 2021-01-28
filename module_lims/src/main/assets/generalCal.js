
/*
 * 判断值是否为数字，不是数字则按默认值赋值；
 */
function nvl(value,defaultValue){
	//return formulajs.NVL(value,defaultValue);
	if(isNaN(value)){
		return defaultValue;
	}else{
		return value;
	}
}

//############################################################
// 修约函数 dData 要修约的数， dPrecision 修约间隔，    add by zhengyongli on 2014/2/20
// 添加 carryRule 修约规则，1四舍五入2四舍六入五成双3去尾法4进一法    add by hfq  2016/04/12
//############################################################
//此方法已修改为 carrySpaceDataCarry（见551行）
function dataCarry(dData,dPrecision,carryRule) {
	if(carryRule == undefined) {
		carryRule = 'LIMSBasicCarryRule/1';
	}

	var tempData;
	var tempstr;
	var pos = 0;
	var intPart;
	var decPart;
	var res;
	var tmpFlag = true; ////正负数的判断的标志  true为正数
	tmpFlag = (dData >= 0);
	dData = Math.abs(dData);
	if (dData == null || dData.length==0) return '';

	tempData = accDiv(dData,dPrecision);
	tempstr = String(tempData);
	pos = tempstr.indexOf(".");

	if(pos != -1) {
		intPart = Number(tempstr.substr(0,pos));
		decPart = Number(tempstr.substr(pos));

		if(carryRule == 'LIMSBasicCarryRule/3') {
			tempData = intPart;
		} else if(carryRule == 'LIMSBasicCarryRule/4') {
			tempData = intPart + 1;
		} else {
			if(decPart > 0.5) {
				tempData = intPart + 1;
			}

			if(decPart == 0.5) {
				if(carryRule == 'LIMSBasicCarryRule/1') {
					tempData = intPart + 1;
				} else if (carryRule == 'LIMSBasicCarryRule/2') {
					if(intPart % 2 == 1) {
						tempData = intPart + 1;
					} else {
						tempData = intPart;
					}
				}
			}

			if(decPart < 0.5) {
				tempData = intPart;
			}
		}
	}

	res = accMul(tempData, dPrecision);
	if(!tmpFlag) {
		res = res * -1;
	}

	return res;
}
//根据修约规则获取修约值
function getCarryRuleValue(originValue,carrySpace,carryRule,digitType){
  	var newValueResult = "";
	if(digitType=='LIMSBasicDigitType/carrySpace'){
		newValueResult=dataCarry(originValue,carrySpace,carryRule);  //计算修约值
		var decLength = 0;
		if(carrySpace.indexOf(".")!=-1){
			decLength = carrySpace.substring(carrySpace.indexOf(".")+1).length;
		}
		return parseFloat(newValueResult).toFixed(decLength);
	}else if(digitType=='LIMSBasicDigitType/significanceDigit'){ //有效数字
		var decLength = 0;
		var originValueNew  = originValue;
		do{  //循环乘，直到没小数
			decLength--;
		} while((originValueNew = accMul(originValueNew,10)).toString().indexOf('.')!=-1);
		decLength += originValueNew.toString().length;//最高位数
		var fixed = parseInt(decLength)-parseInt(carrySpace);
		carrySpaceNew = Math.pow(10,fixed);   //计算修约间隔
		newValueResult=dataCarry(originValue,carrySpaceNew,carryRule);  //计算修约值
		return parseFloat(newValueResult).toFixed(fixed<0?-fixed:0);
	}else if(digitType=='LIMSBasicDigitType/decLength'){ //小数位数
		var carrySpaceNew = Math.pow(10,-1*parseInt(carrySpace));         //小数位数转化为修约间隔，如小数位数为2就转化为0.01
		newValueResult=dataCarry(originValue,carrySpaceNew,carryRule);  //计算修约值
		return parseFloat(newValueResult).toFixed(carrySpace); //修约值赋值
	}
}

//后台调用，判断合格范围等级
function getDispValueLevel(indexStandList,indexId,indexName,dispValue,standId,valueKind,breakStr) {
  	//特殊结果判定
  	var newdispValue = ','+dispValue+',';
  	if(breakStr!=null && breakStr!=""){
    	breakStr=','+breakStr+',';
      	if(breakStr.indexOf(newdispValue)!=-1){
         	return "inBreakStr";
        }
    }
	if((indexId == null && indexName == null) || dispValue == "" || dispValue == null ){
		return "";
	}
	var len = indexStandList.length; // dataObj1从ftl中获取
	var result = "";
	for (i = 0; i < len; i++) {
		if(indexStandList[i].standID.id==standId){
			var standLevel = indexStandList[i].standLevel.standLevel;
			var judgeCond = indexStandList[i].judgeCond+'';
			//结果录入根据分析项目ID比较，报告单根据报告名称比较
			if ((indexId != null && indexStandList[i].standItemIndexId.itemIndexID.id == indexId)||(indexName != null && indexStandList[i].standItemIndexId.reportedName == indexName)) {
				//不合格等级判定条件为空，所有等级范围都不符合时，则结论为不合格
				if (null != judgeCond && judgeCond != undefined && judgeCond != "" && judgeCond != "null") {
					//值类型为计算和数值时，判等时去掉<>号，兼容报告单页面判断
					if(valueKind == "LIMSBasicValueKind/2" || valueKind == "LIMSBasicValueKind/4" || valueKind == "LIMSBasicVKIndex/2") {
						dispValue = dispValue.replace(/[<>]/g, '');
					}
					//var reg = /^[-]?[0-9]+.?[0-9]*$/;
					if (!isNaN(dispValue)) { //数值类型
						//取代
						var replace = judgeCond.replace(/\[Result]/g, dispValue);
					} else { //字符类型
						var replace = judgeCond.replace(/\[Result]/g, "'" + dispValue + "'"); //加引号
					}
					//执行判断，如果报错则返回空，例如3% < 0
					try{
						eval(replace)
					} catch(err){
						return "";
					}
					if(true==eval(replace)){
						return standLevel;
					}
				} else {
					return standLevel;
				}
			}
		}
	}
	return result;
}
//除法函数，用来得到精确的除法结果
//说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
//调用：accDiv(arg1,arg2)
//返回值：arg1除以arg2的精确结果
function accDiv(arg1,arg2){
	arg1=subparam(arg1);
	arg2=subparam(arg2);
	return formulajs.DIVIDE(arg1,arg2);
	/*var t1=0,t2=0,r1,r2;
	try{t1=arg1.toString().split(".")[1].length}catch(e){}
	try{t2=arg2.toString().split(".")[1].length}catch(e){}
	with(Math){
		r1=Number(arg1.toString().replace(".",""))
		r2=Number(arg2.toString().replace(".",""))
		return accMul((r1/r2),pow(10,t2-t1));
	}*/
}
//乘法函数，用来得到精确的乘法结果
//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
//调用：accMul(arg1,arg2)
//返回值：arg1乘以 arg2的精确结果
function accMul(arg1,arg2){
	arg1=subparam(arg1);
	arg2=subparam(arg2);
	return formulajs.MULTIPLY(arg1,arg2);
	/*var m=0,s1=arg1.toString(),s2=arg2.toString();
	try{m+=s1.split(".")[1].length}catch(e){}
	try{m+=s2.split(".")[1].length}catch(e){}
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)*/
}
//加法函数，用来得到精确的加法结果
function add(arg1,arg2){
	arg1=subparam(arg1);
	arg2=subparam(arg2);
	return formulajs.ADD(arg1,arg2);
}
//减法函数，用来得到精确的加法结果
function minus(arg1,arg2){
	arg1=subparam(arg1);
	arg2=subparam(arg2);
	return formulajs.MINUS(arg1,arg2);
}
//减法函数，用来得到精确的减法结果
function minus(arg1,arg2){
	return formulajs.MINUS(arg1,arg2);
}
//求平均值函数，用来得到精确的平均值结果
function ave(range){
	if(range.length === 1){
		return range[0];
	}
	return formulajs.AVERAGE(range);
}
//绝对值函数，用来得到精确的绝对值结果
function abs(arg1){
	return formulajs.ABS(arg1);
}

//三角函数sin
function sin(arg1){
	return formulajs.SIN(arg1);
}
//三角函数cos
function cos(arg1){
	return formulajs.COS(arg1);
}
//三角函数tan
function tan(agr1){
	return formulajs.TAN(agr1);
}
//标准偏差
function stdev(range){
	return formulajs.STDEV.S(range);
}
//基于以参数形式给出的整个样本总体计算标准偏差
function stdevp(range){
	return formulajs.STDEV.P(range);
}
//相对偏差
function rstdev(range){
	var a1=formulajs.STDEV.S(range);
	var a2=formulajs.AVERAGE(range);
	a1=subparam(a1);
	a2=subparam(a2);
	return accDiv(formulajs.STDEV.S(range),formulajs.AVERAGE(range));
}


function linearRegression(data){
    var xsum=new Array();//x的多项和
    var ysum=new Array();//y的多项和

    for(var i=0;i<data.length;i++){
        xsum[i]=data[i].x;
        ysum[i]=data[i].y;
    }
    var xmean=ave(xsum);//x的平均数
    var ymean=ave(ysum);//y的平均数
    var num=0;//多项式和【(x-x的均值)*(y-y的均值)】
    var den=0;//多项式和【(x-x的均值)*(x-x的均值)】
    for(var i=0;i<data.length;i++){
        var x=data[i].x;
        var y=data[i].y;
        num+=accMul((x-xmean),(y-ymean));
        den+=accMul((x-xmean),(x-xmean));
    }
    a=accDiv(num,den);//y=ax+b 的 系数a
    b=ymean-accMul(a,xmean);//y=ax+b 的 系数b
	return "y="+a+"x+"+b;
}

function linearRegression_a(xArray,yArray){
    var xmean=ave(xArray);//x的平均数
    var ymean=ave(yArray);//y的平均数
    var num=0;//多项式和【(x-x的均值)*(y-y的均值)】
    var den=0;//多项式和【(x-x的均值)*(x-x的均值)】
    for(var i=0;i<xArray.length;i++){
        var x=xArray[i];
        var y=yArray[i];
        num+=accMul((x-xmean),(y-ymean));
        den+=accMul((x-xmean),(x-xmean));
    }
	var b=accDiv(num,den);//y=a+bx 的 系数b
    var a=ymean-accMul(b,xmean);//y=a+bx 的 系数a
	return a;
}

function linearRegression_b(xArray,yArray){
    var xmean=ave(xArray);//x的平均数
    var ymean=ave(yArray);//y的平均数
    var num=0;//多项式和【(x-x的均值)*(y-y的均值)】
    var den=0;//多项式和【(x-x的均值)*(x-x的均值)】
    for(var i=0;i<xArray.length;i++){
        var x=xArray[i];
        var y=yArray[i];
        num+=accMul((x-xmean),(y-ymean));
        den+=accMul((x-xmean),(x-xmean));
    }
	var b=accDiv(num,den);//y=a+bx 的 系数b
    var a=ymean-accMul(b,xmean);//y=a+bx 的 系数a
	return b;
}

//自然对数
function ln(arg1){
	return formulajs.LN(arg1);
}
//以指定数为底的对数
//说明：第二个参数为底
function log(arg1,arg2){
	return formulajs.LOG(arg1,arg2);
}
//常用对数
function log10(arg1){
	return formulajs.LOG10(arg1);
}
//以e为底的指数
function exp(arg1){
	return formulajs.EXP(arg1);
}
//乘方
//说明：第二个参数为指数
function power(arg1,arg2){
	return formulajs.POWER(arg1,arg2);
}
//平方根
function sqrt(arg1){
	return formulajs.SQRT(arg1);
}
//反正弦
function asin(arg1){
	return formulajs.ASIN(arg1);
}
//反余弦
function acos(arg1){
	return formulajs.ACOS(arg1);
}
//反正切
function atan(arg1){
	return formulajs.ATAN(arg1);
}
//反余切
function acot(arg1){
	return formulajs.ACOT(arg1);
}
//求和
function sum(range){
  	if(range === undefined || range === null){
       return Error;
    }
	return formulajs.SUM(range);
}

//最大值
function max(range){
	return formulajs.MAX(range);
}
//最小值
function min(range){
	return formulajs.MIN(range);
}
//按递减顺序取第k个值
function large(range,k){
	return formulajs.LARGE(range,k);
}
//按递增顺序取第k个值
function small(range,k){
	return formulajs.SMALL(range,k);
}
//某数字在一列数字中相对于其他数值的大小排名，排名相同则返回平均值排名,order为false表示降序，默认为降序
function rank_avg(arg1,range,order){
	return formulajs.RANK.AVG(arg1,range,order);
}
//某数字在一列数字中相对于其他数值的大小排名,排名相同则返回最佳排名,order为false表示降序，默认为降序
function rank_eq(arg1,range,order){
	return formulajs.RANK.EQ(arg1,range,order);
}
//阶乘
function fact(arg1){
	return formulajs.FACT(arg1);
}
//求除法的商,arg1为被除数,arg2为除数
function quotient(arg1,arg2){
	return formulajs.QUOTIENT(arg1,arg2);
}
//求除法的余数,arg1为被除数,arg2为除数
function mod(arg1,arg2){
	return formulajs.MOD(arg1,arg2);
}
//圆周率π
function pi(){
	return formulajs.PI();
}
//四舍五入,digit为小数位数
function round(arg1,digit){
	return formulajs.ROUND(arg1,digit);
}
//沿绝对值增大方向舍入
function roundup(arg1,digit){
	return formulajs.ROUNDUP(arg1,digit);
}
//沿绝对值减小方向舍入
function rounddown(arg1,digit){
	return formulajs.ROUNDDOWN(arg1,digit);
}

function subparam(arg1){
	var t1=0;
	try{t1=arg1.toString().split(".")[1].length}catch(e){}

  	//hfq 2020-12-09 从7位改为16位
	if(t1 > 16){
		arg1=arg1.toString().substr(0,arg1.toString().indexOf('.')+17);
	}
	return arg1;
}

 function linearRegression_r2(xArray, yArray) {
	if (xArray.length != yArray.length) {
		return false;
	}
	var xmean=ave(xArray);//x的平均数
    var ymean=ave(yArray);//y的平均数
	var n = xArray.length;
	// 第二次计算，求出方差
	var xxbar = 0;
	var yybar = 0;
	var xybar = 0;
	for (var i = 0; i < n; i++) {
		xxbar += accMul(minus(xArray[i],xmean),minus(xArray[i],xmean));
		yybar += accMul(minus(yArray[i],ymean),minus(yArray[i],ymean));
		xybar += accMul(minus(xArray[i],xmean),minus(yArray[i],ymean));
	}
	slope  = accDiv(xybar,xxbar);  //求偏导数的过程
	intercept = minus(ymean,accMul(slope,xmean));
	// 其他的统计数据
	var rss = 0;      // residual sum of squares
	var ssr = 0;      // regression sum of squares
	for (var i = 0; i < n; i++) {
		var fit = add(accMul(slope,xArray[i]) ,intercept);
		rss += accMul(minus(fit,yArray[i]),minus(fit,yArray[i]));
		ssr += accMul(minus(fit,ymean),minus(fit,ymean));
	}
	//var degreesOfFreedom = n-2;
	r2  = accDiv(ssr ,yybar);
	return r2;
	//var svar  = rss / degreesOfFreedom;
	//svar1 = svar / xxbar;
	//svar0 = svar/n + xmean*xmean*svar1;
}
//判断是否在最大最小范围内
function inMaxAndMin(selectRow, originValue,widget) {
	var maxValue = widget.getCellValue(selectRow, "maxValue");
	var minValue = widget.getCellValue(selectRow, "minValue");
  	var limitType = widget.getCellValue(selectRow, "limitType.id");//检出限类别

  	if(limitType=="LIMSBasic_LIMSBasicLimitType/add"){//检出限为添加
        if (maxValue != null && maxValue.length != 0) {
            if (parseFloat(maxValue) < parseFloat(originValue)) {
                widget.setCellValue(selectRow, "dispValue", ">" + maxValue);
                return "addmore";
            }
        }
        if (minValue != null && minValue.length != 0) {
            if (parseFloat(minValue) > parseFloat(originValue)) {
                widget.setCellValue(selectRow, "dispValue", "<" + minValue);
                return "addless";
            }
        }
    }else if(limitType=="LIMSBasic_LIMSBasicLimitType/replace"){//检出限为替换
		if (maxValue != null && maxValue.length != 0) {
          	var maxValueArr = maxValue.split(",");
            if (parseFloat(maxValueArr[0]) < parseFloat(originValue)) {
                widget.setCellValue(selectRow, "dispValue", maxValueArr[1]);
                return "replacemore";
            }
        }
        if (minValue != null && minValue.length != 0) {
          	var minValueArr = minValue.split(",");
            if (parseFloat(minValueArr[0]) > parseFloat(originValue)) {
                widget.setCellValue(selectRow, "dispValue", minValueArr[1]);
                return "replaceless";
            }
        }
    }else if(limitType=="LIMSBasic_LIMSBasicLimitType/reject"){//检出限为拒绝
		if (maxValue != null && maxValue.length != 0) {
            if (parseFloat(maxValue) < parseFloat(originValue)) {

              	//errorBarWidget.showMessage("${getText('LIMSBasic.custom.inmaxminerror070101')}","f");

                widget.setCellValue(selectRow, "dispValue", "");
              	widget.setCellValue(selectRow, "roundValue", "");
              	widget.setCellValue(selectRow, "originalValue", "");
                return "rejectmore";
            }
        }
        if (minValue != null && minValue.length != 0) {
            if (parseFloat(minValue) > parseFloat(originValue)) {
              	//errorBarWidget.showMessage("${getText('LIMSBasic.custom.inmaxminerror070101')}","f");
               	widget.setCellValue(selectRow, "dispValue", "");
              	widget.setCellValue(selectRow, "roundValue", "");
              	widget.setCellValue(selectRow, "originalValue", "");
                return "rejectless";
            }
        }
    }
  	return "pass";
}
//根据检出限进行原始值判断（后台调用）
function autoCastDispValue(originValue,carrySpace,carryRule,digitType,limitType,maxValue,minValue){
  	var newDispValue = originValue;
    if(limitType!=null && limitType!="" ){
         if(limitType=="LIMSBasic_LIMSBasicLimitType/add"){//检出限为添加
            if (maxValue != null && maxValue.length != 0) {
                if (parseFloat(maxValue) < parseFloat(originValue)) {
                    newDispValue = ">" + maxValue;
                    return newDispValue;
                }
            }
            if (minValue != null && minValue.length != 0) {
                if (parseFloat(minValue) > parseFloat(originValue)) {
                    newDispValue = "<" + minValue;
                    return newDispValue;
                }
            }
        }else if(limitType=="LIMSBasic_LIMSBasicLimitType/replace"){
            if (maxValue != null && maxValue.length != 0) {
                var maxValueArr = maxValue.split(",");
                if (parseFloat(maxValueArr[0]) < parseFloat(originValue)) {
                    newDispValue = maxValueArr[1];
                    return newDispValue;
                }
            }
            if (minValue != null && minValue.length != 0) {
                var minValueArr = minValue.split(",");
                if (parseFloat(minValueArr[0]) > parseFloat(originValue)) {
                    newDispValue = minValueArr[1];
                    return newDispValue;
                }
            }
        }else if(limitType=="LIMSBasic_LIMSBasicLimitType/reject"){
            if (maxValue != null && maxValue.length != 0) {
                if (parseFloat(maxValue) < parseFloat(originValue)) {
                    return "reject";
                }
            }
            if (minValue != null && minValue.length != 0) {
                if (parseFloat(minValue) > parseFloat(originValue)) {
                    return "reject";
                }
            }
        }
    }
  	//结果正常，输出修约值
	newDispValue = getCarryRuleValue(originValue,carrySpace,carryRule,digitType);
  	 return newDispValue;
}

//======================================================== 以上是旧版本的函数，部分函数无效，需要重新写================

//======================================================== 以下是5.0版本新增的函数==========================================

/**
*	修约函数 dData 要修约的数， dPrecision 修约间隔，add by zhengyongli on 2014/2/20
* 	添加 carryRule 修约规则：1四舍五入2四舍六入五成双3去尾法4进一法
*	rounding:四舍五入;
*	sqlFunc:四舍六入五成双;
*	roundingDown:去尾法;
*	roundingUp进一法    add by hfq  2016/04/12
**/
function carrySpaceDataCarry(dData,dPrecision,carryRule) {
	dPrecision = dPrecision.toString();
	//定位方式为修约间隔
	var newValueResult = carrySpaceData(dData, parseFloat(dPrecision), carryRule);
	var decLength = 0;
	if(dPrecision.indexOf(".")!=-1){
		decLength = dPrecision.substring(dPrecision.indexOf(".")+1).length;
	}
	return parseFloat(newValueResult).toFixed(decLength);
}


function carrySpaceData(dData,dPrecision,carryRule) {
	var tempData;
	var tempstr;
	var pos = 0;
	var intPart;
	var decPart;
	var res;
	var tmpFlag = true; ////正负数的判断的标志  true为正数
	tmpFlag = (dData >= 0);
	dData = Math.abs(dData);//绝对值
	if (dData == null || dData.length==0) return '';

	tempData = accDiv(dData,dPrecision);//除法
	tempstr = String(tempData);
	pos = tempstr.indexOf(".");

	if(pos != -1) {
		intPart = Number(tempstr.substr(0,pos));
		decPart = Number(tempstr.substr(pos));

		if(carryRule == 'roundingDown') {
			tempData = intPart;
		} else if(carryRule == 'roundingUp') {
			tempData = intPart + 1;
		} else {
			if(decPart > 0.5) {
				tempData = intPart + 1;
			}

			if(decPart == 0.5) {
				if(carryRule == 'rounding') {
					tempData = intPart + 1;
				} else if (carryRule == 'sqlFunc') {
					if(intPart % 2 == 1) {
						tempData = intPart + 1;
					} else {
						tempData = intPart;
					}
				}
			}

			if(decPart < 0.5) {
				tempData = intPart;
			}
		}
	}

	res = accMul(tempData, dPrecision);
	if(!tmpFlag) {
		res = res * -1;
	}

	return res;
}

/**
*	修约函数 dData 要修约的数， decimalDigit 小数位数，add by huangrui on 2014/2/20
* 	添加 carryRule 修约规则：
*	rounding:	四舍五入;
*	sqlFunc:	四舍六入五成双;
*	roundingDown:	去尾法;
*	roundingUp:	进一法    add by hfq  2016/04/12
**/
function decLengthDataCarry(dData, decimalDigit, carryRule){
	var carrySpaceNew = Math.pow(10, -1 * parseInt(decimalDigit));
	carrySpaceNew = parseFloat(carrySpaceNew.toFixed(abs(decimalDigit)));
	//定位方式为小数位数
	var newValueResult = carrySpaceData(dData, carrySpaceNew, carryRule);
	return parseFloat(newValueResult).toFixed(parseInt(decimalDigit));
}

/**
*	修约函数 dData 要修约的数， significanceDigit 有效数字，add by huangrui on 2020/03/12
* 	添加 carryRule 修约规则：
*	rounding:	四舍五入;
*	sqlFunc:	四舍六入五成双;
*	roundingDown:	去尾法;
*	roundingUp:	进一法    add by hfq  2016/04/12
**/
function significanceDigitDataCarry(dData, significanceDigit, carryRule){
	var decLength = 0;
	var originValueNew  = dData;
	do{  //循环乘，直到没小数
		decLength--;
	} while((originValueNew = accMul(originValueNew,10)).toString().indexOf('.')!=-1);
	decLength += originValueNew.toString().length;//最高位数
	var fixed = parseInt(decLength)-parseInt(significanceDigit);
	var carrySpaceNew = Math.pow(10, fixed); //计算修约间隔
	carrySpaceNew = parseFloat(carrySpaceNew.toFixed(abs(fixed)));
	var newValueResult = carrySpaceData(dData, carrySpaceNew, carryRule);
	return parseFloat(newValueResult).toFixed(fixed<0?-fixed:0);
}


/**
* 原始值修约函数
* @param originValue 原始值
* @param digitType 定位方式（系统编码）
* @param carrySpace 修约间隔
* @param carryType 进位方式（系统编码）
* @param carryFormula 修约规则公式
**/
function roundingValue(originValue, digitType, carrySpace, carryType, carryFormula){
	//如果修约规则和自定义修约规则都为空，表示不需要修约
	if((digitType == null || carrySpace === null || carryType === null) && (carryFormula === null || carryFormula === "")){
		return originValue;
	}

	//如果修约规则公式不为空，表示自定义修约
	if(carryFormula != null && carryFormula != ""){
		var evalStr = "function impleRound(){ var result = " + originValue + ";" + carryFormula + "}";
		eval(evalStr);
      	return eval(impleRound());
	}else{
		//修约规则为空，使用修约规则修约

		if(digitType.id == "LIMSBasic_digitType/decLength"){
          	var carrySpaceNew = Math.pow(10, -1 * parseInt(carrySpace));
          	carrySpaceNew = parseFloat(carrySpaceNew.toFixed(abs(carrySpace)));
			//定位方式为小数位数
          	var newValueResult = carrySpaceData(originValue, carrySpaceNew, carryType.id.split("/")[1]);
          	return parseFloat(newValueResult).toFixed(parseInt(carrySpace));

		}else if(digitType.id == "LIMSBasic_digitType/carrySpace"){
			//定位方式为修约间隔
			var newValueResult = carrySpaceData(originValue, parseFloat(carrySpace), carryType.id.split("/")[1]);
			var decLength = 0;
            if(carrySpace.indexOf(".")!=-1){
                decLength = carrySpace.substring(carrySpace.indexOf(".")+1).length;
            }
          	return parseFloat(newValueResult).toFixed(decLength);
		}else if(digitType.id == "LIMSBasic_digitType/significanceDigit"){
          	var decLength = 0;
            var originValueNew  = originValue;
            do{  //循环乘，直到没小数
                decLength--;
            } while((originValueNew = accMul(originValueNew,10)).toString().indexOf('.')!=-1);
            decLength += originValueNew.toString().length;//最高位数
            var fixed = parseInt(decLength)-parseInt(carrySpace);
			var carrySpaceNew = Math.pow(10, fixed); //计算修约间隔
          	carrySpaceNew = parseFloat(carrySpaceNew.toFixed(abs(fixed)));
			var newValueResult = carrySpaceData(originValue, carrySpaceNew, carryType.id.split("/")[1]);
          	return parseFloat(newValueResult).toFixed(fixed<0?-fixed:0);
		}
	}
  	return null;
}

/**
* 高限限判断
* @param originValue 原始值
* @param limitType 检出限类别
* @param maxVal 检出高限
* @param minVal 检出低限
**/
function sectionJudgment(originValue, limitType, maxVal, minVal){
	if(limitType == "LIMSBasic_limitType/add"){
		//检出限类别为添加大于小于
		if(maxVal != null && originValue > parseFloat(maxVal)){
			//原始值大于检出高限
			return ">" + maxVal;
		}

		if(minVal != null && originValue < parseFloat(minVal)){
			//原始值小于检出低限
			return "<" + minVal;
		}
	}else if(limitType == "LIMSBasic_limitType/replace"){
		//检出限类别为替换
		if(maxVal != null){
			var index = maxVal.indexOf(",");
			//高限值
			var max = maxVal.substring(0,index);
			if(originValue > max){
				//原始值大于检出高限
				return maxVal.substring(index + 1);
			}
		}
		if(minVal != null){
			var index = minVal.indexOf(",");
			//低限值
			var min = minVal.substring(0,index);
			if(originValue < min){
				//原始值小于检出低限
				return minVal.substring(index + 1);
			}
		}

	}else if(limitType == "LIMSBasic_limitType/reject"){
		//检出限类别为拒绝
		if(maxVal != null && originValue > parseFloat(maxVal)){
			//原始值大于检出高限
			return "reject";
		}

		if(minVal != null && originValue < parseFloat(minVal)){
			//原始值小于检出低限
			return "reject";
		}
	}
	return originValue;
}

/**
* 结果等级判定
* @param value 值
* @param limits 等级范围
* @param specialResultStr 不参与判等的特殊结果
*
**/
function gradeDetermine(value, limits, specialResultStr, limitType){

	//对特殊结果判断的处理
	if(specialResultStr != null && specialResultStr != ""){
		var specialResultArr = specialResultStr.split(",");
		if(specialResultArr.indexOf(value + "") > -1){
			//报出值符合特殊结论
			return null;
		}
	}

  	if(limitType !== null && limitType === "LIMSBasic_limitType/add"){
       value = value + "";
       value = value.replace(/[<>]/g, '');
    }

	//是否符合等级标记，判等结束如果为false,表示不合格
	var gradeFlag = false;
	var otherRes = null;
	if(limits != null && limits.length > 0){
		var gradeSort = -1;
		for(var i = 0; i < limits.length; i++){
          	if(limits[i].judgeCond === null ){
            	otherRes = null;
              	gradeFlag = true;
              	continue;
            }
			var judgeCond = limits[i].judgeCond;
          	var str = new String(judgeCond);
			judgeCond = str.replace(/\[Result]/g, "resultVal");
			if(isNaN(value)){
				eval("var resultVal = '" + value + "'");
			}else{
				eval("var resultVal = " + value);
			}
			if(eval(judgeCond)){
				if(gradeSort == -1 || (gradeSort != -1 && limits[i].standardGrade.sort < gradeSort)){
					otherRes = limits[i].resultValue;
					gradeSort = limits[i].standardGrade.sort;
					gradeFlag = true;
				}
			}
		}
	}
	if(gradeFlag){
		return otherRes;
	}
	return limits[0].unQualifiedValue;
}