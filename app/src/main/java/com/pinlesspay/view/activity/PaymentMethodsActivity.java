package com.pinlesspay.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyButton;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.Bank;
import com.pinlesspay.model.CreditCard;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;
import com.pinlesspay.view.adapter.SpinnerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 8/2/2017.
 */

public class PaymentMethodsActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private String TAG = PaymentMethodsActivity.this.getClass().getName();
    private Activity activity;
    private LinearLayout payment_methods_list, waterfall_layout;
    private ArrayList<CreditCard> creditCardArrayList = new ArrayList<>();
    private ArrayList<Bank> bankArrayList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private TextInputLayout input_layout_routing_number, input_layout_account_number, input_layout_account_name;
    private EditText et_routing_number, et_account_number, et_account_name, et_card_name, /*edt_cvv,*/
            edt_yy, edt_mm, et_card_number;
    //    private MyTextView txt_account_type;
    private View vw_card_number, vw_card_name, vw_mm, vw_yy/*, vw_cvv*/;
    String[] title = null;
    private String spinner_item = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        activity = PaymentMethodsActivity.this;

        title = getResources().getStringArray(R.array.account_type_array);


        payment_methods_list = (LinearLayout) findViewById(R.id.payment_methods_list);
        waterfall_layout = (LinearLayout) findViewById(R.id.waterfall_layout);
        LinearLayout layout_add_card = (LinearLayout) findViewById(R.id.layout_add_card);
        LinearLayout layout_add_bank = (LinearLayout) findViewById(R.id.layout_add_bank);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(this);
        layout_add_bank.setOnClickListener(this);
        layout_add_card.setOnClickListener(this);

        if (!Preferences.readString(activity, Preferences.BANKING_ENABLED, "").equalsIgnoreCase("Y")) {
            layout_add_bank.setVisibility(View.GONE);
        } else
            layout_add_bank.setVisibility(View.VISIBLE);


        // list of credit cards
        Utils.showLoading(activity);
        ModelManager.getInstance().getPaymentManager().getCreditCard(activity, true, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_add_bank:
                addBankAccount(0, 0);
                break;
            case R.id.layout_add_card:
                addCreditCard(0, 0);
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void setData() {
        if (payment_methods_list.getChildCount() > 0)
            payment_methods_list.removeAllViews();
        if (creditCardArrayList == null)
            creditCardArrayList = new ArrayList<>();
        if (bankArrayList == null)
            bankArrayList = new ArrayList<>();

        if (creditCardArrayList.size() == 0 && bankArrayList.size() == 0) {
            waterfall_layout.setVisibility(View.VISIBLE);
            payment_methods_list.setVisibility(View.GONE);
            return;
        } else {
            waterfall_layout.setVisibility(View.GONE);
            payment_methods_list.setVisibility(View.VISIBLE);
        }

        if (creditCardArrayList.size() > 0)
            createCreditList();
        if (bankArrayList.size() > 0)
            createBankList();
    }

    private void createCreditList() {
        for (int i = 0; i < creditCardArrayList.size(); i++) {
            layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.row_payment_list, null);

            ImageView icon_account = (ImageView) view.findViewById(R.id.icon_account);
            ImageView icon_option = (ImageView) view.findViewById(R.id.icon_option);
            MyTextView txt_card_name = (MyTextView) view.findViewById(R.id.txt_card_name);
            MyTextView txt_card_number = (MyTextView) view.findViewById(R.id.txt_card_number);
            MyTextView txt_default = (MyTextView) view.findViewById(R.id.txt_default);

            if (!Utils.isEmptyString(creditCardArrayList.get(i).getNickName()))
                txt_card_name.setText(creditCardArrayList.get(i).getNickName());
            else
                txt_card_name.setText(activity.getString(R.string.na));
            txt_card_number.setText(creditCardArrayList.get(i).getMaskCardNumber());
            if (creditCardArrayList.get(i).getIsDefault().equalsIgnoreCase("Y"))
                txt_default.setVisibility(View.VISIBLE);
            else
                txt_default.setVisibility(View.INVISIBLE);

            if (creditCardArrayList.get(i).getCardTypeCode().equalsIgnoreCase("MasterCard"))
                icon_account.setImageResource(R.drawable.mastercard_round);
            else if (creditCardArrayList.get(i).getCardTypeCode().equalsIgnoreCase("Amex"))
                icon_account.setImageResource(R.drawable.american_round);
            else if (creditCardArrayList.get(i).getCardTypeCode().equalsIgnoreCase("Discover"))
                icon_account.setImageResource(R.drawable.discover_round);
            else if (creditCardArrayList.get(i).getCardTypeCode().equalsIgnoreCase("Visa"))
                icon_account.setImageResource(R.drawable.visa_round);
            else if (creditCardArrayList.get(i).getCardTypeCode().equalsIgnoreCase("DinnersClub"))
                icon_account.setImageResource(R.drawable.visa_round);
            else if (creditCardArrayList.get(i).getCardTypeCode().equalsIgnoreCase("JCB"))
                icon_account.setImageResource(R.drawable.visa_round);
            else if (creditCardArrayList.get(i).getCardTypeCode().equalsIgnoreCase("DINERS"))
                icon_account.setImageResource(R.drawable.visa_round);

            final int position = i;
            icon_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlert(creditCardArrayList.get(position).getNickName(), 0);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addCreditCard(1, position);
                }
            });

            payment_methods_list.addView(view);
        }
    }

    private void createBankList() {
        for (int i = 0; i < bankArrayList.size(); i++) {
            layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.row_payment_list, null);

            ImageView icon_account = (ImageView) view.findViewById(R.id.icon_account);
            ImageView icon_option = (ImageView) view.findViewById(R.id.icon_option);
            MyTextView txt_card_name = (MyTextView) view.findViewById(R.id.txt_card_name);
            MyTextView txt_card_number = (MyTextView) view.findViewById(R.id.txt_card_number);
            MyTextView txt_default = (MyTextView) view.findViewById(R.id.txt_default);

            if (!Utils.isEmptyString(bankArrayList.get(i).getNickName()))
                txt_card_name.setText(bankArrayList.get(i).getNickName());
            else
                txt_card_name.setText(activity.getString(R.string.na));

            txt_card_number.setText(bankArrayList.get(i).getMaskBankAccountNum());
            if (bankArrayList.get(i).getIsDefault().equalsIgnoreCase("Y"))
                txt_default.setVisibility(View.VISIBLE);
            else
                txt_default.setVisibility(View.INVISIBLE);

            icon_account.setImageResource(R.drawable.bank_round);

            final int position = i;
            icon_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlert(bankArrayList.get(position).getNickName(), 1);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addBankAccount(1, position);
                }
            });

            payment_methods_list.addView(view);
        }
    }

    private void showAlert(String msg, final int type) {
        String message = getString(R.string.delete) + " " + msg + "?";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(getString(R.string.caps_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == 0)
                            Utils.showMessage(activity, "Credit Card Delete operation will be performed");
                        else {
                            Utils.showMessage(activity, "Bank Account Delete operation will be performed");
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // show it
        alertDialog.show();
    }

    private void addCreditCard(final int type, int position) {
        final Dialog dialog = new Dialog(activity, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_add_credit_card);
//Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        //        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        // set the custom dialog components - text, image and button
        et_card_number = (EditText) dialog.findViewById(R.id.et_card_number);
        edt_mm = (EditText) dialog.findViewById(R.id.edt_mm);
        edt_yy = (EditText) dialog.findViewById(R.id.edt_yy);
//        edt_cvv = (EditText) dialog.findViewById(R.id.edt_cvv);
        et_card_name = (EditText) dialog.findViewById(R.id.et_card_name);
        MyButton btn_add = (MyButton) dialog.findViewById(R.id.btn_add);

        vw_card_name = dialog.findViewById(R.id.vw_card_name);
        vw_card_number = dialog.findViewById(R.id.vw_card_number);
//        vw_cvv = dialog.findViewById(R.id.vw_cvv);
        vw_mm = dialog.findViewById(R.id.vw_mm);
        vw_yy = dialog.findViewById(R.id.vw_yy);

        et_card_number.setOnFocusChangeListener(this);
        edt_mm.setOnFocusChangeListener(this);
        edt_yy.setOnFocusChangeListener(this);
//        edt_cvv.setOnFocusChangeListener(this);
        et_card_name.setOnFocusChangeListener(this);

        if (type == 1) {
            et_card_number.setText(creditCardArrayList.get(position).getMaskCardNumber());
            edt_mm.setText(creditCardArrayList.get(position).getCCardExpMM());
            et_card_name.setText(creditCardArrayList.get(position).getNickName());
            edt_yy.setText(creditCardArrayList.get(position).getCCardExpYYYY());
        }

        // if button is clicked, close the custom dialog
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_card_number.getText().toString().trim().length() == 0) {
                    requestFocus(et_card_number);
                    Utils.showMessage(activity, getString(R.string.please_enter_card_number));
                } else if (edt_mm.getText().toString().trim().length() == 0) {
                    requestFocus(edt_mm);
                    Utils.showMessage(activity, getString(R.string.please_enter_mm));
                } else if (edt_yy.getText().toString().trim().length() == 0) {
                    requestFocus(edt_yy);
                    Utils.showMessage(activity, getString(R.string.please_enter_yy));
//                } else if (edt_cvv.getText().toString().trim().length() == 0) {
//                    requestFocus(edt_cvv);
//                    Utils.showMessage(activity, getString(R.string.please_enter_cvv));
                } else if (et_card_name.getText().toString().trim().length() == 0) {
                    requestFocus(et_card_name);
                    Utils.showMessage(activity, getString(R.string.please_enter_card_name));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
                        jsonObject.put("Action", "CreditCardAccount");
                        jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
                        JSONObject jsonObject1 = new JSONObject();
                        if (type == 0) {
                            jsonObject1.put("CardNumber", et_card_number.getText().toString().trim());
                            jsonObject1.put("CCardExpMM", edt_mm.getText().toString().trim());
                            jsonObject1.put("CCardExpYYYY", edt_yy.getText().toString().trim());
                        }
                        jsonObject1.put("NickName", et_card_name.getText().toString().trim());
                        jsonObject.put("data", jsonObject1.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PPLog.e("JSON DATA : ", jsonObject.toString());
                    Utils.showLoading(activity);
                    if (type == 0)
                        ModelManager.getInstance().getPaymentManager().addCreditCard(activity, jsonObject);
                    else
                        ModelManager.getInstance().getPaymentManager().addCreditCard(activity, jsonObject);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void addBankAccount(final int type, int position) {
        final Dialog dialog = new Dialog(activity, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_add_bank_acc);
//Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        //        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        // set the custom dialog components - text, image and button
        input_layout_routing_number = (TextInputLayout) dialog.findViewById(R.id.input_layout_routing_number);
        input_layout_account_number = (TextInputLayout) dialog.findViewById(R.id.input_layout_account_number);
        input_layout_account_name = (TextInputLayout) dialog.findViewById(R.id.input_layout_account_name);
        et_routing_number = (EditText) dialog.findViewById(R.id.et_routing_number);
        et_account_number = (EditText) dialog.findViewById(R.id.et_account_number);
        et_account_name = (EditText) dialog.findViewById(R.id.et_account_name);
        MyButton btn_add = (MyButton) dialog.findViewById(R.id.btn_add);

//        txt_account_type = (MyTextView) dialog.findViewById(R.id.txt_account_type);
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner_account_type);

        SpinnerAdapter adapter = new SpinnerAdapter(activity, title);
        spinner.setAdapter(adapter);

        et_routing_number.addTextChangedListener(new MyTextWatcher(et_routing_number));
        et_account_number.addTextChangedListener(new MyTextWatcher(et_account_number));
        et_account_name.addTextChangedListener(new MyTextWatcher(et_account_name));

        if (type == 1) {
            et_account_name.setText(bankArrayList.get(position).getNickName());
            et_account_number.setText(bankArrayList.get(position).getMaskBankAccountNum());
            et_routing_number.setText(bankArrayList.get(position).getMaskBankRoutingNum());

            if (bankArrayList.get(position).getBankAccountType().equalsIgnoreCase("S")) {
                spinner_item = title[1];
                spinner.setSelection(1);
            } else if (bankArrayList.get(position).getBankAccountType().equalsIgnoreCase("C")) {
                spinner_item = title[2];
                spinner.setSelection(2);
            } else {
                spinner_item = title[0];
                spinner.setSelection(0);
            }
        }

        // if button is clicked, close the custom dialog
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_routing_number.getText().toString().trim().length() == 0) {
                    requestFocus(et_routing_number);
                    Utils.showMessage(activity, getString(R.string.please_enter_rounting_number));
                } else if (et_account_number.getText().toString().trim().length() == 0) {
                    requestFocus(et_account_number);
                    Utils.showMessage(activity, getString(R.string.please_enter_account_number));
                } else if (spinner_item.equalsIgnoreCase(getString(R.string.account_type))) {
                    Utils.showMessage(activity, getString(R.string.please_select_account_type));
                } else if (et_account_name.getText().toString().trim().length() == 0) {
                    requestFocus(et_account_name);
                    Utils.showMessage(activity, getString(R.string.please_enter_account_name));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
                        jsonObject.put("Action", "BankAccount");
                        jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
                        JSONObject jsonObject1 = new JSONObject();
                        if (type == 0) {
                            jsonObject1.put("BankAccountNum", et_account_number.getText().toString().trim());
                            jsonObject1.put("BankRoutingNum", et_routing_number.getText().toString().trim());
                            jsonObject1.put("BankAccountType", spinner_item);
                        }
                        jsonObject1.put("NickName", et_account_name.getText().toString().trim());
                        jsonObject.put("data", jsonObject1.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PPLog.e("JSON DATA : ", jsonObject.toString());
                    Utils.showLoading(activity);
                    if (type == 0)
                        ModelManager.getInstance().getPaymentManager().addBankAccount(activity, jsonObject);
                    else
                        ModelManager.getInstance().getPaymentManager().addBankAccount(activity, jsonObject);
                    dialog.dismiss();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                spinner_item = title[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        dialog.show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_card_name:
                if (hasFocus) {
                    et_card_name.requestFocus();
                    vw_card_name.setBackgroundColor(Utils.setColor(activity, R.color.light_blue));
                } else
                    vw_card_name.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                break;
            case R.id.et_card_number:
                if (hasFocus) {
                    et_card_number.requestFocus();
                    vw_card_number.setBackgroundColor(Utils.setColor(activity, R.color.light_blue));
                } else
                    vw_card_number.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                break;
            case R.id.edt_mm:
                if (hasFocus) {
                    edt_mm.requestFocus();
                    vw_mm.setBackgroundColor(Utils.setColor(activity, R.color.light_blue));
                } else
                    vw_mm.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                break;
            case R.id.edt_yy:
                if (hasFocus) {
                    edt_yy.requestFocus();
                    vw_yy.setBackgroundColor(Utils.setColor(activity, R.color.light_blue));
                } else
                    vw_yy.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                break;
//            case R.id.edt_cvv:
//                if (hasFocus) {
//                    edt_cvv.requestFocus();
//                    vw_cvv.setBackgroundColor(Utils.setColor(activity, R.color.light_blue));
//                } else
//                    vw_cvv.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
//                break;
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_routing_number:
                    validateName(et_routing_number, input_layout_routing_number, getString(R.string.routing_number));
                    break;
                case R.id.et_account_number:
                    validateName(et_account_number, input_layout_account_number, getString(R.string.account_number));
                    break;
                case R.id.et_account_name:
                    validateName(et_account_name, input_layout_account_name, getString(R.string.account_name));
                    break;
            }
        }
    }

    private boolean validateName(EditText inputName, TextInputLayout inputLayoutName, String message) {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(message);
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    public void onEventMainThread(String message) {
        if (message.equalsIgnoreCase("CreditCard True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "CreditCard True");
            creditCardArrayList = ModelManager.getInstance().getPaymentManager().getCreditCard(activity, false, 1);
            if (Preferences.readString(activity, Preferences.BANKING_ENABLED, "").equalsIgnoreCase("Y")) {
                Utils.showLoading(activity);
                ModelManager.getInstance().getPaymentManager().getBank(activity, true, 1);
            } else
                setData();
        } else if (message.contains("CreditCard False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "CreditCard False");
            Utils.dismissLoading();
            if (Preferences.readString(activity, Preferences.BANKING_ENABLED, "").equalsIgnoreCase("Y")) {
                Utils.showLoading(activity);
                ModelManager.getInstance().getPaymentManager().getBank(activity, true, 1);
            } else
                setData();
        } else if (message.equalsIgnoreCase("BankList True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "BankList True");
            bankArrayList = ModelManager.getInstance().getPaymentManager().getBank(activity, false, 1);
            setData();
        } else if (message.contains("BankList False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "BankList False");
            Utils.dismissLoading();
            setData();
        } else if (message.equalsIgnoreCase("AddCC True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "AddCC True");
            Utils.showLoading(activity);
            ModelManager.getInstance().getPaymentManager().getCreditCard(activity, true, 1);
        } else if (message.contains("AddCC False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "AddCC False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("AddBankAccount True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "AddBankAccount True");
            Utils.showLoading(activity);
            ModelManager.getInstance().getPaymentManager().getCreditCard(activity, true, 1);
        } else if (message.contains("AddBankAccount False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "AddBankAccount False");
            Utils.dismissLoading();
        }

    }
}

