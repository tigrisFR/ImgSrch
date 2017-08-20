package fr.nabonne.tigris.imgsrch;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    MVPContracts.ISearchModel model;
    List<MVPContracts.ISearchModel.ImageData> imagesData = new ArrayList<>();
    ImagesDataAdapter adapter;
    SearchView searchView;
    @BindView(R.id.search_toolbar)
    Toolbar toolbar;

    @BindView(R.id.results_view)
    RecyclerView resultsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        model = new GoogleImageSearchParser();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        resultsView.setLayoutManager(layoutManager);

        adapter = new ImagesDataAdapter(imagesData);
        resultsView.setAdapter(adapter);

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
                            imagesData.clear();
                            imagesData.addAll(query.results);
                            adapter.notifyDataSetChanged();
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
