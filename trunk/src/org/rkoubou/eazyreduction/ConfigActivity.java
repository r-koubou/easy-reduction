
package org.rkoubou.eazyreduction;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Application Config Activity
 */
public class ConfigActivity extends Activity implements Constants
{

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.config );
        initComponent();

        Intent from = getIntent();
        if( from != null )
        {
            parseIntentArgs( from );
        }
    }

    /**
     * Construct a UI Components
     */
    private void initComponent()
    {
        Button b;
        SeekBar sb;

        //------------------------------------------------------------------------------
        // Apply
        //------------------------------------------------------------------------------
        b = (Button)findViewById( R.id.configApplyButton );
        b.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                EditText dirEdit       = (EditText)findViewById( R.id.dirEditBox );
                ProgressBar quorityBar = (ProgressBar)findViewById( R.id.quorityLevelBar );

                Intent i = new Intent();
                i.putExtra( KEY_SAVEDIR, dirEdit.getText().toString() );
                i.putExtra( KEY_QUORITY, quorityBar.getProgress() );

                if( ((RadioButton)findViewById( R.id.radioPNG)).isChecked() )
                {
                    i.putExtra( KEY_FORMAT, CompressFormat.PNG.name() );
                }
                else
                {
                    i.putExtra( KEY_FORMAT, CompressFormat.JPEG.name() );
                }

                setResult( RESULT_OK, i );
                finish();
            }
        });

        //------------------------------------------------------------------------------
        // Cancel
        //------------------------------------------------------------------------------
        b = (Button)findViewById( R.id.configCancelButton );
        b.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                abort();
            }
        });

        // mkdir
        b = (Button)findViewById( R.id.configMkDirButton );
        b.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                EditText dirEdit = (EditText)findViewById( R.id.dirEditBox );
                String dir = dirEdit.getText().toString();

                String toastText = dir;
                File d = new File( dir );

                if( d.exists() && d.isDirectory() )
                {
                    toastText = getString( R.string.createDirExist ) + toastText;
                }
                else
                {
                    if( d.mkdir() )
                    {
                        toastText = getString( R.string.createDirOK ) + toastText;
                    }
                    else
                    {
                        toastText = getString( R.string.createDirNG ) + toastText;
                    }
                }

                Toast.makeText( ConfigActivity.this, toastText, TOAST_SHOW_TIME ).show();

            }
        });

        //------------------------------------------------------------------------------
        // Quality
        //------------------------------------------------------------------------------
        sb = (SeekBar)findViewById( R.id.quorityLevelBar );
        sb.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch( SeekBar arg0 )
            {
            }

            @Override
            public void onStartTrackingTouch( SeekBar arg0 )
            {
            }

            @Override
            public void onProgressChanged( SeekBar b, int progress, boolean fromUser )
            {
                TextView tv = (TextView)findViewById( R.id.quorityValue );

                if( progress > 95 )
                {
                    progress = 100;
                }
                else if( progress < 5 )
                {
                    progress = 0;
                }

                progress = ( progress / 10 ) * 10;
                tv.setText( "" + ( progress ) );
                b.setProgress( progress );
            }
        });
    }

    /**
     * Parse intent arguments from Main Activity.
     */
    private void parseIntentArgs( Intent from )
    {
        //------------------------------------------------------------------------------
        // saveDir
        //------------------------------------------------------------------------------
        {
            String saveDir = from.getExtras().getString( KEY_SAVEDIR );
            if( saveDir == null )
            {
                saveDir = "";
            }

            EditText dirEdit = (EditText)findViewById( R.id.dirEditBox );
            dirEdit.setText( saveDir );
        }
        //------------------------------------------------------------------------------
        // quority
        //------------------------------------------------------------------------------
        {
            int q = from.getExtras().getInt( KEY_QUORITY );
            ( (TextView)findViewById( R.id.quorityValue ) ).setText( "" + q );
            ( (ProgressBar)findViewById( R.id.quorityLevelBar) ).setProgress( q );
        }
        //------------------------------------------------------------------------------
        // format
        //------------------------------------------------------------------------------
        {
            String format = from.getExtras().getString( KEY_FORMAT );
            if( format != null )
            {
                RadioGroup rg = (RadioGroup)findViewById( R.id.formatRadioGroup );

                if( CompressFormat.PNG.name().equals( format ) )
                {
                    rg.check( R.id.radioPNG );
                }
                else
                {
                    rg.check( R.id.radioJpeg );
                }
            }
        }
    }

    /**
     * if user canceled.
     */
    private void abort()
    {
        Intent i = new Intent();
        setResult( RESULT_CANCELED, i );
        finish();
    }

}
