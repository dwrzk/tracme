package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class AndroidLog extends Activity {
  
  public File file;
  public File path;
  
  public AndroidLog(String filename) {
    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);    
    file = new File(path, filename);
  }
  
  public void save(String data)
  {
    try
    {
      // Make sure the Pictures directory exists.
      path.mkdirs();
      FileOutputStream fos = new FileOutputStream(file, true);
      fos.write(data.getBytes());
      fos.close();
      System.out.println("File successfully saved.");
    }
    catch (Exception ex)
    {
      System.out.println("Error saving file: " + ex.getLocalizedMessage());
    }
  }

  public String load()
  {
    try
    {
      FileInputStream fis = new FileInputStream(file);
      BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
      String line = null, input="";
      while ((line = reader.readLine()) != null)
          input += line + "\n";
      reader.close();
      fis.close();
      System.out.println("File successfully loaded.");
      return input;
    }
    catch (Exception ex)
    {
      System.out.println("Error loading file: " + ex.getLocalizedMessage());
      return "";
    }
  }
  
  // This function is supposed to print to the screen of the android device but it isnt working
  public void toast(String text)
  {
    Context context = getApplicationContext();
    Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    toast.show();
  }  
}