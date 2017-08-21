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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        MVPContracts.ISearchView{

    MVPContracts.ISearchModel model;
    MVPContracts.ISearchPresenter presenter;
    ImagesDataAdapter adapter;
    SearchView searchView;
    @BindView(R.id.search_toolbar)
    Toolbar toolbar;

    @BindView(R.id.results_view)
    RecyclerView resultsView;

    @BindView(R.id.idle_text_view)
    TextView textView;

    @BindView(R.id.loading_animation_view)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        resultsView.setLayoutManager(layoutManager);
        adapter = new ImagesDataAdapter();
        resultsView.setAdapter(adapter);

        //Now get the presenter from app and register
        ImgSrchApp app = (ImgSrchApp) getApplication();
        presenter = app.getPresenter();
        presenter.registerView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unregisterView(this);
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
        presenter.onActionSearch(queryString);
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        resultsView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        //fetch state from presenter
        textView.setText(presenter.getCurQuery().errorString);

        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        resultsView.setVisibility(View.GONE);
    }

    @Override
    public void showResults() {
        //fetch state from presenter
        adapter.setData(presenter.getImagesData());
        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        resultsView.setVisibility(View.VISIBLE);
    }
}
