package com.tushar.rxjavaimpl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val observable = Observable.just(arrayOf("Tushar", "Anil", "Pingale"))
    private val observableArray = Observable.fromArray("Tushar", "Anil", "Pingale")
    private val observableCreate = Observable.create(ObservableOnSubscribe<String> { emitter ->
        emitter.onNext("From Create")
        emitter.onNext("Tushar")
        emitter.onNext("Anil")
        emitter.onNext("Pingale")
        emitter.onNext("From Create")
        emitter.onNext("on Complete")
    })
    private val observableRange = Observable.range(0, 20)

    companion object {
        const val TAG = "MainActivity"
    }

    private var disposableObserverJust: DisposableObserver<Array<String>>? = null
    private var disposableObserverArray: DisposableObserver<String>? = null
    private var disposableObserverRange: DisposableObserver<Int>? = null
    private var disposableObserverCreate: DisposableObserver<String>? = null
    private var disposableObserverFlatMap: DisposableObserver<String>? = null
    private var disposableObserverBuffer: DisposableObserver<List<Int>>? = null
    private var disposableObserverFilter: DisposableObserver<Int>? = null
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Just Observer
        disposableObserverJust = object : DisposableObserver<Array<String>>() {

            override fun onNext(value: Array<String>?) {
                value?.let {
                    Log.d(TAG, "onNext: $it")
                    it.forEach { name ->
                        //progressText.text = name
                    }

                }
            }

            override fun onError(e: Throwable?) {
                e?.let {
                    Log.d(TAG, "onError: $it")
                }
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: Completed")
            }

        }
        observable.subscribe(disposableObserverJust)
        compositeDisposable.add(disposableObserverJust)

        //From Array
        disposableObserverArray = object : DisposableObserver<String>() {

            override fun onNext(value: String?) {
                value?.let {
                    Log.d(TAG, "onNext : $it")
                }
            }

            override fun onError(e: Throwable?) {
                e?.let {
                    Log.d(TAG, "onError: $it")
                }
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: Completed")
            }

        }
        observableArray.subscribe(disposableObserverArray)
        compositeDisposable.add(disposableObserverArray)

        // Range
        disposableObserverRange = object : DisposableObserver<Int>() {

            override fun onNext(value: Int?) {
                value?.let {
                    Log.d(TAG, "onNext Range:: $it")
                }
            }

            override fun onError(e: Throwable?) {
                e?.let {
                    Log.d(TAG, "onError: $it")
                }
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: Completed")
            }

        }

        compositeDisposable.add(
                observableRange.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(disposableObserverRange)
        )

        // Create & Map
        disposableObserverCreate = object : DisposableObserver<String>() {

            override fun onNext(value: String?) {
                value?.let {
                    Log.d(TAG, "onNext Range:: $it")
                }
            }

            override fun onError(e: Throwable?) {
                e?.let {
                    Log.d(TAG, "onError: $it")
                }
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: Completed")
            }

        }

        compositeDisposable.add(
                observableCreate.map {
                    "$it Applied Map"
                }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(disposableObserverCreate)
        )

        // FlatMap
        disposableObserverFlatMap = object : DisposableObserver<String>() {

            override fun onNext(value: String?) {
                value?.let {
                    Log.d(TAG, "onNext FlatMap:: $it")
                }
            }

            override fun onError(e: Throwable?) {
                e?.let {
                    Log.d(TAG, "onError: $it")
                }
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: Completed")
            }

        }

        compositeDisposable.add(
                observableCreate.flatMap {
                    Observable.just("$it Applied Map", "1")
                }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(disposableObserverFlatMap)
        )

        //Buffer
        disposableObserverBuffer = object : DisposableObserver<List<Int>>() {
            override fun onNext(array: List<Int>?) {
                array?.let {
                    array.forEach {
                        Log.d(TAG, "onNext :: $it")
                    }
                    Log.d(TAG, "onNext Bundle Emitted!")
                }
            }


            override fun onError(e: Throwable?) {
                e?.let {
                    Log.d(TAG, "onError: $it")
                }
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: Completed")
            }

        }

        compositeDisposable.add(
                observableRange
                        .buffer(4)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(disposableObserverBuffer)
        )

        //Filter
        disposableObserverFilter = object : DisposableObserver<Int>() {
            override fun onNext(value: Int?) {
                value?.let {
                    Log.d(TAG, "onNext Filter Value: $it")
                }
            }


            override fun onError(e: Throwable?) {
                e?.let {
                    Log.d(TAG, "onError: $it")
                }
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: Completed")
            }

        }

        compositeDisposable.add(
                observableRange
                        .filter {
                            //Emits items which are divided by 3
                            it % 3 == 0
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(disposableObserverFilter)
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        //Composite disposable
        compositeDisposable.clear()
    }
}