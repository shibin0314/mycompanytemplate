/**
 * 配件模块相关功能
 */
var dmsPart = function() {
	var showOutPrice = function(priceType,container){
		var price="";
		//销售价
		if(priceType=="13111001"){
			price = $("#salesPrice",container).val();
		}
		//建议销售价
		if(priceType=="13111002"){
			price = $("#adviceSalePrice",container).val();
		}
		//索赔价
		if(priceType=="13111003"){
			price = $("#claimPrice",container).val();
		}
		//销售限价
		if(priceType=="13111004"){
			price = $("#limitPrice",container).val();
		}
		//成本单价
		if(priceType=="13111005"){
			price = $("#costPrice",container).val();
		}
		$("#price",container).val(price);
		var priceTie=$("#priceTieShow",container).val();
		result=parseFloat(price)*parseFloat(priceTie);
		if(isNaN(result)){
			result=price;
		}
		return result;
	};

	return {
		sample:function(priceType,container){
			return showOutPrice(priceType,container);
		}
		
	};
}();

/**
 * 维修模块相关功能
 */
var dmsRepair = function() {
	//加载维修项目下拉框数据
	var initLabourData = function(){
		var getTableRows = $("#labourList",getElementContext()).dmsTable().getTableRows();
		var repairPro = $("#labourList",getElementContext()).dmsTable().getRowDataByIndex();
		var selectData = new Array();
		for(var i=0;i<getTableRows;i++){
			selectData.push({id:repairPro[i].labourCode,name:repairPro[i].labourName});
		}
		return selectData;
	};
	//费用结算为查询到数据不可编辑
	var addDisabled = function(container){
		$(".disDiv a",container).setElementReadOnly();
		$("#remark",container).setElementReadOnly();
		$("#discountModeCode",container).setElementReadOnly(); 
		return "";
	};
	
	//费用结算查询到数据后可编辑
	var removeDisabled = function(){
		$(".disDiv a",getElementContext()).removeElementReadOnly();
		$("#remark",getElementContext()).removeElementReadOnly();
		$("#discountModeCode",getElementContext()).removeElementReadOnly(); 
	};
	return {
		initLabourData:function(){
			return initLabourData();
		},
		addDisabled:function(container){
			return addDisabled(container);
		},
		removeDisabled:function(){
			removeDisabled();
		}
	};
	
	
	
}();


/**
 * 客户模块模块相关功能
 */
var dmsCustomer = function() {
	
}();

/**
 * 管理模块相关功能
 */
var dmsManager = function() {
	var sample = function(){
		
	};
	
	return {
		sample:function(){
			sample();
		}
	};
}();

/**
 * 零售模块相关功能
 */
var dmsRetail = function() {
	var saleDatabindChange= function (tableObject,container){
		 var tab=$("#soDecrodateList tbody",container);
		 var tab2=$("#soDecrodatePartList tbody",container);
		 var tab3=$("#soServicesList tbody",container);
		   /***装潢材料******/
			//改变折扣率重置结算方式
			var calDecrodateDiscount = function(obj){
				var inputValue=$(obj).val();
				if(inputValue == 0 || inputValue == ""){
					
				}else{
					//$("select[id^=accountMode]",$(obj).closest("tr")).setDmsValue("");
				}
			}
			//通过下拉框数据实时变更左侧数值
			var calDecrodatePartItemAmount = function(obj){
				var selectValue = $(obj).val();
				if(selectValue =="14061003"||selectValue =="14061004"){
					$("input[id^=discount]",$(obj).closest("tr")).valChange("0");
				}
			}
			//填写序列号数值表格变成1并且不可编辑
			var calDecrodatePartItem = function(obj){
				var inputValue=$(obj).val();
				if(inputValue =="" || inputValue ==undefined){
				$("input[id^=partQuantity]",$(obj).closest("tr")).valChange("1");
				$("input[id^=partQuantity]",$(obj).closest("tr")).removeAttr("disabled");
				}else{
					$("input[id^=partQuantity]",$(obj).closest("tr")).valChange("1");
					$("input[id^=partQuantity]",$(obj).closest("tr")).attr("disabled","true");
				}
			}
			//绑定select事件--	结算方式	
			$("select[id^=accountMode]",tab2).bindChange(function(obj){
				//计算服务项目实收金额
				calDecrodatePartItemAmount(obj);
			});
			
			$("input[id^=partSequence]",tab2).bindChange(function(obj){
				//计算服务项目实收金额
				calDecrodatePartItem(obj);
			});
			
			$("input[id^=discount]",tab2).bindChange(function(obj){
				//计算服务项目实收金额
				calDecrodateDiscount(obj);
			});
			
			/***装潢项目******/
			//通过下拉框数据实时变更左侧数值
			var calDecrodateAmount = function(obj){
				var selectValue = $(obj).val();
				if(selectValue =="14061003"||selectValue =="14061004"){
					$("input[id^=discount]",$(obj).closest("tr")).valChange("0");
				}
			}
			//填写序列号数值表格变成1并且不可编辑
			var calDecrodateItem = function(obj){
				$("input[id^=partQuantity]",$(obj).closest("tr")).valChange("1");
				$("input[id^=partQuantity]",$(obj).closest("tr")).attr("disabled","true");
			}
			//改变加价率重置select值
			var calDecrodateDiscount2 = function(obj){
				var inputValue = $(obj).val();
				if(inputValue == 0 || inputValue == ""){
					
				}else{
					$("select[id^=accountMode]",$(obj).closest("tr")).setDmsValue("");
				}
			}
			
			var calServiceItemAmountList = function(obj){
				var inputValue=$(obj).val();
				if(inputValue == 0 || inputValue == ""){
					
				}else{
					$("select[id^=accountMode]",$(obj).closest("tr")).setDmsValue("14061002");
				}
				 dmsRetail.moneyCalculate(container);
			}
			
			//通过下拉框数据实时变更左侧数值
			var calServiceItemAmount = function(obj){
				var selectValue = $(obj).val();
				if(selectValue =="14061003"||selectValue =="14061004"){
					$("input[id^=afterDiscountAmountServicesList]",$(obj).closest("tr")).valChange("0");
				}
				 dmsRetail.moneyCalculate(container);
			}
			//绑定select事件--	结算方式	
			$("select[id^=accountModeList]",tab3).bindChange(function(obj){
				//计算服务项目实收金额
				calServiceItemAmount(obj);
			});
			
			$("input[id^=afterDiscountAmountServicesList]",tab3).bindChange(function(obj){
				//计算服务项目实收金额
				calServiceItemAmountList(obj);
			});
			
			
	}
	var moneyCalculate = function(container){
		 var tab=$("#soDecrodateList tbody",container);
		 var tab2=$("#soDecrodatePartList tbody",container);
		 var tab3=$("#soServicesList tbody",container);
		 var rows=$("tr",tab).length;
		 var rows2=$("tr",tab2).length;
		 var rows3=$("tr",tab3).length;
		 var afterDiscountAmount=parseFloat(0);
		 var afterDiscountAmountPartList=parseFloat(0);
		 var afterDiscountAmountServicesList=parseFloat(0);
		 var partSalesAmount=parseFloat(0);
		 var directivePrice=parseFloat(0);
		 var DiscountAmountPrice =parseFloat(0);
		 var sumDiscountAmountPrice =parseFloat(0);
		 var sumPresentPrice = parseFloat(0);
		 var sumAfterDiscountAmount=parseFloat(0);
		 var sumAfterDiscountAmountPartList=parseFloat(0);
		 var sumAfterDiscountAmountServicesList=parseFloat(0);
		 var sumPartSalesAmount=parseFloat(0);
		 var sumDirectivePrice=parseFloat(0);
		 var sumPartPresentAmount=parseFloat(0);
	
		 //装潢项目计算
		 if(rows>0){
			 for(var i=0;i<rows;i++){
				afterDiscountAmount = $("tr:eq("+i+") td:eq(7) input[id^=afterDiscountAmount]",tab).val();//装潢项目实收金额
				if(afterDiscountAmount =="" || afterDiscountAmount ==undefined){
				    var afterDiscountAmount=parseFloat(0);
				    sumAfterDiscountAmount=parseFloat(sumAfterDiscountAmount)+parseFloat(afterDiscountAmount);//装潢项目实收金额总和
				}else{
					sumAfterDiscountAmount=parseFloat(sumAfterDiscountAmount)+parseFloat(afterDiscountAmount);//装潢项目实收金额总和
				} 
			 }
		 }else{
			  var sumAfterDiscountAmount=parseFloat(0);
		  }
		// console.log("状态状态状态很好"+sumAfterDiscountAmount+"价格:");
		 
		   //装潢材料
			if(rows2>0){
				 for(var i=0;i<rows2;i++){
					 afterDiscountAmountPartList = $("tr:eq("+i+") td:eq(11) input[id^=afterDiscountAmountPartList]",tab2).val();//装潢材料实收金额
					 partSalesPrice=  $("tr:eq("+i+") td:eq(7) input[id^=partSalesPrice]",tab2).val();//装潢材料销售价格
					 partQuantity=  $("tr:eq("+i+") td:eq(8) input[id^=partQuantity]",tab2).val();//装潢材料销售数量
					 partSalesAmount = $("tr:eq("+i+") td:eq(9) input[id^=partSalesAmount]",tab2).val();//装潢材料销售金额
					 if(typeof(partSalesPrice) == "undefined"||partSalesPrice==''){
						 partSalesPrice=parseFloat(0).toFixed(2);
					 }
					 
					 if(typeof(partQuantity) == "undefined"||partQuantity==''){
						 partQuantity=parseFloat(0).toFixed(2);
					 }
					 
					 if(typeof(partSalesAmount) == "undefined"||partSalesAmount==''){
						 partSalesAmount=parseFloat(partSalesPrice)*parseFloat(partQuantity);
						 var item =$("tr:eq("+i+") td:eq(9)>span",tab2);
						 var digits = $(item).attr("data-autoValueDigits");
						 if(digits){
							 partSalesAmount = partSalesAmount.toFixed(parseInt(digits));
						 }
					   $(item).valChange(partSalesAmount);
							//如果item 不是input 属性
						 if(!$(item).is(":input")){
							 $("input",$(item).parent()).valChange(partSalesAmount);
						 }
						// $("input[id^=partSalesAmount]",$(tab2).closest("tr")).valChange(partSalesAmount);
						
					 }

					 
					 accountMode = $("tr:eq("+i+") td:eq(12) select[id^=accountMode]",tab2).val();
					
					 if(accountMode=="14061004"){
						 sumPartPresentAmount=parseFloat(sumPartPresentAmount)+parseFloat(partSalesAmount);//状态为赠送时的金额总和(销售金额)
					 }
					 if(partSalesAmount =="" || partSalesAmount ==undefined){
						 var afterDiscountAmountPartList =parseFloat(0);
						 var partSalesAmount =parseFloat(0);
						 sumPartSalesAmount=parseFloat(sumPartSalesAmount)+parseFloat(partSalesAmount);//销售金额总和
						 sumAfterDiscountAmountPartList=parseFloat(sumAfterDiscountAmountPartList)+parseFloat(afterDiscountAmountPartList); //装潢材料实收金额总和
					 }else{
						 sumPartSalesAmount=parseFloat(sumPartSalesAmount)+parseFloat(partSalesAmount);//销售金额总和
						 sumAfterDiscountAmountPartList=parseFloat(sumAfterDiscountAmountPartList)+parseFloat(afterDiscountAmountPartList); //装潢材料实收金额总和
					 }
	  				   

				 }
			}else{
				var afterDiscountAmountPartList =parseFloat(0);
				var partSalesAmount =parseFloat(0);
				var DiscountAmountPrice =parseFloat(0);
				var sumDiscountAmountPrice =parseFloat(0);
				var sumPartSalesAmount =parseFloat(0);
				var sumAfterDiscountAmountPartList =parseFloat(0);
			}
			
			   
	   		//服务项目
			 if(rows3>0){
				 for(var i=0;i<rows3;i++){
					 afterDiscountAmountServicesList = $("tr:eq("+i+") td:eq(6) input[id^=afterDiscountAmountServicesList]",tab3).val();//服务项目实收金额
					 directivePrice=$("tr:eq("+i+") td:eq(5) input[id^=directivePrice]",tab3).val();
					 accountModeList = $("tr:eq("+i+") td:eq(7) select[id^=accountModeList]",tab3).val();
					 if(accountModeList=="14061004"){
						 sumDirectivePrice=parseFloat(sumDirectivePrice)+parseFloat(directivePrice);
					 }
					 if(afterDiscountAmountServicesList=="" || afterDiscountAmountServicesList ==undefined){
						 var afterDiscountAmountServicesList=parseFloat(0);
						 sumAfterDiscountAmountServicesList=parseFloat(sumAfterDiscountAmountServicesList)+parseFloat(afterDiscountAmountServicesList);//服务项目实收金额总和
					 }else{
						 sumAfterDiscountAmountServicesList=parseFloat(sumAfterDiscountAmountServicesList)+parseFloat(afterDiscountAmountServicesList);//服务项目实收金额总和
					 }	
				 }
			 }else{
				var afterDiscountAmountServicesList=parseFloat(0);
				var presentPrice=parseFloat(0);
				var directivePrice=parseFloat(0);
				var presentPrice=parseFloat(0);
				var sumPresentPrice=parseFloat(0);
				var sumAfterDiscountAmountServicesList=parseFloat(0);
			}
			
			 if(sumAfterDiscountAmount=="" && sumAfterDiscountAmountPartList !=""){
				 $("#upholsterSum",container).setDmsValue(parseFloat(sumAfterDiscountAmountPartList).toFixed(2));
			 }else if(sumAfterDiscountAmountPartList=="" && sumAfterDiscountAmount !=""){
				 $("#upholsterSum",container).setDmsValue(parseFloat(sumAfterDiscountAmount).toFixed(2));
			 }else if(sumAfterDiscountAmountPartList=="" && sumAfterDiscountAmount==""){
				 $("#upholsterSum",container).setDmsValue(parseFloat(0));
			 }else{
			 		$("#upholsterSum",container).setDmsValue((parseFloat(sumAfterDiscountAmount)+parseFloat(sumAfterDiscountAmountPartList)).toFixed(2));//装潢金额总和	

			 }
			//计算赠送金额
		    if(accountModeList=="14061004" && accountMode !="14061004"){
					$("#presentSum",container).setDmsValue(parseFloat(sumDirectivePrice).toFixed(2));
			 }else if(accountMode=="14061004" && accountModeList !="14061004"){
				 $("#presentSum",container).setDmsValue(parseFloat(sumPartPresentAmount).toFixed(2));
			 }else if(accountModeList=="" && accountMode==""){
				 $("#presentSum",container).setDmsValue(parseFloat(0));
			 }else{
				 $("#presentSum",container).setDmsValue((parseFloat(sumDirectivePrice)+parseFloat(sumPartPresentAmount)).toFixed(2));//赠送金额总和
			 }
			 
			 $("#serviceSum",container).setDmsValue(parseFloat(sumAfterDiscountAmountServicesList).toFixed(2));//服务项目金额总和
			
	};
	
	return {
		moneyCalculate:function(container){
			moneyCalculate(container);
		},
		saleDatabindChange:function(tableObject,container){
			saleDatabindChange(tableObject,container);
		}
	};
}();


/**
 * 车辆模块相关功能
 */
var dmsVehicle = function() {
	var sample = function(container){
		
	};
	
	return {
		sample:function(){
			sample();
		}
	};
}();



