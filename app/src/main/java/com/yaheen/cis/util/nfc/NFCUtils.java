package com.yaheen.cis.util.nfc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.provider.Settings;
import android.widget.Toast;

public class NFCUtils {

    private Tag tagFromIntent;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private IntentFilter[] mNdefExchangeFilters;

    private Context context;

    private String ex_id = "", types = "";

    public void initNFC(Context context) {
        this.context = context;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);// 设备注册
        if (mNfcAdapter == null) {
            // 判断设备是否可用
            Toast.makeText(context, "该设备不支持NFC功能", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(context, "请在系统设置中先启用NFC功能！", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            return;
        }
        mNfcPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String ByteArrayToHexString(byte[] inarray) {
        // arrays to string
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
}
