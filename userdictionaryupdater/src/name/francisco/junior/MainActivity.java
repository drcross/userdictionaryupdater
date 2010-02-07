package name.francisco.junior;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.provider.UserDictionary;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	
	private Button btnLoadDictionary;
	private ProgressBar pbLoadDictionary;
	private TextView txtUpdateStatus;
	
	// For progressbar update ui.
	private Handler hHandler = new Handler();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnLoadDictionary = (Button) findViewById(R.id.loadDictionary);
        pbLoadDictionary = (ProgressBar) findViewById(R.id.pbLoadDictionary);
        pbLoadDictionary.setIndeterminate(false);
        
        txtUpdateStatus = (TextView) findViewById(R.id.txtUpdateStatus);
        
        btnLoadDictionary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// To avoid more than one click.
            	btnLoadDictionary.setEnabled(false);
            	
            	new Thread(new Runnable() {
            		
   	             public void run() {
   	            	loadDictionary();
   	             }
   	             
   	         	}).start();
            	
            	
            }
        });                

        
        //UserDictionary.Words.addWord(this, "palavraprograma", 240, UserDictionary.Words.LOCALE_TYPE_ALL);
        
    }

	protected void loadDictionary() {
		File file = new File("/sdcard/CustomDictionary.txt");
        BufferedReader reader = null;
        int numberOfLines = 0;

        try
        {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            // So we can go back when finish reading the number of lines.
            
            
            // repeat until all lines is read
            while ((text = reader.readLine()) != null)
            {
            	numberOfLines++;
            }
            
            pbLoadDictionary.setMax(numberOfLines);
            pbLoadDictionary.setProgress(0);
            
            reader.close();
            
            reader = new BufferedReader(new FileReader(file));
            
            
            // repeat until all lines is read
            while ((text = reader.readLine()) != null)
            {
            	UserDictionary.Words.addWord(this, text, 240, UserDictionary.Words.LOCALE_TYPE_ALL);
            	
            	hHandler.post(new Runnable() {
                    public void run() {
                    	pbLoadDictionary.incrementProgressBy(1);
                    	//pbLoadDictionary.incrementSecondaryProgressBy(1);
                    	txtUpdateStatus.setText(String.format("Loaded word %s  / %s", pbLoadDictionary.getProgress(), pbLoadDictionary.getMax()));
                    }
                });
            	
            	
            	
            	
            }
            
            hHandler.post(new Runnable() {
                public void run() {
                	
                	btnLoadDictionary.setEnabled(true);
                	pbLoadDictionary.setProgress(0);
                	txtUpdateStatus.setText(String.format("Finished loading all %s words from CustomDictionary.txt", pbLoadDictionary.getMax()));
                }
            });
            
            
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
		
	}		 
}