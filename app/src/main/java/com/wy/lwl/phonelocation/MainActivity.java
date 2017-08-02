package com.wy.lwl.phonelocation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wy.lwl.phonelocation.model.Phone;
import com.wy.lwl.phonelocation.mvp.MvpMainView;
import com.wy.lwl.phonelocation.mvp.impl.MvpPresent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MvpMainView {

    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.result_phone)
    TextView resultPhone;
    @BindView(R.id.result_province)
    TextView resultProvince;
    @BindView(R.id.result_type)
    TextView resultType;
    @BindView(R.id.result_carrier)
    TextView resultCarrier;

    MvpPresent mvpPresent;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mvpPresent = new MvpPresent(this);
    }


    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateView() {
        Phone phone = mvpPresent.getPhoneInfo();
        resultPhone.setText(getString(R.string.result_phone) + phone.getTelString());
        resultProvince.setText(getString(R.string.result_province) + phone.getProvince());
        resultType.setText(getString(R.string.result_type) + phone.getCatName());
        resultCarrier.setText(getString(R.string.result_carrier) + phone.getCarrier());

    }

    @Override
    public void showLoding() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "", "加载中...", true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle("");
            progressDialog.setMessage("加载中...");
        }
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        mvpPresent.searchPhone(edPhone.getText().toString());
    }
}
