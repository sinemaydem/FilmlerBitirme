package com.example.filmlerbitirme.di


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.filmlerbitirme.data.datasource.MoviesDataSource
import com.example.filmlerbitirme.data.repo.MovieDaoRepository
import com.example.filmlerbitirme.data.repo.OrdersRepository
import com.example.filmlerbitirme.retrofit.ApiUtils
import com.example.filmlerbitirme.retrofit.MoviesDaoInterface
import com.example.filmlerbitirme.ui.viewmodel.CartViewModel
import com.example.filmlerbitirme.ui.viewmodel.DetailViewModel
import com.example.filmlerbitirme.ui.viewmodel.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.internal.lifecycle.HiltViewModelMap
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.StringKey
import javax.inject.Singleton




@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMovieDaoRepository(moviesDataSource: MoviesDataSource): MovieDaoRepository {
        return MovieDaoRepository(moviesDataSource)
    }

    @Provides
    @Singleton
    fun provideMoviesDataSource(moviesDaoInterface: MoviesDaoInterface): MoviesDataSource {
        return MoviesDataSource(moviesDaoInterface)
    }

    @Provides
    @Singleton
    fun provideMoviesDaoInterface(): MoviesDaoInterface {
        return ApiUtils.getMoviesDaoInterface()
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    abstract class DetailViewModelModule {
        @Binds
        @IntoMap
        @StringKey("com.example.filmlerbitirme.ui.viewmodel.DetailViewModel")
        @HiltViewModelMap
        abstract fun bindDetailViewModel(viewModel: DetailViewModel): ViewModel
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    abstract class CartViewModelModule {
        @Binds
        @IntoMap
        @StringKey("com.example.filmlerbitirme.ui.viewmodel.CartViewModel")
        @HiltViewModelMap
        abstract fun bindCartViewModel(viewModel: CartViewModel): ViewModel
    }

    // Activity level module for DetailViewModel
    @Module
    @InstallIn(ActivityRetainedComponent::class)
    object DetailViewModelKeyModule {
        @Provides
        @IntoSet
        @HiltViewModelMap.KeySet
        fun provideDetailViewModelKey(): String {
            return "com.example.filmlerbitirme.ui.viewmodel.DetailViewModel"
        }
    }

    // Activity level module for CartViewModel
    @Module
    @InstallIn(ActivityRetainedComponent::class)
    object CartViewModelKeyModule {
        @Provides
        @IntoSet
        @HiltViewModelMap.KeySet
        fun provideCartViewModelKey(): String {
            return "com.example.filmlerbitirme.ui.viewmodel.CartViewModel"
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object RepositoryModule {
        @Provides
        @Singleton
        fun provideOrdersRepository(@ApplicationContext context: Context): OrdersRepository {
            return OrdersRepository(context)
        }
    }
}
