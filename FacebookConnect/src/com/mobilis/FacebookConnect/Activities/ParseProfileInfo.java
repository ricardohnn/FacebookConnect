package com.mobilis.FacebookConnect.Activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

class Profile{
	String name;
	String pic;
	Profile(){}
}
class ProfileContainer {
    List<Profile> data;
    ProfileContainer(){}
}

public class ParseProfileInfo extends AsyncTask<String, Void, String[]>{

	TextView name;
	ImageView profileImage;
	Bitmap bit;
	
	ParseProfileInfo(TextView nome, ImageView imagem_perfil){
		this.name = nome;
		this.profileImage = imagem_perfil;
	}
	@Override
	protected String[] doInBackground(String... json) {
		Gson gson = new Gson();
    	ProfileContainer container = gson.fromJson(json[0],ProfileContainer.class);
        String[] friends = new String[2];
        friends[0] = container.data.get(0).name;
        friends[1] = container.data.get(0).pic;
        
        //if we couldn't download the image...
        if(friends[1]!=null)
        	this.bit = downloadImage(friends[1]);
        
		return friends;
	}
    protected void onPostExecute(String[] result) {
    	this.name.setText(result[0]);
    	if(this.bit != null )
    		this.profileImage.setImageBitmap(this.bit);

    }
    
    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.
                    decodeStream(stream, null, bmOptions);
            stream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }
    private InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }
}
