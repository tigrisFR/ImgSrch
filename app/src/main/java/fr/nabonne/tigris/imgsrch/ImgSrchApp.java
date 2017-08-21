package fr.nabonne.tigris.imgsrch;

import android.app.Application;

/**
 * Created by tigris on 8/20/17.
 */

public class ImgSrchApp extends Application {
    MVPContracts.ISearchModel model;
    MVPContracts.ISearchPresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        model = new GoogleImageSearchParser();
        presenter = new SearchPresenter(model);
    }

    public MVPContracts.ISearchPresenter getPresenter() {
        return presenter;
    }
}
