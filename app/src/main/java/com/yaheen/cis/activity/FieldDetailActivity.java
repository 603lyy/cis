package com.yaheen.cis.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.entity.HouseBean;
import com.yaheen.cis.util.HttpUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import static com.yaheen.cis.util.HttpUtils.houseUrl;

public class FieldDetailActivity extends PermissionActivity {

    private TextView tvHOwner, tvHNumber, tvHAreaType, tvHInspectionPoint, tvHAdderss;

    private TextView tvPUser, tvPPosition, tvPCommitment, tvPPhone, tvPTime;

    private TextView tvMUser, tvMType, tvMName, tvMTime, tvMOwner;

    private LinearLayout llBack, llHouse, llParty, llMerchant;

    private String houseId;

    //岳阳
    private String mHhouseUrl = houseUrl + "/separationSub/getRangeHouseNumberFromApplets.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);

        houseId = getIntent().getStringExtra("houseId");

        llBack = findViewById(R.id.back);

        initHouseData();
        getHouseData(houseId);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initHouseData() {
        tvHNumber = findViewById(R.id.tv_house_number);
        tvHAdderss = findViewById(R.id.tv_house_address);
        tvHOwner = findViewById(R.id.tv_patrol_username);
        tvHAreaType = findViewById(R.id.tv_house_area_type);
        tvHInspectionPoint = findViewById(R.id.tv_house_inspection_point);

        tvPTime = findViewById(R.id.tv_party_time);
        tvPPhone = findViewById(R.id.tv_party_phone);
        tvPUser = findViewById(R.id.tv_party_username);
        tvPPosition = findViewById(R.id.tv_party_position);
        tvPCommitment = findViewById(R.id.tv_party_commitment);

        tvMName = findViewById(R.id.tv_merchant_name);
        tvMType = findViewById(R.id.tv_merchant_type);
        tvMUser = findViewById(R.id.tv_merchant_user);
        tvMTime = findViewById(R.id.tv_merchant_time);
        tvMOwner = findViewById(R.id.tv_merchant_owner);

        llHouse = findViewById(R.id.ll_house_data);
        llParty = findViewById(R.id.ll_party_data);
        llMerchant = findViewById(R.id.ll_merchant_data);
    }

    private void getHouseData(String houseId) {

        if (TextUtils.isEmpty(houseId)) {
            return;
        }

        RequestParams params = new RequestParams(mHhouseUrl);
        params.addQueryStringParameter("houseNumberId", houseId);
        HttpUtils.getPostHttp(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                HouseBean data = gson.fromJson(result, HouseBean.class);
                if (data != null && data.isResult() && data.getJson().size() > 0) {
                    showHouseData(data.getJson().get(0));
                } else {
                    llHouse.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 显示门牌信息，没有数据的不显示
     */
    public void showHouseData(HouseBean.JsonBean jsonBean) {
        if (jsonBean != null) {

            if (jsonBean.getHouseNumber() != null || jsonBean.getUser() != null) {

                if (jsonBean.getUser().size() > 0 && !TextUtils.isEmpty(jsonBean.getUser().get(0).getName())) {
                    tvHOwner.setText(jsonBean.getUser().get(0).getName());
                    tvHOwner.setVisibility(View.VISIBLE);
                } else {
                    tvHOwner.setVisibility(View.GONE);
                }

                if (jsonBean.getHouseNumber() != null) {

                    if (!TextUtils.isEmpty(jsonBean.getHouseNumber().getPeopleNumber())) {
                        tvHNumber.setText(jsonBean.getHouseNumber().getPeopleNumber());
                        tvHNumber.setVisibility(View.VISIBLE);
                    } else {
                        tvHNumber.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(jsonBean.getHouseNumber().getCommunity())) {
                        if (jsonBean.getHouseNumber().getCommunity().equals("A")) {
                            tvHAreaType.setText(getString(R.string.detail_house_type_a));
                            tvHAreaType.setVisibility(View.VISIBLE);
                        } else if (jsonBean.getHouseNumber().getCommunity().equals("N")) {
                            tvHAreaType.setText(getString(R.string.detail_house_type_n));
                            tvHAreaType.setVisibility(View.VISIBLE);
                        } else {
                            tvHAreaType.setVisibility(View.GONE);
                        }
                    } else {
                        tvHAreaType.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(jsonBean.getHouseNumber().getGridInspectionPoint())) {
                        if (jsonBean.getHouseNumber().getGridInspectionPoint().equals("Y")) {
                            tvHInspectionPoint.setText(getString(R.string.text_yes));
                        } else {
                            tvHInspectionPoint.setText(getString(R.string.text_no));
                        }
                        tvHInspectionPoint.setVisibility(View.VISIBLE);
                    } else {
                        tvHInspectionPoint.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(jsonBean.getHouseNumber().getAddress())) {
                        tvHAdderss.setText(jsonBean.getHouseNumber().getAddress());
                        tvHAdderss.setVisibility(View.VISIBLE);
                    } else {
                        tvHAdderss.setVisibility(View.GONE);
                    }
                }
                llHouse.setVisibility(View.VISIBLE);
            } else {
                llHouse.setVisibility(View.GONE);
            }

            if (jsonBean.getPartyMember() != null && jsonBean.getPartyMember().size() > 0) {

                if (!TextUtils.isEmpty(jsonBean.getPartyMember().get(0).getName())) {
                    tvPUser.setText(jsonBean.getPartyMember().get(0).getName());
                    tvPUser.setVisibility(View.VISIBLE);
                } else {
                    tvPUser.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getPartyMember().get(0).getPosition())) {
                    tvPPosition.setText(jsonBean.getPartyMember().get(0).getPosition());
                    tvPPosition.setVisibility(View.VISIBLE);
                } else {
                    tvPPosition.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getPartyMember().get(0).getPromise())) {
                    tvPCommitment.setText(jsonBean.getPartyMember().get(0).getPromise());
                    tvPCommitment.setVisibility(View.VISIBLE);
                } else {
                    tvPCommitment.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getPartyMember().get(0).getPhone())) {
                    tvPPhone.setText(jsonBean.getPartyMember().get(0).getPhone());
                    tvPPhone.setVisibility(View.VISIBLE);
                } else {
                    tvPPhone.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getPartyMember().get(0).getTime())) {
                    tvPTime.setText(jsonBean.getPartyMember().get(0).getTime());
                    tvPTime.setVisibility(View.VISIBLE);
                } else {
                    tvPTime.setVisibility(View.GONE);
                }
                llParty.setVisibility(View.VISIBLE);
            } else {
                llParty.setVisibility(View.GONE);
            }

            if (jsonBean.getMerchants() != null) {

                if (!TextUtils.isEmpty(jsonBean.getMerchants().getHouseOwnerName())) {
                    tvMUser.setText(jsonBean.getMerchants().getHouseOwnerName());
                    tvMUser.setVisibility(View.VISIBLE);
                } else {
                    tvMUser.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getMerchants().getType())) {
                    tvMType.setText(jsonBean.getMerchants().getType());
                    tvMType.setVisibility(View.VISIBLE);
                } else {
                    tvMType.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getMerchants().getName())) {
                    tvMName.setText(jsonBean.getMerchants().getName());
                    tvMName.setVisibility(View.VISIBLE);
                } else {
                    tvMName.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getMerchants().getTime())) {
                    tvMTime.setText(jsonBean.getMerchants().getTime());
                    tvMTime.setVisibility(View.VISIBLE);
                } else {
                    tvMTime.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getMerchants().getUserName())) {
                    tvMOwner.setText(jsonBean.getMerchants().getUserName());
                    tvMOwner.setVisibility(View.VISIBLE);
                } else {
                    tvMOwner.setVisibility(View.GONE);
                }

                String url = jsonBean.getMerchants().getStorephotos();
                String name = jsonBean.getMerchants().getNameList();
                llMerchant.setVisibility(View.VISIBLE);
            } else {
                llMerchant.setVisibility(View.GONE);
            }
        } else {
            llHouse.setVisibility(View.GONE);
            llParty.setVisibility(View.GONE);
            llMerchant.setVisibility(View.GONE);
        }
    }
}
