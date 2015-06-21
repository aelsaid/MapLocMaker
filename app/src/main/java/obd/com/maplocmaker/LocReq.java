package obd.com.maplocmaker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;




public class LocReq extends Activity {
    /**
     * Called when the activity is first created.
     */

    TextView txt;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_req);
        StrictMode.enableDefaults(); //STRICT MODE ENABLED
        // Create a crude view - this should really be set via the layout resources
        // but since its an example saves declaring them in the XML.
        LinearLayout rootLayout = new LinearLayout(getApplicationContext());
        txt = new TextView(getApplicationContext());
        rootLayout.addView(txt);
        setContentView(rootLayout);


        // Set the text and call the connect function.
        txt.setText("Connecting...");
        //call the method to run the data retreival
        txt.setText(getServerData(KEY_121));


    }

    public static final String KEY_121 = "http://people.aero.und.edu/~elemma/getLocation.php"; //i use my real ip here


    private String getServerData(String returnString) {

        InputStream is = null;

        String result = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Location", "macdonalds"));


        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(KEY_121);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();


        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }


        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        //parse json data
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Log.i("log_tag", "id: " + json_data.getInt("id") +
                                ", loc: " + json_data.getString("name") +
                                ", lat: " + json_data.getDouble("lat") +
                                ", lon: " + json_data.getDouble("lon")
                );
                //Get an output to the screen
                returnString += "\n\t" + jArray.getJSONObject(i);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return returnString;
    }
}

