package pe.hankyu.svmgithubbrowser.view.main

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import pe.hankyu.svmgithubbrowser.repo.GithubApi
import pe.hankyu.svmgithubbrowser.repo.model.UserListModel

class MainPresenterImpl(val view: MainPresenter.View):
    MainPresenter {
    private val compositeDisposable = CompositeDisposable()
    override fun loadItem(since: Int) {
        compositeDisposable.add(
            GithubApi.getUserList(since.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ response: List<UserListModel> ->
                    view.addView(response)
                }, { error: Throwable ->
                    Log.d("MainActivity", error.localizedMessage)
                    view.makeToast("리스트의 끝이거나 다음을 읽어들일 수 없습니다.")
                }))
    }

    override fun loadItem() {
        compositeDisposable.add(
            GithubApi.getUserList("0")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ response: List<UserListModel> ->
                    view.updateView(response)
                }, { error: Throwable ->
                    Log.d("MainActivity", error.localizedMessage)
                    view.makeToast("네트워크 연결을 확인하세요.")
                }))
    }

    override fun destroy() {
        compositeDisposable.dispose()
    }
}