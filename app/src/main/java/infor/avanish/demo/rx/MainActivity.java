package infor.avanish.demo.rx;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import infor.avanish.demo.R;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Disposable disposal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> animalStringObservable = getAnimalObservable();

        Observer<String> stringObserver = getStringObserver();

        animalStringObservable.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(s -> {
                    if (s.startsWith("d")){
                        return true;
                    }
                    return false;
                })
                .subscribe(stringObserver);


    }

    private Observer<String> getStringObserver() {
        return new Observer<String>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    Log.d(TAG, "onSubscribe");
                    disposal = d;
                }

                @Override
                public void onNext(@NonNull String s) {
                    Log.d(TAG, "Name: " + s);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Log.e(TAG, "onError: " + e.getMessage());
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "All items are emitted!");
                }
            };
    }


    private Observable<String> getAnimalObservable(){
        return Observable.just("dog","cat","hen","hourse");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposal.dispose();
    }
}