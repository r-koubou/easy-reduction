
package org.rkoubou.eazyreduction;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class ReductionContext implements Constants
{
    private final Context owner;

    private Bitmap currentBitmap;
    private String currentFileName;
    private String saveDir = DEFAULT_SAVE_DIR;

    private Bitmap.CompressFormat format = CompressFormat.JPEG;
    private int quality = 90;

    //////////////////////////////////////////////////////////////////////////
    /**
     * Ctor.
     */
    public ReductionContext( Context owner_ )
    {
        owner = owner_;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 設定読み込み
     */
    public boolean loadPreference()
    {
        boolean initialize = false;

        SharedPreferences p = owner.getSharedPreferences( CONFIG_PREFERENCE_NAME, Context.MODE_PRIVATE );
        saveDir = p.getString( KEY_SAVEDIR, "" );
        quality = p.getInt( KEY_QUORITY, 90 );
        format  = CompressFormat.valueOf( CompressFormat.class, p.getString( KEY_FORMAT, CompressFormat.JPEG.name() ) );

        if( "".equals( saveDir ) )
        {
            initialize = true;
        }
        else
        {
            File d = new File( saveDir );
            initialize =( ! d.exists() || ! d.isDirectory() );
        }

        if( initialize )
        {
            saveDir = DEFAULT_SAVE_DIR;
        }

        return initialize;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 設定書き込み
     */
    public void savePreference()
    {
        SharedPreferences p = owner.getSharedPreferences( CONFIG_PREFERENCE_NAME, Context.MODE_PRIVATE );
        Editor e = p.edit();
        e.putString( KEY_SAVEDIR, saveDir );
        e.putInt( KEY_QUORITY, quality );
        e.putString( KEY_FORMAT, format.name() );
        e.commit();
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @return Returns the currentBitmap.
     */
    public Bitmap getCurrentBitmap()
    {
        return currentBitmap;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @param currentBitmap The currentBitmap to set.
     */
    public void setCurrentBitmap( Bitmap currentBitmap )
    {
        this.currentBitmap = currentBitmap;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @return Returns the currentFileName.
     */
    public String getCurrentFileName()
    {
        return currentFileName;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @param currentFileName The currentFileName to set.
     */
    public void setCurrentFileName( String currentFileName )
    {
        this.currentFileName = currentFileName;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @return Returns the saveDir.
     */
    public String getSaveDir()
    {
        return saveDir;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @param saveDir The saveDir to set.
     */
    public void setSaveDir( String saveDir )
    {
        this.saveDir = saveDir;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @return Returns the format.
     */
    public Bitmap.CompressFormat getFormat()
    {
        return format;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @param format The format to set.
     */
    public void setFormat( Bitmap.CompressFormat format )
    {
        this.format = format;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @return Returns the quality.
     */
    public int getQuality()
    {
        return quality;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @param quality The quality to set.
     */
    public void setQuality( int quality )
    {
        this.quality = quality;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @return Returns the owner.
     */
    public Context getOwner()
    {
        return owner;
    }
}
