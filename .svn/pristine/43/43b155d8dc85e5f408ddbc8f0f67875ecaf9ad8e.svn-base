package com.yonyou.dms.framework.domain;

import java.util.Map;

import org.javalite.activejdbc.ColumnMetadata;
import org.javalite.activejdbc.MetaModel;
import org.javalite.activejdbc.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yonyou.dms.framework.util.bean.AppliactionContextHelper;
import com.yonyou.dms.function.common.CommonConstants;
import com.yonyou.dms.function.exception.DALException;

public abstract class BaseModel extends Model {

    private static final Logger logger      = LoggerFactory.getLogger(BaseModel.class);
    private static final String Dealer_Code = CommonConstants.PUBLIC_DEALER_CODE_NAME;
    private static final String ORGANIZATIONID = CommonConstants.PUBLIC_ORGANIZATION_NAME;

    /**
     * 定义保存之前处理方案
     * 
     * @author zhangxc
     * @date 2016年7月7日 (non-Javadoc)
     * @see org.javalite.activejdbc.CallbackSupport#beforeSave()
     */
    @Override
    public void beforeSave() {
        // 得到当前用户的DealerCode
        LoginInfoDto currentUserInfo = getSessionUserInfo();
        
        if (this.getId() == null) {
            if (isExistsDealerCodeColumn()) {
                if(currentUserInfo!=null){
                    set(Dealer_Code, currentUserInfo.getDealerCode());
                }
            }
            if (isExistsOrganiztionIdColumn()) {
                if(currentUserInfo!=null){
                    set(ORGANIZATIONID, currentUserInfo.getOrgId());
                }
            }
            //如果不为空
            if(currentUserInfo!=null){
                set("created_by", currentUserInfo.getUserAccount());
            }
           
        } else {
            if (isExistsDealerCodeColumn()) {
                String dealerCode = this.getString(Dealer_Code);
                if (currentUserInfo!=null&&dealerCode != null && !dealerCode.equals(currentUserInfo.getDealerCode())) {
                    throw new DALException("数据操作异常");
                }
            }
        }
        // 在修改的情况下，只修改update_by 值
        if(currentUserInfo!=null){
            set("updated_by", currentUserInfo.getUserAccount());
        }
        
    }

    /**
     * 如果有经销商信息，检查经销商代码是否与当前session 中代码一致
     * 
     * @author zhangxc
     * @date 2016年6月30日 (non-Javadoc)
     * @see org.javalite.activejdbc.CallbackSupport#afterLoad()
     */
    @Override
    public void afterLoad() {
        // 如果有经销商信息，检查经销商代码是否与当前session 中代码一致
        if (isExistsDealerCodeColumn()) {
            // 得到当前用户的DealerCode
            String currentDealerCode = getSessionDealerCode();
            String dealerCode = this.getString(Dealer_Code);
            logger.debug("Dealer_code:" + dealerCode + ";Info:" + currentDealerCode);
            if (currentDealerCode !=null && dealerCode != null && !dealerCode.equals(currentDealerCode)) {
                throw new DALException("数据操作异常");
            }
        }
        // logger.debug("into afterLoad");
    }

    /**
     * 在删除之前进行检查
     * 
     * @author zhangxc
     * @date 2016年7月7日 (non-Javadoc)
     * @see org.javalite.activejdbc.CallbackSupport#beforeDelete()
     */
    @Override
    public void beforeDelete() {
        // 如果有经销商信息，检查经销商代码是否与当前session 中代码一致
        if (isExistsDealerCodeColumn()) {
            // 得到当前用户的DealerCode
            String currentDealerCode = getSessionDealerCode();
            String dealerCode = this.getString(Dealer_Code);
            if (currentDealerCode != null && dealerCode != null && !dealerCode.equals(currentDealerCode)) {
                throw new DALException("数据操作异常");
            }
        }
    }

    /**
     * 获得当前用户的经销商代码
     * 
     * @author zhangxc
     * @date 2016年11月9日
     * @return
     */
    private String getSessionDealerCode() {
        try {
            LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
            return loginInfo.getDealerCode();
        } catch (Exception e) {
        }
        ;
        return null;
    }
    
    /**
     * 获得当前用户的经销商代码
     * 
     * @author zhangxc
     * @date 2016年11月9日
     * @return
     */
    private LoginInfoDto getSessionUserInfo() {
        try {
            LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
            return loginInfo;
        } catch (Exception e) {
        }
        ;
        return null;
    }

    /**
     * 判断是否包含经销商代码
     * 
     * @author zhangxc
     * @date 2016年6月30日
     * @return 如果包含，返回true,否则返回false
     */
    private boolean isExistsDealerCodeColumn() {
        return isExistsDefineColumn(Dealer_Code);
    }
    
    /**
     * 
    * 判断是否存在ORGNAIZTION_ID
    * @author zhangxc
    * @date 2016年11月13日
    * @return
     */
    private boolean isExistsOrganiztionIdColumn() {
        return isExistsDefineColumn(ORGANIZATIONID);
    }
    
    /**
     * 
    * 判断是否存在某个字段
    * @author zhangxc
    * @date 2016年11月13日
    * @param columnName
    * @return
     */
    private boolean isExistsDefineColumn(String columnName){
        MetaModel metaModel = this.getMetaModelLocal();
        Map<String, ColumnMetadata> columnMetas = metaModel.getColumnMetadata();

        for (Map.Entry<String, ColumnMetadata> entry : columnMetas.entrySet()) {
            if (columnName.equals(entry.getKey().toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    // /**
    // * 定义增加DealerCode Model
    // * @author zhangxc
    // * @date 2016年7月7日
    // * @param subquery
    // * @param params
    // * @return
    // */
    // public static <T extends Model> LazyList<T> findWithDealer(String subquery, Object... params)
    // {
    // subquery+=" DEALER_CODE = ?";
    // LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
    // Arrays.fill(params, loginInfo.getDealerCode());
    // return find(subquery,params);
    // }
    //
    // /**
    // * 定义增加DealerCode Model
    // * @author zhangxc
    // * @date 2016年7月7日
    // * @param subquery
    // * @param params
    // * @return
    // */
    // public static <T extends Model> LazyList<T> whereWithDealer(String subquery, Object... params)
    // {
    // subquery+=" DEALER_CODE = ?";
    // LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
    // Arrays.fill(params, loginInfo.getDealerCode());
    // return where(subquery,params);
    // }
    // /**
    // * 定义增加DealerCode Model
    // * @author zhangxc
    // * @date 2016年7月7日
    // * @param subquery
    // * @param params
    // * @return
    // */
    // public static <T extends Model> LazyList<T> findAllWithDealer()
    // {
    // String subquery =" DEALER_CODE = ?";
    // LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
    // String[] params = new String[1];
    // Arrays.fill(params, loginInfo.getDealerCode());
    // return find(subquery,params);
    // }
    //
    // @Override
    // protected void afterValidation() {
    // logger.debug("into afterValidation");
    // }
    // @Override
    // public void beforeClosingTag(StringBuilder sb, boolean pretty, String indent, String... attributeNames) {
    // logger.debug("into beforeClosingTag:"+sb.toString()+":pretty"+pretty+":"+indent+":"+attributeNames);
    // super.beforeClosingTag(sb, pretty, indent, attributeNames);
    // }
    //
    // @Override
    // public void beforeClosingBrace(StringBuilder sb, boolean pretty, String indent, String... attributeNames) {
    // logger.debug("into beforeClosingBrace:"+sb.toString()+":pretty"+pretty+":"+indent+":"+attributeNames);
    // super.beforeClosingBrace(sb, pretty, indent, attributeNames);
    // }

}
