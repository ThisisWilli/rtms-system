package com.willi.Bean;

/**
 * @program: bigdataplatform
 * @description:信息实体类
 * @author: Hoodie_Willi
 * @create: 2020-02-19 20:09
 **/
/**
 * 1. user_id | 买家id
 * 2. item_id | 商品id
 * 3. cat_id | 商品类别id
 * 4. merchant_id | 卖家id
 * 5. brand_id | 品牌id
 * 6. month | 交易时间:月
 * 7. day | 交易事件:日
 * 8. action | 行为,取值范围{0,1,2,3},0表示点击，1表示加入购物车，2表示购买，3表示关注商品
 * 9. age_range | 买家年龄分段：1表示年龄<18,2表示年龄在[18,24]，3表示年龄在[25,29]，4表示年龄在[30,34]，5表示年龄在[35,39]，6表示年龄在[40,49]，7和8表示年龄>=50,0和NULL则表示未知
 * 10. gender | 性别:0表示女性，1表示男性，2和NULL表示未知
 * 11. province| 收获地址省份
 */
public class Shopper {
    private Integer userId;
    private Integer itemId;
    private Integer catId;
    private Integer merchantId;
    private Integer brandId;
    private Integer month;
    private Integer day;
    private Integer action;
    private Integer ageRange;
    private Integer gender;
    private String province;

    public Shopper() {
    }



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(Integer ageRange) {
        this.ageRange = ageRange;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
