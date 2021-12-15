package infor.avanish.demo.rx;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import infor.avanish.demo.R;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExampleActivity2 extends AppCompatActivity {

    private static final String TAG = ExampleActivity2.class.getSimpleName();
    private Disposable disposal;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> animalStringObservable = getAnimalObservable();

        DisposableObserver<String> stringObserver = getStringObserver();
        DisposableObserver<String> stringObserverAllCaps = getStringAllCaps();

        compositeDisposable.add(animalStringObservable
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(s -> s.startsWith("d"))
                .subscribeWith(stringObserver));


        compositeDisposable.add(
                animalStringObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(s -> s.toLowerCase().startsWith("c"))
                        .map(s -> s.toUpperCase())
                        .subscribeWith(stringObserverAllCaps));




    }

    private DisposableObserver<String> getStringAllCaps() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<String> getStringObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }


    private Observable<String> getAnimalObservable(){
        return Observable.just("dog","cat","hen","hourse");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}