
package org.rkoubou.eazyreduction;

import java.io.File;

import android.os.Environment;

/**
 * Constants variables.
 */
public interface Constants
{
    /** Toast 表示時間 */
    int TOAST_SHOW_TIME = 5000;

    //-------------------------------------------------------------------------------
    // 保存処理、インテントのキー関係
    //-------------------------------------------------------------------------------
    String CONFIG_PREFERENCE_NAME = "appconfig";
    String DEFAULT_SAVE_DIR       = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES ) + File.separator + "Resized";

    String KEY_SAVEDIR = "saveDir";
    String KEY_QUORITY = "quority";
    String KEY_FORMAT  = "format";

}
