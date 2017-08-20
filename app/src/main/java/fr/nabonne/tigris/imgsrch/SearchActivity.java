package fr.nabonne.tigris.imgsrch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    MVPContracts.ISearchModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        model = new GoogleImageSearchParser();
        model.query(new MVPContracts.ISearchModel.Query("dog"),
                new MVPContracts.ISearchModel.QueryObserver() {
                    @Override
                    public void onResults(MVPContracts.ISearchModel.Query query) {
                        String toastString = query.errorString;
                        if (query.results != null) {
                            toastString = query.results.size() + " results";
                        }
                        if (toastString == null)
                            toastString = "UNKNOWN ERROR";
                        Toast.makeText(SearchActivity.this, toastString, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
