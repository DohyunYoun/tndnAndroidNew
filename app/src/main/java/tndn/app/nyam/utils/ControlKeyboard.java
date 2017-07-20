package tndn.app.nyam.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Pattern;

/**
 * Created by ellen on 2017. 2. 21..
 */

public class ControlKeyboard {
    /* Hide Keyboard */
    public static void hide(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = activity.getCurrentFocus();
        if (focus != null)
            inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 영문~숫자만 특수문자 제한
     **/
    public static InputFilter filter() {
        InputFilter filterAlphaNum = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
                if (!ps.matcher(source).matches()) {
                    return "";
                }
                return null;
            }
        };
        return filterAlphaNum;
    }

}
