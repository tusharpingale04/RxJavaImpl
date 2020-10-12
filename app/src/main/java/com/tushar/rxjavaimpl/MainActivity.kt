package com.tushar.rxjavaimpl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val observable = Observable.just("Tushar")
    companion object{
        const val TAG = "MainActivity"
    }
    //private lateinit var disposable: Disposable
    private var disposableObserver1: DisposableObserver<String> ?= null
    private var disposableObserver2: DisposableObserver<String> ?= null
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observable.subscribeOn(Schedulers.io())
        /*observable.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe: Subscribed")
                d?.let {
                    disposable = d
                }
            }

            override fun onNext(value: String?) {
                value?.let {
                    Log.d(TAG, "onNext: $it")
                    progressText.text = it
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

        })*/
        disposableObserver1 = object : DisposableObserver<String>() {

            override fun onNext(value: String?) {
                value?.let {
                    Log.d(TAG, "onNext: $it")
                    progressText.text = it
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
        observable.subscribe(disposableObserver1)
        compositeDisposable.add(disposableObserver1)

        disposableObserver2 = object : DisposableObserver<String>() {

            override fun onNext(value: String?) {
                value?.let {
                    Log.d(TAG, "onNext: $it")
                    progressText.text = it
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
        observable.subscribe(disposableObserver2)
        compositeDisposable.add(disposableObserver2)
    }

    override fun onDestroy() {
        super.onDestroy()
        /*if(this::disposable.isInitialized){
            disposable.dispose()
        }*/

        //Normal Disposable
        //disposableObserver?.dispose()

        //Composite disposable
        compositeDisposable.clear()
    }
}