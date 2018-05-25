package com.yaheen.cis.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.yaheen.cis.R;
import com.yaheen.cis.base.NfcInterface;
import com.yaheen.cis.util.ProgersssDialog;
import com.yaheen.cis.util.nfc.AESUtils;
import com.yaheen.cis.util.nfc.Converter;
import com.yaheen.cis.util.nfc.NfcVUtil;
import com.yaheen.cis.util.toast.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;

import static com.yaheen.cis.util.nfc.NFCUtils.ByteArrayToHexString;
import static com.yaheen.cis.util.nfc.NFCUtils.toStringHex;

public class BaseActivity extends AppCompatActivity implements NfcInterface {

    private NfcB nfcbTag;
    private Tag tagFromIntent;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private IntentFilter[] mNdefExchangeFilters;

    private ProgersssDialog progersssDialog;

    private String ex_id = "", types = "";

    private TextView tvContent;

    private ToastUtils toastUtils = new ToastUtils();

    //网页按钮标记
    protected String typeStr = "";

    //是否可以读芯片
    private boolean load = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNFC();
    }

    protected void setTitleContent(int content) {
        tvContent = findViewById(R.id.tv_title_content);
        if (tvContent != null) {
            tvContent.setText(content);
        }
    }

    private void initNFC() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);// 设备注册
        if (mNfcAdapter == null) {
            // 判断设备是否可用
            Toast.makeText(this, "该设备不支持NFC功能", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndefDetected = new IntentFilter(
                NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("*/*");// text/plain
        } catch (IntentFilter.MalformedMimeTypeException e) {
        }

        IntentFilter td = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ttech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        mNdefExchangeFilters = new IntentFilter[]{ndefDetected, ttech, td};
    }

    public void showToast(int string){
        toastUtils.showToast(string,this);
    }

    public void showToast(String string){
        toastUtils.showToast(string,this);
    }

    public void showLoadingDialog() {
        progersssDialog = new ProgersssDialog(BaseActivity.this);
    }

    public void cancelLoadingDialog() {
        if (progersssDialog != null) {
            progersssDialog.dismiss();
        }
    }

    protected void setLoadState(boolean load) {
        this.load = load;
    }

    private void resolvIntent(Intent intent) {
        if (!load) {
            return;
        }
        String action = intent.getAction();
        //toast(action);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            tagFromIntent = getIntent()
                    .getParcelableExtra(NfcAdapter.EXTRA_TAG);
            getresult(tagFromIntent);
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            // 处理该intent
            tagFromIntent = getIntent()
                    .getParcelableExtra(NfcAdapter.EXTRA_TAG);
            getresult(tagFromIntent);

        } else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            types = "Tag";
            tagFromIntent = getIntent()
                    .getParcelableExtra(NfcAdapter.EXTRA_TAG);
            getresult(tagFromIntent);
        }
    }

    void getresult(Tag tag) {
        ArrayList<String> list = new ArrayList<String>();
        types = "";
        for (String string : tag.getTechList()) {
            list.add(string);
            types += string.substring(string.lastIndexOf(".") + 1, string.length()) + ",";
        }
        types = types.substring(0, types.length() - 1);
        if (list.contains("android.nfc.tech.MifareUltralight")) {
            String str = readTagUltralight(tag);
            setNoteBody(str);
        } else if (list.contains("android.nfc.tech.NfcV")) {//完成
            NfcV tech = NfcV.get(tag);
            if (tech != null) {
                try {
                    tech.connect();
                    if (tech.isConnected()) {
                        NfcVUtil nfcVUtil = new NfcVUtil(tech);

                        //写芯片数据
                        String str = "";
//                        byte[] by = str.getBytes();
//                        nfcVUtil.writeBlock((byte) 0x01,by);
//                        str="5";
//                        by= str.getBytes();
//                        nfcVUtil.writeBlock((byte) 0x02,by);
//                        str="2";
//                        by= str.getBytes();
//                        nfcVUtil.writeBlock((byte) 0x03,by);
//                        str="1";
//                        by= str.getBytes();
//                        nfcVUtil.writeBlock((byte) 0x04,by);
//                        str="0";
//                        by= str.getBytes();
//                        nfcVUtil.writeBlock((byte) 0x05,by);
//                        str="1";
//                        by= str.getBytes();
//                        nfcVUtil.writeBlock((byte) 0x06,by);

                        str = nfcVUtil.readBlocks(0, 27);
                        tech.close();
                        setNoteBody(str);
                    }
                } catch (IOException e) {

                }
            }
        } else if (list.contains("android.nfc.tech.NdefFormatable")) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            setNoteBody(new String(payload));
        }
    }

    private NdefMessage[] getNdefMessages(Intent intent) {
        //读取nfc数据
        // Parse the intent

        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{
                        record
                });
                msgs = new NdefMessage[]{
                        msg
                };
            }
        } else {
            finish();
        }
        return msgs;
    }

    private String readTagUltralight(Tag tag) {
        MifareUltralight mifare = MifareUltralight.get(tag);
        try {
            mifare.connect();
            StringBuffer sb = new StringBuffer();
            byte[] no10 = new byte[4];  //校验芯片
            byte[] no11 = new byte[4];  //数据块数量

            byte[] readTag = mifare.readPages(10);

            byte[] readCount = mifare.readPages(11);

            if (readTag.length >= 4) {

                for (int i = 0; i < 4; i++) {
                    no10[i] = readTag[i];
                }

                String tagStr = toStringHex(ByteArrayToHexString(no10));

                if (tagStr.equals("YAHN")) {
                    for (int i = 0; i < 4; i++) {
                        no11[i] = readCount[i];
                    }

                    String countStr = toStringHex(ByteArrayToHexString(no11));
                    int count = Integer.valueOf(countStr.trim());

                    for (int i = 12; i < (count); i++) {
                        byte[] readResult = mifare.readPages(i);
                        if (i % 4 == 0) {
                            if (i == count) {
                                byte[] codeEnd = new byte[4];
                                for (int j = 0; j < 4; j++) {
                                    codeEnd[j] = readResult[j];
                                }
                                sb.append(ByteArrayToHexString(codeEnd));
                            } else {
                                sb.append(ByteArrayToHexString(readResult));
                            }
                        }
                    }
                }
            }
            //  String  str=toStringHex(sb.toString());

            String finalResult = AESUtils.decryptToString(toStringHex(sb.toString()), "X2Am6tVLnwMMX8kVgdDk5w==");
//            String finalResult = toStringHex(sb.toString());

            return finalResult;

        } catch (IOException e) {
//            Log.e(TAG, "IOException while writing MifareUltralight message...", e);
            return "";
        } catch (Exception ee) {
//            Log.e(TAG, "IOException while writing MifareUltralight message...", ee);
            return "";
        } finally {
            if (mifare != null) {
                try {
                    mifare.close();
                } catch (IOException e) {
//                    Log.e(TAG, "Error closing tag...", e);
                }
            }
        }
    }

    @Override
    public void setNoteBody(String body) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
        // 读取uidgetIntent()
        byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        ex_id = Converter.getHexString(myNFCID, myNFCID.length);
        // 读取uidgetIntent()
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNfcAdapter == null || !mNfcAdapter.isEnabled()) {
            return;
        }

        //nfc自动读取芯片内容后调用activity的onResume
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, null, null);
            resolvIntent(getIntent());
        }
    }

}
