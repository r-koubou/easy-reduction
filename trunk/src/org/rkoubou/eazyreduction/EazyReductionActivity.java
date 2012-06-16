package org.rkoubou.eazyreduction;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class EazyReductionActivity extends Activity implements Constants
{
    private ReductionContext reductionContext;

    //////////////////////////////////////////////////////////////////////////
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.main );

        reductionContext = new ReductionContext( this );

        boolean firstBoot = reductionContext.loadPreference();

        Intent intent = getIntent();
        if( intent != null )
        {
            loadImage( intent.getData() );
        }

        if( firstBoot )
        {
            File dir = new File( DEFAULT_SAVE_DIR );
            dir.mkdir();

            AlertDialog.Builder b = new AlertDialog.Builder( this );
            b.setTitle( getString( R.string.welocome ) );
            b.setMessage( getString( R.string.firstMsg1 ) + " '"+ reductionContext.getSaveDir() + "' " + getString( R.string.firstMsg2 ) );
            b.setPositiveButton( getString( R.string.ok ), null );
            b.create().show();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * コンテキストメニューの設定
     */
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        super.onCreateOptionsMenu( menu );

        final Activity pMe = this;

        MenuItem i;
        i = menu.add( getString( R.string.config ) );
        i.setIcon( android.R.drawable.ic_menu_preferences );
        i.setOnMenuItemClickListener( new OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick( MenuItem mi )
            {
                Intent config = new Intent( pMe, ConfigActivity.class );
                config.putExtra( ReductionContext.KEY_SAVEDIR, reductionContext.getSaveDir() );
                config.putExtra( ReductionContext.KEY_QUORITY, reductionContext.getQuality() );
                config.putExtra( ReductionContext.KEY_FORMAT,  reductionContext.getFormat().name() );
                startActivityForResult( config, 1 );
                return true;
            }
        });

        i = menu.add( getString( R.string.app_information ) );
        i.setIcon( android.R.drawable.ic_menu_info_details );
        i.setOnMenuItemClickListener( new OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick( MenuItem mi )
            {
                Intent info = new Intent( pMe, InformationActivity.class );
                startActivity( info );
                return true;
            }
        });

        return true;
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data );

        if( resultCode == RESULT_OK )
        {
            reductionContext.setSaveDir( data.getExtras().getString( ReductionContext.KEY_SAVEDIR ) );
            reductionContext.setQuality( data.getExtras().getInt( ReductionContext.KEY_QUORITY ) );
            reductionContext.setFormat( CompressFormat.valueOf( CompressFormat.class, data.getExtras().getString( ReductionContext.KEY_FORMAT ) ) );
        }
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        reductionContext.savePreference();
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 1/2ボタン押下イベントハンドラ
     */
    synchronized public void onClickHalfButton( View button )
    {
        resize( 0.5f );
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 1/4ボタン押下イベントハンドラ
     */
    synchronized public void onClickQuarterButton( View button )
    {
        resize( 0.25f );
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 1/8ボタン押下イベントハンドラ
     */
    synchronized public void onClickOneEighth( View button )
    {
        resize( 0.125f );
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 右回転ボタン押下イベントハンドラ
     */
    synchronized public void onClickRotationRightButton( View button )
    {
        rotateBitmap( 90 );
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 左回転ボタン押下イベントハンドラ
     */
    synchronized public void onClickRotationLeftButton( View button )
    {
        rotateBitmap( -90 );
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 画像の回転
     */
    synchronized private void rotateBitmap( int rotate )
    {
        if( reductionContext.getCurrentBitmap() == null )
        {
            return;
        }

        Bitmap oldBitmap = reductionContext.getCurrentBitmap();
        try
        {
            Matrix mtx = new Matrix();
            mtx.setRotate( rotate );
            Bitmap newBitmap = Bitmap.createBitmap( oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), mtx, false );

            ( (ImageView)findViewById( R.id.imageView ) ).setImageBitmap( newBitmap );
            reductionContext.setCurrentBitmap( newBitmap );
            oldBitmap.recycle();
        }
        catch( Throwable e )
        {
            e.printStackTrace();
        }

    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 画像のリサイズ、出力
     */
    synchronized private void resize( float scale )
    {
        boolean result;

        Bitmap currentBitmap   = reductionContext.getCurrentBitmap();
        CompressFormat format  = reductionContext.getFormat();
        String saveDir         = reductionContext.getSaveDir();
        String currentFileName = reductionContext.getCurrentFileName();

        String path = ReductionProcessor.makePath( saveDir, currentFileName, currentBitmap, scale, format );

        if( path == null )
        {
            // Unknown Compress format...?
            Toast.makeText( this, getString( R.string.saveNG ), TOAST_SHOW_TIME ).show();
            return;
        }

        result = ReductionProcessor.resize( reductionContext, scale );

        if( result )
        {
            Toast.makeText( this, getString( R.string.saveOK ) + path, TOAST_SHOW_TIME ).show();
        }
        else
        {
            Toast.makeText( this, getString( R.string.saveNG ), TOAST_SHOW_TIME ).show();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * 加工元の画像のロード
     */
    synchronized private void loadImage( Uri uri )
    {
        if( uri == null ){ return; }
        ImageView imageView = (ImageView)findViewById( R.id.imageView );
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap( this.getContentResolver(), uri );
            if( bitmap != null )
            {
                File path = new File( uri.getPath() );
                imageView.setImageBitmap( bitmap );
                reductionContext.setCurrentBitmap( bitmap );
                reductionContext.setCurrentFileName( path.getName().replaceAll( "\\.[^\\,]+$", "" ) );

                // ウインドウタイトルをファイ名込みにしてみる
                setTitle( getResources().getString( R.string.app_name ) + " - " + path.getName() );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
