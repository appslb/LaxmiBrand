package com.e.laxmibrand.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BaseResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;


    @SerializedName("data")
    @Expose
    private ResponseData data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ResponseData getResponseData() {
        return data;
    }

    public void setResponseData(ResponseData data) {
        this.data = data;
    }

    public class ResponseData implements Serializable {
        @SerializedName("message")
        @Expose
        private String message;


        @SerializedName("description")
        @Expose
        private String description;

        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mob_no")
        @Expose
        private String mob_no;
        @SerializedName("about_us_desc")
        @Expose
        private String about_us_desc;

        @SerializedName("user_id")
        @Expose
        private String user_id;

        @SerializedName("unique_deviceid")
        @Expose
        private String unique_deviceid;

        @SerializedName("mobile_no")
        @Expose
        private String mobile_no;

        @SerializedName("whatsapp_grp_redirect_link")
        @Expose
        private String whatsapp_grp_redirect_link;

        @SerializedName("order_id")
        @Expose
        private int order_id;
        public String getWhatsapp_grp_redirect_link() {
            return whatsapp_grp_redirect_link;
        }

        public void setWhatsapp_grp_redirect_link(String whatsapp_grp_redirect_link) {
            this.whatsapp_grp_redirect_link = whatsapp_grp_redirect_link;
        }

        public String getuser_id() {
            return user_id;
        }

        public void setuser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
        public String getDescription() {
            return description;
        }
        public String getEmail() {
            return email;
        }
        public String getMob_no() {
            return mob_no;
        }
        public String getMobile_no() {
            return mobile_no;
        }
        public void setMobile_no(String mobile_no) {
            this.mobile_no=mobile_no;
        }

        public String getUniquedevice_id() {
            return unique_deviceid;
        }
        public void setUniquedevice_id(String unique_deviceid) {
            this.unique_deviceid=unique_deviceid;
        }

        public String getAbout_us_desc() {
            return about_us_desc;
        }



    }

    }
