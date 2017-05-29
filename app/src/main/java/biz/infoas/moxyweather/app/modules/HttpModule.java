package biz.infoas.moxyweather.app.modules;

import javax.inject.Singleton;

import biz.infoas.moxyweather.BuildConfig;
import biz.infoas.moxyweather.app.api.WeatherAPI;
import biz.infoas.moxyweather.util.Const;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class HttpModule {
	@Provides
	@Singleton
	public OkHttpClient provideHttpLogging() {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
		return new OkHttpClient.Builder()
				.addInterceptor(logging)
				.build();
	}

	@Provides
	@Singleton
	public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
		return new Retrofit.Builder()
				.baseUrl(Const.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.client(okHttpClient)
				.build();
	}

	@Provides
	@Singleton
	public WeatherAPI provideApiService(Retrofit retrofit) {
		return retrofit.create(WeatherAPI.class);
	}
}
