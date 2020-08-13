package com.example.myqrscannerandgenrator;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;


public class Utils {
    private TextView currentCode;


    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        Log.i("-----------------------", hexKey);
        return TOTP.getOTP(hexKey);
    }

}
