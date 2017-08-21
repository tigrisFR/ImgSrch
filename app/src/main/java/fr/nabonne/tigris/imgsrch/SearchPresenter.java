package fr.nabonne.tigris.imgsrch;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tigris on 8/20/17.
 */

public class SearchPresenter implements MVPContracts.ISearchPresenter {
    MVPContracts.ISearchView searchView;
    MVPContracts.ISearchModel model;

    List<MVPContracts.ISearchModel.ImageData> imagesData = new ArrayList<>();
    private MVPContracts.ISearchModel.Query curQuery;

    private Context context;// FOR DEBUGGING PURPOSES ONLY

    private static enum State {
        INIT,
        ERROR,
        LOADING,
        RESULTS,
    }
    private State curState = State.INIT;

    public SearchPresenter(MVPContracts.ISearchModel model){
        this.model = model;
    }

    @Override
    public void registerView(MVPContracts.ISearchView searchView) {
        this.searchView = searchView;
        this.context = ((SearchActivity) searchView).getApplicationContext();
        emitState();
    }

    @Override
    public void unregisterView(MVPContracts.ISearchView searchView) {
        if (this.searchView == searchView) {
            this.searchView = null;
        }
        // else new activity has registered before previous unregistered
    }

    @Override
    public MVPContracts.ISearchModel.Query getCurQuery() {
        return curQuery;
    }

    @Override
    public List<MVPContracts.ISearchModel.ImageData> getImagesData() {
        return imagesData;
    }

    @Override
    public void onActionSearch(final String queryString) {
        changeState(State.LOADING);

        curQuery = new MVPContracts.ISearchModel.Query(queryString);
        model.query(curQuery,
                new MVPContracts.ISearchModel.QueryObserver() {
                    @Override
                    public void onResults(MVPContracts.ISearchModel.Query query) {
                        //update state and emit it
                        String toastString;
                        if (query.results != null) {
                            toastString = query.results.size() + " results";
                            imagesData = new ArrayList<>();
                            imagesData.addAll(query.results);
                            changeState(State.RESULTS);
                        }
                        else {
                            toastString = query.errorString;
                            if (toastString == null)
                                toastString = "unknown error";
                            changeState(State.ERROR);
                        }
                        emitState();
                        Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean changeState(State newState) {
        final State prevState = curState;
        curState = newState;

        if (prevState == curState) {
            if (prevState == State.LOADING) {
                String toastString = "Previous Search Canceled";
                Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    private void emitState() {
        switch (curState) {
            case LOADING:
                if(searchView != null)
                    searchView.showLoading();
                break;
            case RESULTS:
                if(searchView != null)
                    searchView.showResults();
                break;
            case ERROR:
                if(searchView != null)
                    searchView.showError();
                break;
        }
    }
}
