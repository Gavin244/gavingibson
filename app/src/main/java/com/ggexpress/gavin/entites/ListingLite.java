package com.ggexpress.gavin.entites;

import com.ggexpress.gavin.backend.BackendServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gavin on 28/02/20.
 */

public class ListingLite {
    public String pk, user, parentType, source, productPk, productName, productPrice, productDiscount, productDiscountedPrice, serialNo, howMuch, unit, specifications;
    public String sku, updated, unitPerpack, created, price, parent_id, id;
    int productIntPrice, productIntDiscountedPrice;
    boolean approved, inStock;
    public String addedCart, addedWish;
    public String parentPk, parentName;
    public String parentTypePk, parentTypeName, parentTypeMinCost, parentTypeVisual;
    public String fieldsPk, fieldsName, fieldsValue, fieldsType, fieldsHelpText, fieldsUnit, fieldsData;
    public String filesPk, filesLink, filesAttachment, filesMediaType;
    JSONObject jsonObject;
    public JSONArray itemArray;

    JSONArray filesArray;
    ArrayList<String> imageUrl = new ArrayList<>();

//    public String name, value, fieldType, helpText, unit, data;
    ArrayList<Integer> size= new ArrayList<Integer>();

    public ListingLite(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {
            this.pk = jsonObject.getString("pk");
            this.approved = jsonObject.getBoolean("approved");
            this.source = jsonObject.getString("source");
            this.addedCart = jsonObject.getString("added_cart");
            this.addedWish = jsonObject.getString("added_saved");
            this.inStock = jsonObject.getBoolean("in_stock");
            String str = jsonObject.getString("specifications");
            this.specifications = str;

            JSONObject productObj = jsonObject.getJSONObject("product");
            this.productPk = productObj.getString("pk");
            this.user = productObj.getString("user");
            this.howMuch = productObj.getString("howMuch");
            this.serialNo = productObj.getString("serialNo");
            this.unit = productObj.getString("unit");
            this.productName = productObj.getString("name");
            Double d = Double.parseDouble(productObj.getString("price"));
            this.productIntPrice = (int) Math.round(d);
            this.productPrice = String.valueOf(this.productIntPrice);
            this.productDiscount = productObj.getString("discount");
//            this.productDiscountedPrice = productObj.getString("discountedPrice");
            Double d1 = Double.parseDouble(productObj.getString("discountedPrice"));
            this.productIntDiscountedPrice = (int) Math.round(d1);
            this.productDiscountedPrice = String.valueOf(this.productIntDiscountedPrice);

            this.itemArray = jsonObject.getJSONArray("product_variants");
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject jsonObj = itemArray.getJSONObject(i);
                this.sku = jsonObj.getString("sku");
                this.updated = jsonObj.getString("updated");
                this.unitPerpack = jsonObj.getString("unitPerpack");
                this.created = jsonObj.getString("created");
                this.price = jsonObj.getString("price");
                this.parent_id = jsonObj.getString("parent_id");
                this.id = jsonObj.getString("id");
            }

            this.filesArray = jsonObject.getJSONArray("files");
            if (filesArray==null||filesArray.equals("null")){
                this.filesAttachment = BackendServer.url+"/static/images/ecommerce.jpg";
            } else {
                for (int i = 0; i < filesArray.length(); i++) {
                    JSONObject filesObject = filesArray.getJSONObject(i);
                    this.filesPk = filesObject.getString("pk");
                    this.filesLink = filesObject.getString("link");
                    String filesAttachment = filesObject.getString("attachment");
                    if (filesAttachment.equals("null") || filesAttachment.equals("") || filesAttachment == null) {
                        this.filesAttachment = BackendServer.url+"/media/ecommerce/pictureUploads/1532690173_89_admin_ecommerce.jpg";
                    } else this.filesAttachment = filesAttachment;
                    this.filesMediaType = filesObject.getString("mediaType");
                    imageUrl.add(this.filesAttachment);
                }
            }

            JSONObject parentType = jsonObject.getJSONObject("parentType");
            this.parentTypePk = parentType.getString("pk");
            this.parentTypeName = parentType.getString("name");
            this.parentTypeMinCost = parentType.getString("minCost");
            this.parentTypeVisual = parentType.getString("visual");

            JSONObject parent = parentType.getJSONObject("parent");
            if (parent!=null) {
                this.parentPk = parent.getString("pk");
                this.parentName = parent.getString("name");
            }
            JSONArray fields = parentType.getJSONArray("fields");
            for (int i=0; i<fields.length(); i++) {
                JSONObject fieldsObject = fields.getJSONObject(i);
                this.fieldsPk = fieldsObject.getString("pk");
                this.fieldsName = fieldsObject.getString("name");
                this.fieldsValue = fieldsObject.getString("value");
                this.fieldsType = fieldsObject.getString("fieldType");
                this.fieldsHelpText = fieldsObject.getString("helpText");
                this.fieldsUnit = fieldsObject.getString("unit");
                this.fieldsData = fieldsObject.getString("data");
            }



//            JSONArray data = new JSONArray(str);
//            for (int i=0; i<data.length(); i++){
//                JSONObject object = data.getJSONObject(i);
//                this.name = object.getString("name");
//                this.value = object.getString("value");
//                this.fieldType = object.getString("fieldType");
//                this.helpText = object.getString("helpText");
//                this.unit = object.getString("unit");
//                this.data = object.getString("data");
//                size.add(i);
//            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductPk() {
        return productPk;
    }

    public void setProductPk(String productPk) {
        this.productPk = productPk;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductDiscountedPrice() {
        return productDiscountedPrice;
    }

    public int getProductIntPrice() {
        return productIntPrice;
    }

    public void setProductIntPrice(int productIntPrice) {
        this.productIntPrice = productIntPrice;
    }

    public int getProductIntDiscountedPrice() {
        return productIntDiscountedPrice;
    }

    public void setProductIntDiscountedPrice(int productIntDiscountedPrice) {
        this.productIntDiscountedPrice = productIntDiscountedPrice;
    }

    public void setProductDiscountedPrice(String productDiscountedPrice) {
        this.productDiscountedPrice = productDiscountedPrice;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getParentPk() {
        return parentPk;
    }

    public void setParentPk(String parentPk) {
        this.parentPk = parentPk;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentTypePk() {
        return parentTypePk;
    }

    public void setParentTypePk(String parentTypePk) {
        this.parentTypePk = parentTypePk;
    }

    public String getParentTypeName() {
        return parentTypeName;
    }

    public void setParentTypeName(String parentTypeName) {
        this.parentTypeName = parentTypeName;
    }

    public String getParentTypeMinCost() {
        return parentTypeMinCost;
    }

    public void setParentTypeMinCost(String parentTypeMinCost) {
        this.parentTypeMinCost = parentTypeMinCost;
    }

    public String getParentTypeVisual() {
        return parentTypeVisual;
    }

    public void setParentTypeVisual(String parentTypeVisual) {
        this.parentTypeVisual = parentTypeVisual;
    }

    public String getFieldsPk() {
        return fieldsPk;
    }

    public void setFieldsPk(String fieldsPk) {
        this.fieldsPk = fieldsPk;
    }

    public String getFieldsName() {
        return fieldsName;
    }

    public void setFieldsName(String fieldsName) {
        this.fieldsName = fieldsName;
    }

    public String getFieldsValue() {
        return fieldsValue;
    }

    public void setFieldsValue(String fieldsValue) {
        this.fieldsValue = fieldsValue;
    }

    public String getFieldsType() {
        return fieldsType;
    }

    public void setFieldsType(String fieldsType) {
        this.fieldsType = fieldsType;
    }

    public String getFieldsHelpText() {
        return fieldsHelpText;
    }

    public void setFieldsHelpText(String fieldsHelpText) {
        this.fieldsHelpText = fieldsHelpText;
    }

    public String getFieldsUnit() {
        return fieldsUnit;
    }

    public void setFieldsUnit(String fieldsUnit) {
        this.fieldsUnit = fieldsUnit;
    }

    public String getFieldsData() {
        return fieldsData;
    }

    public void setFieldsData(String fieldsData) {
        this.fieldsData = fieldsData;
    }

    public String getFilesPk() {
        return filesPk;
    }

    public void setFilesPk(String filesPk) {
        this.filesPk = filesPk;
    }

    public String getFilesLink() {
        return filesLink;
    }

    public void setFilesLink(String filesLink) {
        this.filesLink = filesLink;
    }

    public String getFilesAttachment() {
        return filesAttachment;
    }

    public void setFilesAttachment(String filesAttachment) {
        this.filesAttachment = filesAttachment;
    }

    public String getFilesMediaType() {
        return filesMediaType;
    }

    public void setFilesMediaType(String filesMediaType) {
        this.filesMediaType = filesMediaType;
    }

    public ArrayList<Integer> getSize() {
        return size;
    }

    public void setSize(ArrayList<Integer> size) {
        this.size = size;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public JSONArray getFilesArray() {
        return filesArray;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getHowMuch() {
        return howMuch;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setHowMuch(String howMuch) {
        this.howMuch = howMuch;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUnitPerpack() {
        return unitPerpack;
    }

    public void setUnitPerpack(String unitPerpack) {
        this.unitPerpack = unitPerpack;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONArray getItemArray() {
        return itemArray;
    }

    public void setItemArray(JSONArray itemArray) {
        this.itemArray = itemArray;
    }

    public void setFilesArray(JSONArray filesArray) {
        this.filesArray = filesArray;
    }

    public String getAddedCart() {
        return addedCart;
    }

    public void setAddedCart(String addedCart) {
        this.addedCart = addedCart;
    }

    public String getAddedWish() {
        return addedWish;
    }

    public void setAddedWish(String addedWish) {
        this.addedWish = addedWish;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
