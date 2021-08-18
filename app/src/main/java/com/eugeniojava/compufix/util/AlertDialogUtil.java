package com.eugeniojava.compufix.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.eugeniojava.compufix.R;

public class AlertDialogUtil {

    private AlertDialogUtil() {
    }

    public static String validateEditText(Context context, EditText editText, int messageId) {
        String text = editText.getText().toString();

        if (StringUtil.isEmpty(text)) {
            showErrorAlertDialog(context, messageId);
            editText.setText(null);
            editText.requestFocus();

            return null;
        }
        return text.trim();
    }

    public static boolean validateRadioGroup(Context context, RadioGroup radioGroup, int messageId) {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            showErrorAlertDialog(context, messageId);

            return false;
        }
        return true;
    }

    public static void showErrorAlertDialog(Context context, int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.alert_dialog_util_alert_dialog_error_title);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(messageId);
        builder.setNeutralButton(R.string.alert_dialog_util_alert_dialog_ok_button, ((dialog, which) -> {
        }));

        builder.create().show();
    }

    public static void showConfirmationAlertDialog(Context context, String message,
                                                   DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.alert_dialog_util_alert_dialog_confirmation_title);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.alert_dialog_util_alert_dialog_yes_button, onClickListener);
        builder.setNegativeButton(R.string.alert_dialog_util_alert_dialog_no_button, onClickListener);

        builder.create().show();
    }
}
