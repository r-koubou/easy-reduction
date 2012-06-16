
package org.rkoubou.eazyreduction;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 *
 */
public class InformationActivity extends Activity
{
    //////////////////////////////////////////////////////////////////////////
    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.info );

        initComponents();
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     *
     */
    private void initComponents()
    {
        PackageManager pkg = getPackageManager();
        PackageInfo info;
        Resources res = getResources();
        ListView list = (ListView)findViewById( R.id.infoListView );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1 );

        list.setAdapter( adapter );

        try
        {
            info   = pkg.getPackageInfo( getPackageName(), PackageManager.GET_META_DATA );
            adapter.add( res.getString( R.string.app_info_name ) + res.getString( R.string.app_name ) );
            adapter.add( res.getString( R.string.app_info_author ) );
            adapter.add( res.getString( R.string.app_info_version ) + info.versionName );
            setTitle( getResources().getString( R.string.app_information ) );
        }
        catch( Throwable e )
        {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     *
     */
    public void onClickCloseButton( View v )
    {
        finish();
    }
}
