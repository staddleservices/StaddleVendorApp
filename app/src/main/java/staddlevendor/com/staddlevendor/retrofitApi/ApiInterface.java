package staddlevendor.com.staddlevendor.retrofitApi;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import staddlevendor.com.staddlevendor.ResponseClasses.AcceptedResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.AddOfferResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.CompletedOrdersResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.GetCategoryListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.GetSubCategoryListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.GetSubCategoryTreeListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.GetVendorInfoResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.GetVendorSubCategoryListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.LoginResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.MenuListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.OnGoingOfferListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.PendingListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.ProductListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.UploadImageResponse;

public interface ApiInterface {

    @POST(EndApi.USER_LOGIN_API)
    Call<LoginResponse> UserLogin(@Query("mobile") String mobile,
                                  @Query("password") String password,
                                  @Query("device_id") String device_id,
                                  @Query("device_type") String device_type);

    @POST(EndApi.UPLOAD_PICTURE)
    @FormUrlEncoded
    Call<UploadImageResponse> uploadPicture(@Field("image") String image);

    @GET(EndApi.GET_VENDOR_INFO)
    Call<GetVendorInfoResponse> getVendorInfo(@Query("vid") String vid);

    @GET(EndApi.GET_VENDOR_DETAILS)
    Call<JsonElement> getVenderDetails(@Query("vid") String vid);

    @GET("getVenderImgDeatils.php")
    Call<JsonElement> getVenderImageDetails(@Query("vid") String vid);

    @FormUrlEncoded
    @POST("update_vender_images.php")
    Call<JsonElement> updateVenderImages1(@Field("vid") String vid,
                                          @Field("image12") String image1);

    @FormUrlEncoded
    @POST("update_vender_images.php")
    Call<JsonElement> updateVenderImages2(@Field("vid") String vid,
                                          @Field("image2") String image1);

    @FormUrlEncoded
    @POST("update_vender_images.php")
    Call<JsonElement> updateVenderImages3(@Field("vid") String vid,
                                          @Field("image3") String image1);

    @FormUrlEncoded
    @POST("update_vender_images.php")
    Call<JsonElement> updateVenderImages4(@Field("vid") String vid,
                                          @Field("image4") String image1);

    @FormUrlEncoded
    @POST("update_vender_images.php")
    Call<JsonElement> updateVenderImages5(@Field("vid") String vid,
                                          @Field("image5") String image1);

    @FormUrlEncoded
    @POST("update_vender_images.php")
    Call<JsonElement> updateVenderImages6(@Field("vid") String vid,
                                          @Field("gst_no") String image1);

    @FormUrlEncoded
    @POST("update_vender_images.php")
    Call<JsonElement> updateVenderImages7(@Field("vid") String vid,
                                          @Field("pan") String image1);

    @FormUrlEncoded
    @POST("update_vender_images.php")
    Call<JsonElement> updateVenderImages8(@Field("vid") String vid,
                                          @Field("work_certificate") String image1);

    @FormUrlEncoded
    @POST("update_vender_images.php")
    Call<JsonElement> updateVenderImages9(@Field("vid") String vid,
                                          @Field("aadhar") String image1);

    @GET("completed_orderwithcod_list_vender.php?")
    Call<JsonElement> getCodAmounts(@Query("vid") String vid);

    @GET("completed_orderwithoutcod_list_vender.php?")
    Call<JsonElement> getOnlineAmounts(@Query("vid") String vid);

    @GET("changeOrderStatusComplete.php?")
    Call<JsonElement> completeOrder(@Query("id") String id,
                                    @Query("items") String items,
                                    @Query("vname") String vname,
                                    @Query("user_mobile") String user_mobile,
                                    @Query("uid") String uid,
                                    @Query("vid") String vid);

    @GET("edit_vender_starttime_endtime.php")
    Call<JsonElement> updateTime(@Query("vid") String vid,
                                 @Query("opening_time") String opening_time,
                                 @Query("closing_time") String closing_time);

    @GET("wsChangePassVender.php")
    Call<JsonElement> updatePassword(@Query("vid") String vid,
                                     @Query("password") String password,
                                     @Query("newpassword") String newpassword);


    @FormUrlEncoded
    @POST(EndApi.UPDATE_USER_INFO)
    Call<AddOfferResponse> editProfile(@Field("vid") String vid,
                                       @Field("fname") String fname,
                                       @Field("email") String email,
                                       @Field("location") String location,
                                       @Field("mobile") String mobile,
                                       @Field("adharcard_no") String adharcard_no,
                                       @Field("gst_no") String gst_no,
                                       @Field("image") String image);

    @FormUrlEncoded
    @POST(EndApi.ADD_PRODUCT)
    Call<JsonElement> AddOffer(/*@Field("id") String id,*/
            @Field("cid") String cid,
            @Field("sid") String sid,
            @Field("current_price") String current_price,
            @Field("offer_price") String offer_price,
            @Field("product_details") String product_details,
            @Field("offer_start_date") String offer_start_date,
            @Field("offer_end_date") String offer_end_date,
            @Field("image") String image,
            @Field("offer_name") String offer_name,
            @Query("id") String vid);

    @FormUrlEncoded
    @POST(EndApi.ADD_MENU)
    Call<JsonElement> AddMenu(
            @Field("vid") String vid,
            @Field("name") String name,
            @Field("price") String price,
            @Field("sid") String sid
    );


    @FormUrlEncoded
    @POST(EndApi.EDIT_MENU)
    Call<JsonElement> editMenu(
            @Field("vid") String vid,
            @Field("name") String name,
            @Field("price") String price
    );

    @FormUrlEncoded
    @POST(EndApi.DELETE_MENU)
    Call<JsonElement> deleteMenu(
            @Field("id") String id);

    @FormUrlEncoded
    @POST(EndApi.ADD_VENDER_SUB_CATEGORY)
    Call<JsonElement> addSubCategory(
            @Field("vid") String vid,
            @Field("sub_cat_name") String name);

    @FormUrlEncoded
    @POST(EndApi.CHANGE_MENU_STATUS)
    Call<JsonElement> ChangeMenuStatus(
            @Field("status") String vid,
            @Field("id") String name);


    @GET(EndApi.GET_PRODUCT_LIST_VENDOR)
    Call<ProductListResponse> getProductList(@Query("vid") String vid);

    @GET(EndApi.GET_CATEGORY_LIST)
    Call<GetCategoryListResponse> getCategoryList();

    @GET(EndApi.GET_VENDOR_MENU_LIST)
    Call<MenuListResponse> getVendermenulist(@Query("vid") String vid);

    @GET(EndApi.GET_SUB_CATEGORY_LIST)
    Call<GetSubCategoryListResponse> getSubCategoryList(@Query("cid") String cid);

    @GET(EndApi.GET_SUB_CATEGORY_TREE_LIST)
    Call<GetSubCategoryTreeListResponse> getSubCategoryTreeList(@Query("sid") String sid);

    @GET(EndApi.GET_VENDER_SUBCATEGORY_LIST)
    Call<GetVendorSubCategoryListResponse> getVenderSubCatgoryList(@Query("vid") String vid);

    @FormUrlEncoded
    @POST(EndApi.EDIT_VENDER_SUB_CATEGORY)
    Call<JsonElement> editSubCategory(
            @Field("vid") String vid,
            @Field("name") String name);


    @GET(EndApi.GET_PENDING_LIST)
    Call<PendingListResponse> getPendingList(@Query("vid") String vid);
    @GET(EndApi.GET_ACCEPTED_LIST)
    Call<AcceptedResponse> getAcceptedtList(@Query("vid") String vid);

    @GET(EndApi.GET_OFFER_ACCEPTED_LIST)
    Call<PendingListResponse> getAcceptedList(@Query("vid") String vid);

    @GET(EndApi.GET_OFFER_REJECTED_LIST)
    Call<PendingListResponse> getRejectedList(@Query("vid") String vid);

    @GET(EndApi.UPDATE_PRODUCT_STATUS_ACCEPTED)
    Call<JsonElement> updateProductSatusAccepted(@Query("id") String id,
                                                 @Query("vname") String vname,
                                                 @Query("vid") String vid,
                                                 @Query("uid") String uid);

    @GET(EndApi.UPDATE_PRODUCT_STATUS_REJECTED)
    Call<JsonElement> updateProductSatusRejected(@Query("id") String id);

    @GET(EndApi.GET_OFFER_COMPLETED_LIST)
    Call<CompletedOrdersResponse> getCompletedList(@Query("vid") String vid);

    @GET(EndApi.GET_ONGOING_OFFER_LIST)
    Call<OnGoingOfferListResponse> getOnGoingOfferList(@Query("vid") String vid);

    @GET("wsforgotvenderPassword.php")
    Call<JsonElement> forgotPassword(@Query("email") String email);



    //http://staddle.in/mobileapp/api/login_vender.php

}
