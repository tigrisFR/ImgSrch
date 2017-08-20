package fr.nabonne.tigris.imgsrch;

import android.media.Image;

import java.util.List;

/**
 * Created by tigris on 8/20/17.
 */

public interface MVPContracts {
    interface ISearchModel {
        class ImageData {
            final String id;
            final String url;

            public ImageData(String id, String url) {
                this.id = id;
                this.url = url;
            }
        }

        class Query {
            final String queryString;
            String errorString;
            List<ImageData> results;

            public Query(String queryString) {
                this.queryString = queryString;
            }
        }

        interface QueryObserver {
            void onResults(Query query);
        }

        void query(Query query, QueryObserver observer);
    }

    interface ISearchPresenter {
        void onActionSearch(String queryString);
    }

    interface ISearchView{
        void showLoading();
        void showError(String errorString);
        void showResults(List<ISearchModel.ImageData> results);
    }
}
