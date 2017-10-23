package in.ashnehete.cards;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Aashish Nehete on 23-Oct-17.
 */

public class Util {
    public static void showToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}
