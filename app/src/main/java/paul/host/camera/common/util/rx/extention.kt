@file:Suppress("unused")

package paul.host.camera.common.util.rx

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//Single

fun <T> Single<T>.fromIoToMainThread(): Single<T> = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> T.toSingle(): Single<T> = Single.just(this)

//Flowable

fun <T> Flowable<T>.fromIoToMainThread(): Flowable<T> = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.newThread(): Flowable<T> = this
    .subscribeOn(Schedulers.newThread())

fun <T> T.toFlowable(): Flowable<T> = Flowable.just(this)

//Completable

fun Completable.fromIoToMainThread(): Completable = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun Completable.ioThread(): Completable = this
    .subscribeOn(Schedulers.io())

fun Completable.newThread(): Completable = this
    .subscribeOn(Schedulers.newThread())

fun Completable.computationThread(): Completable = this
    .subscribeOn(Schedulers.computation())
