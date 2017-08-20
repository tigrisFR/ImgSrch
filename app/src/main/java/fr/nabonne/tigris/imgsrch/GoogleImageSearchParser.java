package fr.nabonne.tigris.imgsrch;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by tigris on 8/20/17.
 * Each Google image search response body contains the 100 first results as JSON blobs under "div.rg_meta" div
 */

public class GoogleImageSearchParser implements MVPContracts.ISearchModel {
    private Query curQuery;
    private QueryObserver curObserver;

    private final String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
    private final String apiUrl = "https://www.google.com/search?";//String url = "https://www.google.com/search?site=imghp&tbm=isch&source=hp&q=kittens&gws_rd=cr";

    @Override
    public void query(Query query, QueryObserver observer) {
        this.curQuery = query;// If curQuery is non null (incomplete query) it will be discarded
        this.curObserver = observer;

//        String queryString = query.queryString;//TODO format queryString to valid URL param
        String queryString = "puppies";
        String queryParams = "q=" + queryString + "&tbm=isch";

        String queryUrl = apiUrl + queryParams;
        try {
            URL url = new URL(queryUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            curQuery.errorString = "MalformedURLException";
            onQueryResults();
        }

        //do query in the background
        AsyncTask backgroundTask = new GoogleImageSearchTask(queryUrl, userAgent, this);
        Query[] params = new Query[1];
        params[0] = curQuery;
        backgroundTask.execute(params);
    }

    static class GoogleImageSearchTask extends AsyncTask<Query, Object, Object> {
        final String queryUrl;
        final String userAgent;
        final GoogleImageSearchParser asyncTaskObserver;

        GoogleImageSearchTask(String queryUrl, String userAgent, GoogleImageSearchParser asyncTaskObserver) {
            this.queryUrl = queryUrl;
            this.userAgent = userAgent;
            this.asyncTaskObserver = asyncTaskObserver;
        }

        @Override
        protected Query doInBackground(Query... params) {
            Query query = params[0];

            Document doc = null;
            try {
                doc = Jsoup.connect(queryUrl).userAgent(userAgent).referrer("https://www.google.com/").get();
            } catch (IOException e) {
                e.printStackTrace();
                query.errorString = "IOException on URL";
            }
            Elements elements = doc.select("div.rg_meta");


            if (query.results == null)
                query.results = new ArrayList<ImageData>();
            JSONObject imgJson;
            try {
                for (Element element : elements) {
                    if (element.childNodeSize() > 0) {
                        imgJson = new JSONObject(element.childNode(0).toString());
                        final String url = imgJson.getString("ou");
                        final String id = imgJson.getString("id");
                        query.results.add(new ImageData(id, url));
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                query.errorString = "JSONException";
            }
            return query;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            this.asyncTaskObserver.onQueryResults();
        }
    }

    private void onQueryResults() {
        if (curObserver == null || curObserver == null) {
            Log.e(this.getClass().getSimpleName(),
                    "ERROR: missing query/observer references");
            return;
        }
        curObserver.onResults(curQuery);
        curQuery = null;
        curObserver = null;
    }
}
