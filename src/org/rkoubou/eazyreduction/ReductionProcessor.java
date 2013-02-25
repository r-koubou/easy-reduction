
package org.rkoubou.eazyreduction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;

/**
 * Implementation of Image reduction 縮小処理の実装。
 */
public class ReductionProcessor
{

    //////////////////////////////////////////////////////////////////////////
    /**
     * 出力先のパス文字列を作成する。
     */
    static public String makePath( ReductionContext ctx, float scale )
    {
        return makePath(
                ctx.getSaveDir(),
                ctx.getCurrentFileName(),
                ctx.getCurrentBitmap(),
                scale,
                ctx.getFormat()
        );
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 出力先のパス文字列を作成する。
     */
    static public String makePath( String saveDir, String fileName, Bitmap source, float scale, CompressFormat format )
    {
        int w = source.getWidth();
        int h = source.getHeight();

        String path = saveDir + File.separator + fileName + "-" + (int)( w * scale ) + "x" + (int)( h * scale );

        switch( format )
        {
            case JPEG: path += ".jpg"; break;
            case PNG:  path += ".png"; break;
            default:
            {
                return null;
            }
        }

        return path;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 画像のリサイズ、出力
     */
    static synchronized public boolean resize( ReductionContext ctx, float scale )
    {
        String path = makePath( ctx.getSaveDir(), ctx.getCurrentFileName(),ctx.getCurrentBitmap(), scale, ctx.getFormat() );

        return resize(
                ctx.getCurrentBitmap(),
                path,
                ctx.getFormat(),
                scale,
                ctx.getQuality()
        );
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 画像のリサイズ、出力
     */
    static synchronized public boolean resize( Bitmap source, String destPath, CompressFormat format, float scale, int quority )
    {
        return resize( source, new File( destPath ), format, scale, quority );
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 画像のリサイズ、出力
     */
    static synchronized public boolean resize( Bitmap source, File dest, CompressFormat format, float scale, int quority )
    {
        int w = source.getWidth();
        int h = source.getHeight();

        try
        {
            Matrix mtx = new Matrix();
            mtx.setScale( scale, scale );
            Bitmap newBitmap = Bitmap.createBitmap( source, 0, 0, w, h, mtx, true );

            w = newBitmap.getWidth();
            h = newBitmap.getHeight();

            writeDataFile( dest, bmp2data( newBitmap, format, quority ) );
            return true;

        }
        catch( Throwable e )
        {
            e.printStackTrace();
            return false;
        }
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * Bitmap→バイトデータ
     */
    static private byte[] bmp2data( Bitmap source, Bitmap.CompressFormat format, int quality )
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream( 64 * 1024 );
        source.compress( format, quality, os );
        return os.toByteArray();
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * ファイルへのバイトデータ書き込み
     */
    static private void writeDataFile( File path, byte[] buff ) throws IOException
    {
        OutputStream out = null;
        try
        {
            out = new FileOutputStream( path );
            out.write( buff, 0, buff.length );
        }
        finally
        {
            try { out.flush(); } catch( Exception e ) {}
            try { out.close(); } catch( Exception e ) {}
        }
    }
}
