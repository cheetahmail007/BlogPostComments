package com.abhishekpathak.posts.list.di

import androidx.room.Room
import android.content.Context
import com.abhishekpathak.posts.commons.data.local.PostDb
import com.abhishekpathak.posts.commons.data.remote.PostService
import com.abhishekpathak.posts.core.constants.Constants
import com.abhishekpathak.posts.core.di.CoreComponent
import com.abhishekpathak.posts.core.networking.Scheduler
import com.abhishekpathak.posts.list.ListActivity
import com.abhishekpathak.posts.list.PostListAdapter
import com.abhishekpathak.posts.list.model.ListDataContract
import com.abhishekpathak.posts.list.model.ListLocalData
import com.abhishekpathak.posts.list.model.ListRemoteData
import com.abhishekpathak.posts.list.model.ListRepository
import com.abhishekpathak.posts.list.viewmodel.ListViewModelFactory
import com.squareup.picasso.Picasso
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit

@ListScope
@Component(dependencies = [CoreComponent::class], modules = [ListModule::class])
interface ListComponent {

    //Expose to dependent components
    fun postDb(): PostDb

    fun postService(): PostService
    fun picasso(): Picasso
    fun scheduler(): Scheduler

    fun inject(listActivity: ListActivity)
}

@Module
@ListScope
class ListModule {

    /*Adapter*/
    @Provides
    @ListScope
    fun adapter(picasso: Picasso): PostListAdapter = PostListAdapter(picasso)

    /*ViewModel*/
    @Provides
    @ListScope
    fun listViewModelFactory(repository: ListDataContract.Repository,compositeDisposable: CompositeDisposable): ListViewModelFactory = ListViewModelFactory(repository,compositeDisposable)

    /*Repository*/
    @Provides
    @ListScope
    fun listRepo(local: ListDataContract.Local, remote: ListDataContract.Remote, scheduler: Scheduler, compositeDisposable: CompositeDisposable): ListDataContract.Repository = ListRepository(local, remote, scheduler, compositeDisposable)

    @Provides
    @ListScope
    fun remoteData(postService: PostService): ListDataContract.Remote = ListRemoteData(postService)

    @Provides
    @ListScope
    fun localData(postDb: PostDb, scheduler: Scheduler): ListDataContract.Local = ListLocalData(postDb, scheduler)

    @Provides
    @ListScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()

    /*Parent providers to dependents*/
    @Provides
    @ListScope
    fun postDb(context: Context): PostDb = Room.databaseBuilder(context, PostDb::class.java, Constants.Posts.DB_NAME).build()

    @Provides
    @ListScope
    fun postService(retrofit: Retrofit): PostService = retrofit.create(PostService::class.java)
}