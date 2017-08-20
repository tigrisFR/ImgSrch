package fr.nabonne.tigris.imgsrch;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    MVPContracts.ISearchModel model;
    SearchView searchView;
    @BindView(R.id.search_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        model = new GoogleImageSearchParser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_action_bar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(false);// always visible
        searchView.setQueryHint(getString(R.string.action_search));
        searchView.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);

        //TODO Configure the search info and add any event listeners...
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String queryString) {
        model.query(new MVPContracts.ISearchModel.Query(queryString),
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
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
